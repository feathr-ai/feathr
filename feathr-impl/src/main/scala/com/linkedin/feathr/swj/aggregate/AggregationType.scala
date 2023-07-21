package com.linkedin.feathr.swj.aggregate

object AggregationType extends Enumeration {
  type AggregationType = Value
  val SUM, COUNT, COUNT_DISTINCT, DISTINCT, AVG, MAX, TIMESINCE, LATEST, DUMMY, MIN, MAX_POOLING, MIN_POOLING, AVG_POOLING, SUM_POOLING,
  BUCKETED_COUNT_DISTINCT, BUCKETED_SUM = Value
}
