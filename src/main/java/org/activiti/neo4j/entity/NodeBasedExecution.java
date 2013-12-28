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

import java.util.Iterator;

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
public class NodeBasedExecution implements Execution {

  // An execution is actually a thin wrapper around a Neo4j relationship
  // adding al sorts of convenience methods that hide the internal bits
  
  protected Graph graphDb;
  
  protected Edge relationship;
  
  protected NodeBasedProcessInstance processInstance;
  protected NodeBasedActivity activity;

  public NodeBasedExecution(Graph graphDb, Edge relationship) {
    this.graphDb = graphDb;
    this.relationship = relationship;
  }

  public ProcessInstance getProcessInstance() {
    if (processInstance == null) {
      processInstance = new NodeBasedProcessInstance(graphDb, getProcessInstanceNode());
    }
    return processInstance;
  }
  
  protected Vertex getProcessInstanceNode() {
     return relationship.getVertex(Direction.OUT);
  }
  
  public Activity getActivity() {
    if (activity == null) {
      activity = new NodeBasedActivity(relationship.getVertex(Direction.IN));
    }
    return activity;
  }

  public void setVariable(String variableName, Object variableValue) {

    // TODO: need to have variable local, which is a bit trickier,
    // since executions are relationships.
    // Perhaps this needs to be revised
    
    Vertex processInstanceNode = getProcessInstanceNode();

    // Check if the variable node already exists
    Iterator<Edge> variableRelationShipIterator = 
            processInstanceNode.getEdges(Direction.OUT, RelTypes.VARIABLE.toString()).iterator();
    
    Vertex variableNode = null;
    if (variableRelationShipIterator.hasNext()) {
      Edge variableRelationship = variableRelationShipIterator.next();
      variableNode = variableRelationship.getVertex(Direction.IN);
    } else {
      variableNode = graphDb.addVertex(null);
      processInstanceNode.addEdge(RelTypes.VARIABLE.toString(), variableNode);
    }
    
    variableNode.setProperty(variableName, variableValue);
  }

  public Object getVariable(String variableName) {
    
    Vertex processInstanceNode = getProcessInstanceNode();
    Iterator<Edge> variableRelationshipIterator = processInstanceNode.getEdges(Direction.OUT, RelTypes.VARIABLE.toString()).iterator();
    
    if (variableRelationshipIterator.hasNext()) {
      Vertex variableNode = variableRelationshipIterator.next().getVertex(Direction.IN);
      return variableNode.getProperty(variableName);
    } else {
      // No variable associated with this process instance
      return null;
    }
  }
  
  public Object getProperty(String property) {
    return relationship.getProperty(property);
  }
  
  public boolean hasProperty(String property) {
    return (relationship.getProperty(property) != null);
  }
  
  public void setProperty(String property, Object value) {
    relationship.setProperty(property, value);
  }
  
  public Vertex getStartNode() {
    return relationship.getVertex(Direction.OUT);
  }
  
  public Vertex getEndNode() {
    return relationship.getVertex(Direction.IN);
  }
  
  public void delete() {
    // Delete actual execution relationship
    relationship.remove();
  }
  
  public Object removeProperty(String property) {
    return relationship.removeProperty(property);
  }
  
  public Edge getRelationship() {
    return relationship;
  }

}
