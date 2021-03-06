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

import org.activiti.neo4j.Activity;
import org.activiti.neo4j.SequenceFlow;
import org.neo4j.graphdb.Relationship;


/**
 * @author Joram Barrez
 */
public class NodeBasedSequenceFlow implements SequenceFlow {
  
  protected Relationship sequenceFlowRelationship;
  
  protected Activity sourceActivity;
  protected Activity targetActivity;
  
  public NodeBasedSequenceFlow(Relationship sequenceFlowRelationship) {
    this.sequenceFlowRelationship = sequenceFlowRelationship;
  }
  
  public Activity getSourceActivity() {
    if (sourceActivity == null) {
      sourceActivity = new NodeBasedActivity(sequenceFlowRelationship.getStartNode());
    }
    return sourceActivity;
  }
  
  public Activity getTargetActivity() {
    if (targetActivity == null) {
      targetActivity = new NodeBasedActivity(sequenceFlowRelationship.getEndNode());
    }
    return targetActivity;
  }

  public boolean hasProperty(String property) {
    return sequenceFlowRelationship.hasProperty(property);
  }

  public Object getProperty(String property) {
    return sequenceFlowRelationship.getProperty(property);
  }

  public void setProperty(String property, Object value) {
    sequenceFlowRelationship.setProperty(property, value);
  }
  
  public Object removeProperty(String property) {
    return sequenceFlowRelationship.removeProperty(property);
  }

}
