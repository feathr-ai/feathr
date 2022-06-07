package com.linkedin.feathr.config.featureanchor.builder;

import com.google.common.base.Preconditions;
import com.linkedin.feathr.config.featureanchor.builder.transformation.AnchoredFeatureTransformationFunctionBuilder;
import com.linkedin.feathr.core.config.producer.anchors.AnchorConfig;
import com.linkedin.feathr.core.config.producer.anchors.FeatureConfig;
import com.linkedin.feathr.core.config.producer.sources.PassThroughConfig;
import com.linkedin.feathr.featureDataModel.FeatureAnchor.Anchor;
import com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSourceAnchor;
import com.linkedin.feathr.featureDataModel.Clazz;
import com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSource;
import com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction;
import javax.annotation.Nonnull;


class InMemoryPassthroughAnchorBuilder implements AnchorBuilder {

  private final PassThroughConfig _passThroughConfig;
  private final FeatureConfig _featureConfig;
  private final AnchorConfig _anchorConfig;
  private final AnchoredFeatureTransformationFunctionBuilder<TransformationFunction>
      _anchoredFeatureTransformationFunctionBuilder;

  public InMemoryPassthroughAnchorBuilder(@Nonnull PassThroughConfig passThroughConfig,
      @Nonnull FeatureConfig featureConfig, @Nonnull AnchorConfig anchorConfig,
      @Nonnull AnchoredFeatureTransformationFunctionBuilder<TransformationFunction>
          anchoredFeatureTransformationFunctionBuilder) {
    Preconditions.checkNotNull(passThroughConfig);
    Preconditions.checkArgument(passThroughConfig.getDataModel().isPresent(), "Invalid in-memory passthrough feature config");
    Preconditions.checkNotNull(anchorConfig);
    Preconditions.checkNotNull(featureConfig);
    Preconditions.checkNotNull(anchoredFeatureTransformationFunctionBuilder);
    _passThroughConfig = passThroughConfig;
    _featureConfig = featureConfig;
    _anchorConfig = anchorConfig;
    _anchoredFeatureTransformationFunctionBuilder = anchoredFeatureTransformationFunctionBuilder;
  }

  public Anchor build() {
    InMemoryPassthroughDataSourceAnchor inMemoryPassthroughAnchor = new InMemoryPassthroughDataSourceAnchor();
    InMemoryPassthroughDataSource inMemoryPassthroughDataSource = new InMemoryPassthroughDataSource();
    Clazz clazz = new Clazz();
    clazz.setFullyQualifiedName(_passThroughConfig.getDataModel().get());
    inMemoryPassthroughDataSource.setDataModel(clazz);
    inMemoryPassthroughDataSource.setDataSourceRef(_passThroughConfig.getSourceName());
    inMemoryPassthroughAnchor.setSource(inMemoryPassthroughDataSource);
    inMemoryPassthroughAnchor.setTransformationFunction(_anchoredFeatureTransformationFunctionBuilder.build(
        _featureConfig, _anchorConfig));
    Anchor anchor = new Anchor();
    anchor.setInMemoryPassthroughDataSourceAnchor(inMemoryPassthroughAnchor);
    return anchor;
  }
}
