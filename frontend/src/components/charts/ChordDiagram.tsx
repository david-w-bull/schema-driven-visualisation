import React, { useEffect, useRef, useState } from "react";
import DataSet from "@antv/data-set";
import { Chart } from "@antv/g2";
import { VizSchema } from "../../types";

interface ChordDiagramProps {
  vizSchema: VizSchema;
}

const ChordDiagram = ({ vizSchema }: ChordDiagramProps) => {
  const containerRef = useRef(null);
  const [data, setData] = useState<any>(null);
  const [chartRendered, setChartRendered] = useState(false);

  console.log("In the chord component");
  console.log(vizSchema.sqlQuery);

  const testData = {
    nodes: [
      {
        id: 0,
        name: "analytics.cluster",
        value: 21,
      },
      {
        id: 1,
        name: "analytics.graph",
        value: 34,
      },
      {
        id: 2,
        name: "analytics.optimization",
        value: 8,
      },
      {
        id: 3,
        name: "animate",
        value: 40,
      },
      {
        id: 4,
        name: "animate.interpolate",
        value: 18,
      },
      {
        id: 5,
        name: "data.converters",
        value: 25,
      },
      {
        id: 6,
        name: "data",
        value: 10,
      },
      {
        id: 7,
        name: "display",
        value: 4,
      },
    ],
    links: [
      {
        source: 3,
        target: 3,
        sourceWeight: 30,
        targetWeight: 30,
      },
      {
        source: 4,
        target: 4,
        sourceWeight: 16,
        targetWeight: 16,
      },
      {
        source: 5,
        target: 6,
        sourceWeight: 17,
        targetWeight: 2,
      },
      {
        source: 6,
        target: 6,
        sourceWeight: 7,
        targetWeight: 7,
      },
      {
        source: 5,
        target: 5,
        sourceWeight: 7,
        targetWeight: 7,
      },
      {
        source: 0,
        target: 0,
        sourceWeight: 5,
        targetWeight: 5,
      },
      {
        source: 7,
        target: 7,
        sourceWeight: 3,
        targetWeight: 3,
      },
      {
        source: 1,
        target: 3,
        sourceWeight: 5,
        targetWeight: 0,
      },
      {
        source: 1,
        target: 1,
        sourceWeight: 1,
        targetWeight: 1,
      },
      {
        source: 0,
        target: 3,
        sourceWeight: 2,
        targetWeight: 0,
      },
    ],
  };

  useEffect(() => {
    fetch(
      "https://gw.alipayobjects.com/os/antvdemo/assets/data/relationship-with-weight.json"
    )
      .then((res) => res.json())
      .then((data) => {
        setData(data);
        setData(testData);
      });
  }, []);

  useEffect(() => {
    if (data && containerRef.current && !chartRendered) {
      const ds = new DataSet();
      const dv = ds.createView().source(data, {
        type: "graph",
        edges: (d) => d.links,
      });
      dv.transform({
        type: "diagram.arc",
        sourceWeight: (e) => e.sourceWeight,
        targetWeight: (e) => e.targetWeight,
        weight: true,
        marginRatio: 0.3,
      });

      const chart = new Chart({
        container: containerRef.current,
        autoFit: true,
        height: 500,
        padding: 30,
      });
      chart.legend(false);
      chart.tooltip({
        showTitle: false,
        showMarkers: false,
      });
      chart.scale({
        x: {
          sync: true,
          nice: true,
        },
        y: {
          sync: true,
          nice: true,
          max: 1,
        },
      });

      const edgeView = chart.createView();
      edgeView.data(dv.edges);
      edgeView.coordinate("polar").reflect("y");
      edgeView.axis(false);
      edgeView
        .edge()
        .position("x*y")
        .shape("arc")
        .color("source")
        .tooltip("source*target*value")
        .style({
          fillOpacity: 0.5,
        });

      const nodeView = chart.createView();
      nodeView.data(dv.nodes);
      nodeView.coordinate("polar").reflect("y");
      nodeView.axis(false);
      nodeView
        .polygon()
        .position("x*y")
        .color("id")
        .label("name", {
          labelEmit: true,
          style: {
            fill: "#8c8c8c",
          },
        })
        .style({
          fillOpacity: 0.5,
        });
      chart.interaction("element-active");

      chart.render();
      setChartRendered(true);
    }
  }, [data, chartRendered]);

  return <div ref={containerRef}></div>;
};

export default ChordDiagram;
