import java.util.Random;

import org.activiti.neo4j.Constants;
import org.activiti.neo4j.RelTypes;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

/**
 * @author Mike Dias
 */
public class ActivitiOrientTest extends AbstractActivitiGraphTest {

  @Override
  public void setupGraphDb() {
    OrientGraph orientGraph = new OrientGraph("memory:activiti_"+new Random().nextInt()); //random to avoid reuse database
    
    for (RelTypes relType : RelTypes.values()) {
      orientGraph.createEdgeType(relType.toString());
    }
    
    orientGraph.createKeyIndex(Constants.INDEX_KEY_PROCESS_DEFINITION_KEY, Vertex.class);
    orientGraph.createKeyIndex(Constants.INDEX_KEY_TASK_ASSIGNEE, Edge.class);
    
    graphDb = orientGraph;
  }

}
