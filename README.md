![Java](https://img.shields.io/badge/Java-17.0.8-brightgreen.svg)
![Typescript](https://img.shields.io/badge/Typescript-4.9.3-blue.svg)
![React](https://img.shields.io/badge/React-18.2.0-61DAFB.svg)
![Vite](https://img.shields.io/badge/Vite-4.1.0-blueviolet.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0.7-6DB33F.svg)

# Schema Driven Visualisation (SDViz)

## Project Overview
This is an application for visualisation recommendation, designed to assist with exploratory analysis of relational databases.

The application was developed as part of an MSc project at Imperial College London, based on the approach set out by [McBrien and Poulovassilis (2018)](https://www.doc.ic.ac.uk/automed/techreports/MP18a.pdf).

Using this approach, data visualisations are recommended according to selected attributes, but also according to their roles and relationships in the underlying database schema.

## Project Structure
This high-level project structure (ommitting standard files generated as part of the frameworks) is as follows:

**frontend**
- **src**: top-level application files including App.tsx
    - **utils**: helper functions defined for the project
    - **components**: all React components developed as part of the project
      - **charts**: specific React components for rendering Ant-V charts.
 
**<u>backend</u>**
- **src/main**
  - **resources**: includes a number of .json file for application examples and testing.
    - **static**: holds frontend build files
  - **java**: top-level classes, including 'Application.java' which is the application entry point 
    - **api**: classes providing endpoints (*Service* and *Controller* classes) and MongoDB *Repository* classes
    - **schema**: classes for relational database profiling
    - **spec**: classes for producing Vega specifications (also available separately as the *jVega* library)
    - **utils**: utility classes for handling file inputs and type conversions
    - **vizSchema**: classes for mapping database schemas to a visualisation schema patterns
   

### Frontend 
The frontend is implemented in *TypeScript*, using *React.js* and built with *Vite*.

### Backend
The backend is implemented in *Java*, using *Spring Boot* and built with *Maven*.

### External Libraries and Third-Party Technologies
- Application data, such as database schema information and visualisation specifications are stored in MongoDB.
- Example databases are hosted using Azure PostgreSQL Server.
- Database profiling functionality is built on the [Amazing-ER](https://github.com/BoanZhu/ER-API) library.
- The creation of Vega visualisation specifications uses jVega, which was developed as part of this project. 

## Running the Application Locally
*Please note that the application is not set up to run locally out-of-the-box, given that the implementation relies on external resources (MongoDB and Azure PostgreSQL) that are not public.*

*Running the application locally requires MongoDB and PostgreSQL credentials to be added as environment variables. So new resources would need to be created for your local environment, or the default credentials can be provided on request.*

### Prerequisites
Before getting started, make sure you have the following installed:
- [Node.js and npm](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm)
- [Java 17](https://www.oracle.com/uk/java/technologies/downloads/#java17)
- [Maven](https://maven.apache.org/)
- [Docker](https://www.docker.com/)

### Populate Credentials
Update the credentials used for MongoDB and the application's relational databases as follows:
1. Navigate to 'backend/src/main/resources'
2. Populate the credentials listed in '.env.example' and rename the file '.env'

### Launch Locally with Docker
It is recommended to launch the app using the provided dockerfile template. This can be done as follows:
1. Navigate to the 'backend' directory which contains the 'Dockerfile.example' template
2. Populate the ENV placeholder variables in Dockerfile.example with relevant details for your project, and rename the file as 'Dockerfile'
3. Run `docker build -t my_image .` in the 'backend' directory to build the Docker image
4. Run `docker run -p 8080:8080 my_image`.
5. The app should now be accessible at http://localhost:8080 (frontend files are packaged with the backend meaning that the full application should be running).

### Launch the Frontend
The frontend build files are included in 'backend/src/main/resources/static' meaning that the steps above will start the full application.
However, if you would like to start the frontend development server separately, follow these steps:
1. Navigate to the 'frontend' directory
2. Run `npm install` to install the required Node.js packages
3. Run `npm run dev`
4. The app should now be accessible at http://localhost:5173

### Production Application
The production application is deployed on Heroku and is accessible at [schema-driven-visualisation.com](http://schema-driven-visualisation.com)
