import { Treemap as AntTreemap } from "@ant-design/plots";
import { useState, useEffect } from "react";
import "./Treemap.css";

interface TreemapProps {
  vizSchema: any;
}

const Treemap = ({ vizSchema }: TreemapProps) => {
  const createHierarchicalStructure = (
    data: any[],
    parentField: string,
    childField: string,
    sizeField: string | number
  ) => {
    const parents: { [key: string]: any } = {};

    data.forEach((item: any) => {
      const parentValue = item[parentField];
      if (!parents[parentValue]) {
        parents[parentValue] = {
          name: parentValue,
          parentName: parentValue,
          value: 0,
          children: [],
        };
      }

      parents[parentValue].value += parseFloat(item[sizeField]);

      const child = {
        name: item[childField],
        value: parseFloat(item[sizeField]),
      };

      parents[parentValue].children.push(child);
    });

    return Object.values(parents);
  };

  const hierarchicalData = createHierarchicalStructure(
    vizSchema.dataset,
    vizSchema.keyOneAlias, // parent field name
    vizSchema.keyTwoAlias, // child field name
    vizSchema.scalarOneAlias // size field name
  );
  console.log(hierarchicalData);

  const data = {
    name: "root",
    children: hierarchicalData,
  };
  const config = {
    data,
    colorField: "parentName",
    interactions: [
      {
        type: "view-zoom",
      },
      {
        type: "drag-move",
      },
    ],
    legend: {
      position: "top-left" as "top-left",
    },
    tooltip: {
      follow: true,
      enterable: true,
      offset: 5,
      customContent: (value: any, items: any) => {
        if (!items || items.length <= 0) return;
        const { data: itemData } = items[0];
        const parent = itemData.path[1];
        const root = itemData.path[itemData.path.length - 1];
        return (
          `<div class='container'>` +
          `<div class='title'>${itemData.name} (${itemData.parentName})</div>` +
          `<div class='tooltip-item'><span>${itemData.value}</span></div>` +
          `</div>`
        );
      },
    },
  };

  return <AntTreemap {...config} />;
};

export default Treemap;
