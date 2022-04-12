export interface IFeature {
  id: string;
  name: string;
  description: string;
  status: string;
  featureType: string;
  dataSource: string;
  owners: string;
}

export interface IDataSource {
  id?: string;
  name: string;
  type: string;
  event_timestamp_column: string;
  path: string;
}

