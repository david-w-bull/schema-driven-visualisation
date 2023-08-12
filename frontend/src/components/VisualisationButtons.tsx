import { Button } from "antd";

type VisualisationButtonsProps = {
  chartTypes: string[];
  specList: any[];
  setVegaSpec: (spec: any) => void;
  setVegaActionMenu: (value: boolean) => void;
  setSelectedChart: (chart: string | null) => void;
  setIsModalOpen: (isOpen: boolean) => void;
  buttonStyle?: React.CSSProperties;
};

const VisualisationButtons = ({
  chartTypes,
  specList,
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
                setSelectedChart(null);
              } else {
                setSelectedChart(chartType);
              }
              setIsModalOpen(true);
            }}
          >
            {chartType}
          </Button>
        );
      })}
    </div>
  );
};

export default VisualisationButtons;
