import axios from "axios";
import apiRequests from "src/configs/apiRequest.js";

/**
 *  Create the acl policy
 *  @param aclPolicyDTO
 *  @param storedToken
 *  @returns {Promise<axios.AxiosResponse<AclPolicy>>}
 */
const createAclPolicy = async (aclPolicyDTO, storedToken) => {
  console.log("aclPolicyDTO", aclPolicyDTO);
  return axios.post(apiRequests.aclPolicies(), aclPolicyDTO, {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + storedToken,
    },
  });
};

/**
 * Delete acl policies
 * @param {Array<UUID>} aclPolicyIds
 * @param storedToken
 * @returns {Promise<axios.AxiosResponse>}
 */
const deleteAclPolicies = async (aclPolicyIds, storedToken) => {
  console.log("aclPolicyIds", aclPolicyIds);
  const queryParams = aclPolicyIds.map(id => `aclPolicyIds=${id}`).join('&');
  const url = `${apiRequests.aclPolicies()}?${queryParams}`;

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
