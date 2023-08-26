import Button from "@mui/material/Button";
import CachedIcon from "@mui/icons-material/Cached";

type LoadExampleButtonProps = {
  handleLoadExample: () => void;
};

function LoadExampleButton({ handleLoadExample }: LoadExampleButtonProps) {
  return (
    <Button
      variant="contained"
      endIcon={<CachedIcon />}
      onClick={handleLoadExample}
      style={{
        margin: "5px 20px 20px 0px",
        boxShadow: "2px 2px 5px rgba(0, 0, 0, 0.3)",
      }}
    >
      Load Example
    </Button>
  );
}

export default LoadExampleButton;
