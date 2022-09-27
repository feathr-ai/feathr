from pydantic import BaseModel
from typing import List

"""
This file defines abstract backend data models for feature registry.
Backend data models will be used by backend API server to talk to feature registry backend.
Purpose of this is to decouple backend data models from API specific data models.
For each feature registry provider/implementation, they will extend this abstract
data models and backend API.
Diagram of the data models:  ./data-model-diagram.md
"""


class FeatureId(BaseModel):
    """
    Id for Feature, it's unique ID represents Feature.
    """
    id: str  # id of a feature


class FeatureNameId(BaseModel):
    """
    Id for FeatureName, it's unique ID represents FeatureName.
    """
    id: str  # id of a FeatureName


class AnchorId(BaseModel):
    """
    Id for Anchor, it's unique ID represents Anchor.
    """
    id: str  # id of a anchor


class ProjectId(BaseModel):
    """
    Id for Project, it's unique ID represents Project.
    """
    id: str  # id of a project


class Source(BaseModel):
    """
    Source of the feature.
    It defines where the feature is extracted or derived from.
    """
    pass


class DataSource(Source):
    """
    Data source of the feature.
    It defines the raw data source the feature is extracted from.
    """


class FeaturesSource(Source):
    """
    Feature source of the feature.
    It defines one of features where the feature is derived from.
    """
    input_feature_name_id: FeatureNameId  # Input feature name Keys
    pass


class Transformation(BaseModel):
    """
    The transformation of a Feature.
    A transformation function represents the transformation logic to produce feature value from the source of FeatureAnchor
    """
    pass


class Feature(BaseModel):
    """
    Actual implementation of FeatureName.
    An implementation defines where a feature is extracted from (Source) and how it is computed (Transformation).
    The Source of a feature can be raw data sources and/or other features.
    """
    id: FeatureId  # Unique ID for Feature
    feature_name_id: FeatureNameId  # Id of the feature name that the feature belongs to
    source: Source  # Source can be either data source or feature source
    transformation: Transformation  # transformation logic to produce feature value


class AnchorFeature(Feature):
    """
    Feature implementation of FeatureName which anchored to a data source.
    """
    source: DataSource  # Raw data source where the feature is extracted from


class DerivedFeature(Feature):
    """
    Feature implementation that is derived from other FeatureNames.
    """
    source: List[FeaturesSource]  # Source features where the feature is derived from


class FeatureName(BaseModel):
    """
    Named Feature Interface that can be backed by multiple Feature implementations across
    different environments accessing different sources (data lake access for batch training,
    KV store access for online serving). Each FeatureName is defined by feature producer.
    Feature consumers reference a feature by that name to access that feature data,
    agnostic of runtime environment. Each FeatureName also encloses attributes that does not
    change across implementations.
    """
    id: FeatureNameId  # unique ID for FeatureName, used to extract data for current FeatureName
    project_id: ProjectId  # ID of the project the FeatureName belongs to
    feature_ids: List[FeatureId]  # List of ids of feature that the FeatureName has


class Project(BaseModel):
    """
    Group of FeatureNames. It can be a project the team is working on,
    or a namespace which related FeatureNames have.
    """
    id: ProjectId  # Unique ID of the project.
    feature_name_ids: List[FeatureNameId]  # List of feature name ids that the project has
    anchor_ids: List[AnchorId]   # List of Anchor ids that the project has


class Anchor(BaseModel):
    """
    Group of AnchorFeatures which anchored on same DataSource.
    This is mainly used by feature producer gather information about DataSource
    and FeatureImplementations associated with the DataSource.
    """
    id: AnchorId  # Unique ID for Anchor
    project_id: ProjectId  # ID of Project that the anchor belongs to
    source: DataSource  # data source of the Anchor
    anchor_feature_ids: List[FeatureId]  # List of anchor features that the anchor has
