
package com.linkedin.feathr.featureDataModel;

import java.util.List;
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

@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/SequentialJoinFeatureSourcesAnchor.pdl.")
public class ReductionFunction
    extends UnionTemplate
    implements HasTyperefInfo
{

    private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.featureDataModel,enum StandardAggregation{/** Average. Apply to 0-rank numeric scalar. For example, base feature gets a job's applicants, and expansion feature gets the age of an applicant, when average is specified, the resulting feature data is the average age of all applicants for a job. */AVG/** Max. Apply to 0-rank numeric scalar. Refer to AVG for a very similar example. */MAX/** Min. Apply to 0-rank numeric scalar. Refer to AVG for a very similar example. */MIN/** Sum. Apply to 0-rank numeric scalar. Refer to AVG for a very similar example. */SUM/** Union. This combines multiple feature data into a collection. For example, base feature gets member's skill ids, and expansion feature gets a skill name based on skill id, when union is specified, the resulting feature data is a list of skill names for a given member. */UNION/** Element-wise Average, which apply average operation to elements corresponding to the same positional index. It applies to 1+ rank feature data, like a dense vector. For example, base feature gets member's skill ids, and expansion feature gets a skill embedding based on skill id, when ELEMENTWISE_AVG is specified, the resulting feature data is a single embedding by element-wise averaging all skill embeddings of the member. */ELEMENTWISE_AVG/** Element-wise Min, which apply min operation to elements corresponding to the same positional index. It applies to 1+ rank feature data, like a dense vector. Refer to ELEMENTWISE_AVG for a very similar example. */ELEMENTWISE_MIN/** Element-wise Max, which apply max operation to elements corresponding to the same positional index. It applies to 1+ rank feature data, like a dense vector. Refer to ELEMENTWISE_AVG for a very similar example. */ELEMENTWISE_MAX/** Element-wise Sum, which apply sum operation to elements corresponding to the same positional index. It applies to 1+ rank feature data, like a dense vector. Refer to ELEMENTWISE_AVG for a very similar example. */ELEMENTWISE_SUM}}]", SchemaFormatType.PDL));
    private com.linkedin.feathr.featureDataModel.StandardAggregation _standardAggregationMember = null;
    private ReductionFunction.ChangeListener __changeListener = new ReductionFunction.ChangeListener(this);
    private final static DataSchema MEMBER_StandardAggregation = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.StandardAggregation");
    private final static TyperefInfo TYPEREFINFO = new ReductionFunction.UnionTyperefInfo();

    public ReductionFunction() {
        super(new DataMap(2, 0.75F), SCHEMA);
        addChangeListener(__changeListener);
    }

    public ReductionFunction(Object data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static UnionDataSchema dataSchema() {
        return SCHEMA;
    }

    public static ReductionFunction create(com.linkedin.feathr.featureDataModel.StandardAggregation value) {
        ReductionFunction newUnion = new ReductionFunction();
        newUnion.setStandardAggregation(value);
        return newUnion;
    }

    public boolean isStandardAggregation() {
        return memberIs("com.linkedin.feathr.featureDataModel.StandardAggregation");
    }

    public com.linkedin.feathr.featureDataModel.StandardAggregation getStandardAggregation() {
        checkNotNull();
        if (_standardAggregationMember!= null) {
            return _standardAggregationMember;
        }
        Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.StandardAggregation");
        _standardAggregationMember = DataTemplateUtil.coerceEnumOutput(__rawValue, com.linkedin.feathr.featureDataModel.StandardAggregation.class, com.linkedin.feathr.featureDataModel.StandardAggregation.$UNKNOWN);
        return _standardAggregationMember;
    }

    public void setStandardAggregation(com.linkedin.feathr.featureDataModel.StandardAggregation value) {
        checkNotNull();
        super._map.clear();
        _standardAggregationMember = value;
        CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.StandardAggregation", value.name());
    }

    public static ReductionFunction.ProjectionMask createMask() {
        return new ReductionFunction.ProjectionMask();
    }

    @Override
    public ReductionFunction clone()
        throws CloneNotSupportedException
    {
        ReductionFunction __clone = ((ReductionFunction) super.clone());
        __clone.__changeListener = new ReductionFunction.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public ReductionFunction copy()
        throws CloneNotSupportedException
    {
        ReductionFunction __copy = ((ReductionFunction) super.copy());
        __copy._standardAggregationMember = null;
        __copy.__changeListener = new ReductionFunction.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    public TyperefInfo typerefInfo() {
        return TYPEREFINFO;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final ReductionFunction __objectRef;

        private ChangeListener(ReductionFunction reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "com.linkedin.feathr.featureDataModel.StandardAggregation":
                    __objectRef._standardAggregationMember = null;
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

        public PathSpec StandardAggregation() {
            return new PathSpec(getPathComponents(), "com.linkedin.feathr.featureDataModel.StandardAggregation");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {


        ProjectionMask() {
            super(2);
        }

        public ReductionFunction.ProjectionMask withStandardAggregation() {
            getDataMap().put("com.linkedin.feathr.featureDataModel.StandardAggregation", MaskMap.POSITIVE_MASK);
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

        private final static TyperefDataSchema SCHEMA = ((TyperefDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel,typeref ReductionFunction=union[enum StandardAggregation{/** Average. Apply to 0-rank numeric scalar. For example, base feature gets a job's applicants, and expansion feature gets the age of an applicant, when average is specified, the resulting feature data is the average age of all applicants for a job. */AVG/** Max. Apply to 0-rank numeric scalar. Refer to AVG for a very similar example. */MAX/** Min. Apply to 0-rank numeric scalar. Refer to AVG for a very similar example. */MIN/** Sum. Apply to 0-rank numeric scalar. Refer to AVG for a very similar example. */SUM/** Union. This combines multiple feature data into a collection. For example, base feature gets member's skill ids, and expansion feature gets a skill name based on skill id, when union is specified, the resulting feature data is a list of skill names for a given member. */UNION/** Element-wise Average, which apply average operation to elements corresponding to the same positional index. It applies to 1+ rank feature data, like a dense vector. For example, base feature gets member's skill ids, and expansion feature gets a skill embedding based on skill id, when ELEMENTWISE_AVG is specified, the resulting feature data is a single embedding by element-wise averaging all skill embeddings of the member. */ELEMENTWISE_AVG/** Element-wise Min, which apply min operation to elements corresponding to the same positional index. It applies to 1+ rank feature data, like a dense vector. Refer to ELEMENTWISE_AVG for a very similar example. */ELEMENTWISE_MIN/** Element-wise Max, which apply max operation to elements corresponding to the same positional index. It applies to 1+ rank feature data, like a dense vector. Refer to ELEMENTWISE_AVG for a very similar example. */ELEMENTWISE_MAX/** Element-wise Sum, which apply sum operation to elements corresponding to the same positional index. It applies to 1+ rank feature data, like a dense vector. Refer to ELEMENTWISE_AVG for a very similar example. */ELEMENTWISE_SUM}]", SchemaFormatType.PDL));

        public UnionTyperefInfo() {
            super(SCHEMA);
        }

        public static TyperefDataSchema dataSchema() {
            return SCHEMA;
        }

    }

}
