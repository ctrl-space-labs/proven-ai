import axios from "axios";
import apiRequests from "src/configs/apiRequest.js";

/**
 * Get all data pods by organizationId
 * @param organizationId
 * @param storedToken
 * @returns {Promise<axios.AxiosResponse<DataPods>>}
 */
const getDataPodsByOrganization = async (organizationId, storedToken) => {
  return axios.get(apiRequests.dataPods(), {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + storedToken,
    },
    params: {
      organizationId: organizationId,
    },
  });
};

/**
 * Get data pod by id
 * @param dataPodId
 * @param storedToken
 * @returns {Promise<axios.AxiosResponse<DataPod>>}
 */
const getDataPodById = async (dataPodId, storedToken) => {
  return axios.get(apiRequests.getDataPodById(dataPodId), {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + storedToken,
    }    
  });
};

/**
 * Get ACL policies by data pod id
 * @param dataPodId
 * @param storedToken
 * @returns {Promise<axios.AxiosResponse<AclPolicies>>}
 */
const getAclPoliciesByDataPod = async (dataPodId, storedToken) => {
  return axios.get(apiRequests.getAclPoliciesByDataPod(dataPodId), {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + storedToken,
    }
    
  });
};

/**
 * Create a new data pod
 * @param dataPodDTO 
 * @param storedToken
 * @returns {Promise<axios.AxiosResponse<DataPod>>}
 */
const createDataPod = async (dataPodDTO, storedToken) => {
  return axios.post(apiRequests.dataPods(), dataPodDTO, {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + storedToken,
    },
  });
}

export default {
  getDataPodsByOrganization,
  getAclPoliciesByDataPod,
  getDataPodById,
  createDataPod,
};
