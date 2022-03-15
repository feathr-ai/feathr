
from feathr.anchor import FeatureAnchor
from feathr.source import Source
from feathr.feature_derivations import DerivedFeature
from feathr.feature import Feature
from feathr.transformation import Transformation
from typing import Set


class RepoDefinitions:
    def __init__(self,
                sources: Set[Source],
                features: Set[Feature],
                transformations: Set[Transformation],
                feature_anchors: Set[FeatureAnchor],
                derived_features: Set[DerivedFeature]) -> None:
        self.sources = sources
        self.features = features
        self.transformations = transformations
        self.feature_anchors = feature_anchors
        self.derived_features = derived_features