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
    color: "#122c6a",
    wordStyle: {
      fontFamily: "Verdana",
      fontSize: [24, 80] as any,
    },

    interactions: [
      {
        type: "element-active",
      },
    ],
    state: {
      active: {
        style: {
          lineWidth: 3,
        },
      },
    },
  };

  return <AntWordCloud {...config} />;
};

export default WordCloud;
