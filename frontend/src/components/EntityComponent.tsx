import React from "react";
import { Attribute, EntityOrRelationship } from "../types";

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
  item: EntityOrRelationship;
  onAttributeChange: (attributeId: number, checked: boolean) => void;
}

const EntityComponent = ({ item, onAttributeChange }: EntityProps) => {
  return (
    <div>
      <h2>{item.name}</h2>
      {item.attributes.map((attribute) => (
        <AttributeList
          key={attribute.attributeId}
          attribute={attribute}
          onChange={(checked) =>
            onAttributeChange(attribute.attributeId, checked)
          }
        />
      ))}
    </div>
  );
};

export default EntityComponent;
