package com.linkedin.feathr.common.metadata;

import java.util.List;
import java.util.stream.Collectors;


public class Util {
  private Util() {
  }

  public static void require(boolean condition) {
    if (!condition) {
      throw new IllegalArgumentException();
    }
  }

  public static void require(boolean condition, String message) {
    if (!condition) {
      throw new IllegalArgumentException(message);
    }
  }

  /*
   * pretty-print List
   */
  public static <T> String string(List<T> list, String start, String sep, String end) {
    return list.stream().map(T::toString).collect(Collectors.joining(sep, start, end));
  }

  public static <T> String string(List<T> list) {
    return string(list, "[", ", ", "]");
  }

  public static <T> String string(List<T> list, String sep) {
    return string(list, "[", sep, "]");
  }
}
