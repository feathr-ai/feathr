import glob
import importlib
import os
import sys
from pathlib import Path
from typing import List, Optional

from jinja2 import Template
from loguru import logger
from pyapacheatlas.auth import ServicePrincipalAuthentication
from pyapacheatlas.core import (AtlasEntity, AtlasProcess, PurviewClient,
                                TypeCategory)
from pyapacheatlas.core.typedef import (AtlasAttributeDef, EntityTypeDef,
                                        RelationshipTypeDef)
from pyapacheatlas.core.util import GuidTracker
from pyexpat import features
from pyhocon import ConfigFactory

from feathr._envvariableutil import _EnvVaraibleUtil
from feathr._file_utils import write_to_file
from feathr.anchor import FeatureAnchor
from feathr.feature import Feature
from feathr.feature_derivations import DerivedFeature
from feathr.repo_definitions import RepoDefinitions
from feathr.source import Source
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
        self.project_name = envutils.get_environment_variable_with_default('project_config', 'project_name')
        self.FEATURE_REGISTRY_DELIMITER = envutils.get_environment_variable_with_default('feature_registry', 'purview', 'delimiter')
        self.azure_purview_name = envutils.get_environment_variable_with_default('feature_registry', 'purview', 'purview_name')

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

        

    def _register_feathr_feature_types(self):
        """
        Register the feathr types if we haven't done so. Note that this only needs to be called once per provisioning
        a system. Basically this function registers all the feature type definition in a Atlas compatible system.
        """

        # COMMAND ----------

        # Set up a few types and relationships
        # This is a one time thing but necessary to make the demo work
        # It also demonstrates how you can capture different attributes
        # for your dataframes, dataframe columns, and jobs.

        type_feathr_features = EntityTypeDef(
            name="feathr_workspace",
            attributeDefs=[
                AtlasAttributeDef(
                    name="sources", typeName="array<feathr_source>", cardinality="SET"),
                AtlasAttributeDef(
                    name="anchors", typeName="array<feathr_anchor>", cardinality="SET"),
                AtlasAttributeDef(
                    name="derivations", typeName="array<feathr_derivation>", cardinality="SET"),
                AtlasAttributeDef(name="raw_hocon_feature_definition_config",
                                  typeName="string", cardinality="SINGLE"),
                AtlasAttributeDef(name="raw_hocon_feature_join_config",
                                  typeName="string", cardinality="SINGLE"),
                AtlasAttributeDef(name="raw_hocon_feature_generation_config",
                                  typeName="string", cardinality="SINGLE"),

            ],
            superTypes=["DataSet"],
            serviceType="Feathr Workspace"
        )
        type_feathr_sources = EntityTypeDef(
            name="feathr_source",
            attributeDefs=[
                AtlasAttributeDef(name="isTimeSeries",
                                  typeName="boolean", cardinality="SINGLE"),
                AtlasAttributeDef(name="type", typeName="string",
                                  cardinality="SINGLE"),
                AtlasAttributeDef(
                    name="location", typeName="string", cardinality="SINGLE"),
                AtlasAttributeDef(name="timestamp",
                                  typeName="string", cardinality="SINGLE"),
                AtlasAttributeDef(name="timestamp_format",
                                  typeName="string", cardinality="SINGLE"),
                AtlasAttributeDef(name="timestampColumn",
                                  typeName="string", cardinality="SINGLE"),
                AtlasAttributeDef(name="timestampColumnFormat",
                                  typeName="string", cardinality="SINGLE"),
            ],
            superTypes=["DataSet"],
            serviceType="Feathr Feature Source"
        )

        type_feathr_derivations = EntityTypeDef(
            name="feathr_derivation",
            attributeDefs=[
                AtlasAttributeDef(name="key", typeName="string",
                                  cardinality="SINGLE"),
                AtlasAttributeDef(name="inputs", typeName="string",
                                  cardinality="SINGLE"),
                AtlasAttributeDef(name="definition",
                                  typeName="string", cardinality="SINGLE"),
                AtlasAttributeDef(name="type", typeName="string",
                                  cardinality="SINGLE"),
                AtlasAttributeDef(name="tensorCategory",
                                  typeName="string", cardinality="SINGLE"),
                AtlasAttributeDef(name="dimensionType",
                                  typeName="string", cardinality="SINGLE"),
                AtlasAttributeDef(
                    name="valType", typeName="string", cardinality="SINGLE"),
            ],
            superTypes=["DataSet"],
            serviceType="Feathr Feature Derivation"
        )

        type_feathr_anchors = EntityTypeDef(
            name="feathr_anchor",
            attributeDefs=[
                AtlasAttributeDef(
                    name="source", typeName="string", cardinality="SINGLE"),
                AtlasAttributeDef(name="extractor",
                                  typeName="string", cardinality="SINGLE"),
                AtlasAttributeDef(
                    name="features", typeName="array<feathr_anchor_feature>", cardinality="SET"),
                AtlasAttributeDef(name="key", typeName="string",
                                  cardinality="SINGLE"),
                AtlasAttributeDef(name="key.sqlExpr",
                                  typeName="string", cardinality="SINGLE"),
                AtlasAttributeDef(name="timestampColumn",
                                  typeName="string", cardinality="SINGLE"),
                AtlasAttributeDef(name="timestampColumnFormat",
                                  typeName="string", cardinality="SINGLE"),
                AtlasAttributeDef(
                    name="location", typeName="string", cardinality="SINGLE"),
            ],
            superTypes=["DataSet"],
            serviceType="Feathr Anchors"
        )

        type_feathr_anchor_features = EntityTypeDef(
            name="feathr_anchor_feature",
            attributeDefs=[
                AtlasAttributeDef(name="def", typeName="string",
                                  cardinality="SINGLE"),
                AtlasAttributeDef(name="aggregation",
                                  typeName="string", cardinality="SINGLE"),
                AtlasAttributeDef(name="window", typeName="string",
                                  cardinality="SINGLE"),
                AtlasAttributeDef(
                    name="groupBy", typeName="string", cardinality="SINGLE"),
                AtlasAttributeDef(
                    name="default", typeName="string", cardinality="SINGLE"),
                AtlasAttributeDef(name="def.sqlExpr",
                                  typeName="string", cardinality="SINGLE"),
                AtlasAttributeDef(name="type", typeName="string",
                                  cardinality="SINGLE"),
                AtlasAttributeDef(name="tensorCategory",
                                  typeName="string", cardinality="SINGLE"),
                AtlasAttributeDef(name="dimensionType",
                                  typeName="string", cardinality="SINGLE"),
                AtlasAttributeDef(
                    name="valType", typeName="string", cardinality="SINGLE"),
            ],
            superTypes=["DataSet"],
            serviceType="Feathr Feature"
        )

        feathr_source_to_feature_relationship = RelationshipTypeDef(
            name="feathr_source_to_feature_relationship",
            relationshipCategory="COMPOSITION",
            endDef1={
                "type": "feathr_workspace",
                "name": "feathr_feature",
                "isContainer": True,
                "cardinality": "SET",
                "isLegacyAttribute": False
            },
            endDef2={
                "type": "feathr_source",
                "name": "feathr_source",
                "isContainer": False,
                "cardinality": "SINGLE",
                "isLegacyAttribute": False
            }
        )

        feathr_anchor_to_feature_relationship = RelationshipTypeDef(
            name="feathr_anchor_to_feature_relationship",
            relationshipCategory="COMPOSITION",
            endDef1={
                "type": "feathr_workspace",
                "name": "feathr_feature",
                "isContainer": True,
                "cardinality": "SET",
                "isLegacyAttribute": False
            },
            endDef2={
                "type": "feathr_anchor",
                "name": "feathr_anchor",
                "isContainer": False,
                "cardinality": "SINGLE",
                "isLegacyAttribute": False
            }
        )

        feathr_derivation_to_feature_relationship = RelationshipTypeDef(
            name="feathr_derivation_to_feature_relationship",
            relationshipCategory="COMPOSITION",
            endDef1={
                "type": "feathr_workspace",
                "name": "feathr_feature",
                "isContainer": True,
                "cardinality": "SET",
                "isLegacyAttribute": False
            },
            endDef2={
                "type": "feathr_derivation",
                "name": "feathr_derivation",
                "isContainer": False,
                "cardinality": "SINGLE",
                "isLegacyAttribute": False
            }
        )

        typedef_results = self.purview_client.upload_typedefs(
            entityDefs=[type_feathr_anchor_features, type_feathr_anchors,
                        type_feathr_derivations, type_feathr_sources, type_feathr_features],
            relationshipDefs=[feathr_source_to_feature_relationship,
                              feathr_anchor_to_feature_relationship, feathr_derivation_to_feature_relationship],
            force_update=True)
        logger.info("Feature Type System Initialized.")

    def _parse_anchor_features(self, anchor_name: str) -> List[AtlasEntity]:
        """
        This function will parse the actual features inside an anchor

        Args:
            anchor_name (str): name of the anchor

        Returns:
            list[AtlasEntity]: a list of the parsed actual features inside an anchor definition
        """
        anchor_feature_batch = []
        for anchor_feature_name in self.feathr_feature_config["anchors"][anchor_name]["features"]:
            # there will be cases where we don't have a "def" field in this hocon file. First we detect whether it
            # has a "def" field. If the anchor feature does have this field, we will use this field directly otherwise
            res = self.feathr_feature_config["anchors"][anchor_name]["features"][anchor_feature_name]
            if isinstance(res, str):
                anchor_feature = AtlasEntity(
                    name=anchor_feature_name,
                    qualified_name=self.project_name + self.FEATURE_REGISTRY_DELIMITER +
                    anchor_name + self.FEATURE_REGISTRY_DELIMITER + anchor_feature_name,
                    attributes={
                        "def": res,
                    },
                    typeName="feathr_anchor_feature",
                    guid=self.guid.get_guid(),
                )
            else:
                feature_defstr = self.feathr_feature_config["anchors"][
                    anchor_name]["features"][anchor_feature_name]
                anchor_feature = AtlasEntity(
                    name=anchor_feature_name,
                    qualified_name=self.project_name + self.FEATURE_REGISTRY_DELIMITER +
                    anchor_name + self.FEATURE_REGISTRY_DELIMITER + anchor_feature_name,
                    attributes={
                        "def": str(feature_defstr),
                        "aggregation": self.feathr_feature_config["anchors"][anchor_name]["features"][anchor_feature_name].get_string("aggregation", ""),
                        "window": self.feathr_feature_config["anchors"][anchor_name]["features"][anchor_feature_name].get_string("window", ""),
                        "groupBy": self.feathr_feature_config["anchors"][anchor_name]["features"][anchor_feature_name].get_string("groupBy", ""),
                        "default": self.feathr_feature_config["anchors"][anchor_name]["features"][anchor_feature_name].get_string("default", ""),
                        "def.sqlExpr": self.feathr_feature_config["anchors"][anchor_name]["features"][anchor_feature_name].get_string("def.sqlExpr", ""),
                        "type": self.feathr_feature_config["anchors"][anchor_name]["features"][anchor_feature_name].get_string("type", ""),
                        "tensorCategory": self.feathr_feature_config["anchors"][anchor_name]["features"][anchor_feature_name].get_string("tensorCategory", ""),
                        "dimensionType": self.feathr_feature_config["anchors"][anchor_name]["features"][anchor_feature_name].get_string("dimensionType", ""),
                        "valType": self.feathr_feature_config["anchors"][anchor_name]["features"][anchor_feature_name].get_string("valType", ""),
                    },
                    typeName="feathr_anchor_feature",
                    guid=self.guid.get_guid(),
                )

            self.entity_batch_queue.append(anchor_feature)
            anchor_feature_batch.append(anchor_feature)
        return anchor_feature_batch

    def _parse_anchors(self, feathr_anchors: str) -> List[AtlasEntity]:
        """
        parse the actual features inside an anchor

        Args:
            feathr_anchors (str): name of the feathr anchor

        Returns:
            list[AtlasEntity]: a list of the parsed anchors. Note that this is not the actual anchor feature.
        """

        anchors_batch = []

        # note that we want to put anchor_feature_batch in the same level as anchors_batch
        # because we want to make sure all the anchor features are stored and captured globally

        for i, anchor_name in enumerate(feathr_anchors):
            # First, parse all the features in this anchor
            anchor_feature_batch = self._parse_anchor_features(anchor_name)

            # Second, get all the other attributes from the anchors
            anchor = AtlasEntity(
                name=anchor_name,
                qualified_name=self.project_name + self.FEATURE_REGISTRY_DELIMITER + anchor_name,
                attributes={
                    "source": str(self.feathr_feature_config["anchors"][anchor_name]["source"]),
                    "key": str(self.feathr_feature_config["anchors"][anchor_name]["key"]),
                    "features": [s.to_json(minimum=True) for s in anchor_feature_batch],
                    "extractor": str(self.feathr_feature_config["anchors"][anchor_name].get_string("extractor", "")),
                    "key.sqlExpr": str(self.feathr_feature_config["anchors"][anchor_name].get_string("key.sqlExpr", "")),
                    "timestampColumn": str(self.feathr_feature_config["anchors"][anchor_name].get_string("timestampColumn", "")),
                    "timestampColumnFormat": str(self.feathr_feature_config["anchors"][anchor_name].get_string("timestampColumnFormat", "")),
                    "location": str(self.feathr_feature_config["anchors"][anchor_name].get_string("location", "")),
                },
                typeName="feathr_anchor",
                guid=self.guid.get_guid(),
            )

            # add Atlas Process for feature lineage
            for individual_anchor_feature in anchor_feature_batch:
                lineage_process = AtlasProcess(
                    name=individual_anchor_feature.name + " to " + anchor_name,
                    typeName="Process",
                    qualified_name=self.FEATURE_REGISTRY_DELIMITER + "PROCESS" + self.FEATURE_REGISTRY_DELIMITER + self.project_name +
                    self.FEATURE_REGISTRY_DELIMITER + anchor_name + self.FEATURE_REGISTRY_DELIMITER +
                    individual_anchor_feature.name,
                    inputs=[individual_anchor_feature],
                    outputs=[anchor],
                    guid=self.guid.get_guid(),
                )
                self.entity_batch_queue.append(lineage_process)

            self.entity_batch_queue.append(anchor)
            anchors_batch.append(anchor)
        return anchors_batch

    def _parse_sources(self, feathr_sources: str) -> List[AtlasEntity]:
        """
        parse the `sources` section of the feature configuration

        Args:
            feathr_sources (str): the name of the source

        Returns:
            list[AtlasEntity]: list of the parsed sources
        """
        sources_batch = []
        for i, source_name in enumerate(feathr_sources):

            source = AtlasEntity(
                name=source_name,
                qualified_name=self.project_name + self.FEATURE_REGISTRY_DELIMITER + source_name,
                attributes={
                    "location": str(self.feathr_feature_config["sources"][source_name]["location"]["path"]),
                    "type": str(self.feathr_feature_config["sources"][source_name].get_string("type", "")),
                    "isTimeSeries": bool(self.feathr_feature_config["sources"][source_name].get_string("isTimeSeries", "")),
                    "timestamp": str(self.feathr_feature_config["sources"][source_name].get_string("timeWindowParameters.timestamp", "")),
                    "timestamp_format": str(self.feathr_feature_config["sources"][source_name].get_string("timeWindowParameters.timestamp_format", "")),
                    "timestampColumn": str(self.feathr_feature_config["sources"][source_name].get_string("timeWindowParameters.timestampColumn", "")),
                    "timestampColumnFormat": str(self.feathr_feature_config["sources"][source_name].get_string("timeWindowParameters.timestampColumnFormat", ""))
                },
                typeName="feathr_source",
                guid=self.guid.get_guid(),
            )
            self.entity_batch_queue.append(source)
            sources_batch.append(source)
        return sources_batch

    def _parse_derivations(self, feathr_derivations):
        derivations_batch = []

        for i, derivation_name in enumerate(feathr_derivations):

            derivation = AtlasEntity(
                name=derivation_name,
                qualified_name=self.project_name +
                self.FEATURE_REGISTRY_DELIMITER + derivation_name,
                attributes={
                    "key": str(self.feathr_feature_config["derivations"][derivation_name].get_string("key", "")),
                    "type": str(self.feathr_feature_config["derivations"][derivation_name].get_string("type", "")),
                    "inputs": bool(self.feathr_feature_config["derivations"][derivation_name].get_string("inputs", "")),
                    "definition": str(self.feathr_feature_config["derivations"][derivation_name].get_string("definition", "")),
                    "tensorCategory": str(self.feathr_feature_config["derivations"][derivation_name].get_string("tensorCategory", "")),
                    "dimensionType": str(self.feathr_feature_config["derivations"][derivation_name].get_string("dimensionType", "")),
                    "valType": str(self.feathr_feature_config["derivations"][derivation_name].get_string("valType", ""))
                },
                typeName="feathr_derivation",
                guid=self.guid.get_guid(),
            )
            self.entity_batch_queue.append(derivation)
            derivations_batch.append(derivation)
        return derivations_batch

    def _read_config_from_workspace(self, workspace_path: str):
        """
        Read a HOCON file from a workspace
        """
        raw_hocon_feature_definition_config = ""
        raw_hocon_feature_join_config = ""
        raw_hocon_feature_generation_config = ""

        # get feature configuration file
        feature_config_paths = glob.glob(
            os.path.join(workspace_path, "feature_conf", '*.conf'))
        logger.info("Reading feature configuration from {}",
                    feature_config_paths)
        if len(feature_config_paths) > 0:
            feature_config_path = feature_config_paths[0]
            self.feathr_feature_config = ConfigFactory.parse_file(
                feature_config_path)
            with open(feature_config_path, "r") as f:
                raw_hocon_feature_definition_config = f.read()

            # get feature join config file
            feature_join_paths = glob.glob(os.path.join(
                workspace_path, "feature_join_conf", '*.conf'))
            logger.info("Reading feature join configuration from {}",
                    feature_join_paths)
        if len(feature_join_paths) > 0:
            feature_join_path = feature_join_paths[0]
            self.feathr_feature_join = ConfigFactory.parse_file(feature_join_path)
            with open(feature_join_path, "r") as f:
                raw_hocon_feature_join_config = f.read()

        # get feature generation config file
        feature_generation_paths = glob.glob(
            os.path.join(workspace_path, "feature_conf", '*.conf'))
        logger.info("Reading feature generation configuration from {}",
                    feature_generation_paths)
        if len(feature_generation_paths) > 0:
            feature_generation_path = feature_generation_paths[0]
            self.feathr_feature_generation = ConfigFactory.parse_file(
                feature_generation_path)
            with open(feature_generation_path, "r") as f:
                raw_hocon_feature_generation_config = f.read()

        feathr_anchors = self.feathr_feature_config.get("anchors", "")
        feathr_sources = self.feathr_feature_config.get("sources", "")
        feathr_derivations = self.feathr_feature_config.get("derivations", "")

        sources_batch = []
        anchors_batch = []
        # parse all the anchors
        if feathr_anchors:
            anchors_batch = self._parse_anchors(feathr_anchors)

        if feathr_sources:
            sources_batch = self._parse_sources(feathr_sources)

        if feathr_derivations:
            derivations_batch = self._parse_derivations(feathr_derivations)
            workspace = AtlasEntity(
                name=self.project_name,
                qualified_name=self.project_name,
                attributes={
                    "sources": [s.to_json(minimum=True) for s in sources_batch],
                    "anchors": [s.to_json(minimum=True) for s in anchors_batch],
                    "derivations": [s.to_json(minimum=True) for s in derivations_batch],
                    "raw_hocon_feature_definition_config": raw_hocon_feature_definition_config,
                    "raw_hocon_feature_join_config": raw_hocon_feature_join_config,
                    "raw_hocon_feature_generation_config": raw_hocon_feature_generation_config,
                },
                typeName="feathr_workspace",
                guid=self.guid.get_guid(),
            )
        else:
            derivations_batch = self._parse_derivations(feathr_derivations)
            workspace = AtlasEntity(
                name=self.project_name,
                qualified_name=self.project_name,
                attributes={
                    "sources": [s.to_json(minimum=True) for s in sources_batch],
                    "anchors": [s.to_json(minimum=True) for s in anchors_batch],
                    "raw_hocon_feature_definition_config": raw_hocon_feature_definition_config,
                    "raw_hocon_feature_join_config": raw_hocon_feature_join_config,
                    "raw_hocon_feature_generation_config": raw_hocon_feature_generation_config,
                },
                typeName="feathr_workspace",
                guid=self.guid.get_guid(),
            )

        # add lineage from anchor to workspace
        for individual_anchor in anchors_batch:
            lineage_process = AtlasProcess(
                name=individual_anchor.name + " to " + self.project_name,
                typeName="Process",
                qualified_name=self.FEATURE_REGISTRY_DELIMITER + "PROCESS" + self.FEATURE_REGISTRY_DELIMITER + self.project_name +
                self.FEATURE_REGISTRY_DELIMITER + individual_anchor.name,
                inputs=[individual_anchor],
                outputs=[workspace],
                guid=self.guid.get_guid(),
            )
            self.entity_batch_queue.append(lineage_process)

        # add lineage from source to workspace
        for individual_source in sources_batch:
            lineage_process = AtlasProcess(
                name=individual_source.name + " to " + self.project_name,
                typeName="Process",
                qualified_name=self.FEATURE_REGISTRY_DELIMITER + "PROCESS" + self.FEATURE_REGISTRY_DELIMITER + self.project_name +
                self.FEATURE_REGISTRY_DELIMITER + individual_source.name,
                inputs=[individual_source],
                outputs=[workspace],
                guid=self.guid.get_guid(),
            )
            self.entity_batch_queue.append(lineage_process)

        if feathr_derivations:
            # add lineage from derivation to workspace
            for individual_derivation in derivations_batch:
                lineage_process = AtlasProcess(
                    name=individual_derivation.name + " to " + self.project_name,
                    typeName="Process",
                    qualified_name=self.FEATURE_REGISTRY_DELIMITER + "PROCESS" + self.FEATURE_REGISTRY_DELIMITER + self.project_name +
                    self.FEATURE_REGISTRY_DELIMITER + individual_derivation.name,
                    inputs=[individual_derivation],
                    outputs=[workspace],
                    guid=self.guid.get_guid(),
                )
                self.entity_batch_queue.append(lineage_process)

        self.entity_batch_queue.append(workspace)

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
        prefix = os.path.commonprefix([path.resolve(), workspace_path.resolve()])
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
                definitions.transformations.add(vars(derived_feature)["transform"])
            else:
                RuntimeError("Object cannot be parsed. `derived_feature_list` should be a list of `DerivedFeature`.")

        for anchor in anchor_list:
            # obj is `FeatureAnchor`
            definitions.feature_anchors.add(anchor)
            # add the source section of this `FeatureAnchor` object
            definitions.sources.add(vars(anchor)['source'])
            for feature in vars(anchor)['features']:
                # get the transformation object from `Feature` or `DerivedFeature`
                if isinstance(anchor, Feature):
                    # feature is of type `Feature` 
                    definitions.features.add(anchor)
                    definitions.transformations.add(vars(feature)["transform"])
                else:
                    RuntimeError("Object cannot be parsed.")
        
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
        repo_definitions = self._extract_features_from_context(anchor_list, derived_feature_list, local_workspace_dir)
        self._save_request_feature_config(repo_definitions, local_workspace_dir)
        self._save_anchored_feature_config(repo_definitions, local_workspace_dir)
        self._save_derived_feature_config(repo_definitions, local_workspace_dir)

    @classmethod
    def _save_request_feature_config(self, repo_definitions: RepoDefinitions, local_workspace_dir="./"):
        config_file_name = "feature_conf/auto_generated_request_features.conf"        
        tm = Template("""
                    // THIS FILE IS AUTO GENERATED. PLEASE DO NOT EDIT.
                    anchors: {
                        {% for anchor in feature_anchors %}
                            {% if anchor.source.name == "PASSTHROUGH" %}
                                {{anchor.to_feature_config()}}
                            {% endif %}
                        {% endfor %}
                    }
                    """)
        
        request_feature_configs = tm.render(feature_anchors=repo_definitions.feature_anchors)
        config_file_path = os.path.join(local_workspace_dir, config_file_name)
        write_to_file(content=request_feature_configs, full_file_name=config_file_path)

    @classmethod
    def _save_anchored_feature_config(self, repo_definitions: RepoDefinitions, local_workspace_dir="./"):
        config_file_name = "feature_conf/auto_generated_anchored_features.conf"
        tm = Template("""
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
                    """)
        anchored_feature_configs = tm.render(feature_anchors=repo_definitions.feature_anchors,
                                             sources=repo_definitions.sources)
        config_file_path = os.path.join(local_workspace_dir, config_file_name)
        write_to_file(content=anchored_feature_configs, full_file_name=config_file_path)

    @classmethod
    def _save_derived_feature_config(self, repo_definitions: RepoDefinitions , local_workspace_dir="./"):
        config_file_name = "feature_conf/auto_generated_derived_features.conf"
        tm = Template("""
            anchors: {}
            derivations: {
                {% for derived_feature in derived_features %}
                    {{derived_feature.to_feature_config()}}
                {% endfor %}
            }
        """)
        derived_feature_configs = tm.render(derived_features=repo_definitions.derived_features)
        config_file_path = os.path.join(local_workspace_dir, config_file_name)
        write_to_file(content=derived_feature_configs, full_file_name=config_file_path)

    def register_features(self, workspace_path: Optional[Path] = None):
        """Register Features for the specified workspace

        Args:
            workspace_path (str, optional): path to a workspace. Defaults to None.
        """

        # register feature types each time when we register features.
        self._register_feathr_feature_types()
        self._read_config_from_workspace(workspace_path)
        # Upload all entities
        # need to be all in one batch to be uploaded, otherwise the GUID reference won't work
        results = self.purview_client.upload_entities(
            batch=self.entity_batch_queue)
        if results:
            webinterface_path = "https://web.purview.azure.com/resource/" + self.azure_purview_name + \
                                "/main/catalog/browseassettypes"
        else:
            RuntimeError("Feature registration failed.", results)

        logger.info(
            "Finished registering features. See {} to access the Purview web interface", webinterface_path)

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
            "entityType:feathr_anchor_feature")
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
                                                    typeName="feathr_workspace")
        # TODO - Change implementation to support traversing the workspace and construct the file, item by item 
        # We don't support modifying features outside of registring this should be fine.
        
        # Read the three config files from raw hocon field
        feature_conf_content = entities["entities"][0]["attributes"]["raw_hocon_feature_definition_config"]
        feature_join_conf_content = entities["entities"][0]["attributes"]["raw_hocon_feature_join_config"]
        feature_gen_conf_content = entities["entities"][0]["attributes"]["raw_hocon_feature_generation_config"]

        # Define the filenames for each config
        feature_conf_file = os.path.join(workspace_path,"feature_conf", "features.conf")
        feature_join_file = os.path.join(workspace_path,"feature_join_conf", "feature_join.conf")
        feature_gen_file = os.path.join(workspace_path,"feature_gen_conf", "feature_gen.conf")

        # Create file and directory, if does not exist
        os.makedirs(os.path.dirname(feature_conf_file), exist_ok=True)
        os.makedirs(os.path.dirname(feature_join_file), exist_ok=True)
        os.makedirs(os.path.dirname(feature_gen_file), exist_ok=True)

        with open(feature_conf_file, "w") as features:
            features.write(feature_conf_content)
        logger.info("Writing feature configuration from feathr registry to {}", feature_conf_file)
        
        with open(feature_join_file, "w") as offline_config:
            offline_config.write(feature_join_conf_content)
        logger.info("Writing offline configuration from feathr registry to {}", feature_join_file)

        with open(feature_gen_file, "w") as online_config:
            online_config.write(feature_gen_conf_content)
        logger.info("Writing online configuration from feathr registry to {}", feature_gen_file)
