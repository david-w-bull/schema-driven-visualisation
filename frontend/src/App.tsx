import { useState, useEffect } from "react";
import axios from "axios";
import "./App.css";
import Message from "./components/Message";
import { Vega, VegaLite, VisualizationSpec } from "react-vega";
import ListGroup from "./components/ListGroup";

function App() {
  let items = ["Test Viz 1", "Test Viz 2", "Test Viz 3"];
  const blankSpec = {
    width: 0,
    height: 0,
    mark: "bar",
  };
  const [vegaSpec, setVegaSpec] = useState(blankSpec);

  const handleSelectItem = (item: string) => {
    const payload = { viz: item };
    axios
      .post("http://localhost:8080/api/v1/specs/postTest", payload)
      .then((response) => setVegaSpec(response.data.spec));
  };

  // useEffect(() => {
  //   axios
  //     .get(
  //       "http://localhost:8080/api/v1/specs/af49b4ac-ae6a-4956-83a9-40ce2f4a042b"
  //     )
  //     .then((response) => setVegaSpec(response.data.spec));
  // }, []);

  // const testSpec: VisualizationSpec = {
  //   description: "Simple donut chart",
  //   width: 400,
  //   height: 400,
  //   data: [
  //     {
  //       name: "table",
  //       transform: [
  //         {
  //           type: "pie",
  //           field: "field",
  //           startAngle: 0.0,
  //         },
  //       ],
  //       values: [
  //         {
  //           id: 1,
  //           field: 4,
  //         },
  //         {
  //           id: 2,
  //           field: 6,
  //         },
  //         {
  //           id: 3,
  //           field: 10,
  //         },
  //         {
  //           id: 4,
  //           field: 3,
  //         },
  //         {
  //           id: 5,
  //           field: 7,
  //         },
  //         {
  //           id: 6,
  //           field: 8,
  //         },
  //       ],
  //     },
  //   ],
  //   scales: [
  //     {
  //       name: "color",
  //       domain: {
  //         data: "table",
  //         field: "id",
  //       },
  //       range: {
  //         scheme: "category20",
  //       },
  //       round: true,
  //       type: "ordinal",
  //     },
  //   ],
  //   marks: [
  //     {
  //       type: "arc",
  //       from: {
  //         data: "table",
  //       },
  //       encode: {
  //         enter: {
  //           x: [
  //             {
  //               signal: "width / 2",
  //             },
  //           ],
  //           y: [
  //             {
  //               signal: "height / 2",
  //             },
  //           ],
  //           fill: [
  //             {
  //               field: "id",
  //               scale: "color",
  //             },
  //           ],
  //           startAngle: [
  //             {
  //               field: "startAngle",
  //             },
  //           ],
  //           endAngle: [
  //             {
  //               field: "endAngle",
  //             },
  //           ],
  //           padAngle: [
  //             {
  //               value: 0,
  //             },
  //           ],
  //           innerRadius: [
  //             {
  //               value: 140,
  //             },
  //           ],
  //           outerRadius: [
  //             {
  //               signal: "width / 2",
  //             },
  //           ],
  //         },
  //         update: {
  //           startAngle: [
  //             {
  //               field: "startAngle",
  //             },
  //           ],
  //           endAngle: [
  //             {
  //               field: "endAngle",
  //             },
  //           ],
  //           padAngle: [
  //             {
  //               value: 0,
  //             },
  //           ],
  //           innerRadius: [
  //             {
  //               value: 140,
  //             },
  //           ],
  //           outerRadius: [
  //             {
  //               signal: "width / 2",
  //             },
  //           ],
  //         },
  //       },
  //     },
  //   ],
  // };

  return (
    <>
      <ListGroup
        items={items}
        heading="Test Options"
        onSelectItem={handleSelectItem}
      />
      <Vega spec={vegaSpec} actions={false} />
    </>
  );
}

export default App;
