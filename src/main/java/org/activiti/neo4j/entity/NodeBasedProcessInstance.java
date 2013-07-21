/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.activiti.neo4j.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.activiti.neo4j.Activity;
import org.activiti.neo4j.Execution;
import org.activiti.neo4j.ProcessInstance;
import org.activiti.neo4j.RelTypes;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;


/**
 * @author Joram Barrez
 */
public class NodeBasedProcessInstance implements ProcessInstance {
  
  protected Node processInstanceNode;
  
  public NodeBasedProcessInstance(Node processInstanceNode) {
    this.processInstanceNode = processInstanceNode;
  }

  public void setVariable(String variableName, Object variableValue) {
    Node variableNode = getVariableNode();
    if (variableNode == null) {
      variableNode = processInstanceNode.getGraphDatabase().createNode();
      processInstanceNode.createRelationshipTo(variableNode, RelTypes.VARIABLE);
    }
    
    variableNode.setProperty(variableName, variableValue);
  }
  
  protected Node getVariableNode() {
    Iterator<Relationship> variableRelationShipIterator = 
            processInstanceNode.getRelationships(Direction.OUTGOING, RelTypes.VARIABLE).iterator();
    
    Node variableNode = null;
    if (variableRelationShipIterator.hasNext()) {
      Relationship variableRelationship = variableRelationShipIterator.next();
      variableNode = variableRelationship.getEndNode();
    }
    
    return variableNode;
  }
  
  public Execution createNewExecutionInActivity(Activity activity) {
    Relationship executionRelationship = processInstanceNode
            .createRelationshipTo(((NodeBasedActivity) activity).getActivityNode(), RelTypes.EXECUTION);
    return new NodeBasedExecution(executionRelationship);
  }
  
  public void delete() {
    // Executions
    for (Execution execution : getExecutions()) {
      execution.delete();
    }
    
    // Variables
    Node variableNode = getVariableNode();
    if (variableNode != null) {
      variableNode.delete();
    }
    
    // Delete relationship from process definition to process instance
    for (Relationship relationship : processInstanceNode.getRelationships(RelTypes.PROCESS_INSTANCE)) {
      relationship.delete();
    }
    
    processInstanceNode.delete();
  }
  
  public List<Execution> getExecutions() {
    List<Execution> executions = new ArrayList<Execution>();
    for (Relationship executionRelationship : processInstanceNode.getRelationships(Direction.OUTGOING, RelTypes.EXECUTION)) {
      executions.add(new NodeBasedExecution(executionRelationship));
    }
    return executions;
  }
  
}
