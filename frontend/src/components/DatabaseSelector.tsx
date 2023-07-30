import React, { useState, ChangeEvent } from "react";

interface DatabaseSelectorProps {
  onSelectDatabase: (selectedValue: string) => void;
}

const DatabaseSelector = ({ onSelectDatabase }: DatabaseSelectorProps) => {
  const [selectedValue, setSelectedValue] = useState<string>("");

  const handleChange = (event: ChangeEvent<HTMLSelectElement>) => {
    setSelectedValue(event.target.value);
  };

  const handleSubmit = () => {
    if (selectedValue !== "") {
      onSelectDatabase(selectedValue);
    } else {
      alert("Please select a value");
    }
  };

  const values = [
    { value: "64c69202c0471b4a9b8e18ee", label: "Mondial Fragment" },
  ];

  return (
    <div>
      <select value={selectedValue} onChange={handleChange}>
        <option value="" disabled>
          Select a database
        </option>
        {values.map((item, index) => (
          <option key={index} value={item.value}>
            {item.label}
          </option>
        ))}
      </select>
      <button onClick={handleSubmit}>Select Database</button>
    </div>
  );
};

export default DatabaseSelector;
