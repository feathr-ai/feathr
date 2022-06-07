package com.linkedin.feathr.compute;

import com.google.common.collect.Iterables;
import com.linkedin.frame.common.urn.MlFeatureVersionUrn;
import com.linkedin.data.template.RecordTemplate;
import com.linkedin.data.template.StringArray;
import com.linkedin.data.template.StringMap;
import com.linkedin.feathr.config.FeatureDefinition;
import com.linkedin.feathr.core.utils.FeatureRefConverter;
import com.linkedin.feathr.core.utils.MvelInputsResolver;
import com.linkedin.feathr.featureDataModel.FeatureAnchor;
import com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor;
import com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor;
import com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor;
import com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor;
import com.linkedin.feathr.featureDataModel.StandardAggregation;
import com.linkedin.feathr.featureDataModel.MvelExpression;
import com.linkedin.feathr.featureDataModel.SparkSqlExpression;
import com.linkedin.feathr.featureDataModel.UserDefinedFunction;
import com.linkedin.feathr.featureDataModel.KeyPlaceholder;
import com.linkedin.feathr.featureDataModel.FeatureSource;
import com.linkedin.feathr.featureDataModel.TimeFieldFormat;
import com.linkedin.feathr.featureDataModel.TimestampGranularity;
import com.linkedin.feathr.featureDataModel.SlidingWindowAggregation;
import com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation;
import com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable;
import com.linkedin.feathr.featureDataModel.Unit;
import com.linkedin.feathr.featureDataModel.Window;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Converts FeatureDefinitions in the Feature Registry PDL model, into an instance of ComputeGraph.
 *
 * Currently this follows Frame-Offline's interpretation of the FR PDL model only! It won't work for Frame-Online yet.
 */
public class FeatureDefinitionsConverter {
  private static final FeatureDefinitionsConverter INSTANCE = new FeatureDefinitionsConverter();

  private FeatureDefinitionsConverter() {
  }

  /**
   * Build a ComputeGraph based on a feature definitions map.
   * @param featureDefinitionMap the feature definitions
   * @return the feature compute graph
   */
  // TODO there need to be some sister methods that extract relevant environment-specific source configs.
  public static ComputeGraph buildFromFeatureUrns(Map<MlFeatureVersionUrn, FeatureDefinition> featureDefinitionMap)
      throws CloneNotSupportedException {
    return build(featureDefinitionMap.entrySet().stream()
        .collect(Collectors.toMap(entry ->
            FeatureRefConverter.getInstance().getFeatureRefStr(entry.getKey()), entry -> entry.getValue())));
  }

  /**
   * Build a ComputeGraph based on a feature definitions map.
   * @param featureDefinitionMap the feature definitions
   * @return the feature compute graph
   */
  public static ComputeGraph build(Map<String, FeatureDefinition> featureDefinitionMap)
      throws CloneNotSupportedException {
    return INSTANCE.makeGraphForFeatureDefinitionsMap(featureDefinitionMap);
  }

  private ComputeGraph makeGraphForFeatureDefinitionsMap(Map<String, FeatureDefinition> featureDefinitionMap)
      throws CloneNotSupportedException {
    List<ComputeGraph> partialGraphs = new ArrayList<>();

    featureDefinitionMap.forEach((featureName, featureDefinition) -> {
      // ASSUMPTION 1: IN FR DATA MODEL, THERE IS ONLY EVER ONE ANCHOR DEFINED FOR A FEATURE
      FeatureAnchor.Anchor anchor = Iterables.getOnlyElement(featureDefinition.getFeatureAnchors()).getAnchor();
      FeatureVersion version = featureDefinition.getFeatureVersion();

      // ASSUMPTION 2:
      //   Allowed Anchor Types (current Frame-Offline behavior):
      //     HdfsDataSource
      //     ObservationPassthroughDataSource
      //     OfflineFeatureSourcesAnchor
      //     SequentialJoinFeatureSourcesAnchor

      if (anchor.isHdfsDataSourceAnchor()) {
        // TODO: SHOULD WE DEDUPE THE INPUT SOURCE DEFINITIONS FIRST BEFORE PROCESSING THEM, INSTEAD OF AFTER?
        partialGraphs.add(makeGraphForHdfsDataSourceAnchor(featureName, anchor.getHdfsDataSourceAnchor(), version));
      } else if (anchor.isObservationPassthroughDataSourceAnchor()) {
        partialGraphs.add(makeGraphForPassthroughDataSourceAnchor(featureName,
            anchor.getObservationPassthroughDataSourceAnchor(), version));
      } else if (anchor.isOfflineFeatureSourcesAnchor()) {
        partialGraphs.add(makeGraphForOfflineFeatureSourcesAnchor(featureName,
            anchor.getOfflineFeatureSourcesAnchor(), version));
      } else if (anchor.isSequentialJoinFeatureSourcesAnchor()) {
        partialGraphs.add(makeGraphForSequentialJoinFeatureSourcesAnchor(featureName,
            anchor.getSequentialJoinFeatureSourcesAnchor(), version));
      } else {
        throw new RuntimeException("Unhandled anchor type: " + anchor.memberType() + ". Data was " + anchor);
      }
    });

    return ComputeGraphs.removeRedundancies(ComputeGraphs.merge(partialGraphs));
  }

