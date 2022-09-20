import React, { CSSProperties } from "react";
import type { DataNode } from "antd/es/tree";
import { UpCircleOutlined } from "@ant-design/icons";
import {
  BackTop,
  Button,
  Form,
  Input,
  Select,
  Space,
  Typography,
  Tree,
} from "antd";

type Props = {
  onSubmit: any;
  newFeatureProp: any;
};

const ReviewAndCreate = ({ onSubmit, newFeatureProp }: Props) => {
  const treeData: DataNode[] = [
    {
      title: "Name",
      key: "Name",
    },
  ];
  const { Title } = Typography;
  return (
    <div>
      <Title level={4}>New Feature Review</Title>
      <p>
        {" "}
        <tr></tr>Name : {newFeatureProp.name} <br></br>
      </p>
      <p>
        {" "}
        <tr></tr>Qualified Name : {newFeatureProp.qualifiedName} <br></br>
      </p>
      <p>
        <tr></tr>Tags : {newFeatureProp.tags} <br></br>
      </p>
      {/* <p>
        <tr></tr>Feature Keys : {newFeatureProp.keys} <br></br>
      </p>
      <tr></tr>Feature Type : <br></br>
      <p>
        <tr></tr>Type : {newFeatureProp.type.typeName} <br></br>
      </p>
      <p>
        <tr></tr>Tensor Category : {newFeatureProp.type.tensorCategory}{" "}
        <br></br>
      </p>
      <p>
        <tr></tr>Tensor Category : {newFeatureProp.type.dimensionType} <br></br>
      </p>
      <p>
        <tr></tr>Tensor Category : {newFeatureProp.type.valueType} <br></br>
      </p>
      <p>
        <tr></tr>Transformation : <br></br>
      </p>
      <p>
        <tr></tr>Tensor Category : {newFeatureProp.transformation.transformExpr}{" "}
        <br></br>
      </p>
      <p>
        <tr></tr>Tensor Category : {newFeatureProp.transformation.window}{" "}
        <br></br>
      </p>
      <p>
        <tr></tr>Tensor Category :{" "}
        {newFeatureProp.transformation.udfTransformation} <br></br>
      </p> */}
      <Button
        type="primary"
        htmlType="button"
        title="submit to create this feature"
        style={{ float: "inline-start", marginTop: "5%" }}
        onClick={() => {
          onSubmit();
        }}
      >
        Submit
      </Button>
    </div>
  );
};
export default ReviewAndCreate;
