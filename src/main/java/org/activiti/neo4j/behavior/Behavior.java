package org.activiti.neo4j.behavior;

import org.activiti.neo4j.Execution;
import org.activiti.neo4j.InternalActivitiEngine;


public interface Behavior {
  
  void execute(Execution execution, InternalActivitiEngine internalActivitiEngine);
  
  void signal(Execution execution, InternalActivitiEngine internalActivitiEngine);

}
