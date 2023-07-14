import { useState, useEffect } from "react";
import axios from "axios";
import "./App.css";
import Message from "./components/Message";
import { Vega, VegaLite, VisualizationSpec } from "react-vega";
import ListGroup from "./components/ListGroup";
import { Data, Entity, Attribute } from "./types";
import EntityList from "./components/EntityList";

function App() {
  let items = ["Test Viz 1", "Test Viz 2", "Test Viz 3"];
  const blankSpec = {
    width: 0,
    height: 0,
    mark: "bar",
  };
  const [vegaSpec, setVegaSpec] = useState(blankSpec);

  const handleSelectItem = (item: string) => {
    const payload = { viz: item };
    axios
      .post("http://localhost:8080/api/v1/specs/postTest", payload)
      .then((response) => setVegaSpec(response.data.spec));
  };

  const testData: Data = {
    id: null,
    testId: -1,
    schemaId: 1,
    name: "public",
    entityList: [
      {
        entityID: 1,
        entityName: "airport",
        entityType: "STRONG",
        relatedStrongEntity: null,
        entityAttributes: [
          {
            attributeId: 1,
            attributeName: "iata_code",
            mandatory: true,
            optional: false,
            multiValued: false,
            dataType: "VARCHAR",
            isPrimary: true,
          },
          {
            attributeId: 2,
            attributeName: "name",
            mandatory: false,
            optional: true,
            multiValued: false,
            dataType: "VARCHAR",
            isPrimary: false,
          },
          {
            attributeId: 3,
            attributeName: "latitude",
            mandatory: true,
            optional: false,
            multiValued: false,
            dataType: "NUMERIC",
            isPrimary: false,
          },
          {
            attributeId: 4,
            attributeName: "longitude",
            mandatory: true,
            optional: false,
            multiValued: false,
            dataType: "NUMERIC",
            isPrimary: false,
          },
          {
            attributeId: 5,
            attributeName: "elevation",
            mandatory: false,
            optional: true,
            multiValued: false,
            dataType: "INT4",
            isPrimary: false,
          },
          {
            attributeId: 6,
            attributeName: "gmt_offset",
            mandatory: true,
            optional: false,
            multiValued: false,
            dataType: "INT4",
            isPrimary: false,
          },
        ],
      },
      {
        entityID: 2,
        entityName: "country",
        entityType: "STRONG",
        relatedStrongEntity: null,
        entityAttributes: [
          {
            attributeId: 7,
            attributeName: "name",
            mandatory: true,
            optional: false,
            multiValued: false,
            dataType: "VARCHAR",
            isPrimary: false,
          },
          {
            attributeId: 8,
            attributeName: "code",
            mandatory: true,
            optional: false,
            multiValued: false,
            dataType: "VARCHAR",
            isPrimary: true,
          },
          {
            attributeId: 9,
            attributeName: "capital",
            mandatory: false,
            optional: true,
            multiValued: false,
            dataType: "VARCHAR",
            isPrimary: false,
          },
          {
            attributeId: 10,
            attributeName: "province",
            mandatory: false,
            optional: true,
            multiValued: false,
            dataType: "VARCHAR",
            isPrimary: false,
          },
          {
            attributeId: 11,
            attributeName: "area",
            mandatory: true,
            optional: false,
            multiValued: false,
            dataType: "NUMERIC",
            isPrimary: false,
          },
          {
            attributeId: 12,
            attributeName: "population",
            mandatory: true,
            optional: false,
            multiValued: false,
            dataType: "INT4",
            isPrimary: false,
          },
        ],
      },
    ],
  };
  // useEffect(() => {
  //   axios
  //     .get(
  //       "http://localhost:8080/api/v1/specs/af49b4ac-ae6a-4956-83a9-40ce2f4a042b"
  //     )
  //     .then((response) => setVegaSpec(response.data.spec));
  // }, []);

  return (
    <>
      <EntityList data={testData}></EntityList>
      {/* <ListGroup
        items={items}
        heading="Test Options"
        onSelectItem={handleSelectItem}
      />
      <Vega spec={vegaSpec} actions={false} /> */}
    </>
  );
}

export default App;
