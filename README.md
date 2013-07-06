Activiti - Neo4J
=================

This is a prototype on how the Activiti BPM engine (http://activiti.org/) could be written on top of a Graph Database, in this case Neo4J (http://www.neo4j.org/)

This is a regular Maven project.

Uses existing Activiti concepts: Executions, Command Context, services, etc. So if we want to ever make it an option in Activiti to choose underlying persistency.

## High level

As with Activiti, the ProcessEngine is the starting class of it all. 
Through the ProcessEngine, the different services can be obtained.

Internally, the ProcessEngine manages the GraphDB connection and a CommandExecutor.
The CommandExecutor is similar to the way it currently is implemented in Activiti.
For the moment, it 
* Wraps the logic in a Neo4J transaction
* Maintains an internal *agenda* (linkedlist of Runnables). All internal engine operations *must* put Runnables on this queue. The command executor simply takes Runnables from the queue and executes them.

Note that the CommandExecutor implements an interface called *InternalActivitiEngine*. 
This probably is similar to the AtomicOperations in the Activiti engine, exposing the 
operations that are possible in the engine (eg continue process, signal wait state, etc.)


## How it works

Suppose we have a simple process

start --> user task --> end

### Deploy the process

The process is defined in the standard BPMN 2.0 XML. The *RepositoryService* takes an *InputStream* to this XML.
The XML is now parsed to a Neo4J model. Neo4J basically has two data types: nodes and relationships. Both can have properties.

After parsing, the graph model looks as follows. --> means a relationship

[Node] Process definition (id, key) ---'is started from'--> [Node] Start event --> [Node] user task --> end

The start, task and end node have properties matching the process definition (name, id, etc.)
Each of these nodes has a *type* property, which internally maps to a certain *Behaviour* class.
This Behaviour class is executed when the execution arrives in the specific node.

The reason to have a separate Process Definition Node is because we add this node to an index, so
we can easily retrieve it.

### Start a Process Instance

* Query the Process definition using the index
* Create a Process instance node
* Follow the *is started from* relation ship, get the Start event node
* Create a relationship called *execution* to this node
* Put the behaviour of the start event node on the *agenda*

The start event behaviour is doing nothing more than simplu leaving the node. This is generic behaviour:
* Fetch all next nodes, by following outgoing relationships from the current node
* Put the continuation of these nodes on the *agenda*
* 

### User task

The user task is a wait state. When the engine arrives here
* Moves the execution relationship to the task node
* Add this relationship to the task index, so it can be easily queries later

The user task behaviour has a signal method (similar to Activiti).
The TaskService.complete() method will call this, which will basicvally trigger the generic 'leave' method from above.


### End

The behavior of the end simply removes all runtime nodes and executions.



### Variables

Variables are stored in a *variable node* which is related to the Process Instance node through a relationship called *variables*

Execution local/task local variables are possible in the same way, simply attach a variable node to that specific node.

## Thougths

* Migrations of process instance (when deploying process definitions) gets really easy with this model. It is a matter of moving execution relationships to the new process definition.
* Neo4J data can be easily shared: eg on machine A you have the data around process definition 1,2,3 and on machine B the process definiton 4. Also multi-tenancy gets easy.


