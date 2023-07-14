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

export type Entity = {
  entityID: number;
  entityName: string;
  entityType: string;
  relatedStrongEntity: null;
  entityAttributes: Attribute[];
};

export type Data = {
  id: null;
  testId: number;
  schemaId: number;
  name: string;
  entityList: Entity[];
};
