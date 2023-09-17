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

  const isNumeric = (n: any) => {
    return !isNaN(parseFloat(n)) && isFinite(n);
  };

  // Check if the 'type' field is numeric
  const isTypeNumeric = DATA.length > 0 && isNumeric(DATA[0].type);

  if (isTypeNumeric) {
    DATA.sort((a: any, b: any) => parseFloat(a.type) - parseFloat(b.type));
  }

  const config = {
    data: DATA,
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
