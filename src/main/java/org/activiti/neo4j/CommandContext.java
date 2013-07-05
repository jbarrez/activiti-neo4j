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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.activiti.neo4j.behavior.Behavior;
import org.activiti.neo4j.behavior.ExclusiveGatewayBehavior;
import org.activiti.neo4j.behavior.NoneEndEventBehavior;
import org.activiti.neo4j.behavior.NoneStartEventBehavior;
import org.activiti.neo4j.behavior.ParallelGatewayBehavior;
import org.activiti.neo4j.behavior.ServiceTaskBehaviour;
import org.activiti.neo4j.behavior.UserTaskBehavior;


/**
 * @author Joram Barrez
 */
public class CommandContext<T> implements InternalActivitiEngine {
  
  // TODO: needs to be extracted and made pluggable (eg from the process engine probably)
  private static Map<String, Behavior> behaviorMapping;
  
  static {
    behaviorMapping = new HashMap<String, Behavior>();
    
    behaviorMapping.put(Constants.TYPE_START_EVENT, new NoneStartEventBehavior());
    behaviorMapping.put(Constants.TYPE_USER_TASK, new UserTaskBehavior());
    behaviorMapping.put(Constants.TYPE_END_EVENT, new NoneEndEventBehavior());
    behaviorMapping.put(Constants.TYPE_PARALLEL_GATEWAY, new ParallelGatewayBehavior());
    behaviorMapping.put(Constants.TYPE_SERVICE_TASK, new ServiceTaskBehaviour());
    behaviorMapping.put(Constants.TYPE_EXCLUSIVE_GATEWAY, new ExclusiveGatewayBehavior());
  }

  protected LinkedList<Runnable> agenda = new LinkedList<Runnable>();
  protected InternalActivitiEngine internalActivitiEngine;
  
  protected T result = null;
  
  /* package */ CommandContext() {
    this.agenda = new LinkedList<Runnable>();
  }

  public LinkedList<Runnable> getAgenda() {
    return agenda;
  }

  public void setAgenda(LinkedList<Runnable> agenda) {
    this.agenda = agenda;
  }
  
  public void continueProcess(final Execution execution) {
    agenda.add(new Runnable() {
      
      public void run() {
//      System.out.println("Next node is " + execution.getEndNode().getProperty("id"));
        String type = (String) execution.getEndNode().getProperty("type");
        Behavior behavior = behaviorMapping.get(type);
        behavior.execute(execution, CommandContext.this);
      }
    });
  }
  
  public void signal(final Execution execution) {
    agenda.add(new Runnable() {
      
      public void run() {
        String type = (String) execution.getEndNode().getProperty("type");
        Behavior behavior = behaviorMapping.get(type);
        behavior.signal(execution, CommandContext.this);
      }
    });
  }

  
  public T getResult() {
    return result;
  }

  public void setResult(T result) {
    this.result = result;
  }
  
  
}
