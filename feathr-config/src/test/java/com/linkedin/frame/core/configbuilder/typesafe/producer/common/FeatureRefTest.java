package com.linkedin.frame.core.configbuilder.typesafe.producer.common;

import com.linkedin.frame.core.config.producer.common.FeatureRef;
import com.linkedin.frame.core.config.producer.common.Namespace;
import com.linkedin.frame.core.config.producer.common.TypedRef;
import com.linkedin.frame.core.config.producer.common.Version;
import com.linkedin.frame.core.configbuilder.ConfigBuilderException;
import org.testng.annotations.Test;

import static com.linkedin.frame.core.config.producer.common.TypedRef.*;
import static org.testng.Assert.*;

/*
 * Tests build of FeatureRef objects
 */
public class FeatureRefTest {

  @Test(description = "Tests build of FeatureRef with explicit namespace and version")
  public void testWithExplicitNamespaceAndVersion() {
    String namespaceStr = "flagship";
    String name = "eAffinity";
    String majorVersion = "2";
    String minorVersion = "0";

    String featureRefString = String.join(DELIM, namespaceStr, name, majorVersion, minorVersion);

    Namespace namespace = new Namespace(namespaceStr);
    Version version = new Version(String.join(".", majorVersion, minorVersion));

    FeatureRef expFeatureRef = new FeatureRef(namespace, name, version);

    testFeatureRef(featureRefString, expFeatureRef);
  }

  @Test(description = "Tests build of FeatureRef with only name")
  public void testLegacyFeatureRef() {
    String name = "eAffinity";

    FeatureRef expFeatureRef = new FeatureRef(name);

    testFeatureRef(name, expFeatureRef);
  }

  @Test(description = "Tests string representation of FeatureRef with explicit namespace, name and version")
  public void testToStringWithExplicitNamespaceAndVersion() {
    String namespaceStr = "flagship";
    String name = "eAffinity";
    String majorVersion = "2";
    String minorVersion = "0";

    String expFeatureRefString = String.join(DELIM, namespaceStr, name, majorVersion, minorVersion);

    Namespace namespace = new Namespace(namespaceStr);
    Version version = new Version(String.join(".", majorVersion, minorVersion));

    FeatureRef featureRef = new FeatureRef(namespace, name, version);

    String obsFeatureRefString = featureRef.toString();
    assertEquals(obsFeatureRefString, expFeatureRefString);
  }

  @Test(description = "Tests string representation of FeatureRef with explicit name and version")
  public void testToStringWithExplicitNameAndVersion() {
    String name = "eAffinity";
    String majorVersion = "2";
    String minorVersion = "0";

    String expFeatureRefString = String.join(DELIM, name, majorVersion, minorVersion);

    Version version = new Version(String.join(".", majorVersion, minorVersion));

    FeatureRef featureRef = new FeatureRef(null, name, version);

    String obsFeatureRefString = featureRef.toString();
    assertEquals(obsFeatureRefString, expFeatureRefString);
  }

  @Test(description = "Tests string representation of FeatureRef with only name")
  public void testToStringWithLegacyName() {
    String name = "eAffinity";

    FeatureRef featureRef = new FeatureRef(name);

    String obsFeatureRefString = featureRef.toString();
    assertEquals(obsFeatureRefString, name);
  }

  @Test(description = "Tests string representation of FeatureRef with namespace and name only")
  public void testFeatureRefWithNoVersion() {
    String namespace = "flagship";
    String name = "one";

    String featureRefStr = String.join(DELIM, namespace, name);

    FeatureRef obsFeatureRef = new FeatureRef(featureRefStr);
    assertEquals(obsFeatureRef.toString(), featureRefStr);
  }

  @Test
  public void testLegacyFeatureRefWithSpecialCharacters() {
    String name = "eAffinity:xyz.1.0";

    FeatureRef featureRef = new FeatureRef(name);

    String obsFeatureRefString = featureRef.getName();
    assertEquals(obsFeatureRefString, name);
  }

  @Test(description = "Test FeatureRef creation with a leading '_'",
  expectedExceptions = ConfigBuilderException.class)
  public void testFeatureRefWithLeadingUnderscore() {
    String namespace = "_flagship";
    String name = "one";
    String major = "4";
    String minor = "2";

    String featureRefStr = String.join(DELIM, namespace, name, major, minor);

    FeatureRef featureRef = new FeatureRef(featureRefStr);
  }

  private void testFeatureRef(String featureRefStr, FeatureRef expFeatureRef) {
    FeatureRef obsFeatureRef = new FeatureRef(featureRefStr);
    assertEquals(obsFeatureRef, expFeatureRef);
  }
}
