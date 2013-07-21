package org.activiti.neo4j;

import org.activiti.neo4j.entity.NodeBasedExecution;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;

public class RuntimeService {

  protected GraphDatabaseService graphDb;
  protected CommandExecutor commandExecutor;

  public RuntimeService(GraphDatabaseService graphDb, CommandExecutor commandExecutor) {
    this.graphDb = graphDb;
    this.commandExecutor = commandExecutor;
  }

  public void startProcessInstanceByKey(final String key) {
    commandExecutor.execute(new Command<Void>() {
      
      public void execute(CommandContext<Void> commandContext) {
        // Find process definition node
        
        // TODO: encapsulate in a manager!
        Index<Node> processDefinitionIndex = graphDb.index().forNodes(Constants.PROCESS_DEFINITION_INDEX);
        Node processDefinitionNode = processDefinitionIndex.get(Constants.INDEX_KEY_PROCESS_DEFINITION_KEY, key).getSingle();
        Node startEventNode = processDefinitionNode.getRelationships(Direction.OUTGOING, RelTypes.IS_STARTED_FROM).iterator().next().getEndNode();

        // Create process instance node and link it to the process definition
        Node processInstanceNode = graphDb.createNode();
        processDefinitionNode.createRelationshipTo(processInstanceNode, RelTypes.PROCESS_INSTANCE);
                
//        // Traverse the process definition
//        TraversalDescription traversalDescription = Traversal.description()
//                .breadthFirst()
//                .relationships( RelTypes.SEQ_FLOW, Direction.OUTGOING )
//                .evaluator(Evaluators.all());
//        Traverser traverser = traversalDescription.traverse(startEventNode);
        
        // Add one execution link to the startnode
        Relationship relationShipExecution = processInstanceNode.createRelationshipTo(startEventNode, RelTypes.EXECUTION);
        
        // Execute the process
        Execution execution = new NodeBasedExecution(relationShipExecution);
        commandContext.continueProcess(execution);
      }
      
    });
    
  }
}
