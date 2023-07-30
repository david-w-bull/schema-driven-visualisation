import { BooleanValue } from "vega";

export type Attribute = {
  attributeId: number;
  attributeName: string;
  mandatory: boolean;
  optional: boolean;
  multiValued: boolean;
  dataType: string;
  isPrimary: boolean;
  parentEntityName: string;
  isChecked?: boolean;
};

export type ForeignKey = {
  fkTableName: string;
  fkColumnNames: string[];
  pkTableName: string;
  pkColumnNames: string[];
};

export type Entity = {
  id: number;
  name: string;
  entityType: string;
  relatedStrongEntity: null;
  attributes: Attribute[];
  foreignKeys: ForeignKey[];
};

export type Relationship = {
  id: number;
  name: string;
  isWeakRelationship: boolean;
  entityA: string;
  entityACardinality: string;
  entityB: string;
  entityBCardinality: string;
  overallCardinality: string;
  relationships: RelationshipEdge[];
  attributes: Attribute[];
  foreignKeys: ForeignKey[];
};

export type RelationshipEdge = {
  cardinality: string;
  edgeId: number;
  entityId: number;
  entityName: string;
  isKey: boolean;
  relationshipId: number;
};

export type Data = {
  id?: string | null;
  testId?: number;
  schemaId: number;
  name: string;
  connectionString: string;
  entityList: Entity[];
  relationshipList: Relationship[];
};
