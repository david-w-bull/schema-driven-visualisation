import React from "react";
import { Table } from "antd";

type TableProps = {
  data: any[];
  scrollHeight: number;
};

const DataTable = ({ data, scrollHeight }: TableProps) => {
  if (!data || data.length === 0) return null;

  const dataSource = data.map((item, index) => ({
    key: index.toString(),
    ...item,
  }));

  const genericSorter = (a: any, b: any, dataIndex: string) => {
    if (typeof a[dataIndex] === "number" && typeof b[dataIndex] === "number") {
      return a[dataIndex] - b[dataIndex];
    }
    return (a[dataIndex] || "").localeCompare(b[dataIndex] || "");
  };

  // Generate column names from the first item's keys
  const columns = Object.keys(data[0]).map((key) => ({
    // title: key.charAt(0).toUpperCase() + key.slice(1).replace("_", " "),
    title: key,
    dataIndex: key,
    key: key,
    sorter: (a: any, b: any) => genericSorter(a, b, key),
  }));

  // Exclude columns used in backend vizSchema calculations
  const columnsToExclude = ["a_count", "b_count"];

  const filteredColumns = columns.filter(
    (column) => !columnsToExclude.includes(column.dataIndex)
  );

  return (
    <Table
      dataSource={dataSource}
      columns={filteredColumns}
      scroll={{ y: scrollHeight }}
      pagination={false}
    />
  );
};

export default DataTable;
