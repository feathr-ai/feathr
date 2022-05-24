
package com.linkedin.feathr.featureDataModel;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import javax.annotation.Generated;
import com.linkedin.data.DataList;
import com.linkedin.data.DataMap;
import com.linkedin.data.schema.ArrayDataSchema;
import com.linkedin.data.schema.MaskMap;
import com.linkedin.data.schema.PathSpec;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.TemplateOutputCastException;
import com.linkedin.data.template.WrappingArrayTemplate;

@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/OfflineFeatureSourcesAnchor.pdl.")
public class FeatureSourceArray
    extends WrappingArrayTemplate<FeatureSource>
{

    private final static ArrayDataSchema SCHEMA = ((ArrayDataSchema) DataTemplateUtil.parseSchema("array[{namespace com.linkedin.feathr.featureDataModel/**This represents a feature source for FeatureAnchor. That is, it is a source of type 'feature' that can be used for creating other features. For example, say there exist two features: member skills and job skills, they can be the sources of another feature for computing their cosine similarity. In feathr, the feature that represents cosine similarity is called derived feature, refer to go/feathrglossary and go/feathroverview for more details.*/record FeatureSource includes/**Represents the identifier of a DataSource object. This class is expected to be included so the enclosed field can be reused in each concrete DataSource.*/record DataSourceRef{/**Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.*/dataSourceRef:optional string}{/**This is the unique id for the MlFeatureVersionEntity used as the source.*/urn:{namespace com.linkedin.frame.common/**Standardized MLFeature identifier.*/@java.class=\"com.linkedin.frame.common.urn.MlFeatureVersionUrn\"typeref MlFeatureVersionUrn=string}/**A feature's alias to be used in transformation function. It should be unique in the scope of a FeatureSourcesAnchor. It is useful when the same feature is used as FeatureSource multiple times with different keys. For example, if we are modeing the problem of the probability of a member A (viewer) seeing the profile of member B (viewee) and we want to use the skills of both viewer and viewee as features. If the alias is set, the alias (instead of feature name) will be used to reference this FeatureSource in the transformation function (especially expression languages like MVEL).*/alias:optional string/**Represents zero, one or multiple keyPlaceholderRefs which are used as the identifiers to reference KeyPlaceholders of the FeatureSourcesAnchor. This is to ensure corresponding key values can be propogated to the right FeatureSource. Refer to KeyPlaceholders for a full example.*/keyPlaceholderRefs:array[/**Represents the identifier of KeyPlaceholder object. It should be meaningful (eg. memberId) and unique in the scope of a feature's definition. It is used to uniformly reference a KeyPlaceholder object in the feature definition. Refer to KeyPlaceholders for a full example.*/typeref KeyPlaceholderRef=string]=[]}}]", SchemaFormatType.PDL));

    public FeatureSourceArray() {
        this(new DataList());
    }

    public FeatureSourceArray(int initialCapacity) {
        this(new DataList(initialCapacity));
    }

    public FeatureSourceArray(Collection<FeatureSource> c) {
        this(new DataList(c.size()));
        addAll(c);
    }

    public FeatureSourceArray(DataList data) {
        super(data, SCHEMA, FeatureSource.class);
    }

    public FeatureSourceArray(FeatureSource first, FeatureSource... rest) {
        this(new DataList((rest.length + 1)));
        add(first);
        addAll(Arrays.asList(rest));
    }

    public static ArrayDataSchema dataSchema() {
        return SCHEMA;
    }

    public static FeatureSourceArray.ProjectionMask createMask() {
        return new FeatureSourceArray.ProjectionMask();
    }

    @Override
    public FeatureSourceArray clone()
        throws CloneNotSupportedException
    {
        FeatureSourceArray __clone = ((FeatureSourceArray) super.clone());
        return __clone;
    }

    @Override
    public FeatureSourceArray copy()
        throws CloneNotSupportedException
    {
        FeatureSourceArray __copy = ((FeatureSourceArray) super.copy());
        return __copy;
    }

    @Override
    protected FeatureSource coerceOutput(Object object)
        throws TemplateOutputCastException
    {
        assert(object != null);
        return ((object == null)?null:new FeatureSource(DataTemplateUtil.castOrThrow(object, DataMap.class)));
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

        public com.linkedin.feathr.featureDataModel.FeatureSource.Fields items() {
            return new com.linkedin.feathr.featureDataModel.FeatureSource.Fields(getPathComponents(), PathSpec.WILDCARD);
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.FeatureSource.ProjectionMask _itemsMask;

        ProjectionMask() {
            super(4);
        }

        public FeatureSourceArray.ProjectionMask withItems(Function<com.linkedin.feathr.featureDataModel.FeatureSource.ProjectionMask, com.linkedin.feathr.featureDataModel.FeatureSource.ProjectionMask> nestedMask) {
            _itemsMask = nestedMask.apply(((_itemsMask == null)?FeatureSource.createMask():_itemsMask));
            getDataMap().put("$*", _itemsMask.getDataMap());
            return this;
        }

    }

}
