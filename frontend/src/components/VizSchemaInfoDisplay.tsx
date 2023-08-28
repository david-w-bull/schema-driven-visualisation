import styled from "styled-components";
import { Divider, Avatar, List, Col, Row } from "antd";
import DataTable from "./DataTable";
import FieldInfoDisplay from "./FieldInfoDisplay";

interface VizSchemaInfoDisplayProps {
  vizSchema: any;
}

const VizSchemaInfoDisplay = ({ vizSchema }: VizSchemaInfoDisplayProps) => {
  function getRelationshipLabel(dataRelationship: string) {
    switch (dataRelationship) {
      case "ONETOMANY":
        return "1:M";
      case "MANYTOMANY":
        return "M:N";
      case "BASIC":
        return "BASIC";
      case "SUBSET":
        return "SUBSET";
      case "WEAK":
        return "WEAK";

      default:
        return "Unknown";
    }
  }

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        paddingTop: "20px",
      }}
    >
      <Header>Visualisation Schema</Header>
      <SubHeader>Information about the matched visualisation pattern</SubHeader>
      <StyledDivider style={{ paddingBottom: "30px" }} />
      <Row style={{ paddingBottom: "30px" }}>
        {vizSchema.keyOneAlias && (
          <FieldInfoDisplay
            text="K1"
            title="Key One"
            description={vizSchema.keyOneAlias}
          />
        )}
        {vizSchema.keyTwoAlias && (
          <FieldInfoDisplay
            text="K2"
            title="Key Two"
            description={vizSchema.keyTwoAlias}
          />
        )}
        {vizSchema.scalarOneAlias && (
          <FieldInfoDisplay
            text="A1"
            title="Scalar One"
            description={vizSchema.scalarOneAlias}
          />
        )}
        {vizSchema.scalarTwoAlias && (
          <FieldInfoDisplay
            text="A2"
            title="Scalar Two"
            description={vizSchema.scalarTwoAlias}
          />
        )}
      </Row>
      <Row style={{ paddingBottom: "30px" }}>
        <FieldInfoDisplay
          text={getRelationshipLabel(vizSchema.type)}
          title="Schema Relationship"
          backgroundColor="#c2eafc"
          color="#007bb2"
          shape="square"
          description="The relationship between entities in database metadata"
        />
        {vizSchema.dataRelationship &&
          vizSchema.dataRelationship !== vizSchema.type && (
            <FieldInfoDisplay
              text={getRelationshipLabel(vizSchema.dataRelationship)}
              title="Data Relationship"
              backgroundColor="#c2eafc"
              color="#007bb2"
              shape="square"
              description="The relationship between keys in your query results"
            />
          )}
      </Row>
      <div style={{ height: "20px" }}></div>
      <Header>Example Data</Header>
      <SubHeader>
        A small sample of data demonstrating the data relationship
      </SubHeader>
      <StyledDivider />
      <div style={{ height: "20px" }}></div>
      <DataTable data={vizSchema.exampleData} scrollHeight={300}></DataTable>
    </div>
  );
};

export default VizSchemaInfoDisplay;

const StyledDivider = styled(Divider)`
  margin: 2px 0px 0px 0px;
`;

const Header = styled.h1`
  font-size: 30px;
  font-family: "Roboto", "Helvetica", "Arial", sans-serif;
  font-weight: 500;
  margin: 0px;
`;

const SubHeader = styled.p`
  font-size: 16px;
  font-family: "Roboto", "Helvetica", "Arial", sans-serif;
  font-weight: 300;
  margin: 0px;
`;

const RelationshipIcon = styled.img`
    width: 70px;
    }
`;
