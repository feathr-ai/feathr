package com.linkedin.feathr.common.tensorbuilder;

import com.linkedin.feathr.common.tensorbuilder.TensorBuilder;
import com.linkedin.feathr.common.tensorbuilder.TensorBuilderFactory;
import com.linkedin.feathr.common.tensor.TensorType;


public class LOLTensorBuilderFactory implements TensorBuilderFactory {
  public static final LOLTensorBuilderFactory INSTANCE = new LOLTensorBuilderFactory();
  @Override
  public TensorBuilder<?> getTensorBuilder(TensorType tensorType) {
    return new LOLTensorBuilder(tensorType.getColumnTypes());
  }
}