  private ComputeGraph makeGraphForHdfsDataSourceAnchor(String featureName, HdfsDataSourceAnchor anchor, FeatureVersion version) {
    ComputeGraphBuilder graphBuilder = new ComputeGraphBuilder();

    List<String> keyPlaceholders = anchor.getKeyPlaceholders().stream()
        .map(KeyPlaceholder::getKeyPlaceholderRef)
        .collect(Collectors.toList());
    // We only know how many keys this feature has by looking at the number of keyPlaceholders.
    int numKeyParts = keyPlaceholders.size();

    if (keyPlaceholders.size() != new HashSet<>(keyPlaceholders).size()) {
      throw new RuntimeException("Encountered non-unique key placeholders in " + anchor);
    }

    boolean isSlidingWindowAggregation = (anchor.getTransformationFunction().isSlidingWindowAggregation()
        || anchor.getTransformationFunction().isSlidingWindowEmbeddingAggregation()
        || anchor.getTransformationFunction().isSlidingWindowLatestAvailable());

    String keyExpression;
    KeyExpressionType keyExpressiontype;

    if (anchor.getSource().getKeyFunction().isMvelExpression()) {
      keyExpression = anchor.getSource().getKeyFunction().getMvelExpression().getMvel();
      keyExpressiontype = KeyExpressionType.MVEL;
    } else if (anchor.getSource().getKeyFunction().isSparkSqlExpression()) {
      keyExpression = anchor.getSource().getKeyFunction().getSparkSqlExpression().getSql();
      keyExpressiontype = KeyExpressionType.SQL;
    } else if (anchor.getSource().getKeyFunction().isUserDefinedFunction()) {
      keyExpression = anchor.getSource().getKeyFunction().getUserDefinedFunction().getClazz().getFullyQualifiedName();
      keyExpressiontype = KeyExpressionType.UDF;
    } else {
      throw new RuntimeException("Unsupported key type " + anchor.getSource().getKeyFunction());
    }

    boolean isHdfs = anchor.getSource().getDatasetLocation().isHdfsLocation();

    // Compute-model DataSource doesn't contain details needed to actually access that data. Instead it has a
    // sourceId which the Runtime must use to resolve and access the actual source data.

    String path = null;
    if (isHdfs) {
      path = anchor.getSource().getDatasetLocation().getHdfsLocation().getPath();
    } else {
      path = anchor.getSource().getDatasetLocation().getDaliLocation().getUri().toString();
    }
    DataSource dataSourceNode = graphBuilder.addNewDataSource().setExternalSourceRef(path)
        // Use "keyPlaceholders" for the entity parameter for the data source, NOT OfflineDataSourceKey.keyFunction.
        // It is UNCLEAR AND NEEDS TO BE CLARIFIED how/whether OfflineDataSourceKey.keyFunction should be used here.
        // We might need to add a Transformation node that applies the keyFunction.
        .setSourceType(isSlidingWindowAggregation ? DataSourceType.EVENT : DataSourceType.UPDATE).setKeyExpression(keyExpression)
        .setKeyExpressionType(keyExpressiontype);


    NodeReference referenceToSource = makeNodeReferenceWithSimpleKeyReference(dataSourceNode.getId(), numKeyParts);

    RecordTemplate operatorNode;
    RecordTemplate operatorReference = makeTransformationOrAggregationFunction(anchor.getTransformationFunction());
    if (operatorReference instanceof AggregationFunction) {
      operatorNode = graphBuilder.addNewAggregation()
          .setFunction((AggregationFunction) operatorReference)
          .setInput(referenceToSource)
          .setFeatureName(featureName); // TODO THIS MIGHT BE A PROBLEM. or maybe not.
      // An oddity: The way we determine the Source Type given the Frame pegasus configs is by looking at what kind of
      // transformation function is attached to it (an aggregation or a regular transform). So fill in this info about
      // the DataSourceNode now.
      String filePartitionFormat = null;
      if (anchor.getSource().hasDatasetSnapshotTimeFormat()) {
        filePartitionFormat = anchor.getSource().getDatasetSnapshotTimeFormat().getDateTimeFormat();
      }

      TimestampCol timestampCol = null;
      if (anchor.getSource().hasTimeField()) {
        TimeFieldFormat timestampColFormat = anchor.getSource().getTimeField().getFormat();
        String timestampColExpr = anchor.getSource().getTimeField().getName();
        timestampCol = new TimestampCol().setExpression(timestampColExpr).setFormat(resolveTimeFieldFormatTerm(timestampColFormat));
      }

      if (filePartitionFormat != null && timestampCol != null) {
        dataSourceNode.setSourceType(DataSourceType.EVENT).setFilePartitionFormat(filePartitionFormat).setTimestampColumnInfo(timestampCol);
      } else if (timestampCol != null) {
        dataSourceNode.setSourceType(DataSourceType.EVENT).setTimestampColumnInfo(timestampCol);
      } else {
        dataSourceNode.setSourceType(DataSourceType.EVENT).setFilePartitionFormat(filePartitionFormat);
      }

      HdfsDataSourceAnchor.TransformationFunction transformationFunction = anchor.getTransformationFunction();
      Window featureWindow = null;
      if (transformationFunction.isSlidingWindowAggregation()) {
        featureWindow = transformationFunction.getSlidingWindowAggregation().getWindow();
      } else if (transformationFunction.isSlidingWindowLatestAvailable()) {
        featureWindow = transformationFunction.getSlidingWindowLatestAvailable().getWindow();
      } else if (transformationFunction.isSlidingWindowEmbeddingAggregation()) {
        featureWindow = transformationFunction.getSlidingWindowEmbeddingAggregation().getWindow();
      }

      if (dataSourceNode.getWindow() == null || featureWindow.getSize() > dataSourceNode.getWindow().getSize()) {
        dataSourceNode.setWindow(featureWindow);
      }
      // graphBuilder.addDataSource(featurePath, featureWindow);
    } else if (operatorReference instanceof TransformationFunction) {
      operatorNode = graphBuilder.addNewTransformation()
          .setFunction((TransformationFunction) operatorReference)
          .setInputs(new NodeReferenceArray(Collections.singleton(referenceToSource)))
          .setFeatureName(featureName);
      // See comment a few lines above about "oddity".
      dataSourceNode.setSourceType(DataSourceType.UPDATE);
    } else {
      throw new RuntimeException("Unexpected operator reference type " + operatorReference.getClass() + " - data: "
          + operatorReference);
    }

    FeatureDefintion featureDefinition = new FeatureDefintion().setFeatureVersion(version).setNodeId(PegasusUtils.getNodeId(operatorNode));
    graphBuilder.addFeatureName(featureName, featureDefinition);
    return graphBuilder.build();
  }

