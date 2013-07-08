package org.activiti.neo4j.behavior;

import org.activiti.neo4j.EngineOperations;
import org.activiti.neo4j.Execution;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;


public class NoneEndEventBehavior extends AbstractBehavior {
  
  public void execute(Execution execution, EngineOperations engineOperations) {
    // Remove process instance node
    Node processInstanceNode = execution.getStartNode();
    
    for (Relationship relationship : processInstanceNode.getRelationships()) {
      relationship.delete();
    }
    
    processInstanceNode.delete();
    
//    System.out.println("Process instance ended");
    
  }

}
