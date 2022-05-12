
package com.linkedin.feathr.compute;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.annotation.Generated;
import com.linkedin.data.DataMap;
import com.linkedin.data.schema.MapDataSchema;
import com.linkedin.data.schema.MaskMap;
import com.linkedin.data.schema.PathSpec;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.TemplateOutputCastException;
import com.linkedin.data.template.WrappingMapTemplate;

@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/compute/ComputeGraph.pdl.")
public class FeatureDefintionMap
    extends WrappingMapTemplate<FeatureDefintion>
{

    private final static MapDataSchema SCHEMA = ((MapDataSchema) DataTemplateUtil.parseSchema("map[string{namespace com.linkedin.feathr.compute,record FeatureDefintion{nodeId:int,featureVersion:/**A Feature can have multiple FeatureVersions. Versioning of a feature is declared by feature producers per semantic versioning. Every time the definition of a feature changes, a new FeatureVersion should be created. Each FeatureVersion enclosed attributes that don't change across environments.*/@gma.aspect.entity.urn=\"com.linkedin.common.MlFeatureVersionUrn\"record FeatureVersion{/**Defines the high level semantic type of a feature.  The high level semantic types are supported in early version of Frame before Tensorization and will be kept around until a full transition to Tensor types is completed*/type:/**The high level types associated with a feature.  In contrast with TensorFeatureFormat which contains additional metadata about the type of the tensor, this represents the high level semantic types supported by early versions of Frame. See https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Feature+Representation+and+Feature+Type+System for more detais. TODO(PROML-13658): this is expected to be deprecated once the full transition to TensorType is completed*/enum FrameFeatureType{/** Boolean valued feature */BOOLEAN/** Numerically valued feature such as INT, LONG, DOUBLE, etc */NUMERIC/** Represents a feature that consists of a single category (e.g. MOBILE, DESKSTOP) */CATEGORICAL/** Represents a feature that consists of multiple categories (e.g. MOBILE, DESKSTOP) */CATEGORICAL_SET/** Represents a feature in vector format where the the majority of the elements are non-zero */DENSE_VECTOR/** Represents features that has string terms and numeric value*/TERM_VECTOR/** Represents tensor based features.  Note: this represents the high level semantic tensor type but does not include the low level tensor format such as category, shape, dimension and value types.  The latter are defined as part of the new tensor annotation (via TensorFeatureFormat) or the legacy FML (go/FML).*/TENSOR/** Placeholder for when no types are specified */UNSPECIFIED}=\"UNSPECIFIED\"/**Defines the format of feature data. Feature data is produced by applying transformation on source, in a FeatureAnchor. Frame will make some default assumptions if FeatureFormat is not provided, but this should be considered limited support, and format should be defined for all new features.*/format:optional/**Defines the format of feature data. Feature data is produced by applying transformation on source, in a FeatureAnchor. Tensor is used to represent feature data. A tensor is a generalization of vectors and matrices to potentially higher dimensions. In Quince Tensor specifically, the last column is designated as the value, and the rest of the columns are keys (aka dimensions). Each row defines a single key/value pair, each column can have a different type. For more details, refer to doc: https://docs.google.com/document/d/1D3JZWBwI7sgHrNzkHZwV3YNEHn69lZcl4VfhdHVmDJo/edit#. Currently in Frame, there are two ways to specify Feature formats, one is via Name-Term-Value (NTV) types (eg. NUMERIC, TERM_VECTOR, CATEGORICAL, see go/featuretypes), the other is via FML metadata (Feature Metadata Library, go/fml). For NTV types, there is a conversion path to Quince Tensor via Auto Tensorization. Existing NTV types can be mapped to different combinations of valueType and dimensionTypes in a deterministic manner. Refer to doc: https://docs.google.com/document/d/10bJMYlCixhsghCtyD08FsQaoQdAJMcpGnRyGe64TSr4/edit#. Feature owners can choose to define FML metadata (eg. valType, dimension's type, etc, see go/fml), which will also be converted to Quince Tensor internally. The data model in this class should be able to uniformly represent both cases.*/record TensorFeatureFormat{/**Type of the tensor, for example, dense tensor.*/tensorCategory:/**Supported Tensor categories in Frame and Quince.*/enum TensorCategory{/**Dense tensors store values in a contiguous sequential block of memory where all values are represented.*/DENSE/**Sparse tensor represents a dataset in which most of the entries are zero. It does not store the whole values of the tensor object but stores the non-zero values and the corresponding coordinates of them.*/SPARSE/**Ragged tensors (also known as nested tensors) are similar to dense tensors but have variable-length dimensions.*/RAGGED}/**Type of the value column.*/valueType:/**Tensor is used to represent feature data. A tensor is a generalization of vectors and matrices to potentially higher dimensions. In Quince Tensor specifically, the last column is designated as the value, and the rest of the columns are keys (or dimensions); Each row defines a single key/value pair. This enum defines supported value types for tensors in Quince and Frame.*/enum ValueType{/** Integer. */INT/** Long. */LONG/** Float. */FLOAT/** Double. */DOUBLE/** String. */STRING/** Boolean. */BOOLEAN/** Byte array. */BYTES}/**A feature data can have zero or more dimensions (columns that represent keys).*/dimensions:array[/**Tensor is used to represent feature data. A tensor is a generalization of vectors and matrices to potentially higher dimensions. In Quince Tensor specifically, the last column is designated as the value, and the rest of the columns are keys (aka dimensions).*/record Dimension{/**Type of the dimension in the tensor. Each dimension can have a different type.*/type:/**Supported dimension types for tensors in Quince and Frame.*/enum DimensionType{/** Long. */LONG/** Integer. */INT/** String. */STRING/** Boolean. */BOOLEAN}/**Size of the dimension in the tensor. If unset, it means the size is unknown and actual size will be determined at runtime. TODO(PROML): TODO(PROML-10173): To decide whether to use -1 or optional to indicate unknown shape in the week of 08/10/2020.*/shape:optional int}]}/**An optional default value can be provided. In case of missing data or errors occurred while applying transformation on source in FeatureAnchor, the default value will be used to populate feature data.*/defaultValue:optional/**Defines supported types that can be used to represent the value of a feature data. An example usage is specifying feature's default value. It currently starts with scalar types and more complex types can be added along with more use cases.*/typeref FeatureValue=union[boolean,int,long,float,double,string,bytes]}}}]", SchemaFormatType.PDL));

    public FeatureDefintionMap() {
        this(new DataMap());
    }

    public FeatureDefintionMap(int initialCapacity) {
        this(new DataMap(initialCapacity));
    }

    public FeatureDefintionMap(int initialCapacity, float loadFactor) {
        this(new DataMap(initialCapacity, loadFactor));
    }

    public FeatureDefintionMap(Map<String, FeatureDefintion> m) {
        this(newDataMapOfSize(m.size()));
        putAll(m);
    }

    public FeatureDefintionMap(DataMap data) {
        super(data, SCHEMA, FeatureDefintion.class);
    }

    public static MapDataSchema dataSchema() {
        return SCHEMA;
    }

    public static FeatureDefintionMap.ProjectionMask createMask() {
        return new FeatureDefintionMap.ProjectionMask();
    }

    @Override
    public FeatureDefintionMap clone()
        throws CloneNotSupportedException
    {
        FeatureDefintionMap __clone = ((FeatureDefintionMap) super.clone());
        return __clone;
    }

    @Override
    public FeatureDefintionMap copy()
        throws CloneNotSupportedException
    {
        FeatureDefintionMap __copy = ((FeatureDefintionMap) super.copy());
        return __copy;
    }

    @Override
    protected FeatureDefintion coerceOutput(Object object)
        throws TemplateOutputCastException
    {
        if (object == null) {
            return null;
        }
        return ((object == null)?null:new FeatureDefintion(DataTemplateUtil.castOrThrow(object, DataMap.class)));
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

        public com.linkedin.feathr.compute.FeatureDefintion.Fields values() {
            return new com.linkedin.feathr.compute.FeatureDefintion.Fields(getPathComponents(), PathSpec.WILDCARD);
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.compute.FeatureDefintion.ProjectionMask _valuesMask;

        ProjectionMask() {
            super(4);
        }

        public FeatureDefintionMap.ProjectionMask withValues(Function<com.linkedin.feathr.compute.FeatureDefintion.ProjectionMask, com.linkedin.feathr.compute.FeatureDefintion.ProjectionMask> nestedMask) {
            _valuesMask = nestedMask.apply(((_valuesMask == null)?FeatureDefintion.createMask():_valuesMask));
            getDataMap().put("$*", _valuesMask.getDataMap());
            return this;
        }

    }

}
