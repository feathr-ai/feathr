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
  definition: string;
  def: FeatureDef;
  "def.sqlExpr": string;
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

export interface FeatureDef {
  sqlExpr: string;
}

export interface FeatureTransformation {
  transform_expr: string
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

