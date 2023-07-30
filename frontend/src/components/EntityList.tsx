import React, { useState, useEffect } from "react";
import { Data, Entity, Attribute, Relationship, ForeignKey } from "../types";
import { BLANKSCHEMA } from "../constants";

interface AttributeProps {
  attribute: Attribute;
  onChange: (checked: boolean) => void;
}

const AttributeList = ({ attribute, onChange }: AttributeProps) => {
  const handleCheckboxChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    onChange(e.target.checked);
  };

  return (
    <div>
      <input
        type="checkbox"
        checked={attribute.isChecked || false}
        onChange={handleCheckboxChange}
      />
      <label>{attribute.attributeName}</label>
    </div>
  );
};

interface EntityProps {
  entity: Entity;
  onAttributeChange: (attributeIndex: number, checked: boolean) => void;
}

const EntityComponent = ({ entity, onAttributeChange }: EntityProps) => {
  return (
    <div>
      <h2>{entity.name}</h2>
      {entity.attributes.map((attribute, attributeIndex) => (
        <AttributeList
          key={attribute.attributeId}
          attribute={attribute}
          onChange={(checked) => onAttributeChange(attributeIndex, checked)}
        />
      ))}
    </div>
  );
};

interface EntityListProps {
  data: Data;
  onSelectedData: (selectedData: Data) => void;
}

const EntityList = ({ data: initialData, onSelectedData }: EntityListProps) => {
  const [data, setData] = useState(initialData);

  useEffect(() => {
    setData(initialData);
  }, [initialData]);

  const handleAttributeChange = (
    entityIndex: number,
    attributeIndex: number,
    checked: boolean
  ) => {
    let newData = [...data.entityList];
    newData[entityIndex].attributes[attributeIndex].isChecked = checked;
    setData({ ...data, entityList: newData });
  };

  const handleSubmit = () => {
    const selectedData = {
      schemaId: data.schemaId,
      name: data.name,
      connectionString: data.connectionString,
      entityList: [] as Entity[],
      relationshipList: [] as Relationship[],
    };

    const entityIdsWithCheckedAttributes: number[] = [];
    const entitiesWithCheckedAttributes: Entity[] = [];

    for (const entity of data.entityList) {
      const selectedAttributes =
        entity.attributes?.filter((attr) => attr.isChecked) || [];

      if (selectedAttributes.length > 0) {
        entityIdsWithCheckedAttributes.push(entity.id);
        entitiesWithCheckedAttributes.push({
          ...entity,
          attributes: selectedAttributes,
        });
      }
    }

    // Add entity names with checked attributes to a separate array
    const entityNamesWithCheckedAttributes = entitiesWithCheckedAttributes.map(
      (entity) => entity.name
    );

    // Filter foreign keys for each entity with selected attributes
    for (const entity of entitiesWithCheckedAttributes) {
      const selectedForeignKeys =
        entity.foreignKeys?.filter((fk) =>
          entityNamesWithCheckedAttributes.includes(fk.pkTableName)
        ) || [];

      selectedData.entityList.push({
        ...entity,
        foreignKeys: selectedForeignKeys,
      });
    }

    for (const relationship of data.relationshipList) {
      const relatedEntityIds = relationship.relationships.map(
        (relationshipEdge) => relationshipEdge.entityId
      );

      const entitiesInRelationshipAreChecked = relatedEntityIds.every((id) =>
        entityIdsWithCheckedAttributes.includes(id)
      );

      if (entitiesInRelationshipAreChecked) {
        selectedData.relationshipList.push(relationship);
      }
    }

    onSelectedData(selectedData);
  };

  return (
    <div>
      {data.entityList.map((entity, entityIndex) => (
        <EntityComponent
          key={entity.id}
          entity={entity}
          onAttributeChange={(attributeIndex, checked) =>
            handleAttributeChange(entityIndex, attributeIndex, checked)
          }
        />
      ))}
      <button onClick={handleSubmit}>Submit</button>
    </div>
  );
};

export default EntityList;
