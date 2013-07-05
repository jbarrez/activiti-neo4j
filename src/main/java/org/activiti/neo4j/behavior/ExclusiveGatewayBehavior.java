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
package org.activiti.neo4j.behavior;

import org.activiti.neo4j.Execution;
import org.activiti.neo4j.InternalActivitiEngine;
import org.activiti.neo4j.RelTypes;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;


/**
 * @author Joram Barrez
 */
public class ExclusiveGatewayBehavior extends AbstractBehavior {
  
  public void execute(Execution execution, InternalActivitiEngine internalActivitiEngine) {
    
    // Evaluate every outgoing sequence flow. The first that evaluates to true is selected.
    // Otherwise the default flow is followed.
    
    Node currentActivityNode = execution.getEndNode();
    String defaultFlowId = (String) currentActivityNode.getProperty("defaultFlow");
    
    Node nextNode = null;
    Relationship defaultFlow = null;
    for (Relationship sequenceFlow : currentActivityNode.getRelationships(Direction.OUTGOING, RelTypes.SEQ_FLOW)) {
      
      // Get condition, if true -> evaluate
      String conditionExpression =  null;
      
      if (sequenceFlow.hasProperty("condition")) {
        conditionExpression = (String) sequenceFlow.getProperty("condition");
      }
      
      if (sequenceFlow.getProperty("id").equals(defaultFlowId)) {
        defaultFlow = sequenceFlow;
      } else if (conditionExpression != null) {
        
        // TODO: implement.... for the moment always true
        nextNode = sequenceFlow.getEndNode();
      }
      
    }
    
    if (nextNode != null) {
      gotoNode(nextNode, execution, internalActivitiEngine);
    } else if (defaultFlow != null) {
      gotoNode(defaultFlow.getEndNode(), execution, internalActivitiEngine);
    } else {
      throw new RuntimeException("Could not find a sequenceflow with true condition nor default flow");
    }
  }

}
