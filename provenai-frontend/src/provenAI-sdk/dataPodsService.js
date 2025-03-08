import axios from "axios";
import apiRequests from "src/configs/apiRequest.js";

/**
 * Get all data pods by organizationId
 * @param organizationId
 * @param token
 * @returns {Promise<axios.AxiosResponse<DataPods>>}
 */
const getDataPodsByOrganization = async (organizationId, token) => {
  return axios.get(apiRequests.dataPods(), {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    },
    params: {
      organizationId: organizationId,
    },
  });
};


/**
 * Get all data pods
 * @param token
 * @returns {Promise<axios.AxiosResponse<DataPods>>}
 */
const getAllDataPods = async (token) => {
  return axios.get(apiRequests.dataPods(), {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    },
  });
};


/**
 * Get public data pods by data pod ids in
 * @param token
 * @Param dataPodIdIn
 * @returns {Promise<axios.AxiosResponse<DataPods>>}
 */
const getDataPodsByIdIn = async (dataPodIdIn, token) => {
  return axios.get(apiRequests.publicDataPods(), {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    },
    params: {
      dataPodIdIn: dataPodIdIn,
    },
  });
};

/**
 * Get data pod by id
 * @param dataPodId
 * @param token
 * @returns {Promise<axios.AxiosResponse<DataPod>>}
 */
const getDataPodById = async (dataPodId, token) => {
  return axios.get(apiRequests.getDataPodById(dataPodId), {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    }
  });
};

/**
 * Get ACL policies by data pod id
 * @param dataPodId
 * @param token
 * @returns {Promise<axios.AxiosResponse<AclPolicies>>}
 */
const getAclPoliciesByDataPod = async (dataPodId, token) => {
  return axios.get(apiRequests.getAclPoliciesByDataPod(dataPodId), {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    }

  });
};

/**
 * Create a new data pod
 * @param dataPodDTO
 * @param token
 * @returns {Promise<axios.AxiosResponse<DataPod>>}
 */
const createDataPod = async (dataPodDTO, token) => {
  return axios.post(apiRequests.dataPods(), dataPodDTO, {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    },
  });
}

export default {
  getDataPodsByOrganization,
  getDataPodsByIdIn,
  getAclPoliciesByDataPod,
  getDataPodById,
  createDataPod,
  getAllDataPods
};
