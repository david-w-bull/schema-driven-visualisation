import React from "react";
import { useState } from "react";
import { Data, Entity, Attribute } from "../types";

interface EntityListProps {
  data: Data;
}

const EntityList = ({ data: initialData }: EntityListProps) => {
  const [data, setData] = useState(initialData);

  const handleCheckChange = (
    event: React.ChangeEvent<HTMLInputElement>,
    entityIndex: number,
    attributeIndex: number
  ) => {
    let newData = [...data.entityList];
    newData[entityIndex].entityAttributes[attributeIndex].isChecked =
      event.target.checked;
    setData({ ...data, entityList: newData });
  };

  if (!data) {
    return "Loading...";
  }

  return (
    <div>
      {data.entityList.map((entity, entityIndex) => (
        <div key={entity.entityID}>
          <h2>{entity.entityName}</h2>
          {entity.entityAttributes.map((attribute, attributeIndex) => (
            <div key={attribute.attributeId}>
              <input
                type="checkbox"
                checked={attribute.isChecked || false}
                onChange={(e) =>
                  handleCheckChange(e, entityIndex, attributeIndex)
                }
              />
              <label>{attribute.attributeName}</label>
            </div>
          ))}
        </div>
      ))}
    </div>
  );
};

export default EntityList;
