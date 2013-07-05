package org.activiti.neo4j;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;

public class TaskService {

  // TODO: can be put in a command service super class
  protected GraphDatabaseService graphDb;
  protected CommandExecutor commandExecutor;

  public TaskService(GraphDatabaseService graphDb, CommandExecutor commandExecutor) {
    this.graphDb = graphDb;
    this.commandExecutor = commandExecutor;
  }

  public List<Task> findTasksFor(final String assignee) {
    return commandExecutor.execute(new Command<List<Task>>() {
      
      public void execute(CommandContext<List<Task>> commandContext) {
        List<Task> tasks = new ArrayList<Task>();
        commandContext.setResult(tasks);
        
        Index<Relationship> taskIndex = graphDb.index().forRelationships(Constants.TASK_INDEX);
        for (Relationship execution : taskIndex.get(Constants.INDEX_KEY_TASK_ASSIGNEE, assignee)) {
          Task task = new Task();
          task.setId(execution.getId());
          task.setName((String) execution.getProperty("name"));
          tasks.add(task);
        }
      }
      
    });
  }

  public void complete(final long taskId) {
    commandExecutor.execute(new Command<Void>() {
      
      public void execute(CommandContext<Void> commandContext) {
        Relationship executionRelationShip = graphDb.getRelationshipById(taskId);
        commandContext.signal(new Execution(executionRelationShip));
      }
      
    });
  }

}
