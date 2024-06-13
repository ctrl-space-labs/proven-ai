import axios from "axios";
import apiRequests from "src/configs/apiRequest.js";

/**
 * Get all proven AI organization by organizationId
 * @param List<String> organizationIds
 * @param storedToken
 * @returns {Promise<axios.AxiosResponse<List<Organization>>}
 */
const getOrganizationsByCriteria = async (organizationIds, storedToken) => {
  return axios.get(apiRequests.getOrganizationsByCriteria(organizationIds), {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + storedToken,
    }
    
  });
};


/**
 * Get all organization users
 * @param organizationId 
 * @param storedToken
 * @returns {Promise<axios.AxiosResponse<Organization>>}
 */
const getProvenOrganizationById = async (organizationId, storedToken) => {
  return axios.get(apiRequests.getProvenOrganizationById(organizationId), {
      headers: {
          'Content-Type': 'application/json',
          Authorization: 'Bearer ' + storedToken
      }
      
  });
}

export default {
  getOrganizationsByCriteria,
  getProvenOrganizationById
};
