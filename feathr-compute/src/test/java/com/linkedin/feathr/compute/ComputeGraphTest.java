package com.linkedin.feathr.compute;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.collect.ImmutableMap;
import com.linkedin.frame.common.urn.MlFeatureVersionUrn;
import com.linkedin.data.codec.JacksonDataCodec;
import com.linkedin.data.template.RecordTemplate;
import com.linkedin.data.template.StringMap;
import com.linkedin.frame.config.FeatureDefinition;
import com.linkedin.frame.config.FeatureDefinitionLoaderFactory;
import com.linkedin.frame.config.featureanchor.FeatureAnchorEnvironment;
import com.linkedin.frame.core.configdataprovider.ResourceConfigDataProvider;
import com.linkedin.frame.core.utils.FeatureRefConverter;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.testng.Assert;
import org.testng.annotations.Test;


public class ComputeGraphTest {

  @Test(enabled = false) // something is wrong with the resource directory setup (works in IDE but not with mint build)
  public void testFeatureCareers() throws CloneNotSupportedException {
    Map<MlFeatureVersionUrn, FeatureDefinition> features = FeatureDefinitionLoaderFactory.getInstance()
        .loadAllFeatureDefinitions(new ResourceConfigDataProvider("frame-feature-careers-featureDef-offline.conf"),
            FeatureAnchorEnvironment.OFFLINE);
    ComputeGraph output = ComputeGraphs.removeRedundancies(FeatureDefinitionsConverter.buildFromFeatureUrns(features));
    System.out.println(pegasusToYaml(output));
    System.out.println(output.getNodes().size() + " nodes.");
    Assert.assertEquals(features.keySet().stream().map(FeatureRefConverter.getInstance()::getFeatureRefStr).collect(Collectors.toSet()),
        output.getFeatureNames().keySet());
  }

  @Test
  public void foo() {
    List<String> inputs = SqlUtil.getInputsFromSqlExpression("A + A");
    System.out.println(inputs);
  }

  @Test
  public void testMergeGraphs() throws Exception {
    DataSource dataSource1 = new DataSource().setId(0).setSourceType(DataSourceType.UPDATE).setExternalSourceRef("foo");
    Transformation transformation1 = new Transformation().setId(1)
        .setInputs(new NodeReferenceArray(new NodeReference().setId(0).setKeyReference(new KeyReferenceArray(new KeyReference().setPosition(0)))))
        .setFunction(new TransformationFunction().setOperator("foo:bar:1").setParameters(new StringMap(Collections.singletonMap("foo", "bar"))));
    AnyNodeArray nodeArray1 = new AnyNodeArray(AnyNode.create(dataSource1), AnyNode.create(transformation1));
    FeatureDefintionMap featureNameMap1 = new FeatureDefintionMap(Collections.singletonMap("baz", new FeatureDefintion().setNodeId(1)));
    ComputeGraph graph1 = new ComputeGraph().setNodes(nodeArray1).setFeatureNames(featureNameMap1);

    DataSource dataSource2 = new DataSource().setId(0).setSourceType(DataSourceType.UPDATE).setExternalSourceRef("bar");
    Transformation transformation2 = new Transformation().setId(1)
        .setInputs(new NodeReferenceArray((new NodeReference().setId(0).setKeyReference(new KeyReferenceArray(new KeyReference().setPosition(0))))))
        .setFunction(new TransformationFunction().setOperator("foo:baz:1"));
    Transformation transformation3 = new Transformation().setId(2)
        .setInputs(new NodeReferenceArray((new NodeReference().setId(1).setKeyReference(new KeyReferenceArray(new KeyReference().setPosition(0))))))
        .setFunction(new TransformationFunction().setOperator("foo:foo:2"));
    AnyNodeArray nodeArray2 = new AnyNodeArray(AnyNode.create(dataSource2), AnyNode.create(transformation2), AnyNode.create(transformation3));
    FeatureDefintionMap featureNameMap2 = new FeatureDefintionMap(ImmutableMap.of("fizz", new FeatureDefintion().setNodeId(1),
        "buzz", new FeatureDefintion().setNodeId(1)));
    ComputeGraph graph2 = new ComputeGraph().setNodes(nodeArray2).setFeatureNames(featureNameMap2);

    ComputeGraph merged = ComputeGraphs.merge(Arrays.asList(graph1, graph2));
    System.out.println(pegasusToYaml(merged));
  }

  @Test
  public void testMergeGraphWithFeatureDependencies() {
    External featureReference1 = new External().setId(0).setName("feature1");
    Transformation transformation1 = new Transformation().setId(1)
        .setInputs(new NodeReferenceArray(new NodeReference().setId(0).setKeyReference(new KeyReferenceArray(new KeyReference().setPosition(0)))))
        .setFunction(new TransformationFunction().setOperator("foobar1"));
    AnyNodeArray nodeArray1 = new AnyNodeArray(AnyNode.create(featureReference1), AnyNode.create(transformation1));
    FeatureDefintionMap featureNameMap1 = new FeatureDefintionMap(Collections.singletonMap("apple", new FeatureDefintion().setNodeId(1)));
    ComputeGraph graph1 = new ComputeGraph().setNodes(nodeArray1).setFeatureNames(featureNameMap1);

    External featureReference2 = new External().setId(0).setName("feature2");
    Transformation transformation2 = new Transformation().setId(1)
        .setInputs(new NodeReferenceArray(new NodeReference().setId(0).setKeyReference(new KeyReferenceArray(new KeyReference().setPosition(0)))))
        .setFunction(new TransformationFunction().setOperator("foobar2"));
    AnyNodeArray nodeArray2 = new AnyNodeArray(AnyNode.create(featureReference2), AnyNode.create(transformation2));
    FeatureDefintionMap featureNameMap2 = new FeatureDefintionMap(Collections.singletonMap("feature1", new FeatureDefintion().setNodeId(1)));
    ComputeGraph graph2 = new ComputeGraph().setNodes(nodeArray2).setFeatureNames(featureNameMap2);

    ComputeGraph merged = ComputeGraphs.merge(Arrays.asList(graph1, graph2));
    System.out.println(pegasusToYaml(merged));
  }

