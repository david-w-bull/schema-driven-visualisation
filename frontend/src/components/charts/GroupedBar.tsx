import { Bar } from "@ant-design/plots";

interface GroupedBarProps {
  vizSchema: any;
}

const GroupedBar = ({ vizSchema }: GroupedBarProps) => {
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
    isGroup: true,
    xField: "value",
    yField: "label",
    seriesField: vizSchema.keyTwoAlias ? "type" : undefined,
    marginRatio: 0,
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
    label: {
      position: "right" as "right",
      layout: [
        {
          type: "interval-adjust-position",
        },
        {
          type: "interval-hide-overlap",
        },
        {
          type: "adjust-color",
        },
        {
          type: "limit-in-shape",
        },
      ],
    },
    barStyle: {
      radius: [2, 2, 0, 0],
    },
    brush: {
      enabled: true,
      type: "x-rect" as "x-rect",
      action: "filter" as "filter",
    },
  };
  return <Bar {...config} />;
};

export default GroupedBar;
