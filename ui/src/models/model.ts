export interface Feature {
  id: string;
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
  _input_anchor_features: Feature[];
  _input_derived_features: Feature[]
}

export interface FeatureType {
  dimensionType: string[],
  tensorCategory: string,
  type: string,
  valType: string
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

export interface DataSource {
  attributes: DataSourceAttributes;
}

export interface DataSourceAttributes {
  name: string;
  qualifiedName: string;
  path: string;
}


export interface FeatureLineage {
  guidEntityMap: any;
  relations: any;
}

export interface UserRole {
  id: number;
  scope: string;
  userName: string;
  roleName: string;
  createTime: string;
  createReason: string;
  deleteTime?: any;
  deleteReason?: any;
}

export interface Role {
  scope: string;
  userName: string;
  roleName: string;
  reason: string;
}

export interface EnvConfig {
  azureClientId?: string;
  azureTenantId?: string;
}
