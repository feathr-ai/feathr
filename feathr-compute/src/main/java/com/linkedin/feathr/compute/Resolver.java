package com.linkedin.feathr.compute;

import com.linkedin.data.template.IntegerArray;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


// TODO rename to JoinPlanner?
public class Resolver {
  private final ComputeGraph _definitionGraph;

  public Resolver(ComputeGraph graph) {
    _definitionGraph = ComputeGraphs.validate(graph);
    // TODO should also validate that this is an unlinked/definition graph (contains no concrete keys attached)
  }

  public static Resolver create(ComputeGraph graph) {
    return new Resolver(graph);
  }

  public ComputeGraph resolveForRequest(List<FeatureRequest> featureRequestList) throws CloneNotSupportedException {
    // preconditions
    // 1. all requested features are defined in the graph
    // 2. no colliding output-feature-names
    // 3. right number of keys for each feature (this would be quite hard to verity! without more info in the model.)

    List<ComputeGraph> graphParts = featureRequestList.stream()
        .map(request -> {
          try {
            return resolveForRequest(request);
          } catch (CloneNotSupportedException e) {
            e.printStackTrace();
          }
          return null;
        })
        .collect(Collectors.toList());

    return ComputeGraphs.removeRedundancies(ComputeGraphs.merge(graphParts));
  }

  public ComputeGraph resolveForRequest(FeatureRequest featureRequest) throws CloneNotSupportedException {
    return resolveForFeature(featureRequest._featureName, featureRequest._keys, featureRequest._timeDelay, featureRequest._alias);
  }

  public ComputeGraph resolveForFeature(String featureName, List<String> keys, Duration timeDelay, String alias)
      throws CloneNotSupportedException {
    if (!_definitionGraph.getFeatureNames().containsKey(featureName)) {
      throw new IllegalArgumentException("Feature graph does not contain requested feature " + featureName);
    }
    if (alias == null) {
      alias = featureName;
    }
    ComputeGraphBuilder builder = new ComputeGraphBuilder();

    ConcreteKey concreteKey = new ConcreteKey().setKey(new IntegerArray());
    keys.forEach(key -> {
      DataSource source = builder.addNewDataSource()
          .setSourceType(DataSourceType.CONTEXT)
          .setExternalSourceRef(key);
      concreteKey.getKey().add(source.getId());
    });

    ConcreteKeyAttacher concreteKeyAttacher = new ConcreteKeyAttacher(builder);
    int rootNodeId = concreteKeyAttacher.addNodeAndAttachKey(_definitionGraph.getFeatureNames().get(featureName).getNodeId(), concreteKey, timeDelay.toDays());
    FeatureDefintion copyFeatureDefintion = _definitionGraph.getFeatureNames().get(featureName).copy();
    builder.addFeatureName(alias, copyFeatureDefintion.setNodeId(rootNodeId));

    return builder.build();
  }

  private class ConcreteKeyAttacher {
    private final ComputeGraphBuilder _builder;

    public ConcreteKeyAttacher(ComputeGraphBuilder builder) {
      _builder = builder;
    }

    /**
     * @param nodeId node id in the original (definition) feature graph
     * @param key the "concrete key" to attach. references should be into the new (resolved) graph.
     * @return the node id of the newly created counterpart node in the new (resolved) graph
     */
    int addNodeAndAttachKey(int nodeId, ConcreteKey key, Long timeDelay) {
      AnyNode node = _definitionGraph.getNodes().get(nodeId);
      if (PegasusUtils.hasConcreteKey(node)) {
        throw new RuntimeException("Assertion failed. Did not expect to encounter key-annotated node");
      }
      AnyNode newNode = PegasusUtils.copy(node);
      PegasusUtils.setConcreteKey(newNode, key);
      attachKeyToDependencies(newNode, key, timeDelay);
      return _builder.addNode(newNode);
    }

    /**
     * @param nodeId node id in the original (definition) feature graph
     * @param key the "concrete key" to attach. references should be into the new (resolved) graph.
     * @return the node id of the newly created counterpart node in the new (resolved) graph
     */
    int addNodeAndAttachKey(int nodeId, ConcreteKey key) {
      return addNodeAndAttachKey(nodeId, key, 0L);
    }

    private void attachKeyToDependencies(AnyNode node, ConcreteKey key, Long timeDelay) {
      if (node.isAggregation()) {
        attachKeyToDependencies(node.getAggregation(), key, timeDelay);
      } else if (node.isDataSource()) {
        attachKeyToDependencies(node.getDataSource(), key);
      } else if (node.isLookup()) {
        attachKeyToDependencies(node.getLookup(), key);
      } else if (node.isTransformation()) {
        attachKeyToDependencies(node.getTransformation(), key);
      } else if (node.isExternal()) {
        attachKeyToDependencies(node.getExternal(), key);
      } else {
        throw new RuntimeException("Unhandled kind of AnyNode: " + node);
      }
    }

    private void attachKeyToDependencies(Aggregation node, ConcreteKey key, Long timeDelay) {
      NodeReference childNodeReference = node.getInput();
      if (_definitionGraph.getNodes().get(childNodeReference.getId()).isDataSource()) {
        ArrayList keyReferenceArray = new ArrayList<KeyReference>();
        for (int i = 0; i < key.getKey().size(); i++) {
          keyReferenceArray.add(new KeyReference().setPosition(i));
        }

        KeyReferenceArray keyReferenceArray1 = new KeyReferenceArray(keyReferenceArray);
        childNodeReference.setKeyReference(keyReferenceArray1);
      }
      ConcreteKey childKey = transformConcreteKey(key, childNodeReference.getKeyReference());
      int childDefinitionNodeId = childNodeReference.getId();
      int resolvedChildNodeId = addNodeAndAttachKey(childDefinitionNodeId, childKey, timeDelay);
      childNodeReference.setId(resolvedChildNodeId);
    }

