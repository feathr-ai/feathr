package com.linkedin.frame.common.urn;

import com.linkedin.data.template.Custom;
import com.linkedin.data.template.DirectCoercer;
import com.linkedin.data.template.TemplateOutputCastException;
import com.linkedin.util.ArgumentUtil;
import java.net.URISyntaxException;


public class MlFeatureUrn extends Urn {
  public static final String ENTITY_TYPE = "mlFeature";
  public static final String NAMESPACE = "li";
  private final String _namespace;
  private final String _name;

  protected MlFeatureUrn(String namespace, String name) {
    super("li", "mlFeature", TupleKey.create(new Object[]{namespace, name}));
    this._namespace = namespace;
    this._name = name;
  }

  private MlFeatureUrn(TupleKey entityKey, String namespace, String name) {
    super("li", "mlFeature", entityKey);
    this._namespace = namespace;
    this._name = name;
  }

  /** @deprecated */
  @Deprecated
  public static MlFeatureUrn createFromString(String rawUrn) throws URISyntaxException {
    return createFromUrn(Urn.createFromString(rawUrn));
  }

  public static MlFeatureUrn createFromUrn(Urn urn) throws URISyntaxException {
    if (!"li".equals(urn.getNamespace())) {
      throw new URISyntaxException(urn.toString(), "Urn namespace type should be 'li'.");
    } else if (!"mlFeature".equals(urn.getEntityType())) {
      throw new URISyntaxException(urn.toString(), "Urn entity type should be 'mlFeature'.");
    } else {
      TupleKey key = urn.getEntityKey();
      if (key.size() != 2) {
        throw new URISyntaxException(urn.toString(), "Invalid number of keys.");
      } else {
        try {
          return decodeUrn((String)key.getAs(0, String.class), (String)key.getAs(1, String.class));
        } catch (Exception var3) {
          throw new URISyntaxException(urn.toString(), "Invalid URN Parameter: '" + var3.getMessage());
        }
      }
    }
  }

  public static MlFeatureUrn deserialize(String serializedUrn) throws URISyntaxException {
    ArgumentUtil.notNull(serializedUrn, "serializedUrn");
    String prefix = "urn:li:mlFeature:";
    if (!serializedUrn.startsWith(prefix)) {
      throw new URISyntaxException(serializedUrn, "Invalid Urn: should have prefix '" + prefix + "'.");
    } else {
      String typeSpecificString = serializedUrn.substring(prefix.length());
      TupleKey key = TupleKey.fromString(typeSpecificString);
      if (key.size() != 2) {
        throw new URISyntaxException(serializedUrn, "Invalid number of keys.");
      } else {
        try {
          return decodeUrn((String)key.getAs(0, String.class), (String)key.getAs(1, String.class));
        } catch (Exception var5) {
          throw new URISyntaxException(serializedUrn, "Invalid URN Parameter: '" + var5.getMessage());
        }
      }
    }
  }

  private static MlFeatureUrn decodeUrn(String namespace, String name) throws Exception {
    return new MlFeatureUrn(TupleKey.create(new Object[]{namespace, name}), namespace, name);
  }

  protected String getNamespaceEntity() {
    return this._namespace;
  }

  protected String getNameEntity() {
    return this._name;
  }

  static {
    Custom.registerCoercer(new DirectCoercer<MlFeatureUrn>() {
      public String coerceInput(MlFeatureUrn object) throws ClassCastException {
        return object.toString();
      }

      public MlFeatureUrn coerceOutput(Object object) throws TemplateOutputCastException {
        if (object instanceof String) {
          try {
            return MlFeatureUrn.deserialize((String)object);
          } catch (URISyntaxException var3) {
            throw new TemplateOutputCastException("Deserializing output '" + object + "' failed", var3);
          }
        } else {
          throw new TemplateOutputCastException("Output '" + object + "' is not a String, and cannot be coerced to " + MlFeatureUrn.class.getName());
        }
      }
    }, MlFeatureUrn.class);
  }
}
