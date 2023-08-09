import React from "react";
import { Table } from "antd";

type TableProps = {
  data: any[];
};

const DataTable = ({ data }: TableProps) => {
  if (!data || data.length === 0) return null;

  const dataSource = data.map((item, index) => ({
    key: index.toString(),
    ...item,
  }));

  // Generate column names from the first item's keys
  const columns = Object.keys(data[0]).map((key) => ({
    // title: key.charAt(0).toUpperCase() + key.slice(1).replace("_", " "),
    title: key,
    dataIndex: key,
    key: key,
  }));

  const columnsToExclude = ["a_count", "b_count"];

  const filteredColumns = columns.filter(
    (column) => !columnsToExclude.includes(column.dataIndex)
  );

  return (
    <Table
      dataSource={dataSource}
      columns={filteredColumns}
      scroll={{ y: 300 }}
      pagination={false}
    />
  );
};

export default DataTable;
