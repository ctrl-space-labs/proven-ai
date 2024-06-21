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
  return axios.get(apiRequests.provenOrganizationById(organizationId), {
      headers: {
          'Content-Type': 'application/json',
          Authorization: 'Bearer ' + storedToken
      }
      
  });
}



/**
 *  Update the organization
 *  @param organizationDTO
 *  @param storedToken
 *  @returns {Promise<axios.AxiosResponse<Organization>>}
 */ 
  const updateOrganization = async (organizationDTO, storedToken) => {
    return axios.put(apiRequests.provenOrganizationById(organizationDTO.id), organizationDTO, {
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + storedToken,
      },
    });
  };


  /**
   * Registration a new organization
   * @param organizationDTO
   * @param storedToken
   * @returns {Promise<axios.AxiosResponse<Organization>>}
   
   */
  const createOrganization = async (organizationDTO, storedToken) => {
    return axios.post(apiRequests.organizationRegistration(), organizationDTO, {
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + storedToken,
      },
    });
  }
 

export default {
  getOrganizationsByCriteria,
  getProvenOrganizationById,
  updateOrganization,
  createOrganization
};
