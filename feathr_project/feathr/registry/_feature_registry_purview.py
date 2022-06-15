import glob
import importlib
import inspect
import itertools
import os
import sys
import ast
import types
from graphlib import TopologicalSorter
from pathlib import Path
from tracemalloc import stop
from typing import Dict, List, Optional, Tuple, Union
from urllib.parse import urlparse
from time import sleep

from azure.identity import DefaultAzureCredential
from jinja2 import Template
from loguru import logger
from numpy import deprecate
from pyapacheatlas.auth import ServicePrincipalAuthentication
from pyapacheatlas.auth.azcredential import AzCredentialWrapper
from pyapacheatlas.core import (AtlasClassification, AtlasEntity, AtlasProcess,
                                PurviewClient, TypeCategory)
from pyapacheatlas.core.typedef import (AtlasAttributeDef,
                                        AtlasRelationshipEndDef, Cardinality,
                                        EntityTypeDef, RelationshipTypeDef)
                            
from pyapacheatlas.core.util import GuidTracker
from pyhocon import ConfigFactory
from collections import defaultdict

from feathr.definition.dtype import *
from feathr.utils._file_utils import write_to_file
from feathr.definition.anchor import FeatureAnchor
from feathr.constants import *
from feathr.definition.feature import Feature, FeatureType,FeatureBase
from feathr.definition.feature_derivations import DerivedFeature
from feathr.definition.repo_definitions import RepoDefinitions
from feathr.definition.source import HdfsSource, InputContext, Source
from feathr.definition.transformation import (ExpressionTransformation, Transformation,
                                   WindowAggTransformation)
from feathr.definition.typed_key import TypedKey
from feathr.registry.feature_registry import FeathrRegistry

