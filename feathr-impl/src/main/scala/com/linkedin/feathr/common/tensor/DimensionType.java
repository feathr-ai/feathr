package com.linkedin.feathr.common.tensor;

import java.io.Serializable;

/**
 * Base type for all the dimension types.
 */
public abstract class DimensionType implements Serializable, Representable {
    /**
     * The reported shape when there is no explicit knowledge other than the underlying primitive type.
     */
    public static final int UNKNOWN_SHAPE = -1;

    /**
     * dummy name for dimensionTypes that have not define name
     */
    public static final String DUMMY_NAME = "dummy";

    /**
     * The reported string value for values out of the mapping for the dimension.
     */
    public static final String OUT_OF_VOCAB = "OUT_OF_VOCAB";


    /**
     * Sets a dimension in a particular column of a target tuple.
     * For primitive dimensions, implementations will just copy the value over. (This is the default behavior.)
     * For high-level semantic dimensions that maintain a different set of "outward-facing" and "internal" representations
     * of the dimension values, implementations will apply any necessary translation on the input dimensionValue before
     * inserting into the tuple.
     * @param target the destination where the dimension value should be set. Hint: This will often be a {@link com.linkedin.feathr.common.TensorBuilder}
     * @param column the column number of the target tuple to be set
     * @param dimensionValue the value of the dimension
     * @throws RuntimeException will be thrown if value is not valid for this dimension
     */
    public void setDimensionValue(WriteableTuple target, int column, Object dimensionValue) {
        getRepresentation().from(dimensionValue, target, column);
    }

    /**
     * The inverse of {@link #setDimensionValue}. Reads a dimension from a particular column of a tuple.
     * @param tuple the tuple from which to read. Hint: This will often be a {@link com.linkedin.feathr.common.tensor.TensorIterator}
     * @param column the column to read from the tuple
     * @return the value of the dimension
     */
    public Object getDimensionValue(ReadableTuple tuple, int column) {
        return getRepresentation().toObject(tuple, column);
    }

    /**
     * Get the shape.
     * @return the shape - the number of maximum possible unique values in this dimension.
     * Can be UNKNOWN_SHAPE.
     */
    public int getShape() {
        return UNKNOWN_SHAPE;
    }

    /**
     * Get the name of the DimensionType
     * @return the name
     */
    public String getName() {
        return DUMMY_NAME;
    }
}
