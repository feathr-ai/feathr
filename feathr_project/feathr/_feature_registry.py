import glob
import importlib
import inspect
import os
import sys
from graphlib import TopologicalSorter
from pathlib import Path
from tracemalloc import stop
from typing import Dict, List, Optional, Tuple, Union
from urllib.parse import urlparse

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

from feathr._file_utils import write_to_file
from feathr.anchor import FeatureAnchor
from feathr.constants import *
from feathr.feature import Feature, FeatureType
from feathr.feature_derivations import DerivedFeature
from feathr.repo_definitions import RepoDefinitions
from feathr.source import HdfsSource, InputContext, Source
from feathr.transformation import (ExpressionTransformation, Transformation,
                                   WindowAggTransformation)
from feathr.typed_key import TypedKey


class _FeatureRegistry():
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

            anchor_entity = AtlasEntity(
                name=anchor.name,
                qualified_name=self.project_name + self.registry_delimiter + anchor.name,
                attributes={
                    "source": source_entity.to_json(minimum=True),
                    "features": [s.to_json(minimum=True) for s in anchor_feature_entities],
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
                    "tags": derived_feature.registry_tags
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
    def save_to_feature_config(self, workspace_path: Path):
        """Save feature definition within the workspace into HOCON feature config files"""
        repo_definitions = self._extract_features(workspace_path)
        self._save_request_feature_config(repo_definitions)
        self._save_anchored_feature_config(repo_definitions)
        self._save_derived_feature_config(repo_definitions)

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
        self._delete_all_feathr_entitties()
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

    def _delete_all_feathr_entitties(self):
        """
        Delete all the corresonding entity for feathr registry. For internal use only

        :param guid: The guid or guids you want to remove.
        """
        entities = self.purview_client.discovery.search_entities(
            "feathr*", limit=20)

        # [print(entity) for entity in entities]
        guid_list = [entity["id"] for entity in entities]

        # should not be large than this, otherwise the backend might throw out error
        batch_delte_size = 15
        for i in range(0, len(guid_list), batch_delte_size):
            self.purview_client.delete_entity(
                guid=guid_list[i:i+batch_delte_size])
            logger.info("{} feathr entities deleted", batch_delte_size)
    
    @classmethod
    def _get_registry_client(self):
        """
        Return a client object and users can operate more on it (like doing search)
        """
        return self.purview_client

    def list_registered_features(self, project_name: str = None, limit=50, starting_offset=0) -> List[str]:
        """
        List all the already registered features. If project_name is not provided or is None, it will return all the
        registered features; otherwise it will only return only features under this project
        """
        entities = self.purview_client.discovery.search_entities(
            f"entityType:{TYPEDEF_ANCHOR_FEATURE} or entityType:{TYPEDEF_DERIVED_FEATURE}", limit=limit, starting_offset=starting_offset)
        feature_list = []
        for entity in entities:
            if project_name:
                # if project_name is a valid string, only append entities if the qualified name start with
                # project_name+delimiter
                qualified_name: str = entity["qualifiedName"]
                # split the name based on delimiter
                result = qualified_name.split(self.registry_delimiter)
                if result[0].casefold() == project_name:
                    feature_list.append(entity["name"])
            else:
                # otherwise append all the entities
                feature_list.append(entity["name"])

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
        guid = self.get_feature_guid(qualifiedName)
        return self.get_feature_by_guid(guid)
    
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

    def get_feature_guid(self, qualifiedName):
        """
        Get guid of a feature given its qualifiedName
        """        
        search_term = "qualifiedName:{0}".format(qualifiedName)
        entities = self.purview_client.discovery.search_entities(search_term)
        for entity in entities:
            if entity.get('qualifiedName') == qualifiedName:
                return entity.get('id')

    def search_features(self, searchTerm):
        """
        Search the registry for the given query term
        For a ride hailing company few examples could be - "taxi", "passenger", "fare" etc.
        It's a keyword search on the registry metadata
        """        
        search_term = "qualifiedName:{0}".format(searchTerm)
        entities = self.purview_client.discovery.search_entities(search_term)
        return entities
    
    def _list_registered_entities_with_details(self, project_name: str = None, entity_type: Union[str, List[str]] = None, limit=50, starting_offset=0,) -> List[Dict]:
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

        # the search grammar is less documented in Atlas/Purview
        search_string = "".join(
            [f" or entityType:{e}" for e in entity_type_list])
        # remvoe the first additional " or "
        search_string = search_string[4:]
        result_entities = self.purview_client.discovery.search_entities(
            search_string, limit=limit, starting_offset=starting_offset)
        # Important properties returned includes:
        # id (the guid of the entity), name, qualifiedName, @search.score,
        # and @search.highlights
        guid_list = []
        for entity in result_entities:
            if project_name:
                # if project_name is a valid string, only append entities if the qualified name start with
                # project_name+delimiter
                qualified_name: str = entity["qualifiedName"]
                # split the name based on delimiter
                result = qualified_name.split(self.registry_delimiter)
                if result[0].casefold() == project_name:
                    guid_list.append(entity["id"])
            else:
                # otherwise append all the entities
                guid_list.append(entity["id"])
        entity_res = [] if guid_list is None else self.purview_client.get_entity(
            guid=guid_list)["entities"]
        return entity_res
