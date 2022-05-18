import string

import pytest

from feathr import FeatureBase, FeatureNameValidationError


@pytest.mark.parametrize('bad_feature_name',
                         [None,
                          ''])
def test_feature_name_fails_on_empty_name(bad_feature_name: str):
    with pytest.raises(FeatureNameValidationError, match="empty feature name"):
        FeatureBase.validate_feature_name(bad_feature_name)


def test_feature_name_fails_on_leading_number():
    with pytest.raises(FeatureNameValidationError, match="cannot start with a number"):
        FeatureBase.validate_feature_name("4featurename")


def test_feature_name_fails_on_punctuation_chars():
    for char in set(string.punctuation) - set('_'):
        with pytest.raises(FeatureNameValidationError,  match="only letters, numbers, and underscores are allowed"):
            FeatureBase.validate_feature_name(f"feature_{char}_name")


@pytest.mark.parametrize('feature_name',
                         ["feature_name",
                          "features4lyfe",
                          "f_4_feature_",
                          "_leading_underscores_are_ok",
                          "CapitalizedFeature"
                          ''])
def test_feature_name_validates_ok(feature_name: str):
    assert FeatureBase.validate_feature_name(feature_name)

