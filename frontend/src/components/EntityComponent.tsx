import React from "react";
import { Attribute, EntityOrRelationship } from "../types";
import styled from "styled-components";
import "../App.css";

interface AttributeProps {
  attribute: Attribute;
  onChange: (checked: boolean) => void;
}

const AttributeList = ({ attribute, onChange }: AttributeProps) => {
  const handleCheckboxChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    onChange(e.target.checked);
  };

  return (
    <div
      className="custom-control custom-checkbox"
      style={{
        display: "flex",
        gap: "5px",
      }}
    >
      <input
        type="checkbox"
        className="custom-control-input pr-3"
        id={attribute.attributeId.toString()}
        checked={attribute.isChecked || false}
        onChange={handleCheckboxChange}
      />
      <label
        className="custom-control-label"
        htmlFor={attribute.attributeId.toString()}
        style={{
          fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
          fontWeight: 300,
          // fontFamily: "'Rubik', sans-serif",
        }}
      >
        {attribute.attributeName}
      </label>
    </div>
  );
};

interface EntityProps {
  item: EntityOrRelationship;
  onAttributeChange: (attribute: Attribute, checked: boolean) => void;
}

const EntityComponent = ({ item, onAttributeChange }: EntityProps) => {
  return (
    <EntityContainer>
      <EntityHeader>{item.name}</EntityHeader>
      <Divider />
      {item.attributes.map((attribute) => (
        <AttributeList
          key={attribute.attributeId}
          attribute={attribute}
          onChange={(checked) => onAttributeChange(attribute, checked)}
        />
      ))}
    </EntityContainer>
  );
};

export default EntityComponent;

const EntityContainer = styled.div`
  position: relative;
  width: 300px;
  border-color: #f0f0f0;
  border-weight: 1px;
  padding: 10px;
  margin: 30px 20px;
  border-radius: 10px;
`;

const Divider = styled.div`
  height: 1px;
  background-color: #d9d9d9;
  margin: 10px 0;
`;

const EntityHeader = styled.h1`
  font-size: 26px;
  color: #333;
  margin: 0;
  padding: 2px;
`;
