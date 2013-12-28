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

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;


/**
 * @author Joram Barrez
 */
public class NodeBasedTaskManager implements TaskManager {
  
  protected Graph graphDb;
  
  public List<Task> getTasksByAssignee(String assignee) {
    List<Task> tasks = new ArrayList<Task>();
    
    for (Edge execution : graphDb.query().has(Constants.INDEX_KEY_TASK_ASSIGNEE, assignee).edges()) {
      Task task = new Task();
      task.setId(execution.getId().toString());
      task.setName((String) execution.getProperty("name"));
      tasks.add(task);
    }
    return tasks;
  }

  public Graph getGraphDb() {
    return graphDb;
  }

  public void setGraphDb(Graph graphDb) {
    this.graphDb = graphDb;
  }
  
}
