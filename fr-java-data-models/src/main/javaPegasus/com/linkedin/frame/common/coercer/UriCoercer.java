package com.linkedin.frame.common.coercer;

import java.net.URI;
import java.net.URISyntaxException;

import com.linkedin.data.template.Custom;
import com.linkedin.data.template.DirectCoercer;
import com.linkedin.data.template.TemplateOutputCastException;


/**
 * @author Josh Walker
 * @version $Revision: $
 */

public class UriCoercer implements DirectCoercer<URI> {
  //Auto-register this coercer.  See {@link Custom#initializeCoercerClass}
  private static final boolean REGISTER_COERCER = Custom.registerCoercer(new UriCoercer(), URI.class);

  @Override
  public Object coerceInput(URI object) throws ClassCastException {
    return object.toString();
  }

  @Override
  public URI coerceOutput(Object object) throws TemplateOutputCastException {
    try {
      return new URI((String) object);
    } catch (URISyntaxException e) {
      throw new TemplateOutputCastException("Invalid URI format", e);
    }
  }
}
