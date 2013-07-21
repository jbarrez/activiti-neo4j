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
import java.util.List;

import org.activiti.neo4j.Activity;
import org.activiti.neo4j.RelTypes;
import org.activiti.neo4j.SequenceFlow;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;


/**
 * @author Joram Barrez
 */
public class NodeBasedActivity implements Activity {
  
  protected Node activityNode;
  protected List<SequenceFlow> incomingSequenceFlows;
  protected List<SequenceFlow> outgoingSequenceFlows;
  
  public NodeBasedActivity(Node activityNode) {
    this.activityNode = activityNode;
  }
  
  public List<SequenceFlow> getOutgoingSequenceFlow() {
    if (outgoingSequenceFlows == null) {
      outgoingSequenceFlows = new ArrayList<SequenceFlow>();
      for (Relationship sequenceFlowRelationship : activityNode.getRelationships(Direction.OUTGOING, RelTypes.SEQ_FLOW)) {
        outgoingSequenceFlows.add(new NodeBasedSequenceFlow(sequenceFlowRelationship));
      }
    }
    return outgoingSequenceFlows;
  }
  
  public List<SequenceFlow> getIncomingSequenceFlow() {
    if (incomingSequenceFlows == null) {
      incomingSequenceFlows = new ArrayList<SequenceFlow>();
      for (Relationship sequenceFlowRelationship : activityNode.getRelationships(Direction.INCOMING, RelTypes.SEQ_FLOW)) {
        incomingSequenceFlows.add(new NodeBasedSequenceFlow(sequenceFlowRelationship));
      }
    }
    return incomingSequenceFlows;
  }
  
  public String getId() {
    return (String) activityNode.getProperty("id");
  }
  
  public Object getProperty(String property) {
    return activityNode.getProperty(property);
  }
  
  public boolean hasProperty(String property) {
    return activityNode.hasProperty(property);
  }
  
  public void setProperty(String property, Object value) {
    activityNode.setProperty(property, value);
  }
  
  public Object removeProperty(String property) {
    return activityNode.removeProperty(property);
  }
  
  public Node getActivityNode() {
    return activityNode;
  }

  public void setActivityNode(Node activityNode) {
    this.activityNode = activityNode;
  }

}
