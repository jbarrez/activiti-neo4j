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

import org.activiti.neo4j.manager.ExecutionManager;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.TransactionalGraph;


/**
 * @author Joram Barrez
 * 
 */
public class CommandExecutor {
  
  protected Graph graph;
  protected Core core;
  protected ExecutionManager executionManager;
  
  public CommandExecutor(Graph graph) {
    this.graph = graph;
  }
  
  public <T> T execute(final Command<T> command) {
    
    // TODO: create interceptor stack analogue to the Activiti interceptor stack
    // to separate transaction interceptor from command execution interceptor
    
    final CommandContext<T> commandContext = initialiseCommandContext(command);
    
    TransactionalGraph txGraph = (TransactionalGraph)graph;
    try {
      
      while (!commandContext.getAgenda().isEmpty()) {
        Runnable runnable = commandContext.getAgenda().poll();
        runnable.run();
      }
      
      txGraph.commit();
      
    } catch (Exception e) {
      e.printStackTrace();
      txGraph.rollback(); 
    }
    
    return commandContext.getResult();
    
  }

  protected <T> CommandContext<T> initialiseCommandContext(final Command<T> command) {
    final CommandContext<T> commandContext = new CommandContext<T>();
    commandContext.setCore(core);
    commandContext.setExecutionManager(executionManager);
    
    commandContext.getAgenda().add(new Runnable() {
      
      public void run() {
        command.execute(commandContext);
      }
      
    });
    return commandContext;
  }

  
  public Graph getGraph() {
    return graph;
  }

  
  public void setGraphDatabaseService(Graph graph) {
    this.graph = graph;
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
