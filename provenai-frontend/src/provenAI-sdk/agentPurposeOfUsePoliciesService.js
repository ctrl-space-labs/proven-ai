import axios from "axios";
import apiRequests from "src/configs/apiRequest.js";

/**
 *  Create the Agent Purpose Of Use policies 
 *  @param agentPurposeOfUsePolicyDTO
 *  @param storedToken
 *  @returns {Promise<axios.AxiosResponse<AgentPurposeOfUsePolicy>>}
 */
const createAgentPurposeOfUsePolicy = async (agentPurposeOfUsePolicyDTO, storedToken) => {
  return axios.post(apiRequests.agentPurposeOfUsePolicies(), agentPurposeOfUsePolicyDTO, {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + storedToken,
    },
  });
};

/**
 * Delete Agent Purpose Of Use policies
 * @param {Array<UUID>} agentPurposeOfUsePolicyIds
 * @param storedToken
 * @returns {Promise<axios.AxiosResponse>}
 */
const deleteAgentPurposeOfUsePolicies = async (agentPurposeOfUsePolicyIds, storedToken) => {
  const queryParams = agentPurposeOfUsePolicyIds.map(id => `agentPurposeOfUsePolicyIds=${id}`).join('&');
  const url = `${apiRequests.agentPurposeOfUsePolicies()}?${queryParams}`;

  return axios.delete(url, {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + storedToken,
    }
    
  });
};





export default {
  createAgentPurposeOfUsePolicy,
  deleteAgentPurposeOfUsePolicies,
};
