package com.linkedin.feathr.offline.evaluator.datasource

import com.linkedin.feathr.compute._
import com.linkedin.feathr.offline.client.NodeContext
import org.apache.spark.sql.DataFrame

private[offline] object ContextNodeEvaluator {
  private[offline] def processContextNode(contextDataFrame: DataFrame, dataSource: DataSource): NodeContext = {
    if (!dataSource.hasConcreteKey) {
      // This is the feature column being extracted
      val colName = dataSource.getExternalSourceRef // This is a bit strange. Maybe Context should be its own node type.
      // extractor - maybe since it is the join key
      NodeContext(contextDataFrame, Seq(colName))
    } else {
      val concreteKey = dataSource.getConcreteKey().getKey()
      NodeContext(contextDataFrame, Seq("keyCol"))
    }
  }
}
