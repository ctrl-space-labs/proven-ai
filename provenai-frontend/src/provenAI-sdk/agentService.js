import axios from "axios";
import apiRequests from "src/configs/apiRequest.js";

/**
 * Get all agents by organizationId
 * @param organizationId
 * @param storedToken
 * @returns {Promise<axios.AxiosResponse<Agent>>}
 */
const getAgentsByOrganization = async (organizationId, storedToken) => {
  return axios.get(apiRequests.getAgentsByOrganization(), {
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
 * Get all agents by organizationId 
 * @param storedToken
 * @returns {Promise<axios.AxiosResponse<AgentDTO>>}
 */
const getAgentWithoutVc = async (storedToken) => {
  return axios.get(apiRequests.getAgentWithoutVc(), {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + storedToken,
    },
    
  });
};

/**
 * Get Agent by id
 * @param agentId
 * @param storedToken
 * @returns {Promise<axios.AxiosResponse<Agent>>}
 */
const getAgentById = async (agentId, storedToken) => {
  return axios.get(apiRequests.getAgentById(agentId), {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + storedToken,
    }    
  });
};


/**
 * Get  policies by agent id
 * @param agentId
 * @param storedToken
 * @returns {Promise<axios.AxiosResponse<AgentPurposeOfUsePolicies>>}
 */
const getPoliciesByAgent = async (agentId, storedToken) => {
  return axios.get(apiRequests.getPoliciesByAgent(agentId), {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + storedToken,
    }
    
  });
};




export default {
  getAgentsByOrganization,
  getAgentWithoutVc,
  getAgentById,
  getPoliciesByAgent
};
