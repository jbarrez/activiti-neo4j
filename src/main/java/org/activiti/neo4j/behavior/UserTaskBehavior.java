package org.activiti.neo4j.behavior;

import org.activiti.neo4j.Constants;
import org.activiti.neo4j.Execution;
import org.activiti.neo4j.InternalActivitiEngine;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;


public class UserTaskBehavior extends AbstractBehavior {
  
  public void execute(Execution execution, InternalActivitiEngine internalActivitiEngine) {

    Node taskNode = execution.getEndNode();
//    System.out.println("Entered user task " + taskNode.getProperty("id"));
    
    execution.setProperty("isTask", true);
    execution.setProperty("name", taskNode.getProperty("name"));
    
    
    Index<Relationship> taskIndex = execution.getRelationship().getGraphDatabase().index().forRelationships(Constants.TASK_INDEX);
    taskIndex.add(execution.getRelationship(), Constants.INDEX_KEY_TASK_ASSIGNEE, taskNode.getProperty("assignee"));
    
    // No leave(), task == wait state
  }
  
  @Override
  public void signal(Execution execution, InternalActivitiEngine internalActivitiEngine) {
    
    // remove properties from execution
    execution.removeProperty("isTask");
    execution.removeProperty("name");
    
    // Leave step
    leave(execution, internalActivitiEngine);
  }

}
