package org.activiti.neo4j;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.ParallelGateway;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.ServiceTask;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.neo4j.helper.BpmnModelUtil;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;

public class RepositoryService {

  protected GraphDatabaseService graphDb;
  protected CommandExecutor commandExecutor;

  protected Map<String, Node> nodeMap;
  protected Set<SequenceFlow> sequenceFlows;

  public RepositoryService(GraphDatabaseService graphDb, CommandExecutor commandExecutor) {
    this.graphDb = graphDb;
    this.commandExecutor = commandExecutor;
  }

  public ProcessDefinition deploy(final InputStream inputStream) {

    return commandExecutor.execute(new Command<ProcessDefinition>() {

      public void execute(CommandContext<ProcessDefinition> commandContext) {
        Process process = null;

        
        // TODO: extract in parser
        try {
          XMLInputFactory xif = XMLInputFactory.newInstance();
          InputStreamReader in = new InputStreamReader(inputStream, "UTF-8");
          XMLStreamReader xtr = xif.createXMLStreamReader(in);
          BpmnXMLConverter converter = new BpmnXMLConverter();
          BpmnModel bpmnModel = converter.convertToBpmnModel(xtr);
          process = bpmnModel.getProcesses().get(0);
        } catch (Exception e) {
          e.printStackTrace();
        }

        // TODO: move the below stuff to a parser / behaviour / BPMNParseHandler thingy

        // Create Node representation
        ProcessDefinition processDefinition = null;

        nodeMap = new HashMap<String, Node>();
        sequenceFlows = new HashSet<SequenceFlow>();
        for (FlowElement flowElement : process.getFlowElements()) {
          if (flowElement instanceof StartEvent) {
            addStartEvent((StartEvent) flowElement);
          } else if (flowElement instanceof UserTask) {
            addUserTask((UserTask) flowElement);
          } else if (flowElement instanceof EndEvent) {
            addEndEvent((EndEvent) flowElement);
          } else if (flowElement instanceof ParallelGateway) {
            addParallelGateway((ParallelGateway) flowElement);
          } else if (flowElement instanceof ExclusiveGateway) {
            addExclusiveGateway((ExclusiveGateway) flowElement);
          } else if (flowElement instanceof ServiceTask) {
            addServiceTask((ServiceTask) flowElement);
          } else if (flowElement instanceof SequenceFlow) {
            sequenceFlows.add((SequenceFlow) flowElement);
          } 
        }
        processSequenceFlows();

        // Create process definition node
        Node processDefinitionNode = graphDb.createNode();
        processDefinition = new ProcessDefinition();
        processDefinition.setId(processDefinitionNode.getId());
        processDefinition.setKey(process.getId());

        // Temporary (for visualization)
        graphDb.getReferenceNode().createRelationshipTo(processDefinitionNode, RelTypes.PROCESS_DEFINITION);

        // Create relationship from process definition node to start event
        StartEvent startEvent = BpmnModelUtil.findFlowElementsOfType(process, StartEvent.class).get(0);
        Node startEventNode = nodeMap.get(startEvent.getId());
        processDefinitionNode.createRelationshipTo(startEventNode, RelTypes.IS_STARTED_FROM);

        // Add process definition to index
        Index<Node> processDefinitionIndex = graphDb.index().forNodes(Constants.PROCESS_DEFINITION_INDEX);
        processDefinitionIndex.add(processDefinitionNode, Constants.INDEX_KEY_PROCESS_DEFINITION_KEY, processDefinition.getKey());
        
        commandContext.setResult(processDefinition);
      }

    });
  }
  protected void addStartEvent(StartEvent startEvent) {
    Node startEventNode = createNode(startEvent);
    startEventNode.setProperty("type", Constants.TYPE_START_EVENT);
  }

  protected void addEndEvent(EndEvent endEvent) {
    Node endEventNode = createNode(endEvent);
    endEventNode.setProperty("type", Constants.TYPE_END_EVENT);
  }

  protected void addParallelGateway(ParallelGateway parallelGateway) {
    Node parallelGwNode = createNode(parallelGateway);
    parallelGwNode.setProperty("type", Constants.TYPE_PARALLEL_GATEWAY);
  }
  
  protected void addExclusiveGateway(ExclusiveGateway exclusiveGateway) {
    Node exclusiveGwNode = createNode(exclusiveGateway);
    exclusiveGwNode.setProperty("type", Constants.TYPE_EXCLUSIVE_GATEWAY);
    
    if (exclusiveGateway.getDefaultFlow() != null) {
      exclusiveGwNode.setProperty("defaultFlow", exclusiveGateway.getDefaultFlow());
    }
  }

  protected void addUserTask(UserTask userTask) {
    Node userTaskNode = createNode(userTask);
    userTaskNode.setProperty("type", Constants.TYPE_USER_TASK);

    if (userTask.getName() != null) {
      userTaskNode.setProperty("name", userTask.getName());
    }

    if (userTask.getAssignee() != null) {
      userTaskNode.setProperty("assignee", userTask.getAssignee());
    }
  }

  protected void addServiceTask(ServiceTask serviceTask) {
    Node serviceTaskNode = createNode(serviceTask);
    serviceTaskNode.setProperty("type", Constants.TYPE_SERVICE_TASK);
    serviceTaskNode.setProperty("class", serviceTask.getImplementation());
  }
  
  protected Node createNode(FlowNode flowNode) {
    Node node = graphDb.createNode();
    node.setProperty("id", flowNode.getId());
    
    nodeMap.put(flowNode.getId(), node);
    
    return node;
  }

  protected void processSequenceFlows() {
    for (SequenceFlow sequenceFlow : sequenceFlows) {
      Node sourceNode = nodeMap.get(sequenceFlow.getSourceRef());
      Node targetNode = nodeMap.get(sequenceFlow.getTargetRef());

      Relationship sequenceflowRelationship = sourceNode.createRelationshipTo(targetNode, RelTypes.SEQ_FLOW);
      sequenceflowRelationship.setProperty("id", sequenceFlow.getId());
      if (sequenceFlow.getConditionExpression() != null) {
        sequenceflowRelationship.setProperty("condition", sequenceFlow.getConditionExpression());
      }
    }
  }

}
