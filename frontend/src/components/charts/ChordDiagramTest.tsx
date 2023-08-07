import React, { useState, useEffect } from "react";
import { Chord } from "@ant-design/plots";
import { VizSchema } from "../../types";

interface ChordDiagramProps {
  vizSchema: any;
}

const ChordDiagramTest = ({ vizSchema }: ChordDiagramProps) => {
  // if (!vizSchema || !vizSchema.dataset) {
  //   return <div>Loading...</div>; // or return null;
  // }

  console.log(vizSchema);

  const DATA = vizSchema.dataset.map((item: any) => ({
    source: item[vizSchema.keyOneAlias],
    target: item[vizSchema.keyTwoAlias],
    value: parseFloat(item[vizSchema.scalarOneAlias]),
  }));

  const colors = ["#FF5733", "#33FF57", "#5733FF", "#F33FF5"];

  const config = {
    data: DATA,
    sourceField: "source",
    targetField: "target",
    weightField: "value",
    color: colors,
  };
  return <Chord {...config} />;
};

export default ChordDiagramTest;
