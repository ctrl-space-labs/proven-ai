import axios from "axios";
import apiRequests from "src/configs/apiRequest.js";

/**
 * Get all proven AI organization by organizationId
 * @param List<String> organizationIds
 * @param token
 * @returns {Promise<axios.AxiosResponse<List<Organization>>}
 */
const getOrganizationsByCriteria = async (organizationIds, token) => {
  return axios.get(apiRequests.getOrganizationsByCriteria(organizationIds), {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    }

  });
};


/**
 * Get VC Offer URL
 * @param token
 * @param organizationId
 * @returns {Promise<axios.AxiosResponse<{credentialVerificationUrl: string}>>}
 */
const getVcOfferUrl = async (token, organizationId, redirectURL) => {
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
        Authorization: "Bearer " + token,
        }
    });

}


/**
 * Get all organization users
 * @param organizationId
 * @param token
 * @returns {Promise<axios.AxiosResponse<Organization>>}
 */
const getProvenOrganizationById = async (organizationId, token) => {
  return axios.get(apiRequests.provenOrganizationById(organizationId), {
      headers: {
          'Content-Type': 'application/json',
          Authorization: 'Bearer ' + token
      }

  });
}



/**
 *  Update the organization
 *  @param organizationDTO
 *  @param token
 *  @returns {Promise<axios.AxiosResponse<Organization>>}
 */
  const updateOrganization = async (organizationDTO, token) => {
    return axios.put(apiRequests.provenOrganizationById(organizationDTO.id), organizationDTO, {
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + token,
      },
    });
  };


  /**
   * Registration a new organization
   * @param organizationDTO
   * @param token
   * @returns {Promise<axios.AxiosResponse<Organization>>}

   */
  const createOrganization = async (organizationDTO, token) => {
    return axios.post(apiRequests.organizationRegistration(), organizationDTO, {
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + token,
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
