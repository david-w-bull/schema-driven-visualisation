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
import ChordDiagram from "./components/charts/ChordDiagram";
import ChordDiagramTest from "./components/charts/ChordDiagramTest";
import GroupedBar from "./components/charts/GroupedBar";
import StackedBar from "./components/charts/StackedBar";
import TreeMap from "./components/charts/TreeMap";
import ScatterPlot from "./components/charts/ScatterPlot";
import * as d3 from "d3";
import SQLEditor from "./components/SQLEditor";
import DataTable from "./components/DataTable";
import VisualisationButtons from "./components/VisualisationButtons";
import styled from "styled-components";
import Split from "react-split";
import { Button as AntButton, Radio, RadioChangeEvent } from "antd";
import Button from "@mui/material/Button";
import AddchartIcon from "@mui/icons-material/Addchart";
import StorageIcon from "@mui/icons-material/Storage";
import CachedIcon from "@mui/icons-material/Cached";

function App() {
  const [vegaSpec, setVegaSpec] = useState(BLANKSPEC);
  const [vegaActionMenu, setVegaActionMenu] = useState(false);
  const [schemaInfo, setSchemaInfo] = useState(BLANKSCHEMA);
  const [schemaConnection, setSchemaConnection] = useState("No connection");

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
        setSchemaConnection(response.data.connectionString);
        console.log(schemaConnection);
      })
      .catch((error) => {
        console.error("There was an error!", error);
      });
  };

  const [chartTypes, setChartTypes] = useState<any>(null);
  const [selectedChart, setSelectedChart] = useState<string | null>(null);
  const [specList, setSpecList] = useState<any[]>([]);
  const [vizSchema, setVizSchema] = useState<VizSchema>(BLANKVIZSCHEMA);

  const handleSelectedData = (data: Data) => {
    setVegaSpec(BLANKSPEC);
    setVegaActionMenu(false);
    axios
      // the 'data' payload is a DatabaseSchema object filtered based on user selections
      .post("http://localhost:8080/api/v1/specs/specFromSchema", data)
      .then((response) => {
        console.log(JSON.stringify(response.data));
        setChartTypes(response.data.vizSchema.allChartTypes);
        setVizSchema(response.data.vizSchema);
        setSqlCode(response.data.vizSchema.sqlQuery);

        // Copy the reference to the VizSchema data into any Vega specs as 'rawData'.
        response.data.specs.forEach((specItem: any) => {
          specItem.data.forEach((dataItem: any) => {
            if (dataItem.name === "rawData") {
              dataItem.values = response.data.vizSchema.dataset;
            }
          });
        });
        setSpecList(response.data.specs);
      });

    console.log(JSON.stringify(data));
  };

  const [isModalOpen, setIsModalOpen] = useState(false);

  const renderChartComponent = (chartType: string) => {
    switch (chartType) {
      case "Chord Diagram":
        return <ChordDiagramTest vizSchema={vizSchema} />;
      case "Grouped Bar Chart":
        return <GroupedBar vizSchema={vizSchema} />;
      case "Stacked Bar Chart":
        return <StackedBar vizSchema={vizSchema} />;
      case "Scatter Plot":
        return <ScatterPlot vizSchema={vizSchema} />;
      default:
        return null;
    }
  };

  const updateRawDataInSpecList = (newVizSchemaDataset: any) => {
    const updatedSpecList = specList.map((specItem) => {
      const updatedData = specItem.data.map((dataItem: any) => {
        if (dataItem.name === "rawData") {
          return { ...dataItem, values: newVizSchemaDataset };
        }
        return dataItem;
      });
      return { ...specItem, data: updatedData };
    });

    setSpecList(updatedSpecList);
  };

  const [sqlCode, setSqlCode] = useState("");

  const handleSqlSubmit = () => {
    // Add code to revert to previous schema if the returned vizSchema is of type NONE or ERROR
    const updatedVizSchema = {
      ...vizSchema,
      sqlQuery: sqlCode,
      allChartTypes: null,
    };

    axios
      .post(
        "http://localhost:8080/api/v1/specs/updateSqlData",
        updatedVizSchema
      )
      .then((response) => {
        setVizSchema(response.data);
        console.log(response.data);
        setChartTypes(response.data.allChartTypes);
        updateRawDataInSpecList(response.data.dataset);
      });
  };

  const [radioSelect, setRadioSelect] = useState("SQL");

  const handleRadioSelect = (e: RadioChangeEvent) => {
    setRadioSelect(e.target.value);
  };

  return (
    <>
      <div style={{ display: "flex", height: "100vh", width: "100%" }}>
        <Split
          className="split"
          sizes={[20, 80]}
          minSize={[350, 700]}
          expandToMin={false}
          gutterSize={10}
          gutterAlign="center"
          snapOffset={30}
          dragInterval={1}
          direction="horizontal"
          cursor="col-resize"
          style={{ width: "100%" }}
        >
          <div
            style={{
              overflowY: "auto",
              display: "flex",
              flexDirection: "column",
            }}
          >
            <DatabaseSelector onSelectDatabase={handleSelectDatabase} />
            <EntityList
              data={schemaInfo}
              onSelectedData={handleSelectedData}
            ></EntityList>
          </div>

          <div
            style={{
              padding: "5%",
              backgroundImage:
                "linear-gradient(to right bottom, #ffffff, #f5f5f5, #eaeaeb, #e0e0e2, #d6d6d8)",
            }}
          >
            <Radio.Group
              onChange={handleRadioSelect}
              defaultValue="SQL"
              // buttonStyle="solid"
              size="large"
              style={{ marginBottom: "20px" }}
            >
              <Radio.Button value="SQL">SQL</Radio.Button>
              <Radio.Button value="Visualisations">Visualisations</Radio.Button>
              <Radio.Button value="Schema">Schema</Radio.Button>
            </Radio.Group>

            {radioSelect === "SQL" && (
              <div>
                <div>
                  <SQLEditor value={sqlCode} onChange={setSqlCode} />
                  <Button
                    variant="contained"
                    endIcon={<CachedIcon />}
                    onClick={() => {
                      handleSqlSubmit();
                    }}
                    style={{
                      margin: "5px 20px 20px 0px",
                      boxShadow: "2px 2px 5px rgba(0, 0, 0, 0.3)",
                    }}
                  >
                    Update SQL
                  </Button>
                  <p>{vizSchema.dataRelationship}</p>
                </div>
                {typeof vizSchema.exampleData != "undefined" && (
                  <DataTable data={vizSchema.exampleData}></DataTable>
                )}
              </div>
            )}

            {radioSelect === "Visualisations" && (
              <div
                style={{
                  display: "flex",
                  flexDirection: "column",
                }}
              >
                <h1>Recommended</h1>
                <VisualisationButtons
                  chartTypes={chartTypes.Recommended}
                  specList={specList}
                  setVegaSpec={setVegaSpec}
                  setVegaActionMenu={setVegaActionMenu}
                  setSelectedChart={setSelectedChart}
                  setIsModalOpen={setIsModalOpen}
                />
                <h1>Possible</h1>
                <VisualisationButtons
                  chartTypes={chartTypes.Possible}
                  specList={specList}
                  setVegaSpec={setVegaSpec}
                  setVegaActionMenu={setVegaActionMenu}
                  setSelectedChart={setSelectedChart}
                  setIsModalOpen={setIsModalOpen}
                />
                <h1>Other</h1>
                <VisualisationButtons
                  chartTypes={chartTypes.Other}
                  specList={specList}
                  setVegaSpec={setVegaSpec}
                  setVegaActionMenu={setVegaActionMenu}
                  setSelectedChart={setSelectedChart}
                  setIsModalOpen={setIsModalOpen}
                />
              </div>
            )}
          </div>
        </Split>
      </div>
      {isModalOpen && (
        <ModalContainer>
          <ModalBackdrop />
          <ModalContent>
            <CloseIcon onClick={() => setIsModalOpen(false)}>×</CloseIcon>
            {selectedChart ? (
              renderChartComponent(selectedChart)
            ) : (
              <Vega spec={vegaSpec} actions={vegaActionMenu} />
            )}
          </ModalContent>
        </ModalContainer>
      )}
    </>
  );
}

export default App;

const ModalContainer = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
`;

const ModalBackdrop = styled.div`
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
`;

const ModalContent = styled.div`
  position: relative;
  background: #fff;
  padding: 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  width: 80%;
  height: 80%;
  z-index: 1001;
`;

const CloseIcon = styled.div`
  position: absolute;
  top: 10px;
  right: 10px;
  cursor: pointer;
  font-size: 24px;
  color: #000;
`;
