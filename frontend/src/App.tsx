import { useState, useEffect, useRef } from "react";
import axios from "axios";
import "./App.css";
import { Vega, VegaLite, VisualizationSpec } from "react-vega";
import {
  Data,
  VizSchema,
  CardinalityLimits,
  ChartRecommendations,
} from "./types";
import {
  BLANKSPEC,
  BLANKSCHEMA,
  BLANKVIZSCHEMA,
  BLANKRECOMMENDATIONS,
} from "./constants";
import cardinalityLimitsData from "./cardinalityLimitsData";
import { categorizeCharts } from "./utils/chartUtils";
import EntityList from "./components/EntityList";
import DatabaseSelector from "./components/DatabaseSelector";
import CardinalitySettings from "./components/CardinalitySettings";
import ERDiagram from "./components/ERDiagram";
import ChordDiagramTest from "./components/charts/ChordDiagramTest";
import GroupedBar from "./components/charts/GroupedBar";
import StackedBar from "./components/charts/StackedBar";
import TreeMap from "./components/charts/TreeMap";
import ScatterPlot from "./components/charts/ScatterPlot";
import SQLEditor from "./components/SQLEditor";
import DataTable from "./components/DataTable";
import VisualisationButtonsGroup from "./components/VisualisationButtonsGroup";
import styled from "styled-components";
import Split from "react-split";
import { Radio, RadioChangeEvent, FloatButton, Drawer } from "antd";
import { QuestionCircleOutlined, SettingOutlined } from "@ant-design/icons";
import Button from "@mui/material/Button";
import CachedIcon from "@mui/icons-material/Cached";

function App() {
  const [vegaSpec, setVegaSpec] = useState(BLANKSPEC);
  const [vegaActionMenu, setVegaActionMenu] = useState(false);
  const [schemaInfo, setSchemaInfo] = useState(BLANKSCHEMA);
  const [schemaConnection, setSchemaConnection] = useState("No connection");

  const handleSelectDatabase = (selectedValue: string) => {
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

  const [chartTypes, setChartTypes] = useState<string[]>([]);
  const [recommendedCharts, setRecommendedCharts] =
    useState<ChartRecommendations>(BLANKRECOMMENDATIONS);
  const [selectedChart, setSelectedChart] = useState<string | null>(null);
  const [specList, setSpecList] = useState<any[]>([]);
  const [vizSchema, setVizSchema] = useState<VizSchema>(BLANKVIZSCHEMA);
  const [keyCardinality, setKeyCardinality] = useState<number>(0);

  const [cardinalityLimits, setCardinalityLimits] = useState<CardinalityLimits>(
    cardinalityLimitsData
  );

  const handleCardinalityUpdate = (key: string, value: number) => {
    const newCardinalityLimits = {
      ...cardinalityLimits,
      [key]: value,
    };
    setCardinalityLimits(newCardinalityLimits);

    let recommendedCharts = categorizeCharts(
      chartTypes,
      newCardinalityLimits,
      keyCardinality
    );

    setRecommendedCharts(recommendedCharts);
  };

  const handleSelectedData = (data: Data) => {
    setVegaSpec(BLANKSPEC);
    setVegaActionMenu(false);
    axios
      // the 'data' payload is a DatabaseSchema object filtered based on user selections
      .post("http://localhost:8080/api/v1/specs/specFromSchema", data)
      .then((response) => {
        console.log(JSON.stringify(response.data));
        setVizSchema(response.data.vizSchema);
        setSqlCode(response.data.vizSchema.sqlQuery);
        setKeyCardinality(response.data.vizSchema.keyCardinality);
        setChartTypes(response.data.vizSchema.chartTypes);

        let recommendedCharts = categorizeCharts(
          response.data.vizSchema.chartTypes,
          cardinalityLimits,
          response.data.vizSchema.keyCardinality
        );

        setRecommendedCharts(recommendedCharts);

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
      chartTypes: [],
    };

    axios
      .post(
        "http://localhost:8080/api/v1/specs/updateSqlData",
        updatedVizSchema
      )
      .then((response) => {
        setVizSchema(response.data);
        console.log(response.data);
        setChartTypes(response.data.chartTypes);
        setKeyCardinality(response.data.keyCardinality);
        let recommendedCharts = categorizeCharts(
          response.data.chartTypes,
          cardinalityLimits,
          response.data.keyCardinality
        );

        setRecommendedCharts(recommendedCharts);
        updateRawDataInSpecList(response.data.dataset);
      });
  };

  const [radioSelect, setRadioSelect] = useState("SQL");

  const handleRadioSelect = (e: RadioChangeEvent) => {
    setRadioSelect(e.target.value);
  };

  const [settingsDrawIsOpen, setSettingsDrawIsOpen] = useState(false);

  const showSettingsDrawer = () => {
    setSettingsDrawIsOpen(true);
  };

  const closeSettingsDrawer = () => {
    setSettingsDrawIsOpen(false);
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
              <VisualisationButtonsGroup
                chartTypes={recommendedCharts}
                specList={specList}
                cardinalityLimits={cardinalityLimits}
                keyCardinality={keyCardinality}
                setVegaSpec={setVegaSpec}
                setVegaActionMenu={setVegaActionMenu}
                setSelectedChart={setSelectedChart}
                setIsModalOpen={setIsModalOpen}
              />
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
      <Drawer
        title="Cardinality Limits"
        placement="right"
        closable={true}
        onClose={closeSettingsDrawer}
        open={settingsDrawIsOpen}
      >
        <CardinalitySettings
          data={cardinalityLimits}
          onDataUpdate={handleCardinalityUpdate}
        />
      </Drawer>
      <FloatButton.Group shape="square" style={{ right: 24 }}>
        <FloatButton icon={<QuestionCircleOutlined />} />
        <FloatButton icon={<SettingOutlined />} onClick={showSettingsDrawer} />
      </FloatButton.Group>
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
