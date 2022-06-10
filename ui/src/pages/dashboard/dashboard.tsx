import React from 'react';
import { Avatar, Button, List, Space, Card, Col, Row, Typography } from 'antd';
import { CopyOutlined, DatabaseOutlined, DashboardFilled, EyeOutlined, ProjectOutlined, RocketOutlined } from '@ant-design/icons';
import {
  LineChart,
  ResponsiveContainer,
  Legend, Tooltip,
  AreaChart, Area,
  Line,
  XAxis,
  YAxis,
  CartesianGrid
} from 'recharts';

const { Title, Text, Link } = Typography;
type Props = {};
type CardSummaryProps = {
  iconName: string;
  title: string;
  subtitle: string;
};



const CardSummary: React.FC<CardSummaryProps> = ({ iconName, title, subtitle }) => {
  return (
    <Col span={6}>
      <Card style={{ marginTop: "15px", marginRight: "15px", boxShadow: "5px 8px 15px 5px rgba(208, 216, 243, 0.6)", borderRadius: "8px" }}>
        <Row>
          <Col span={6}>
            <ProjectOutlined style={{ fontSize: '80px', color: '#177ddc' }} />
          </Col>
          <Col span={18}>
            <Row>
              <Col span={8}>
                <Title level={2}>{title}</Title>
              </Col>
            </Row>
            <Row>
              <Col span={24}>
                <span>{subtitle}
                  <a target="_blank" rel="noreferrer"
                    href="https://linkedin.github.io/feathr/concepts/feathr-concepts-for-beginners.html"> Learn More</a>
                </span>
              </Col>
            </Row>
          </Col>
        </Row>
      </Card>
    </Col>
  )
}

const data = [
  {
      name: 'May 20',
      store1: 11,
      store2: 5
  },
  {
      name: 'May 21',
      store1: 15,
      store2: 12
  },
  {
      name: 'May 22',
      store1: 5,
      store2: 10
  },
  {
      name: 'May 23',
      store1: 10,
      store2: 5
  },
  {
      name: 'May 24',
      store1: 9,
      store2: 4
  },
  {
      name: 'May 25',
      store1: 10,
      store2: 8
  },
  {
      name: 'May 26',
      store1: 10,
      store2: 28
  }
];


const monitoringData = [
  {
      name: 'May 20',
      f1: 90,
      f2: 80
  },
  {
      name: 'May 21',
      f1: 95,
      f2: 83
  },
  {
      name: 'May 22',
      f1: 99,
      f2: 85
  },
  {
      name: 'May 23',
      f1: 85,
      f2: 20
  },
  {
      name: 'May 24',
      f1: 91,
      f2: 80
  },
  {
      name: 'May 25',
      f1: 94,
      f2: 90
  },
  {
      name: 'May 26',
      f1: 88,
      f2: 92
  },
];


