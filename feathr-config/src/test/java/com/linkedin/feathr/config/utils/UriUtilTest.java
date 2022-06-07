package com.linkedin.feathr.config.utils;

import java.net.URI;
import java.util.List;
import org.testng.annotations.Test;

import static org.testng.Assert.*;


public class UriUtilTest {
  @Test
  public void testBuildUri() {
    String testUriString = "testUriString";
    URI uri = UriUtil.buildUri(testUriString);
    assertEquals(uri.toString(), testUriString);
  }

  @Test
  public void testBuildUris() {
    String[] testUriStrings = new String[] {"testUri1", "testUri2", "testUri3"};
    List<URI> uris = UriUtil.buildUris(testUriStrings);
    assertEquals(uris.size(), 3);
    assertEquals(uris.get(0).toString(), testUriStrings[0]);
    assertEquals(uris.get(1).toString(), testUriStrings[1]);
    assertEquals(uris.get(2).toString(), testUriStrings[2]);
  }
}
