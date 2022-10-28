import React from "react";
import { Select } from "antd";
import { fetchProjects } from "@/api";
import { useQuery } from "react-query";

export interface DataSourcesSelectProps {
  width?: number;
  defaultValue?: string;
  onChange?: (value: string) => void;
}

const DataSourcesSelect = (props: DataSourcesSelectProps) => {
  const { width = 350, defaultValue, onChange, ...restProps } = props;

  const { isLoading, data: options } = useQuery<
    { value: string; label: string }[]
  >(
    ["dataSources"],
    async () => {
      try {
        const result = await fetchProjects();
        return result.map((item) => ({
          value: item,
          label: item,
        }));
      } catch (e) {
        return Promise.reject(e);
      }
    },
    {
      retry: false,
      refetchOnWindowFocus: false,
    }
  );

  return (
    <Select
      style={{ width }}
      showSearch
      defaultValue={defaultValue}
      loading={isLoading}
      placeholder="Project Name"
      options={options}
      notFoundContent={"No projects found from server"}
      onChange={onChange}
      {...restProps}
    />
  );
};

export default DataSourcesSelect;
