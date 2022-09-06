package com.feathr.online;

import java.util.HashMap;
import java.util.Map;


/**
 * Demo class to showcase how Feathr online works.
 */
public class EntityKey {
  // TODO: should we do strong typing here: EntityKeyName -> Object?
  private Map<String, Object> entityKeyMap;
  private Object requestData;


  public EntityKey(Map<String, Object> entityKeyMap) {
    this.entityKeyMap = entityKeyMap;
  }

  public EntityKey(String entityKeyName, Object entityKeyValue) {
    Map<String, Object> entityKeyMap = new HashMap<>();
    entityKeyMap.put(entityKeyName, entityKeyValue);
    this.entityKeyMap = entityKeyMap;
  }

  public EntityKey(String entityKeyName, Object entityKeyValue, Object requestData) {
    Map<String, Object> entityKeyMap = new HashMap<>();
    entityKeyMap.put(entityKeyName, entityKeyValue);
    this.entityKeyMap = entityKeyMap;
    this.requestData = requestData;
  }

  public Object getEntityKey(String entityKeyName) {
    return entityKeyMap.get(entityKeyName);
  }
}
