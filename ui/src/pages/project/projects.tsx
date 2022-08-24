import React from "react";
import { Card, Typography } from "antd";
import ProjectList from "../../components/projectList";

const { Title } = Typography;

const Projects = () => {
  return (
    <div className="page">
      <Card>
        <Title level={3}>Projects</Title>
        <ProjectList />
      </Card>
    </div>
  );
};

export default Projects;
