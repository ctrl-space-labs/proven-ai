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
 * Get VC Offer URL
 * @param storedToken
 * @param organizationId
 * @returns {Promise<axios.AxiosResponse<{credentialVerificationUrl: string}>>}
 */
const getVcOfferUrl = async (storedToken, organizationId, redirectURL) => {
    return axios.post(apiRequests.provenGetVcOfferUrl(organizationId, redirectURL), {
      "vc_policies": [
        "expired",
        "not-before"
      ],
      "request_credentials": [
        "NaturalPersonVerifiableID",
        "VerifiableID",
        "VerifiableId",
        "eIDAS2PID",
        "LegalEntityVerifiableID",
        "VerifiablePID"
      ]
    }, {
        headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + storedToken,
        }
    });

}


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
  createOrganization,
  getVcOfferUrl
};
