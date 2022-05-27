package com.linkedin.feathr.common.util;

import com.linkedin.data.template.IntegerArray;
import com.linkedin.feathr.common.exception.FeathrException;
import com.linkedin.feathr.common.tensor.DimensionType;
import com.linkedin.feathr.common.types.ValueType;
import com.linkedin.feathr.fds.DimensionTypeArray;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class PegasusTensorMetadataConverter {
  private PegasusTensorMetadataConverter() {

  }
  /**
   * Gets a list of Integer shape from an array of primitive integer shape.
   */
  public static IntegerArray getShape(int[] shape) {

    List<Integer> list = Arrays.stream(shape).boxed().collect(Collectors.toList());
    return new IntegerArray(list);
  }


  /**
   * Converts Quince {@link DimensionType} representation to Pegasus Dimension type.
   */
  private static com.linkedin.feathr.fds.DimensionType toPegasusDimensionType(DimensionType dimensionType)
      throws FeathrException {
    switch (dimensionType.getRepresentation()) {
      case LONG:
        return com.linkedin.feathr.fds.DimensionType.LONG;
      case INT:
        return com.linkedin.feathr.fds.DimensionType.INT;
      case STRING:
        return com.linkedin.feathr.fds.DimensionType.STRING;
      default:
        throw new FeathrException(String.format("The provided DimensionType [%s] is not supported", dimensionType.getRepresentation()));
    }
  }

  public static DimensionTypeArray toPegasusDimensionTypes(List<DimensionType> dimensionTypes)
      throws FeathrException {

    if (dimensionTypes == null) {
      // default value is an empty array
      return new DimensionTypeArray();
    }

    List<com.linkedin.feathr.fds.DimensionType> dimensionTypeList = new ArrayList<>(dimensionTypes.size());
    for (DimensionType dimensionType : dimensionTypes) {
      dimensionTypeList.add(toPegasusDimensionType(dimensionType));
    }
    return new DimensionTypeArray(dimensionTypeList);
  }


  /**
   * Converts Quince {@link ValueType} representation to Pegasus Value type.
   */
  public static com.linkedin.feathr.fds.ValueType toPegasusValueType(ValueType valueType) throws FeathrException {
    switch (valueType.getRepresentation()) {
      case STRING:
        return com.linkedin.feathr.fds.ValueType.STRING;
      case LONG:
        return com.linkedin.feathr.fds.ValueType.LONG;
      case INT:
        return com.linkedin.feathr.fds.ValueType.INT;
      case BOOLEAN:
        return com.linkedin.feathr.fds.ValueType.BOOLEAN;
      case DOUBLE:
        return com.linkedin.feathr.fds.ValueType.DOUBLE;
      case FLOAT:
        return com.linkedin.feathr.fds.ValueType.FLOAT;
      case BYTES:
        return com.linkedin.feathr.fds.ValueType.BYTES;
      default:
        throw new FeathrException(String.format("The provided ValueType [%s] is not supported", valueType));
    }
  }
}
