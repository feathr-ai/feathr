export interface Project {
  name: string;
}

export interface Feature {
  attributes: FeatureAttributes;
  displayText: string;
  guid: string;
  labels: string[];
  name: string;
  qualifiedName: string;
  status: string;
  typeName: string;
  version: string;
}

export interface FeatureAttributes {
  inputAnchorFeatures: InputFeature[];
  inputDerivedFeatures: InputFeature[];
  key: FeatureKey[];
  name: string;
  qualifiedName: string;
  tags: Object;
  transformation: FeatureTransformation;
  type: FeatureType;
}

export interface FeatureType {
  dimensionType: string[];
  tensorCategory: string;
  type: string;
  valType: string;
}

export interface FeatureTransformation {
  transformExpr: string;
  filter: string;
  aggFunc: string;
  limit: string;
  groupBy: string;
  window: string;
  defExpr: string;
}

export interface FeatureKey {
  description: string;
  fullName: string;
  keyColumn: string;
  keyColumnAlias: string;
  keyColumnType: string;
}

export interface InputFeature {
  guid: string;
  typeName: string;
  uniqueAttributes: InputFeatureAttributes;
}

export interface InputFeatureAttributes {
  qualifiedName: string;
  version: string;
}

export interface DataSource {
  attributes: DataSourceAttributes;
  displayText: string;
  guid: string;
  lastModifiedTS: string;
  status: string;
  typeName: string;
  version: string;
}

export interface DataSourceAttributes {
  eventTimestampColumn: string;
  name: string;
  path: string;
  preprocessing: string;
  qualifiedName: string;
  tags: string[];
  timestampFormat: string;
  type: string;
  qualified_name: string;
  timestamp_format: string;
  event_timestamp_column: string;
}

export interface RelationData {
  fromEntityId: string;
  relationshipId: string;
  relationshipType: string;
  toEntityId: string;
}

export interface FeatureLineage {
  guidEntityMap: Record<string, Feature>;
  relations: RelationData[];
}

export interface UserRole {
  id: number;
  scope: string;
  userName: string;
  roleName: string;
  createBy: string;
  createTime: string;
  createReason: string;
  deleteBy: string;
  deleteTime?: any;
  deleteReason?: any;
  access?: string;
}

export interface Role {
  scope: string;
  userName: string;
  roleName: string;
  reason: string;
}
