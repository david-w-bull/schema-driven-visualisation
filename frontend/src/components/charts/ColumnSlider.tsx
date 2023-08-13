import { useState, useEffect } from "react";
import { Column, Bar } from "@ant-design/plots";

interface ColumnSliderProps {
  vizSchema: any;
}

const ColumnSlider = ({ vizSchema }: ColumnSliderProps) => {
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
    xField: vizSchema.keyOneAlias,
    yField: vizSchema.scalarOneAlias,
    xAxis: {
      label: {
        autoRotate: true,
      },
    },
    slider: {
      start: 0.0,
      end: 1.0,
    },
  };

  return <Column {...config} />;
};

export default ColumnSlider;
