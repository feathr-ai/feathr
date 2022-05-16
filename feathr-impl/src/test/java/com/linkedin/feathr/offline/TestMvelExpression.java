package com.linkedin.feathr.offline;

import com.linkedin.feathr.offline.mvel.MvelContext;
import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.mvel2.MVEL;
import org.mvel2.integration.PropertyHandlerFactory;
import org.mvel2.integration.VariableResolverFactory;
import org.mvel2.integration.impl.MapVariableResolverFactory;
import org.mvel2.optimizers.OptimizerFactory;
import org.scalatest.testng.TestNGSuite;
import org.testng.annotations.Test;

import static org.testng.Assert.*;


/**
 * Test MVEL expression evaluator
 */
public class TestMvelExpression extends TestNGSuite {
  @Test(description = "test mvel expression foo.bar on a map field of GenericRecord, where foo is the map field "
      + "and bar is the target key in the map")
  public void testMVELExpressionOnMap() {
    String schemaFile =
        "{ \"name\" : \"a\", \"type\" : \"record\", \"fields\" : [" + "   { \"name\" : \"testMap\", " + "     \"type\" : {"
            + "          \"type\" : \"map\", \"values\" : \"string\"" + "     } " + "   } " + "  ] " + "}";
    Schema schema = Schema.parse(schemaFile);
    GenericRecord firstRecord = new GenericData.Record(schema);
    // the map value for the first record, note that it does NOT have the target key 'testKey'
    Map<String, String> firstRecordValue = new HashMap();
    // the map value for the second record, note that it DOES have the target key 'testKey'
    firstRecordValue.put("testKey", "testMapValue1");
    firstRecord.put("testMap", firstRecordValue);

    OptimizerFactory.setDefaultOptimizer(OptimizerFactory.SAFE_REFLECTIVE);
    // test case 1: simple expression
    Serializable compiledExpression = MVEL.compileExpression("testMap.testKey");
    // test case 2: Bug reproduced
    Serializable compiledExpression2 = MVEL.compileExpression("testMap.testKey != null? testMap.testKey : null;\n");
    // test case 3: Working fine, the only difference between case 2 and 3 is changing the first testMap.testKey to testMap['testKey']
    Serializable compiledExpression3 = MVEL.compileExpression("testMap['testKey'] != null? testMap.testKey : null;\n");
    VariableResolverFactory factory = new MapVariableResolverFactory(new HashMap<String, Object>());
    PropertyHandlerFactory.registerPropertyHandler(GenericRecord.class, MvelContext.GenericRecordPropertyHandler.INSTANCE);

    // Evaluate the first record using the compiled MVEL expression, this is the direct root cause of the issue
    assertEquals(MVEL.executeExpression(compiledExpression, firstRecord, factory, String.class), "testMapValue1");
    assertEquals(MVEL.executeExpression(compiledExpression2, firstRecord, factory, String.class), "testMapValue1");
    assertEquals(MVEL.executeExpression(compiledExpression3, firstRecord, factory, String.class), "testMapValue1");

    Map<String, String> secondRecordValue = new HashMap();
    // the map value for the second record, note that it DOES have the target key 'testKey'
    secondRecordValue.put("testKey", "testMapValue");
    GenericRecord secondRecord = new GenericData.Record(schema);
    secondRecord.put("testMap", secondRecordValue);

    // This assert will fail, and MVEL library is on version < 2.4.7, there were 3 ways to fix this:
    // 1. Comment out the previous MVEL.executeExpression()
    // 2. Recompile the MVEL expression here, but this will bring performance issue (as we should not compile for each record)
    // 3. Change the elCtx to ctx in ThisValueAccessor
    assertEquals(MVEL.executeExpression(compiledExpression, secondRecord, factory, String.class), "testMapValue");
    assertEquals(MVEL.executeExpression(compiledExpression2, secondRecord, factory, String.class), "testMapValue");
    assertEquals(MVEL.executeExpression(compiledExpression3, secondRecord, factory, String.class), "testMapValue");
  }
}