import { useState, useEffect } from "react";
import { Column, Bar } from "@ant-design/plots";

interface BarSliderProps {
  vizSchema: any;
}

const BarSlider = ({ vizSchema }: BarSliderProps) => {
  const [data, setData] = useState([]);
  const [hasSecondKey, setHasSecondKey] = useState(
    vizSchema.keyTwoAlias != null
  );

  useEffect(() => {
    setData(
      vizSchema.dataset.map((item: any) => {
        return {
          ...item,
          [vizSchema.scalarOneAlias]: parseFloat(
            item[vizSchema.scalarOneAlias]
          ),
          compound_key: hasSecondKey
            ? `${item[vizSchema.keyOneAlias]} > ${item[vizSchema.keyTwoAlias]}`
            : null,
        };
      })
    );
  }, [vizSchema]);

  const config = {
    data,
    yField: hasSecondKey ? "compound_key" : vizSchema.keyOneAlias,
    xField: vizSchema.scalarOneAlias,
    seriesField: hasSecondKey ? vizSchema.keyOneAlias : undefined,
    yAxis: {
      label: {
        autoRotate: true,
      },
    },
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
    slider: {
      start: 0.0,
      end: 1.0,
    },
    brush: {
      enabled: true,
      type: "x-rect" as "x-rect",
      action: "filter" as "filter",
    },
  };

  return <Bar {...config} />;
};

export default BarSlider;
