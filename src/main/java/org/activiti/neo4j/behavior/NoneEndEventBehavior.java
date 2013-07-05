package org.activiti.neo4j.behavior;

import org.activiti.neo4j.Execution;
import org.activiti.neo4j.InternalActivitiEngine;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;


public class NoneEndEventBehavior extends AbstractBehavior {
  
  public void execute(Execution execution, InternalActivitiEngine internalActivitiEngine) {
    // Remove process instance node
    Node processInstanceNode = execution.getStartNode();
    
    for (Relationship relationship : processInstanceNode.getRelationships()) {
      relationship.delete();
    }
    
    processInstanceNode.delete();
    
//    System.out.println("Process instance ended");
    
  }

}
