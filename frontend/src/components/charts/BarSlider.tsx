import { useState, useEffect } from "react";
import { Column, Bar } from "@ant-design/plots";

interface BarSliderProps {
  vizSchema: any;
}

const BarSlider = ({ vizSchema }: BarSliderProps) => {
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
    yField: vizSchema.keyOneAlias,
    xField: vizSchema.scalarOneAlias,
    yAxis: {
      label: {
        autoRotate: true,
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
