import axios from "axios";
import apiRequests from "src/configs/apiRequest.js";

/**
 * Get all agents by organizationId
 * @param organizationId
 * @param storedToken
 * @returns {Promise<axios.AxiosResponse<Agent>>}
 */
const getAgentsByOrganization = async (organizationId, storedToken) => {
  return axios.get(apiRequests.agents(), {
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
 * Get all public agents
 * @param storedToken
 * @returns {Promise<axios.AxiosResponse<AgentDTO>>} 
 */
const getPublicAgents = async (storedToken) => {
  return axios.get(apiRequests.publicAgents(), {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + storedToken,
    },
    params: {
      fetchAll: true,
    },
  });
}
/**
 * Get public agents by agent ids in
 * @param agentIdIn
 * @param storedToken
 * @returns {Promise<axios.AxiosResponse<AgentDTO>>}
 */
const getAgentsByIdIn = async (agentIdIn, storedToken) => {
  return axios.get(apiRequests.publicAgents(), {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + storedToken,
    },
    params: {
      agentIdIn: agentIdIn,
    },
  });
}

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



/**
 * Create a new agent
 * @param agentDTO
 * @param storedToken
 * @returns {Promise<axios.AxiosResponse<Agent>>}
 
 */
const createAgent = async (agentDTO, storedToken) => {
  return axios.post(apiRequests.agents(), agentDTO, {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + storedToken,
    },
  });
}


export default {
  getAgentsByOrganization,
  getPublicAgents,
  getAgentsByIdIn,
  getAgentById,
  getPoliciesByAgent,  
  createAgent
};
