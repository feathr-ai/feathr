package com.linkedin.frame.config.featureanchor.builder;

import com.google.common.base.Preconditions;
import com.linkedin.data.template.StringArray;
import com.linkedin.frame.config.featureanchor.builder.key.KeyPlaceholdersBuilder;
import com.linkedin.frame.core.config.producer.derivations.BaseFeatureConfig;
import com.linkedin.frame.core.config.producer.derivations.KeyedFeature;
import com.linkedin.frame.core.config.producer.derivations.SequentialJoinConfig;
import com.linkedin.frame.core.utils.MlFeatureVersionUrnCreator;
import com.linkedin.feathr.featureDataModel.FeatureAnchor.Anchor;
import com.linkedin.feathr.featureDataModel.ExpansionKeyFunction;
import com.linkedin.feathr.featureDataModel.ReductionFunction;
import com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor;
import com.linkedin.feathr.featureDataModel.StandardAggregation;
import com.linkedin.feathr.featureDataModel.Clazz;
import com.linkedin.feathr.featureDataModel.IdentityFunction;
import com.linkedin.feathr.featureDataModel.MvelExpression;
import com.linkedin.feathr.featureDataModel.UserDefinedFunction;
import com.linkedin.feathr.featureDataModel.KeyPlaceholder;
import com.linkedin.feathr.featureDataModel.KeyPlaceholderArray;
import com.linkedin.feathr.featureDataModel.FeatureSource;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;


public class SequentialJoinFeatureSourcesAnchorBuilder implements AnchorBuilder {

  /**
   * In sequential join syntax (go/frame/sequentialjoin), there is a discrepancy between online and offline,
   * which is outputKey field and transformation field under base section can be optional for offline sequential join features.
   * When these two fields are optional, feature data of the base feature is put to a column in the resulting DataFrame
   * and key field under expansion section represents the key transformation logic on top of this column.
   *
   * To ensure consistency between online and offline, for the above case, we will create default output keys
   * as prefix + index position of expansion keys.
   */
  private static final String SEQUENTIAL_JOIN_DEFAULT_OUTPUT_KEY_PREFIX = "__SequentialJoinDefaultOutputKey__";

  private final SequentialJoinConfig _sequentialJoinConfig;
  private final MlFeatureVersionUrnCreator _mlFeatureVersionUrnCreator;
  private final KeyPlaceholdersBuilder _keyPlaceholdersBuilder;

  /**
   * Check if the specific sequential join key is a default output key, by checking if it has the default output key
   * prefix.
   *
   * For offline sequential join features, outputKey field and transformation field under base section can be optional.
   * When they are not present, we will create default output keys as prefix + index position of expansion keys and
   * reference them here.
   */
  public static boolean isSequentialJoinDefaultOutputKey(String key) {
    return key.startsWith(SEQUENTIAL_JOIN_DEFAULT_OUTPUT_KEY_PREFIX);
  }

  public SequentialJoinFeatureSourcesAnchorBuilder(@Nonnull SequentialJoinConfig sequentialJoinConfig,
      @Nonnull MlFeatureVersionUrnCreator mlFeatureVersionUrnCreator,
      @Nonnull KeyPlaceholdersBuilder keyPlaceholdersBuilder) {
    Preconditions.checkNotNull(sequentialJoinConfig);
    Preconditions.checkNotNull(mlFeatureVersionUrnCreator);
    Preconditions.checkNotNull(keyPlaceholdersBuilder);
    _sequentialJoinConfig = sequentialJoinConfig;
    _mlFeatureVersionUrnCreator = mlFeatureVersionUrnCreator;
    _keyPlaceholdersBuilder = keyPlaceholdersBuilder;
  }

  public Anchor build() {
    SequentialJoinFeatureSourcesAnchor sequentialJoinFeatureSourcesAnchor = new SequentialJoinFeatureSourcesAnchor();

    sequentialJoinFeatureSourcesAnchor.setKeyPlaceholders(_keyPlaceholdersBuilder.build());
    sequentialJoinFeatureSourcesAnchor.setBase(buildBaseFeatureSource());
    sequentialJoinFeatureSourcesAnchor.setExpansionKeyFunction(buildExpansionKeyFunction());
    sequentialJoinFeatureSourcesAnchor.setExpansion(buildExpansionFeatureSource());
    sequentialJoinFeatureSourcesAnchor.setReductionFunction(buildReduceFunction());

    Anchor anchor = new Anchor();
    anchor.setSequentialJoinFeatureSourcesAnchor(sequentialJoinFeatureSourcesAnchor);
    return anchor;
  }

  private FeatureSource buildBaseFeatureSource() {
    FeatureSource featureSource = new FeatureSource();
    BaseFeatureConfig baseFeatureConfig = _sequentialJoinConfig.getBase();
    featureSource.setUrn(_mlFeatureVersionUrnCreator.create(baseFeatureConfig.getFeature()));
    featureSource.setKeyPlaceholderRefs(new StringArray(baseFeatureConfig.getKey()));
    return featureSource;
  }

  private ExpansionKeyFunction buildExpansionKeyFunction() {
    BaseFeatureConfig baseFeatureConfig = _sequentialJoinConfig.getBase();
    KeyedFeature expansion = _sequentialJoinConfig.getExpansion();

    ExpansionKeyFunction expansionKeyFunction = new ExpansionKeyFunction();

    expansionKeyFunction.setKeyFunction(buildKeyFunctionInExpansionKeyFunction(baseFeatureConfig, expansion));

    expansionKeyFunction.setKeyPlaceholders(new KeyPlaceholderArray(
        buildKeyPlaceholdersInExpansionKeyFunction(baseFeatureConfig, expansion)));
    return expansionKeyFunction;
  }

