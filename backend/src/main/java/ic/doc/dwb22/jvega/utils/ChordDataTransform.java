package ic.doc.dwb22.jvega.utils;

import java.util.*;

public class ChordDataTransform {

    private double[][] chordMatrix;
    private List<String> labels;

    public void generateChordMatrixFromJsonResultSet(List<Map<String, Object>> data) {

        // Create a set of unique country codes
        Set<String> countriesSet = new HashSet<>();
        for (Map<String, Object> row : data) {
            countriesSet.add((String) row.get("country1_code"));
            countriesSet.add((String) row.get("country2_code"));
        }
        List<String> countries = new ArrayList<>(countriesSet);

        // Create a lookup map to get the index of a country code
        Map<String, Integer> countryIndex = new HashMap<>();
        for (int i = 0; i < countries.size(); i++) {
            countryIndex.put(countries.get(i), i);
        }

        // Initialize the matrix with zeros
        double[][] matrix = new double[countries.size()][countries.size()];

        // Fill in the matrix with the border lengths
        for (Map<String, Object> row : data) {
            int i = countryIndex.get(row.get("country1_code"));
            int j = countryIndex.get(row.get("country2_code"));
            double length = Double.parseDouble((String) row.get("borders_length"));
            matrix[i][j] = length;
            matrix[j][i] = length; // If the relationship is symmetrical
        }

        // Print the matrix
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}
