import java.util.Random;

import org.activiti.neo4j.Constants;
import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;

import com.thinkaurelius.titan.core.TitanFactory;
import com.thinkaurelius.titan.core.TitanGraph;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

/**
 * @author Mike Dias
 */
public class ActivitiTitanTest extends AbstractActivitiGraphTest {

  @Override
  public void setupGraphDb() {
    
    Configuration conf = new BaseConfiguration();
    conf.setProperty("storage.backend", "inmemory"); 
    
    TitanGraph titanGraph = TitanFactory.open(conf);
    
    titanGraph.createKeyIndex(Constants.INDEX_KEY_PROCESS_DEFINITION_KEY, Vertex.class);
    titanGraph.createKeyIndex(Constants.INDEX_KEY_TASK_ASSIGNEE, Edge.class);
    
    graphDb = titanGraph;
  }

}
