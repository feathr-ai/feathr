from pydantic import BaseModel
from typing import List

"""
This file defines abstract backend data models for feature registry.
Backend data models will be used by backend API server to talk to feature registry backend.
Purpose of this is to decouple backend data models from API specific data models.
For each feature registry provider/implementation, they will extend this abstract
data models and backend API.
Diagram of the data models:
                                  +-----------------+
                                  |     Project     |
                                  +-----------------+
                                          |  
                                          |
                                          | 
                            +------------------------------+    
                            |                              |      
                            | 1:n                          | 1:n         
                  +------------------+           +------------------+
      +---------- |   FeatureName    |           |     Anchor       |
      |      1:n  +------------------+           +------------------+
      |                 ｜                             ｜          |
      |                 ｜ 1:n                         ｜1:n       |
      |                 +------------------------------+          |
      |                               ｜                           |
      |                               ｜1:n                       \|/ has
      |   +--------------+    +---------------+                 +----------------+
      |   |Transformation|----|    Feature    | --------------- |   DataSource   |            
      |   +--------------+ has+---------------+                 +----------------+
      |                              /|\   |                     /|\           |extends
      |                               |    |                      |            |
      |                               |    +----------------------|---------+  |
      |                               |                           |         |  |
      |          +--------------------------------------+         |      has| \|/
      |          |                                      |         |        +----------+
      |          |extends                               |extends  |has     |  Source  |
      |    +----------------+                      +-----------------+     +----------+
      |    | DerivedFeature |                      |  AnchorFeature  |          /|\
      |    +----------------+                      +-----------------+           |
      |            |                                                             |
      |            |                                                             |extends
      |            |                                                       +------------------+
      |            +-------------------------------------------------------|   FeatureSource  |
      |                                                                has +------------------+
      |                                                                             |
      +-----------------------------------------------------------------------------|
                      
                      
                      
                      
                      
                      
"""


class FeatureId(BaseModel):
    """
    Id for Feature, it's unique ID represents Feature.
    """
    pass


class FeatureNameId(BaseModel):
    """
    Id for FeatureName, it's unique ID represents FeatureName.
    """
    pass


class AnchorId(BaseModel):
    """
    Id for Anchor, it's unique ID represents Anchor.
    """
    pass


class ProjectId(BaseModel):
    """
    Id for Project, it's unique ID represents Project.
    """
    pass


class Source(BaseModel):
    """
    Type of FeatureValue.
    It defines where the feature is extracted from.
    """
    pass


class DataSource(Source):
    pass


class FeatureSource(Source):
    input_feature_name_ids: List[FeatureNameId]  # List of input feature name Keys
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
    source: DataSource


class DerivedFeature(Feature):
    """
    Feature implementation that is derived from other FeatureNames.
    """
    source: FeatureSource


class FeatureName(BaseModel):
    """
    Named Feature Interface of FeatureImplementations.
    Each FeatureName is defined by feature producer and interact
    by feature consumers.Each FeatureName enclosed attributes that don't change across
    implementations.
    One FeatureName can have multiple Features for different environments.
    """
    id: FeatureNameId  # unique ID for FeatureName, used to extract data for current FeatureName
    project_id: ProjectId  # ID of the project the FeatureName belongs to
    features: List[FeatureId]  # List of ids of feature that the FeatureName has


class Project(BaseModel):
    """
    Group of FeatureInterfaces. It can be a project the team is working on,
    or a namespace which related FeatureInterfaces have.
    """
    id: ProjectId  # Unique ID of the project.
    feature_names: List[FeatureNameId]  # List of feature name ids that the project has
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
    anchor_features: List[FeatureId]  # List of anchor features that the anchor has
