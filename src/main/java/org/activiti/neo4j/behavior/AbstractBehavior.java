package org.activiti.neo4j.behavior;

import java.util.ArrayList;
import java.util.List;

import org.activiti.neo4j.EngineOperations;
import org.activiti.neo4j.Execution;
import org.activiti.neo4j.RelTypes;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;


public abstract class AbstractBehavior implements Behavior {
  
  protected void leave(Execution execution, EngineOperations engineOperations) {
    Node processInstanceNode = execution.getStartNode();
    Node currentActivityNode = execution.getEndNode();
    
//    System.out.println("Entered " + currentActivityNode.getProperty("id"));
    
    List<Node> nextNodes = new ArrayList<Node>();
    for (Relationship sequenceFlow : currentActivityNode.getRelationships(Direction.OUTGOING, RelTypes.SEQ_FLOW)) {
      nextNodes.add(sequenceFlow.getEndNode());
    }
    
    // Move execution to next activities
    // TODO: is it possible to reuse this execute for a little bit extra performance?
    execution.delete();
    
    for (Node nextNode : nextNodes) {
      Relationship outgoingExecution = processInstanceNode.createRelationshipTo(nextNode, RelTypes.EXECUTION);
      engineOperations.continueProcess(new Execution(outgoingExecution));
    }
  }
  
  protected void gotoNode(Node node, Execution execution, EngineOperations engineOperations) {
    execution.delete();
    
    Node processInstanceNode = execution.getStartNode();
    Relationship outgoingExecution = processInstanceNode.createRelationshipTo(node, RelTypes.EXECUTION);
    engineOperations.continueProcess(new Execution(outgoingExecution));
  }
  
  public void signal(Execution execution, EngineOperations engineOperations) {
    throw new RuntimeException("This behavior does not accept signals");
  }

}
