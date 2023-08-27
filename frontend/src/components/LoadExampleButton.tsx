import Button from "@mui/material/Button";
import CachedIcon from "@mui/icons-material/Cached";

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
      variant="outlined"
      onClick={handleLoadExample}
      style={{
        height: "100px",
        width: "200px",
      }}
    >
      {buttonText ? buttonText : "Example"}
    </Button>
  );
}

export default LoadExampleButton;
