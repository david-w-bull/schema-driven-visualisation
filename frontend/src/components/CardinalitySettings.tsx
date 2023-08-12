import React, { useState } from "react";
import TextField from "@mui/material/TextField";
import Grid from "@mui/material/Grid";
import { CardinalityLimits } from "../types";

type CardinalitySettingProps = {
  data: CardinalityLimits;
  onDataUpdate: (key: string, value: number) => void;
};

function CardinalitySettings({ data, onDataUpdate }: CardinalitySettingProps) {
  console.log("Cardinality");
  console.log(data);
  const handleInputChange = (key: string, value: number) => {
    onDataUpdate(key, value);
  };

  return (
    <div
      style={{
        display: "flex",
        flexWrap: "wrap",
        justifyContent: "space-between",
      }}
    >
      {Object.entries(data).map(([key, value]) => (
        <div
          key={key}
          style={{
            flex: "0 1 calc(50% - 10px)",
            margin: "5px 5px 15px 5px",
            minWidth: "calc(50% - 10px)",
          }}
        >
          <TextField
            id={key}
            label={key}
            type="number"
            value={value}
            onChange={(e) => handleInputChange(key, parseInt(e.target.value))}
            InputLabelProps={{
              shrink: true,
            }}
          />
        </div>
      ))}
    </div>
  );
}

export default CardinalitySettings;
