package com.linkedin.feathr.offline.mvel

import com.linkedin.feathr.common.{FeatureValue, FeatureVariableResolver}
import com.linkedin.feathr.offline.mvel.plugins.FeathrExpressionExecutionContext
import org.mvel2.integration.VariableResolver
import org.mvel2.integration.impl.BaseVariableResolverFactory
import collection.JavaConverters._

import java.util.Optional
import scala.collection.JavaConverters._

private[offline] class FeatureVariableResolverFactory(features: Map[String, Option[FeatureValue]], mvelContext: Option[FeathrExpressionExecutionContext],
  shouldConvert: Option[java.lang.Boolean] = Some(false)) extends BaseVariableResolverFactory {

  variableResolvers = features.mapValues(x => new FeatureVariableResolver(x.orNull, Optional.ofNullable(mvelContext.orNull), Optional.ofNullable(shouldConvert.orNull))).asInstanceOf[Map[String, VariableResolver]].asJava

  override def isTarget(name: String): Boolean = features.contains(name)

  override def createVariable(name: String, value: scala.Any): VariableResolver = throw new UnsupportedOperationException

  override def createVariable(name: String, value: scala.Any, `type`: Class[_]): VariableResolver = throw new UnsupportedOperationException

  override def isResolveable(name: String): Boolean = {
    features.contains(name) || (if (getNextFactory != null) getNextFactory.isResolveable(name) else false)
  }
}
