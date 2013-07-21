package org.activiti.neo4j.behavior;

import org.activiti.neo4j.EngineOperations;
import org.activiti.neo4j.Execution;
import org.activiti.neo4j.ProcessInstance;


public class NoneEndEventBehavior extends AbstractBehavior {
  
  public void execute(Execution execution, EngineOperations engineOperations) {
    // Remove process instance node
    ProcessInstance processInstance = execution.getProcessInstance();
    processInstance.delete();
  }

}
