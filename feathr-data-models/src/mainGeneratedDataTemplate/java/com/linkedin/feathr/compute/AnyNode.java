
package com.linkedin.feathr.compute;

import java.util.List;
import java.util.function.Function;
import javax.annotation.Generated;
import com.linkedin.data.DataMap;
import com.linkedin.data.collections.CheckedUtil;
import com.linkedin.data.schema.DataSchema;
import com.linkedin.data.schema.MaskMap;
import com.linkedin.data.schema.PathSpec;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.schema.TyperefDataSchema;
import com.linkedin.data.schema.UnionDataSchema;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.HasTyperefInfo;
import com.linkedin.data.template.TyperefInfo;
import com.linkedin.data.template.UnionTemplate;

@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/compute/AnyNode.pdl.")
public class AnyNode
    extends UnionTemplate
    implements HasTyperefInfo
{

    private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.compute,record Aggregation includes record AbstractNode{id:typeref NodeId=int/**The key for which this node is being requested.\nIf this node is a Source node, the engine can use the key to fetch or join the feature.\nIf this node is NOT a Source node, the engine should NOT use the key to determine fetch/join behavior, but\nshould follow the node's inputs. (The core libraries may use the key information in order to optimize the graph,\ne.g. it can be used for identifying duplicate sections of the graph that can be pruned.)\n\nTODO: From the Engines' point of view, this field should be private. Maybe we should consider revising the data model.*/concreteKey:optional record ConcreteKey{key:array[NodeId]}}{input:record NodeReference{id:NodeId,keyReference:array[record KeyReference{position:int}]}function:record AggregationFunction{operator:typeref OperatorId=string,parameters:optional map[string,string]}featureName:optional string}}{namespace com.linkedin.feathr.compute,record DataSource includes AbstractNode{sourceType:enum DataSourceType{UPDATE,EVENT,CONTEXT}externalSourceRef:string,keyExpression:string,filePartitionFormat:optional string,timestampColumnInfo:optional record TimestampCol{expression:string,format:string}window:optional{namespace com.linkedin.feathr.featureDataModel/**Represents a time window used in sliding window algorithms.*/record Window{/**Represents the duration of the window.*/size:int/**Represents a unit of time.*/unit:enum Unit{/** A day. */DAY/** An hour. */HOUR/** A minute. */MINUTE/** A second. */SECOND}}}}}{namespace com.linkedin.feathr.compute,record Lookup includes AbstractNode{lookupKey:array[union[NodeReference,KeyReference]]lookupNode:NodeId,aggregation:string,featureName:optional string}}{namespace com.linkedin.feathr.compute,record Transformation includes AbstractNode{inputs:array[NodeReference]function:record TransformationFunction{operator:OperatorId,parameters:optional map[string,string]}featureName:optional string}}{namespace com.linkedin.feathr.compute,record External includes AbstractNode{name:string}}]", SchemaFormatType.PDL));
    private com.linkedin.feathr.compute.Aggregation _aggregationMember = null;
    private com.linkedin.feathr.compute.DataSource _dataSourceMember = null;
    private com.linkedin.feathr.compute.Lookup _lookupMember = null;
    private com.linkedin.feathr.compute.Transformation _transformationMember = null;
    private com.linkedin.feathr.compute.External _externalMember = null;
    private AnyNode.ChangeListener __changeListener = new AnyNode.ChangeListener(this);
    private final static DataSchema MEMBER_Aggregation = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.compute.Aggregation");
    private final static DataSchema MEMBER_DataSource = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.compute.DataSource");
    private final static DataSchema MEMBER_Lookup = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.compute.Lookup");
    private final static DataSchema MEMBER_Transformation = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.compute.Transformation");
    private final static DataSchema MEMBER_External = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.compute.External");
    private final static TyperefInfo TYPEREFINFO = new AnyNode.UnionTyperefInfo();

    public AnyNode() {
        super(new DataMap(2, 0.75F), SCHEMA);
        addChangeListener(__changeListener);
    }

    public AnyNode(Object data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static UnionDataSchema dataSchema() {
        return SCHEMA;
    }

    public static AnyNode create(com.linkedin.feathr.compute.Aggregation value) {
        AnyNode newUnion = new AnyNode();
        newUnion.setAggregation(value);
        return newUnion;
    }

    public boolean isAggregation() {
        return memberIs("com.linkedin.feathr.compute.Aggregation");
    }

    public com.linkedin.feathr.compute.Aggregation getAggregation() {
        checkNotNull();
        if (_aggregationMember!= null) {
            return _aggregationMember;
        }
        Object __rawValue = super._map.get("com.linkedin.feathr.compute.Aggregation");
        _aggregationMember = ((__rawValue == null)?null:new com.linkedin.feathr.compute.Aggregation(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
        return _aggregationMember;
    }

    public void setAggregation(com.linkedin.feathr.compute.Aggregation value) {
        checkNotNull();
        super._map.clear();
        _aggregationMember = value;
        CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.compute.Aggregation", value.data());
    }

    public static AnyNode create(com.linkedin.feathr.compute.DataSource value) {
        AnyNode newUnion = new AnyNode();
        newUnion.setDataSource(value);
        return newUnion;
    }

    public boolean isDataSource() {
        return memberIs("com.linkedin.feathr.compute.DataSource");
    }

    public com.linkedin.feathr.compute.DataSource getDataSource() {
        checkNotNull();
        if (_dataSourceMember!= null) {
            return _dataSourceMember;
        }
        Object __rawValue = super._map.get("com.linkedin.feathr.compute.DataSource");
        _dataSourceMember = ((__rawValue == null)?null:new com.linkedin.feathr.compute.DataSource(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
        return _dataSourceMember;
    }

    public void setDataSource(com.linkedin.feathr.compute.DataSource value) {
        checkNotNull();
        super._map.clear();
        _dataSourceMember = value;
        CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.compute.DataSource", value.data());
    }

    public static AnyNode create(com.linkedin.feathr.compute.Lookup value) {
        AnyNode newUnion = new AnyNode();
        newUnion.setLookup(value);
        return newUnion;
    }

    public boolean isLookup() {
        return memberIs("com.linkedin.feathr.compute.Lookup");
    }

    public com.linkedin.feathr.compute.Lookup getLookup() {
        checkNotNull();
        if (_lookupMember!= null) {
            return _lookupMember;
        }
        Object __rawValue = super._map.get("com.linkedin.feathr.compute.Lookup");
        _lookupMember = ((__rawValue == null)?null:new com.linkedin.feathr.compute.Lookup(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
        return _lookupMember;
    }

    public void setLookup(com.linkedin.feathr.compute.Lookup value) {
        checkNotNull();
        super._map.clear();
        _lookupMember = value;
        CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.compute.Lookup", value.data());
    }

    public static AnyNode create(com.linkedin.feathr.compute.Transformation value) {
        AnyNode newUnion = new AnyNode();
        newUnion.setTransformation(value);
        return newUnion;
    }

    public boolean isTransformation() {
        return memberIs("com.linkedin.feathr.compute.Transformation");
    }

    public com.linkedin.feathr.compute.Transformation getTransformation() {
        checkNotNull();
        if (_transformationMember!= null) {
            return _transformationMember;
        }
        Object __rawValue = super._map.get("com.linkedin.feathr.compute.Transformation");
        _transformationMember = ((__rawValue == null)?null:new com.linkedin.feathr.compute.Transformation(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
        return _transformationMember;
    }

    public void setTransformation(com.linkedin.feathr.compute.Transformation value) {
        checkNotNull();
        super._map.clear();
        _transformationMember = value;
        CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.compute.Transformation", value.data());
    }

    public static AnyNode create(com.linkedin.feathr.compute.External value) {
        AnyNode newUnion = new AnyNode();
        newUnion.setExternal(value);
        return newUnion;
    }

    public boolean isExternal() {
        return memberIs("com.linkedin.feathr.compute.External");
    }

    public com.linkedin.feathr.compute.External getExternal() {
        checkNotNull();
        if (_externalMember!= null) {
            return _externalMember;
        }
        Object __rawValue = super._map.get("com.linkedin.feathr.compute.External");
        _externalMember = ((__rawValue == null)?null:new com.linkedin.feathr.compute.External(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
        return _externalMember;
    }

    public void setExternal(com.linkedin.feathr.compute.External value) {
        checkNotNull();
        super._map.clear();
        _externalMember = value;
        CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.compute.External", value.data());
    }

    public static AnyNode.ProjectionMask createMask() {
        return new AnyNode.ProjectionMask();
    }

    @Override
    public AnyNode clone()
        throws CloneNotSupportedException
    {
        AnyNode __clone = ((AnyNode) super.clone());
        __clone.__changeListener = new AnyNode.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public AnyNode copy()
        throws CloneNotSupportedException
    {
        AnyNode __copy = ((AnyNode) super.copy());
        __copy._lookupMember = null;
        __copy._transformationMember = null;
        __copy._aggregationMember = null;
        __copy._dataSourceMember = null;
        __copy._externalMember = null;
        __copy.__changeListener = new AnyNode.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    public TyperefInfo typerefInfo() {
        return TYPEREFINFO;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final AnyNode __objectRef;

        private ChangeListener(AnyNode reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "com.linkedin.feathr.compute.Lookup":
                    __objectRef._lookupMember = null;
                    break;
                case "com.linkedin.feathr.compute.Transformation":
                    __objectRef._transformationMember = null;
                    break;
                case "com.linkedin.feathr.compute.Aggregation":
                    __objectRef._aggregationMember = null;
                    break;
                case "com.linkedin.feathr.compute.DataSource":
                    __objectRef._dataSourceMember = null;
                    break;
                case "com.linkedin.feathr.compute.External":
                    __objectRef._externalMember = null;
                    break;
            }
        }

    }

    public static class Fields
        extends PathSpec
    {


        public Fields(List<String> path, String name) {
            super(path, name);
        }

        public Fields() {
            super();
        }

        public com.linkedin.feathr.compute.Aggregation.Fields Aggregation() {
            return new com.linkedin.feathr.compute.Aggregation.Fields(getPathComponents(), "com.linkedin.feathr.compute.Aggregation");
        }

        public com.linkedin.feathr.compute.DataSource.Fields DataSource() {
            return new com.linkedin.feathr.compute.DataSource.Fields(getPathComponents(), "com.linkedin.feathr.compute.DataSource");
        }

        public com.linkedin.feathr.compute.Lookup.Fields Lookup() {
            return new com.linkedin.feathr.compute.Lookup.Fields(getPathComponents(), "com.linkedin.feathr.compute.Lookup");
        }

        public com.linkedin.feathr.compute.Transformation.Fields Transformation() {
            return new com.linkedin.feathr.compute.Transformation.Fields(getPathComponents(), "com.linkedin.feathr.compute.Transformation");
        }

        public com.linkedin.feathr.compute.External.Fields External() {
            return new com.linkedin.feathr.compute.External.Fields(getPathComponents(), "com.linkedin.feathr.compute.External");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.compute.Aggregation.ProjectionMask _AggregationMask;
        private com.linkedin.feathr.compute.DataSource.ProjectionMask _DataSourceMask;
        private com.linkedin.feathr.compute.Lookup.ProjectionMask _LookupMask;
        private com.linkedin.feathr.compute.Transformation.ProjectionMask _TransformationMask;
        private com.linkedin.feathr.compute.External.ProjectionMask _ExternalMask;

        ProjectionMask() {
            super(7);
        }

        public AnyNode.ProjectionMask withAggregation(Function<com.linkedin.feathr.compute.Aggregation.ProjectionMask, com.linkedin.feathr.compute.Aggregation.ProjectionMask> nestedMask) {
            _AggregationMask = nestedMask.apply(((_AggregationMask == null)?com.linkedin.feathr.compute.Aggregation.createMask():_AggregationMask));
            getDataMap().put("com.linkedin.feathr.compute.Aggregation", _AggregationMask.getDataMap());
            return this;
        }

        public AnyNode.ProjectionMask withDataSource(Function<com.linkedin.feathr.compute.DataSource.ProjectionMask, com.linkedin.feathr.compute.DataSource.ProjectionMask> nestedMask) {
            _DataSourceMask = nestedMask.apply(((_DataSourceMask == null)?com.linkedin.feathr.compute.DataSource.createMask():_DataSourceMask));
            getDataMap().put("com.linkedin.feathr.compute.DataSource", _DataSourceMask.getDataMap());
            return this;
        }

        public AnyNode.ProjectionMask withLookup(Function<com.linkedin.feathr.compute.Lookup.ProjectionMask, com.linkedin.feathr.compute.Lookup.ProjectionMask> nestedMask) {
            _LookupMask = nestedMask.apply(((_LookupMask == null)?com.linkedin.feathr.compute.Lookup.createMask():_LookupMask));
            getDataMap().put("com.linkedin.feathr.compute.Lookup", _LookupMask.getDataMap());
            return this;
        }

        public AnyNode.ProjectionMask withTransformation(Function<com.linkedin.feathr.compute.Transformation.ProjectionMask, com.linkedin.feathr.compute.Transformation.ProjectionMask> nestedMask) {
            _TransformationMask = nestedMask.apply(((_TransformationMask == null)?com.linkedin.feathr.compute.Transformation.createMask():_TransformationMask));
            getDataMap().put("com.linkedin.feathr.compute.Transformation", _TransformationMask.getDataMap());
            return this;
        }

        public AnyNode.ProjectionMask withExternal(Function<com.linkedin.feathr.compute.External.ProjectionMask, com.linkedin.feathr.compute.External.ProjectionMask> nestedMask) {
            _ExternalMask = nestedMask.apply(((_ExternalMask == null)?com.linkedin.feathr.compute.External.createMask():_ExternalMask));
            getDataMap().put("com.linkedin.feathr.compute.External", _ExternalMask.getDataMap());
            return this;
        }

    }


    /**
     * 
     * 
     */
    private final static class UnionTyperefInfo
        extends TyperefInfo
    {

        private final static TyperefDataSchema SCHEMA = ((TyperefDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute,typeref AnyNode=union[record Aggregation includes record AbstractNode{id:typeref NodeId=int/**The key for which this node is being requested.\nIf this node is a Source node, the engine can use the key to fetch or join the feature.\nIf this node is NOT a Source node, the engine should NOT use the key to determine fetch/join behavior, but\nshould follow the node's inputs. (The core libraries may use the key information in order to optimize the graph,\ne.g. it can be used for identifying duplicate sections of the graph that can be pruned.)\n\nTODO: From the Engines' point of view, this field should be private. Maybe we should consider revising the data model.*/concreteKey:optional record ConcreteKey{key:array[NodeId]}}{input:record NodeReference{id:NodeId,keyReference:array[record KeyReference{position:int}]}function:record AggregationFunction{operator:typeref OperatorId=string,parameters:optional map[string,string]}featureName:optional string}record DataSource includes AbstractNode{sourceType:enum DataSourceType{UPDATE,EVENT,CONTEXT}externalSourceRef:string,keyExpression:string,filePartitionFormat:optional string,timestampColumnInfo:optional record TimestampCol{expression:string,format:string}window:optional{namespace com.linkedin.feathr.featureDataModel/**Represents a time window used in sliding window algorithms.*/record Window{/**Represents the duration of the window.*/size:int/**Represents a unit of time.*/unit:enum Unit{/** A day. */DAY/** An hour. */HOUR/** A minute. */MINUTE/** A second. */SECOND}}}}record Lookup includes AbstractNode{lookupKey:array[union[NodeReference,KeyReference]]lookupNode:NodeId,aggregation:string,featureName:optional string}record Transformation includes AbstractNode{inputs:array[NodeReference]function:record TransformationFunction{operator:OperatorId,parameters:optional map[string,string]}featureName:optional string}record External includes AbstractNode{name:string}]", SchemaFormatType.PDL));

        public UnionTyperefInfo() {
            super(SCHEMA);
        }

        public static TyperefDataSchema dataSchema() {
            return SCHEMA;
        }

    }

}
