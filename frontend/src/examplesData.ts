const examplesData: any[] = [
  {
    "exampleName": "Airport Elevation",
    "databaseId": "64e4b8f4fc72440674f39f11",
    "databaseName": "mondial_full",
    "attributeIdList": [1, 5],
    "vizId": "7b32ad40-1024-449e-b424-d4ced7c59104",
    "queryString":
      "SELECT \n" +
      "\tairport.iata_code AS airport_iata_code,\n" +
      "\tairport.elevation AS airport_elevation\n\n" +
      "FROM airport\n\n" +
      "WHERE airport.elevation > 100",
  },
  {
    "exampleName": "South American Borders",
    "databaseId": "64e4b803fe3299654b1739bf",
    "databaseName": "mondial_fragment",
    "attributeIdList": [9, 42],
    "vizId": "72a20c8a-d649-4714-ad85-50f1bd761550",
    "queryString": `SELECT 
	country2.name AS country2_name, 
	country1.name AS country1_name, 
	borders.length AS borders_length
    
    FROM borders
    
    JOIN country AS country2
    ON borders.country2 = country2.code
    
    JOIN country AS country1
    ON borders.country1 = country1.code
    
    JOIN encompasses
    ON encompasses.country = country1.code
    AND encompasses.continent = 'South America'`,
  },
];

export default examplesData;
