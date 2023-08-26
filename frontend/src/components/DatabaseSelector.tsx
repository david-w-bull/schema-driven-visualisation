import React, { useState, useEffect, ChangeEvent } from "react";
import { Select, FormControl, InputLabel, MenuItem } from "@mui/material";
import { SelectChangeEvent } from "@mui/material/Select";

interface DatabaseSelectorProps {
  selectedValue: string;
  onSelectDatabase: (selectedValue: string) => void;
}

const DatabaseSelector = ({
  selectedValue,
  onSelectDatabase,
}: DatabaseSelectorProps) => {
  // Ids associated with default databases in MongoDB
  const databaseLookups = [
    { value: "64e4b803fe3299654b1739bf", label: "Mondial Fragment" },
    { value: "64e4b8f4fc72440674f39f11", label: "Mondial Full" },
  ];

  const handleChange = (event: SelectChangeEvent<any>) => {
    const newValue = event.target.value;
    onSelectDatabase(newValue);
  };

  useEffect(() => {
    onSelectDatabase(selectedValue);
  }, []);

  // Find the label corresponding to the selected value
  const selectedDatabase = databaseLookups.find(
    (db) => db.value === selectedValue
  );

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
          {databaseLookups.map((database) => (
            <MenuItem key={database.value} value={database.value}>
              {database.label}
            </MenuItem>
          ))}
        </Select>
      </FormControl>
    </div>
  );
};

export default DatabaseSelector;
