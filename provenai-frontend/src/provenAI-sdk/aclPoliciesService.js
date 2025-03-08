import axios from "axios";
import apiRequests from "src/configs/apiRequest.js";

/**
 *  Create the acl policy
 *  @param aclPolicyDTO
 *  @param token
 *  @returns {Promise<axios.AxiosResponse<AclPolicy>>}
 */
const createAclPolicy = async (aclPolicyDTO, token) => {
  return axios.post(apiRequests.aclPolicies(aclPolicyDTO.dataPodId), aclPolicyDTO, {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    },
  });
};

/**
 * Delete acl policies
 * @param dataPodId
 * @param {Array<UUID>} aclPolicyIds
 * @param token
 * @returns {Promise<axios.AxiosResponse>}
 */
const deleteAclPolicies = async (dataPodId, aclPolicyIds, token) => {
  const queryParams = aclPolicyIds.map(id => `aclPolicyIds=${id}`).join('&');
  const url = `${apiRequests.aclPolicies(dataPodId)}?${queryParams}`;

  return axios.delete(url, {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    }

  });
};





export default {
  createAclPolicy,
  deleteAclPolicies,
};
