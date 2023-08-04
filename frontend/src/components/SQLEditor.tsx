import React from "react";
import AceEditor from "react-ace";

import "ace-builds/src-noconflict/mode-sql"; // Importing the generic SQL mode
import "ace-builds/src-noconflict/theme-tomorrow"; // Importing a theme

interface SQLEditorProps {
  value: string;
  onChange: (value: string) => void;
}

const SQLEditor = ({ value, onChange }: SQLEditorProps) => {
  return (
    <AceEditor
      mode="sql"
      theme="tomorrow"
      value={value}
      onChange={onChange}
      name="sql_editor_div"
      fontSize={18}
      showPrintMargin={true}
      showGutter={true}
      highlightActiveLine={true}
      editorProps={{ $blockScrolling: true }}
      setOptions={{
        enableBasicAutocompletion: false,
        enableLiveAutocompletion: false,
        enableSnippets: false,
        showLineNumbers: true,
        tabSize: 2,
      }}
    />
  );
};

export default SQLEditor;
