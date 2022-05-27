package com.linkedin.feathr.common.tensorbuilder;

import com.linkedin.feathr.common.tensor.LOLTensorData;
import com.linkedin.feathr.common.tensor.Representable;
import com.linkedin.feathr.common.tensorbuilder.TensorBuilder;
import com.linkedin.feathr.common.tensor.TensorData;
import com.linkedin.feathr.common.tensor.Primitive;

import java.util.ArrayList;
import java.util.List;


/**
 * Build a {@link LOLTensorData} tensor of the given column types
 * An instance of LOLTensorBuilder can be reused by calling start method.
 */
public class LOLTensorBuilder implements TensorBuilder {
  private static final int DEFAULT_CAPACITY = 10;
  private final Representable[] _columnTypes;
  private List<List<?>> _keyColumns;
  private List<?> _valColumn;

  public LOLTensorBuilder(Representable[] columnTypes) {
    _columnTypes = columnTypes;
    int numKeyCols = _columnTypes.length - 1;
    for (int i = 0; i < numKeyCols; i++) {
      switch (_columnTypes[i].getRepresentation()) {
        case INT:
        case LONG:
        case STRING:
        case BOOLEAN:
        case BYTES:
          break;
        default:
          throw new IllegalArgumentException("Cannot support key columns of type: "
              + _columnTypes[i].getRepresentation());
      }
    }
    if (!_columnTypes[numKeyCols].getRepresentation().canBeValue()) {
      throw new IllegalArgumentException("Cannot support value columns of type: "
          + _columnTypes[numKeyCols].getRepresentation());
    }
  }

  @Override
  public TensorData build(boolean sort) {
    int numRows = 0;
    int numDims = _keyColumns.size();
    if (numDims > 0) {
      numRows = _keyColumns.get(0).size();
      for (int i = 1; i < numDims; i++) {
        if (_keyColumns.get(i).size() != numRows) {
          throw new IllegalStateException(
              String.format("Number of rows[%d] in column %d doesn't match number of rows[%d] 0th column",
                  _keyColumns.get(i).size(), i, numRows));
        }
      }
    }
    if (numDims == 0 && _valColumn.size() > 1 || numDims != 0 &&  _valColumn.size() != numRows) {
      throw new IllegalStateException(
          String.format("Number of rows[%d] in value column doesn't match number of rows[%d] 0th column",
              _valColumn.size(), numRows));
    }
    return new LOLTensorData(_columnTypes, _keyColumns, _valColumn, sort);
  }

  @Override
  public LOLTensorBuilder append() {
    return this;
  }

  @Override
  public LOLTensorBuilder start(int estimatedRows) {
    int numKeyCols = _columnTypes.length - 1;
    _keyColumns = new ArrayList<>();
    for (int i = 0; i < numKeyCols; i++) {
      _keyColumns.add(new ArrayList<>(estimatedRows));
    }
    _valColumn = new ArrayList<>(estimatedRows);
    return this;
  }

  @Override
  public Representable[] getOutputTypes() {
    return _columnTypes;
  }

  @Override
  public Representable[] getTypes() {
    return _columnTypes;
  }

  @Override
  public LOLTensorBuilder setInt(int column, int value) {
    if (_keyColumns == null) {
      start(DEFAULT_CAPACITY);
    }
    List<Integer> intList = isValueType(column, Primitive.INT) ? (List) _valColumn : (List) _keyColumns.get(column);
    intList.add(value);
    return this;
  }

  @Override
  public LOLTensorBuilder setLong(int column, long value) {
    if (_valColumn == null || _keyColumns == null) {
      start(DEFAULT_CAPACITY);
    }
    List<Long> longList = isValueType(column, Primitive.LONG) ? (List) _valColumn : (List) _keyColumns.get(column);
    longList.add(value);
    return this;
  }

  @Override
  public LOLTensorBuilder setBoolean(int column, boolean value) {
    if (_valColumn == null || _keyColumns == null) {
      start(DEFAULT_CAPACITY);
    }
    List<Boolean> booleanList = isValueType(column, Primitive.BOOLEAN) ? (List) _valColumn
        : (List) _keyColumns.get(column);
    booleanList.add(value);
    return this;
  }

  @Override
  public LOLTensorBuilder setString(int column, String value) {
    if (_keyColumns == null) {
      start(DEFAULT_CAPACITY);
    }
    List<String> stringList = isValueType(column, Primitive.STRING) ? (List) _valColumn
        : (List) _keyColumns.get(column);
    stringList.add(value);
    return this;
  }

  @Override
  public LOLTensorBuilder setFloat(int column, float value) {
    if (_valColumn == null) {
      start(DEFAULT_CAPACITY);
    }
    if (column != _columnTypes.length - 1) {
      throw new IllegalArgumentException(String.format("Cannot set float to column %d", column));
    }
    List<Float> floatList = (List) _valColumn;
    floatList.add(value);
    return this;
  }

  @Override
  public LOLTensorBuilder setDouble(int column, double value) {
    if (_valColumn == null) {
      start(DEFAULT_CAPACITY);
    }
    if (column != _columnTypes.length - 1) {
      throw new IllegalArgumentException(String.format("Cannot set double to column %d", column));
    }
    List<Double> doubleList = (List) _valColumn;
    doubleList.add(value);
    return this;
  }

  @Override
  public LOLTensorBuilder setBytes(int column, byte[] value) {
    if (_keyColumns == null) {
      start(DEFAULT_CAPACITY);
    }
    List<byte[]> bytesList = isValueType(column, Primitive.BYTES) ? (List) _valColumn
        : (List) _keyColumns.get(column);
    bytesList.add(value);
    return this;
  }

  private boolean isValueType(int column, Primitive valueType) {
    if (column == _columnTypes.length - 1) {
      boolean correctValueType = _columnTypes[_columnTypes.length - 1].getRepresentation() == valueType;
      if (!correctValueType) {
        throw new IllegalArgumentException(String.format("Cannot set valueType as value to column %d", column));
      }
      return true;
    }
    return false;
  }
}