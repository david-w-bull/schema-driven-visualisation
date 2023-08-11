import { Button as AntButton } from "antd";

type VisualisationButtonsProps = {
  chartTypes: string[];
  specList: any[];
  setVegaSpec: (spec: any) => void;
  setVegaActionMenu: (value: boolean) => void;
  setSelectedChart: (chart: string | null) => void;
  setIsModalOpen: (isOpen: boolean) => void;
};

const VisualisationButtons = ({
  chartTypes,
  specList,
  setVegaSpec,
  setVegaActionMenu,
  setSelectedChart,
  setIsModalOpen,
}: VisualisationButtonsProps) => {
  return (
    <div>
      {chartTypes?.map((chartType: string, index: number) => {
        const matchingSpec = specList.find(
          (spec: any) => spec.description === chartType
        );
        return (
          <AntButton
            type="default"
            key={index}
            style={{
              width: "150px",
              height: "150px",
              marginRight: "20px", // Update to use grid layout
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
          </AntButton>
        );
      })}
    </div>
  );
};

export default VisualisationButtons;
