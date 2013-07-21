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

import java.util.Iterator;

import org.activiti.neo4j.Activity;
import org.activiti.neo4j.EngineOperations;
import org.activiti.neo4j.Execution;
import org.activiti.neo4j.SequenceFlow;


/**
 * @author Joram Barrez
 */
public class ExclusiveGatewayBehavior extends AbstractBehavior {
  
  public void execute(Execution execution, EngineOperations engineOperations) {
    
    // Evaluate every outgoing sequence flow. The first that evaluates to true is selected.
    // Otherwise the default flow is followed.
    
    Activity currentActivity = execution.getActivity();
    String defaultFlowId = (String) currentActivity.getProperty("defaultFlow");
    
    Activity nextActivity = null;
    SequenceFlow defaultSequenceFlow = null;
    
    boolean found = false;
    Iterator<SequenceFlow> sequenceFlowIterator = currentActivity.getOutgoingSequenceFlow().iterator();
    while (!found && sequenceFlowIterator.hasNext()) {
      
      SequenceFlow sequenceFlow = sequenceFlowIterator.next();
      
      // Get condition, if true -> evaluate
      String conditionExpression =  null;
      
      if (sequenceFlow.hasProperty("condition")) {
        conditionExpression = (String) sequenceFlow.getProperty("condition");
      }
      
      if (sequenceFlow.getProperty("id").equals(defaultFlowId)) {
        defaultSequenceFlow = sequenceFlow;
      } else if (conditionExpression != null) {
        
        // TODO: implement expressions! .... for the moment always true
        nextActivity = sequenceFlow.getTargetActivity();
      }
      
    }
    
    if (nextActivity != null) {
      goToNextActivity(nextActivity, execution, engineOperations);
    } else if (defaultSequenceFlow != null) {
      goToNextActivity(defaultSequenceFlow.getTargetActivity(), execution, engineOperations);
    } else {
      throw new RuntimeException("Could not find a sequenceflow with true condition nor default flow");
    }
  }

}
