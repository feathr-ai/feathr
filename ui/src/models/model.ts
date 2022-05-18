export interface Features {
  features: IFeature[];
}

export interface IFeature {
  id: string;
  name: string;
  qualifiedName: string;
  description: string;
  status: string;
  featureType: string;
  dataSource: string;
  owners: string;
}

export interface IFeatureDetail {
  entity: IFeatureEntity;
}

export interface IFeatureEntity {
  attributes: FeatureAttributes;
}

export interface FeatureAttributes {
  qualifiedName: string;
  name: string;
  type: string;
  transformation: FeatureTransformation;
  key: FeatureKey[];
  window: string;
  input_anchor_features: InputAnchorFeatures[];
  input_derived_features: InputDerivedFeatures[]
}

export interface InputAnchorFeatures {
  uniqueAttributes: FeatureAttributes;
}

export interface InputDerivedFeatures {
  uniqueAttributes: FeatureAttributes;
}

export interface FeatureTransformation {
  transform_expr: string,
  filter: string,
  agg_func: string,
  limit: string,
  group_by: string,
  window: string,
  def_expr: string
}

export interface FeatureKey {
  full_name: string,
  key_column: string,
  description: string,
  key_column_alias: string,
  key_column_type: string
}

export interface IDataSource {
  attributes: DataSourceAttributes;
}

export interface DataSourceAttributes {
  name: string;
  qualifiedName: string;
  path: string;
}


export interface IFeatureLineage {
  guidEntityMap: any;
  relations: any;
}

