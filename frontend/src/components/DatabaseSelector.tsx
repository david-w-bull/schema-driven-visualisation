import React, { useState, useEffect, ChangeEvent } from "react";
import { Select, FormControl, InputLabel, MenuItem } from "@mui/material";
import { SelectChangeEvent } from "@mui/material/Select";

interface DatabaseSelectorProps {
  onSelectDatabase: (selectedValue: string) => void;
}

const DatabaseSelector = ({ onSelectDatabase }: DatabaseSelectorProps) => {
  const [selectedValue, setSelectedValue] = useState<string>(
    "64e4b803fe3299654b1739bf"
  );

  const handleChange = (event: SelectChangeEvent<any>) => {
    const newValue = event.target.value;
    setSelectedValue(newValue);
    onSelectDatabase(newValue);
  };

  useEffect(() => {
    onSelectDatabase(selectedValue);
  }, []);

  return (
    <div
      style={{
        margin: "20px 20px 5px 20px",
      }}
    >
      <FormControl fullWidth>
        <InputLabel id="demo-simple-select-label">Database</InputLabel>
        <Select
          labelId="demo-simple-select-label"
          id="demo-simple-select"
          value={selectedValue}
          label="Database"
          onChange={handleChange}
        >
          <MenuItem value={"64e4b803fe3299654b1739bf"}>
            Mondial Fragment
          </MenuItem>
          <MenuItem value={"64e4b8f4fc72440674f39f11"}>Mondial Full</MenuItem>
        </Select>
      </FormControl>
    </div>
  );
};

export default DatabaseSelector;
