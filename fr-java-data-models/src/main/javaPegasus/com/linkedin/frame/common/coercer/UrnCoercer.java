package com.linkedin.frame.common.coercer;

import com.linkedin.data.template.DirectCoercer;
import com.linkedin.data.template.TemplateOutputCastException;
import com.linkedin.frame.common.urn.Urn;
import java.net.URISyntaxException;


/**
 * @author Josh Walker
 * @version $Revision: $
 */

public class UrnCoercer implements DirectCoercer<Urn> {
  @Override
  public Object coerceInput(Urn object) throws ClassCastException {
    return object.toString();
  }

  @Override
  public Urn coerceOutput(Object object) throws TemplateOutputCastException {
    if (object.getClass() != String.class) {
      throw new TemplateOutputCastException("Urn not backed by String");
    }
    try {
      return Urn.createFromString((String) object);
    } catch (URISyntaxException e) {
      throw new TemplateOutputCastException("Invalid URN syntax: " + e.getMessage(), e);
    }
  }

}