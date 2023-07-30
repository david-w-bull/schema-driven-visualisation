import React, { useState, useEffect } from "react";
import { Data, Entity, Attribute, Relationship } from "../types";

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
  entity: Entity | Relationship;
  onAttributeChange: (
    entityIndex: number,
    attributeIndex: number,
    checked: boolean
  ) => void;
}

const EntityComponent = ({ entity, onAttributeChange }: EntityProps) => {
  return (
    <div>
      <h2>{entity.name}</h2>
      {entity.attributes?.map((attribute, attributeIndex) => (
        <AttributeList
          key={attribute.attributeId}
          attribute={attribute}
          onChange={(checked) =>
            onAttributeChange(entity.id, attributeIndex, checked)
          }
        />
      ))}
    </div>
  );
};

interface EntityListProps {
  data: Data;
  onSelectedData: (selectedData: Data) => void;
}

const EntityListTest = ({
  data: initialData,
  onSelectedData,
}: EntityListProps) => {
  const [data, setData] = useState(initialData);

  useEffect(() => {
    setData(initialData);
  }, [initialData]);

  const handleAttributeChange = (
    entityIndex: number,
    attributeIndex: number,
    checked: boolean
  ) => {
    let newData = { ...data };
    newData.entityList = [...data.entityList];
    newData.relationshipList = [...data.relationshipList];

    const entityOrRelationship =
      newData.entityList.find((entity) => entity.id === entityIndex) ||
      newData.relationshipList.find(
        (relationship) => relationship.id === entityIndex
      );

    if (entityOrRelationship) {
      entityOrRelationship.attributes[attributeIndex].isChecked = checked;
      setData(newData);
    }
  };

  const handleSubmit = () => {
    // Filter entities with selected attributes
    const selectedEntities = data.entityList.filter(
      (entity) =>
        entity.attributes && entity.attributes.some((attr) => attr.isChecked)
    );

    // Filter relationships with selected attributes
    const selectedRelationships = data.relationshipList.filter(
      (relationship) =>
        relationship.attributes &&
        relationship.attributes.some((attr) => attr.isChecked)
    );

    const selectedData: Data = {
      schemaId: data.schemaId,
      name: data.name,
      connectionString: data.connectionString,
      entityList: selectedEntities,
      relationshipList: selectedRelationships,
    };

    onSelectedData(selectedData);
  };

  return (
    <div>
      {data.entityList.map((entity) => (
        <EntityComponent
          key={entity.id}
          entity={entity}
          onAttributeChange={handleAttributeChange}
        />
      ))}
      {data.relationshipList
        .filter(
          (relationship) =>
            relationship.attributes && relationship.attributes.length > 0
        )
        .map((relationship) => (
          <EntityComponent
            key={relationship.id}
            entity={relationship}
            onAttributeChange={handleAttributeChange}
          />
        ))}
      <button onClick={handleSubmit}>Submit</button>
    </div>
  );
};

export default EntityListTest;
