package com.linkedin.feathr.compute;

/**
 * In the compute graph, operators are referenced by their names.
 *
 * TODO should be by operator_set + operator_set_version + operator_name
 */
public class Operators {
  // TODO a lot more docs and details will be required here
  private Operators() {
  }

  /**
   * Name: mvel
   * Description: MVEL operator
   *
   * Input: Any
   * Output: Any
   *
   * Parameters:
   *  - expression
   */
  public static final String OPERATOR_ID_MVEL = "frame:mvel:0";

  /**
   * Name: sliding_window_aggregation
   * Description: Configurable sliding window aggregator
   *
   * Input: Series
   * Output: Any
   *
   * Parameters:
   *  - target_column
   *  - aggregation_type
   *  - window_size
   *  - window_unit
   *  - lateral_view_expression_0, lateral_view_expression_1, ...
   *  - lateral_view_table_alias_0, lateral_view_table_alias_1, ...
   *  - filter_expression
   *  - group_by_expression
   *  - max_number_groups
   */
  public static final String OPERATOR_ID_SLIDING_WINDOW_AGGREGATION = "frame:sliding_window_aggregation:0";

  /**
   * Name: java_udf_feature_extractor
   * Description: Runs a Java UDF (TODO need to provide details around this)
   *
   * Input: Any
   * Output: Any
   *
   * Parameters:
   *  - class
   *  - userParam_foo, userParam_bar
   */
  public static final String OPERATOR_ID_JAVA_UDF_FEATURE_EXTRACTOR = "frame:java_udf_feature_extractor:0";

  /**
   * Name: spark_sql_feature_extractor
   * Description: SQL operator
   *
   * Input: Any
   * Output: Any
   *
   * Parameters:
   *  - expression
   */
  public static final String OPERATOR_ID_SPARK_SQL_FEATURE_EXTRACTOR = "frame:spark_sql_feature_extractor:0";

  /**
   * Name: extract_from_tuple
   * Description: select i-th item from tuple
   *
   * Input: Tuple
   * Output: Any
   *
   * Parameter:
   *  - index
   */
  public static final String OPERATOR_ID_EXTRACT_FROM_TUPLE = "frame:extract_from_tuple:0";

  /**
   * Name: feature_alias
   * Description: given a feature, create another feature with the same values but different feature name. Main usage
   * is for intermediate features in sequential join and derived features. Note that no parameters are needed because
   * the input node's output feature will be aliases as this transformation node's feature name.
   *
   * Input: Feature
   * Output: Alias Feature
   *
   * Parameter: None
   */
  public static final String OPERATOR_FEATURE_ALIAS = "frame:feature_alias:0";
}
