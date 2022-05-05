import Axios from "axios";
import { Features, IDataSource, IFeature, IFeatureDetail, IFeatureLineage } from "../models/model";

const API_ENDPOINT = "https://feathr-api-test.azurewebsites.net";
const project = "feathr_awe_demo";
const token = "aa";

export const fetchDataSources = async () => {
  return Axios
    .get<IDataSource[]>(`${ API_ENDPOINT }/v0.1/projects/${ project }/datasources?code=${ token }`,
      { headers: {} })
    .then((response) => {
      return response.data;
    })
};

export const fetchFeatures = async (page: number, limit: number, keyword: string) => {
  return Axios
    .get<Features>(`${ API_ENDPOINT }/v0.1/projects/${ project }/features?code=${ token }`,
      {
        params: { 'keyword': keyword, 'page': page, 'limit': limit },
        headers: {}
      })
    .then((response) => {
      return response.data.features;
    })
};

export const createFeature = async (feature: IFeature) => {
  return Axios
    .post(`${ API_ENDPOINT }/features`, feature,
      {
        headers: { "Content-Type": "application/json;" },
        params: {},
      }).then((response) => {
      return response;
    }).catch((error) => {
      return error.response;
    });
}

export const fetchFeature = async (qualifiedName: string) => {
  return Axios
    .get<IFeatureDetail>(`${ API_ENDPOINT }/v0.1/features/${ qualifiedName }?code=${ token }`, {})
    .then((response) => {
      return response.data?.entity?.attributes;
    })
};

export const fetchProjectLineages = async () => {
  return Axios
    .get<IFeatureLineage>(`${ API_ENDPOINT }/v0.1/features/lineage/${ project }?code=${ token }`, {})
    .then((response) => {
      return response.data;
    })
};

export const deleteFeature = async (qualifiedName: string) => {
  return await Axios
    .delete(`${ API_ENDPOINT }/features/${ qualifiedName }`,
      {
        headers: { "Content-Type": "application/json;" },
        params: {},
      }).then((response) => {
      return response
    }).catch((error) => {
      return error.response
    });
};

export const updateFeature = async (feature: IFeature, id: string) => {
  feature.id = id;
  return await Axios.put(`${ API_ENDPOINT }/features/${ id }`, feature,
    {
      headers: { "Content-Type": "application/json;" },
      params: {},
    }).then((response) => {
    return response
  }).catch((error) => {
    return error.response
  });
};
