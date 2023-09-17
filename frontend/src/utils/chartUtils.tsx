import { CardinalityLimits, ChartRecommendations, VizSchema } from "../types";

import ChordDiagramTest from "../components/charts/ChordDiagramTest";
import GroupedBar from "../components/charts/GroupedBar";
import StackedBar from "../components/charts/StackedBar";
import ScatterPlot from "../components/charts/ScatterPlot";
import WordCloud from "../components/charts/WordCloud";
import BarSlider from "../components/charts/BarSlider";
import Treemap from "../components/charts/Treemap";
import SankeyDiagram from "../components/charts/SankeyDiagram";
import LineChart from "../components/charts/LineChart";

/**
 * Renders the correct chart type based on provided information, usually passed from a user selection.
 * @param chartType {string} The type of chart that should be rendered, passed from parent object
 * @param vizSchema {VizSchema} The VizSchema containing the dataset and field information for the visualisation
 * @returns A component which provides a visualisaion of the specificied chart type
 */
export const renderChartComponent = (
  chartType: string,
  vizSchema: VizSchema
) => {
  switch (chartType) {
    case "Bar Chart":
      return <BarSlider vizSchema={vizSchema} />;
    case "Word Cloud":
      return <WordCloud vizSchema={vizSchema} />;
    case "Chord Diagram":
      return <ChordDiagramTest vizSchema={vizSchema} />;
    case "Grouped Bar Chart":
      return <GroupedBar vizSchema={vizSchema} />;
    case "Stacked Bar Chart":
      return <StackedBar vizSchema={vizSchema} />;
    case "Scatter Plot":
      return <ScatterPlot vizSchema={vizSchema} />;
    case "Treemap":
      return <Treemap vizSchema={vizSchema} />;
    case "Sankey Diagram":
      return <SankeyDiagram vizSchema={vizSchema} />;
    case "Line Chart":
      return <LineChart vizSchema={vizSchema} />;
    default:
      return null;
  }
};

/**
 * Compares the recommended chart types returned by the backend and compares them to cardinality thresholds
 * @param charts {string[]} A list of all the recommended charts returned by the backend
 * @param cardinalityLimits {CardinalityLimits} An object mapping chart type to cardinality threshold
 * @param vizCardinality {number} The maximum cardinality across all keys in the VizSchema
 * @param keyOneCardinality {number} The specific cardinality of key one, used by line charts
 * @returns Three arrays of strings for different levels of chart recommendations
 */
export const categorizeCharts = (
  charts: string[],
  cardinalityLimits: CardinalityLimits,
  vizCardinality: number,
  keyOneCardinality: number
): ChartRecommendations => {
  const Recommended: string[] = [];
  const Possible: string[] = [];
  const Other: string[] = [];
  if (charts != undefined) {
    for (const chart of charts) {
      if (cardinalityLimits[chart] !== undefined) {
        if (
          chart === "Line Chart" &&
          cardinalityLimits[chart] >= keyOneCardinality
        ) {
          Recommended.push(chart);
        } else if (cardinalityLimits[chart] >= vizCardinality) {
          Recommended.push(chart);
        } else {
          Possible.push(chart);
        }
      }
    }

    // Any chart types not returned by the backend go to the 'not recommended' list
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

/**
 * Provides a copy of a given JSON object.
 * Not for use with complex objects as relies on conversion to string type
 */
export const copyJson = (jsonObject: any): any => {
  return JSON.parse(JSON.stringify(jsonObject));
};

/**
 * A helper function to swap all KeyOne.. fields with KeyTwo... fields in a VizSchema
 * Primarily used for composite key visualisations where a hierarchy needs to be created between keys
 * @param vizSchema {VizSchema} A VizSchema object containing keys to be swapped
 * @returns A VizSchema objects with KeyOne... and KeyTwo... fields swapped
 */
export const swapKeyFields = (vizSchema: VizSchema): VizSchema => {
  const {
    keyOne,
    keyOneAlias,
    keyOneCardinality,
    keyTwo,
    keyTwoAlias,
    keyTwoCardinality,
    ...rest
  } = vizSchema;

  return {
    ...rest,
    keyOne: keyTwo,
    keyOneAlias: keyTwoAlias,
    keyOneCardinality: keyTwoCardinality,
    keyTwo: keyOne,
    keyTwoAlias: keyOneAlias,
    keyTwoCardinality: keyOneCardinality,
  };
};
