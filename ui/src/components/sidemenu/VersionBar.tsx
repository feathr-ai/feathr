import React from "react";
import { Space } from "antd";
import dayjs from "dayjs";

export interface VersionBarProps {
  className?: string;
}
const VersionBar = (props: VersionBarProps) => {
  const { className } = props;
  const generatedTime = dayjs(process.env.FEATHR_GENERATED_TIME)
    .utc()
    .format("YYYY-MM-DD HH:mm:DD UTC");

  return (
    <Space className={className} direction="vertical" size="small">
      <span>Feathr UI Version: {process.env.FEATHR_VERSION}</span>
      <span>Feathr UI Build Generated at {generatedTime}</span>
    </Space>
  );
};

export default VersionBar;
