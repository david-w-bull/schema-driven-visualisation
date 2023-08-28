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
import examplesData from "./examplesData";
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
} from "antd";
import { QuestionCircleOutlined, SettingOutlined } from "@ant-design/icons";
import ChartDisplayModal from "./components/ChartDisplayModal";
import VizSchemaInfoDisplay from "./components/VizSchemaInfoDisplay";
import LoadExampleButton from "./components/LoadExampleButton";

const apiUrl = "/api/v1";

function App() {
  const [vegaSpec, setVegaSpec] = useState(BLANKSPEC);
  const [vegaActionMenu, setVegaActionMenu] = useState(false);
  const [schemaInfo, setSchemaInfo] = useState(BLANKSCHEMA);
  const [selectedDatabase, setSelectedDatabase] = useState(
    "64e4b803fe3299654b1739bf"
  );
  const [vizPayloadId, setVizPayloadId] = useState("");

  const handleSelectDatabase = (newValue: string) => {
    setSelectedDatabase(newValue);
    axios
      .get(`${apiUrl}/schemas/` + newValue)
      .then((response) => {
        console.log(response.data);
        setSchemaInfo(response.data);
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

  const [selectedData, setSelectedData] = useState<Data>(BLANKSCHEMA);

  const handleSelectedData = (data: Data) => {
    setVegaSpec(BLANKSPEC);
    setVegaActionMenu(false);
    setSqlCode("");
    setShowErrors(false);
    setSelectedData(data);
    setRadioRecommendations("Schema");
    axios
      // the 'data' payload is a DatabaseSchema object filtered based on user selections
      .post(`${apiUrl}/specs/specFromSchema`, data)
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
            console.log("Swapping keys");
            returnedVizSchema = swapKeyFields(returnedVizSchema);
          }
        }

        setVizSchema(returnedVizSchema);
        setVizPayloadId(response.data.vizId);
        console.log("VizId");
        console.log(response.data.vizId);
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
          setShowErrors(true);
          return;
        }

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

    return ![...vizSchemaFields].every((element) =>
      updatedVizSchemaFields.has(element)
    );
  }

  const handleBackendErrors = (updatedVizSchema: VizSchema) => {
    if (updatedVizSchema.type === "ERROR") {
      setErrorMessages(updatedVizSchema.messages);
      setShowErrors(true);
      return true;
    }
    if (
      !updatedVizSchema.dataset ||
      (updatedVizSchema.dataset && updatedVizSchema.dataset.length === 0)
    ) {
      setErrorMessages(["Your query returned no data"]);
      setShowErrors(true);
      return true;
    }

    if (hasAliasChanged(vizSchema, updatedVizSchema)) {
      setErrorMessages([
        "Field aliases cannot be changed as part of SQL updates",
      ]);
      setShowErrors(true);
      return true;
    }
    return false;
  };

  const handleSqlSubmitButton = () => {
    // A function to separate event handler calls of this function
    // vs. calls with custom parameters
    handleSqlSubmit();
  };

  const handleSqlSubmit = (customSql?: string, loadedVizSchema?: VizSchema) => {
    // Clear previous alerts
    setShowErrors(false);

    const vizSchemaToUpdate =
      loadedVizSchema != undefined ? loadedVizSchema : vizSchema;

    const updatedVizSchema = {
      ...vizSchemaToUpdate,
      sqlQuery: customSql ? customSql : sqlCode,
      chartTypes: [],
      dataChartTypes: [],
    };

    console.log("Updated VizSchema");
    console.log(updatedVizSchema);

    axios
      .post(`${apiUrl}/specs/updateSqlData`, updatedVizSchema)
      .then((response) => {
        console.log(response.data);
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
            console.log("Swapping keys");
            returnedVizSchema = swapKeyFields(returnedVizSchema);
          }
        }
        setVizSchema(returnedVizSchema);
        console.log("Custom SQL");
        console.log(customSql);
        customSql && setSqlCode(customSql);
        console.log(returnedVizSchema);
        setSchemaChartTypes(returnedVizSchema.chartTypes);
        setDataChartTypes(returnedVizSchema.dataChartTypes);
        setKeyCardinality(returnedVizSchema.keyCardinality);

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

  const handleLoadExample = (
    databaseId: string,
    databaseName: string,
    attributeIds: number[],
    vizId: string,
    queryString: string
  ) => {
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
            handleSqlSubmit(queryString, response.data.vizSchema);
            // setVizSchema(response.data.vizSchema);
            setRadioEnabled(true);
          })
          .catch((error) => {
            console.error("There was an error!", error);
          });
      }
    }, 500);

    return () => clearInterval(interval);
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
                  <div style={{ display: "flex", flexDirection: "row" }}>
                    <SQLEditor value={sqlCode} onChange={setSqlCode} />
                    <div
                      style={{
                        display: "flex",
                        flexDirection: "row",
                        flexWrap: "wrap",
                        overflowY: "auto",
                        marginLeft: "50px",
                        maxHeight: "300px", // Set to a height that works for you
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
                {vizSchema.dataRelationship !== vizSchema.type && (
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
