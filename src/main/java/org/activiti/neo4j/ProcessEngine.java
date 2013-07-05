package org.activiti.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;


public class ProcessEngine {
  
  protected GraphDatabaseService graphDb;

  protected RepositoryService repositoryService;
  protected RuntimeService runtimeService;
  protected TaskService taskService;
  
  protected CommandExecutor commandExecutor;
  
  public ProcessEngine(GraphDatabaseService graphDatabaseService) {
    this.graphDb = graphDatabaseService;
    this.commandExecutor = new CommandExecutor(graphDatabaseService);
    
    this.repositoryService = new RepositoryService(graphDatabaseService, commandExecutor);
    this.runtimeService = new RuntimeService(graphDatabaseService, commandExecutor);
    this.taskService = new TaskService(graphDatabaseService, commandExecutor);
  }

  public RepositoryService getRepositoryService() {
    return repositoryService;
  }

  public void setRepositoryService(RepositoryService repositoryService) {
    this.repositoryService = repositoryService;
  }
  
  public GraphDatabaseService getGraphDb() {
    return graphDb;
  }
  
  public void setGraphDb(GraphDatabaseService graphDb) {
    this.graphDb = graphDb;
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
  
}
