package com.linkedin.feathr.offline.mvel.plugins;

import com.linkedin.feathr.common.FeatureValue;
import org.mvel2.ConversionHandler;
import org.mvel2.DataConversion;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;


/**
 * A plugin that allows an advanced user to add additional capabilities or behaviors to Feathr's MVEL runtime.
 *
 * NOTE: This class is intended for advanced users only, and specifically as a "migration aid" for migrating from
 * some previous versions of Feathr whose FeatureValue representations had a different class name, while preserving
 * compatibility with feature definitions written against those older versions of Feathr.
 */
public class FeathrMvelPluginContext implements Serializable {
  private static final AtomicReference<FeathrMvelPluginContext> INSTALLED_CONTEXT = new AtomicReference<>();

  private final Map<Class<?>, FeatureValueTypeAdaptor<?>> _typeAdaptors;

  private FeathrMvelPluginContext(Map<Class<?>, FeatureValueTypeAdaptor<?>> _typeAdaptors) {
    this._typeAdaptors = _typeAdaptors;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static FeathrMvelPluginContext getInstalledContext() {
    return INSTALLED_CONTEXT.get();
  }

  public static void ensureInstalledIfDefined(FeathrMvelPluginContext pluginContext) {
    if (pluginContext != null) {
      pluginContext.installTypeAdaptorsIntoMvelRuntime();
    }
  }

  /**
   * Installs this FeathrMvelPluginContext into the MVEL runtime of this JVM.
   *
   * (Calling this more than once should be safe, and should have the same effect as calling it once. However,
   * calling it concurrently would be dangerous due to non-threadsafe constructs in the MVEL runtime.)
   */
  public void installTypeAdaptorsIntoMvelRuntime() {
    if (!iAmInstalled()) {
      synchronized (FeathrMvelPluginContext.class) {
        if (!iAmInstalled()) {
          if (INSTALLED_CONTEXT.get() != null) {
            throw new RuntimeException("Tried to install plugin context " + this + " but a different one is already " +
                    "installed: " + INSTALLED_CONTEXT.get());
          }
          doInstall();
          INSTALLED_CONTEXT.set(this);
        }
      }
    }
  }

  @Override
  public String toString() {
    return "FeathrMvelPluginContext{" +
            "_typeAdaptors=" + _typeAdaptors +
            '}';
  }

  private void doInstall() {
    DataConversion.addConversionHandler(FeatureValue.class, new FeathrFeatureValueConversionHandler());
    _typeAdaptors.forEach((clazz, typeAdaptor) -> {
      DataConversion.addConversionHandler(clazz, new ExternalFeatureValueConversionHandler(typeAdaptor));
    });
  }

  private boolean iAmInstalled() {
    return INSTALLED_CONTEXT.get() == this;
  }

  class FeathrFeatureValueConversionHandler implements ConversionHandler {
    @Override
    @SuppressWarnings("unchecked")
    public Object convertFrom(Object in) {
      FeatureValueTypeAdaptor<Object> adaptor = (FeatureValueTypeAdaptor<Object>) _typeAdaptors.get(in.getClass());
      if (adaptor == null) {
        throw new IllegalArgumentException("Can't convert to Feathr FeatureValue from " + in);
      }
      return adaptor.toFeathrFeatureValue(in);
    }

    @Override
    public boolean canConvertFrom(Class cls) {
      return _typeAdaptors.containsKey(cls);
    }
  }

  static class ExternalFeatureValueConversionHandler implements ConversionHandler {
    private final FeatureValueTypeAdaptor<?> _adaptor;

    public ExternalFeatureValueConversionHandler(FeatureValueTypeAdaptor<?> adaptor) {
      _adaptor = adaptor;
    }

    @Override
    public Object convertFrom(Object in) {
      return _adaptor.fromFeathrFeatureValue((FeatureValue) in);
    }

    @Override
    public boolean canConvertFrom(Class cls) {
      return FeatureValue.class.equals(cls);
    }
  }

  public static class Builder {
    private final Map<Class<?>, FeatureValueTypeAdaptor<?>> _typeAdaptors = new HashMap<>();

    /**
     * Add a type adaptor that will enable Feathr's MVEL expressions to support some alternative class representation
     * of {@link FeatureValue} via coercion.
     *
     * @param clazz the class of the "other" alternative representation of feature value
     * @param typeAdaptor the type adaptor that can convert between the "other" representation and {@link FeatureValue}
     * @param <T> type parameter for the "other" feature value class
     * @return this builder
     */
    @SuppressWarnings("unchecked")
    public <T> Builder addFeatureTypeAdaptor(Class<T> clazz, FeatureValueTypeAdaptor<T> typeAdaptor) {
      // TODO: MAKE SURE clazz IS NOT ONE OF THE CLASSES ALREADY COVERED IN org.mvel2.DataConversion.CONVERTERS!
      //       IF WE OVERRIDE ANY OF THOSE, IT MIGHT CAUSE MVEL TO BEHAVE IN STRANGE AND UNEXPECTED WAYS!
      _typeAdaptors.put(clazz, typeAdaptor);
      return this;
    }

    /**
     * Builds a {@link FeathrMvelPluginContext} which can be used to actually install the adaptors into the MVEL runtime.
     * @return the plugin context object
     */
    public FeathrMvelPluginContext build() {
      return new FeathrMvelPluginContext(Collections.unmodifiableMap(new HashMap<>(_typeAdaptors)));
    }
  }
}