  private ComputeGraph makeGraphForPassthroughDataSourceAnchor(String featureName,
      ObservationPassthroughDataSourceAnchor anchor, FeatureVersion version) {
    ComputeGraphBuilder graphBuilder = new ComputeGraphBuilder();
    DataSource dataSourceNode = graphBuilder.addNewDataSource()
        .setSourceType(DataSourceType.CONTEXT);

    // IT'S A BIT UNCLEAR YET how data source references to CONTEXT sources should really work. how many keys do they have???
    NodeReference dataSourceNodeReference = makeNodeReferenceWithSimpleKeyReference(dataSourceNode.getId(), 0);

    TransformationFunction transformationFunction = makeTransformationFunction(anchor.getTransformationFunction());
    Transformation transformationNode = graphBuilder.addNewTransformation()
        .setFunction(transformationFunction)
        .setInputs(new NodeReferenceArray(Collections.singleton(dataSourceNodeReference)))
        .setFeatureName(featureName);

    FeatureDefintion featureDefinition = new FeatureDefintion().setFeatureVersion(version).setNodeId(transformationNode.getId());
    graphBuilder.addFeatureName(featureName, featureDefinition);
    return graphBuilder.build();
  }

  private ComputeGraph makeGraphForSequentialJoinFeatureSourcesAnchor(String featureName,
      SequentialJoinFeatureSourcesAnchor anchor, FeatureVersion version) {
    ComputeGraphBuilder graphBuilder = new ComputeGraphBuilder();
    String baseFeatureName = FeatureRefConverter.getInstance().getFeatureRefStr(anchor.getBase().getUrn());
    List<String> baseFeatureKeys = anchor.getBase().getKeyPlaceholderRefs();
    List<String> entityParameters = anchor.getKeyPlaceholders().stream()
        .map(KeyPlaceholder::getKeyPlaceholderRef).collect(Collectors.toList());
    External baseExternalFeatureNode = graphBuilder.addNewExternal().setName(baseFeatureName);
    KeyReferenceArray keyReferenceArray = baseFeatureKeys.stream()
        .map(entityParameters::indexOf)
        .map(position -> new KeyReference().setPosition(position))
        .collect(Collectors.toCollection(KeyReferenceArray::new));

    int nodeId = baseExternalFeatureNode.getId();
    NodeReference baseNodeReference = new NodeReference().setId(nodeId).setKeyReference(keyReferenceArray);
    Lookup.LookupKey lookupKey;
    String featureNameAlias = anchor.getExpansionKeyFunction().getKeyPlaceholders().get(0).getKeyPlaceholderRef();

    // Here we want to check if there is an expansion key function and add a transformation node on top of the
    // base external feature node in that case. Note we only support MVEL in this case in the HOCON config.
    if (anchor.getExpansionKeyFunction().getKeyFunction().isMvelExpression()) {
      MvelExpression baseFeatureTransformationExpression = anchor.getExpansionKeyFunction().getKeyFunction().getMvelExpression();
      // Should be just the base feature.
      List<String> inputFeatureNames = MvelInputsResolver.getInstance().getInputFeatures(baseFeatureTransformationExpression.getMvel());
      TransformationFunction transformationFunction = makeTransformationFunction(baseFeatureTransformationExpression,
          inputFeatureNames).setOperator(Operators.OPERATOR_ID_MVEL);
      // Note here we specifically do not set the base feature name or add a feature definition because this is not a named feature,
      // it is a intermediate feature that will only be used for sequential join so a name will be generated for it.
      Transformation transformationNode = graphBuilder.addNewTransformation()
          .setInputs(new NodeReferenceArray(Collections.singleton(baseNodeReference)))
          .setFunction(transformationFunction)
          .setFeatureName(anchor.getExpansionKeyFunction().getKeyPlaceholders().get(0).getKeyPlaceholderRef());
      int transformationNodeId = transformationNode.getId();

      NodeReference baseTransformationNodeReference = new NodeReference().setId(transformationNodeId).setKeyReference(keyReferenceArray);
      lookupKey = new Lookup.LookupKey().create(baseTransformationNodeReference);
    } else {
      lookupKey = new Lookup.LookupKey().create(baseNodeReference);
    }

    // Create lookup key array based on key reference and base node reference.
    StringArray expansionKeysArray = anchor.getExpansion().getKeyPlaceholderRefs();
    Lookup.LookupKeyArray lookupKeyArray = expansionKeysArray.stream()
        .map(entityParameters::indexOf)
        .map(position -> position == -1 ? lookupKey
            : entityParameters.get(position).equals(featureNameAlias) ? lookupKey
                : new Lookup.LookupKey().create(new KeyReference().setPosition(position))
        )
        .collect(Collectors.toCollection(Lookup.LookupKeyArray::new));

    // create an external node without key reference for expansion.
    String expansionFeatureName = FeatureRefConverter.getInstance().getFeatureRefStr(anchor.getExpansion().getUrn());
    External expansionExternalFeatureNode = graphBuilder.addNewExternal().setName(expansionFeatureName);

    // get aggregation function
    StandardAggregation aggType = anchor.getReductionFunction().getStandardAggregation();
    Lookup lookupNode = graphBuilder.addNewLookup().setLookupNode(expansionExternalFeatureNode.getId())
        .setLookupKey(lookupKeyArray).setAggregation(aggType.toString()).setFeatureName(featureName);
    FeatureDefintion featureDefinition = new FeatureDefintion().setFeatureVersion(version).setNodeId(lookupNode.getId());
    graphBuilder.addFeatureName(featureName, featureDefinition);
    // graphBuilder.addFeatureName(baseFeatureName, baseExternalFeatureNode.getId());
    // graphBuilder.addFeatureName(expansionFeatureName, expansionExternalFeatureNode.getId());
    return graphBuilder.build();
    // throw new UnsupportedOperationException(); // TODO!
  }

