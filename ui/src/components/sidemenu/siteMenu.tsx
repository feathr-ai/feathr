import { Layout, Menu, Typography } from "antd";
import {
  CopyOutlined,
  DatabaseOutlined,
  EyeOutlined,
  RocketOutlined,
} from "@ant-design/icons";
import { Link } from "react-router-dom";

const { Title } = Typography;
const { Sider } = Layout;

const SideMenu = () => {
  return (
    <Sider theme="dark">
      <Title
        level={1}
        style={{
          fontSize: "36px",
          color: "white",
          margin: "10px",
          paddingLeft: "35px",
        }}
      >
        Feathr
      </Title>
      <Menu
        theme="dark"
        mode="inline"
        defaultSelectedKeys={["/"]}
        defaultOpenKeys={["/"]}
      >
        <Menu.Item
          key="/dataSources"
          icon={
            <DatabaseOutlined style={{ fontSize: "20px", color: "#13a8a8" }} />
          }
        >
          <Link to="/dataSources">Data Sources</Link>
        </Menu.Item>
        <Menu.Item
          key="/features"
          icon={<CopyOutlined style={{ fontSize: "20px", color: "#d89614" }} />}
        >
          <Link to="/features">Features</Link>
        </Menu.Item>
        <Menu.Item
          key="/jobs"
          icon={
            <RocketOutlined style={{ fontSize: "20px", color: "#642ab5" }} />
          }
        >
          <Link to="/jobs">Jobs</Link>
        </Menu.Item>
        <Menu.Item
          key="/monitoring"
          icon={<EyeOutlined style={{ fontSize: "20px", color: "#e84749" }} />}
        >
          <Link to="/monitoring">Monitoring</Link>
        </Menu.Item>
      </Menu>
    </Sider>
  );
};

export default SideMenu;
