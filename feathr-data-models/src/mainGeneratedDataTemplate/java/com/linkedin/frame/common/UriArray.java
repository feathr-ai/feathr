
package com.linkedin.frame.common;

import java.util.Arrays;
import java.util.Collection;
import javax.annotation.Generated;
import com.linkedin.data.DataList;
import com.linkedin.data.schema.ArrayDataSchema;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.Custom;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.DirectArrayTemplate;
import com.linkedin.data.template.TemplateOutputCastException;
import com.linkedin.frame.common.coercer.UriCoercer;
import com.linkedin.util.ArgumentUtil;

@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/CouchbaseDataSource.pdl.")
public class UriArray
    extends DirectArrayTemplate<java.net.URI>
{

    private final static ArrayDataSchema SCHEMA = ((ArrayDataSchema) DataTemplateUtil.parseSchema("array[{namespace com.linkedin.frame.common@java={\"class\":\"java.net.URI\",\"coercerClass\":\"com.linkedin.frame.common.coercer.UriCoercer\"}typeref Uri=string}]", SchemaFormatType.PDL));

    static {
        Custom.initializeCustomClass(java.net.URI.class);
        Custom.initializeCoercerClass(UriCoercer.class);
    }

    public UriArray() {
        this(new DataList());
    }

    public UriArray(int initialCapacity) {
        this(new DataList(initialCapacity));
    }

    public UriArray(Collection<java.net.URI> c) {
        this(new DataList(c.size()));
        addAll(c);
    }

    public UriArray(DataList data) {
        super(data, SCHEMA, java.net.URI.class, String.class);
    }

    public UriArray(java.net.URI first, java.net.URI... rest) {
        this(new DataList((rest.length + 1)));
        add(first);
        addAll(Arrays.asList(rest));
    }

    public static ArrayDataSchema dataSchema() {
        return SCHEMA;
    }

    @Override
    public UriArray clone()
        throws CloneNotSupportedException
    {
        UriArray __clone = ((UriArray) super.clone());
        return __clone;
    }

    @Override
    public UriArray copy()
        throws CloneNotSupportedException
    {
        UriArray __copy = ((UriArray) super.copy());
        return __copy;
    }

    @Override
    protected Object coerceInput(java.net.URI object)
        throws ClassCastException
    {
        ArgumentUtil.notNull(object, "object");
        return DataTemplateUtil.coerceCustomInput(object, java.net.URI.class);
    }

    @Override
    protected java.net.URI coerceOutput(Object object)
        throws TemplateOutputCastException
    {
        assert(object != null);
        return DataTemplateUtil.coerceCustomOutput(object, java.net.URI.class);
    }

}