  // aka "derived feature"
  // TODO SIMPLIFY THIS FUNCTION AND REMOVE REDUNDANT CODE!
  private ComputeGraph makeGraphForOfflineFeatureSourcesAnchor(String featureName, OfflineFeatureSourcesAnchor anchor, FeatureVersion version) {
    // the original implementation considered 3 main cases
    if (anchor.getTransformationFunction().isUserDefinedFunction()) {
      // "Category 1: Derivation with Class"
      ComputeGraphBuilder graphBuilder = new ComputeGraphBuilder();

      List<String> entityParameters = anchor.getKeyPlaceholders().stream()
          .map(KeyPlaceholder::getKeyPlaceholderRef).collect(Collectors.toList());

      Map<String, External> externalFeatureNodes = anchor.getSource().stream()
          .map(FeatureSource::getUrn)
          .map(FeatureRefConverter.getInstance()::getFeatureRefStr)
          .distinct()
          .collect(Collectors.toMap(
              Function.identity(),
              name -> graphBuilder.addNewExternal().setName(name)));

      NodeReferenceArray inputs = anchor.getSource().stream().map(featureSource -> {
        String inputFeatureName = FeatureRefConverter.getInstance().getFeatureRefStr(featureSource.getUrn());
        List<String> entityArgs = featureSource.getKeyPlaceholderRefs();

        KeyReferenceArray keyReferenceArray = entityArgs.stream()
            .map(entityParameters::indexOf) // entityParameters should always be small (no 10+ dimensional keys etc)
            .map(position -> new KeyReference().setPosition(position))
            .collect(Collectors.toCollection(KeyReferenceArray::new));
        int nodeId = externalFeatureNodes.get(inputFeatureName).getId();

        return new NodeReference().setId(nodeId).setKeyReference(keyReferenceArray);
      }).collect(Collectors.toCollection(NodeReferenceArray::new));

      // I DON'T THINK THE FR-PDL "FEATURE ALIASES" ARE USED FOR DERIVATION-WITH-CLASS TODO NEED TO CONFIRM THIS.
      // List<String> aliases = anchor.getSource().stream().map(FeatureSource::getAlias).collect(Collectors.toList());

      TransformationFunction transformationFunction = makeTransformationFunction(anchor.getTransformationFunction().getUserDefinedFunction());
      Transformation transformationNode = graphBuilder.addNewTransformation()
          .setInputs(inputs)
          .setFunction(transformationFunction)
          .setFeatureName(featureName);

      // could make this part common.
      FeatureDefintion featureDefinition = new FeatureDefintion().setFeatureVersion(version).setNodeId(transformationNode.getId());
      graphBuilder.addFeatureName(featureName, featureDefinition);
      return graphBuilder.build();
      // end common-ish part.
    } else if (anchor.getKeyPlaceholders().isEmpty()) {
      // "Category 2: Simple Derivation"
      ComputeGraphBuilder graphBuilder = new ComputeGraphBuilder();
      List<String> inputFeatureNames;
      TransformationFunction transformationFunction;
      if (anchor.getTransformationFunction().isMvelExpression()) {
        String mvel = anchor.getTransformationFunction().getMvelExpression().getMvel();
        inputFeatureNames = MvelInputsResolver.getInstance().getInputFeatures(mvel);
        transformationFunction = makeTransformationFunction(anchor.getTransformationFunction().getMvelExpression(),
            inputFeatureNames);
      } else if (anchor.getTransformationFunction().isSparkSqlExpression()) {
        String sql = anchor.getTransformationFunction().getSparkSqlExpression().getSql();
        inputFeatureNames = SqlUtil.getInputsFromSqlExpression(sql);
        transformationFunction = makeTransformationFunction(anchor.getTransformationFunction().getSparkSqlExpression(),
            inputFeatureNames);
      } else {
        throw new RuntimeException("Unsupported transformation function " + anchor.getTransformationFunction() + " for"
            + " feature " + featureName);
      }
      Map<String, External> externalFeatureNodes = inputFeatureNames.stream()
          .collect(Collectors.toMap(Function.identity(),
              name -> graphBuilder.addNewExternal().setName(name)));
      NodeReferenceArray nodeReferences = inputFeatureNames.stream().map(inputFeatureName -> {
            int featureDependencyNodeId = externalFeatureNodes.get(inputFeatureName).getId();
            // PROBLEM: WE HAVE NO WAY OF KNOWING how many keys the feature has. Perhaps this ambiguity should be specifically
            //          allowed for in the compute model. For now, we will ASSUME THAT SIMPLE DERIVATIONS ARE ALWAYS FOR
            //          SINGLE-KEY FEATURES. The following line makes this assumption.
            return makeNodeReferenceWithSimpleKeyReference(featureDependencyNodeId, 1);
          }
      ).collect(Collectors.toCollection(NodeReferenceArray::new));
      Transformation transformationNode = graphBuilder.addNewTransformation()
          .setInputs(nodeReferences)
          .setFunction(transformationFunction)
          .setFeatureName(featureName);
      FeatureDefintion featureDefinition = new FeatureDefintion().setFeatureVersion(version).setNodeId(transformationNode.getId());
      graphBuilder.addFeatureName(featureName, featureDefinition);
      return graphBuilder.build();
    } else {
      // "Category 3: Derivation with Expression"
      ComputeGraphBuilder graphBuilder = new ComputeGraphBuilder();
      // TODO SIMPLIFY AND REDUCE DUPLICATE CODE WITH THE OTHER CASES.

      List<String> entityParameters = anchor.getKeyPlaceholders().stream()
          .map(KeyPlaceholder::getKeyPlaceholderRef).collect(Collectors.toList());

      Map<String, External> externalFeatureNodes = anchor.getSource().stream()
          .map(FeatureSource::getUrn)
          .map(FeatureRefConverter.getInstance()::getFeatureRefStr)
          .distinct()
          .collect(Collectors.toMap(
              Function.identity(),
              name -> graphBuilder.addNewExternal().setName(name)));

      NodeReferenceArray inputs = anchor.getSource().stream().map(featureSource -> {
        String inputFeatureName = FeatureRefConverter.getInstance().getFeatureRefStr(featureSource.getUrn());
        List<String> entityArgs = featureSource.getKeyPlaceholderRefs();

        KeyReferenceArray keyReferenceArray = entityArgs.stream()
            .map(entityParameters::indexOf) // entityParameters should always be small (no 10+ dimensional keys etc)
            .map(position -> new KeyReference().setPosition(position))
            .collect(Collectors.toCollection(KeyReferenceArray::new));
        int inputNodeId = externalFeatureNodes.get(inputFeatureName).getId();

        // If there is a featureAlias, add a feature alias transformation node on top of the external node which
        // represents the input feature.
        if (featureSource.getAlias() != null) {
          ArrayList regularKeyReferenceArray = new ArrayList<KeyReference>();
          for (int i = 0; i < entityArgs.size(); i++) {
            regularKeyReferenceArray.add(new KeyReference().setPosition(i));
          }
          KeyReferenceArray simpleKeyReferenceArray = new KeyReferenceArray(regularKeyReferenceArray);
          NodeReference inputNodeReference = new NodeReference().setId(inputNodeId).setKeyReference(simpleKeyReferenceArray);

          TransformationFunction featureAliasFunction = new TransformationFunction().setOperator(Operators.OPERATOR_FEATURE_ALIAS);
          Transformation transformationNode = graphBuilder.addNewTransformation()
              .setInputs(new NodeReferenceArray(Collections.singleton(inputNodeReference)))
              .setFunction(featureAliasFunction)
              .setFeatureName(featureSource.getAlias());
          inputNodeId = transformationNode.getId();
        }

        return new NodeReference().setId(inputNodeId).setKeyReference(keyReferenceArray);
      }).collect(Collectors.toCollection(NodeReferenceArray::new));

      List<String> inputParameterNames = anchor.getSource().stream()
          .map(FeatureSource::getAlias)
          .collect(Collectors.toList());

      TransformationFunction transformationFunction;
      if (anchor.getTransformationFunction().isMvelExpression()) {
        transformationFunction = makeTransformationFunction(anchor.getTransformationFunction().getMvelExpression(),
            inputParameterNames);
      } else if (anchor.getTransformationFunction().isSparkSqlExpression()) {
        transformationFunction = makeTransformationFunction(anchor.getTransformationFunction().getSparkSqlExpression(),
            inputParameterNames);
      } else {
        throw new RuntimeException("Unsupported transformation function " + anchor.getTransformationFunction() + " for"
            + " feature " + featureName);
      }

      Transformation transformationNode = graphBuilder.addNewTransformation()
          .setInputs(inputs)
          .setFunction(transformationFunction)
          .setFeatureName(featureName);

      FeatureDefintion featureDefinition = new FeatureDefintion().setFeatureVersion(version).setNodeId(transformationNode.getId());
      graphBuilder.addFeatureName(featureName, featureDefinition);
      return graphBuilder.build();
    }
  }

