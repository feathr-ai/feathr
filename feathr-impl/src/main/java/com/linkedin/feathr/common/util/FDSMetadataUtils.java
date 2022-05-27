package com.linkedin.feathr.common.util;

import com.linkedin.feathr.common.tensor.TensorCategory;

public class FDSMetadataUtils {
  public static com.linkedin.feathr.fds.TensorCategory convertCategory(TensorCategory tensorCategory) {
    switch (tensorCategory) {
      case SPARSE:
        return com.linkedin.feathr.fds.TensorCategory.SPARSE;
      case DENSE:
        return com.linkedin.feathr.fds.TensorCategory.DENSE;
      case RAGGED:
        return com.linkedin.feathr.fds.TensorCategory.RAGGED;
      default:
        throw new IllegalArgumentException("Unsupported tensor category: " + tensorCategory);
    }
  }
}
