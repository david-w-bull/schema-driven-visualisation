import React, { useState, useEffect } from "react";
import EntityComponent from "./EntityComponent";
import { Data, Entity, Attribute, Relationship } from "../types";
import styled from "styled-components";
// import { Button } from "antd";
// import { RightCircleTwoTone } from "@ant-design/icons";
import Button from "@mui/material/Button";
import IconButton from "@mui/material/IconButton";
import AddchartIcon from "@mui/icons-material/Addchart";

interface EntityListProps {
  data: Data;
  onSelectedData: (selectedData: Data) => void;
}

const EntityList = ({ data: initialData, onSelectedData }: EntityListProps) => {
  const [data, setData] = useState(initialData);
  const [isSelectionMade, setIsSelectionMade] = useState(false);

  useEffect(() => {
    setData(initialData);
    setIsSelectionMade(checkAnyCheckboxChecked(initialData));
  }, [initialData]);

  const checkAnyCheckboxChecked = (data: Data) => {
    for (const entity of data.entityList) {
      for (const attribute of entity.attributes || []) {
        if (attribute.isChecked) {
          return true;
        }
      }
    }

    for (const relationship of data.relationshipList) {
      for (const attribute of relationship.attributes || []) {
        if (attribute.isChecked) {
          return true;
        }
      }
    }

    return false;
  };

  const handleAttributeChange = (
    isEntity: boolean,
    itemId: number,
    attributeId: number,
    checked: boolean
  ) => {
    let newData = { ...data };

    const updateAttributeCheckedStatus = (attributes: Attribute[]) => {
      const attributeIndex = attributes.findIndex(
        (attribute) => attribute.attributeId === attributeId
      );
      if (attributeIndex !== -1) {
        attributes[attributeIndex].isChecked = checked;
      }
    };

    if (isEntity) {
      const entityIndex = newData.entityList.findIndex(
        (entity) => entity.id === itemId
      );
      if (entityIndex !== -1) {
        updateAttributeCheckedStatus(
          newData.entityList[entityIndex].attributes
        );
      }
    } else {
      const relationshipIndex = newData.relationshipList.findIndex(
        (relationship) => relationship.id === itemId
      );
      if (relationshipIndex !== -1) {
        updateAttributeCheckedStatus(
          newData.relationshipList[relationshipIndex].attributes
        );
      }
    }

    setData(newData);
    setIsSelectionMade(checkAnyCheckboxChecked(newData));
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
    const relationshipsWithCheckedAttributes: Relationship[] = [];

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

    for (const relationship of data.relationshipList) {
      const selectedAttributes =
        relationship.attributes?.filter((attr) => attr.isChecked) || [];

      if (selectedAttributes.length > 0) {
        relationshipsWithCheckedAttributes.push({
          ...relationship,
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

      if (
        entitiesInRelationshipAreChecked ||
        relationshipsWithCheckedAttributes.some(
          (checkedRelationship) => checkedRelationship.id === relationship.id
        )
      ) {
        // Only include attributes where isChecked is true
        const selectedAttributes =
          relationship.attributes?.filter((attr) => attr.isChecked) || [];

        // Push the modified relationship object
        selectedData.relationshipList.push({
          ...relationship,
          attributes: selectedAttributes,
        });
      }
    }

    onSelectedData(selectedData);
  };

  return (
    <>
      <Button
        variant="contained"
        endIcon={<AddchartIcon />}
        onClick={() => {
          handleSubmit();
        }}
        style={{
          margin: "5px 20px 20px 20px",
        }}
        disabled={!isSelectionMade}
      >
        Visualise
      </Button>
      <ComponentContainer>
        {data.entityList.map((entity) => (
          <EntityComponent
            key={entity.id}
            item={entity}
            onAttributeChange={(attributeId, checked) =>
              handleAttributeChange(true, entity.id, attributeId, checked)
            }
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
              item={relationship}
              onAttributeChange={(attributeId, checked) =>
                handleAttributeChange(
                  false,
                  relationship.id,
                  attributeId,
                  checked
                )
              }
            />
          ))}
      </ComponentContainer>
      {/* <button onClick={handleSubmit}>Submit</button> */}
    </>
  );
};

export default EntityList;

const ComponentContainer = styled.div`
  display: flex;
  flex-wrap: wrap;
  justify-content: start;
`;