  // TODO explain why we do this
  private NodeReference makeNodeReferenceWithSimpleKeyReference(int nodeId, int nKeyParts) {
    return new NodeReference()
        .setId(nodeId)
        .setKeyReference(IntStream.range(0, nKeyParts)
            .mapToObj(i -> new KeyReference().setPosition(i))
            .collect(Collectors.toCollection(KeyReferenceArray::new)));
  }

  // TODO: PROBABLE OPTIMIZATION: IF THE MVEL EXPRESSION IS A SINGLE COLUMN NAME, THEN WE SHOULD MAYBE NOT USE MVEL,
  //       BUT USE A DIFFERENT SIMPLER OPERATOR THAT JUST EXTRACTS THAT ONE FIELD/COLUMN.
  // This one will operate on a struct. There is no argument-ordering-legend to pass in.
  private TransformationFunction makeTransformationFunction(MvelExpression input) {
    return new TransformationFunction()
        .setOperator(Operators.OPERATOR_ID_MVEL)
        .setParameters(new StringMap(Collections.singletonMap("expression", input.getMvel())));
  }

  // TODO: PROBABLE OPTIMIZATION: IF THE MVEL EXPRESSION IS A SINGLE COLUMN NAME, THEN WE SHOULD MAYBE NOT USE MVEL,
  //       BUT USE A DIFFERENT SIMPLER OPERATOR THAT JUST EXTRACTS THAT ONE FIELD/COLUMN.
  // This one will operate on a tuple of inputs (the Feature Derivation case). In this case, the transform function
  // will consume a tuple. A list of names will inform the transformer about how to apply the elements in the tuple
  // (based on their order) to the variable names used in the MVEL expression itself (e.g. feature1, feature2).
  // TODO add a clearer example
  private TransformationFunction makeTransformationFunction(MvelExpression input, List<String> parameterNames) {
    // Treat derivation mvel derived features differently?
    TransformationFunction tf = makeTransformationFunction(input).setOperator(Operators.OPERATOR_ID_EXTRACT_FROM_TUPLE);
    tf.getParameters().put("parameterNames", String.join(",", parameterNames));
    return tf;
  }

