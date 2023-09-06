import { Button } from "antd";
import { CardinalityLimits } from "../types";
import { BLANKSPEC } from "../constants";

type VisualisationButtonsProps = {
  chartTypes: string[];
  specList: any[];
  cardinalityLimits?: CardinalityLimits;
  keyCardinality?: number;
  keyOneCardinality?: number;
  setVegaSpec: (spec: any) => void;
  setVegaActionMenu: (value: boolean) => void;
  setSelectedChart: (chart: string | null) => void;
  setIsModalOpen: (isOpen: boolean) => void;
  buttonStyle?: React.CSSProperties;
};

const VisualisationButtons = ({
  chartTypes,
  specList,
  cardinalityLimits,
  keyCardinality,
  keyOneCardinality,
  setVegaSpec,
  setVegaActionMenu,
  setSelectedChart,
  setIsModalOpen,
  buttonStyle,
}: VisualisationButtonsProps) => {
  return (
    <div>
      {chartTypes?.map((chartType: string, index: number) => {
        const matchingSpec = specList.find(
          (spec: any) => spec.description === chartType
        );
        return (
          <Button
            type="default"
            key={index}
            style={{
              ...buttonStyle,
            }}
            onClick={() => {
              if (matchingSpec) {
                setVegaSpec(matchingSpec);
                setVegaActionMenu(true);
              } else {
                setVegaSpec(BLANKSPEC);
                setVegaActionMenu(false);
              }
              setSelectedChart(chartType);
              setIsModalOpen(true);
            }}
          >
            {cardinalityLimits ? (
              <div
                style={{
                  display: "flex",
                  flexDirection: "column",
                  height: "100%",
                }}
              >
                <div
                  style={{
                    flex: 1,
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                  }}
                >
                  {chartType}
                </div>
                <div style={{ fontSize: 11, color: "#999999" }}>
                  Dataset:{" "}
                  {chartType === "Line Chart"
                    ? keyOneCardinality
                    : keyCardinality}
                </div>
                <div style={{ fontSize: 11, color: "#999999" }}>
                  Limit: {cardinalityLimits && cardinalityLimits[chartType]}
                </div>
              </div>
            ) : (
              chartType
            )}
          </Button>
        );
      })}
    </div>
  );
};

export default VisualisationButtons;
