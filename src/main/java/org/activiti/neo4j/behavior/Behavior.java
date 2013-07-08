package org.activiti.neo4j.behavior;

import org.activiti.neo4j.EngineOperations;
import org.activiti.neo4j.Execution;


public interface Behavior {
  
  void execute(Execution execution, EngineOperations engineOperations);
  
  void signal(Execution execution, EngineOperations engineOperations);

}
