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
package org.activiti.neo4j.manager;

import java.util.ArrayList;
import java.util.List;

import org.activiti.neo4j.Constants;
import org.activiti.neo4j.Task;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;


/**
 * @author Joram Barrez
 */
public class NodeBasedTaskManager implements TaskManager {
  
  protected GraphDatabaseService graphDb;
  
  public List<Task> getTasksByAssignee(String assignee) {
    List<Task> tasks = new ArrayList<Task>();
    Index<Relationship> taskIndex = graphDb.index().forRelationships(Constants.TASK_INDEX);
    for (Relationship execution : taskIndex.get(Constants.INDEX_KEY_TASK_ASSIGNEE, assignee)) {
      Task task = new Task();
      task.setId(execution.getId());
      task.setName((String) execution.getProperty("name"));
      tasks.add(task);
    }
    return tasks;
  }

  public GraphDatabaseService getGraphDb() {
    return graphDb;
  }

  public void setGraphDb(GraphDatabaseService graphDb) {
    this.graphDb = graphDb;
  }
  
}
