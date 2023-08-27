// import Button from "@mui/material/Button";
import { Button } from "antd";
type LoadExampleButtonProps = {
  handleLoadExample: () => void;
  buttonText?: string;
};

function LoadExampleButton({
  handleLoadExample,
  buttonText,
}: LoadExampleButtonProps) {
  return (
    <Button
      onClick={handleLoadExample}
      type="primary"
      style={{
        whiteSpace: "normal",
        height: "90px",
        width: "150px",
        marginRight: "20px",
        marginBottom: "10px",
      }}
      ghost
    >
      {buttonText ? buttonText : "Example"}
    </Button>
  );
}

export default LoadExampleButton;
