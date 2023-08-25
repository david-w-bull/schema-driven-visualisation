import styled from "styled-components";
import { Divider, Avatar, List } from "antd";
import manyManyIcon from "../assets/many-to-many.svg";
import oneManyIcon from "../assets/one-to-many.svg";

interface VizSchemaInfoDisplayProps {
  vizSchema: any;
}

const VizSchemaInfoDisplay = ({ vizSchema }: VizSchemaInfoDisplayProps) => {
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
      <StyledDivider />
      <List itemLayout="horizontal">
        {vizSchema.keyOne && (
          <List.Item>
            <List.Item.Meta
              avatar={
                <Avatar
                  size="large"
                  style={{
                    backgroundColor: "#fde3cf",
                    color: "#f56a00",
                  }}
                >
                  K1
                </Avatar>
              }
              title={
                <>
                  <div>Key One</div>
                  <div style={{ fontWeight: 300 }}>{vizSchema.keyOneAlias}</div>
                </>
              }
            />
          </List.Item>
        )}
        {vizSchema.keyTwo && (
          <List.Item>
            <List.Item.Meta
              avatar={
                <Avatar
                  size="large"
                  style={{
                    backgroundColor: "#fde3cf",
                    color: "#f56a00",
                  }}
                >
                  K2
                </Avatar>
              }
              title={
                <>
                  <div>Key Two</div>
                  <div style={{ fontWeight: 300 }}>{vizSchema.keyTwoAlias}</div>
                </>
              }
            />
          </List.Item>
        )}
        {vizSchema.scalarOne && (
          <List.Item>
            <List.Item.Meta
              avatar={
                <Avatar
                  size="large"
                  style={{
                    backgroundColor: "#fde3cf",
                    color: "#f56a00",
                  }}
                >
                  A1
                </Avatar>
              }
              title={
                <>
                  <div>Scalar One</div>
                  <div style={{ fontWeight: 300 }}>
                    {vizSchema.scalarOneAlias}
                  </div>
                </>
              }
            />
          </List.Item>
        )}
      </List>
      <div>
        <div style={{ height: "40px" }}></div>
        <Header>Schema Relationship</Header>
        <SubHeader>
          Based on database metadata. Indicative of how data can be related
          between your selected tables
        </SubHeader>
        <div>
          {vizSchema.type == "ONETOMANY" ? (
            <RelationshipIcon src={oneManyIcon}></RelationshipIcon>
          ) : (
            <RelationshipIcon src={manyManyIcon}></RelationshipIcon>
          )}
        </div>
        <div style={{ height: "40px" }}></div>
        <Header>Data Relationship</Header>
        <SubHeader>
          The actual relationship exhibited by the data returned by your query.
          May not hold for all queries.
        </SubHeader>
        <div>
          {vizSchema.dataRelationship == "ONETOMANY" ? (
            <RelationshipIcon src={oneManyIcon}></RelationshipIcon>
          ) : (
            <RelationshipIcon src={manyManyIcon}></RelationshipIcon>
          )}
        </div>
      </div>
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
