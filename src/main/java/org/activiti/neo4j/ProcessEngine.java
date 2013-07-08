package org.activiti.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;


public class ProcessEngine {
  
  protected GraphDatabaseService graphDatabaseService;

  protected RepositoryService repositoryService;
  protected RuntimeService runtimeService;
  protected TaskService taskService;
  
  protected CommandExecutor commandExecutor;
  
  public ProcessEngine() {
    
  }
  
  public GraphDatabaseService getGraphDatabaseService() {
    return graphDatabaseService;
  }
  
  public void setGraphDatabaseService(GraphDatabaseService graphDatabaseService) {
    this.graphDatabaseService = graphDatabaseService;
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
