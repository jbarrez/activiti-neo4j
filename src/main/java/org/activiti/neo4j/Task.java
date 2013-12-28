package org.activiti.neo4j;

/**
 * @author jbarrez
 */
public class Task {
  
  protected String id;
  protected String name;
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
}
