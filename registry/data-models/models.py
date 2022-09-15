from pydantic import BaseModel
from typing import List, Optional

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
       | FeatureInterface |           |     Anchor       |
       +------------------+           +------------------+
               ｜                             ｜        |
               ｜ 1:n                         ｜1:n     |
               +------------------------------+        |
                             ｜                         |
                             ｜1:n                      | contains
                      +---------------+           +---------------+
                      |    Feature    | --------- |    Source     |
                      +---------------+ contains  +---------------+
                              |
                              |
                      +----------------+
                      | Transformation |
                      +----------------+
"""


class FeatureValue(BaseModel):
    """
    A value of a Feature.
    NOTE: FeatureValue is to be customized in each registry
    """
    pass


class FeatureValueType(BaseModel):
    """
    A value of a Feature.
    NOTE: FeatureValue is to be customized in each registry
    """
    pass


class Source(BaseModel):
    """
    Type of FeatureValue.
    It defines where the feature is extracted from. It can be online, offline, Restli, or other FeatureInterfaces.
    """
    pass


class DataSource(Source):
    pass


class FeatureSource(Source):
    input_feature_interface_ids: List[str]  # List of input feature interface IDs
    pass


class Transformation(BaseModel):
    """
    The transformation of a Feature.
    A transformation function represents the transformation logic to produce feature value from the source of FeatureAnchor
    NOTE: Transformation is to be customized in each registry
    """
    pass


class Feature(BaseModel):
    """
    Actual implementation of FeatureInterface.
    An implementation defines where a feature is extracted from (Source) and how it is computed (Transformation).
    The Source of a feature can be raw data sources (like Rest.li, HDFS) and/or other features.
    """
    feature_interface_id: str  # ID of the feature interface that the feature belongs to
    id: str
    name: str
    source: Source  # Source can be either data source or feature source
    transformation: Transformation  # transformation logic to produce feature value


class AnchorFeature(Feature):
    """
    Feature implementation of FeatureInterface which anchored to a data source.
    """
    source: DataSource


class DerivedFeature(Feature):
    """
    Feature implementation that is derived from other FeatureInterfaces.
    """
    source: FeatureSource


class FeatureInterface(BaseModel):
    """
    Named Feature Interface of FeatureImplementations.
    Each FeatureInterface is defined by feature producer and interact
    by feature consumers.Each FeatureInterface enclosed attributes that don't change across
    implementations. (eg. name, default value, value type, format, status, ownership).
    One FeatureInterface can have multiple Features for different environments.
    """
    default_value: Optional[FeatureValue]  # Optional default value
    id: str
    value_type: FeatureValueType  # Feature value type
    project_id: str
    features: List[Feature]  # List of features that the FeatureInterface has


class Project(BaseModel):
    """
    Group of FeatureInterfaces. It can be a project the team is working on,
    or a namespace which related FeatureInterfaces have.
    """
    id: str
    name: str
    feature_interfaces: List[FeatureInterface]  # List of feature interfaces that the project has


class Anchor(BaseModel):
    """
    Group of AnchorFeatures which anchored on same DataSource.
    This is mainly used by feature producer gather information about DataSource
    and FeatureImplementations associated with the DataSource.
    """
    id: str
    project_id: str  # ID of Project that the anchor belongs to
    source: DataSource  # data source of the Anchor
    anchor_features: List[AnchorFeature]  # List of anchor features that the anchor has
