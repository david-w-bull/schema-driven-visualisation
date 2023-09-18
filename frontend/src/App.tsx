import { useState, useEffect, useRef } from "react";
import styled from "styled-components";
import axios from "axios";
import "./App.css";
import cardinalityLimitsData from "./cardinalityLimitsData";
import examplesData from "./examplesData";
import EntityList from "./components/EntityList";
import DatabaseSelector from "./components/DatabaseSelector";
import CardinalitySettings from "./components/CardinalitySettings";
import SQLEditor from "./components/SQLEditor";
import SQLSubmitButton from "./components/SQLSubmitButton";
import DataTable from "./components/DataTable";
import VisualisationButtonsGroup from "./components/VisualisationButtonsGroup";
import Split from "react-split";
import ChartDisplayModal from "./components/ChartDisplayModal";
import VizSchemaInfoDisplay from "./components/VizSchemaInfoDisplay";
import LoadExampleButton from "./components/LoadExampleButton";
import { QuestionCircleOutlined, SettingOutlined } from "@ant-design/icons";
import { categorizeCharts, copyJson, swapKeyFields } from "./utils/chartUtils";
import aboutText from "./userInformation";
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
import {
  Radio,
  RadioChangeEvent,
  FloatButton,
  Drawer,
  Alert,
  Divider,
  Spin,
} from "antd";

