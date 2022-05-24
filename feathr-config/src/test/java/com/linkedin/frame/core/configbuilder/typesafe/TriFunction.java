package com.linkedin.frame.core.configbuilder.typesafe;

@FunctionalInterface
public interface TriFunction<T, U, V, R> {
  R apply(T t, U u, V v);
}
