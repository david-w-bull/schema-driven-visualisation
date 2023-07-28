import { BooleanValue } from "vega";

export type Attribute = {
  attributeId: number;
  attributeName: string;
  mandatory: boolean;
  optional: boolean;
  multiValued: boolean;
  dataType: string;
  isPrimary: boolean;
  isChecked?: boolean;
};

export type ForeignKey = {
  fkTableName: string;
  fkColumnNames: string[];
  pkTableName: string;
  pkColumnNames: string[];
};

export type Entity = {
  entityID: number;
  entityName: string;
  entityType: string;
  relatedStrongEntity: null;
  entityAttributes: Attribute[];
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

export type Relationship = {
  relationshipId: number;
  relationshipName: string;
  isWeakRelationship: boolean;
  entityA: string;
  entityACardinality: string;
  entityB: string;
  entityBCardinality: string;
  overallCardinality: string;
  relationships: RelationshipEdge[];
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
