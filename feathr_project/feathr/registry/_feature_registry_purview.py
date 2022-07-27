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
from uuid import UUID

from azure.identity import DefaultAzureCredential
from jinja2 import Template
from loguru import logger
from pyapacheatlas.auth import ServicePrincipalAuthentication
from pyapacheatlas.auth.azcredential import AzCredentialWrapper
from pyapacheatlas.core import (AtlasClassification, AtlasEntity, AtlasProcess,
                                PurviewClient, TypeCategory)
from pyapacheatlas.core.typedef import (AtlasAttributeDef,
                                        AtlasRelationshipEndDef, Cardinality,
                                        EntityTypeDef, RelationshipTypeDef)
                            
from pyapacheatlas.core.util import GuidTracker,AtlasException
from pyhocon import ConfigFactory

from feathr.definition.dtype import *
from feathr.registry.registry_utils import *
from feathr.utils._file_utils import write_to_file
from feathr.definition.anchor import FeatureAnchor
from feathr.constants import *
from feathr.definition.feature import Feature, FeatureType,FeatureBase
from feathr.definition.feature_derivations import DerivedFeature
from feathr.definition.repo_definitions import RepoDefinitions
from feathr.definition.source import HdfsSource, InputContext, JdbcSource, Source
from feathr.definition.transformation import (ExpressionTransformation, Transformation,
                                   WindowAggTransformation)
from feathr.definition.typed_key import TypedKey
from feathr.registry.feature_registry import FeathrRegistry

from feathr.constants import *

