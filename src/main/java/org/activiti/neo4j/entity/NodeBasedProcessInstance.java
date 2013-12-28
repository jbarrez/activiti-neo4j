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

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;


/**
 * @author Joram Barrez
 */
public class NodeBasedProcessInstance implements ProcessInstance {
  
  protected Graph graphDb;
  
  protected Vertex processInstanceNode;
  
  public NodeBasedProcessInstance(Graph graphDb, Vertex processInstanceNode) {
    this.graphDb = graphDb;
    this.processInstanceNode = processInstanceNode;
  }

  public void setVariable(String variableName, Object variableValue) {
    Vertex variableNode = getVariableNode();
    if (variableNode == null) {
      variableNode = graphDb.addVertex(null);
      processInstanceNode.addEdge(RelTypes.VARIABLE.toString(), variableNode);
    }
    
    variableNode.setProperty(variableName, variableValue);
  }
  
  protected Vertex getVariableNode() {
    Iterator<Edge> variableRelationShipIterator = 
            processInstanceNode.getEdges(Direction.OUT, RelTypes.VARIABLE.toString()).iterator();
    
    Vertex variableNode = null;
    if (variableRelationShipIterator.hasNext()) {
      Edge variableRelationship = variableRelationShipIterator.next();
      variableNode = variableRelationship.getVertex(Direction.IN);
    }
    
    return variableNode;
  }
  
  public Execution createNewExecutionInActivity(Activity activity) {
    Edge executionRelationship = processInstanceNode
            .addEdge(RelTypes.EXECUTION.toString(), ((NodeBasedActivity) activity).getActivityNode());
    return new NodeBasedExecution(graphDb, executionRelationship);
  }
  
  public void delete() {
    // Executions
    for (Execution execution : getExecutions()) {
      execution.delete();
    }
    
    // Variables
    Vertex variableNode = getVariableNode();
    if (variableNode != null) {
      variableNode.remove();
    }
    
    // Delete relationship from process definition to process instance
    for (Edge relationship : processInstanceNode.getEdges(Direction.BOTH, RelTypes.PROCESS_INSTANCE.toString())) {
      relationship.remove();
    }
    
    processInstanceNode.remove();
  }
  
  public List<Execution> getExecutions() {
    List<Execution> executions = new ArrayList<Execution>();
    for (Edge executionRelationship : processInstanceNode.getEdges(Direction.OUT, RelTypes.EXECUTION.toString())) {
      executions.add(new NodeBasedExecution(graphDb, executionRelationship));
    }
    return executions;
  }
  
}
