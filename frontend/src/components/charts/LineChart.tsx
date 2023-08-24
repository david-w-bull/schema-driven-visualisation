import { Line } from "@ant-design/plots";

interface LineChartProps {
  vizSchema: any;
}

const isNumeric = (n: any) => {
  return !isNaN(parseFloat(n)) && isFinite(n);
};

const LineChart = ({ vizSchema }: LineChartProps) => {
  const data = vizSchema.dataset.map((item: any) => ({
    label: item[vizSchema.keyOneAlias],
    type: item[vizSchema.keyTwoAlias],
    value: parseFloat(item[vizSchema.scalarOneAlias]),
  }));

  // Check if the 'type' field is numeric
  const isTypeNumeric = data.length > 0 && isNumeric(data[0].type);

  if (isTypeNumeric) {
    data.sort((a: any, b: any) => parseFloat(a.type) - parseFloat(b.type));
  }

  const config = {
    data,
    xField: "type",
    yField: "value",
    seriesField: vizSchema.keyTwoAlias ? "label" : undefined,
    legend: {
      position: "right" as "right",
    },
  };

  return <Line {...config} />;
};

export default LineChart;