class _PurviewRegistry(FeathrRegistry):
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
                AtlasAttributeDef(
                    name="url", typeName="string", cardinality=Cardinality.SINGLE),
                AtlasAttributeDef(
                    name="dbtable", typeName="string", cardinality=Cardinality.SINGLE),
                AtlasAttributeDef(
                    name="query", typeName="string", cardinality=Cardinality.SINGLE),
                AtlasAttributeDef(
                    name="auth", typeName="string", cardinality=Cardinality.SINGLE),
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
            
    def _parse_source(self, source: Union[Source, HdfsSource, JdbcSource]) -> AtlasEntity:
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

        attrs = {}
        if isinstance(source, JdbcSource):
            {
                "type": INPUT_CONTEXT if input_context else urlparse(source.path).scheme,
                "url": INPUT_CONTEXT if input_context else source.url,
                "timestamp_format": source.timestamp_format,
                "event_timestamp_column": source.event_timestamp_column,
                "tags": source.registry_tags,
                "preprocessing": preprocessing_func  # store the UDF as a string
            }
            if source.auth is not None:
                attrs["auth"] = source.auth
            if source.dbtable is not None:
                attrs["dbtable"] = source.dbtable
            if source.query is not None:
                attrs["query"] = source.query
        else:
            attrs = {
                "type": INPUT_CONTEXT if input_context else urlparse(source.path).scheme,
                "path": INPUT_CONTEXT if input_context else source.path,
                "timestamp_format": source.timestamp_format,
                "event_timestamp_column": source.event_timestamp_column,
                "tags": source.registry_tags,
                "preprocessing": preprocessing_func  # store the UDF as a string
            }
        source_entity = AtlasEntity(
            name=source.name,
            qualified_name=self.project_name + self.registry_delimiter + source.name,
            attributes=attrs,
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
                      
    def _create_project(self) -> UUID:
        ''' 
        create a project entity
        ''' 
        predefined_guid = self.guid.get_guid()
        feathr_project_entity = AtlasEntity(
            name=self.project_name,
            qualified_name=self.project_name,
            typeName=TYPEDEF_FEATHR_PROJECT,
            guid=predefined_guid)
        guid = self.upload_single_entity_to_purview(feathr_project_entity)
        return guid

    def upload_single_entity_to_purview(self,entity:Union[AtlasEntity,AtlasProcess]):
        '''
        Upload a single entity to purview, could be a process entity or atlasentity. 
        Since this is used for migration existing project, ignore Atlas PreconditionFail (412)
        If the eneity already exists, return the existing entity's GUID.
        Otherwise, return the new entity GUID.
        The entity itself will also be modified, fill the GUID with real GUID in Purview.
        In order to avoid having concurrency issue, and provide clear guidance, this method only allows entity uploading once at a time.
        '''
        try:
            entity.lastModifiedTS="0"
            result = self.purview_client.upload_entities([entity])
            entity.guid = result['guidAssignments'][entity.guid]
            print(f"Successfully created {entity.typeName} -- {entity.qualifiedName}")
        except AtlasException as e:
            if "PreConditionCheckFailed" in e.args[0]:
                entity.guid = self.purview_client.get_entity(qualifiedName=entity.qualifiedName,typeName = entity.typeName)['entities'][0]['guid']
                print(f"Found existing entity  {entity.guid}, {entity.typeName} -- {entity.qualifiedName}")
        return UUID(entity.guid)
    
    def _generate_relation_pairs(self, from_entity:dict, to_entity:dict, relation_type):
        type_lookup = {RELATION_CONTAINS: RELATION_BELONGSTO, RELATION_CONSUMES: RELATION_PRODUCES}

        forward_relation =  AtlasProcess(
            name=str(from_entity["guid"]) + " to " + str(to_entity["guid"]),
            typeName="Process",
            qualified_name=self.registry_delimiter.join(
                [relation_type,str(from_entity["guid"]), str(to_entity["guid"])]),
            inputs=[self.to_min_repr(from_entity)],
            outputs=[self.to_min_repr(to_entity)],
            guid=self.guid.get_guid())
        
        backward_relation = AtlasProcess(
            name=str(to_entity["guid"]) + " to " + str(from_entity["guid"]),
            typeName="Process",
            qualified_name=self.registry_delimiter.join(
                [type_lookup[relation_type], str(to_entity["guid"]), str(from_entity["guid"])]),
            inputs=[self.to_min_repr(to_entity)],
            outputs=[self.to_min_repr(from_entity)],
            guid=self.guid.get_guid())
        return [forward_relation,backward_relation]
    
    def to_min_repr(self,entity:dict) -> dict:
        return {
            'qualifiedName':entity['attributes']["qualifiedName"],
            'guid':str(entity["guid"]),
            'typeName':str(entity['typeName']),
        }

    def _create_source(self, s: Source) -> UUID:
        '''
        create a data source under a project.
        this will create the data source entity, together with the relation entity
        '''
        project_entity = self.purview_client.get_entity(qualifiedName=self.project_name,typeName=TYPEDEF_FEATHR_PROJECT)['entities'][0]
        attrs = source_to_def(s)
        qualified_name = self.registry_delimiter.join([project_entity['attributes']['qualifiedName'],attrs['name']])
        source_entity = AtlasEntity(
            name=attrs['name'],
            qualified_name=qualified_name,
            attributes= {k:v for k,v in attrs.items() if k !="name"},
            typeName=TYPEDEF_SOURCE,
            guid=self.guid.get_guid(),
        )
        source_id = self.upload_single_entity_to_purview(source_entity)
        
        # change from AtlasEntity to Entity
        source_entity = self.purview_client.get_entity(source_id)['entities'][0]

        # Project contains source, source belongs to project
        project_contains_source_relation = self._generate_relation_pairs(
            project_entity, source_entity, RELATION_CONTAINS)
        [self.upload_single_entity_to_purview(x) for x in project_contains_source_relation]
        return source_id

    def _create_anchor(self, s: FeatureAnchor) -> UUID:
        '''
        Create anchor under project ,and based on the data source
        This will also create two relation pairs 
        '''
        project_entity = self.purview_client.get_entity(qualifiedName=self.project_name,typeName=TYPEDEF_FEATHR_PROJECT)['entities'][0]
        source_entity = self.purview_client.get_entity(qualifiedName=self.registry_delimiter.join([self.project_name,s.source.name]),typeName=TYPEDEF_SOURCE)['entities'][0]
        attrs = anchor_to_def(s)
        qualified_name = self.registry_delimiter.join([self.project_name,attrs['name']])
        anchor_entity = AtlasEntity(
            name=s.name,
            qualified_name=qualified_name,
            attributes= {k:v for k,v in attrs.items() if k not in ['name','qualifiedName']},
            typeName=TYPEDEF_ANCHOR,
            guid=self.guid.get_guid(),
        )

        anchor_id = self.upload_single_entity_to_purview(anchor_entity)

        # change from AtlasEntity to Entity
        anchor_entity = self.purview_client.get_entity(anchor_id)['entities'][0]

        
        # project contians anchor, anchor belongs to project.
        project_contains_anchor_relation = self._generate_relation_pairs(
            project_entity, anchor_entity, RELATION_CONTAINS)
        anchor_consumes_source_relation = self._generate_relation_pairs(
            anchor_entity,source_entity, RELATION_CONSUMES)
        [self.upload_single_entity_to_purview(x) for x in project_contains_anchor_relation + anchor_consumes_source_relation]

        return anchor_id

    def _create_anchor_feature(self, anchor_id: str, source:Source,s: Feature) -> UUID:
        '''
        Create anchor feature under anchor. 
        This will also create three relation pairs
        '''
        project_entity = self.purview_client.get_entity(qualifiedName=self.project_name,typeName=TYPEDEF_FEATHR_PROJECT)['entities'][0]
        anchor_entity = self.purview_client.get_entity(anchor_id)['entities'][0]
        source_entity = self.purview_client.get_entity(qualifiedName=self.registry_delimiter.join([self.project_name,source.name]),typeName=TYPEDEF_SOURCE)['entities'][0]

        attrs = feature_to_def(s)
        attrs['type'] = attrs['featureType']
        qualified_name = self.registry_delimiter.join([self.project_name,
                                                       anchor_entity["attributes"]["name"],
                                                       attrs['name']])

        anchor_feature_entity = AtlasEntity(
            name=s.name,
            qualified_name=qualified_name,
            attributes= {k:v for k,v in attrs.items() if k not in ['name','qualifiedName']},
            typeName=TYPEDEF_ANCHOR_FEATURE,
            guid=self.guid.get_guid())
        anchor_feature_id = self.upload_single_entity_to_purview(anchor_feature_entity)

        # change from AtlasEntity to Entity
        anchor_feature_entity = self.purview_client.get_entity(anchor_feature_id)['entities'][0]

        # Project contains AnchorFeature, AnchorFeature belongs to Project
        project_contains_feature_relation = self._generate_relation_pairs(
            project_entity, anchor_feature_entity, RELATION_CONTAINS)
        anchor_contains_feature_relation = self._generate_relation_pairs(
            anchor_entity, anchor_feature_entity, RELATION_CONTAINS)
        feature_consumes_source_relation = self._generate_relation_pairs(
            anchor_feature_entity, source_entity, RELATION_CONSUMES)

        [self.upload_single_entity_to_purview(x) for x in  
        project_contains_feature_relation
        + anchor_contains_feature_relation
        + feature_consumes_source_relation]
        
        return anchor_feature_id

    def _create_derived_feature(self, s: DerivedFeature) -> UUID:
        '''
        Create DerivedFeature.
        This will also create multiple relations.
        '''
        input_features = [self.purview_client.get_entity(x._registry_id)['entities'][0] for x in s.input_features]
        attrs = derived_feature_to_def(s)
        attrs['type'] = attrs['featureType']

        project_entity = self.purview_client.get_entity(qualifiedName=self.project_name,typeName=TYPEDEF_FEATHR_PROJECT)['entities'][0]
        qualified_name = self.registry_delimiter.join([self.project_name,attrs['name']])
        derived_feature_entity = AtlasEntity(
            name=s.name,
            qualified_name=qualified_name,
            attributes={k:v for k,v in attrs.items() if k not in ['name','qualifiedName']},
            typeName=TYPEDEF_DERIVED_FEATURE,
            guid=self.guid.get_guid())
        derived_feature_id = self.upload_single_entity_to_purview(derived_feature_entity)
        
        # change from AtlasEntity to Entity
        derived_feature_entity = self.purview_client.get_entity(derived_feature_id)['entities'][0]

        # Project contains DerivedFeature, DerivedFeature belongs to Project.
        feature_project_contain_belong_pairs = self._generate_relation_pairs(
            project_entity, derived_feature_entity, RELATION_CONTAINS)

        consume_produce_pairs = []
        # Each input feature produces DerivedFeature, DerivedFeatures consumes from each input feature.
        for input_feature in input_features:
            consume_produce_pairs += self._generate_relation_pairs(
                    derived_feature_entity, input_feature,RELATION_CONSUMES)
        
        [self.upload_single_entity_to_purview(x) for x in  
        feature_project_contain_belong_pairs
            + consume_produce_pairs]
        
        return derived_feature_id

    def register_features(self, workspace_path: Optional[Path] = None, from_context: bool = True, anchor_list:List[FeatureAnchor]=[], derived_feature_list:List[DerivedFeature]=[]):
        """Register Features for the specified workspace. 

        Args:
            workspace_path (str, optional): path to a workspace. Defaults to None.
            from_context: whether the feature is from context (i.e. end users has to callFeathrClient.build_features()) or the feature is from a pre-built config file. Currently Feathr only supports register features from context.
            anchor_list: The anchor list after feature build
            derived_feature_list: the derived feature list after feature build
        """

        if not from_context:
            raise RuntimeError("Currently Feathr only supports registering features from context (i.e. you must call FeathrClient.build_features() before calling this function).")
                # Before starting, create the project
        self._register_feathr_feature_types()

        self.project_id = self._create_project()

        for anchor in anchor_list:
            source = anchor.source
            # 1. Create Source on the registry
            # We always re-create INPUT_CONTEXT as lots of existing codes reuse the singleton in different projects
            if (source.name == INPUT_CONTEXT) or (not hasattr(source, "_registry_id")):
                source._registry_id = self._create_source(source)
            # 2. Create Anchor on the registry
            if not hasattr(anchor, "_registry_id"):
                anchor._registry_id = self._create_anchor(anchor)
            # 3. Create all features on the registry
            for feature in anchor.features:
                if not hasattr(feature, "_registry_id"):
                    feature._registry_id = self._create_anchor_feature(
                        anchor._registry_id, anchor.source,feature)
        # 4. Create all derived features on the registry
        for df in topological_sort(derived_feature_list):
            if not hasattr(df, "_registry_id"):
                df._registry_id = self._create_derived_feature(df)
        logger.info(
            "Finished registering features.")


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
        List all the already registered features. If project_name is not provided or is None, it will return all the
        registered features; otherwise it will only return only features under this project
        """

        feature_list = []

        if not project_name:
            raise RuntimeError("project_name must be specified.")

        # get the corresponding features belongs to a certain project.
        # note that we need to use "startswith" to filter out the features that don't belong to this project.
        # see syntax here: https://docs.microsoft.com/en-us/rest/api/purview/catalogdataplane/discovery/query#discovery_query_andornested
        query_filter = {
            "and": [
                {
                    "or":
                    [
                        {"entityType": TYPEDEF_DERIVED_FEATURE},
                        {"entityType": TYPEDEF_ANCHOR_FEATURE}
                    ]
                },
                {
                    "attributeName": "qualifiedName",
                    "operator": "startswith",
                    "attributeValue": project_name + self.registry_delimiter
                }
            ]
        }
        result = self.purview_client.discovery.query(filter=query_filter)

        entities = result['value']
        # entities = self.purview_client.discovery.search_entities(query = None, search_filter=query_filter, limit=limit)

        for entity in entities:
            feature_list.append({"name":entity["name"],'id':entity['id'],"qualifiedName":entity['qualifiedName']})

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
    
    def _list_registered_entities_with_details(self, project_name: str, entity_type: Union[str, List[str]] = None, limit=1000, starting_offset=0,) -> List[Dict]:
        """
        List all the already registered entities. entity_type should be one of: SOURCE, DERIVED_FEATURE, ANCHOR, ANCHOR_FEATURE, FEATHR_PROJECT, or a list of those values
        limit: a maximum 1000 will be enforced at the underlying API
        
        returns a list of the result entities.
        """
        entity_type_list = [entity_type] if isinstance(
            entity_type, str) else entity_type

        for i in entity_type_list:
            if i not in {TYPEDEF_SOURCE, TYPEDEF_DERIVED_FEATURE, TYPEDEF_ANCHOR, TYPEDEF_ANCHOR_FEATURE, TYPEDEF_FEATHR_PROJECT}:
                raise RuntimeError(
                    f'only SOURCE, DERIVED_FEATURE, ANCHOR, ANCHOR_FEATURE, FEATHR_PROJECT are supported when listing the registered entities, {entity_type} is not one of them.')

        if project_name is None:
            raise RuntimeError("You need to specify a project_name")
        # the search grammar: 
        # https://docs.microsoft.com/en-us/azure/purview/how-to-search-catalog#search-query-syntax
        # https://docs.microsoft.com/en-us/rest/api/datacatalog/data-catalog-search-syntax-reference

        # get the corresponding features belongs to a certain project.
        # note that we need to use "startswith" to filter out the features that don't belong to this project.
        # see syntax here: https://docs.microsoft.com/en-us/rest/api/purview/catalogdataplane/discovery/query#discovery_query_andornested
        # this search does the following:
        # search all the entities that start with project_name+delimiter for all the search entities
        # However, for TYPEDEF_FEATHR_PROJECT, it doesn't have delimiter in the qualifiedName
        # Hence if TYPEDEF_FEATHR_PROJECT is in the `entity_type` input, we need to search for that specifically
        # and finally "OR" the result to union them
        query_filter = {
            "or":
            [{
                "and": [{
                    # this is a list of the entity types that you want to query
                    "or": [{"entityType": e} for e in entity_type_list]
                },
                    {
                    "attributeName": "qualifiedName",
                    "operator": "startswith",
                    # use `project_name + self.registry_delimiter` to limit the search results
                    "attributeValue": project_name + self.registry_delimiter
                }]},
                # if we are querying TYPEDEF_FEATHR_PROJECT, then "union" the result by using this query
                {
                "and": [{
                    "or": [{"entityType": TYPEDEF_FEATHR_PROJECT}] if TYPEDEF_FEATHR_PROJECT in entity_type_list else None
                },
                    {
                    "attributeName": "qualifiedName",
                    "operator": "startswith",
                    "attributeValue": project_name
                }]}]
        }

        # Important properties returned includes:
        # id (the guid of the entity), name, qualifiedName, @search.score,
        # and @search.highlights
        # TODO: it might be throttled in the backend and wait for the `pyapacheatlas` to fix this
        # https://github.com/wjohnson/pyapacheatlas/issues/206
        # `pyapacheatlas` needs a bit optimization to avoid additional calls.
        result_entities = self.purview_client.discovery.search_entities(query=None, search_filter=query_filter, limit = limit)
        
        # append the guid list. Since we are using project_name + delimiter to search, all the result will be valid.
        guid_list = [entity["id"] for entity in result_entities]

        entity_res = [] if guid_list is None or len(guid_list)==0 else self.purview_client.get_entity(
            guid=guid_list)["entities"]
        return entity_res

    def get_features_from_registry(self, project_name: str) -> Tuple[List[FeatureAnchor], List[DerivedFeature]]:
        """Sync Features from registry to local workspace, given a project_name, will write project's features from registry to to user's local workspace]
        If the project is big, the return result could be huge.
        Args:
            project_name (str): project name.
        """
        project_entity = self.purview_client.get_entity(qualifiedName=project_name,typeName=TYPEDEF_FEATHR_PROJECT)['entities'][0]
        
        single_direction_process = [entity for _,entity in self.purview_client.get_entity_lineage(project_entity['guid'])['guidEntityMap'].items() \
            if entity['typeName']=='Process' and \
                (entity['attributes']['qualifiedName'].startswith(RELATION_CONTAINS) \
                    or entity['attributes']['qualifiedName'].startswith(RELATION_CONSUMES))]
        contain_relations = [x['displayText'].split(' to ') for x in single_direction_process if x['attributes']['qualifiedName'].startswith(RELATION_CONTAINS)]

        consume_relations = [x['displayText'].split(' to ') for x in single_direction_process if x['attributes']['qualifiedName'].startswith(RELATION_CONSUMES)]

        entities_under_project = [self.purview_client.get_entity(x[1])['entities'][0] for x in contain_relations if x[0]== project_entity['guid']]
        if not entities_under_project:
            # if the result is empty
            return (None, None)
        entities_dict = {x['guid']:x for x in entities_under_project}

        derived_feature_list = []

        for derived_feature_entity in [x for x in entities_under_project if x['typeName']==TYPEDEF_DERIVED_FEATURE]:
            # this will be used to generate DerivedFeature instance
            derived_feature_key_list = []
                
            for key in derived_feature_entity["attributes"]["key"]:
                derived_feature_key_list.append(TypedKey(key_column=key["keyColumn"], key_column_type=key["keyColumnType"], full_name=key["fullName"], description=key["description"], key_column_alias=key["keyColumnAlias"]))
            
            def search_for_input_feature(elem, full_relations,full_entities):
                matching_relations = [x for x in full_relations if x[0]==elem['guid']]
                target_entities = [full_entities[x[1]] for x in matching_relations]
                input_features = [x for x in target_entities if x['typeName']==TYPEDEF_ANCHOR_FEATURE \
                    or x['typeName']==TYPEDEF_DERIVED_FEATURE]
                return input_features

            all_input_features = search_for_input_feature(derived_feature_entity,consume_relations,entities_dict)
            derived_feature_list.append(DerivedFeature(name=derived_feature_entity["attributes"]["name"],
                                feature_type=self._get_feature_type_from_hocon(derived_feature_entity["attributes"]["type"]),
                                transform=self._get_transformation_from_dict(derived_feature_entity["attributes"]['transformation']),
                                key=derived_feature_key_list,
                                input_features= all_input_features,
                                registry_tags=derived_feature_entity["attributes"]["tags"]))                    
        
        # anchor_result = self.purview_client.get_entity(guid=anchor_guid)["entities"]
        anchor_list = []

        for anchor_entity in [x for x in entities_under_project if x['typeName']==TYPEDEF_ANCHOR]:
            consume_items_under_anchor = [entities_dict[x[1]] for x in consume_relations if x[0]==anchor_entity['guid']]
            source_entity = [x for x in consume_items_under_anchor if x['typeName']==TYPEDEF_SOURCE][0]

            contain_items_under_anchor = [entities_dict[x[1]] for x in contain_relations if x[0]==anchor_entity['guid']]
            anchor_features_guid = [x['guid'] for x in contain_items_under_anchor if x['typeName']==TYPEDEF_ANCHOR_FEATURE]

            anchor_list.append(FeatureAnchor(name=anchor_entity["attributes"]["name"],
                                source=HdfsSource(
                                    name=source_entity["attributes"]["name"],
                                    event_timestamp_column=source_entity["attributes"]["event_timestamp_column"],
                                    timestamp_format=source_entity["attributes"]["timestamp_format"],
                                    preprocessing=self._correct_function_identation(source_entity["attributes"]["preprocessing"]),
                                    path=source_entity["attributes"]["path"],
                                    registry_tags=source_entity["attributes"]["tags"]
                                    ),
                                features=self._get_features_by_guid_or_entities(guid_list = anchor_features_guid, entity_list=entities_under_project),
                                registry_tags=anchor_entity["attributes"]["tags"]))

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
            new_derived_features = [x["guid"] for x in current_input["attributes"]["input_derived_features"]]
            new_anchor_features = [x["guid"] for x in current_input["attributes"]["input_anchor_features"]]
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

        # if source_entity["attributes"]["path"] is INPUT_CONTEXT, it will also be assigned to this returned object
        return HdfsSource(name=source_entity["attributes"]["name"],
                event_timestamp_column=source_entity["attributes"]["event_timestamp_column"],
                timestamp_format=source_entity["attributes"]["timestamp_format"],
                preprocessing=self._correct_function_identation(source_entity["attributes"]["preprocessing"]),
                path=source_entity["attributes"]["path"],
                registry_tags=source_entity["attributes"]["tags"]
                )



    def _get_feature_type_from_hocon(self, input_str: str) -> FeatureType:
        """Get Feature types from a HOCON config, given that we stored the feature type in a plain string.

        Args:
            input_str (str): the input string for the stored HOCON config

        Returns:
            FeatureType: feature type that can be used by Python
        """
        if not input_str:
            return None
        conf = ConfigFactory.parse_string(input_str)
        valType = conf.get_string('valType') if 'valType' in conf else conf.get_string('type.valType')
        dimensionType = conf.get_string('dimensionType') if 'dimensionType' in conf else conf.get_string('type.dimensionType')
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
        if 'transformExpr' in input:
            # it's ExpressionTransformation
            return ExpressionTransformation(input['transformExpr'])
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
                key_list.append(TypedKey(key_column=key["keyColumn"], key_column_type=key["keyColumnType"], full_name=key["fullName"], description=key["description"], key_column_alias=key["keyColumnAlias"]))

            # after get keys, put them in features
            feature_list.append(Feature(name=feature_entity["attributes"]["name"],
                    feature_type=self._get_feature_type_from_hocon(feature_entity["attributes"]["type"]), # stored as a hocon string, can be parsed using pyhocon
                    transform=self._get_transformation_from_dict(feature_entity["attributes"]['transformation']), #transform attributes are stored in a dict fashion , can be put in a WindowAggTransformation
                    key=key_list,
                    registry_tags=feature_entity["attributes"]["tags"],

            ))
        return feature_list 
