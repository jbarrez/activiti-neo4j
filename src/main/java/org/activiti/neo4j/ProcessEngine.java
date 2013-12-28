package org.activiti.neo4j;

import com.tinkerpop.blueprints.Graph;


public class ProcessEngine {
  
  protected Graph graph;

  protected RepositoryService repositoryService;
  protected RuntimeService runtimeService;
  protected TaskService taskService;
  
  protected CommandExecutor commandExecutor;
  
  public ProcessEngine() {
    
  }
  
  public Graph getGraph() {
    return graph;
  }
  
  public void setGraph(Graph graph) {
    this.graph = graph;
  }

  public RepositoryService getRepositoryService() {
    return repositoryService;
  }
  
  public void setRepositoryService(RepositoryService repositoryService) {
    this.repositoryService = repositoryService;
  }

  public RuntimeService getRuntimeService() {
    return runtimeService;
  }

  public void setRuntimeService(RuntimeService runtimeService) {
    this.runtimeService = runtimeService;
  }
  
  public TaskService getTaskService() {
    return taskService;
  }
  
  public void setTaskService(TaskService taskService) {
    this.taskService = taskService;
  }
  
  public CommandExecutor getCommandExecutor() {
    return commandExecutor;
  }
  
  public void setCommandExecutor(CommandExecutor commandExecutor) {
    this.commandExecutor = commandExecutor;
  }

}
