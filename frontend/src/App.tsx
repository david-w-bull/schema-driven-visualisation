import { useState, useEffect } from "react";
import axios from "axios";
import "./App.css";
import Message from "./components/Message";
import { Vega, VegaLite, VisualizationSpec } from "react-vega";
import ListGroup from "./components/ListGroup";
import { Data, Entity, Attribute } from "./types";
import { BLANKSPEC, BLANKSCHEMA } from "./constants";
import EntityList from "./components/EntityList";
import DatabaseSelector from "./components/DatabaseSelector";

function App() {
  let items = ["Test Viz 1", "Test Viz 2", "Test Viz 3"];

  const [vegaSpec, setVegaSpec] = useState(BLANKSPEC);
  const [schemaInfo, setSchemaInfo] = useState(BLANKSCHEMA);

  const handleSelectItem = (item: string) => {
    const payload = { viz: item };
    axios
      .post("http://localhost:8080/api/v1/specs/postTest", payload)
      .then((response) => setVegaSpec(response.data.spec));
  };

  const handleSelectDatabase = (selectedValue: string) => {
    console.log(selectedValue);
    axios
      .get("http://localhost:8080/api/v1/schemas/" + selectedValue)
      .then((response) => {
        console.log(response.data);
        setSchemaInfo(response.data);
      })
      .catch((error) => {
        console.error("There was an error!", error);
      });
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
      <DatabaseSelector onSelectDatabase={handleSelectDatabase} />
      <EntityList data={schemaInfo}></EntityList>
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
