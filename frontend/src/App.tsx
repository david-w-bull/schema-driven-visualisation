import { useState, useEffect, useRef } from "react";
import axios from "axios";
import "./App.css";
import Message from "./components/Message";
import { Vega, VegaLite, VisualizationSpec } from "react-vega";
import ListGroup from "./components/ListGroup";
import { Data, Entity, Attribute } from "./types";
import { BLANKSPEC, BLANKSCHEMA } from "./constants";
import EntityList from "./components/EntityList";
import DatabaseSelector from "./components/DatabaseSelector";
import ERDiagram from "./components/ERDiagram";
import * as d3 from "d3";

function App() {
  let items = ["Test Viz 1", "Test Viz 2", "Test Viz 3"];

  const ref = useRef(null);

  const svg = d3.select(ref.current);

  let innerRadius = 200;

  // let matrix = [
  //   [11975, 5871, 8916, 2868],
  //   [1951, 10048, 2060, 6171],
  //   [8010, 16145, 8090, 8045],
  //   [1013, 990, 940, 6907],
  // ];

  let matrix = [
    [0.0, 172.1, 0.0, 0.0],
    [172.0, 0.0, 282.0, 151.0],
    [0.0, 282.0, 0.0, 0.0],
    [0.0, 151.0, 0.0, 0.0],
  ];

  const chordGenerator = d3.chord().padAngle(0.05).sortSubgroups(d3.descending);
  const chords = chordGenerator(matrix);

  const ribbon = d3.ribbon().radius(innerRadius);

  console.log(JSON.stringify(chords));
  console.log(JSON.stringify(chords.groups));

  const ribbons = chords.map((chord) => {
    return {
      source: {
        startAngle: chord.source.startAngle,
        endAngle: chord.source.endAngle,
        radius: innerRadius,
      },
      target: {
        startAngle: chord.target.startAngle,
        endAngle: chord.target.endAngle,
        radius: innerRadius,
      },
    };
  });

  console.log(
    JSON.stringify(
      ribbons.map((r) => {
        return { path: ribbon(r) };
      })
    )
  );

  const [vegaSpec, setVegaSpec] = useState(BLANKSPEC);
  const [schemaInfo, setSchemaInfo] = useState(BLANKSCHEMA);
  //const [chartType, setChartType] = useState<string>("");
  const [specList, setSpecList] = useState<any[]>([]);

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

  const [selectedData, setSelectedData] = useState<Data | null>(null);

  const handleSelectedData = (data: Data) => {
    setVegaSpec(BLANKSPEC);
    setSelectedData(data);
    const payload = { schema: JSON.stringify(data) };
    axios
      .post("http://localhost:8080/api/v1/specs/specFromSchema", payload)
      .then((response) => {
        console.log(JSON.stringify(response.data));
        //setChartType(response.data.chartType);
        setSpecList(response.data.spec);
      });
    //.then((response) => setVegaSpec(response.data.spec[0]));
    //.then((response) => console.log(JSON.stringify(response.data.spec)));

    console.log(JSON.stringify(data));
  };

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
      {specList.map((spec: any, index: number) => (
        <button key={index} onClick={() => setVegaSpec(spec)}>
          {spec.description}
        </button>
      ))}
      <Vega spec={vegaSpec} actions={false} />
    </>
  );
}

export default App;
