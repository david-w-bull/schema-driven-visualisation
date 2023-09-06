import styled from "styled-components";
import { Vega } from "react-vega";
import { VizSchema } from "../types";
import { BLANKSPEC } from "../constants";
import { renderChartComponent } from "../utils/chartUtils";
import { useState } from "react";
import { Divider } from "antd";
import SwapHorizIcon from "@mui/icons-material/SwapHoriz";
import { Button } from "@mui/material";

type ChartDisplayModalProps = {
  setIsModalOpen: (isOpen: boolean) => void;
  selectedChart: string | null;
  vegaSpec: any;
  vegaActionMenu: boolean;
  vizSchema: VizSchema;
};

function ChartDisplayModal({
  setIsModalOpen,
  selectedChart,
  vegaSpec,
  vegaActionMenu,
  vizSchema,
}: ChartDisplayModalProps) {
  const [showVegaSpec, setShowVegaSpec] = useState(false);

  const handleToggleVega = () => {
    setShowVegaSpec((prevState) => !prevState);
  };

  return (
    <ModalContainer>
      <ModalBackdrop />
      <ModalContent>
        <CloseIcon onClick={() => setIsModalOpen(false)}>×</CloseIcon>
        <div
          style={{
            display: "flex",
            flexDirection: "column",
            width: "95%",
            alignItems: "center",
          }}
        >
          <Header>{selectedChart}</Header>
          {showVegaSpec && (
            <SubHeader>
              Vega specifications are included for evaluation purposes only.
              Data may not be displayed correctly.
            </SubHeader>
          )}
          <StyledDivider></StyledDivider>
        </div>

        {showVegaSpec ? (
          <div
            style={{
              width: "95%",
              height: "95%",
              display: "flex",
              flexDirection: "column",
              justifyContent: "center",
              alignItems: "center",
            }}
          >
            <Vega spec={vegaSpec} actions={vegaActionMenu} />
          </div>
        ) : selectedChart ? (
          <div style={{ width: "95%", height: "95%" }}>
            {renderChartComponent(selectedChart, vizSchema)}
          </div>
        ) : (
          <div>Visualisation Unavailable</div>
        )}

        {vegaSpec && vegaSpec !== BLANKSPEC && (
          <Button
            variant="outlined"
            endIcon={<SwapHorizIcon />}
            style={{
              alignSelf: "flex-end",
              bottom: "10px",
              right: "10px",
              textTransform: "none",
            }}
            onClick={handleToggleVega}
          >
            {showVegaSpec ? "Ant-V" : "Vega"}
          </Button>
        )}
      </ModalContent>
    </ModalContainer>
  );
}

export default ChartDisplayModal;

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
  width: 100%;
  height: 100%;
  max-width: 80vw;
  max-height: 80vh;
  z-index: 1001;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
`;

const CloseIcon = styled.div`
  position: absolute;
  top: 10px;
  right: 10px;
  cursor: pointer;
  font-size: 24px;
  color: #000;
`;

const Header = styled.h1`
  font-size: 25px;
  font-family: "Roboto", "Helvetica", "Arial", sans-serif;
  font-weight: 500;
  margin: 0px;
  color: #36454f;
`;

const SubHeader = styled.p`
  font-size: 14px;
  font-family: "Roboto", "Helvetica", "Arial", sans-serif;
  font-weight: 300;
  margin: 0px;
`;

const StyledDivider = styled(Divider)`
  margin: 2px 0px 10px 0px;
  border-top-color: #d3d3d3;
`;