  private AggregationFunction makeAggregationFunction(SlidingWindowAggregation input) {
    Map<String, String> parameterMap = new HashMap<>();
    parameterMap.put("target_column", input.getTargetColumn().getSparkSqlExpression().getSql());
    parameterMap.put("aggregation_type", input.getAggregationType().name());
    Duration window = convert(input.getWindow());
    parameterMap.put("window_size", window.toString());
    parameterMap.put("window_unit", input.getWindow().getUnit().name());
    // lateral view expression capability should be rethought
    for (int i = 0; i < input.getLateralViews().size(); i++) {
      parameterMap.put("lateral_view_expression_" + i, input.getLateralViews().get(i)
          .getTableGeneratingFunction().getSparkSqlExpression().getSql());
      parameterMap.put("lateral_view_table_alias_" + i, input.getLateralViews().get(i)
          .getVirtualTableAlias());
    }
    if (input.hasFilter()) {
      parameterMap.put("filter_expression", input.getFilter().getSparkSqlExpression().getSql());
    }
    if (input.hasGroupBy()) {
      parameterMap.put("group_by_expression", input.getGroupBy().getSparkSqlExpression().getSql());
    }
    if (input.hasLimit()) {
      parameterMap.put("max_number_groups", input.getLimit().toString());
    }
    return new AggregationFunction()
        .setOperator(Operators.OPERATOR_ID_SLIDING_WINDOW_AGGREGATION)
        .setParameters(new StringMap(parameterMap));
  }

