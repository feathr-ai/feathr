
package com.linkedin.feathr.fds;

import java.util.Arrays;
import java.util.Collection;
import javax.annotation.Generated;
import com.linkedin.data.DataList;
import com.linkedin.data.schema.ArrayDataSchema;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.DirectArrayTemplate;
import com.linkedin.data.template.TemplateOutputCastException;
import com.linkedin.util.ArgumentUtil;

@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/fds/FeatureColumnMetadata.pdl.")
public class DimensionTypeArray
    extends DirectArrayTemplate<DimensionType>
{

    private final static ArrayDataSchema SCHEMA = ((ArrayDataSchema) DataTemplateUtil.parseSchema("array[{namespace com.linkedin.feathr.fds/**Allowable types for dimensions of a tensors stored in FDS. The type determines the acceptable values for indices of\nthat dimension. For example, if for a two-dimesional tensor, the first dimension is INT and the second is STRING,\nelements of this tensor can be referenced with an integer and a string (e.g., my_tensor[2][\"Germany\"]).*/enum DimensionType{/**The tensor dimension is of type integer.*/INT/**The tensor dimension is of type long.*/LONG/**The tensor dimension is of type string.*/STRING}}]", SchemaFormatType.PDL));

    public DimensionTypeArray() {
        this(new DataList());
    }

    public DimensionTypeArray(int initialCapacity) {
        this(new DataList(initialCapacity));
    }

    public DimensionTypeArray(Collection<DimensionType> c) {
        this(new DataList(c.size()));
        addAll(c);
    }

    public DimensionTypeArray(DataList data) {
        super(data, SCHEMA, DimensionType.class, String.class);
    }

    public DimensionTypeArray(DimensionType first, DimensionType... rest) {
        this(new DataList((rest.length + 1)));
        add(first);
        addAll(Arrays.asList(rest));
    }

    public static ArrayDataSchema dataSchema() {
        return SCHEMA;
    }

    @Override
    public DimensionTypeArray clone()
        throws CloneNotSupportedException
    {
        DimensionTypeArray __clone = ((DimensionTypeArray) super.clone());
        return __clone;
    }

    @Override
    public DimensionTypeArray copy()
        throws CloneNotSupportedException
    {
        DimensionTypeArray __copy = ((DimensionTypeArray) super.copy());
        return __copy;
    }

    @Override
    protected Object coerceInput(DimensionType object)
        throws ClassCastException
    {
        ArgumentUtil.notNull(object, "object");
        return object.name();
    }

    @Override
    protected DimensionType coerceOutput(Object object)
        throws TemplateOutputCastException
    {
        assert(object != null);
        return DataTemplateUtil.coerceEnumOutput(object, DimensionType.class, DimensionType.$UNKNOWN);
    }

}
