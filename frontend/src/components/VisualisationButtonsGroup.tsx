import React from "react";
import { Divider, Skeleton } from "antd";
import VisualisationButtons from "./VisualisationButtons";
import { DotChartOutlined } from "@ant-design/icons";
import styled from "styled-components";

type VisualisationButtonsGroupProps = {
  chartTypes: {
    Recommended: string[];
    Possible: string[];
    Other: string[];
  };
  specList: any[];
  setVegaSpec: (spec: any) => void;
  setVegaActionMenu: (value: boolean) => void;
  setSelectedChart: (chart: string | null) => void;
  setIsModalOpen: (isOpen: boolean) => void;
};

const VisualisationButtonsGroup = ({
  chartTypes,
  specList,
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
          <DotChartOutlined style={{ fontSize: 40, color: "#bfbfbf" }} />
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
            setVegaSpec={setVegaSpec}
            setVegaActionMenu={setVegaActionMenu}
            setSelectedChart={setSelectedChart}
            setIsModalOpen={setIsModalOpen}
          />
        </>
      )}
      <Header>Not recommended</Header>
      <SubHeader>
        Visualisations that do not match the schema pattern of your selected
        data and may not display correctly
      </SubHeader>
      <StyledDivider />
      <VisualisationButtons
        chartTypes={chartTypes.Other}
        specList={specList}
        setVegaSpec={setVegaSpec}
        setVegaActionMenu={setVegaActionMenu}
        setSelectedChart={setSelectedChart}
        setIsModalOpen={setIsModalOpen}
      />
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