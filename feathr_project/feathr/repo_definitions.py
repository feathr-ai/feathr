from feathr.sdk.feature_anchor import FeatureAnchor
from feathr.sdk.source import Source
from feathr.sdk.feature import Feature
from feathr.sdk.transformation import Transformation
from typing import Set

class RepoDefinitions:
    def __init__(self,
                sources: Set[Source],
                features: Set[Feature],
                transformations: Set[Transformation],
                feature_anchors: Set[FeatureAnchor]) -> None:
        self.sources = sources
        self.features = features
        self.transformations = transformations
        self.feature_anchors = feature_anchors