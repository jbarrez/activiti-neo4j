package org.activiti.neo4j.behavior;

import org.activiti.neo4j.Execution;
import org.activiti.neo4j.InternalActivitiEngine;


public class NoneStartEventBehavior extends AbstractBehavior {
  
  public void execute(Execution execution, InternalActivitiEngine internalActivitiEngine) {
    leave(execution, internalActivitiEngine);
  }

}
