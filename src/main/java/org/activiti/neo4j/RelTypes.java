package org.activiti.neo4j;

import org.neo4j.graphdb.RelationshipType;

public enum RelTypes implements RelationshipType {
  IS_STARTED_FROM,
  PROCESS_INSTANCE,
  PROCESS_DEFINITION,
  EXECUTION,
  SEQ_FLOW,
  VARIABLE
}