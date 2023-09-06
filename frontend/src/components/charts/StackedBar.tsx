import { Bar } from "@ant-design/plots";

interface StackedBarProps {
  vizSchema: any;
}

const StackedBar = ({ vizSchema }: StackedBarProps) => {
  const DATA = vizSchema.dataset.map((item: any) => ({
    label: item[vizSchema.keyOneAlias],
    type: item[vizSchema.keyTwoAlias],
    value: parseFloat(item[vizSchema.scalarOneAlias]),
  }));

  const config = {
    data: DATA.reverse(),
    isStack: true,
    xField: "value",
    yField: "label",
    seriesField: "type",
    xAxis: {
      label: {
        autoRotate: true,
        style: {
          fill: "#aaa",
        },
      },
      title: {
        text: vizSchema.scalarOne.attributeName,
        style: {
          fontSize: 14,
        },
      },
    },
    brush: {
      enabled: true,
      type: "x-rect" as "x-rect",
      action: "filter" as "filter",
    },
  };
  return <Bar {...config} />;
};

export default StackedBar;