  /**
   * Resolve Frame-Offline [[TimeWindowParams]] timestamp format String representation from the Pegasus [[TimeFieldFormat]] object
   * @param timeFieldFormat the input Pegasus [[TimeFieldFormat]] object
   * @return the timestamp format String representation that can be used as [[TimeWindowParams.timestampColumnFormat]]
   */
  String resolveTimeFieldFormatTerm(TimeFieldFormat timeFieldFormat) {
    String timeFormat = null;
    if (timeFieldFormat.isTimestampGranularity()) {
      if (timeFieldFormat.getTimestampGranularity() == TimestampGranularity.SECONDS) {
        timeFormat = "EPOCH";
      } else if (timeFieldFormat.getTimestampGranularity() == TimestampGranularity.MILLISECONDS) {
        timeFormat = "EPOCH_MILLIS";
      }
    } else if (timeFieldFormat.isDateTimeFormat()) {
      timeFormat = timeFieldFormat.getDateTimeFormat();
    } else {
      throw new RuntimeException("Unsupported TimeFieldFormat type");
    }
    return timeFormat;
  }

  private AggregationFunction makeAggregationFunction(SlidingWindowEmbeddingAggregation input) {
    Map<String, String> parameterMap = new HashMap<>();
    parameterMap.put("target_column", input.getTargetColumn().getSparkSqlExpression().getSql());
    parameterMap.put("aggregation_type", input.getAggregationType().name());
    Duration window = convert(input.getWindow());
    parameterMap.put("window_size", window.toString());
    parameterMap.put("window_unit", input.getWindow().getUnit().name());
    if (input.hasFilter()) {
      parameterMap.put("filter_expression", input.getFilter().getSparkSqlExpression().getSql());
    }
    if (input.hasGroupBy()) {
      parameterMap.put("group_by_expression", input.getGroupBy().getSparkSqlExpression().getSql());
    }
    return new AggregationFunction()
        .setOperator(Operators.OPERATOR_ID_SLIDING_WINDOW_AGGREGATION)
        .setParameters(new StringMap(parameterMap));
  }




  private TransformationFunction makeTransformationFunction(SparkSqlExpression input) {
    return new TransformationFunction().setOperator(Operators.OPERATOR_ID_SPARK_SQL_FEATURE_EXTRACTOR)
        .setParameters(new StringMap(Collections.singletonMap("expression", input.getSql())));
  }

