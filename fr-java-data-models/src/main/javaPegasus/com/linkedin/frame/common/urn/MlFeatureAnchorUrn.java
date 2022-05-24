
package com.linkedin.frame.common.urn;

import java.net.URISyntaxException;
import javax.annotation.Generated;
import com.linkedin.data.template.Custom;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.DirectCoercer;
import com.linkedin.data.template.TemplateOutputCastException;
import com.linkedin.util.ArgumentUtil;


/**
 * An URN that identifies a feature anchor, which contains information about the source of the feature, its extractor, environment and derivation. See go/frameoverview
 * @author urn:li:corpuser:bowu
 * @author urn:li:corpuser:dteng
 * @author urn:li:corpuser:jisha
 * @author urn:li:corpuser:jiwzhang
 * @author urn:li:corpuser:kkao
 * @author urn:li:corpuser:llu
 * @author urn:li:corpuser:qtong
 * @author urn:li:corpuser:yahao
 * @author urn:li:corpuser:yaou
 *
 */
@Generated(value = "com.linkedin.resourceidentity.urnplugin.UrnClassCreator", comments = "generated from models/src/main/resources/com/linkedin/common/urn/registry/MlFeatureAnchor.json")
public class MlFeatureAnchorUrn
    extends Urn
{

  /**
   * Uniquely identifies this Urn's Key Type.
   *
   */
  public final static String ENTITY_TYPE = "mlFeatureAnchor";
  /**
   * Identifies this Urn's namespace.
   *
   */
  public final static String NAMESPACE = "li";
  /**
   * A specific version of a machine learning feature.
   *
   */
  private final MlFeatureVersionUrn _mlFeatureVersion;
  /**
   * The unique id representing a feature anchor.
   *
   */
  private final Long _id;

  static {
    Custom.initializeCustomClass(MlFeatureVersionUrn.class);
    Custom.registerCoercer(new DirectCoercer<MlFeatureAnchorUrn>() {


                             @Override
                             public String coerceInput(MlFeatureAnchorUrn object)
                                 throws ClassCastException
                             {
                               return object.toString();
                             }

                             @Override
                             public MlFeatureAnchorUrn coerceOutput(Object object)
                                 throws TemplateOutputCastException
                             {
                               if (object instanceof String) {
                                 try {
                                   return MlFeatureAnchorUrn.deserialize(((String) object));
                                 } catch (URISyntaxException e) {
                                   throw new TemplateOutputCastException((("Deserializing output '"+ object)+"' failed"), e);
                                 }
                               }
                               throw new TemplateOutputCastException((("Output '"+ object)+("' is not a String, and cannot be coerced to "+ MlFeatureAnchorUrn.class.getName())));
                             }

                           }
        , MlFeatureAnchorUrn.class);
  }

  /**
   * Creates a new instance of a {@link MlFeatureAnchorUrn }.
   *
   * @param mlFeatureVersion
   *     A specific version of a machine learning feature.
   * @param id
   *     The unique id representing a feature anchor.
   */
  protected MlFeatureAnchorUrn(MlFeatureVersionUrn mlFeatureVersion, Long id) {
    super(NAMESPACE, ENTITY_TYPE, TupleKey.create(mlFeatureVersion, id));
    this._mlFeatureVersion = mlFeatureVersion;
    this._id = id;
  }

  /**
   * Internal use only. AutoEncode enabled string fields in entityKey are expected to be already encoded using UrnFieldUtil.
   *
   * @param mlFeatureVersion
   *     A specific version of a machine learning feature.
   * @param entityKey
   *     store encoded string fields
   * @param id
   *     The unique id representing a feature anchor.
   * @param serializedUrn
   *     The serialized URN string if available, else null
   */
  private MlFeatureAnchorUrn(String serializedUrn, TupleKey entityKey, MlFeatureVersionUrn mlFeatureVersion, Long id) {
    super(NAMESPACE, ENTITY_TYPE, entityKey, serializedUrn);
    this._mlFeatureVersion = mlFeatureVersion;
    this._id = id;
  }

  /**
   * Creates an instance of a MlFeatureAnchorUrn from a raw urn string.
   *
   * @param rawUrn
   *     The raw urn input to convert to a full MlFeatureAnchorUrn instance.
   * @deprecated
   *     call {@link #deserialize(String)} instead.
   */
  @Deprecated
  public static MlFeatureAnchorUrn createFromString(String rawUrn)
      throws URISyntaxException
  {
    return MlFeatureAnchorUrn.deserialize(rawUrn);
  }

  /**
   * Creates an instance of a MlFeatureAnchorUrn from generic Urn.
   *
   * @param urn
   *     The URN input to convert to a full MlFeatureAnchorUrn instance.
   */
  public static MlFeatureAnchorUrn createFromUrn(Urn urn)
      throws URISyntaxException
  {
    if (!NAMESPACE.equals(urn.getNamespace())) {
      throw new URISyntaxException(urn.toString(), (("Urn namespace type should be '"+ NAMESPACE)+"'."));
    }
    if (!ENTITY_TYPE.equals(urn.getEntityType())) {
      throw new URISyntaxException(urn.toString(), (("Urn entity type should be '"+ ENTITY_TYPE)+"'."));
    }
    TupleKey key = urn.getEntityKey();
    if (!(key.size() == 2)) {
      throw new URISyntaxException(urn.toString(), "Invalid number of keys.");
    }
    try {
      return new MlFeatureAnchorUrn(null, key, DataTemplateUtil.coerceOutput(key.get(0), MlFeatureVersionUrn.class), key.getAsLong(1));
    } catch (Exception ex) {
      throw new URISyntaxException(urn.toString(), ("Invalid URN Parameter: '"+ ex.getMessage()));
    }
  }

  /**
   * Deserialize a serialized MlFeatureAnchor
   *
   * @param serializedUrn
   *     The serialized urn input to deserialize to a full MlFeatureAnchorUrn instance.
   */
  public static MlFeatureAnchorUrn deserialize(String serializedUrn)
      throws URISyntaxException
  {
    ArgumentUtil.notNull(serializedUrn, "serializedUrn");
    String prefix = "urn:li:mlFeatureAnchor:";
    if (!serializedUrn.startsWith(prefix)) {
      throw new URISyntaxException(serializedUrn, (("Invalid Urn: should have prefix '"+ prefix)+"'."));
    }
    TupleKey key = TupleKey.fromString(serializedUrn, prefix.length());
    if (key.size()!= 2) {
      throw new URISyntaxException(serializedUrn, "Invalid number of keys.");
    }
    try {
      return new MlFeatureAnchorUrn(serializedUrn, key, DataTemplateUtil.coerceOutput(key.get(0), MlFeatureVersionUrn.class), key.getAsLong(1));
    } catch (Exception ex) {
      throw new URISyntaxException(serializedUrn, ("Invalid URN Parameter: '"+ ex.getMessage()));
    }
  }

  private static MlFeatureAnchorUrn decodeUrn(MlFeatureVersionUrn mlFeatureVersion, Long id)
      throws Exception
  {
    return new MlFeatureAnchorUrn(null, TupleKey.create(mlFeatureVersion, id), mlFeatureVersion, id);
  }

  /**
   * A specific version of a machine learning feature.
   *
   * @return
   *     Gets the mlFeatureVersion entity key.
   */
  protected MlFeatureVersionUrn getMlFeatureVersionEntity() {
    return _mlFeatureVersion;
  }

  /**
   * The unique id representing a feature anchor.
   *
   * @return
   *     Gets the id entity key.
   */
  protected Long getIdEntity() {
    return _id;
  }

}
