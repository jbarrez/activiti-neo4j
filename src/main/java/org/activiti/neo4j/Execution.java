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
package org.activiti.neo4j;

import java.util.Iterator;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

/**
 * @author Joram Barrez
 */
public class Execution {

  // An execution is actually a thin wrapper around a Neo4j relationship
  // adding al sorts of convenience methods that hide the internal bits
  protected Relationship relationship;

  public Execution(Relationship relationship) {
    this.relationship = relationship;
  }

  public Node getProcessInstance() {
    return relationship.getStartNode();
  }

  public void setVariable(String variableName, Object variableValue) {

    // TODO: need to have variable local, which is a bit trickier,
    // since executions are relationships.
    // Perhaps this needs to be revised
    
    Node processInstanceNode = getProcessInstance();

    // Check if the variable node already exists
    Iterator<Relationship> variableRelationShipIterator = 
            processInstanceNode.getRelationships(Direction.OUTGOING, RelTypes.VARIABLE).iterator();
    
    Node variableNode = null;
    if (variableRelationShipIterator.hasNext()) {
      Relationship variableRelationship = variableRelationShipIterator.next();
      variableNode = variableRelationship.getEndNode();
    } else {
      variableNode = processInstanceNode.getGraphDatabase().createNode();
      processInstanceNode.createRelationshipTo(variableNode, RelTypes.VARIABLE);
    }
    
    variableNode.setProperty(variableName, variableValue);
  }

  public Object getVariable(String variableName) {
    
    Node processInstanceNode = getProcessInstance();
    Iterator<Relationship> variableRelationshipIterator = processInstanceNode.getRelationships(RelTypes.VARIABLE).iterator();
    
    if (variableRelationshipIterator.hasNext()) {
      Node variableNode = variableRelationshipIterator.next().getEndNode();
      return variableNode.getProperty(variableName);
    } else {
      // No variable associated with this process instance
      return null;
    }
  }
  
  public Node getStartNode() {
    return relationship.getStartNode();
  }
  
  public Node getEndNode() {
    return relationship.getEndNode();
  }
  
  public void delete() {
    relationship.delete();
  }
  
  public void setProperty(String property, Object value) {
    relationship.setProperty(property, value);
  }
  
  public Object removeProperty(String property) {
    return relationship.removeProperty(property);
  }
  
  public Relationship getRelationship() {
    return relationship;
  }

}
