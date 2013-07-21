package org.activiti.neo4j.behavior;

import java.util.ArrayList;
import java.util.List;

import org.activiti.neo4j.Activity;
import org.activiti.neo4j.EngineOperations;
import org.activiti.neo4j.Execution;
import org.activiti.neo4j.SequenceFlow;


public abstract class AbstractBehavior implements Behavior {
  
  protected void leave(Execution execution, EngineOperations engineOperations) {
    
    List<Execution> outgoingExecutions = new ArrayList<Execution>();
    for (SequenceFlow sequenceFlow : execution.getActivity().getOutgoingSequenceFlow()) {
      Activity targetActivity = sequenceFlow.getTargetActivity();
      outgoingExecutions.add(execution.getProcessInstance().createNewExecutionInActivity(targetActivity));
    }
    
    // TODO: is it possible to reuse this execute for a little bit extra performance?
    // Or is this just overengineering? (we did it for exections in regular activiti ...)
    execution.delete();
    
    for (Execution outgoingExecuions : outgoingExecutions) {
      engineOperations.continueProcess(outgoingExecuions);
    }
    
  }
  
  protected void goToNextActivity(Activity activity, Execution execution, EngineOperations engineOperations) {
    Execution outgoingExecution = execution.getProcessInstance().createNewExecutionInActivity(activity);
    execution.delete();
    engineOperations.continueProcess(outgoingExecution);
  }
  
  public void signal(Execution execution, EngineOperations engineOperations) {
    throw new RuntimeException("This behavior does not accept signals");
  }

}
