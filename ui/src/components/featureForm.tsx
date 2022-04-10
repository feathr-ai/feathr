import React, { CSSProperties, useEffect, useState } from 'react';
import { BackTop, Button, Form, Input, message, Space } from 'antd';
import { createFeature, updateFeature } from '../api';
import { Redirect } from 'react-router';
import { UpCircleOutlined } from '@ant-design/icons'
import { IFeature } from "../models/feature";

type FeatureFormProps = {
  isDisabled: boolean;
  feature?: IFeature;
};

const FeatureForm: React.FC<FeatureFormProps> = ({ isDisabled, feature }) => {
  const [fireRedirect, setRedirect] = useState<boolean>(false);
  const [createLoading, setCreateLoading] = useState<boolean>(false);
  const [form] = Form.useForm();

  useEffect(() => {
    if (feature !== undefined) {
      form.setFieldsValue(feature);
    }
  }, []);

  const onClickSave = async () => {
    setCreateLoading(true);
    const feature: IFeature = form.getFieldsValue();
    if (feature?.id !== undefined) {
      const resp = await updateFeature(feature, feature.id);
      if (resp.status === 200) {
        message.success("Feature is updated successfully");
        setRedirect(true);
      } else {
        message.error(`${ resp.data }`, 8)
      }
    } else {
      const resp = await createFeature(feature);
      if (resp.status === 201) {
        message.success("New feature created");
        setRedirect(true);
      } else {
        message.error(`${ resp.data }`, 8)
      }
    }
    setCreateLoading(false);
  }

  const styling: CSSProperties = { width: "92%" }
  return (
    <>
      <Form
        form={ form }
        style={ styling }
        labelCol={ { span: 4 } }
        wrapperCol={ { span: 24 } }
        layout="horizontal"
        initialValues={ { remember: true } }
      >
        <Space direction="vertical" size="large" style={ styling }>
          <h3>Feature Information</h3>
          <Form.Item name="name" label="Name" rules={ [{ required: true, message: 'Name is required' }] }>
            <Input disabled={ isDisabled } />
          </Form.Item>
          <Form.Item name="description" label="Description">
            <Input disabled={ isDisabled } />
          </Form.Item>
          <Form.Item name="status" label="Status">
            <Input disabled={ isDisabled } />
          </Form.Item>
          <Form.Item name="featureType" label="Feature Type">
            <Input disabled={ isDisabled } />
          </Form.Item>
          <Form.Item name="dataSource" label="Data Source">
            <Input disabled={ isDisabled } />
          </Form.Item>
          <Form.Item name="owners" label="Owners">
            <Input disabled={ isDisabled } />
          </Form.Item>
        </Space>
        <Form.Item wrapperCol={ { offset: 11 } }>
          <Button type="primary" htmlType="button" title="submit and go back to list"
                  style={ { float: 'inline-start' } }
                  onClick={ onClickSave }
                  loading={ createLoading }
                  disabled={ isDisabled }
          >
            Submit
          </Button>
        </Form.Item>
        <BackTop style={ { marginBottom: '5%', marginRight: '20px' } }><UpCircleOutlined
          style={ { fontSize: '400%', color: '#3F51B5' } } /></BackTop>
      </Form>
      { fireRedirect && (<Redirect to={ '/' }></Redirect>) }
    </>
  );
};

export default FeatureForm
