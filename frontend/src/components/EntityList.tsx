import React, { useState, useEffect } from "react";
import EntityComponent from "./EntityComponent";
import {
  Data,
  Entity,
  Attribute,
  Relationship,
  EntityOrRelationship,
} from "../types";
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
  const [entityNames, setEntityNames] = useState<string[]>([]);
  const [relationshipNames, setRelationshipNames] = useState<string[]>([]);
  const [isSelectionMade, setIsSelectionMade] = useState(false);
  const [checkedAttributes, setCheckedAttributes] = useState<Attribute[]>([]);

  useEffect(() => {
    setData(initialData);
    setIsSelectionMade(checkAnyCheckboxChecked(initialData));
    setEntityNames(initialData.entityList.map((entity) => entity.name));
    setRelationshipNames(
      initialData.relationshipList.map((relationship) => relationship.name)
    );
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
    attribute: Attribute,
    checked: boolean
  ) => {
    let newData = { ...data };

    const updateAttributeCheckedStatus = (attributes: Attribute[]) => {
      const attributeIndex = attributes.findIndex(
        (attr) => attr.attributeId === attribute.attributeId
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

    // Track checked attributes
    if (checked) {
      setCheckedAttributes((prevAttributes) => [...prevAttributes, attribute]);
    } else {
      setCheckedAttributes((prevAttributes) =>
        prevAttributes.filter(
          (attr) => attr.attributeId !== attribute.attributeId
        )
      );
    }

    setData(newData);
    setIsSelectionMade(checkAnyCheckboxChecked(newData));
  };

  const getCheckedAttributesData = (data: Data) => {
    const entitiesWithCheckedAttributes: Set<Entity> = new Set();
    const entityNamesWithCheckedAttributes: Set<string> = new Set();
    const entityIdsWithCheckedAttributes: Set<number> = new Set();

    const relationshipsWithCheckedAttributes: Set<Relationship> = new Set();
    const relationshipNamesWithCheckedAttributes: Set<string> = new Set();
    const relationshipIdsWithCheckedAttributes: Set<number> = new Set();

    for (const checkedAttr of checkedAttributes) {
      if (entityNames.includes(checkedAttr.parentEntityName)) {
        entityNamesWithCheckedAttributes.add(checkedAttr.parentEntityName);
      } else if (relationshipNames.includes(checkedAttr.parentEntityName)) {
        relationshipNamesWithCheckedAttributes.add(
          checkedAttr.parentEntityName
        );
      }
    }

    for (const entityName of entityNamesWithCheckedAttributes) {
      let entity = data.entityList.find((entity) => entity.name === entityName);
      // Add the entity but only included selected attributes in its attribute list
      if (entity) {
        const selectedAttributes =
          entity.attributes?.filter((attr) => attr.isChecked) || [];
        entitiesWithCheckedAttributes.add({
          ...entity,
          attributes: selectedAttributes,
        });
        entityIdsWithCheckedAttributes.add(entity.id);
      }
    }

    for (const relationshipName of relationshipNamesWithCheckedAttributes) {
      let relationship = data.relationshipList.find(
        (relationship) => relationship.name === relationshipName
      );
      if (relationship) {
        // Add the relationship but only included selected attributes in its attribute list
        const selectedAttributes =
          relationship.attributes?.filter((attr) => attr.isChecked) || [];
        relationshipsWithCheckedAttributes.add({
          ...relationship,
          attributes: selectedAttributes,
        });
        relationshipIdsWithCheckedAttributes.add(relationship.id);
      }
    }

    return {
      entitiesWithCheckedAttributes,
      entityNamesWithCheckedAttributes,
      entityIdsWithCheckedAttributes,
      relationshipsWithCheckedAttributes,
      relationshipNamesWithCheckedAttributes,
      relationshipIdsWithCheckedAttributes,
    };
  };

  const handleSubmit = () => {
    const selectedData = {
      schemaId: data.schemaId,
      name: data.name,
      connectionString: data.connectionString,
      entityList: [] as Entity[],
      relationshipList: [] as Relationship[],
    };

    const {
      entitiesWithCheckedAttributes,
      entityNamesWithCheckedAttributes,
      entityIdsWithCheckedAttributes,
      relationshipIdsWithCheckedAttributes,
    } = getCheckedAttributesData(data);

    // Filter foreign keys for each entity with selected attributes
    for (const entity of entitiesWithCheckedAttributes) {
      const selectedForeignKeys =
        entity.foreignKeys?.filter((fk) =>
          entityNamesWithCheckedAttributes.has(fk.pkTableName)
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
        entityIdsWithCheckedAttributes.has(id)
      );

      if (
        entitiesInRelationshipAreChecked ||
        relationshipIdsWithCheckedAttributes.has(relationship.id)
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
    console.log("Selected data");
    console.log(selectedData);
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
            onAttributeChange={(attribute, checked) =>
              handleAttributeChange(true, entity.id, attribute, checked)
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
              onAttributeChange={(attribute, checked) =>
                handleAttributeChange(
                  false,
                  relationship.id,
                  attribute,
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
