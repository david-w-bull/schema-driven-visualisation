import { Scatter } from "@ant-design/plots";
import { useState, useEffect } from "react";

interface ScatterPlotProps {
  vizSchema: any;
}

const ScatterPlot = ({ vizSchema }: ScatterPlotProps) => {
  const DATA = vizSchema.dataset.map((item: any) => ({
    ...item,
    [vizSchema.scalarOneAlias]: parseFloat(item[vizSchema.scalarOneAlias]),
    [vizSchema.scalarTwoAlias]: parseFloat(item[vizSchema.scalarTwoAlias]),
  }));

  const config = {
    appendPadding: 30,
    data: DATA,
    xField: vizSchema.scalarOneAlias,
    yField: vizSchema.scalarTwoAlias,
    colorField: vizSchema.keyOneAlias,
    legend: { visible: false },
    color: ["#1976d2"],
    size: 5,
    shape: "circle",
    pointStyle: {
      fillOpacity: 0.5,
    },
    yAxis: {
      nice: true,
      line: {
        style: {
          stroke: "#aaa",
        },
      },
    },
    xAxis: {
      grid: {
        line: {
          style: {
            stroke: "#eee",
          },
        },
      },
      line: {
        style: {
          stroke: "#aaa",
        },
      },
    },
    brush: {
      enabled: true,
      type: "rect" as "rect",
      action: "filter" as "filter",
    },
  };

  return <Scatter {...config} />;
};

export default ScatterPlot;
