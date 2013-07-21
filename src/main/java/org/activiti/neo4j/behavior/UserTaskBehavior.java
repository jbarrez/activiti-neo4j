package org.activiti.neo4j.behavior;

import org.activiti.neo4j.Activity;
import org.activiti.neo4j.Constants;
import org.activiti.neo4j.EngineOperations;
import org.activiti.neo4j.Execution;


public class UserTaskBehavior extends AbstractBehavior {
  
  public void execute(Execution execution, EngineOperations engineOperations) {
    
    Activity taskActivity = execution.getActivity();

    execution.setProperty("isTask", true);
    execution.setProperty("name", taskActivity.getProperty("name"));
    
    // Add to task index
    execution.addToIndex(Constants.TASK_INDEX, Constants.INDEX_KEY_TASK_ASSIGNEE, taskActivity.getProperty("assignee"));
    
    // No leave(), task == wait state
  }
  
  @Override
  public void signal(Execution execution, EngineOperations engineOperations) {
    
    // remove properties from execution
    // TODO: need to find a better way to manage this
    execution.removeProperty("isTask");
    execution.removeProperty("name");
    
    // Leave step
    leave(execution, engineOperations);
  }

}
