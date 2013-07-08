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
package org.activiti.neo4j.behavior;

import org.activiti.neo4j.EngineOperations;
import org.activiti.neo4j.Execution;
import org.activiti.neo4j.JavaDelegate;
import org.neo4j.graphdb.Node;



/**
 * @author Joram Barrez
 */
public class ServiceTaskBehaviour extends AbstractBehavior {
  
  public void execute(Execution execution, EngineOperations engineOperations) {
    executeDelegate(execution);
    leave(execution, engineOperations);
  }

  protected void executeDelegate(Execution execution) {
    Node activityNode = execution.getEndNode();
    String className = (String) activityNode.getProperty("class");
    
    try {
      Class<?> clazz = Class.forName(className);
      
      if (!JavaDelegate.class.isAssignableFrom(clazz)) {
        throw new RuntimeException("The referenced class " + clazz.getCanonicalName() +  "does not implement a correct delegation class");
      }
     JavaDelegate javaDelegate = (JavaDelegate) clazz.newInstance();
     javaDelegate.execute(execution);
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
