import { Button } from "antd";

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
          <Button
            type="default"
            key={index}
            style={{
              width: "150px",
              height: "150px",
              margin: "25px 20px 30px 0px",
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