const Dashboard: React.FC<Props> = () => {
  return (
    <div className="home" style={{ margin: "2%" }}>
      <Card style={{ minWidth: '1000px', boxShadow: "5px 8px 15px 5px rgba(208, 216, 243, 0.6)", borderRadius: "8px" }}>
        <Title level={2}>Welcome to Feathr Feature Store</Title>
        <span>In Feathr Feature Store, you can manage and share features.
          <a target="_blank" rel="noreferrer"
            href="https://linkedin.github.io/feathr/concepts/feathr-concepts-for-beginners.html"> Learn More</a>
        </span>
      </Card>
      <>
        <Row justify="space-between">
          <CardSummary iconName="" title="Projects" subtitle="10 projects" />
          <Col span={6}>
            <Card style={{ marginTop: "15px", marginRight: "15px", boxShadow: "5px 8px 15px 5px rgba(208, 216, 243, 0.6)", borderRadius: "8px" }}>
              <Row>
                <Col span={6}>
                  <DatabaseOutlined style={{ fontSize: '80px', color: '#219ebc' }} />
                </Col>
                <Col span={18}>
                  <Row>
                    <Col span={24}>
                      <Title level={2}>Sources</Title>
                    </Col>
                  </Row>
                  <Row>
                    <Col span={24}>
                      <span>30 sources
                        <a target="_blank" rel="noreferrer"
                          href="https://linkedin.github.io/feathr/concepts/feathr-concepts-for-beginners.html"> Learn More</a>
                      </span>
                    </Col>
                  </Row>
                </Col>
              </Row>
            </Card>
          </Col>
          <Col span={6}>
            <Card style={{ marginTop: "15px", marginRight: "15px", boxShadow: "5px 8px 15px 5px rgba(208, 216, 243, 0.6)", borderRadius: "8px" }}>
              <Row>
                <Col span={6}>
                  <CopyOutlined style={{ fontSize: '80px', color: '#ffb703' }} />
                </Col>
                <Col span={18}>
                  <Row>
                    <Col span={24}>
                      <Title level={2}>Features</Title>
                    </Col>
                  </Row>
                  <Row>
                    <Col span={24}>
                      <span>310 features
                        <a target="_blank" rel="noreferrer"
                          href="https://linkedin.github.io/feathr/concepts/feathr-concepts-for-beginners.html"> Learn More</a>
                      </span>
                    </Col>
                  </Row>
                </Col>
              </Row>
            </Card>
          </Col>
          <Col span={6}>
            <Card style={{ marginTop: "15px", boxShadow: "5px 8px 15px 5px rgba(208, 216, 243, 0.6)", borderRadius: "8px" }}>
              <Row>
                <Col span={6}>
                  <EyeOutlined style={{ fontSize: '80px', color: '#fb8500' }} />
                </Col>
                <Col span={18}>
                  <Row>
                    <Col span={24}>
                      <Title level={2}>Monitoring</Title>
                    </Col>
                  </Row>
                  <Row>
                    <Col span={24}>
                      <span>30 monitoring jobs
                        <a target="_blank" rel="noreferrer"
                          href="https://linkedin.github.io/feathr/concepts/feathr-concepts-for-beginners.html"> Learn More</a>
                      </span>
                    </Col>
                  </Row>
                </Col>
              </Row>
            </Card>
          </Col>
        </Row>
      </>
      <>
        <Row>
          <Col span={16}>
            <Row>
              <Col span={12}>
                <Card style={{ marginTop: "15px", marginRight: "15px", boxShadow: "5px 8px 15px 5px rgba(208, 216, 243, 0.6)", borderRadius: "8px" }}>
                <Title level={2}>Feature Store Ingestion Volume</Title>
                  <AreaChart width={670} height={300} data={data}
                    margin={{ top: 10, right: 30, left: 0, bottom: 0 }}>
                    <defs>
                      <linearGradient id="colorUv" x1="store1" y1="0" x2="0" y2="1">
                        <stop offset="5%" stopColor="#8884d8" stopOpacity={0.8}/>
                        <stop offset="95%" stopColor="#8884d8" stopOpacity={0}/>
                      </linearGradient>
                      <linearGradient id="colorPv" x1="0" y1="0" x2="0" y2="1">
                        <stop offset="5%" stopColor="#82ca9d" stopOpacity={0.8}/>
                        <stop offset="95%" stopColor="#82ca9d" stopOpacity={0}/>
                      </linearGradient>
                    </defs>
                    <XAxis dataKey="name" />
                    <YAxis />
                    <CartesianGrid strokeDasharray="3 3" />
                    <Legend verticalAlign="top" height={36}/>
                    <Tooltip />
                    <Area type="monotone" dataKey="store1" stroke="#8884d8" fillOpacity={1} fill="url(#colorUv)" />
                    <Area type="monotone" dataKey="store2" stroke="#82ca9d" fillOpacity={1} fill="url(#colorPv)" />
                  </AreaChart>
                </Card>
              </Col>
              <Col span={12}>
                <Card style={{ marginTop: "15px", marginRight: "15px", boxShadow: "5px 8px 15px 5px rgba(208, 216, 243, 0.6)", borderRadius: "8px" }}>
                <Title level={2}>Feature Monitoring Pipeline Health</Title>
                <AreaChart width={670} height={300} data={monitoringData}
                    margin={{ top: 10, right: 30, left: 0, bottom: 0 }}>
                    <defs>
                      <linearGradient id="colorUv" x1="store1" y1="0" x2="0" y2="1">
                        <stop offset="5%" stopColor="#8884d8" stopOpacity={0.8}/>
                        <stop offset="95%" stopColor="#8884d8" stopOpacity={0}/>
                      </linearGradient>
                      <linearGradient id="colorPv" x1="0" y1="0" x2="0" y2="1">
                        <stop offset="5%" stopColor="#82ca9d" stopOpacity={0.8}/>
                        <stop offset="95%" stopColor="#82ca9d" stopOpacity={0}/>
                      </linearGradient>
                    </defs>
                    <XAxis dataKey="name" />
                    <YAxis />
                    <CartesianGrid strokeDasharray="3 3" />
                    <Legend verticalAlign="top" height={36}/>
                    <Tooltip />
                    <Area type="monotone" dataKey="f1" stroke="#8884d8" fillOpacity={1} fill="url(#colorUv)" />
                    <Area type="monotone" dataKey="f2" stroke="#82ca9d" fillOpacity={1} fill="url(#colorPv)" />
                  </AreaChart>
                </Card>
              </Col>
            </Row>
            <Row>
              <Col span={24}>
                <Card style={{ marginTop: "15px", marginRight: "15px", minWidth: '1000px', boxShadow: "5px 8px 15px 5px rgba(208, 216, 243, 0.6)", borderRadius: "8px" }}>
                  <Title level={2}>Features You Might be Interested</Title>
                  <span>You might be interested in these features:
                    <Button type="link">member_feature_x</Button>
                    <Button type="link">member_feature_y</Button>
                    <Button type="link">job_feature_x</Button>
                    <Button type="link">job_feature_y</Button>
                  </span>
                </Card>
              </Col>
            </Row>
            <Row>
              <Col span={24}>
                <Card style={{ marginTop: "15px", marginRight: "15px", minWidth: '1000px', boxShadow: "5px 8px 15px 5px rgba(208, 216, 243, 0.6)", borderRadius: "8px" }}>
                  <Title level={2}>You recently worked on these features</Title>
                  <span>You recently worked on these features:
                    <Button type="link">my_feature_x</Button>
                    <Button type="link">my_feature_y</Button>
                  </span>
                </Card>
              </Col>
            </Row>
            <Row>
              <Col span={24}>
              <Card style={{ marginTop: "15px", marginRight: "15px", minWidth: '1000px', boxShadow: "5px 8px 15px 5px rgba(208, 216, 243, 0.6)", borderRadius: "8px" }}>
                  <Title level={2}>Need more helps?</Title>
                  <span>Create a Github issue
                    <a target="_blank" rel="noreferrer"
                      href="https://linkedin.github.io/feathr/concepts/feathr-concepts-for-beginners.html"> Learn More</a>
                  </span>
                </Card>
              </Col>
            </Row>
          </Col>
          <Col span={8}>
            <Card style={{ marginTop: "15px", boxShadow: "5px 8px 15px 5px rgba(208, 216, 243, 0.6)", borderRadius: "8px" }}>
              <Title level={2}>Recent Activity</Title>
              <List
                  itemLayout="horizontal"
                >
                    <List.Item>
                      <Text>
                        <CopyOutlined style={{ fontSize: '20px', color: '#13a8a8' }} />
                        1 new feature created in your<Link href="https://linkedin.github.io/feathr/concepts/feathr-concepts-for-beginners.html" target="_blank"> project (Link)</Link>
                      </Text>
                    </List.Item>
                    <List.Item>
                      <Text type="success"><RocketOutlined style={{ fontSize: '20px', color: '#642ab5' }} /> Your feature materialization job for f1 has completed.</Text>
                    </List.Item>
                    <List.Item>
                      <Text type="warning"><RocketOutlined style={{ fontSize: '20px', color: '#642ab5' }} /> Your feature materialization job for f1 and f2 has failed.</Text>
                    </List.Item>
                    <List.Item>
                      <Text type="danger"><EyeOutlined style={{ fontSize: '20px', color: '#e84749' }} /> You have one feature monitoring alert for feature f1 and f2.</Text>
                    </List.Item>
                    <List.Item>
                      <Text type="success"><RocketOutlined style={{ fontSize: '20px', color: '#642ab5' }} /> Your feature materialization job f3 has completed.</Text>
                    </List.Item>
                </List>
            </Card>
          </Col>
        </Row>
      </>
    </div>
  );
};

export default Dashboard;
