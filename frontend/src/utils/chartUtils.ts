import { CardinalityLimits, ChartTypes as RecommendedCharts } from "../types";

export const categorizeCharts = (
  charts: string[],
  cardinalityLimits: CardinalityLimits,
  vizCardinality: number
): RecommendedCharts => {
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
