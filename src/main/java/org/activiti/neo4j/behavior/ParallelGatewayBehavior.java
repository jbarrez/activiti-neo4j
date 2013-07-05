package org.activiti.neo4j.behavior;

import java.util.ArrayList;
import java.util.List;

import org.activiti.neo4j.Execution;
import org.activiti.neo4j.InternalActivitiEngine;
import org.activiti.neo4j.RelTypes;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;


public class ParallelGatewayBehavior extends AbstractBehavior {

  public void execute(Execution execution, InternalActivitiEngine internalActivitiEngine) {
    
    // Check the number of incoming to see if we have to join or fork
    Node parallelGwNode = execution.getEndNode();
    Iterable<Relationship> incomingSequenceFlows = parallelGwNode.getRelationships(Direction.INCOMING, RelTypes.SEQ_FLOW);

    // Is there a better way to do a count? Maybe just store it as property?
    int incomingSequenceFlowCount = 0;
    for (Relationship incomingSequenceFlow : incomingSequenceFlows) {
      incomingSequenceFlowCount++;
    }
    
    if (incomingSequenceFlowCount == 1) {
      
      // Fork immediately (optimisation)
      leave(execution, internalActivitiEngine);
      
    } else {
      
      // Join: if number of executions arrived here = number of incoming sequenceflow -> continue
      List<Execution> waitingExecutions = new ArrayList<Execution>();
      waitingExecutions.add(execution);
      
      Node processInstanceNode = execution.getStartNode();
      for (Relationship exec : processInstanceNode.getRelationships(Direction.OUTGOING, RelTypes.EXECUTION)){
        if (exec.getEndNode().getProperty("id").equals(parallelGwNode.getProperty("id"))) {
          waitingExecutions.add(new Execution(exec));
        }
      }
      
      if (waitingExecutions.size() == incomingSequenceFlowCount) {
        
        // Remove all executions
        for (Execution exec : waitingExecutions) {
          exec.delete();
        }
        
        // Create new one to leave gateway
        Relationship outgoingExecution = processInstanceNode.createRelationshipTo(parallelGwNode, RelTypes.EXECUTION);
        leave(new Execution(outgoingExecution), internalActivitiEngine);
        
      }
      
    }
    
  }

}
