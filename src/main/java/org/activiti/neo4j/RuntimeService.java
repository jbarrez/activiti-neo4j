package org.activiti.neo4j;

import org.activiti.neo4j.entity.NodeBasedExecution;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class RuntimeService {

  protected Graph graphDb;
  protected CommandExecutor commandExecutor;

  public RuntimeService(Graph graphDb, CommandExecutor commandExecutor) {
    this.graphDb = graphDb;
    this.commandExecutor = commandExecutor;
  }

  public void startProcessInstanceByKey(final String key) {
    commandExecutor.execute(new Command<Void>() {
      
      public void execute(CommandContext<Void> commandContext) {
        // Find process definition node
        
        // TODO: encapsulate in a manager!
        Vertex processDefinitionNode = graphDb.getVertices(Constants.INDEX_KEY_PROCESS_DEFINITION_KEY, key).iterator().next();
        Vertex startEventNode = processDefinitionNode.getEdges(Direction.OUT, RelTypes.IS_STARTED_FROM.toString()).iterator().next().getVertex(Direction.IN);

        // Create process instance node and link it to the process definition
        Vertex processInstanceNode = graphDb.addVertex(null);
        processDefinitionNode.addEdge(RelTypes.PROCESS_INSTANCE.toString(), processInstanceNode);
                
//        // Traverse the process definition
//        TraversalDescription traversalDescription = Traversal.description()
//                .breadthFirst()
//                .relationships( RelTypes.SEQ_FLOW, Direction.OUTGOING )
//                .evaluator(Evaluators.all());
//        Traverser traverser = traversalDescription.traverse(startEventNode);
        
        // Add one execution link to the startnode
        Edge relationShipExecution = processInstanceNode.addEdge(RelTypes.EXECUTION.toString(), startEventNode);
        
        // Execute the process
        Execution execution = new NodeBasedExecution(graphDb, relationShipExecution);
        commandContext.continueProcess(execution);
      }
      
    });
    
  }
}
