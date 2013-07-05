package org.activiti.neo4j;


public interface Constants {

  // Indexes
  String PROCESS_DEFINITION_INDEX = "process-definitions-index";
  String TASK_INDEX = "task-index";
  
  // Index key
  String INDEX_KEY_PROCESS_DEFINITION_KEY = "processDefinitionKey";
  
  String INDEX_KEY_TASK_ASSIGNEE = "taskAssignee";
  
  // Behavior types
  String TYPE_START_EVENT = "startEvent";
  String TYPE_END_EVENT = "endEvent";
  String TYPE_USER_TASK = "userTask";
  String TYPE_PARALLEL_GATEWAY = "parallelGateway";
  String TYPE_EXCLUSIVE_GATEWAY = "exclusiveGateway";
  String TYPE_SERVICE_TASK = "serviceTask";
  
}
