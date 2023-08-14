import React, { useState, useEffect } from "react";
import { WordCloud as AntWordCloud } from "@ant-design/plots";

interface WordCloudProps {
  vizSchema: any;
}

const WordCloud = ({ vizSchema }: WordCloudProps) => {
  const [data, setData] = useState([]);

  useEffect(() => {
    setData(
      vizSchema.dataset.map((item: any) => ({
        ...item,
        [vizSchema.scalarOneAlias]: parseFloat(item[vizSchema.scalarOneAlias]),
      }))
    );
  }, [vizSchema]);

  const config = {
    data,
    wordField: vizSchema.keyOneAlias,
    weightField: vizSchema.scalarOneAlias,
    color: "#1976d2",
    wordStyle: {
      fontFamily: "'Roboto', 'Helvetica', 'Arial', sans-serif",
      fontSize: [16, 80] as any,
    },

    interactions: [
      {
        type: "element-active",
      },
    ],
    state: {
      active: {
        style: {
          fill: "#fc7d0b",
          lineWidth: 0,
        },
      },
    },
    tooltip: {
      showContent: true,
      formatter: (datum: any) => {
        const { text, value } = datum;

        return {
          name: vizSchema.scalarOneAlias,
          value: `${value}`,
        };
      },
    },
  };

  return <AntWordCloud {...config} />;
};

export default WordCloud;