  ExpansionKeyFunction.KeyFunction buildKeyFunctionInExpansionKeyFunction(BaseFeatureConfig baseFeatureConfig,
      KeyedFeature expansion) {
    ExpansionKeyFunction.KeyFunction keyFunction = new ExpansionKeyFunction.KeyFunction();
    //Details on the keyFunction logic is documented in
    //https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Feature+Registry+Data+Model+Building+Logic+for+Sequential+Join
    if (baseFeatureConfig.getTransformation().isPresent() || baseFeatureConfig.getTransformationClass().isPresent()) {
      // when either transformation or transformationClass is set, the outputKeys should be set.
      // The only case outputKeys can be missing is for online, when outputKey, transformation, transformationClass are all missing,
      // frame will use the default output key and default transformation.
      if (!baseFeatureConfig.getOutputKeys().isPresent()) {
        throw new IllegalArgumentException(String.format("transformation/transformationClass is set in base feature config %s, but output"
            + "key is unset", baseFeatureConfig));
      }
      if (baseFeatureConfig.getTransformation().isPresent()) {
        keyFunction.setMvelExpression(new MvelExpression().setMvel(baseFeatureConfig.getTransformation().get()));
      } else {
        Clazz clazz = new Clazz().setFullyQualifiedName(baseFeatureConfig.getTransformationClass().get());
        keyFunction.setUserDefinedFunction(new UserDefinedFunction().setClazz(clazz));
      }
    } else {
      // if output key is not specified, then the number of expansion key can only be one
      if ((!baseFeatureConfig.getOutputKeys().isPresent()) && (expansion.getKey().size() != 1)) {
        throw new IllegalArgumentException(String.format("When base.outputKey is missing, the number of expansion "
            + "key can only be one, but got %d", expansion.getKey().size()));
      }
      keyFunction.setIdentityFunction(new IdentityFunction());
    }
    return keyFunction;
  }

  private List<KeyPlaceholder> buildKeyPlaceholdersInExpansionKeyFunction(BaseFeatureConfig baseFeatureConfig, KeyedFeature expansion) {
    List<KeyPlaceholder> keyPlaceholders = new ArrayList<>();
    if (baseFeatureConfig.getOutputKeys().isPresent()) {
      baseFeatureConfig.getOutputKeys().get().forEach(outputKey ->
          keyPlaceholders.add(new KeyPlaceholder().setKeyPlaceholderRef(outputKey)));
    } else {
      // In sequential join syntax (go/frame/sequentialjoin), there is a discrepancy between online and offline,
      // which is outputKey field and transformation field under base section can be optional for offline sequential join features.
      // When these two fields are optional, feature data of the base feature is put to a column in the resulting DataFrame
      // and key field under expansion section represents the key transformation logic on top of this column.
      //
      // To ensure consistency between online and offline, for the above case, we will create default output keys
      // as prefix + index position of expansion keys.
      for (int i = 0; i < expansion.getKey().size(); i++) {
        keyPlaceholders.add(new KeyPlaceholder().setKeyPlaceholderRef(SEQUENTIAL_JOIN_DEFAULT_OUTPUT_KEY_PREFIX + i));
      }
    }
    return keyPlaceholders;
  }

  private FeatureSource buildExpansionFeatureSource() {
    BaseFeatureConfig baseFeatureConfig = _sequentialJoinConfig.getBase();
    KeyedFeature expansion = _sequentialJoinConfig.getExpansion();

    FeatureSource expansionFeatureSource = new FeatureSource();

    expansionFeatureSource.setKeyPlaceholderRefs(new StringArray(
        buildKeyPlaceholderRefsInExpansionFeatureSource(baseFeatureConfig, expansion)));
    expansionFeatureSource.setUrn(_mlFeatureVersionUrnCreator.create(expansion.getFeature()));
    return expansionFeatureSource;
  }

  private List<String> buildKeyPlaceholderRefsInExpansionFeatureSource(BaseFeatureConfig baseFeatureConfig, KeyedFeature expansion) {
    List<String> keyPlaceholderRefs = new ArrayList<>();
    if (baseFeatureConfig.getOutputKeys().isPresent()) {
      keyPlaceholderRefs = expansion.getKey();
    } else {
      // In sequential join syntax (go/frame/sequentialjoin), there is a discrepancy between online and offline,
      // which is outputKey field and transformation field under base section can be optional for offline sequential join features.
      // When these two fields are optional, feature data of the base feature is put to a column in the resulting DataFrame
      // and key field under expansion section represents the key transformation logic on top of this column.
      //
      // To ensure consistency between online and offline, for the above case, we will create default output keys
      // as prefix + index position of expansion keys and reference them here.
      for (int i = 0; i < expansion.getKey().size(); i++) {
        keyPlaceholderRefs.add(SEQUENTIAL_JOIN_DEFAULT_OUTPUT_KEY_PREFIX + i);
      }
    }
    return keyPlaceholderRefs;
  }

  private ReductionFunction buildReduceFunction() {
    String aggregation = _sequentialJoinConfig.getAggregation();

    ReductionFunction reductionFunction = new ReductionFunction();
    reductionFunction.setStandardAggregation(
        StandardAggregation.valueOf(aggregation.toUpperCase()));
    return reductionFunction;
  }
}
