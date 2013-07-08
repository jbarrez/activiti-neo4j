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

import java.util.HashMap;

import org.activiti.neo4j.Constants;


/**
 * @author Joram Barrez
 */
public class BehaviorMappingImpl implements BehaviorMapping {
  
  protected static final HashMap<String, Behavior> behaviorMapping;
  
  static {
    behaviorMapping = new HashMap<String, Behavior>();
    
    behaviorMapping.put(Constants.TYPE_START_EVENT, new NoneStartEventBehavior());
    behaviorMapping.put(Constants.TYPE_USER_TASK, new UserTaskBehavior());
    behaviorMapping.put(Constants.TYPE_END_EVENT, new NoneEndEventBehavior());
    behaviorMapping.put(Constants.TYPE_PARALLEL_GATEWAY, new ParallelGatewayBehavior());
    behaviorMapping.put(Constants.TYPE_SERVICE_TASK, new ServiceTaskBehaviour());
    behaviorMapping.put(Constants.TYPE_EXCLUSIVE_GATEWAY, new ExclusiveGatewayBehavior());
  }
  
  public Behavior getBehaviorForType(String type) {
    return behaviorMapping.get(type);
  }
  
  public void setBehavior(String type, Behavior behavior) {
    behaviorMapping.put(type, behavior);
  }

}
