import React, { useState } from "react";
import {
  DownOutlined,
  SmileOutlined,
  MehOutlined,
  FrownOutlined,
} from "@ant-design/icons";
import { Dropdown, Menu, Space } from "antd";

const items = [
  {
    label: <SmileOutlined />,
    key: "0",
  },
  {
    label: <MehOutlined />,
    key: "1",
  },
  {
    label: <FrownOutlined />,
    key: "3",
  },
];

const DataTypeSelector = () => {
  const [selectedItem, setSelectedItem] = useState<string | null>(null);

  const handleMenuClick = (e: { key: React.Key }) => {
    setSelectedItem(e.key.toString());
    console.log(e.key.toString());
    // Here you can add additional logic if needed based on the clicked menu item
  };

  return (
    <Dropdown
      menu={{
        onClick: handleMenuClick,
        items,
      }}
      trigger={["click"]}
    >
      <a onClick={(e) => e.preventDefault()}>
        <Space>
          <DownOutlined />
        </Space>
      </a>
    </Dropdown>
  );
};

export default DataTypeSelector;
