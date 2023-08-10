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

  const config = {
    data: DATA,
    isGroup: true,
    xField: "value",
    yField: "label",
    seriesField: "type",
    marginRatio: 0,
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
