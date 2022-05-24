package com.linkedin.frame.common.urn;

import com.linkedin.data.template.Custom;
import com.linkedin.data.template.DirectCoercer;
import com.linkedin.data.template.TemplateOutputCastException;
import com.linkedin.util.ArgumentUtil;
import java.net.URISyntaxException;


public class MlFeatureVersionUrn extends Urn {
  public static final String ENTITY_TYPE = "mlFeatureVersion";
  public static final String NAMESPACE = "li";
  private final MlFeatureUrn _mlFeature;
  private final Integer _majorVersion;
  private final Integer _minorVersion;
  private final Integer _patchVersion;

  protected MlFeatureVersionUrn(MlFeatureUrn mlFeature, Integer majorVersion, Integer minorVersion, Integer patchVersion) {
    super("li", "mlFeatureVersion", TupleKey.create(new Object[]{mlFeature, majorVersion, minorVersion, patchVersion}));
    this._mlFeature = mlFeature;
    this._majorVersion = majorVersion;
    this._minorVersion = minorVersion;
    this._patchVersion = patchVersion;
  }

  private MlFeatureVersionUrn(TupleKey entityKey, MlFeatureUrn mlFeature, Integer majorVersion, Integer minorVersion, Integer patchVersion) {
    super("li", "mlFeatureVersion", entityKey);
    this._mlFeature = mlFeature;
    this._majorVersion = majorVersion;
    this._minorVersion = minorVersion;
    this._patchVersion = patchVersion;
  }

  /** @deprecated */
  @Deprecated
  public static MlFeatureVersionUrn createFromString(String rawUrn) throws URISyntaxException {
    return createFromUrn(Urn.createFromString(rawUrn));
  }

  public static MlFeatureVersionUrn createFromUrn(Urn urn) throws URISyntaxException {
    if (!"li".equals(urn.getNamespace())) {
      throw new URISyntaxException(urn.toString(), "Urn namespace type should be 'li'.");
    } else if (!"mlFeatureVersion".equals(urn.getEntityType())) {
      throw new URISyntaxException(urn.toString(), "Urn entity type should be 'mlFeatureVersion'.");
    } else {
      TupleKey key = urn.getEntityKey();
      if (key.size() != 4) {
        throw new URISyntaxException(urn.toString(), "Invalid number of keys.");
      } else {
        try {
          return decodeUrn((MlFeatureUrn)key.getAs(0, MlFeatureUrn.class), (Integer)key.getAs(1, Integer.class), (Integer)key.getAs(2, Integer.class), (Integer)key.getAs(3, Integer.class));
        } catch (Exception var3) {
          throw new URISyntaxException(urn.toString(), "Invalid URN Parameter: '" + var3.getMessage());
        }
      }
    }
  }

  public static MlFeatureVersionUrn deserialize(String serializedUrn) throws URISyntaxException {
    ArgumentUtil.notNull(serializedUrn, "serializedUrn");
    String prefix = "urn:li:mlFeatureVersion:";
    if (!serializedUrn.startsWith(prefix)) {
      throw new URISyntaxException(serializedUrn, "Invalid Urn: should have prefix '" + prefix + "'.");
    } else {
      String typeSpecificString = serializedUrn.substring(prefix.length());
      TupleKey key = TupleKey.fromString(typeSpecificString);
      if (key.size() != 4) {
        throw new URISyntaxException(serializedUrn, "Invalid number of keys.");
      } else {
        try {
          return decodeUrn((MlFeatureUrn)key.getAs(0, MlFeatureUrn.class), (Integer)key.getAs(1, Integer.class), (Integer)key.getAs(2, Integer.class), (Integer)key.getAs(3, Integer.class));
        } catch (Exception var5) {
          throw new URISyntaxException(serializedUrn, "Invalid URN Parameter: '" + var5.getMessage());
        }
      }
    }
  }

  private static MlFeatureVersionUrn decodeUrn(MlFeatureUrn mlFeature, Integer majorVersion, Integer minorVersion, Integer patchVersion) throws Exception {
    return new MlFeatureVersionUrn(TupleKey.create(new Object[]{mlFeature, majorVersion, minorVersion, patchVersion}), mlFeature, majorVersion, minorVersion, patchVersion);
  }

  protected MlFeatureUrn getMlFeatureEntity() {
    return this._mlFeature;
  }

  protected Integer getMajorVersionEntity() {
    return this._majorVersion;
  }

  protected Integer getMinorVersionEntity() {
    return this._minorVersion;
  }

  protected Integer getPatchVersionEntity() {
    return this._patchVersion;
  }

  static {
    Custom.initializeCustomClass(MlFeatureUrn.class);
    Custom.registerCoercer(new DirectCoercer<MlFeatureVersionUrn>() {
      public String coerceInput(MlFeatureVersionUrn object) throws ClassCastException {
        return object.toString();
      }

      public MlFeatureVersionUrn coerceOutput(Object object) throws TemplateOutputCastException {
        if (object instanceof String) {
          try {
            return MlFeatureVersionUrn.deserialize((String)object);
          } catch (URISyntaxException var3) {
            throw new TemplateOutputCastException("Deserializing output '" + object + "' failed", var3);
          }
        } else {
          throw new TemplateOutputCastException("Output '" + object + "' is not a String, and cannot be coerced to " + MlFeatureVersionUrn.class.getName());
        }
      }
    }, MlFeatureVersionUrn.class);
  }
}
