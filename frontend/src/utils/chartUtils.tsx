import { CardinalityLimits, ChartRecommendations, VizSchema } from "../types";

import ChordDiagramTest from "../components/charts/ChordDiagramTest";
import GroupedBar from "../components/charts/GroupedBar";
import StackedBar from "../components/charts/StackedBar";
import ScatterPlot from "../components/charts/ScatterPlot";

export const renderChartComponent = (
  chartType: string,
  vizSchema: VizSchema
) => {
  switch (chartType) {
    case "Chord Diagram":
      return <ChordDiagramTest vizSchema={vizSchema} />;
    case "Grouped Bar Chart":
      return <GroupedBar vizSchema={vizSchema} />;
    case "Stacked Bar Chart":
      return <StackedBar vizSchema={vizSchema} />;
    case "Scatter Plot":
      return <ScatterPlot vizSchema={vizSchema} />;
    default:
      return null;
  }
};

export const categorizeCharts = (
  charts: string[],
  cardinalityLimits: CardinalityLimits,
  vizCardinality: number
): ChartRecommendations => {
  const Recommended: string[] = [];
  const Possible: string[] = [];
  const Other: string[] = [];
  if (charts != undefined) {
    for (const chart of charts) {
      if (cardinalityLimits[chart] !== undefined) {
        if (cardinalityLimits[chart] >= vizCardinality) {
          Recommended.push(chart);
        } else {
          Possible.push(chart);
        }
      }
    }

    for (const key in cardinalityLimits) {
      if (charts.indexOf(key) === -1) {
        Other.push(key);
      }
    }
  } else {
    for (const key in cardinalityLimits) {
      Other.push(key);
    }
  }

  return {
    Recommended,
    Possible,
    Other,
  };
};
