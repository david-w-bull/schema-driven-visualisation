import React, { useState, useEffect } from "react";
import { Data, Entity, Attribute } from "../types";
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
      <h2>{entity.entityName}</h2>
      {entity.entityAttributes.map((attribute, attributeIndex) => (
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
}

const EntityList = ({ data: initialData }: EntityListProps) => {
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
    newData[entityIndex].entityAttributes[attributeIndex].isChecked = checked;
    setData({ ...data, entityList: newData });
  };

  const handleSubmit = () => {
    const selectedData = { ...data, entityList: [] as Entity[] };

    for (const entity of data.entityList) {
      const selectedAttributes = entity.entityAttributes.filter(
        (attr) => attr.isChecked
      );

      if (selectedAttributes.length > 0) {
        selectedData.entityList.push({
          ...entity,
          entityAttributes: selectedAttributes,
        });
      }
    }

    console.log(JSON.stringify(selectedData));
  };

  return (
    <div>
      {data.entityList.map((entity, entityIndex) => (
        <EntityComponent
          key={entity.entityID}
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
