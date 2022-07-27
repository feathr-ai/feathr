package com.linkedin.feathr.offline.mvel.plugins;

import com.linkedin.feathr.common.FeatureValue;
import org.mvel2.ConversionHandler;
import org.mvel2.DataConversion;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * A plugin that allows an advanced user to add additional capabilities or behaviors to Feathr's MVEL runtime.
 *
 * NOTE: This class is intended for advanced users only, and specifically as a "migration aid" for migrating from
 * some previous versions of Feathr whose FeatureValue representations had a different class name, while preserving
 * compatibility with feature definitions written against those older versions of Feathr.
 */
public class FeathrMvelPluginContext {
  // TODO: Does this need to be "translated" into a different pattern whereby we track the CLASSNAME of the type adaptors
  //       instead of the instance, such that the class mappings can be broadcasted via Spark and then reinitialized on
  //       executor hosts?
  private static final ConcurrentMap<Class<?>, FeatureValueTypeAdaptor<?>> TYPE_ADAPTORS;

  static {
    TYPE_ADAPTORS = new ConcurrentHashMap<>();
    DataConversion.addConversionHandler(FeatureValue.class, new FeathrFeatureValueConversionHandler());
  }

  /**
   * Add a type adaptor to Feathr's MVEL runtime, that will enable Feathr's expressions to support some alternative
   * class representation of {@link FeatureValue} via coercion.
   * @param clazz the class of the "other" alternative representation of feature value
   * @param typeAdaptor the type adaptor that can convert between the "other" representation and {@link FeatureValue}
   * @param <T> type parameter for the "other" feature value class
   */
  @SuppressWarnings("unchecked")
  public <T> void addFeatureTypeAdaptor(Class<T> clazz, FeatureValueTypeAdaptor<T> typeAdaptor) {
    // TODO: MAKE SURE clazz IS NOT ONE OF THE CLASSES ALREADY COVERED IN org.mvel2.DataConversion.CONVERTERS!
    //       IF WE OVERRIDE ANY OF THOSE, IT MIGHT CAUSE MVEL TO BEHAVE IN STRANGE AND UNEXPECTED WAYS!
    TYPE_ADAPTORS.put(clazz, typeAdaptor);
    DataConversion.addConversionHandler(clazz, new ExternalFeatureValueConversionHandler(typeAdaptor));
  }

  static class FeathrFeatureValueConversionHandler implements ConversionHandler {
    @Override
    @SuppressWarnings("unchecked")
    public Object convertFrom(Object in) {
      FeatureValueTypeAdaptor<Object> adaptor = (FeatureValueTypeAdaptor<Object>) TYPE_ADAPTORS.get(in.getClass());
      if (adaptor == null) {
        throw new IllegalArgumentException("Can't convert to Feathr FeatureValue from " + in);
      }
      return adaptor.toFeathrFeatureValue(in);
    }

    @Override
    public boolean canConvertFrom(Class cls) {
      return TYPE_ADAPTORS.containsKey(cls);
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
}
