package com.linkedin.feathr.core.configdataprovider;

import java.io.BufferedReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.testng.annotations.Test;

import static org.testng.Assert.*;


/**
 * Unit tests for {@link ManifestConfigDataProvider}
 */
public class ManifestConfigDataProviderTest {

  @Test(description = "Tests getting Readers for files listed in a manifest file")
  public void test() {
    String manifest = "config/manifest3.conf";

    try (ManifestConfigDataProvider cdp = new ManifestConfigDataProvider(manifest)) {
      List<BufferedReader> readers = cdp.getConfigDataReaders()
          .stream()
          .map(BufferedReader::new)
          .collect(Collectors.toList());

      assertEquals(readers.size(), 2);

      for (BufferedReader r : readers) {
        Stream<String> stringStream = r.lines();
        long lineCount = stringStream.count();
        assertTrue(lineCount > 0, "Expected line count > 0, found " + lineCount);
      }
    } catch (Exception e) {
      fail("Caught exception", e);
    }
  }
}
