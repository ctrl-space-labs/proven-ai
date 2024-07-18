import axios from "axios";
import apiRequests from "src/configs/apiRequest.js";

/**
 *  Create the acl policy
 *  @param aclPolicyDTO
 *  @param storedToken
 *  @returns {Promise<axios.AxiosResponse<AclPolicy>>}
 */
const createAclPolicy = async (aclPolicyDTO, storedToken) => {
  return axios.post(apiRequests.aclPolicies(aclPolicyDTO.dataPodId), aclPolicyDTO, {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + storedToken,
    },
  });
};

/**
 * Delete acl policies
 * @param dataPodId
 * @param {Array<UUID>} aclPolicyIds
 * @param storedToken
 * @returns {Promise<axios.AxiosResponse>}
 */
const deleteAclPolicies = async (dataPodId, aclPolicyIds, storedToken) => {
  const queryParams = aclPolicyIds.map(id => `aclPolicyIds=${id}`).join('&');
  const url = `${apiRequests.aclPolicies(dataPodId)}?${queryParams}`;

  return axios.delete(url, {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + storedToken,
    }
    
  });
};





export default {
  createAclPolicy,
  deleteAclPolicies,
};
