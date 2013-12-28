import org.neo4j.test.TestGraphDatabaseFactory;

import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;

/**
 * @author Mike Dias
 */
public class ActivitiNeo4jTest extends AbstractActivitiGraphTest {
  
  @Override
  public void setupGraphDb() {
    Neo4jGraph neo4jGraph = new Neo4jGraph(new TestGraphDatabaseFactory().newImpermanentDatabase());
    
    // Neo4J runs much faster without indexes... I don't know why...
    
//    neo4jGraph.createKeyIndex(Constants.INDEX_KEY_PROCESS_DEFINITION_KEY, Vertex.class);
//    neo4jGraph.createKeyIndex(Constants.INDEX_KEY_TASK_ASSIGNEE, Edge.class);
    
    graphDb = neo4jGraph;
  }

}
