import { Avatar, Col } from "antd";

interface FieldInfoDisplayProps {
  text: string;
  title: string;
  description: string;
  backgroundColor?: string;
  color?: string;
  shape?: string;
}

const FieldInfoDisplay = ({
  text,
  title,
  description,
  backgroundColor = "#fde3cf",
  color = "#f56a00",
  shape = "",
}: FieldInfoDisplayProps) => {
  return (
    <Col>
      <div
        style={{
          display: "flex",
          alignItems: "center",
          paddingTop: "20px",
          paddingRight: "40px",
        }}
      >
        <Avatar size={60} style={{ backgroundColor, color }}>
          {text}
        </Avatar>
        <div style={{ marginLeft: "10px" }}>
          <div style={{ fontWeight: "bold", fontSize: 20 }}>{title}</div>
          <div style={{ fontWeight: "300", fontSize: 18 }}>{description}</div>
        </div>
      </div>
    </Col>
  );
};

export default FieldInfoDisplay;