function App() {
  // const apiUrl = "/api/v1"; // Production API URL stem
  const apiUrl = "http://localhost:8080/api/v1"; // Dev API URL stem

  /* State variables managing Vega visualisations */
  const [vegaSpec, setVegaSpec] = useState(BLANKSPEC);
  const [vegaActionMenu, setVegaActionMenu] = useState(false);
  const [specList, setSpecList] = useState<any[]>([]);

  /* State variables managing database schema */
  const [schemaInfo, setSchemaInfo] = useState(BLANKSCHEMA);
  const [selectedDatabase, setSelectedDatabase] = useState(
    "64e4b803fe3299654b1739bf"
  );

  /* State variables managing UI components and their visibility */
  const [radioSelect, setRadioSelect] = useState("SQL");
  const [radioEnabled, setRadioEnabled] = useState(false);
  const [radioRecommendations, setRadioRecommendations] = useState("Schema");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [settingsDrawIsOpen, setSettingsDrawIsOpen] = useState(false);
  const [helpDrawerIsOpen, setHelpDrawerIsOpen] = useState(false);
  const [showErrors, setShowErrors] = useState(false);
  const [errorMessages, setErrorMessages] = useState<string[]>([]);
  const [loading, setLoading] = useState(false);

  /* State variables managing VizSchema information and user queries */
  const [vizSchema, setVizSchema] = useState<VizSchema>(BLANKVIZSCHEMA);
  const [vizPayloadId, setVizPayloadId] = useState("");
  const [selectedChart, setSelectedChart] = useState<string | null>(null);
  const [keyCardinality, setKeyCardinality] = useState<number>(0);
  const [keyOneCardinality, setKeyOneCardinality] = useState<number>(0);
  const [schemaChartTypes, setSchemaChartTypes] = useState<string[]>([]);
  const [dataChartTypes, setDataChartTypes] = useState<string[]>([]);
  const [sqlCode, setSqlCode] = useState("");

  /* State variables managing visualisation recommendations */
  const [schemaRecommendedCharts, setSchemaRecommendedCharts] =
    useState<ChartRecommendations>(BLANKRECOMMENDATIONS);
  const [dataRecommendedCharts, setDataRecommendedCharts] =
    useState<ChartRecommendations>(BLANKRECOMMENDATIONS);
  const [cardinalityLimits, setCardinalityLimits] = useState<CardinalityLimits>(
    cardinalityLimitsData
  );

  /**
   * Fetches stored schema information from MongoDB
   * @param newValue  A string representing the database UUID
   */
  const handleSelectDatabase = (newValue: string) => {
    setSelectedDatabase(newValue);
    axios
      .get(`${apiUrl}/schemas/` + newValue)
      .then((response) => {
        console.log(response.data);
        setSchemaInfo(response.data);
      })
      .catch((error) => {
        console.error("Error fetching database schema information: ", error);
      });
  };

  /**
   * Handler for user interaction with radio selections for page browsing
   */
  const handleRadioSelect = (e: RadioChangeEvent) => {
    setRadioSelect(e.target.value);
  };

  /**
   * Handler for user interaction with radio selection for data vs. schema visualisation
   */
  const handleChangeRecommendations = (e: RadioChangeEvent) => {
    setRadioRecommendations(e.target.value);
  };

  /**
   * Regenerates chart recommendations in response to updated cardinality limits
   * @param key {string} The chart type which the cardinality refers to
   * @param value {number} The new cardinality limit to be applied
   */
  const handleCardinalityUpdate = (key: string, value: number) => {
    const newCardinalityLimits = {
      ...cardinalityLimits,
      [key]: value,
    };
    setCardinalityLimits(newCardinalityLimits);

    let recommendedCharts = categorizeCharts(
      schemaChartTypes,
      newCardinalityLimits,
      keyCardinality,
      keyOneCardinality
    );

    setSchemaRecommendedCharts(recommendedCharts);
  };

  /**
   * Sends a DatabaseSchema object to the backend, filtered based on user selections.
   * Returns a full VizSpecPayload object which is used to populate all UI state.
   * @param data A DatabaseSchema object, filtered based on user selections
   */
  const handleSelectedData = (data: Data) => {
    setLoading(true);
    setVegaSpec(BLANKSPEC);
    setVegaActionMenu(false);
    setSqlCode("");
    setShowErrors(false);
    setRadioRecommendations("Schema");
    axios
      .post(`${apiUrl}/specs/specFromSchema`, data)
      .then((response) => {
        console.log(response.data);
        let returnedVizSchema = response.data.vizSchema;
        if (
          returnedVizSchema.dataRelationship &&
          returnedVizSchema.dataRelationship == "ONETOMANY"
        ) {
          if (
            returnedVizSchema.keyOneCardinality >
            returnedVizSchema.keyTwoCardinality
          ) {
            returnedVizSchema = swapKeyFields(returnedVizSchema);
          }
        }

        setVizSchema(returnedVizSchema);
        setVizPayloadId(response.data.vizId);
        if (
          returnedVizSchema.type == "NONE" ||
          returnedVizSchema.type == "ERROR"
        ) {
          setSchemaChartTypes([]);
          setDataChartTypes([]);
          setSchemaRecommendedCharts(BLANKRECOMMENDATIONS);
          setDataRecommendedCharts(BLANKRECOMMENDATIONS);
          setErrorMessages([
            "Your selected fields did not match any visualisation schema patterns",
          ]);
          setLoading(false);
          setShowErrors(true);
          return;
        }

        setRadioEnabled(true);
        returnedVizSchema.sqlQuery && setSqlCode(returnedVizSchema.sqlQuery);
        returnedVizSchema.keyCardinality &&
          setKeyCardinality(returnedVizSchema.keyCardinality);
        returnedVizSchema.keyOneCardinality &&
          setKeyOneCardinality(returnedVizSchema.keyOneCardinality);
        returnedVizSchema.chartTypes &&
          setSchemaChartTypes(returnedVizSchema.chartTypes);
        returnedVizSchema.dataChartTypes &&
          setDataChartTypes(returnedVizSchema.dataChartTypes);

        let schemaRecommendedCharts = categorizeCharts(
          returnedVizSchema.chartTypes,
          cardinalityLimits,
          returnedVizSchema.keyCardinality,
          returnedVizSchema.keyOneCardinality
        );

        let dataRecommendedCharts = categorizeCharts(
          returnedVizSchema.dataChartTypes,
          cardinalityLimits,
          returnedVizSchema.keyCardinality,
          returnedVizSchema.keyOneCardinality
        );

        setSchemaRecommendedCharts(schemaRecommendedCharts);
        setDataRecommendedCharts(dataRecommendedCharts);

        // Copy the reference to the VizSchema data into any Vega specs as the 'rawData' dataset
        response.data.specs.forEach((specItem: any) => {
          specItem.data.forEach((dataItem: any) => {
            if (dataItem.name === "rawData") {
              dataItem.values = copyJson(returnedVizSchema.dataset);
            }
          });
        });
        setSpecList(response.data.specs);
        setLoading(false);
      })
      .catch((error) => {
        setErrorMessages([
          "There was an error processing your selected fields. Please try again later.",
        ]);
        setLoading(false);
        setShowErrors(true);
        console.error("Error creating VizSchema from selected data: ", error);
      });
  };

  /**
   * A helper function to update the dataset used by Vega specifications when data updates
   * This may be in response to a user-generated SQL query, for example
   * @param newVizSchemaDataset {VizSchema} A JSON object representing the VizSchema
   * @param loadedSpecList {VegaSpec[]} A list of Vega specs. Only utilised during loadExamples function.
   */
  const updateRawDataInSpecList = (
    newVizSchemaDataset: any,
    loadedSpecList?: any[]
  ) => {
    // When examples are loaded the specList comes from MongoDB rather than user selections
    const specListToUpdate = loadedSpecList ? loadedSpecList : specList;
    const updatedSpecList = specListToUpdate.map((specItem) => {
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

  /**
   * A helper function to identify whether user SQL updates have changed field aliases (currently not supported)
   * @param vizSchema {VizSchema} The VizSchema before user updates
   * @param updatedVizSchema {VizSchema} The VizSchema after user updates
   * @returns
   */
  function hasAliasChanged(
    vizSchema: VizSchema,
    updatedVizSchema: VizSchema
  ): boolean {
    if (!vizSchema.dataset || !updatedVizSchema.dataset) {
      return false;
    }

    const vizSchemaFields = new Set(Object.keys(vizSchema.dataset[0]));
    const updatedVizSchemaFields = new Set(
      Object.keys(updatedVizSchema.dataset[0])
    );

    // Check if every key in vizSchemaFields exists in updatedVizSchemaFields
    const vizIsSubsetOfUpdated = [...vizSchemaFields].every((element) =>
      updatedVizSchemaFields.has(element)
    );

    // Check if every key in updatedVizSchemaFields exists in vizSchemaFields
    const updatedIsSubsetOfViz = [...updatedVizSchemaFields].every((element) =>
      vizSchemaFields.has(element)
    );

    return !vizIsSubsetOfUpdated && !updatedIsSubsetOfViz;
  }

  /**
   * A helper function to catch and handle common error, including user notifications
   * @param updatedVizSchema {VizShema} The VizSchema object to check for errors
   *  This is compared to the current VizSchema that is held in state.
   * @returns boolean
   */
  const handleBackendErrors = (updatedVizSchema: VizSchema) => {
    if (updatedVizSchema.type === "ERROR") {
      setErrorMessages(updatedVizSchema.messages);
      setLoading(false);
      setShowErrors(true);
      return true;
    }
    if (
      !updatedVizSchema.dataset ||
      (updatedVizSchema.dataset && updatedVizSchema.dataset.length === 0)
    ) {
      setErrorMessages(["Your query returned no data"]);
      setLoading(false);
      setShowErrors(true);
      return true;
    }

    if (hasAliasChanged(vizSchema, updatedVizSchema)) {
      setErrorMessages([
        "Field aliases cannot be changed as part of SQL updates",
      ]);
      setLoading(false);
      setShowErrors(true);
      return true;
    }
    return false;
  };

  /**
   * Sends user SQL updates to the backend to regenerate all VizSchema information and UI state
   * @param customSql {string} Optional parameter for loading example SQL code from a file rather than from state
   * @param loadedVizSchema {VizSchema} Optional parameter for loading VizSchema from MongoDB rather than from state
   * @param loadedSpecList {VegaSpec[]} Optional parameter for loading Vega specs from MongoDB rather than from state
   */
  const handleSqlSubmit = (
    customSql?: string,
    loadedVizSchema?: VizSchema,
    loadedSpecList?: any[]
  ) => {
    // Clear previous alerts
    setShowErrors(false);
    setLoading(true);

    const vizSchemaToUpdate =
      loadedVizSchema != undefined ? loadedVizSchema : vizSchema;

    const updatedVizSchema = {
      ...vizSchemaToUpdate,
      sqlQuery: customSql ? customSql : sqlCode,
      chartTypes: [],
      dataChartTypes: [],
    };

    axios
      .post(`${apiUrl}/specs/updateSqlData`, updatedVizSchema)
      .then((response) => {
        if (!loadedVizSchema) {
          if (handleBackendErrors(response.data)) {
            return;
          }
        }
        let returnedVizSchema = response.data;
        if (
          returnedVizSchema.dataRelationship &&
          returnedVizSchema.dataRelationship == "ONETOMANY"
        ) {
          if (
            returnedVizSchema.keyOneCardinality >
            returnedVizSchema.keyTwoCardinality
          ) {
            returnedVizSchema = swapKeyFields(returnedVizSchema);
          }
        }
        setVizSchema(returnedVizSchema);
        customSql && setSqlCode(customSql);
        setSchemaChartTypes(returnedVizSchema.chartTypes);
        setDataChartTypes(returnedVizSchema.dataChartTypes);
        setKeyCardinality(returnedVizSchema.keyCardinality);
        setKeyOneCardinality(returnedVizSchema.keyOneCardinality);

        let schemaRecommendedCharts = categorizeCharts(
          returnedVizSchema.chartTypes,
          cardinalityLimits,
          returnedVizSchema.keyCardinality,
          returnedVizSchema.keyOneCardinality
        );

        let dataRecommendedCharts = categorizeCharts(
          returnedVizSchema.dataChartTypes,
          cardinalityLimits,
          returnedVizSchema.keyCardinality,
          returnedVizSchema.keyOneCardinality
        );

        setSchemaRecommendedCharts(schemaRecommendedCharts);
        setDataRecommendedCharts(dataRecommendedCharts);

        if (!loadedSpecList) {
          updateRawDataInSpecList(copyJson(response.data.dataset));
        } else {
          updateRawDataInSpecList(
            copyJson(response.data.dataset),
            loadedSpecList
          );
        }
        setLoading(false);
      })
      .catch((error) => {
        setErrorMessages([
          "There was an error updating the SQL query. Please try again.",
        ]);
        setLoading(false);
        setShowErrors(true);
        console.error("Error updating SQL query: ", error);
      });
  };

  /**
   * A function to separate event handler calls of handleSqlSubmit()
   * as opposed to calls with custom parameters
   */
  const handleSqlSubmitButton = () => {
    handleSqlSubmit();
  };

  /**
   * Functions to manage the state of the UI setting drawer
   */
  const showSettingsDrawer = () => {
    setHelpDrawerIsOpen(false);
    setSettingsDrawIsOpen(true);
  };

  const closeSettingsDrawer = () => {
    setSettingsDrawIsOpen(false);
  };

  const showHelpDrawer = () => {
    setSettingsDrawIsOpen(false);
    setHelpDrawerIsOpen(true);
  };

  const closeHelpDrawer = () => {
    setHelpDrawerIsOpen(false);
  };

  /**
   * Handles loading examples from the example buttons provided in the UI
   * @param databaseId {string} A UUID for the target database in MongoDB
   * @param databaseName {string} The name of the target database
   * @param attributeIds {number[]} A list of attributeIds from the Entity/Relationship object, used to select relevant attribute checkboxes
   * @param vizId {string} A UUID for the stored VizSpecPayload in MongoDB
   * @param queryString {string} A SQL query string
   */
  const handleLoadExample = (
    databaseId: string,
    databaseName: string,
    attributeIds: number[],
    vizId: string,
    queryString: string
  ) => {
    setLoading(true);
    setVizSchema(BLANKVIZSCHEMA);
    handleSelectDatabase(databaseId);
    setRadioRecommendations("Schema");

    let attempts = 0;
    const maxAttempts = 10;

    const interval = setInterval(() => {
      let allFound = true;

      attributeIds.forEach((number) => {
        const element = document.getElementById(
          databaseName + "-attr-" + number
        ) as HTMLInputElement;

        if (element) {
          if (!element.checked) {
            element.click();
          }
        } else {
          allFound = false;
        }
      });

      attempts += 1;
      if (allFound || attempts >= maxAttempts) {
        clearInterval(interval);
        axios
          .get(`${apiUrl}/specs/` + vizId)
          .then((response) => {
            setVizSchema(response.data.vizSchema);
            // Copy the reference to the VizSchema data into any Vega specs as 'rawData'.
            response.data.specs.forEach((specItem: any) => {
              specItem.data.forEach((dataItem: any) => {
                if (dataItem.name === "rawData") {
                  dataItem.values = copyJson(response.data.vizSchema.dataset);
                }
              });
            });
            setSpecList(response.data.specs);
            handleSqlSubmit(
              queryString,
              response.data.vizSchema,
              response.data.specs
            );
            setRadioEnabled(true);
          })
          .catch((error) => {
            setErrorMessages([
              "Example data could not be loaded. Please try again later.",
            ]);
            setLoading(false);
            setShowErrors(true);
            console.error("Error loading example: ", error);
          });
      }
    }, 500);

    return () => clearInterval(interval);
  };

  return (
    <>
      <Spin spinning={loading} size="large">
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
              <DatabaseSelector
                selectedValue={selectedDatabase}
                onSelectDatabase={handleSelectDatabase}
              />
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
                size="large"
                style={{ marginBottom: "20px" }}
                disabled={!radioEnabled}
              >
                <Radio.Button value="SQL">SQL</Radio.Button>
                <Radio.Button value="Visualisations">
                  Visualisations
                </Radio.Button>
                <Radio.Button value="Schema">Schema</Radio.Button>
              </Radio.Group>

              {radioSelect === "SQL" && (
                <div>
                  <div>
                    <div style={{ display: "flex", flexDirection: "row" }}>
                      <SQLEditor value={sqlCode} onChange={setSqlCode} />
                      <div
                        style={{
                          display: "flex",
                          flexDirection: "row",
                          flexWrap: "wrap",
                          overflowY: "auto",
                          marginLeft: "50px",
                          maxHeight: "300px",
                          width: "30%",
                        }}
                      >
                        {examplesData.map((example, index) => (
                          <LoadExampleButton
                            key={index}
                            buttonText={example.exampleName}
                            handleLoadExample={() =>
                              handleLoadExample(
                                example.databaseId,
                                example.databaseName,
                                example.attributeIdList,
                                example.vizId,
                                example.queryString
                              )
                            }
                          />
                        ))}
                      </div>
                    </div>
                    <SQLSubmitButton
                      sqlCode={sqlCode}
                      radioEnabled={radioEnabled}
                      handleSqlSubmit={handleSqlSubmitButton}
                    />
                  </div>
                  <DataTable
                    data={vizSchema.dataset ? vizSchema.dataset : []}
                    scrollHeight={380}
                  ></DataTable>
                </div>
              )}

              {radioSelect === "Visualisations" && (
                <>
                  {vizSchema.dataChartTypes &&
                    vizSchema.dataRelationship !== vizSchema.type && (
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
                    keyOneCardinality={keyOneCardinality}
                    setVegaSpec={setVegaSpec}
                    setVegaActionMenu={setVegaActionMenu}
                    setSelectedChart={setSelectedChart}
                    setIsModalOpen={setIsModalOpen}
                  />
                </>
              )}
              {radioSelect === "Schema" && (
                <VizSchemaInfoDisplay vizSchema={vizSchema} />
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
            vizSchema={{ ...vizSchema }}
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
        <Drawer
          title="About"
          placement="right"
          closable={true}
          onClose={closeHelpDrawer}
          open={helpDrawerIsOpen}
        >
          {aboutText.content.map((paragraph, index) => (
            <p key={index} dangerouslySetInnerHTML={{ __html: paragraph }}></p>
          ))}
        </Drawer>
        <FloatButton.Group shape="square" style={{ right: 24 }}>
          <FloatButton
            icon={<QuestionCircleOutlined />}
            onClick={showHelpDrawer}
          />
          <FloatButton
            icon={<SettingOutlined />}
            onClick={showSettingsDrawer}
          />
        </FloatButton.Group>
        {showErrors && (
          <TopWarningAlert
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
      </Spin>
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
