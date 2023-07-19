import React from "react";
import go, {
  Diagram,
  GraphObject,
  Link,
  Node,
  Binding,
  Shape,
  Panel,
  TextBlock,
} from "gojs";
import { ReactDiagram } from "gojs-react";

type NodeType = "entity" | "relationship";

interface NodeData {
  key: string;
  properties: string;
  type: NodeType;
}

interface LinkData {
  from: string;
  to: string;
  text: string;
}

interface ERDiagramProps {
  nodes: NodeData[];
  links: LinkData[];
}

function initDiagram(): Diagram {
  const $: any = GraphObject.make;
  const diagram: Diagram = $(Diagram, {
    "undoManager.isEnabled": true,
    layout: $(go.TreeLayout, { angle: 90 }),
  });

  diagram.nodeTemplate = $(
    Node,
    "Auto",
    $(
      Shape,
      { strokeWidth: 0, fill: "lightgray" },
      new Binding("figure", "type", (type: NodeType) =>
        type === "relationship" ? "Diamond" : "Rectangle"
      )
    ),
    $(
      Panel,
      "Table",
      $(go.RowColumnDefinition, { column: 1, width: 4 }),
      $(
        TextBlock,
        {
          row: 0,
          column: 0,
          columnSpan: 2,
          margin: 5,
          textAlign: "center",
          font: "bold 12px sans-serif",
        },
        new Binding("text", "key")
      ),
      $(
        TextBlock,
        "properties",
        { row: 1, column: 0, font: "italic 10px sans-serif" },
        new Binding("text", "properties")
      )
    )
  );

  diagram.linkTemplate = $(
    Link,
    { routing: Link.AvoidsNodes },
    $(Shape),
    $(Shape, { toArrow: "Standard" }),
    $(
      Panel,
      "Auto",
      $(Shape, "Rectangle", { fill: "white" }),
      $(
        TextBlock,
        { segmentOffset: new go.Point(0, -10) },
        new Binding("text", "text")
      )
    )
  );

  return diagram;
}

const ERDiagram = ({ nodes, links }: ERDiagramProps) => {
  return (
    <ReactDiagram
      initDiagram={initDiagram}
      divClassName="diagram-component"
      nodeDataArray={nodes}
      linkDataArray={links}
      skipsDiagramUpdate={false}
    />
  );
};

export default ERDiagram;
