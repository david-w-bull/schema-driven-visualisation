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

export type VizSchema = {
  type: string;
  keyOne?: Attribute;
  keyOneAlias?: string;
  keyTwo?: Attribute;
  keyTwoAlias?: string;
  scalarOne?: Attribute;
  scalarOneAlias?: string;
  reflexive?: boolean;
  dataset?: any[];
  keyCardinality?: number;
  dataRelationship?: string;
  exampleData?: any[];
  sqlQuery?: string;
  connectionString?: string;
  chartTypes?: string[];
  messages: string[];
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

export type EntityOrRelationship = Entity | Relationship;

export type RelationshipEdge = {
  cardinality: string;
  edgeId: number;
  entityId: number;
  entityName: string;
  isKey: boolean;
  relationshipId: number;
};

// this actually corresponds to the DatabaseSchema type in the backend
export type Data = {
  id?: string | null;
  testId?: number;
  schemaId: number;
  name: string;
  connectionString: string;
  entityList: Entity[];
  relationshipList: Relationship[];
};

export type CardinalityLimits = {
  [key: string]: number;
};

export type ChartRecommendations = {
  Recommended: string[];
  Possible: string[];
  Other: string[];
};
