
package com.linkedin.feathr.fds;

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

@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/fds/FeaturizedDatasetMetadata.pdl.")
public class ColumnMetadataMap
    extends WrappingMapTemplate<ColumnMetadata>
{

    private final static MapDataSchema SCHEMA = ((MapDataSchema) DataTemplateUtil.parseSchema("map[string{namespace com.linkedin.feathr.fds/**Mandatory Metadata for each column in a Featurized Dataset.\n<p/>\nMore details can be found in the specification at docs/qt_fds_spec.md*/record ColumnMetadata{/**metadata for a single column in FDS.*/metadata:union[/**FeatureColumnMetadata structure capturing metadata of columns tgat cintain features (\nas defined in the spec). In this context, \"feature columns\" contain features, labels,\nentity ids, weights and all other columns that are used in computation/training (as opposed\nto opaque contextual columns).\n<p/>\nMore details can be found in the specification at docs/qt_fds_spec.md*/record FeatureColumnMetadata{/**Category of the tensor (e.g. TensorCategory.DENSE, TensorCategory.SPARSE)*/tensorCategory:/**Representation type of a feature\n<p/>\nMore details can be found in the specification\nhttps://docs.google.com/document/d/12VZis-6VyQN3Ivy2rQxf-OktZqCowuTvVrsIFxwTVXU/edit#heading=h.jjuk2t7bn84*/enum TensorCategory{/**A tensor where we only store values for some positions.*/SPARSE/**A tensor where we store a value for each position.*/DENSE/**RAGGED tensors (also called nested tensors) are similar to dense tensors but have variable-length\ndimensions.*/RAGGED}/**The shape (sometimes called the \u201csize\u201d or \u201cdense shape\u201d) of the tensor.\nGiven as a list of integers. The first element gives the size of the first dimension\nin the tensor, the second element gives the size of the second dimension, and so on.\nThe length of the tensorShape array is the number of dimensions in the tensor, also\ncalled the tensor's rank.\nThe data schema must match the tensor\u2019s rank as indicated in the tensorShape array;\nwhich in case of DENSE means that the level of nesting of the array must\nmatch the rank, and in case of SPARSE there must be the same number of\nindices fields as the rank.\nDimensions are allowed to have unknown size. Elements in the tensorShape array may be\nset as (-1) to indicate unknown.\nFor any dimension having dimensionType of string, that dimension\u2019s size must be set\nto (-1) (unknown).*/tensorShape:array[int]/**List of the types for each dimension. Given as an array of strings.\nFor DENSE features, must contain only DimensionType.INT. For SPARSE features,\nmust correspond to the data types of the corresponding indices fields in the data schema*/dimensionTypes:array[/**Allowable types for dimensions of a tensors stored in FDS. The type determines the acceptable values for indices of\nthat dimension. For example, if for a two-dimesional tensor, the first dimension is INT and the second is STRING,\nelements of this tensor can be referenced with an integer and a string (e.g., my_tensor[2][\"Germany\"]).*/enum DimensionType{/**The tensor dimension is of type integer.*/INT/**The tensor dimension is of type long.*/LONG/**The tensor dimension is of type string.*/STRING}]/**The value type (e.g. ValueType.INT, ValueType.FLOAT)*/valueType:/**The value type for a feature (column) of FDS.*/enum ValueType{/**The value is an integer.*/INT/**The value is a long.*/LONG/**The value is a float.*/FLOAT/**The value is a double.*/DOUBLE/**The value is a boolean.*/BOOLEAN/**The value is a string.*/STRING/**The value is a byte-array.*/BYTES}/**Optional metadata that helps with linkage of a feature. See the documentation\non FeatureColumnLinkageMetadata for more info.*/linkageMetadata:optional/**Metadata that helps with linkage of a feature. That is, metadata\nneeded to link the column name to an access method for getting the contents.\nFor example, if a column is populated using a frame feature and then a model\nis trained using that feature/column, the external feature name in the model\nwill be the same as the column name. Linkage metadata allows us to link/bind\nthat external feature of the model with some value during inference. For example,\nif the FDS column corresponded to a frame feature, linkage metadata will contain\nthe unique identifier of that frame feature. This infomation can then be encoded\nin the model bundle and used to bind that external feature to a concrete value\nat inference time.*/record FeatureColumnLinkageMetadata{/** See above **/featureLinkageMetadata:union[/**Used when the feature column contains a frame feature.*/record FrameFeatureMetadata{/**Name of a frame feature, uniquely identifying a particular version of a frame feature.\nThis name includes the version (e.g., myFeature-1_0_0).*/frameFeatureName:string/**List of columns that have entity IDs for this feature.*/keyColumns:array[string]}]}}/**OpaqueContextualColumnMetadata structure capturing metadata of columns that contain\nOpaque Contextual Data.\n<p/>\nMore details can be found in the specification at docs/qt_fds_spec.md*/record OpaqueContextualColumnMetadata{}]}}]", SchemaFormatType.PDL));

    public ColumnMetadataMap() {
        this(new DataMap());
    }

    public ColumnMetadataMap(int initialCapacity) {
        this(new DataMap(initialCapacity));
    }

    public ColumnMetadataMap(int initialCapacity, float loadFactor) {
        this(new DataMap(initialCapacity, loadFactor));
    }

    public ColumnMetadataMap(Map<String, ColumnMetadata> m) {
        this(newDataMapOfSize(m.size()));
        putAll(m);
    }

    public ColumnMetadataMap(DataMap data) {
        super(data, SCHEMA, ColumnMetadata.class);
    }

    public static MapDataSchema dataSchema() {
        return SCHEMA;
    }

    public static ColumnMetadataMap.ProjectionMask createMask() {
        return new ColumnMetadataMap.ProjectionMask();
    }

    @Override
    public ColumnMetadataMap clone()
        throws CloneNotSupportedException
    {
        ColumnMetadataMap __clone = ((ColumnMetadataMap) super.clone());
        return __clone;
    }

    @Override
    public ColumnMetadataMap copy()
        throws CloneNotSupportedException
    {
        ColumnMetadataMap __copy = ((ColumnMetadataMap) super.copy());
        return __copy;
    }

    @Override
    protected ColumnMetadata coerceOutput(Object object)
        throws TemplateOutputCastException
    {
        if (object == null) {
            return null;
        }
        return ((object == null)?null:new ColumnMetadata(DataTemplateUtil.castOrThrow(object, DataMap.class)));
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

        public com.linkedin.feathr.fds.ColumnMetadata.Fields values() {
            return new com.linkedin.feathr.fds.ColumnMetadata.Fields(getPathComponents(), PathSpec.WILDCARD);
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.fds.ColumnMetadata.ProjectionMask _valuesMask;

        ProjectionMask() {
            super(4);
        }

        public ColumnMetadataMap.ProjectionMask withValues(Function<com.linkedin.feathr.fds.ColumnMetadata.ProjectionMask, com.linkedin.feathr.fds.ColumnMetadata.ProjectionMask> nestedMask) {
            _valuesMask = nestedMask.apply(((_valuesMask == null)?ColumnMetadata.createMask():_valuesMask));
            getDataMap().put("$*", _valuesMask.getDataMap());
            return this;
        }

    }

}
