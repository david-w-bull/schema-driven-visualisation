import { useState, useEffect, useRef } from "react";
import axios from "axios";
import "./App.css";
import Message from "./components/Message";
import { Vega, VegaLite, VisualizationSpec } from "react-vega";
import ListGroup from "./components/ListGroup";
import { Data, Entity, Attribute, VizSchema } from "./types";
import { BLANKSPEC, BLANKSCHEMA, BLANKVIZSCHEMA } from "./constants";
import EntityList from "./components/EntityList";
import DatabaseSelector from "./components/DatabaseSelector";
import ERDiagram from "./components/ERDiagram";
import ChordDiagram from "./components/ChordDiagram";
import ChordDiagramTest from "./components/ChordDiagramTest";
import TreeMap from "./components/TreeMap";
import * as d3 from "d3";
import SQLEditor from "./components/SQLEditor";

function App() {
  let items = ["Test Viz 1", "Test Viz 2", "Test Viz 3"];

  const nodes = [
    {
      key: "Entity1",
      properties: "id: number\nname: string",
      type: "entity" as const,
    },
    {
      key: "Relationship1",
      properties: "entity1: number\nentity2: number",
      type: "relationship" as const,
    },
    // Add more entities and relationships as needed...
  ];
  const links = [
    { id: "1", from: "Entity1", to: "Relationship1", text: "1 - n" },
    // Add more relationships as needed...
  ];

  const [vegaSpec, setVegaSpec] = useState(BLANKSPEC);
  const [vegaActionMenu, setVegaActionMenu] = useState(false);
  const [schemaInfo, setSchemaInfo] = useState(BLANKSCHEMA);
  //const [chartType, setChartType] = useState<string>("");

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

  const chordData = {
    nodes: [{ id: "A" }, { id: "B" }, { id: "C" }],
    edges: [
      { source: "A", target: "B", value: 10 },
      { source: "A", target: "C", value: 15 },
      { source: "B", target: "C", value: 8 },
    ],
  };

  const [selectedData, setSelectedData] = useState<Data | null>(null);
  const [chartTypes, setChartTypes] = useState<string[]>([]);
  const [selectedChart, setSelectedChart] = useState<string | null>(null);
  const [specList, setSpecList] = useState<any[]>([]);
  const [vizSchema, setVizSchema] = useState<VizSchema>(BLANKVIZSCHEMA);

  const handleSelectedData = (data: Data) => {
    setVegaSpec(BLANKSPEC);
    setVegaActionMenu(false);
    setSelectedData(data);
    const payload = { schema: JSON.stringify(data) };
    axios
      .post("http://localhost:8080/api/v1/specs/specFromSchema", payload)
      .then((response) => {
        console.log(JSON.stringify(response.data));
        response.data.specs.forEach((specItem: any) => {
          // Iterate over each data item in the current 'spec' item
          specItem.data.forEach((dataItem: any) => {
            // Check if the 'name' field of the current data item is 'rawData'
            if (dataItem.name === "rawData") {
              // If the name is 'rawData', set the 'values' field of the current data item to the 'dataset' array
              dataItem.values = response.data.vizSchema.dataset;
            }
          });
          setChartTypes(response.data.vizSchema.chartTypes);
          setSpecList(response.data.specs);
          setVizSchema(response.data.vizSchema);
          setSqlCode(response.data.vizSchema.sqlQuery);
          console.log("Logging vizSchema");
          console.log(response.data.vizSchema);
        });
      });

    console.log(JSON.stringify(data));
  };

  const renderChartComponent = (chartType: string) => {
    switch (chartType) {
      case "Chord Diagram":
        //return <ChordDiagram vizInfo={vizInfo} />;
        //return <ChordDiagram vizSchema={vizSchema} />;
        return <ChordDiagramTest vizSchema={vizSchema} />;
      // ... add more cases for other chart types ...
      default:
        return null;
    }
  };

  const [sqlCode, setSqlCode] = useState("SELECT * FROM users;");

  const handleSqlSubmit = () => {
    console.log("Submitted SQL Code:", sqlCode);
  };

  return (
    <>
      <DatabaseSelector onSelectDatabase={handleSelectDatabase} />
      <EntityList
        data={schemaInfo}
        onSelectedData={handleSelectedData}
      ></EntityList>
      {/* <ERDiagram nodes={nodes} links={links} /> */}
      {/* <ListGroup
        items={items}
        heading="Test Options"
        onSelectItem={handleSelectItem}
      />*/}
      {chartTypes?.map((chartType: string, index: number) => {
        const matchingSpec = specList.find(
          (spec: any) => spec.description === chartType
        );
        return (
          <button
            key={index}
            onClick={() => {
              if (matchingSpec) {
                setVegaSpec(matchingSpec);
                setVegaActionMenu(true);
                setSelectedChart(null);
              } else {
                setSelectedChart(chartType);
              }
            }}
          >
            {chartType}
          </button>
        );
      })}
      {selectedChart ? (
        renderChartComponent(selectedChart)
      ) : (
        <Vega spec={vegaSpec} actions={vegaActionMenu} />
      )}
      <div>
        <SQLEditor value={sqlCode} onChange={setSqlCode} />
        <button onClick={handleSqlSubmit}>Update SQL</button>
      </div>
    </>
  );
}

export default App;
