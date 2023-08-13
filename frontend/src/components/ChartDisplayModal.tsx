import styled from "styled-components";
import { Vega, VisualizationSpec } from "react-vega";
import { VizSchema } from "../types";
import { renderChartComponent } from "../utils/chartUtils";

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
  return (
    <ModalContainer>
      <ModalBackdrop />
      <ModalContent>
        <CloseIcon onClick={() => setIsModalOpen(false)}>×</CloseIcon>
        {selectedChart ? (
          renderChartComponent(selectedChart, vizSchema)
        ) : (
          <Vega spec={vegaSpec} actions={vegaActionMenu} />
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
