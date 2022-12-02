import { useState } from "react";
import { PageHeader } from "antd";
import { useSearchParams } from "react-router-dom";
import SearchBar, { SearchValue } from "./components/SearchBar";
import FeatureTable from "./components/FeatureTable";

const Features = () => {
  const [searchParams] = useSearchParams();

  const [search, setProject] = useState<SearchValue>({
    project: searchParams.get("project") || undefined,
    keyword: searchParams.get("keyword") || undefined,
  });

  const onSearch = (values: SearchValue) => {
    setProject(values);
  };

  return (
    <div className="page">
      <PageHeader ghost={false} title="Features">
        <SearchBar defaultValues={search} onSearch={onSearch} />
        <FeatureTable project={search.project} keyword={search.keyword} />
      </PageHeader>
    </div>
  );
};

export default Features;
