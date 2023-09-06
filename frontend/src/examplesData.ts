const examplesData: any[] = [
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
  {
    "exampleName": "Multi-Continent Countries",
    "databaseId": "64e4b803fe3299654b1739bf",
    "databaseName": "mondial_fragment",
    "attributeIdList": [7, 9, 43],
    "vizId": "645c9696-f128-41ba-8789-3f42ee0e9564",
    "queryString": `SELECT 
	continent.name AS continent_name, 
	country.name AS country_name, 
	ROUND(country.area * encompasses.percentage / 100) AS encompasses_percentage
    
FROM encompasses
    
JOIN continent
ON encompasses.continent = continent.name
    
JOIN country
ON encompasses.country = country.code
    
WHERE country.code IN (SELECT country FROM encompasses GROUP BY country
    HAVING COUNT(DISTINCT continent) > 1)`,
  },
  {
    "exampleName": "Country Populations Over Time",
    "databaseId": "64e4b8f4fc72440674f39f11",
    "databaseName": "mondial_full",
    "attributeIdList": [16, 17, 18],
    "vizId": "c34f4864-43b0-46af-9e04-461fd07a5020",
    "queryString": `SELECT
	country.name AS country_population_country,
	country_population.year AS country_population_year,
	country_population.population AS country_population_population

FROM country_population

JOIN country 
ON country_population.country = country.code
AND country.name IN ('France', 'Spain', 'Italy')
AND country_population.year BETWEEN 1960 AND 2015`,
  },
  {
    "exampleName": "Islands in Waterbodies",
    "databaseId": "64e4b803fe3299654b1739bf",
    "databaseName": "mondial_fragment",
    "attributeIdList": [15, 17, 20],
    "vizId": "54bb78ff-e3b1-40e0-b6ff-8cdfd9f7997e",
    "queryString": `SELECT
	island.name AS island_name,
	island.area AS island_area,
	waterbody.name AS waterbody_name

FROM island

JOIN waterbody
ON island.in_waterbody = waterbody.name`,
  },
  {
    "exampleName": "European GDP vs. Unemployment",
    "databaseId": "64e4b803fe3299654b1739bf",
    "databaseName": "mondial_fragment",
    "attributeIdList": [9, 29, 34],
    "vizId": "ab98e432-02aa-4a21-b07c-115e51cec866",
    "queryString": `SELECT
    country.name AS country_name,
    economy.gdp AS economy_gdp,
    economy.unemployment AS economy_unemployment
  
FROM economy
  
JOIN country
ON economy.country = country.code
  
JOIN encompasses
ON encompasses.country = country.code
AND encompasses.continent = 'Europe'`,
  },
  {
    "exampleName": "South American Languages",
    "databaseId": "64e4b8f4fc72440674f39f11",
    "databaseName": "mondial_full",
    "attributeIdList": [9, 37, 38],
    "vizId": "dda59fcd-ff36-4cb6-8fad-49dd19df5ae0",
    "queryString": `-- Shows the languages spoken in South American countries
-- Limited to languages spoken by at least 1m people in the continent
    
WITH sa_languages AS (
SELECT
  spoken.name AS spoken_name,
  spoken.percentage AS spoken_percentage,
  country.name AS country_name,
  country.population AS country_pop,
  ROUND(country.population * (spoken.percentage / 100)) AS spoken_pop
    
FROM spoken
    
JOIN country
ON spoken.country = country.code
    
JOIN encompasses
ON encompasses.country = country.code
    
WHERE encompasses.continent = 'South America'
)
    
, language_totals AS (
SELECT 
  spoken_name, 
  SUM(spoken_pop) AS total_speakers
FROM sa_languages
GROUP BY spoken_name
)
    
SELECT *
FROM sa_languages
WHERE spoken_name NOT IN (
  SELECT spoken_name
  FROM language_totals
  WHERE total_speakers < 1000000
  OR total_speakers IS NULL
  )
ORDER BY spoken_name`,
  },
];

export default examplesData;
