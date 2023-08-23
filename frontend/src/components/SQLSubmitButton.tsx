import Button from "@mui/material/Button";
import CachedIcon from "@mui/icons-material/Cached";
import Tooltip from "@mui/material/Tooltip";

type SQLSubmitButtonProps = {
  sqlCode: string;
  radioEnabled: boolean;
  handleSqlSubmit: () => void;
};

function SQLSubmitButton({
  sqlCode,
  radioEnabled,
  handleSqlSubmit,
}: SQLSubmitButtonProps) {
  const isDisabled = sqlCode.length === 0 || !radioEnabled;
  //   const tooltipMessage = isDisabled
  //     ? "Button is disabled!"
  //     : "Click to update SQL";

  let tooltipMessage = "";

  if (!radioEnabled) {
    tooltipMessage =
      "You must first select fields from the checkbox menu to generate a query";
  } else if (sqlCode.length === 0) {
    tooltipMessage = "Your SQL query is empty";
  } else {
    tooltipMessage =
      "SQL updates do not support changes to the generated aliases of your SELECT fields, though you add further SELECT fields, add JOINS and add other clauses, such as WHERE.\n\nThese changes will not alter the detected visualisation schema and are designed primarily to allow data filtering.";
  }

  return (
    <Tooltip
      title={
        <>
          {tooltipMessage.split("\n").map((str, index, array) => (
            <span key={index}>
              {str}
              {index === array.length - 1 ? null : <br />}
            </span>
          ))}
        </>
      }
    >
      <span>
        <Button
          variant="contained"
          endIcon={<CachedIcon />}
          disabled={isDisabled}
          onClick={handleSqlSubmit}
          style={{
            margin: "5px 20px 20px 0px",
            boxShadow: "2px 2px 5px rgba(0, 0, 0, 0.3)",
          }}
        >
          Update SQL
        </Button>
      </span>
    </Tooltip>
  );
}

export default SQLSubmitButton;
