import React, { useState } from "react";
import { Attribute, EntityOrRelationship } from "../types";
import styled from "styled-components";
import { Select } from "antd";
import "../App.css";
import DataTypeSelector from "./DataTypeSelector";

interface AttributeProps {
  attribute: Attribute;
  onChange: (checked: boolean) => void;
  databaseName: string;
}

const { Option } = Select;

const AttributeList = ({
  attribute,
  onChange,
  databaseName,
}: AttributeProps) => {
  const handleCheckboxChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    onChange(e.target.checked);
  };

  return (
    <div
      className="custom-control custom-checkbox"
      style={{
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
      }}
    >
      <div style={{ display: "flex", alignItems: "center", gap: "5px" }}>
        <input
          type="checkbox"
          className="custom-control-input pr-3"
          id={databaseName + "-attr-" + attribute.attributeId.toString()}
          checked={attribute.isChecked || false}
          onChange={handleCheckboxChange}
        />
        <label
          className="custom-control-label"
          htmlFor={databaseName + "-attr-" + attribute.attributeId.toString()}
          style={{
            fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
            fontWeight: 300,
          }}
        >
          {attribute.attributeName}
        </label>
      </div>
      {/* Data type selection POC trialled but not functional or connected to backend data */}
      {/* {attribute.isChecked && <DataTypeSelector />} */}
    </div>
  );
};

interface EntityProps {
  item: EntityOrRelationship;
  onAttributeChange: (attribute: Attribute, checked: boolean) => void;
  databaseName: string;
}

const EntityComponent = ({
  item,
  onAttributeChange,
  databaseName,
}: EntityProps) => {
  return (
    <EntityContainer>
      <EntityHeader>{item.name}</EntityHeader>
      <Divider />
      {item.attributes.map((attribute) => (
        <AttributeList
          key={attribute.attributeId}
          attribute={attribute}
          onChange={(checked) => onAttributeChange(attribute, checked)}
          databaseName={databaseName}
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
