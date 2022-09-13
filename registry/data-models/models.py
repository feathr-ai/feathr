from pydantic import BaseModel
from typing import List, Optional


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
    pass


class Transformation(BaseModel):
    """
    The transformation of a Feature.
    A transformation function represents the transformation logic to produce feature value from the source of FeatureAnchor
    NOTE: Transformation is to be customized in each registry
    """
    pass


class Project(BaseModel):
    """
    Group of FeatureInterfaces. It can be a project the team is working on,
    or a namespace which related FeatureInterfaces have.
    """
    id: str
    name: str


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


class Anchor(BaseModel):
    """
    Group of AnchorFeatures which anchored on same DataSource.
    This is mainly used by feature producer gather information about DataSource
    and FeatureImplementations associated with the DataSource.
    """
    id: str
    source: DataSource


class Feature(BaseModel):
    """
    Actual implementation of FeatureInterface.
    An implementation defines where a feature is extracted from (Source) and how it is computed (Transformation).
    The Source of a feature can be raw data sources (like Rest.li, HDFS) and/or other features.
    """
    feature_interface_id: str  # ID of the feature interface that the feature anchor belongs to
    id: str
    name: str
    source: Source  # Source can be either data source or feature source
    transformation: Transformation  # transformation logic to produce feature value


class AnchorFeature(Feature):
    """
    Feature implementation of FeatureInterface which anchored to a data source.
    """
    pass


class DerivedFeature(Feature):
    """
    Feature implementation that is derived from other FeatureInterfaces.
    """
    input_feature_interface_ids: List[str]  # List of input feature interface IDs
