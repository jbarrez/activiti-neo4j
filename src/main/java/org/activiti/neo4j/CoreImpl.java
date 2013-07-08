package org.activiti.neo4j;
import org.activiti.neo4j.behavior.Behavior;
import org.activiti.neo4j.behavior.BehaviorMapping;


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

/**
 * @author Joram Barrez
 */
public class CoreImpl implements Core {
  
  protected BehaviorMapping behaviorMapping;
  
  public void continueProcess(final CommandContext<?> commandContext, final Execution execution) {
    commandContext.getAgenda().add(new Runnable() {
      
      public void run() {
//      System.out.println("Next node is " + execution.getEndNode().getProperty("id"));
        String type = (String) execution.getEndNode().getProperty("type");
        Behavior behavior = behaviorMapping.getBehaviorForType(type);
        behavior.execute(execution, commandContext);
      }
    });
  }
  
  public void signal(final CommandContext<?> commandContext, final Execution execution) {
    commandContext.getAgenda().add(new Runnable() {
      
      public void run() {
        String type = (String) execution.getEndNode().getProperty("type");
        Behavior behavior = behaviorMapping.getBehaviorForType(type);
        behavior.signal(execution, commandContext);
      }
    });
  }

  public BehaviorMapping getBehaviorMapping() {
    return behaviorMapping;
  }

  public void setBehaviorMapping(BehaviorMapping behaviorMapping) {
    this.behaviorMapping = behaviorMapping;
  }
  
}
