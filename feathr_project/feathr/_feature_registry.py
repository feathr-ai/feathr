import glob
import importlib
import os
import sys
from pathlib import Path
from typing import List, Optional, Union

from pyapacheatlas.core import PurviewClient, AtlasClassification, AtlasEntity, AtlasProcess


from jinja2 import Template
from loguru import logger
from numpy import deprecate
from pyapacheatlas.auth import ServicePrincipalAuthentication
from pyapacheatlas.core import (AtlasEntity, AtlasProcess, PurviewClient,
                                TypeCategory)
from pyapacheatlas.core.typedef import (AtlasAttributeDef, Cardinality,
                                        EntityTypeDef, RelationshipTypeDef, AtlasRelationshipEndDef)
from pyapacheatlas.core.util import GuidTracker
from pyhocon import ConfigFactory
from urllib.parse import urlparse
from feathr._envvariableutil import _EnvVaraibleUtil
from feathr._file_utils import write_to_file
from feathr.anchor import FeatureAnchor
from feathr.constants import *
from feathr.feature import Feature
from feathr.feature_derivations import DerivedFeature
from feathr.repo_definitions import RepoDefinitions
from feathr.source import HdfsSource, InputContext, Source
from feathr.transformation import Transformation


class _FeatureRegistry():

    def __init__(self, config_path):
        """
        Initializes the feature registry, doing the following:
        - Use an Azure Service Principal to communicate with Azure Purview
        - Initialize an Azure Purview Client
        - Initialize the GUID tracker, project name, etc.
        """
        envutils = _EnvVaraibleUtil(config_path)
        self.project_name = envutils.get_environment_variable_with_default(
            'project_config', 'project_name')
        self.FEATURE_REGISTRY_DELIMITER = envutils.get_environment_variable_with_default(
            'feature_registry', 'purview', 'delimiter')
        self.azure_purview_name = envutils.get_environment_variable_with_default(
            'feature_registry', 'purview', 'purview_name')

        # only initialize all the purview client etc. when the name is set. This will enable more pluggable reigstry in the future.
        if self.azure_purview_name:
            self.oauth = ServicePrincipalAuthentication(
                tenant_id=_EnvVaraibleUtil.get_environment_variable(
                    "AZURE_TENANT_ID"),
                client_id=_EnvVaraibleUtil.get_environment_variable(
                    "AZURE_CLIENT_ID"),
                client_secret=_EnvVaraibleUtil.get_environment_variable(
                    "AZURE_CLIENT_SECRET")
            )
            self.purview_client = PurviewClient(
                account_name=self.azure_purview_name,
                authentication=self.oauth
            )

        self.guid = GuidTracker(starting=-1000)
        self.entity_batch_queue = []
        # for searching in derived features
        self.global_feature_entity_dict = {}

    def _register_feathr_feature_types(self):
        """
        Register the feathr types if we haven't done so. Note that this only needs to be called once per provisioning
        a system. Basically this function registers all the feature type definition in a Atlas compatible system.
        """

        # Each feature is registered under a certain Feathr project. The project should what we refer to, however for backward compatibility, the type name would be `feathr_workspace`
        type_feathr_project = EntityTypeDef(
            name=FEATHR_PROJECT,
            typeVersion=REGISTRY_VERSION,
            attributeDefs=[
                AtlasAttributeDef(
                    name="anchor_features", typeName=ARRAY_ANCHOR, cardinality=Cardinality.SET),
                AtlasAttributeDef(
                    name="derived_features", typeName=ARRAY_DERIVED_FEATURE, cardinality=Cardinality.SET),

                # Below are for backward compatibility. DO NOT USE.
                # AtlasAttributeDef(name="raw_hocon_feature_definition_config",
                #                   typeName="string", cardinality=Cardinality.SINGLE),
                # AtlasAttributeDef(name="raw_hocon_feature_join_config",
                #                   typeName="string", cardinality=Cardinality.SINGLE),
                # AtlasAttributeDef(name="raw_hocon_feature_generation_config",
                #                   typeName="string", cardinality=Cardinality.SINGLE),
                # AtlasAttributeDef(
                #     name="derivations", typeName=ARRAY_DERIVED_FEATURE, cardinality=Cardinality.SET),
            ],
            superTypes=["DataSet"],

        )
        type_feathr_sources = EntityTypeDef(
            name=SOURCE,
            typeVersion=REGISTRY_VERSION,
            attributeDefs=[

                AtlasAttributeDef(
                    name="location", typeName="string", cardinality=Cardinality.SINGLE),
                AtlasAttributeDef(name="timestampColumn",
                                  typeName="string", cardinality=Cardinality.SINGLE),
                AtlasAttributeDef(name="timestamp_format",
                                  typeName="string", cardinality=Cardinality.SINGLE),
                AtlasAttributeDef(name="type", typeName="string",
                                  cardinality=Cardinality.SINGLE),
                # Below are for backward compatibility. DO NOT USE.

                # AtlasAttributeDef(name="isTimeSeries",
                #                   typeName="boolean", cardinality=Cardinality.SINGLE),
                # AtlasAttributeDef(name="timestamp",
                #                   typeName="string", cardinality=Cardinality.SINGLE),

                # AtlasAttributeDef(name="timestampColumnFormat",
                #                   typeName="string", cardinality=Cardinality.SINGLE),

            ],
            superTypes=["DataSet"],
        )

        type_feathr_anchor_features = EntityTypeDef(
            name=ANCHOR_FEATURE,
            typeVersion=REGISTRY_VERSION,
            attributeDefs=[

                AtlasAttributeDef(name="type", typeName="string",
                                  cardinality=Cardinality.SINGLE),
                AtlasAttributeDef(name="key", typeName="array<map<string,string>>",
                                  cardinality=Cardinality.SET),
                AtlasAttributeDef(name="transformation", typeName="string",
                                  cardinality=Cardinality.SINGLE),

                # Below are for backward compatibility. DO NOT USE.
                # AtlasAttributeDef(name="tags", typeName="map<string,string>",
                #                   cardinality=Cardinality.SINGLE),
                
            ],
            superTypes=["DataSet"],
        )

        type_feathr_derived_features = EntityTypeDef(
            name=DERIVED_FEATURE,
            typeVersion=REGISTRY_VERSION,
            attributeDefs=[
                AtlasAttributeDef(name="type", typeName="string",
                                  cardinality=Cardinality.SINGLE),

                # currently only registering derived features on anchor features.
                # TODO: support derived features on other derived features
                AtlasAttributeDef(name="input_features", typeName=ARRAY_ANCHOR_FEATURE,
                                  cardinality=Cardinality.SET),
                AtlasAttributeDef(name="key", typeName="array<map<string,string>>",
                                  cardinality=Cardinality.SET),
                AtlasAttributeDef(name="transformation", typeName="string",
                                  cardinality=Cardinality.SINGLE),

                # Below are for backward compatibility. DO NOT USE.
                # AtlasAttributeDef(name="definition",
                #                   typeName="string", cardinality=Cardinality.SINGLE),

                # AtlasAttributeDef(name="tensorCategory",
                #                   typeName="string", cardinality=Cardinality.SINGLE),
                # AtlasAttributeDef(name="dimensionType",
                #                   typeName="string", cardinality=Cardinality.SINGLE),
                # AtlasAttributeDef(
                #     name="valType", typeName="string", cardinality=Cardinality.SINGLE),
            ],
            superTypes=["DataSet"],
            # display it in the purview UI
            options={"schemaElementsAttribute": ANCHOR_FEATURE}
        )

        type_feathr_anchors = EntityTypeDef(
            name=ANCHOR,
            typeVersion=REGISTRY_VERSION,
            attributeDefs=[
                AtlasAttributeDef(
                    name="source", typeName=SOURCE, cardinality=Cardinality.SINGLE),
                AtlasAttributeDef(
                    name="features", typeName=ARRAY_ANCHOR_FEATURE, cardinality=Cardinality.SET),
                AtlasAttributeDef(name="preprocessing", typeName="string",
                                  cardinality=Cardinality.SINGLE),

                # Below are for backward compatibility. DO NOT USE.
                # AtlasAttributeDef(name="key", typeName="string",
                #                   cardinality=Cardinality.SINGLE),
                # AtlasAttributeDef(name="extractor",
                #                   typeName="string", cardinality=Cardinality.SINGLE),
                # AtlasAttributeDef(name="key.sqlExpr",
                #                   typeName="string", cardinality=Cardinality.SINGLE),
                # AtlasAttributeDef(name="timestampColumn",
                #                   typeName="string", cardinality=Cardinality.SINGLE),
                # AtlasAttributeDef(name="timestampColumnFormat",
                #                   typeName="string", cardinality=Cardinality.SINGLE),
                # AtlasAttributeDef(
                #     name="location", typeName="string", cardinality=Cardinality.SINGLE),
            ],
            superTypes=["DataSet"],
            # display it in the purview UI
            options={"schemaElementsAttribute": ANCHOR_FEATURE}
        )

        # define the relationships in the type system
        # see more samples here: https://github.com/wjohnson/pyapacheatlas/blob/master/samples/CRUD/create_type_with_schema.py
        project_to_derived_feature_relationship = RelationshipTypeDef(
            name=PROJECT_TO_DERIVED_FEATURE,
            relationshipCategory="COMPOSITION",
            # we are using SET below so should set this to True (i.e. a project can have multiple derived features)
            endDef1= AtlasRelationshipEndDef(name=FEATHR_PROJECT, typeName=FEATHR_PROJECT, isContainer=True,cardinality=Cardinality.SET),
            endDef2=AtlasRelationshipEndDef(name=DERIVED_FEATURE, typeName=DERIVED_FEATURE, isContainer=False,cardinality=Cardinality.SINGLE),
        )
        project_to_anchor_relationship = RelationshipTypeDef(
            name=PROJECT_TO_ANCHOR,
            relationshipCategory="COMPOSITION",
            endDef1=AtlasRelationshipEndDef(name=FEATHR_PROJECT, typeName=FEATHR_PROJECT, isContainer=True,cardinality=Cardinality.SET),
            endDef2=AtlasRelationshipEndDef(name=ANCHOR, typeName=ANCHOR, isContainer=False,cardinality=Cardinality.SINGLE),
        )

        anchor_to_source_relationship = RelationshipTypeDef(
            name=ANCHOR_TO_SOURCE,
            relationshipCategory="COMPOSITION",
            endDef1=AtlasRelationshipEndDef(name=ANCHOR, typeName=ANCHOR, isContainer=True,cardinality=Cardinality.SET),
            endDef2=AtlasRelationshipEndDef(name=SOURCE, typeName=SOURCE, isContainer=False,cardinality=Cardinality.SINGLE),
        )

        anchor_to_feature_relationship = RelationshipTypeDef(
            name=ANCHOR_TO_FEATURE,
            relationshipCategory="COMPOSITION",
            endDef1=AtlasRelationshipEndDef(name=ANCHOR, typeName=ANCHOR, isContainer=True,cardinality=Cardinality.SET),
            endDef2=AtlasRelationshipEndDef(name=ANCHOR_FEATURE, typeName=ANCHOR_FEATURE, isContainer=False,cardinality=Cardinality.SINGLE),
        )
        #TODO: currently only supports derived feature to anchor feature. derived feature to derived feature will be supported in the future.
        derived_feature_to_feature = RelationshipTypeDef(
            name=DERIVED_FEATURE_TO_FEATURE,
            relationshipCategory="COMPOSITION",
            endDef1=AtlasRelationshipEndDef(name=DERIVED_FEATURE, typeName=DERIVED_FEATURE, isContainer=True,cardinality=Cardinality.SET),
            endDef2=AtlasRelationshipEndDef(name=ANCHOR_FEATURE, typeName=ANCHOR_FEATURE, isContainer=False,cardinality=Cardinality.SINGLE),
        )

        def_result = self.purview_client.upload_typedefs(
            entityDefs=[type_feathr_anchor_features, type_feathr_anchors,
                        type_feathr_derived_features, type_feathr_sources, type_feathr_project],
            relationshipDefs=[project_to_anchor_relationship, project_to_derived_feature_relationship,anchor_to_source_relationship,anchor_to_feature_relationship,derived_feature_to_feature],
            force_update=True)
        logger.info("Feathr Feature Type System Initialized.")

    def _parse_anchor_features(self, anchor: FeatureAnchor) -> List[AtlasEntity]:
        """
        This function will parse the anchor features inside an anchor
        """

        anchor_feature_batch = []
        anchor_feature: Feature  # annotate type
        for anchor_feature in anchor.features:
            key_list = []
            for individual_key in anchor_feature.key:
                key_dict={"key_column":individual_key.key_column, "key_column_type":individual_key.key_column_type.to_feature_config(),"full_name":individual_key.full_name, "description":individual_key.description, "key_column_alias":individual_key.key_column_alias}
                key_list.append(key_dict)

            anchor_feature_entity = AtlasEntity(
                name=anchor_feature.name,
                qualified_name=self.project_name + self.FEATURE_REGISTRY_DELIMITER +
                anchor.name + self.FEATURE_REGISTRY_DELIMITER + anchor_feature.name,
                attributes={
                    "type": anchor_feature.feature_type.to_feature_config(),
                    # TODO: need to think about this for keys
                    "key": key_list,
                    "transformation": anchor_feature.transform.to_feature_config(),
                    "tags":anchor_feature.registry_tags,
                },
                typeName=ANCHOR_FEATURE,
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
        for anchor_entity in anchor_list:
            # First, parse all the features in this anchor
            anchor_feature_batch = self._parse_anchor_features(anchor_entity)
            # then parse the source of that anchor
            source = self._parse_source(anchor_entity.source)

            anchor_entity = AtlasEntity(
                name=anchor_entity.name,
                qualified_name=self.project_name + self.FEATURE_REGISTRY_DELIMITER + anchor_entity.name,
                attributes={
                    "source": source.to_json(minimum=True),
                    "features": [s.to_json(minimum=True) for s in anchor_feature_batch],
                    # "preprocessing": "TODO",
                },
                typeName=ANCHOR,
                
                guid=self.guid.get_guid(),
            )
            # anchor_entity.addCustomAttribute({"abccccc":"dddddddddddddddddd"})
            # add feature lineage between anchor and feature
            for individual_feature_entity in anchor_feature_batch:
                lineage = AtlasProcess(
                    name=individual_feature_entity.name + " to " + anchor_entity.name,
                    typeName="Process",
                    qualified_name=self.FEATURE_REGISTRY_DELIMITER + "PROCESS" + self.FEATURE_REGISTRY_DELIMITER + self.project_name +
                    self.FEATURE_REGISTRY_DELIMITER + anchor_entity.name + self.FEATURE_REGISTRY_DELIMITER +
                    individual_feature_entity.name,
                    inputs=[individual_feature_entity],
                    outputs=[anchor_entity],
                    guid=self.guid.get_guid(),
                )
                self.entity_batch_queue.append(lineage)
            
            # add lineage between anchor and source
            anchor_source_lineage = AtlasProcess(
                name=source.name + " to " + anchor_entity.name,
                typeName="Process",
                qualified_name=self.FEATURE_REGISTRY_DELIMITER + "PROCESS" + self.FEATURE_REGISTRY_DELIMITER + self.project_name +
                self.FEATURE_REGISTRY_DELIMITER + anchor_entity.name + self.FEATURE_REGISTRY_DELIMITER +
                source.name,
                inputs=[source],
                outputs=[anchor_entity],
                guid=self.guid.get_guid(),
            )
            self.entity_batch_queue.append(anchor_source_lineage)

            anchors_batch.append(anchor_entity)
        return anchors_batch

    def _parse_source(self, source: Union[Source, HdfsSource]) -> AtlasEntity:
        """
        parse the `sources` section of the feature configuration
        """
        input_context=False
        if isinstance(source, InputContext):
            input_context=True
    
        source_entity = AtlasEntity(
            name=source.name,
            qualified_name=self.project_name + self.FEATURE_REGISTRY_DELIMITER + source.name,
            attributes={
                "type": INPUT_CONTEXT if input_context else urlparse(source.path).scheme,
                "location": INPUT_CONTEXT if input_context else source.path,
                "timestamp_format": source.timestamp_format,
                "timestampColumn": source.event_timestamp_column,
            },
            typeName=SOURCE,
            guid=self.guid.get_guid(),
        )
        self.entity_batch_queue.append(source_entity)
        return source_entity

    def _parse_derived_features(self, derived_features: List[DerivedFeature]) -> List[AtlasEntity]:
        derivation_entities = []

        for derived_feature in derived_features:
            # get the corresponding Atlas entity by searching feature name
            input_feature_entity_list: List[AtlasEntity] = [
                self.global_feature_entity_dict[f.name] for f in derived_feature.input_features]
            key_list = []
            for individual_key in derived_feature.key:
                key_dict={"key_column":individual_key.key_column, "key_column_type":individual_key.key_column_type.to_feature_config(),"full_name":individual_key.full_name, "description":individual_key.description, "key_column_alias":individual_key.key_column_alias}
                key_list.append(key_dict)
            derived_feature = AtlasEntity(
                name=derived_feature.name,
                qualified_name=self.project_name +
                self.FEATURE_REGISTRY_DELIMITER + derived_feature.name,
                attributes={
                    # TODO: parse the string
                    "type": derived_feature.feature_type.to_feature_config(),
                    "key": key_list,
                    "input_features": [f.to_json(minimum=True) for f in input_feature_entity_list],
                    "transformation": derived_feature.transform.to_feature_config(),
                    
                },
                version=5,
                typeName=DERIVED_FEATURE,
                guid=self.guid.get_guid(),
            )
            

            for input_feature_entity in input_feature_entity_list:
                # add lineage between anchor feature and derived feature
                derived_feature_feature_lineage = AtlasProcess(
                    name=input_feature_entity.name + " to " + derived_feature.name,
                    typeName="Process",
                    qualified_name=self.FEATURE_REGISTRY_DELIMITER + "PROCESS" + self.FEATURE_REGISTRY_DELIMITER + self.project_name +
                    self.FEATURE_REGISTRY_DELIMITER + derived_feature.name + self.FEATURE_REGISTRY_DELIMITER +
                    input_feature_entity.name,
                    inputs=[input_feature_entity],
                    outputs=[derived_feature],
                    guid=self.guid.get_guid(),
                )
                self.entity_batch_queue.append(derived_feature_feature_lineage)


            self.entity_batch_queue.append(derived_feature)
            derivation_entities.append(derived_feature)
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

        attributes = {"anchor_features": [
            s.to_json(minimum=True) for s in anchor_entities]}
        # add derived feature if it's there
        if derived_feature_list:
            derived_feature_entities = self._parse_derived_features(
                derived_feature_list)
            attributes["derived_features"] = [
                s.to_json(minimum=True) for s in derived_feature_entities]
        
        # define project in Atlas entity
        feathr_project_entity = AtlasEntity(
            name=self.project_name,
            qualified_name=self.project_name,
            attributes=attributes,
            typeName=FEATHR_PROJECT,
            guid=self.guid.get_guid(),
        )
        
        # add lineage from anchor to project
        for individual_anchor_entity in anchor_entities:
            individual_anchor_entity.addRelationship(**{FEATHR_PROJECT:feathr_project_entity})

            lineage_process = AtlasProcess(
                name=individual_anchor_entity.name + " to " + self.project_name,
                typeName="Process",
                # fqdn: PROCESS+PROJECT_NAME+ANCHOR_NAME
                qualified_name=self.FEATURE_REGISTRY_DELIMITER + "PROCESS" + self.FEATURE_REGISTRY_DELIMITER + self.project_name +
                self.FEATURE_REGISTRY_DELIMITER + individual_anchor_entity.name,
                inputs=[individual_anchor_entity],
                outputs=[feathr_project_entity],
                guid=self.guid.get_guid(),
            )
            self.entity_batch_queue.append(lineage_process)

        # add lineage from derivation to project
        for derived_feature_entity in derived_feature_entities:
            derived_feature_entity.addRelationship(**{FEATHR_PROJECT:feathr_project_entity})

            lineage_process = AtlasProcess(
                name=derived_feature_entity.name + " to " + self.project_name,
                typeName="Process",
                # fqdn: PROCESS+PROJECT_NAME+DERIVATION_NAME
                qualified_name=self.FEATURE_REGISTRY_DELIMITER + "PROCESS" + self.FEATURE_REGISTRY_DELIMITER + self.project_name +
                self.FEATURE_REGISTRY_DELIMITER + derived_feature_entity.name,
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
        self._save_request_feature_config(
            repo_definitions, local_workspace_dir)
        self._save_anchored_feature_config(
            repo_definitions, local_workspace_dir)
        self._save_derived_feature_config(
            repo_definitions, local_workspace_dir)

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
        """Register Features for the specified workspace

        Args:
            workspace_path (str, optional): path to a workspace. Defaults to None.
        """

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

    def _delete_all_feathr_entities(self):
        """
        Delete all the entities for feathr registry. Only use it for non-production use case, as it will delete all the feathr related entities.
        """
        pass

    def get_registry_client(self):
        """
        Return a client object and users can operate more on it (like doing search)
        """
        return self.purview_client

    def list_registered_features(self, project_name: str = None) -> List[str]:
        """
        List all the already registered features. If project_name is not provided or is None, it will return all the
        registered features; otherwise it will only return only features under this project
        """
        entities = self.purview_client.discovery.search_entities(
            f"entityType:{ANCHOR_FEATURE} or entityType:{DERIVED_FEATURE}")
        feature_list = []
        for entity in entities:
            # Important properties returned includes:
            # id (the guid of the entity), name, qualifiedName, @search.score,
            # and @search.highlights

            if project_name:
                # if project_name is a valid string, only append entities if the qualified name start with
                # project_name+delimiter
                if entity["qualifiedName"].startswith(project_name+self.FEATURE_REGISTRY_DELIMITER):
                    feature_list.append(entity["name"])
                    entity_res = self.purview_client.get_entity(guid=entity['id'])
                    # print(entity_res[1]["version"])
                    for entity_result in entity_res:
                        print(entity_result)
                        print(entity_res[entity_result])
                        # exit(0)
            else:
                # otherwise append all the entities
                feature_list.append(entity["name"])
                

        return feature_list

    def get_features_from_registry(self, project_name: str, workspace_path: str):
        """[Sync Features from registry to local workspace, given a project_name, will write project's features from registry to to user's local workspace]
        Args:
            project_name (str): project name.
            workspace_path (str): path to a workspace.
        """

        entities = self.purview_client.get_entity(qualifiedName=project_name,
                                                  typeName=FEATHR_PROJECT)
        # TODO - Change implementation to support traversing the workspace and construct the file, item by item
        # We don't support modifying features outside of registring this should be fine.

        # Read the three config files from raw hocon field
        feature_conf_content = entities["entities"][0]["attributes"]["raw_hocon_feature_definition_config"]
        feature_join_conf_content = entities["entities"][0]["attributes"]["raw_hocon_feature_join_config"]
        feature_gen_conf_content = entities["entities"][0]["attributes"]["raw_hocon_feature_generation_config"]

        # Define the filenames for each config
        feature_conf_file = os.path.join(
            workspace_path, "feature_conf", "features.conf")
        feature_join_file = os.path.join(
            workspace_path, "feature_join_conf", "feature_join.conf")
        feature_gen_file = os.path.join(
            workspace_path, "feature_gen_conf", "feature_gen.conf")

        # Create file and directory, if does not exist
        os.makedirs(os.path.dirname(feature_conf_file), exist_ok=True)
        os.makedirs(os.path.dirname(feature_join_file), exist_ok=True)
        os.makedirs(os.path.dirname(feature_gen_file), exist_ok=True)

        with open(feature_conf_file, "w") as features:
            features.write(feature_conf_content)
        logger.info(
            "Writing feature configuration from feathr registry to {}", feature_conf_file)

        with open(feature_join_file, "w") as offline_config:
            offline_config.write(feature_join_conf_content)
        logger.info(
            "Writing offline configuration from feathr registry to {}", feature_join_file)

        with open(feature_gen_file, "w") as online_config:
            online_config.write(feature_gen_conf_content)
        logger.info(
            "Writing online configuration from feathr registry to {}", feature_gen_file)
