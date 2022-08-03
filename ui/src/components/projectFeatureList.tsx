import React, { useCallback, useEffect, useState } from "react";
import { Form, Select, Table } from "antd";

const ProjectFeatureList: React.FC = () => {
  const [project, setProject] = useState("");
  const [feature, setFeature] = useState("");
  const [features, setFeatures] = useState([]);
  return (
    <div>
      <Form.Item
        label="Select Feature: "
        style={{ minWidth: "35%", float: "left", paddingLeft: "10px" }}
        rules={[
          {
            required: true,
            message: "Please select a feature to check.",
          },
        ]}
      >
        <Select
          options={features}
          defaultValue={feature}
          value={feature}
          optionFilterProp="label"
          notFoundContent={<div>No projects found from server</div>}
          //onChange={onFeatureChange}
        ></Select>
      </Form.Item>
    </div>
  );
};

export default ProjectFeatureList;
