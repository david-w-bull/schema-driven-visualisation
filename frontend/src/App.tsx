import { useState, useEffect, useRef } from "react";
import styled from "styled-components";
import axios from "axios";
import "./App.css";
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
import { categorizeCharts, swapKeyFields } from "./utils/chartUtils";
import EntityList from "./components/EntityList";
import DatabaseSelector from "./components/DatabaseSelector";
import CardinalitySettings from "./components/CardinalitySettings";
import SQLEditor from "./components/SQLEditor";
import SQLSubmitButton from "./components/SQLSubmitButton";
import DataTable from "./components/DataTable";
import VisualisationButtonsGroup from "./components/VisualisationButtonsGroup";
import Split from "react-split";
import {
  Radio,
  RadioChangeEvent,
  FloatButton,
  Drawer,
  Alert,
  Divider,
  Avatar,
  List,
} from "antd";
import { QuestionCircleOutlined, SettingOutlined } from "@ant-design/icons";
import ChartDisplayModal from "./components/ChartDisplayModal";
import manyManyIcon from "./assets/many-to-many.svg";
import oneManyIcon from "./assets/one-to-many.svg";

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
      })
      .catch((error) => {
        console.error("There was an error!", error);
      });
  };

  const [schemaChartTypes, setSchemaChartTypes] = useState<string[]>([]);
  const [dataChartTypes, setDataChartTypes] = useState<string[]>([]);

  const [schemaRecommendedCharts, setSchemaRecommendedCharts] =
    useState<ChartRecommendations>(BLANKRECOMMENDATIONS);
  const [dataRecommendedCharts, setDataRecommendedCharts] =
    useState<ChartRecommendations>(BLANKRECOMMENDATIONS);

  const [selectedChart, setSelectedChart] = useState<string | null>(null);
  const [specList, setSpecList] = useState<any[]>([]);
  const [vizSchema, setVizSchema] = useState<VizSchema>(BLANKVIZSCHEMA);
  const [keyCardinality, setKeyCardinality] = useState<number>(0);

  const [cardinalityLimits, setCardinalityLimits] = useState<CardinalityLimits>(
    cardinalityLimitsData
  );

  const [radioSelect, setRadioSelect] = useState("SQL");
  const [radioEnabled, setRadioEnabled] = useState(false);

  const [radioRecommendations, setRadioRecommendations] = useState("Schema");

  const handleRadioSelect = (e: RadioChangeEvent) => {
    setRadioSelect(e.target.value);
  };

  const handleChangeRecommendations = (e: RadioChangeEvent) => {
    setRadioRecommendations(e.target.value);
  };

  const handleCardinalityUpdate = (key: string, value: number) => {
    const newCardinalityLimits = {
      ...cardinalityLimits,
      [key]: value,
    };
    setCardinalityLimits(newCardinalityLimits);

    let recommendedCharts = categorizeCharts(
      schemaChartTypes,
      newCardinalityLimits,
      keyCardinality
    );

    setSchemaRecommendedCharts(recommendedCharts);
  };

  const handleSelectedData = (data: Data) => {
    setVegaSpec(BLANKSPEC);
    setVegaActionMenu(false);
    setSqlCode("");
    axios
      // the 'data' payload is a DatabaseSchema object filtered based on user selections
      .post("http://localhost:8080/api/v1/specs/specFromSchema", data)
      .then((response) => {
        console.log(response.data);
        console.log(JSON.stringify(response.data));
        let returnedVizSchema = response.data.vizSchema;
        if (
          returnedVizSchema.dataRelationship &&
          returnedVizSchema.dataRelationship == "ONETOMANY"
        ) {
          if (
            returnedVizSchema.keyOneCardinality >
            returnedVizSchema.keyTwoCardinality
          ) {
            console.log("Swapping");
            console.log(returnedVizSchema);
            returnedVizSchema = swapKeyFields(returnedVizSchema);
            console.log(returnedVizSchema);
          }
        }

        setVizSchema(returnedVizSchema);
        setRadioEnabled(true);
        returnedVizSchema.sqlQuery && setSqlCode(returnedVizSchema.sqlQuery);
        returnedVizSchema.keyCardinality &&
          setKeyCardinality(returnedVizSchema.keyCardinality);
        returnedVizSchema.chartTypes &&
          setSchemaChartTypes(returnedVizSchema.chartTypes);
        returnedVizSchema.dataChartTypes &&
          setDataChartTypes(returnedVizSchema.dataChartTypes);

        let schemaRecommendedCharts = categorizeCharts(
          returnedVizSchema.chartTypes,
          cardinalityLimits,
          returnedVizSchema.keyCardinality
        );

        let dataRecommendedCharts = categorizeCharts(
          returnedVizSchema.dataChartTypes,
          cardinalityLimits,
          returnedVizSchema.keyCardinality
        );

        setSchemaRecommendedCharts(schemaRecommendedCharts);
        setDataRecommendedCharts(dataRecommendedCharts);

        // Copy the reference to the VizSchema data into any Vega specs as 'rawData'.
        response.data.specs.forEach((specItem: any) => {
          specItem.data.forEach((dataItem: any) => {
            if (dataItem.name === "rawData") {
              dataItem.values = returnedVizSchema.dataset;
            }
          });
        });
        setSpecList(response.data.specs);
      });

    console.log(data);
  };

  const [isModalOpen, setIsModalOpen] = useState(false);

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

  const [errorMessages, setErrorMessages] = useState<string[]>([]);
  const [showErrors, setShowErrors] = useState(false);

  const handleBackendErrors = (vizSchema: VizSchema) => {
    if (vizSchema.type === "ERROR") {
      setErrorMessages(vizSchema.messages);
      setShowErrors(true);
      return true;
    }
    if (
      !vizSchema.dataset ||
      (vizSchema.dataset && vizSchema.dataset.length === 0)
    ) {
      setErrorMessages(["Your query returned no data"]);
      setShowErrors(true);
      return true;
    }

    return false;
  };

  const handleSqlSubmit = () => {
    // Clear previous alerts
    setShowErrors(false);

    const updatedVizSchema = {
      ...vizSchema,
      sqlQuery: sqlCode,
      chartTypes: [],
      dataChartTypes: [],
    };

    axios
      .post(
        "http://localhost:8080/api/v1/specs/updateSqlData",
        updatedVizSchema
      )
      .then((response) => {
        console.log(response.data);
        if (handleBackendErrors(response.data)) {
          return;
        }
        setVizSchema(response.data);
        console.log(response.data);
        setSchemaChartTypes(response.data.chartTypes);
        setDataChartTypes(response.data.dataChartTypes);
        setKeyCardinality(response.data.keyCardinality);

        let schemaRecommendedCharts = categorizeCharts(
          response.data.chartTypes,
          cardinalityLimits,
          response.data.keyCardinality
        );

        let dataRecommendedCharts = categorizeCharts(
          response.data.dataChartTypes,
          cardinalityLimits,
          response.data.keyCardinality
        );

        setSchemaRecommendedCharts(schemaRecommendedCharts);
        setDataRecommendedCharts(dataRecommendedCharts);
        updateRawDataInSpecList(response.data.dataset);
      });
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
              display: "flex",
              flexDirection: "column",
            }}
          >
            <Radio.Group
              onChange={handleRadioSelect}
              defaultValue="SQL"
              // buttonStyle="solid"
              size="large"
              style={{ marginBottom: "20px" }}
              disabled={!radioEnabled}
            >
              <Radio.Button value="SQL">SQL</Radio.Button>
              <Radio.Button value="Visualisations">Visualisations</Radio.Button>
              <Radio.Button value="Schema">Schema</Radio.Button>
            </Radio.Group>

            {radioSelect === "SQL" && (
              <div>
                <div>
                  <SQLEditor value={sqlCode} onChange={setSqlCode} />
                  <SQLSubmitButton
                    sqlCode={sqlCode}
                    radioEnabled={radioEnabled}
                    handleSqlSubmit={handleSqlSubmit}
                  />
                </div>
                {typeof vizSchema.exampleData != "undefined" && (
                  <DataTable data={vizSchema.exampleData}></DataTable>
                )}
              </div>
            )}

            {radioSelect === "Visualisations" && (
              <>
                {dataChartTypes && dataChartTypes.length > 0 && (
                  <Radio.Group
                    onChange={handleChangeRecommendations}
                    value={radioRecommendations}
                  >
                    <Radio value={"Schema"}>Schema</Radio>
                    <Radio value={"Data"}>Data</Radio>
                  </Radio.Group>
                )}
                <VisualisationButtonsGroup
                  vizSchemaType={vizSchema.type}
                  chartTypes={
                    radioRecommendations == "Data"
                      ? dataRecommendedCharts
                      : schemaRecommendedCharts
                  }
                  specList={specList}
                  cardinalityLimits={cardinalityLimits}
                  keyCardinality={keyCardinality}
                  setVegaSpec={setVegaSpec}
                  setVegaActionMenu={setVegaActionMenu}
                  setSelectedChart={setSelectedChart}
                  setIsModalOpen={setIsModalOpen}
                />
              </>
            )}
            {radioSelect === "Schema" && (
              <div
                style={{
                  display: "flex",
                  flexDirection: "column",
                  paddingTop: "20px",
                }}
              >
                <Header>Visualisation Schema</Header>
                <SubHeader>
                  Information about the matched visualisation pattern
                </SubHeader>
                <StyledDivider />
                <List itemLayout="horizontal">
                  {vizSchema.keyOne && (
                    <List.Item>
                      <List.Item.Meta
                        avatar={
                          <Avatar
                            size="large"
                            style={{
                              backgroundColor: "#fde3cf",
                              color: "#f56a00",
                            }}
                          >
                            K1
                          </Avatar>
                        }
                        title={
                          <>
                            <div>Key One</div>
                            <div style={{ fontWeight: 300 }}>
                              {vizSchema.keyOneAlias}
                            </div>
                          </>
                        }
                      />
                    </List.Item>
                  )}
                  {vizSchema.keyTwo && (
                    <List.Item>
                      <List.Item.Meta
                        avatar={
                          <Avatar
                            size="large"
                            style={{
                              backgroundColor: "#fde3cf",
                              color: "#f56a00",
                            }}
                          >
                            K2
                          </Avatar>
                        }
                        title={
                          <>
                            <div>Key Two</div>
                            <div style={{ fontWeight: 300 }}>
                              {vizSchema.keyTwoAlias}
                            </div>
                          </>
                        }
                      />
                    </List.Item>
                  )}
                  {vizSchema.scalarOne && (
                    <List.Item>
                      <List.Item.Meta
                        avatar={
                          <Avatar
                            size="large"
                            style={{
                              backgroundColor: "#fde3cf",
                              color: "#f56a00",
                            }}
                          >
                            A1
                          </Avatar>
                        }
                        title={
                          <>
                            <div>Scalar One</div>
                            <div style={{ fontWeight: 300 }}>
                              {vizSchema.scalarOneAlias}
                            </div>
                          </>
                        }
                      />
                    </List.Item>
                  )}
                </List>
                <div>
                  <div style={{ height: "40px" }}></div>
                  <Header>Schema Relationship</Header>
                  <SubHeader>
                    Based on database metadata. Indicative of how data can be
                    related between your selected tables
                  </SubHeader>
                  <div>
                    {vizSchema.type == "ONETOMANY" ? (
                      <RelationshipIcon src={oneManyIcon}></RelationshipIcon>
                    ) : (
                      <RelationshipIcon src={manyManyIcon}></RelationshipIcon>
                    )}
                  </div>
                  <div style={{ height: "40px" }}></div>
                  <Header>Data Relationship</Header>
                  <SubHeader>
                    The actual relationship exhibited by the data returned by
                    your query. May not hold for all queries.
                  </SubHeader>
                  <div>
                    {vizSchema.dataRelationship == "ONETOMANY" ? (
                      <RelationshipIcon src={oneManyIcon}></RelationshipIcon>
                    ) : (
                      <RelationshipIcon src={manyManyIcon}></RelationshipIcon>
                    )}
                  </div>
                </div>
              </div>
            )}
          </div>
        </Split>
      </div>
      {isModalOpen && (
        <ChartDisplayModal
          setIsModalOpen={setIsModalOpen}
          selectedChart={selectedChart}
          vegaSpec={vegaSpec}
          vegaActionMenu={vegaActionMenu}
          vizSchema={vizSchema}
        ></ChartDisplayModal>
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
      {showErrors && (
        <TopWarningAlert
          className="top-warning-alert"
          message="Error"
          description={errorMessages.join("\n")}
          type="error"
          showIcon
          closable
          onClose={() => {
            setShowErrors(false);
          }}
        />
      )}
    </>
  );
}

export default App;

const TopWarningAlert = styled(Alert)`
  position: fixed;
  top: 0;
  left: 50%;
  transform: translateX(-50%);
  z-index: 2000;
  width: 40%;
`;

const StyledDivider = styled(Divider)`
  margin: 2px 0px 0px 0px;
`;

const Header = styled.h1`
  font-size: 30px;
  font-family: "Roboto", "Helvetica", "Arial", sans-serif;
  font-weight: 500;
  margin: 0px;
`;

const SubHeader = styled.p`
  font-size: 16px;
  font-family: "Roboto", "Helvetica", "Arial", sans-serif;
  font-weight: 300;
  margin: 0px;
`;

const RelationshipIcon = styled.img`
    width: 70px;
    }
`;
