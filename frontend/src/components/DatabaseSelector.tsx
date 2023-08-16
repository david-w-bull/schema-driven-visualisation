import React, { useState, useEffect, ChangeEvent } from "react";
import { Select, FormControl, InputLabel, MenuItem } from "@mui/material";
import { SelectChangeEvent } from "@mui/material/Select";

interface DatabaseSelectorProps {
  onSelectDatabase: (selectedValue: string) => void;
}

const DatabaseSelector = ({ onSelectDatabase }: DatabaseSelectorProps) => {
  const [selectedValue, setSelectedValue] = useState<string>(
    "64dbbc759fbef90ba1359128"
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
          <MenuItem value={"64dbbc759fbef90ba1359128"}>
            Mondial Fragment
          </MenuItem>
          <MenuItem value={"64c6a0b0c078197638e7bd14"}>
            Another database
          </MenuItem>
        </Select>
      </FormControl>
    </div>
  );
};

export default DatabaseSelector;
