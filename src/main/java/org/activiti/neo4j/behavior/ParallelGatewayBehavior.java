package org.activiti.neo4j.behavior;

import java.util.ArrayList;
import java.util.List;

import org.activiti.neo4j.Activity;
import org.activiti.neo4j.EngineOperations;
import org.activiti.neo4j.Execution;
import org.activiti.neo4j.ProcessInstance;


public class ParallelGatewayBehavior extends AbstractBehavior {

  public void execute(Execution execution, EngineOperations engineOperations) {
    
    Activity parallelGatewayActivity = execution.getActivity();
    
    // Check the number of incoming to see if we have to join or fork
    int incomingSequenceFlowCount = execution.getActivity().getIncomingSequenceFlow().size();
    
    if (incomingSequenceFlowCount == 1) {
      
      // Fork immediately (optimisation)
      leave(execution, engineOperations);
      
    } else {
      
      // Join: if number of executions arrived here = number of incoming sequenceflow -> continue
      List<Execution> waitingExecutions = new ArrayList<Execution>();
      waitingExecutions.add(execution);
      
      ProcessInstance processInstance = execution.getProcessInstance();
      for (Execution e : processInstance.getExecutions()){
        if (e.getActivity().getId().equals(execution.getActivity().getId())) {
          waitingExecutions.add(e);
        }
      }
      
      if (waitingExecutions.size() == incomingSequenceFlowCount) {
        
        // Remove all executions
        for (Execution e : waitingExecutions) {
          e.delete();
        }
        
        // Create new one to leave gateway
        Execution outgoingExecution = processInstance.createNewExecutionInActivity(parallelGatewayActivity);
        leave(outgoingExecution, engineOperations);
        
      }
      
    }
    
  }

}
