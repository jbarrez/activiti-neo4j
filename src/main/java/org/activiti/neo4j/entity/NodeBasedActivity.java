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

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

/**
 * @author Joram Barrez
 */
public class NodeBasedActivity implements Activity {
  
  protected Vertex activityNode;
  protected List<SequenceFlow> incomingSequenceFlows;
  protected List<SequenceFlow> outgoingSequenceFlows;
  
  public NodeBasedActivity(Vertex activityNode) {
    this.activityNode = activityNode;
  }
  
  public List<SequenceFlow> getOutgoingSequenceFlow() {
    if (outgoingSequenceFlows == null) {
      outgoingSequenceFlows = new ArrayList<SequenceFlow>();
      for (Edge sequenceFlowRelationship : activityNode.getEdges(Direction.OUT, RelTypes.SEQ_FLOW.toString())) {
        outgoingSequenceFlows.add(new NodeBasedSequenceFlow(sequenceFlowRelationship));
      }
    }
    return outgoingSequenceFlows;
  }
  
  public List<SequenceFlow> getIncomingSequenceFlow() {
    if (incomingSequenceFlows == null) {
      incomingSequenceFlows = new ArrayList<SequenceFlow>();
      for (Edge sequenceFlowRelationship : activityNode.getEdges(Direction.IN, RelTypes.SEQ_FLOW.toString())) {
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
    return (activityNode.getProperty(property) != null);
  }
  
  public void setProperty(String property, Object value) {
    activityNode.setProperty(property, value);
  }
  
  public Object removeProperty(String property) {
    return activityNode.removeProperty(property);
  }
  
  public Vertex getActivityNode() {
    return activityNode;
  }

  public void setActivityNode(Vertex activityNode) {
    this.activityNode = activityNode;
  }

}
