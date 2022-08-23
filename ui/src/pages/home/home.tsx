import React from "react";
import { Link } from "react-router-dom";
import { Card, Col, Row, Typography } from "antd";
import {
  CopyOutlined,
  DatabaseOutlined,
  EyeOutlined,
  ProjectOutlined,
} from "@ant-design/icons";

const { Title } = Typography;

const Home = () => {
  return (
    <div className="page">
      <Card
        style={{
          boxShadow: "5px 8px 15px 5px rgba(208, 216, 243, 0.6)",
          borderRadius: "8px",
        }}
      >
        <Title level={2}>Welcome to Feathr Feature Store</Title>
        <span>
          You can use Feathr UI to search features, identify data sources, track
          feature lineages and manage access controls.
          <a
            target="_blank"
            href="https://linkedin.github.io/feathr/concepts/feature-registry.html#accessing-feathr-ui"
            rel="noreferrer"
          >
            {" "}
            Learn More
          </a>
        </span>
      </Card>
      <Row justify="space-between">
        <Col span={6}>
          <Card
            style={{
              marginTop: "15px",
              marginRight: "15px",
              boxShadow: "5px 8px 15px 5px rgba(208, 216, 243, 0.6)",
              borderRadius: "8px",
            }}
          >
            <Row>
              <Col span={6}>
                <ProjectOutlined
                  style={{ fontSize: "80px", color: "#177ddc" }}
                />
              </Col>
              <Col span={18}>
                <Row>
                  <Col span={24}>
                    <Title level={2}>Projects</Title>
                  </Col>
                </Row>
                <Row>
                  <Col span={24}>
                    <span>
                      <Link to="/projects">See all</Link>
                    </span>
                  </Col>
                </Row>
              </Col>
            </Row>
          </Card>
        </Col>
        <Col span={6}>
          <Card
            style={{
              marginTop: "15px",
              marginRight: "15px",
              boxShadow: "5px 8px 15px 5px rgba(208, 216, 243, 0.6)",
              borderRadius: "8px",
            }}
          >
            <Row>
              <Col span={6}>
                <DatabaseOutlined
                  style={{ fontSize: "80px", color: "#219ebc" }}
                />
              </Col>
              <Col span={18}>
                <Row>
                  <Col span={24}>
                    <Title level={2}>Sources</Title>
                  </Col>
                </Row>
                <Row>
                  <Col span={24}>
                    <span>
                      <Link to="/dataSources">See all</Link>
                    </span>
                  </Col>
                </Row>
              </Col>
            </Row>
          </Card>
        </Col>
        <Col span={6}>
          <Card
            style={{
              marginTop: "15px",
              marginRight: "15px",
              boxShadow: "5px 8px 15px 5px rgba(208, 216, 243, 0.6)",
              borderRadius: "8px",
            }}
          >
            <Row>
              <Col span={6}>
                <CopyOutlined style={{ fontSize: "80px", color: "#ffb703" }} />
              </Col>
              <Col span={18}>
                <Row>
                  <Col span={24}>
                    <Title level={2}>Features</Title>
                  </Col>
                </Row>
                <Row>
                  <Col span={24}>
                    <span>
                      <Link to="/features">See all</Link>
                    </span>
                  </Col>
                </Row>
              </Col>
            </Row>
          </Card>
        </Col>
        <Col span={6}>
          <Card
            style={{
              marginTop: "15px",
              boxShadow: "5px 8px 15px 5px rgba(208, 216, 243, 0.6)",
              borderRadius: "8px",
            }}
          >
            <Row>
              <Col span={6}>
                <EyeOutlined style={{ fontSize: "80px", color: "#fb8500" }} />
              </Col>
              <Col span={18}>
                <Row>
                  <Col span={24}>
                    <Title level={2}>Monitoring</Title>
                  </Col>
                </Row>
                <Row>
                  <Col span={24}>
                    <span>
                      <Link to="/monitoring">See all</Link>
                    </span>
                  </Col>
                </Row>
              </Col>
            </Row>
          </Card>
        </Col>
      </Row>
      <Row>
        <Col span={16}>
          <Row>
            <Col span={24}>
              <Card
                style={{
                  marginTop: "15px",
                  marginRight: "15px",
                  boxShadow: "5px 8px 15px 5px rgba(208, 216, 243, 0.6)",
                  borderRadius: "8px",
                }}
              >
                <Title level={2}>Need help to get started?</Title>
                Explore the following resources to get started with Feathr:
                <ul>
                  <li>
                    <a
                      target="_blank"
                      href="https://github.com/linkedin/feathr#-documentation"
                      rel="noreferrer"
                    >
                      Documentation
                    </a>{" "}
                    provides docs for getting started
                  </li>
                  <li>
                    <a
                      target="_blank"
                      href="https://github.com/linkedin/feathr#%EF%B8%8F-running-feathr-on-cloud-with-a-few-simple-steps"
                      rel="noreferrer"
                    >
                      Running Feathr on Cloud
                    </a>{" "}
                    describes how to run Feathr to Azure with Databricks or
                    Synapse
                  </li>
                  <li>
                    <a
                      target="_blank"
                      href="https://github.com/linkedin/feathr#%EF%B8%8F-cloud-integrations-and-architecture"
                      rel="noreferrer"
                    >
                      Cloud Integrations and Architecture on Cloud
                    </a>{" "}
                    describes Feathr architecture
                  </li>
                  <li>
                    <a
                      target="_blank"
                      href="https://github.com/linkedin/feathr#-slack-channel"
                      rel="noreferrer"
                    >
                      Slack Channel
                    </a>{" "}
                    describes how to join Slack channel for questions and
                    discussions
                  </li>
                  <li>
                    <a
                      target="_blank"
                      href="https://github.com/linkedin/feathr#-community-guidelines"
                      rel="noreferrer"
                    >
                      Community Guidelines
                    </a>{" "}
                    describes how to contribute to Feathr
                  </li>
                </ul>
                <p>
                  Visit
                  <a
                    target="_blank"
                    rel="noreferrer"
                    href="https://linkedin.github.io/feathr/concepts/feathr-concepts-for-beginners.html"
                  >
                    {" "}
                    Feathr Github Homepage
                  </a>{" "}
                  to learn more.
                </p>
              </Card>
            </Col>
          </Row>
        </Col>
        <Col span={8}>
          <Row>
            <Col span={24}>
              <Card
                style={{
                  marginTop: "15px",
                  boxShadow: "5px 8px 15px 5px rgba(208, 216, 243, 0.6)",
                  borderRadius: "8px",
                }}
              >
                <Title level={2}>Recent Activity</Title>
                <span>Under construction</span>
              </Card>
            </Col>
          </Row>
        </Col>
      </Row>
    </div>
  );
};

export default Home;
