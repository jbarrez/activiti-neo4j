package org.activiti.neo4j.behavior;

import org.activiti.neo4j.EngineOperations;
import org.activiti.neo4j.Execution;


public class NoneStartEventBehavior extends AbstractBehavior {
  
  public void execute(Execution execution, EngineOperations engineOperations) {
    leave(execution, engineOperations);
  }

}