    private void attachKeyToDependencies(DataSource node, ConcreteKey key) {
      // no dependencies
      if (node.hasSourceType() && node.getSourceType() == DataSourceType.UPDATE) {
        node.setConcreteKey(key);
      }
      // TODO what if they tried to attach a key to a CONTEXT DataSource node?
    }

    // TODO need to add some explanations here
    private void attachKeyToDependencies(Lookup node, ConcreteKey inputConcreteKey) {
      ConcreteKey concreteLookupKey = new ConcreteKey().setKey(new IntegerArray());
      IntegerArray concreteKeyClone = new IntegerArray();
      concreteKeyClone.addAll(inputConcreteKey.getKey());
      ConcreteKey inputConcreteKeyClone = new ConcreteKey().setKey(concreteKeyClone);
      node.getLookupKey().forEach(lookupKeyPart -> {
        if (lookupKeyPart.isKeyReference()) {
          int relativeKey = lookupKeyPart.getKeyReference().getPosition();
          concreteLookupKey.getKey().add(inputConcreteKeyClone.getKey().get(relativeKey));
        } else if (lookupKeyPart.isNodeReference()) {
          NodeReference childNodeReference = lookupKeyPart.getNodeReference();
          ConcreteKey childConcreteKey = transformConcreteKey(inputConcreteKey, childNodeReference.getKeyReference());
          int childDefinitionNodeId = childNodeReference.getId();
          int resolvedChildNodeId = 0;
          resolvedChildNodeId = addNodeAndAttachKey(childDefinitionNodeId, childConcreteKey);

          IntegerArray keysToBeRemoved = childConcreteKey.getKey();
          inputConcreteKey.getKey().removeAll(keysToBeRemoved);
          childNodeReference.setId(resolvedChildNodeId);
          concreteLookupKey.getKey().add(resolvedChildNodeId);
        } else {
          throw new RuntimeException("Unhandled kind of LookupKey: " + lookupKeyPart);
        }
      });

      int lookupDefinitionNodeId = node.getLookupNode();
      int resolvedLookupNodeId = addNodeAndAttachKey(lookupDefinitionNodeId, new ConcreteKey().setKey(concreteLookupKey.getKey()));
      inputConcreteKey.setKey(concreteKeyClone);
      node.setLookupNode(resolvedLookupNodeId);
    }

    private void attachKeyToDependencies(Transformation node, ConcreteKey key) {
      // key.getKey().forEach(x -> node.getInputs().stream().map(NodeReference::getId).collect(Collectors.toSet()).add(x));
          //node.getInputs().add(new NodeReference().setId(x).setKeyReference(new KeyReferenceArray())));
      node.getInputs().forEach(childNodeReference -> {
        if (_definitionGraph.getNodes().get(childNodeReference.getId()).isDataSource()) {
          ArrayList keyReferenceArray = new ArrayList<KeyReference>();
          for (int i = 0; i < key.getKey().size(); i++) {
            keyReferenceArray.add(new KeyReference().setPosition(i));
          }
          KeyReferenceArray keyReferenceArray1 = new KeyReferenceArray(keyReferenceArray);
          childNodeReference.setKeyReference(keyReferenceArray1);
        }

        ConcreteKey childKey = transformConcreteKey(key, childNodeReference.getKeyReference());
        int childDefinitionNodeId = childNodeReference.getId();
        int resolvedChildNodeId = 0;
        resolvedChildNodeId = addNodeAndAttachKey(childDefinitionNodeId, childKey);

        childNodeReference.setId(resolvedChildNodeId);
      });
    }

    private void attachKeyToDependencies(External node, ConcreteKey key) {
      throw new RuntimeException("Internal error: Can't link key to external feature node not defined in this graph.");
    }
  }

  public static class FeatureRequest {
    private final String _featureName;
    private final List<String> _keys;
    private final Duration _timeDelay;
    private final String _alias;

    public FeatureRequest(String featureName, List<String> keys, Duration timeDelay, String alias) {
      _featureName = featureName;
      _keys = keys;
      _timeDelay = timeDelay;
      _alias = alias;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof FeatureRequest)) {
        return false;
      }
      FeatureRequest that = (FeatureRequest) o;
      return Objects.equals(_featureName, that._featureName) && Objects.equals(_keys, that._keys) && Objects.equals(
          _alias, that._alias);
    }

    @Override
    public int hashCode() {
      return Objects.hash(_featureName, _keys, _alias);
    }
  }

  /**
   * @param original the original (or parent) key
   * @param keyReference the relative key, whose parts refer to relative positions in the parent key
   * @return the child key obtained by applying the keyReference to the parent key
   */
  private static ConcreteKey transformConcreteKey(ConcreteKey original, KeyReferenceArray keyReference) {
    // TODO THIS is going to be VERY hard to debug and needs condition checks and error messages!
    return new ConcreteKey().setKey(
        keyReference.stream()
            .map(KeyReference::getPosition)
            .map(original.getKey()::get)
            .collect(Collectors.toCollection(IntegerArray::new)));
  }
}
