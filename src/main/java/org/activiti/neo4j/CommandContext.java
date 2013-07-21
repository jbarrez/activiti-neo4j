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

import java.util.LinkedList;

import org.activiti.neo4j.manager.ExecutionManager;


/**
 * @author Joram Barrez
 */
public class CommandContext<T> implements EngineOperations {
  
  protected Core core;
  protected ExecutionManager executionManager;
  
  protected LinkedList<Runnable> agenda = new LinkedList<Runnable>();
  protected T result = null;
  
  public void continueProcess(Execution execution) {
    core.continueProcess(this, execution);
  }
  
  public void signal(Execution execution) {
    core.signal(this, execution);
  }
  
  /* package */ CommandContext() {
    this.agenda = new LinkedList<Runnable>();
  }

  public LinkedList<Runnable> getAgenda() {
    return agenda;
  }

  public void setAgenda(LinkedList<Runnable> agenda) {
    this.agenda = agenda;
  }
  
  public T getResult() {
    return result;
  }

  public void setResult(T result) {
    this.result = result;
  }

  public Core getCore() {
    return core;
  }

  public void setCore(Core core) {
    this.core = core;
  }

  public ExecutionManager getExecutionManager() {
    return executionManager;
  }

  public void setExecutionManager(ExecutionManager executionManager) {
    this.executionManager = executionManager;
  }
  
}
