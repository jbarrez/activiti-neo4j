package org.activiti.neo4j;


public class ProcessDefinition {
  
  protected long id;
  protected String key;
  
  public long getId() {
    return id;
  }
  
  public void setId(long id) {
    this.id = id;
  }
  
  public String getKey() {
    return key;
  }
  
  public void setKey(String key) {
    this.key = key;
  }
  
}