  private TransformationFunction makeTransformationFunction(SparkSqlExpression input, List<String> parameterNames) {
    TransformationFunction tf = makeTransformationFunction(input);
    tf.getParameters().put("parameterNames", String.join(",", parameterNames));
    return tf;
  }

  private TransformationFunction makeTransformationFunction(UserDefinedFunction input) {
    Map<String, String> parameterMap = new HashMap<>();
    parameterMap.put("class", input.getClazz().getFullyQualifiedName());
    input.getParameters().forEach((userParamName, userParamValue) -> {
      parameterMap.put("userParam_" + userParamName, userParamValue);
    });
    return new TransformationFunction()
        .setOperator(Operators.OPERATOR_ID_JAVA_UDF_FEATURE_EXTRACTOR)
        .setParameters(new StringMap(parameterMap));
    // TODO: WHAT IF THE UDF PRODUCES A TUPLE? E.g. anchor extractors that produce many features. Need to have a separate
    //       node that extracts the requested one?
  }

  private Duration convert(Window frWindow) {
    int size = frWindow.getSize();
    if (frWindow.getUnit() == Unit.DAY) {
      return Duration.ofDays(size);
    } else if (frWindow.getUnit() == Unit.HOUR) {
      return Duration.ofHours(size);
    } else if (frWindow.getUnit() == Unit.MINUTE) {
      return Duration.ofMinutes(size);
    } else if (frWindow.getUnit() == Unit.SECOND) {
      return Duration.ofSeconds(size);
    } else {
      throw new RuntimeException("'window' field($frWindow) is not correctly set. The correct example \" +\n"
          + "            \"can be '1d'(1 day) or '2h'(2 hour) or '3m'(3 minute) or '4s'(4 second) ");
    }
  }

  private AggregationFunction makeAggregationFunction(SlidingWindowLatestAvailable input) {
    Map<String, String> parameterMap = new HashMap<>();
    parameterMap.put("target_column", input.getTargetColumn().getSparkSqlExpression().getSql());
    parameterMap.put("aggregation_type", "LATEST");
    Duration window = convert(input.getWindow());
    parameterMap.put("window_size", window.toString());
    parameterMap.put("window_unit", input.getWindow().getUnit().name());
    // lateral view expression capability should be rethought
    for (int i = 0; i < input.getLateralViews().size(); i++) {
      parameterMap.put("lateral_view_expression_" + i, input.getLateralViews().get(i)
          .getTableGeneratingFunction().getSparkSqlExpression().getSql());
      parameterMap.put("lateral_view_table_alias_" + i, input.getLateralViews().get(i)
          .getVirtualTableAlias());
    }
    if (input.hasFilter()) {
      parameterMap.put("filter_expression", input.getFilter().getSparkSqlExpression().getSql());
    }
    if (input.hasGroupBy()) {
      parameterMap.put("group_by_expression", input.getGroupBy().getSparkSqlExpression().getSql());
    }
    if (input.hasLimit()) {
      parameterMap.put("max_number_groups", input.getLimit().toString());
    }
    return new AggregationFunction()
        .setOperator(Operators.OPERATOR_ID_SLIDING_WINDOW_AGGREGATION)
        .setParameters(new StringMap(parameterMap));
  }

  private RecordTemplate makeTransformationOrAggregationFunction(HdfsDataSourceAnchor.TransformationFunction input) {
    if (input.isMvelExpression()) {
      return makeTransformationFunction(input.getMvelExpression());
    } else if (input.isSlidingWindowAggregation()) {
      return makeAggregationFunction(input.getSlidingWindowAggregation());
    } else if (input.isSparkSqlExpression()) {
      return makeTransformationFunction(input.getSparkSqlExpression());
    } else if (input.isUserDefinedFunction()) {
      return makeTransformationFunction(input.getUserDefinedFunction());
    } else if (input.isSlidingWindowEmbeddingAggregation()) {
      return makeAggregationFunction(input.getSlidingWindowEmbeddingAggregation());
    } else if (input.isSlidingWindowLatestAvailable()) {
      return makeAggregationFunction(input.getSlidingWindowLatestAvailable());
    } else {
      throw new RuntimeException("No known way to handle HdfsDataSourceAnchor.TransformationFunction " + input);
    }
  }

  private TransformationFunction makeTransformationFunction(ObservationPassthroughDataSourceAnchor.TransformationFunction input) {
    if (input.isMvelExpression()) {
      return makeTransformationFunction(input.getMvelExpression());
    } else if (input.isSparkSqlExpression()) {
      return makeTransformationFunction(input.getSparkSqlExpression());
    } else if (input.isUserDefinedFunction()) {
      return makeTransformationFunction(input.getUserDefinedFunction());
    } else {
      throw new RuntimeException("No known way to handle ObservationPassthroughDataSourceAnchor.TransformationFunction " + input);
    }
  }
}
