from pprint import pprint
from typing import Union, List

from feathr.anchor import FeatureAnchor
from feathr.feature_derivations import DerivedFeature

class FeaturePrinter:
    """The class for pretty-printing features"""
    
    @staticmethod
    def pretty_print(feature_list: Union[List[FeatureAnchor], List[DerivedFeature]]) -> None:
        """Pretty print features

        Args:
            feature_list: FeatureAnchor or DerivedFeature
        """

        if all(isinstance(feature, FeatureAnchor) for feature in feature_list):
            for feature_anchor in feature_list:
                pprint("%s is the achor feature of %s" % \
                        (feature_anchor.name, [feature.name for feature in feature_anchor.features]))

        if all(isinstance(feature, DerivedFeature) for feature in feature_list):
            for derived_feature in feature_list:
                    pprint("%s is the derived feature of %s" % \
                            (derived_feature.name, [feature.name for feature in derived_feature.input_features]))