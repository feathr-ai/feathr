import Axios from "axios";
import { DataSource, Feature, FeatureLineage, UserRole, Role } from "../models/model";
import mockUserRole from "./mock/userrole.json";

const API_ENDPOINT = process.env.REACT_APP_API_ENDPOINT + "/api/v1";
const token = "mockAppServiceKey";

export const fetchDataSources = async (project: string) => {
  return Axios
    .get<DataSource[]>(`${ API_ENDPOINT }/projects/${ project }/datasources?code=${ token }`,
      { headers: {} })
    .then((response) => {
      return response.data;
    })
};

export const fetchProjects = async () => {
  return Axios
    .get<[]>(`${ API_ENDPOINT }/projects?code=${ token }`,
      {
        headers: {}
      })
    .then((response) => {
      return response.data;
    })
};

export const fetchFeatures = async (project: string, page: number, limit: number, keyword: string) => {
  return Axios
    .get<Feature[]>(`${ API_ENDPOINT }/projects/${ project }/features?code=${ token }`,
      {
        params: { 'keyword': keyword, 'page': page, 'limit': limit },
        headers: {}
      })
    .then((response) => {
      return response.data;
    })
};

export const fetchFeature = async (project: string, featureId: string) => {
  return Axios
    .get<Feature>(`${ API_ENDPOINT }/features/${ featureId }?code=${ token }`, {})
    .then((response) => {
      return response.data;
    })
};

export const fetchProjectLineages = async (project: string) => {
  return Axios
    .get<FeatureLineage>(`${ API_ENDPOINT }/projects/${ project }?code=${ token }`, {})
    .then((response) => {
      return response.data;
    })
};

export const fetchFeatureLineages = async (project: string) => {
  return Axios
    .get<FeatureLineage>(`${ API_ENDPOINT }/features/lineage/${ project }?code=${ token }`, {})
    .then((response) => {
      return response.data;
    })
};

// Following are place-holder code
export const createFeature = async (feature: Feature) => {
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

export const updateFeature = async (feature: Feature, id: string) => {
  feature.guid = id;
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
  let data:UserRole[] = mockUserRole
  return data
};

export const getUserRole = async (userName: string) => {
  return await Axios
  .get<UserRole>(`${ API_ENDPOINT }/user/${userName}/userroles?code=${ token }`, {})
  .then((response) => {
    return response.data;
  })
}

export const addUserRole = async (role: Role) => {
  return await Axios
  .post(`${ API_ENDPOINT }/user/${role.userName}/userroles/new`, role,
      {
        headers: { "Content-Type": "application/json;" },
        params: {},
      }).then((response) => {
      return response;
    }).catch((error) => {
      return error.response;
    });
}

export const deleteUserRole = async (role: Role) => {
  return await Axios
  .post(`${ API_ENDPOINT }/user/${role.userName}/userroles/delete`, role,
      {
        headers: { "Content-Type": "application/json;" },
        params: {},
      }).then((response) => {
      return response;
    }).catch((error) => {
      return error.response;
    });
}
