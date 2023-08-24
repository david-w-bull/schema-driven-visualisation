import React, { useState, useEffect } from "react";
import { Sankey } from "@ant-design/plots";
import { VizSchema } from "../../types";

interface SankeyDiagramProps {
  vizSchema: any;
}

const SankeyDiagram = ({ vizSchema }: SankeyDiagramProps) => {
  const finalTestData = vizSchema.dataset || [];

  const sankeyData: any[] = [];
  const keys = [vizSchema.keyOneAlias, vizSchema.keyTwoAlias];

  finalTestData.forEach((d: any) => {
    keys.reduce((a, b) => {
      if (a && b && d[a] && d[b]) {
        sankeyData.push({
          source: d[a],
          target: d[b],
          value: parseFloat(d[vizSchema.scalarOneAlias]),
          path: keys.map((key) => d[key]).join(" -> "),
        });
      }
      return b;
    });
  });

  console.log(sankeyData);

  const config = {
    data: sankeyData,
    sourceField: "source",
    targetField: "target",
    weightField: "value",
    nodeWidthRatio: 0.01,
    nodePaddingRatio: 0.03,
    nodeDraggable: true,
    rawFields: ["path"],
    tooltip: {
      fields: ["path", "value"],
      showContent: true,
      formatter: (tooltipFields: any) => {
        const { path, value } = tooltipFields;
        return {
          name: path,
          value: value,
        };
      },
    },
  };

  return <Sankey {...config} />;
};

export default SankeyDiagram;