class _FeatureRegistry(FeathrRegistry):
    """
    Initializes the feature registry, doing the following:
    - Use an DefaultAzureCredential() to communicate with Azure Purview
    - Initialize an Azure Purview Client
    - Initialize the GUID tracker, project name, etc.
    """
    def __init__(self, project_name: str, azure_purview_name: str, registry_delimiter: str, project_tags: Dict[str, str] = None, credential=None, config_path=None,):
        self.project_name = project_name
        self.registry_delimiter = registry_delimiter
        self.azure_purview_name = azure_purview_name
        self.project_tags = project_tags

        self.credential = DefaultAzureCredential(exclude_interactive_browser_credential=False) if credential is None else credential
        self.oauth = AzCredentialWrapper(credential=self.credential)
        self.purview_client = PurviewClient(
                account_name=self.azure_purview_name,
                authentication=self.oauth
            )   
        self.guid = GuidTracker(starting=-1000)
        self.entity_batch_queue = []

        # for searching in derived features by name
        self.global_feature_entity_dict = {}

    def _register_feathr_feature_types(self):
        """
        Register the feathr types if we haven't done so. Note that this only needs to be called once per provisioning
        a system. Basically this function registers all the feature type definition in a Atlas compatible system.
        """

        # Each feature is registered under a certain Feathr project. The project should what we refer to, however for backward compatibility, the type name would be `feathr_workspace`
        type_feathr_project = EntityTypeDef(
            name=TYPEDEF_FEATHR_PROJECT,
            attributeDefs=[
                # TODO: this should be called "anchors" rather than "anchor_features" to make it less confusing.
                AtlasAttributeDef(
                    name="anchor_features", typeName=TYPEDEF_ARRAY_ANCHOR, cardinality=Cardinality.SET),
                AtlasAttributeDef(
                    name="derived_features", typeName=TYPEDEF_ARRAY_DERIVED_FEATURE, cardinality=Cardinality.SET),
                AtlasAttributeDef(name="tags", typeName="map<string,string>",
                                  cardinality=Cardinality.SINGLE),
            ],
            superTypes=["DataSet"],

        )
        type_feathr_sources = EntityTypeDef(
            name=TYPEDEF_SOURCE,
            attributeDefs=[

                AtlasAttributeDef(
                    name="path", typeName="string", cardinality=Cardinality.SINGLE),
                AtlasAttributeDef(name="event_timestamp_column",
                                  typeName="string", cardinality=Cardinality.SINGLE),
                AtlasAttributeDef(name="timestamp_format",
                                  typeName="string", cardinality=Cardinality.SINGLE),
                AtlasAttributeDef(name="type", typeName="string",
                                  cardinality=Cardinality.SINGLE),
                AtlasAttributeDef(name="preprocessing", typeName="string",
                                  cardinality=Cardinality.SINGLE),
                AtlasAttributeDef(name="tags", typeName="map<string,string>",
                                  cardinality=Cardinality.SINGLE),
            ],
            superTypes=["DataSet"],
        )

        type_feathr_anchor_features = EntityTypeDef(
            name=TYPEDEF_ANCHOR_FEATURE,
            attributeDefs=[
                AtlasAttributeDef(name="type", typeName="string",
                                  cardinality=Cardinality.SINGLE),
                AtlasAttributeDef(name="key", typeName="array<map<string,string>>",
                                  cardinality=Cardinality.SET),
                AtlasAttributeDef(name="transformation", typeName="map<string,string>",
                                  cardinality=Cardinality.SINGLE),
                AtlasAttributeDef(name="tags", typeName="map<string,string>",
                                  cardinality=Cardinality.SINGLE),
            ],
            superTypes=["DataSet"],
        )

        type_feathr_derived_features = EntityTypeDef(
            name=TYPEDEF_DERIVED_FEATURE,
            attributeDefs=[
                AtlasAttributeDef(name="type", typeName="string",
                                  cardinality=Cardinality.SINGLE),

                AtlasAttributeDef(name="input_anchor_features", typeName=TYPEDEF_ARRAY_ANCHOR_FEATURE,
                                  cardinality=Cardinality.SET),
                AtlasAttributeDef(name="input_derived_features", typeName=TYPEDEF_ARRAY_DERIVED_FEATURE,
                                  cardinality=Cardinality.SET),
                AtlasAttributeDef(name="key", typeName="array<map<string,string>>",
                                  cardinality=Cardinality.SET),
                AtlasAttributeDef(name="transformation", typeName="map<string,string>",
                                  cardinality=Cardinality.SINGLE),
                AtlasAttributeDef(name="tags", typeName="map<string,string>",
                                  cardinality=Cardinality.SINGLE),
            ],
            superTypes=["DataSet"],
        )

        type_feathr_anchors = EntityTypeDef(
            name=TYPEDEF_ANCHOR,
            attributeDefs=[
                AtlasAttributeDef(
                    name="source", typeName=TYPEDEF_SOURCE, cardinality=Cardinality.SINGLE),
                AtlasAttributeDef(
                    name="features", typeName=TYPEDEF_ARRAY_ANCHOR_FEATURE, cardinality=Cardinality.SET),
                AtlasAttributeDef(name="tags", typeName="map<string,string>",
                                  cardinality=Cardinality.SINGLE),
            ],
            superTypes=["DataSet"],
        )

        def_result = self.purview_client.upload_typedefs(
            entityDefs=[type_feathr_anchor_features, type_feathr_anchors,
                        type_feathr_derived_features, type_feathr_sources, type_feathr_project],
            force_update=True)
        logger.info("Feathr Feature Type System Initialized.")

    def _parse_anchor_features(self, anchor: FeatureAnchor) -> List[AtlasEntity]:
        """
        This function will parse the anchor features and sources inside an anchor
        """

        anchor_feature_batch = []
        # annotate type
        anchor_feature: Feature

        for anchor_feature in anchor.features:
            key_list = []
            for individual_key in anchor_feature.key:
                key_dict = {"key_column": individual_key.key_column, "key_column_type": individual_key.key_column_type.value,
                            "full_name": individual_key.full_name, "description": individual_key.description, "key_column_alias": individual_key.key_column_alias}
                key_list.append(key_dict)

            # define a dict to save all the transformation schema
            transform_dict = {}
            if isinstance(anchor_feature.transform, ExpressionTransformation):
                transform_dict = {"transform_expr": anchor_feature.transform.expr}
            elif isinstance(anchor_feature.transform, WindowAggTransformation):
                transform_dict = {
                    "def_expr": anchor_feature.transform.def_expr,
                    "agg_func": anchor_feature.transform.agg_func,
                    "window": anchor_feature.transform.window,
                    "group_by": anchor_feature.transform.group_by,
                    "filter": anchor_feature.transform.filter,
                    "limit": anchor_feature.transform.limit,
                }

            anchor_feature_entity = AtlasEntity(
                name=anchor_feature.name,
                qualified_name=self.project_name + self.registry_delimiter +
                anchor.name + self.registry_delimiter + anchor_feature.name,
                attributes={
                    "type": anchor_feature.feature_type.to_feature_config(),
                    "key": key_list,
                    "transformation": transform_dict,
                    "tags": anchor_feature.registry_tags,
                },
                typeName=TYPEDEF_ANCHOR_FEATURE,
                guid=self.guid.get_guid(),
            )
            self.entity_batch_queue.append(anchor_feature_entity)
            # add the entity to a dict that can search by name
            self.global_feature_entity_dict[anchor_feature.name] = anchor_feature_entity
            anchor_feature_batch.append(anchor_feature_entity)
        return anchor_feature_batch

    def _parse_anchors(self, anchor_list: List[FeatureAnchor]) -> List[AtlasEntity]:
        """
        parse content of an anchor
        """
        anchors_batch = []
        for anchor in anchor_list:
            # First, parse all the features in this anchor
            anchor_feature_entities = self._parse_anchor_features(anchor)
            # then parse the source of that anchor
            source_entity = self._parse_source(anchor.source)
            anchor_fully_qualified_name  = self.project_name+self.registry_delimiter+anchor.name
            original_id = self.get_feature_id(anchor_fully_qualified_name, type=TYPEDEF_ANCHOR )
            original_anchor = self.get_feature_by_guid(original_id) if original_id else None
            merged_elements = self._merge_anchor(original_anchor,anchor_feature_entities)
            anchor_entity = AtlasEntity(
                name=anchor.name,
                qualified_name=anchor_fully_qualified_name ,
                attributes={
                    "source": source_entity.to_json(minimum=True),
                    "features": merged_elements,
                    "tags": anchor.registry_tags
                },
                typeName=TYPEDEF_ANCHOR,
                guid=self.guid.get_guid(),
            )
            # add feature lineage between anchor and feature
            for anchor_feature_entity in anchor_feature_entities:
                lineage = AtlasProcess(
                    name=anchor_feature_entity.name + " to " + anchor.name,
                    typeName="Process",
                    qualified_name=self.registry_delimiter + "PROCESS" + self.registry_delimiter + self.project_name +
                    self.registry_delimiter + anchor.name + self.registry_delimiter +
                    anchor_feature_entity.name,
                    inputs=[anchor_feature_entity],
                    outputs=[anchor_entity],
                    guid=self.guid.get_guid(),
                )
                self.entity_batch_queue.append(lineage)

            # add lineage between anchor and source
            anchor_source_lineage = AtlasProcess(
                name=source_entity.name + " to " + anchor.name,
                typeName="Process",
                qualified_name=self.registry_delimiter + "PROCESS" + self.registry_delimiter + self.project_name +
                self.registry_delimiter + anchor.name + self.registry_delimiter +
                source_entity.name,
                inputs=[source_entity],
                outputs=[anchor_entity],
                guid=self.guid.get_guid(),
            )
            self.entity_batch_queue.append(anchor_source_lineage)

            anchors_batch.append(anchor_entity)
        return anchors_batch

    def _merge_anchor(self,original_anchor:Dict, new_anchor:Dict)->List[Dict[str,any]]:
        '''
        Merge the new anchors defined locally with the anchors that is defined in the centralized registry.
        '''
        # TODO: This will serve as a quick fix, full fix will work with MVCC, and is in progress.
        new_anchor_json_repr = [s.to_json(minimum=True) for s in new_anchor]
        if not original_anchor:
            # if the anchor is not present on the registry, return json representation of the locally defined anchor.
            # sample : [{'guid':'GUID_OF_ANCHOR','typeName':'','qualifiedName':'QUALIFIED_NAME'}
            return new_anchor_json_repr
        else:
            original_anchor_elements = [x for x in original_anchor['entity']['attributes']['features']]
            transformed_original_elements = {
                x['uniqueAttributes']['qualifiedName']:
                {
                    'guid':x['guid'],
                    'typeName':x['typeName'],
                    'qualifiedName':x['uniqueAttributes']['qualifiedName']
                }
                for x in original_anchor_elements}
            for elem in new_anchor_json_repr:
                transformed_original_elements.setdefault(elem['qualifiedName'],elem)
            return list(transformed_original_elements.values())
            
    def _parse_source(self, source: Union[Source, HdfsSource]) -> AtlasEntity:
        """
        parse the input sources
        """
        input_context = False
        if isinstance(source, InputContext):
            input_context = True
        
        # only set preprocessing if it's available in the object and is not None
        if 'preprocessing' in dir(source) and source.preprocessing is not None:
            preprocessing_func = inspect.getsource(source.preprocessing)
        else:
            preprocessing_func = None

        source_entity = AtlasEntity(
            name=source.name,
            qualified_name=self.project_name + self.registry_delimiter + source.name,
            attributes={
                "type": INPUT_CONTEXT if input_context else urlparse(source.path).scheme,
                "path": INPUT_CONTEXT if input_context else source.path,
                "timestamp_format": source.timestamp_format,
                "event_timestamp_column": source.event_timestamp_column,
                "tags": source.registry_tags,
                "preprocessing": preprocessing_func  # store the UDF as a string
            },
            typeName=TYPEDEF_SOURCE,
            guid=self.guid.get_guid(),
        )
        self.entity_batch_queue.append(source_entity)
        return source_entity

    def _add_all_derived_features(self, derived_features: List[DerivedFeature], ts:TopologicalSorter ) -> None:
        """iterate thru all the dependencies of the derived feature and return a derived feature list in a topological sorted way (the result list only has derived features, without their anchor features)

        Args:
            derived_features (List[DerivedFeature]): input derive feature list
            ts (TopologicalSorter): a topological sorter by python

        Returns:
            None. The topo sorter will maitain a static topo sorted order.
        """
        # return if the list is empty
        if derived_features is None:
            return

        for derived_feature in derived_features:
            # make sure the input is derived feature
            if isinstance(derived_feature, DerivedFeature):
                # add this derived feature in the topo sort graph without any precessesors
                # since regardless we need it
                ts.add(derived_feature)
                for input_feature in derived_feature.input_features:
                    if isinstance(input_feature, DerivedFeature):
                        # `input_feature` is predecessor of `derived_feature`
                        ts.add(derived_feature, input_feature)
                        # if any of the input feature is a derived feature, have this recursive call
                        # use this for code simplicity. 
                        # if the amount of features is huge, consider only add the derived features into the function call
                        self._add_all_derived_features(input_feature.input_features, ts)

                        


    def _parse_derived_features(self, derived_features: List[DerivedFeature]) -> List[AtlasEntity]:
        """parse derived feature

        Args:
            derived_features (List[DerivedFeature]): derived feature in a list fashion. This function will handle the derived feature dependencies using topological sort to ensure that the corresponding features can be parsed correctly

        Returns:
            List[AtlasEntity]: list of parsed Atlas entities for each of the derived feature
        """
        derivation_entities = []
        ts = TopologicalSorter()

        self._add_all_derived_features(derived_features, ts)
        # topo sort the derived features to make sure that we can correctly refer to them later in the registry
        toposorted_derived_feature_list: List[DerivedFeature] = list(ts.static_order())
        
        for derived_feature in toposorted_derived_feature_list:
            # get the corresponding Atlas entity by searching feature name
            # Since this list is topo sorted, so you can always find the corresponding name
            input_feature_entity_list: List[AtlasEntity] = [
                self.global_feature_entity_dict[f.name] for f in derived_feature.input_features]
            key_list = []
            for individual_key in derived_feature.key:
                key_dict = {"key_column": individual_key.key_column, "key_column_type": individual_key.key_column_type.value,
                            "full_name": individual_key.full_name, "description": individual_key.description, "key_column_alias": individual_key.key_column_alias}
                key_list.append(key_dict)

            # define a dict to save all the transformation schema
            transform_dict = {}
            if isinstance(derived_feature.transform, ExpressionTransformation):
                transform_dict = {"transform_expr": derived_feature.transform.expr}
            elif isinstance(derived_feature.transform, WindowAggTransformation):
                transform_dict = {
                    "def_expr": derived_feature.transform.def_expr,
                    "agg_func": derived_feature.transform.agg_func,
                    "window": derived_feature.transform.window,
                    "group_by": derived_feature.transform.group_by,
                    "filter": derived_feature.transform.filter,
                    "limit": derived_feature.transform.limit,
                }
            
            derived_feature_entity = AtlasEntity(
                name=derived_feature.name,
                qualified_name=self.project_name +
                self.registry_delimiter + derived_feature.name,
                attributes={
                    "type": derived_feature.feature_type.to_feature_config(),
                    "key": key_list,
                    "input_anchor_features": [f.to_json(minimum=True) for f in input_feature_entity_list if f.typeName==TYPEDEF_ANCHOR_FEATURE],
                    "input_derived_features": [f.to_json(minimum=True) for f in input_feature_entity_list if f.typeName==TYPEDEF_DERIVED_FEATURE],
                    "transformation": transform_dict,
                    "tags": derived_feature.registry_tags,

                },
                typeName=TYPEDEF_DERIVED_FEATURE,
                guid=self.guid.get_guid(),
            )

            # Add the feature entity in the global dict so that it can be referenced further. 
            self.global_feature_entity_dict[derived_feature.name] = derived_feature_entity

            for input_feature_entity in input_feature_entity_list:
                # add lineage between anchor feature and derived feature
                derived_feature_feature_lineage = AtlasProcess(
                    name=input_feature_entity.name + " to " + derived_feature.name,
                    typeName="Process",
                    qualified_name=self.registry_delimiter + "PROCESS" + self.registry_delimiter + self.project_name +
                    self.registry_delimiter + derived_feature.name + self.registry_delimiter +
                    input_feature_entity.name,
                    inputs=[input_feature_entity],
                    outputs=[derived_feature_entity],
                    guid=self.guid.get_guid(),
                )
                self.entity_batch_queue.append(derived_feature_feature_lineage)

            self.entity_batch_queue.append(derived_feature_entity)
            derivation_entities.append(derived_feature_entity)
        return derivation_entities

    def _parse_features_from_context(self, workspace_path: str, anchor_list, derived_feature_list):
        """
        Read feature content from python objects (which is provided in the context)
        """
        # define it here to make sure the variable is accessible
        anchor_entities = derived_feature_entities = []

        # parse all the anchors
        if anchor_list:
            anchor_entities = self._parse_anchors(anchor_list)

        project_attributes = {"anchor_features": [
            s.to_json(minimum=True) for s in anchor_entities], "tags": self.project_tags}
        # add derived feature if it's there
        if derived_feature_list:
            derived_feature_entities = self._parse_derived_features(
                derived_feature_list)
            project_attributes["derived_features"] = [
                s.to_json(minimum=True) for s in derived_feature_entities]

        # define project in Atlas entity
        feathr_project_entity = AtlasEntity(
            name=self.project_name,
            qualified_name=self.project_name,
            attributes=project_attributes,
            typeName=TYPEDEF_FEATHR_PROJECT,
            guid=self.guid.get_guid(),
        )

        # add lineage from anchor to project
        for individual_anchor_entity in anchor_entities:

            lineage_process = AtlasProcess(
                name=individual_anchor_entity.name + " to " + self.project_name,
                typeName="Process",
                # fqdn: PROCESS+PROJECT_NAME+ANCHOR_NAME
                qualified_name=self.registry_delimiter + "PROCESS" + self.registry_delimiter + self.project_name +
                self.registry_delimiter + individual_anchor_entity.name,
                inputs=[individual_anchor_entity],
                outputs=[feathr_project_entity],
                guid=self.guid.get_guid(),
            )
            self.entity_batch_queue.append(lineage_process)

        # add lineage from derivation to project
        for derived_feature_entity in derived_feature_entities:
            lineage_process = AtlasProcess(
                name=derived_feature_entity.name + " to " + self.project_name,
                typeName="Process",
                # fqdn: PROCESS+PROJECT_NAME+DERIVATION_NAME
                qualified_name=self.registry_delimiter + "PROCESS" + self.registry_delimiter + self.project_name +
                self.registry_delimiter + derived_feature_entity.name,
                inputs=[derived_feature_entity],
                outputs=[feathr_project_entity],
                guid=self.guid.get_guid(),
            )
            self.entity_batch_queue.append(lineage_process)
            
        self.entity_batch_queue.append(feathr_project_entity)
        self.entity_batch_queue.extend(anchor_entities)
        self.entity_batch_queue.extend(derived_feature_entities)

    @classmethod
    def _get_py_files(self, path: Path) -> List[Path]:
        """Get all Python files under path recursively, excluding __init__.py"""
        py_files = []
        for item in path.glob('**/*.py'):
            if "__init__.py" != item.name:
                py_files.append(item)
        return py_files

    @classmethod
    def _convert_to_module_path(self, path: Path, workspace_path: Path) -> str:
        """Convert a Python file path to its module path so that we can import it later"""
        prefix = os.path.commonprefix(
            [path.resolve(), workspace_path.resolve()])
        resolved_path = str(path.resolve())
        module_path = resolved_path[len(prefix): -len(".py")]
        # Convert features under nested folder to module name
        # e.g. /path/to/pyfile will become path.to.pyfile
        return (
            module_path
            .lstrip('/')
            .replace("/", ".")
        )

    @classmethod
    def _extract_features_from_context(self, anchor_list, derived_feature_list, result_path: Path) -> RepoDefinitions:
        """Collect feature definitions from the context instead of python files"""
        definitions = RepoDefinitions(
            sources=set(),
            features=set(),
            transformations=set(),
            feature_anchors=set(),
            derived_features=set()
        )
        for derived_feature in derived_feature_list:
            if isinstance(derived_feature, DerivedFeature):
                definitions.derived_features.add(derived_feature)
                definitions.transformations.add(
                    vars(derived_feature)["transform"])
            else:
                raise RuntimeError(
                    "Object cannot be parsed. `derived_feature_list` should be a list of `DerivedFeature`.")

        for anchor in anchor_list:
            # obj is `FeatureAnchor`
            definitions.feature_anchors.add(anchor)
            # add the source section of this `FeatureAnchor` object
            definitions.sources.add(vars(anchor)['source'])
            for feature in vars(anchor)['features']:
                # get the transformation object from `Feature` or `DerivedFeature`
                if isinstance(feature, Feature):
                    # feature is of type `Feature`
                    definitions.features.add(feature)
                    definitions.transformations.add(vars(feature)["transform"])
                else:
                    raise RuntimeError("Object cannot be parsed.")

        return definitions

    @classmethod
    def _extract_features(self, workspace_path: Path) -> RepoDefinitions:
        """Collect feature definitions from the python file, convert them into feature config and save them locally"""
        os.chdir(workspace_path)
        # Add workspace path to system path so that we can load features defined in Python via import_module
        sys.path.append(str(workspace_path))
        definitions = RepoDefinitions(
            sources=set(),
            features=set(),
            transformations=set(),
            feature_anchors=set(),
            derived_features=set()
        )
        for py_file in self._get_py_files(workspace_path):
            module_path = self._convert_to_module_path(py_file, workspace_path)
            module = importlib.import_module(module_path)
            for attr_name in dir(module):
                obj = getattr(module, attr_name)
                if isinstance(obj, Source):
                    definitions.sources.add(obj)
                elif isinstance(obj, Feature):
                    definitions.features.add(obj)
                elif isinstance(obj, DerivedFeature):
                    definitions.derived_features.add(obj)
                elif isinstance(obj, FeatureAnchor):
                    definitions.feature_anchors.add(obj)
                elif isinstance(obj, Transformation):
                    definitions.transformations.add(obj)
        return definitions

    @classmethod
    def save_to_feature_config(self, workspace_path: Path, config_save_dir: Path):
        """Save feature definition within the workspace into HOCON feature config files"""
        repo_definitions = self._extract_features(workspace_path)
        self._save_request_feature_config(repo_definitions, config_save_dir)
        self._save_anchored_feature_config(repo_definitions, config_save_dir)
        self._save_derived_feature_config(repo_definitions, config_save_dir)

    @classmethod
    def save_to_feature_config_from_context(self, anchor_list, derived_feature_list, local_workspace_dir: Path):
        """Save feature definition within the workspace into HOCON feature config files from current context, rather than reading from python files"""
        repo_definitions = self._extract_features_from_context(
            anchor_list, derived_feature_list, local_workspace_dir)
        self._save_request_feature_config(repo_definitions, local_workspace_dir)
        self._save_anchored_feature_config(repo_definitions, local_workspace_dir)
        self._save_derived_feature_config(repo_definitions, local_workspace_dir)

    @classmethod
    def _save_request_feature_config(self, repo_definitions: RepoDefinitions, local_workspace_dir="./"):
        config_file_name = "feature_conf/auto_generated_request_features.conf"
        tm = Template(
            """
// THIS FILE IS AUTO GENERATED. PLEASE DO NOT EDIT.
anchors: {
    {% for anchor in feature_anchors %}
        {% if anchor.source.name == "PASSTHROUGH" %}
            {{anchor.to_feature_config()}}
        {% endif %}
    {% endfor %}
}
"""
        )

        request_feature_configs = tm.render(
            feature_anchors=repo_definitions.feature_anchors)
        config_file_path = os.path.join(local_workspace_dir, config_file_name)
        write_to_file(content=request_feature_configs,
                      full_file_name=config_file_path)

    @classmethod
    def _save_anchored_feature_config(self, repo_definitions: RepoDefinitions, local_workspace_dir="./"):
        config_file_name = "feature_conf/auto_generated_anchored_features.conf"
        tm = Template(
            """
// THIS FILE IS AUTO GENERATED. PLEASE DO NOT EDIT.
anchors: {
    {% for anchor in feature_anchors %}
        {% if not anchor.source.name == "PASSTHROUGH" %}
            {{anchor.to_feature_config()}}
        {% endif %}
    {% endfor %}
}

sources: {
    {% for source in sources%}
        {% if not source.name == "PASSTHROUGH" %}
            {{source.to_feature_config()}}
        {% endif %}
    {% endfor %}
}
"""
        )
        anchored_feature_configs = tm.render(feature_anchors=repo_definitions.feature_anchors,
                                             sources=repo_definitions.sources)
        config_file_path = os.path.join(local_workspace_dir, config_file_name)
        write_to_file(content=anchored_feature_configs,
                      full_file_name=config_file_path)

    @classmethod
    def _save_derived_feature_config(self, repo_definitions: RepoDefinitions, local_workspace_dir="./"):
        config_file_name = "feature_conf/auto_generated_derived_features.conf"
        tm = Template(
            """
anchors: {}
derivations: {
    {% for derived_feature in derived_features %}
        {{derived_feature.to_feature_config()}}
    {% endfor %}
}
"""
        )
        derived_feature_configs = tm.render(
            derived_features=repo_definitions.derived_features)
        config_file_path = os.path.join(local_workspace_dir, config_file_name)
        write_to_file(content=derived_feature_configs,
                      full_file_name=config_file_path)

    def register_features(self, workspace_path: Optional[Path] = None, from_context: bool = True, anchor_list=[], derived_feature_list=[]):
        """Register Features for the specified workspace. 

        Args:
            workspace_path (str, optional): path to a workspace. Defaults to None.
            from_context: whether the feature is from context (i.e. end users has to callFeathrClient.build_features()) or the feature is from a pre-built config file. Currently Feathr only supports register features from context.
            anchor_list: The anchor list after feature build
            derived_feature_list: the derived feature list after feature build
        """

        if not from_context:
            raise RuntimeError("Currently Feathr only supports registering features from context (i.e. you must call FeathrClient.build_features() before calling this function).")

        # register feature types each time when we register features.
        self._register_feathr_feature_types()
        self._parse_features_from_context(
            workspace_path, anchor_list, derived_feature_list)
        # Upload all entities
        # need to be all in one batch to be uploaded, otherwise the GUID reference won't work
        results = self.purview_client.upload_entities(
            batch=self.entity_batch_queue)
        if results:
            webinterface_path = "https://web.purview.azure.com/resource/" + self.azure_purview_name + \
                                "/main/catalog/browseassettypes"
        else:
            raise RuntimeError("Feature registration failed.", results)

        logger.info(
            "Finished registering features. See {} to access the Purview web interface", webinterface_path)


    def _purge_feathr_registry(self):
        """
        Delete all the feathr related entities and type definitions in feathr registry. For internal use only
        """
        self._delete_all_feathr_entities()
        self._delete_all_feathr_types()


    def _delete_all_feathr_types(self):
        """
        Delete all the corresonding type definitions for feathr registry. For internal use only
        """
        typedefs = self.purview_client.get_all_typedefs()

        relationshipdef_list=[]
        for relationshipdef in typedefs['relationshipDefs']:
            if "feathr" in relationshipdef['name']:
                relationshipdef_list.append(relationshipdef)
        self.purview_client.delete_typedefs(relationshipDefs=relationshipdef_list)
        

        entitydef_list=[]
        for typedef in typedefs['entityDefs']:
            if "feathr" in typedef['name']:
                entitydef_list.append(typedef )
        self.purview_client.delete_typedefs(entityDefs=entitydef_list)

        logger.info("Deleted all the Feathr related definitions.")

    def _delete_all_feathr_entities(self):
        """
        Delete all the corresonding entity for feathr registry. For internal use only

        :param guid: The guid or guids you want to remove.
        """
        # should not be large than this, otherwise the backend might throw out error
        batch_delte_size = 100

        # use the `query` API so that it can return immediatelly (don't use the search_entity API as it will try to return all the results in a single request)

        while True:
            result = self.purview_client.discovery.query(
                "feathr", limit=batch_delte_size)
            logger.info("Total number of entities:",result['@search.count'] )

            # if no results, break:
            if result['@search.count']  == 0:
                break
            entities = result['value']
            guid_list = [entity["id"] for entity in entities]
            self.purview_client.delete_entity(guid=guid_list)
            logger.info("{} feathr entities deleted", batch_delte_size)
            # sleep here, otherwise backend might throttle
            # process the next batch after sleep
            sleep(1)
    
    @classmethod
    def _get_registry_client(self):
        """
        Return a client object and users can operate more on it (like doing search)
        """
        return self.purview_client

    def list_registered_features(self, project_name: str, limit=1000, starting_offset=0) -> List[Dict[str,str]]:
        """
        List all the already registered features in a certain project.
        """

        feature_list = []

        if not project_name:
            raise RuntimeError("project_name must be specified.")

        # get the ID of the project name
        res = self.purview_client.get_entity(qualifiedName=project_name, typeName=TYPEDEF_FEATHR_PROJECT)

        # error handling
        if len(res) == 0:
            logger.warning(f"project {project_name} is not found.")
            return []
        # there should be only one entity
        project_guid = res['entities'][0]['guid']  
        # return the project lineage that we can use
        # maintain almost the same parameter with the Purview portal. Can be changed later.
        result = self.purview_client.get_entity_lineage(guid=project_guid, depth=6, width=100, direction="BOTH", includeParent=True,getDerivedLineage=False)
        
        # result['guidEntityMap'] should be a dict and we only care about the values
        for entity in result['guidEntityMap'].values():
            if entity['typeName'] in {TYPEDEF_ANCHOR_FEATURE, TYPEDEF_DERIVED_FEATURE}:
                feature_list.append({"name":entity['attributes']["name"],'id':entity['guid'],"qualifiedName":entity['attributes']['qualifiedName']}) 

        return feature_list
   
    def get_feature_by_fqdn_type(self, qualifiedName, typeName):
        """
        Get a single feature by it's QualifiedName and Type
        Returns the feature else throws an AtlasException with 400 error code
        """
        response = self.purview_client.get_entity(qualifiedName=qualifiedName, typeName=typeName)
        entities = response.get('entities')
        for entity in entities:
            if entity.get('typeName') == typeName and entity.get('attributes').get('qualifiedName') == qualifiedName: 
                return entity
       
    def get_feature_by_fqdn(self, qualifiedName):
        """
        Get feature by qualifiedName
        Returns the feature else throws an AtlasException with 400 error code
        """        
        id = self.get_feature_id(qualifiedName)
        return self.get_feature_by_guid(id)
    
    def get_feature_by_guid(self, guid):
        """
        Get a single feature by it's GUID
        Returns the feature else throws an AtlasException with 400 error code
        """ 
        response = self.purview_client.get_single_entity(guid=guid)
        return response
    
    def get_feature_lineage(self, guid):
        """
        Get feature's lineage by it's GUID
        Returns the feature else throws an AtlasException with 400 error code
        """
        return self.purview_client.get_entity_lineage(guid=guid)

    def get_feature_id(self, qualifiedName, type: str):
        """
        Get guid of a feature given its qualifiedName
        """        
        # the search term should be full qualified name
        # TODO: need to update the calling functions to add `type` field to make it more performant
        # purview_client.get_entity(qualifiedName=qualifiedName) might not work here since it requires an additonal typeName parameter
        # Currently still use the `query` API to get the result in a "full name match" way.
        # self.purview_client.get_entity(qualifiedName=qualifiedName, typeName=type)

        # get the corresponding features belongs to a certain project.
        # note that we need to use "eq" to filter exactly this qualified name
        # see syntax here: https://docs.microsoft.com/en-us/rest/api/purview/catalogdataplane/discovery/query#discovery_query_andornested
        query_filter = {
            "attributeName": "qualifiedName",
            "operator": "eq",
            "attributeValue": qualifiedName
        }
        result = self.purview_client.discovery.query(keywords = None, filter=query_filter)
        entities = result['value']
        # There should be exactly one result, but we don't enforce the check here
        for entity in entities:
            if entity.get('qualifiedName') == qualifiedName:
                return entity.get('id')

    def search_features(self, searchTerm):
        """
        Search the registry for the given query term
        For a ride hailing company few examples could be - "taxi", "passenger", "fare" etc.
        It's a keyword search on the registry metadata
        """        
        entities = self.purview_client.discovery.search_entities(searchTerm)
        return entities
    
    def _get_lineage_map(self, project_name: str, entity_type: Union[str, List[str]] = None, limit=1000, starting_offset=0,) -> List[Dict]:
        """
        List all the already registered entities. entity_type should be one of: SOURCE, DERIVED_FEATURE, ANCHOR, ANCHOR_FEATURE, FEATHR_PROJECT, or a list of those values
        limit: a maximum 1000 will be enforced at the underlying API
        
        returns a list of the result entities.
        """
        if project_name is None:
            raise RuntimeError("You need to specify a project_name")
        
        # get the ID of the project name
        res = self.purview_client.get_entity(qualifiedName=project_name, typeName=TYPEDEF_FEATHR_PROJECT)

        # error handling
        if len(res) == 0:
            logger.warning(f"project {project_name} is not found.")
            return []
        # there should be only one entity
        project_guid = res['entities'][0]['guid']  
        # return the project lineage that we can use
        # maintain almost the same parameter with the Purview portal. Can be changed later.
        lineage_map = self.purview_client.get_entity_lineage(guid=project_guid, depth=6, width=100, direction="BOTH", includeParent=True,getDerivedLineage=False)
        

        return lineage_map
    def _reverse_relations(self, input_relations: List[Dict[str, str]], base_entity_guid):
        """Reverse the `fromEntityId` and `toEntityId` field, as currently we are using a bottom up way to store in Purview
        """
        output_reversed_relations = []        
        output_reversed_relation_lookup = defaultdict(list)
        for d in input_relations:
            output_reversed_relations.append({"fromEntityId": d['toEntityId'], "toEntityId": d['fromEntityId']})
            # lookup table so that we can easily get the associated entities given a higher up level entity
            # for example given a project entity, we can use this output_reversed_relation_lookup to look up the associated anchor/derived feature.
            output_reversed_relation_lookup[d['toEntityId']].append(d['fromEntityId'])

        return output_reversed_relation_lookup

    def _get_child_entity_with_type(self, parent_guid: str, lookup_dict: Dict[str, List], type: str, guidEntityMap):
        """Get corresponding entity with certain type, by providing a parent_guid to lookup, a `lookup_dict` which can make the search faster, the target type, and the full `guidEntityMap` so that we know the type for each entity ID
        The reason that we need to have this function is because in Purview, the "parent" entity (say a project) and the corresponding "child" entity (say an anchor associated with the project) don't connect directly. They are connected thru an `AtlasProcees`, so the connection is like this:
        child entity > PROCESS > parent entity.

        Because of this, in order to find the corresponding child entity, we need to look up twice here, first to find child entity > PROCESS, then find PROCESS > parent entity

        Returns: a list of entities that is the child entity of the given parent entity, while matching the given type.
        """

        return_guid = []
        for next_guid in lookup_dict[parent_guid]:
            for two_hop_guid in lookup_dict[next_guid]:
                if guidEntityMap[two_hop_guid]['typeName'] == type:
                    return_guid.append(two_hop_guid)
        
        return return_guid


    def get_features_from_registry(self, project_name: str) -> Tuple[List[FeatureAnchor], List[DerivedFeature]]:
        """Sync Features from registry to local workspace, given a project_name, will write project's features from registry to to user's local workspace]
        If the project is big, the return result could be huge.
        Args:
            project_name (str): project name.
        """
        lineage_result = self._get_lineage_map(project_name=project_name)
        self.lineage_result = lineage_result
        # result['guidEntityMap'] should be a dict and we only care about the values (key will be the GUID of the entity)
        all_entities_in_project = []
        entity_type_set = set([TYPEDEF_DERIVED_FEATURE, TYPEDEF_ANCHOR_FEATURE, TYPEDEF_FEATHR_PROJECT, TYPEDEF_ANCHOR, TYPEDEF_SOURCE])
        for entity in lineage_result['guidEntityMap'].values():
            if entity['typeName'] in entity_type_set:
                all_entities_in_project.append(entity) 
        if not all_entities_in_project:
            # if the result is empty
            return (None, None)
        
        reversed_relations_lookup = self._reverse_relations(lineage_result['relations'], base_entity_guid=lineage_result['baseEntityGuid'])
        self.reversed_relations_lookup = reversed_relations_lookup
        # get project entity, the else are feature entities (derived+anchor)
        project_entity = [x for x in all_entities_in_project if x['typeName']==TYPEDEF_FEATHR_PROJECT][0] # there's only one available
        feature_entities = [x for x in all_entities_in_project if (x['typeName']==TYPEDEF_ANCHOR_FEATURE or x['typeName']==TYPEDEF_DERIVED_FEATURE)]
        feature_entity_guid_mapping = {x['guid']:x for x in feature_entities}

        # this is guid for feature anchor (GROUP of anchor features)
        anchor_guid = [anchor_entity["guid"] for anchor_entity in project_entity["attributes"]["anchor_features"]]
        derived_feature_guid = [derived_feature_entity["guid"] for derived_feature_entity in project_entity["attributes"]["derived_features"]]
        
        derived_feature_ids = [feature_entity_guid_mapping[x] for x in derived_feature_guid]
        
        derived_feature_list = []
        for derived_feature_entity_id in derived_feature_ids:
            # this will be used to generate DerivedFeature instance
            derived_feature_key_list = []
                
            for key in derived_feature_entity_id["attributes"]["key"]:
                derived_feature_key_list.append(TypedKey(key_column=key["key_column"], key_column_type=key["key_column_type"], full_name=key["full_name"], description=key["description"], key_column_alias=key["key_column_alias"]))
            
            # for feature anchor (GROUP), input features are splitted into input anchor features & input derived features
            
            anchor_feature_guid = self._get_child_entity_with_type(derived_feature_entity_id['guid'],lookup_dict=reversed_relations_lookup, type=TYPEDEF_ANCHOR_FEATURE, guidEntityMap=lineage_result['guidEntityMap'])
            derived_feature_guid = self._get_child_entity_with_type(derived_feature_entity_id['guid'],lookup_dict=reversed_relations_lookup, type=TYPEDEF_DERIVED_FEATURE, guidEntityMap=lineage_result['guidEntityMap'])
            # for derived features, search all related input features.
            input_features_guid = self.search_input_anchor_features(derived_feature_guid,feature_entity_guid_mapping)
            # chain the input features together
            # filter out features that is related with this derived feature 
            all_input_features = self._get_features_by_guid_or_entities(guid_list=input_features_guid+anchor_feature_guid, entity_list=all_entities_in_project) 
            derived_feature_list.append(DerivedFeature(name=derived_feature_entity_id["attributes"]["name"],
                                feature_type=self._get_feature_type_from_hocon(derived_feature_entity_id["attributes"]["type"]),
                                transform=self._get_transformation_from_dict(derived_feature_entity_id["attributes"]['transformation']),
                                key=derived_feature_key_list,
                                input_features= all_input_features,
                                # use dict.get() since "tags" field is optional
                                registry_tags=derived_feature_entity_id["attributes"].get("tags")))                    
        
        # anchor_result = self.purview_client.get_entity(guid=anchor_guid)["entities"]
        anchor_result = [x for x in all_entities_in_project if x['typeName']==TYPEDEF_ANCHOR]
        anchor_list = []

        for anchor_entity in anchor_result:
            feature_guid = self._get_child_entity_with_type(anchor_entity['guid'],lookup_dict=reversed_relations_lookup, type=TYPEDEF_ANCHOR_FEATURE, guidEntityMap=lineage_result['guidEntityMap'])
            anchor_list.append(FeatureAnchor(name=anchor_entity["attributes"]["name"],
                                source=self._get_source_by_guid(anchor_entity["attributes"]["source"]["guid"], entity_list = all_entities_in_project),
                                features=self._get_features_by_guid_or_entities(guid_list = feature_guid, entity_list=all_entities_in_project),
                                registry_tags=anchor_entity["attributes"].get("tags")))

        return (anchor_list, derived_feature_list)

    def search_input_anchor_features(self,derived_guids,feature_entity_guid_mapping) ->List[str]:
        '''
        Iterate all derived features and its parent links, extract and aggregate all inputs
        '''
        stack = [x for x in derived_guids]
        result = []
        while len(stack)>0:
            current_derived_guid = stack.pop()
            current_input = feature_entity_guid_mapping[current_derived_guid]
            
            new_derived_features = self._get_child_entity_with_type(current_input['guid'],lookup_dict=self.reversed_relations_lookup,type=TYPEDEF_DERIVED_FEATURE, guidEntityMap=self.lineage_result['guidEntityMap'])
            new_anchor_features = self._get_child_entity_with_type(current_input['guid'],lookup_dict=self.reversed_relations_lookup,type=TYPEDEF_ANCHOR_FEATURE, guidEntityMap=self.lineage_result['guidEntityMap'])
            for feature_guid in new_derived_features:
                stack.append(feature_guid)
            result += new_anchor_features
            result = list(set(result))
        return result


    def _correct_function_identation(self, user_func: str) -> str:
        """
        The function read from registry might have the wrong identation. We need to correct those identations.
        More specifically, we are using the inspect module to copy the function body for UDF for further submission. In that case, there will be situations like this:

        def feathr_udf1(df)
            return df

                def feathr_udf2(df)
                    return df

        For example, in Feathr test cases, there are similar patterns for `feathr_udf2`, since it's defined in another function body (the registry_test_setup method).
        This is not an ideal way of dealing with that, but we'll keep it here until we figure out a better way.
        """
        if user_func is None:
            return None
        # if user_func is a string, turn it into a list of strings so that it can be used below
        temp_udf_source_code = user_func.split('\n')
        # assuming the first line is the function name
        leading_space_num = len(temp_udf_source_code[0]) - len(temp_udf_source_code[0].lstrip())
        # strip the lines to make sure the function has the correct indentation
        udf_source_code_striped = [line[leading_space_num:] for line in temp_udf_source_code]
        # append '\n' back since it was deleted due to the previous split
        udf_source_code = [line+'\n' for line in udf_source_code_striped]
        return " ".join(udf_source_code)

    def _get_source_by_guid(self, guid, entity_list) -> Source:
        """give a entity list and the target GUID for the source entity, return a python `Source` object.
        """
        # TODO: currently return HDFS source by default. For JDBC source, it's currently implemented using HDFS Source so we should split in the future

        # there should be only one entity available
        source_entity = [x for x in entity_list if x['guid'] == guid][0]

        # return different source type
        if source_entity["attributes"]["type"] == INPUT_CONTEXT:
            return InputContext()
        else:
            
            return HdfsSource(name=source_entity["attributes"]["name"],
                    event_timestamp_column=source_entity["attributes"]["event_timestamp_column"],
                    timestamp_format=source_entity["attributes"]["timestamp_format"],
                    preprocessing=self._correct_function_identation(source_entity["attributes"]["preprocessing"]),
                    path=source_entity["attributes"]["path"],
                    registry_tags=source_entity["attributes"].get("tags")
                    )



    def _get_feature_type_from_hocon(self, input_str: str) -> FeatureType:
        """Get Feature types from a HOCON config, given that we stored the feature type in a plain string.

        Args:
            input_str (str): the input string for the stored HOCON config

        Returns:
            FeatureType: feature type that can be used by Python
        """
        conf = ConfigFactory.parse_string(input_str)
        valType = conf.get_string('type.valType')
        dimensionType = conf.get_string('type.dimensionType')
        if dimensionType == '[INT]':
            # if it's not empty, i.e. [INT], indicating it's vectors
            if valType == 'DOUBLE':
                return DoubleVectorFeatureType()
            elif valType == 'LONG':
                return Int64VectorFeatureType()
            elif valType == 'INT':
                return Int32VectorFeatureType()
            elif valType == 'FLOAT':
                return FloatVectorFeatureType()
            else:
                logger.error("{} cannot be parsed.", valType)
        else:
            if valType == 'STRING':
                return StringFeatureType()
            elif valType == 'BYTES':
                return BytesFeatureType()
            elif valType == 'DOUBLE':
                return DoubleFeatureType()
            elif valType == 'FLOAT':
                return FloatFeatureType()
            elif valType == 'LONG':
                return Int64FeatureType()
            elif valType == 'INT':
                return Int32FeatureType()
            elif valType == 'BOOLEAN':
                return BooleanFeatureType()
            else:
                logger.error("{} cannot be parsed.", valType)

    def _get_transformation_from_dict(self, input: Dict) -> FeatureType:
        if 'transform_expr' in input:
            # it's ExpressionTransformation
            return ExpressionTransformation(input['transform_expr'])
        elif 'def_expr' in input:
            return WindowAggTransformation(agg_expr=input['def_expr'], agg_func=input['agg_func'], window=input['window'], group_by=input['group_by'], filter=input['filter'], limit=input['limit'])
        else:
            # no transformation function observed
            return None

    def _get_features_by_guid_or_entities(self, guid_list, entity_list) -> List[FeatureAnchor]:
        """return a python list of the features that are referenced by a list of guids. 
        If entity_list is provided, use entity_list to reconstruct those features
        This is for "anchor feature" only.
        """
        if not entity_list:
            feature_entities = self.purview_client.get_entity(guid=guid_list)["entities"]
        else:
            guid_set = set(guid_list)
            feature_entities = [x for x in entity_list if x['guid'] in guid_set]

            # raise error if we cannot find all the guid
            if len(feature_entities) != len(guid_list):
                raise RuntimeError("Number of `feature_entities` is less than provided GUID list for search. The project might be broken.")

        feature_list=[]
        key_list = []
        for feature_entity in feature_entities:
            for key in feature_entity["attributes"]["key"]:
                key_list.append(TypedKey(key_column=key["key_column"], key_column_type=key["key_column_type"], full_name=key["full_name"], description=key["description"], key_column_alias=key["key_column_alias"]))

            # after get keys, put them in features
            feature_list.append(Feature(name=feature_entity["attributes"]["name"],
                    feature_type=self._get_feature_type_from_hocon(feature_entity["attributes"]["type"]), # stored as a hocon string, can be parsed using pyhocon
                    transform=self._get_transformation_from_dict(feature_entity["attributes"]['transformation']), #transform attributes are stored in a dict fashion , can be put in a WindowAggTransformation
                    key=key_list,
                    # use dict.get() since "tags" field is optional
                    registry_tags=feature_entity["attributes"].get("tags"),

            ))
        return feature_list 
