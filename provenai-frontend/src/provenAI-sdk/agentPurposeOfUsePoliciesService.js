import axios from "axios";
import apiRequests from "src/configs/apiRequest.js";

/**
 *  Create the Agent Purpose Of Use policies
 *  @param agentPurposeOfUsePolicyDTO
 *  @param token
 *  @returns {Promise<axios.AxiosResponse<AgentPurposeOfUsePolicy>>}
 */
const createAgentPurposeOfUsePolicy = async (agentPurposeOfUsePolicyDTO, token) => {
  return axios.post(apiRequests.agentPurposeOfUsePolicies(agentPurposeOfUsePolicyDTO.agentId), agentPurposeOfUsePolicyDTO, {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    },
  });
};

/**
 * Delete Agent Purpose Of Use policies
 * @param UUID agentId
 * @param {Array<UUID>} agentPurposeOfUsePolicyIds
 * @param token
 * @returns {Promise<axios.AxiosResponse>}
 */
const deleteAgentPurposeOfUsePolicies = async (agentId, agentPurposeOfUsePolicyIds, token) => {
  const queryParams = agentPurposeOfUsePolicyIds.map(id => `agentPurposeOfUsePolicyIds=${id}`).join('&');
  const url = `${apiRequests.agentPurposeOfUsePolicies(agentId)}?${queryParams}`;

  return axios.delete(url, {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    }

  });
};





export default {
  createAgentPurposeOfUsePolicy,
  deleteAgentPurposeOfUsePolicies,
};