  @Test
  public void testRemoveDuplicates() throws CloneNotSupportedException {
    External featureReference1 = new External().setId(0).setName("feature1");
    Transformation transformation1 = new Transformation().setId(1)
        .setInputs(new NodeReferenceArray(new NodeReference().setId(0).setKeyReference(new KeyReferenceArray(new KeyReference().setPosition(0)))))
        .setFunction(new TransformationFunction().setOperator("foobar1"));
    External featureReference2 = new External().setId(2).setName("feature1");
    Transformation transformation2 = new Transformation().setId(3)
        .setInputs(new NodeReferenceArray(new NodeReference().setId(2).setKeyReference(new KeyReferenceArray(new KeyReference().setPosition(0)))))
        .setFunction(new TransformationFunction().setOperator("foobar2"));
    AnyNodeArray nodeArray = new AnyNodeArray(
        AnyNode.create(featureReference1),
        AnyNode.create(featureReference2),
        AnyNode.create(transformation1),
        AnyNode.create(transformation2));
    FeatureDefintionMap featureNameMap = new FeatureDefintionMap(ImmutableMap.of("apple", new FeatureDefintion().setNodeId(1),
        "banana", new FeatureDefintion().setNodeId(3)));
    ComputeGraph graph = new ComputeGraph().setNodes(nodeArray).setFeatureNames(featureNameMap);

    System.out.println(pegasusToJson(graph));
    ComputeGraph simplified = ComputeGraphs.removeRedundancies(graph);
    System.out.println(pegasusToJson(simplified));
  }

  @Test(enabled = false)
  public void testResolveGraph() throws CloneNotSupportedException {
    DataSource dataSource1 = new DataSource().setId(0).setSourceType(DataSourceType.UPDATE).setExternalSourceRef("dataSource1");
    Transformation transformation1 = new Transformation().setId(1)
        .setInputs(new NodeReferenceArray(new NodeReference().setId(0).setKeyReference(new KeyReferenceArray(new KeyReference().setPosition(0)))))
        .setFunction(new TransformationFunction().setOperator("foobar1"));
    AnyNodeArray nodeArray1 = new AnyNodeArray(AnyNode.create(dataSource1), AnyNode.create(transformation1));
    FeatureDefintionMap featureNameMap1 = new FeatureDefintionMap(Collections.singletonMap("apple", new FeatureDefintion().setNodeId(1)));
    ComputeGraph graph1 = new ComputeGraph().setNodes(nodeArray1).setFeatureNames(featureNameMap1);

    System.out.println(pegasusToYaml(graph1));

    List<Resolver.FeatureRequest> requestedFeatures = Arrays.asList(
        new Resolver.FeatureRequest("apple", Collections.singletonList("viewer"), Duration.ZERO, "apple__viewer"),
        new Resolver.FeatureRequest("apple", Collections.singletonList("viewee"), Duration.ZERO, "apple__viewee"));
    ComputeGraph resolved = Resolver.create(graph1).resolveForRequest(requestedFeatures);

    System.out.println(pegasusToYaml(resolved));
//
//    External featureReference2 = new External().setId(0).setName("feature2");
//    Transformation transformation2 = new Transformation().setId(1)
//        .setInputs(new NodeReferenceArray(new NodeReference().setId(0).setKeyReference(new KeyReferenceArray(new KeyReference().setPosition(0)))))
//        .setFunction(new TransformationFunction().setOperator("foobar2"));
//    AnyNodeArray nodeArray2 = new AnyNodeArray(AnyNode.create(featureReference2), AnyNode.create(transformation2));
//    IntegerMap featureNameMap2 = new IntegerMap(Collections.singletonMap("feature1", 1));
//    ComputeGraph graph2 = new ComputeGraph().setNodes(nodeArray2).setFeatureNames(featureNameMap2);
//
//    ComputeGraph merged = Graphs.merge(Arrays.asList(graph1, graph2));

  }

  private String pegasusToYaml(RecordTemplate record) {
    try {
      String json = new JacksonDataCodec().mapToString(record.data());
      Object node = new ObjectMapper().readTree(json);
      return new ObjectMapper(new YAMLFactory()).writeValueAsString(node);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private String pegasusToJson(RecordTemplate record) {
    try {
      JacksonDataCodec codec = new JacksonDataCodec();
      codec.setPrettyPrinter(new DefaultPrettyPrinter());
      return codec.mapToString(record.data());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
