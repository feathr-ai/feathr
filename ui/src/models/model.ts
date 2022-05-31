export interface IFeature {
  guid: string;
  status: string;
  displayText: string;
  typeName: string;
  attributes: FeatureAttributes;
}

export interface FeatureAttributes {
  qualifiedName: string;
  name: string;
  type: FeatureType;
  transformation: FeatureTransformation;
  key: FeatureKey[];
  window: string;
  input_anchor_features: InputAnchorFeatures[];
  input_derived_features: InputDerivedFeatures[]
}

export interface FeatureType {
  type: string,
  tensor_category: string,
  dimension_type: string[],
  val_type: string
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

export interface IUserRole {
  id: number;
  scope: string;
  userName: string;
  roleName: string;
  createTime: string;
  createReason: string;
  deleteTime?: any;
  deleteReason?: any;
}

export interface RoleForm {
  scope: string;
  userName: string;
  roleName: string;
  reason: string;
}
