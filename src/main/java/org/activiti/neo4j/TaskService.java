package org.activiti.neo4j;

import java.util.List;

import org.activiti.neo4j.manager.TaskManager;

public class TaskService {

  // TODO: can be put in a command service super class
  protected CommandExecutor commandExecutor;
  protected TaskManager taskManager;

  public TaskService(CommandExecutor commandExecutor) {
    this.commandExecutor = commandExecutor;
  }

  public List<Task> findTasksFor(final String assignee) {
    return commandExecutor.execute(new Command<List<Task>>() {
      
      public void execute(CommandContext<List<Task>> commandContext) {
        commandContext.setResult(taskManager.getTasksByAssignee(assignee));
      }
      
    });
  }

  public void complete(final long taskId) {
    commandExecutor.execute(new Command<Void>() {
      
      public void execute(CommandContext<Void> commandContext) {
        commandContext.signal(commandContext.getExecutionManager().getExecutionById(taskId));
      }
      
    });
  }

  public TaskManager getTaskManager() {
    return taskManager;
  }
  
  public void setTaskManager(TaskManager taskManager) {
    this.taskManager = taskManager;
  }

}
