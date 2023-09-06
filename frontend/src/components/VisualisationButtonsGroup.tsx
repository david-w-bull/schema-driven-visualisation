import React from "react";
import { Divider, Skeleton } from "antd";
import VisualisationButtons from "./VisualisationButtons";
import { BarChartOutlined } from "@ant-design/icons";
import styled from "styled-components";
import { CardinalityLimits, ChartRecommendations } from "../types";

type VisualisationButtonsGroupProps = {
  vizSchemaType: string;
  chartTypes: ChartRecommendations;
  specList: any[];
  cardinalityLimits?: CardinalityLimits;
  keyCardinality?: number;
  keyOneCardinality?: number;
  setVegaSpec: (spec: any) => void;
  setVegaActionMenu: (value: boolean) => void;
  setSelectedChart: (chart: string | null) => void;
  setIsModalOpen: (isOpen: boolean) => void;
};

const VisualisationButtonsGroup = ({
  vizSchemaType,
  chartTypes,
  specList,
  cardinalityLimits,
  keyCardinality,
  keyOneCardinality,
  setVegaSpec,
  setVegaActionMenu,
  setSelectedChart,
  setIsModalOpen,
}: VisualisationButtonsGroupProps) => {
  return (
    <div
      style={{ display: "flex", flexDirection: "column", paddingTop: "20px" }}
    >
      <Header>Recommended</Header>
      <SubHeader>
        Visualisations that match the schema pattern of your selected fields and
        configured cardinality limits
      </SubHeader>
      <StyledDivider />
      {chartTypes.Recommended != null && chartTypes.Recommended.length > 0 ? (
        <VisualisationButtons
          chartTypes={chartTypes.Recommended}
          specList={specList}
          setVegaSpec={setVegaSpec}
          setVegaActionMenu={setVegaActionMenu}
          setSelectedChart={setSelectedChart}
          setIsModalOpen={setIsModalOpen}
          buttonStyle={{
            width: "150px",
            height: "150px",
            margin: "25px 20px 30px 0px",
          }}
        />
      ) : (
        <Skeleton.Node
          active={false}
          style={{
            width: "140px",
            height: "140px",
            margin: "25px 20px 30px 0px", // Update to use grid layout
          }}
        >
          <BarChartOutlined style={{ fontSize: 40, color: "#bfbfbf" }} />
        </Skeleton.Node>
      )}
      {chartTypes.Possible.length > 0 && (
        <>
          <Header>Recommended - with adjustments</Header>
          <SubHeader>
            Visualisations that match the schema pattern of your selected fields
            but where cardinality limits are exceeded
          </SubHeader>
          <StyledDivider />
          <VisualisationButtons
            chartTypes={chartTypes.Possible}
            specList={specList}
            keyCardinality={keyCardinality}
            keyOneCardinality={keyOneCardinality}
            cardinalityLimits={cardinalityLimits}
            setVegaSpec={setVegaSpec}
            setVegaActionMenu={setVegaActionMenu}
            setSelectedChart={setSelectedChart}
            setIsModalOpen={setIsModalOpen}
            buttonStyle={{
              width: "150px",
              height: "150px",
              margin: "25px 20px 30px 0px",
            }}
          />
        </>
      )}
      {vizSchemaType != "NONE" && vizSchemaType != "ERROR" && (
        <>
          <Header>Not recommended</Header>
          <SubHeader>
            Visualisations that do not match the schema pattern of your selected
            data and may not display correctly
          </SubHeader>
          <StyledDivider />
          <div style={{ paddingTop: "20px", width: "700px" }}>
            <VisualisationButtons
              chartTypes={chartTypes.Other}
              specList={specList}
              setVegaSpec={setVegaSpec}
              setVegaActionMenu={setVegaActionMenu}
              setSelectedChart={setSelectedChart}
              setIsModalOpen={setIsModalOpen}
              buttonStyle={{
                margin: "5px 5px 5px 0px",
              }}
            />
          </div>
        </>
      )}
    </div>
  );
};

export default VisualisationButtonsGroup;

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
