package com.linkedin.feathr.core.utils;

import com.linkedin.feathr.core.config.producer.derivations.DerivationConfig;
import com.linkedin.feathr.core.config.producer.derivations.DerivationConfigWithExpr;
import com.linkedin.feathr.core.config.producer.derivations.DerivationConfigWithExtractor;
import com.linkedin.feathr.core.config.producer.derivations.KeyedFeature;
import com.linkedin.feathr.core.config.producer.derivations.SequentialJoinConfig;
import com.linkedin.feathr.core.config.producer.derivations.SimpleDerivationConfig;
import com.linkedin.frame.common.urn.MlFeatureVersionUrn;
import com.linkedin.data.template.StringArray;
import com.linkedin.feathr.core.config.producer.ExprType;
import com.linkedin.feathr.core.config.producer.TypedExpr;
import com.linkedin.feathr.exception.FrameException;
import com.linkedin.feathr.featureDataModel.FeatureSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;


/**
 * A class to figure out the dependent features(a.k.a. input features) of derived feature.
 * TODO: The long-term solution should be that we create a domain model on top the the ConfigObj and build the following
 * logic into it.
 * https://jira01.corp.linkedin.com:8443/browse/PROML-7595
 */
public class DerivedFeatureDependencyResolver {
  private final MlFeatureVersionUrnCreator _mlFeatureVersionUrnCreator;
  private final MvelInputsResolver _mvelInputsResolver;

  public DerivedFeatureDependencyResolver(MlFeatureVersionUrnCreator mlFeatureVersionUrnCreator,
      MvelInputsResolver mvelInputsResolver) {
    _mlFeatureVersionUrnCreator = mlFeatureVersionUrnCreator;
    _mvelInputsResolver = mvelInputsResolver;
  }

  /**
   * Gets dependent features(a.k.a input features) for the derived feature.
   * For a derived feature that depends two identical features of different alias(like classical viewer and viewee
   * example), it's going to return two identical features.
   */
  public List<FeatureSource> getDependentFeatures(DerivationConfig derivationConfig) {
    if (derivationConfig instanceof SimpleDerivationConfig) {
      return getDependentFeatures((SimpleDerivationConfig) derivationConfig);
    } else if (derivationConfig instanceof SequentialJoinConfig) {
      return getDependentFeatures((SequentialJoinConfig) derivationConfig);
    } else if (derivationConfig instanceof DerivationConfigWithExpr) {
      return getDependentFeatures((DerivationConfigWithExpr) derivationConfig);
    } else if (derivationConfig instanceof DerivationConfigWithExtractor) {
      return getDependentFeatures((DerivationConfigWithExtractor) derivationConfig);
    } else {
      throw new FrameException("The DerivationConfig class is not supported: " + derivationConfig.getClass());
    }
  }

  private List<FeatureSource> getDependentFeatures(SimpleDerivationConfig simpleDerivationConfig) {
    TypedExpr featureTypedExpr = simpleDerivationConfig.getFeatureTypedExpr();
    // TODO(PROML-7896): SQL is not supported and we plan to migrate SQL to MVEL for derived features.
    if (featureTypedExpr.getExprType() == ExprType.SQL) {
      throw new FrameException("Computing the dependent features for SQL expression " + featureTypedExpr.getExpr() + " is not supported.");
    }
    String expr = featureTypedExpr.getExpr();

    List<FeatureSource> list = new ArrayList<>();
    for (String featureName : _mvelInputsResolver.getInputFeatures(expr)) {
      MlFeatureVersionUrn mlFeatureVersionUrn = _mlFeatureVersionUrnCreator.create(featureName);
      list.add(buildFeatureSource(mlFeatureVersionUrn, new StringArray(), null));
    }
    return list;
  }

  private List<FeatureSource> getDependentFeatures(SequentialJoinConfig sequentialJoinConfig) {
    List<FeatureSource> featureSources = new ArrayList<>();
    featureSources.add(buildFeatureSource(_mlFeatureVersionUrnCreator.create(
        sequentialJoinConfig.getBase().getFeature()), new StringArray(sequentialJoinConfig.getBase().getKey()),
        null));
    featureSources.add(buildFeatureSource(_mlFeatureVersionUrnCreator.create(
        sequentialJoinConfig.getExpansion().getFeature()), new StringArray(sequentialJoinConfig.getExpansion().getKey()),
        null));
    return featureSources;
  }

  private List<FeatureSource> getDependentFeatures(DerivationConfigWithExpr derivationConfigWithExpr) {
    List<FeatureSource> list = new ArrayList<>();
    for (Map.Entry<String, KeyedFeature> entry : derivationConfigWithExpr.getInputs().entrySet()) {
      String alias = entry.getKey();
      KeyedFeature keyedFeature = entry.getValue();
      String featureName = keyedFeature.getFeature();
      MlFeatureVersionUrn mlFeatureVersionUrn = _mlFeatureVersionUrnCreator.create(featureName);
      list.add(buildFeatureSource(mlFeatureVersionUrn, new StringArray(keyedFeature.getKey()), alias));
    }
    return list;
  }

  private List<FeatureSource> getDependentFeatures(DerivationConfigWithExtractor derivationConfigWithExtractor) {
    List<FeatureSource> list = new ArrayList<>();
    for (KeyedFeature keyedFeature : derivationConfigWithExtractor.getInputs()) {
      String featureName = keyedFeature.getFeature();
      MlFeatureVersionUrn mlFeatureVersionUrn = _mlFeatureVersionUrnCreator.create(featureName);
      list.add(buildFeatureSource(mlFeatureVersionUrn, new StringArray(keyedFeature.getKey()), null));
    }
    return list;
  }


  private FeatureSource buildFeatureSource(MlFeatureVersionUrn mlFeatureVersionUrn, StringArray keyPlaceholders,
      @Nullable String alias) {
    FeatureSource featureSource = new FeatureSource();
    featureSource.setUrn(mlFeatureVersionUrn);
    if (alias != null) {
      featureSource.setAlias(alias);
    }
    featureSource.setKeyPlaceholderRefs(keyPlaceholders);
    return featureSource;
  }
}
