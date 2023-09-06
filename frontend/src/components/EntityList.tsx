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
import Button from "@mui/material/Button";
import AddchartIcon from "@mui/icons-material/Addchart";
import DeleteSweepIcon from "@mui/icons-material/DeleteSweep";

interface EntityListProps {
  data: Data;
  onSelectedData: (selectedData: Data) => void;
}

const EntityList = ({ data: initialData, onSelectedData }: EntityListProps) => {
  const [data, setData] = useState(initialData);
  const [entityNames, setEntityNames] = useState<string[]>([]);
  const [relationshipNames, setRelationshipNames] = useState<string[]>([]);
  const [databaseName, setDatabaseName] = useState("");
  const [subsetEntities, setSubsetEntities] = useState<Entity[]>([]);
  const [isSelectionMade, setIsSelectionMade] = useState(false);
  const [checkedAttributes, setCheckedAttributes] = useState<Attribute[]>([]);
  const [reachableEntities, setReachableEntities] = useState<Set<string>>(
    new Set()
  );
  const [reachableRelationships, setReachableRelationships] = useState<
    Set<string>
  >(new Set());

  useEffect(() => {
    setData(initialData);
    setEntityNames(initialData.entityList.map((entity) => entity.name));
    setRelationshipNames(
      initialData.relationshipList.map((relationship) => relationship.name)
    );
    setSubsetEntities(
      initialData.entityList.filter((entity) => entity.entityType == "SUBSET")
    );
    setIsSelectionMade(false);
    setCheckedAttributes([]);
    setReachableEntities(new Set());
    setReachableRelationships(new Set());

    const lastSlashIndex = initialData.connectionString.lastIndexOf("/");
    const currentDatabaseName = initialData.connectionString.substring(
      lastSlashIndex + 1
    );
    setDatabaseName(currentDatabaseName);
  }, [initialData]);

  const handleClearSelection = () => {
    checkedAttributes.forEach((attribute) => {
      const parentName = attribute.parentEntityName;
      const isEntity = entityNames.includes(parentName);
      let itemId = -1;

      const searchList: EntityOrRelationship[] = isEntity
        ? data.entityList
        : data.relationshipList;
      const parentObject = searchList.find(
        (item): any => item.name === parentName
      );
      parentObject && (itemId = parentObject.id);
      // console.log("----Attribute----");
      // console.log(isEntity);
      // console.log(itemId);
      // console.log(attribute);
      handleAttributeChange(isEntity, itemId, attribute, false);
    });
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
      setCheckedAttributes((prevAttributes) => {
        const newAttributes = [...prevAttributes, attribute];
        setIsSelectionMade(newAttributes.length > 0);
        console.log("Checked:");
        console.log(newAttributes);
        return newAttributes;
      });
    } else {
      setCheckedAttributes((prevAttributes) => {
        const newAttributes = prevAttributes.filter(
          (attr) => attr.attributeId !== attribute.attributeId
        );
        console.log("Checked:");
        console.log(newAttributes);
        setIsSelectionMade(newAttributes.length > 0);
        return newAttributes;
      });
    }
    setData(newData);
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

  const getActiveRelationships = (
    data: Data,
    entityNames: Set<string>,
    relationshipNames: Set<string>
  ) => {
    return data.relationshipList.filter((relationship) => {
      return (
        entityNames.has(relationship.entityA) ||
        entityNames.has(relationship.entityB) ||
        relationshipNames.has(relationship.name)
      );
    });
  };

  useEffect(() => {
    const {
      entitiesWithCheckedAttributes,
      entityNamesWithCheckedAttributes,
      relationshipNamesWithCheckedAttributes,
    } = getCheckedAttributesData(data);
    const activeRelationships = getActiveRelationships(
      data,
      entityNamesWithCheckedAttributes,
      relationshipNamesWithCheckedAttributes
    );

    // Add current selections as reachable
    const allReachableEntities: Set<string> = new Set(
      entityNamesWithCheckedAttributes
    );
    const allReachableRelationships: Set<string> = new Set(
      relationshipNamesWithCheckedAttributes
    );

    // Add one-hop relationships as reachable
    activeRelationships.forEach((rel) => {
      allReachableEntities.add(rel.entityA);
      allReachableEntities.add(rel.entityB);
      allReachableRelationships.add(rel.name);
    });

    // Add is-a relationships as reachable in both directions

    // When the checked attribute is on the subset
    for (const entity of entitiesWithCheckedAttributes) {
      if (entity.relatedStrongEntity !== null) {
        allReachableEntities.add(entity.relatedStrongEntity.name);
      }
    }

    // When the checked attribute is on the parent
    const matchedEntities = subsetEntities.filter(
      (entity) =>
        entity.relatedStrongEntity &&
        entityNamesWithCheckedAttributes.has(entity.relatedStrongEntity.name)
    );
    const matchedNames = matchedEntities.map((entity) => entity.name);
    matchedNames.forEach((entityName) => allReachableEntities.add(entityName));

    setReachableEntities(allReachableEntities);
    setReachableRelationships(allReachableRelationships);
  }, [checkedAttributes]);

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
    onSelectedData(selectedData);
  };

  return (
    <>
      <Button
        id="button-submit-selected-fields"
        variant="contained"
        endIcon={<AddchartIcon />}
        onClick={() => {
          handleSubmit();
        }}
        style={{
          margin: "5px 20px 5px 20px",
        }}
        disabled={!isSelectionMade}
      >
        Visualise
      </Button>
      {isSelectionMade ? (
        <Button
          variant="text"
          endIcon={<DeleteSweepIcon />}
          onClick={() => {
            handleClearSelection();
          }}
          style={{
            margin: "5px 20px 5px 20px",
          }}
        >
          Clear Selection
        </Button>
      ) : (
        <div
          style={{
            minHeight: "20px",
            margin: "5px 20px 5px 20px",
          }}
        ></div>
      )}
      <ComponentContainer>
        {(checkedAttributes.length > 0
          ? data.entityList.filter((entity) =>
              reachableEntities.has(entity.name)
            )
          : data.entityList
        ).map((entity) => (
          <EntityComponent
            key={entity.id}
            item={entity}
            onAttributeChange={(attributeId, checked) =>
              handleAttributeChange(true, entity.id, attributeId, checked)
            }
            databaseName={databaseName}
          />
        ))}
        {(checkedAttributes.length > 0
          ? data.relationshipList.filter((relationship) =>
              reachableRelationships.has(relationship.name)
            )
          : data.relationshipList
        )
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
              databaseName={databaseName}
            />
          ))}
      </ComponentContainer>
    </>
  );
};

export default EntityList;

const ComponentContainer = styled.div`
  display: flex;
  flex-wrap: wrap;
  justify-content: start;
`;
