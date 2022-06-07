package com.linkedin.feathr.config.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * URI util class
 */
public class UriUtil {
  private UriUtil() {
  }

  public static URI buildUri(String uriString) {
    try {
      return new URI(uriString);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("Invalid Uri " + uriString, e);
    }
  }

  public static List<URI> buildUris(String[] uriStrings) {
    return Arrays.stream(uriStrings)
        .map(UriUtil::buildUri)
        .collect(Collectors.toList());
  }
}
