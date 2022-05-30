import Axios from "axios";
import { Features, IDataSource, IFeature, IFeatureDetail, IFeatureLineage, IUserRole, RoleForm } from "../models/model";
import mockUserRole from "./mock/userrole.json";

const API_ENDPOINT = "https://feathr-api-test.azurewebsites.net";
const purview = "feathrazuretest3-purview1"
const token = "mockAppServiceKey";

export const fetchDataSources = async (project: string) => {
  return Axios
    .get<IDataSource[]>(`${ API_ENDPOINT }/v1/purview/${ purview }/projects/${ project }/datasources?code=${ token }`,
      { headers: {} })
    .then((response) => {
      return response.data;
    })
};

export const fetchProjects = async () => {
  return Axios
    .get<[]>(`${ API_ENDPOINT }/v1/purview/${ purview }/projects?code=${ token }`,
      {
        headers: {}
      })
    .then((response) => {
      return response.data;
    })
};

export const fetchFeatures = async (project: string, page: number, limit: number, keyword: string) => {
  return Axios
    .get<Features>(`${ API_ENDPOINT }/v1/purview/${ purview }/projects/${ project }/features?code=${ token }`,
      {
        params: { 'keyword': keyword, 'page': page, 'limit': limit },
        headers: {}
      })
    .then((response) => {
      return response.data.features;
    })
};

export const fetchFeature = async (project: string, qualifiedName: string) => {
  return Axios
    .get<IFeatureDetail>(`${ API_ENDPOINT }/v1/purview/${ purview }/features/${ qualifiedName }?code=${ token }`, {})
    .then((response) => {
      return response.data?.entity?.attributes;
    })
};

export const fetchProjectLineages = async (project: string) => {
  return Axios
    .get<IFeatureLineage>(`${ API_ENDPOINT }/v1/purview/${ purview }/features/lineage/${ project }?code=${ token }`, {})
    .then((response) => {
      return response.data;
    })
};

// Following are place-holder code
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

export const listUserRole = async () => {
  let data:IUserRole[] = mockUserRole
  return data
};

export const getUserRole = async (userName: string) => {
  return await Axios
  .get<IUserRole>(`${ API_ENDPOINT }/user/${userName}/userroles?code=${ token }`, {})
  .then((response) => {
    return response.data;
  })
}

export const addUserRole = async (roleForm: RoleForm) => {
  return await Axios
  .post(`${ API_ENDPOINT }/user/${roleForm.userName}/userroles/new`, roleForm,
      {
        headers: { "Content-Type": "application/json;" },
        params: {},
      }).then((response) => {
      return response;
    }).catch((error) => {
      return error.response;
    });
}

export const deleteUserRole = async (roleForm: RoleForm) => {
  return await Axios
  .post(`${ API_ENDPOINT }/user/${roleForm.userName}/userroles/delete`, roleForm,
      {
        headers: { "Content-Type": "application/json;" },
        params: {},
      }).then((response) => {
      return response;
    }).catch((error) => {
      return error.response;
    });
}