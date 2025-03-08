import axios from "axios";
import apiRequests from "src/configs/apiRequest.js";

/**
 * Get all agents by organizationId
 * @param organizationId
 * @param token
 * @returns {Promise<axios.AxiosResponse<Agent>>}
 */
const getAgentsByOrganization = async (organizationId, token) => {
  return axios.get(apiRequests.agents(), {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    },
    params: {
      organizationId: organizationId,
    },
  });
};

/**
 * Get all public agents
 * @param token
 * @returns {Promise<axios.AxiosResponse<AgentDTO>>}
 */
const getPublicAgents = async (token) => {
  return axios.get(apiRequests.publicAgents(), {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    },
    params: {
      fetchAll: true,
    },
  });
}
/**
 * Get public agents by agent ids in
 * @param agentIdIn
 * @param token
 * @returns {Promise<axios.AxiosResponse<AgentDTO>>}
 */
const getAgentsByIdIn = async (agentIdIn, token) => {
  return axios.get(apiRequests.publicAgents(), {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    },
    params: {
      agentIdIn: agentIdIn,
    },
  });
}

/**
 * Get Agent by id
 * @param agentId
 * @param token
 * @returns {Promise<axios.AxiosResponse<Agent>>}
 */
const getAgentById = async (agentId, token) => {
  return axios.get(apiRequests.getAgentById(agentId), {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    }
  });
};


/**
 * Get  policies by agent id
 * @param agentId
 * @param token
 * @returns {Promise<axios.AxiosResponse<AgentPurposeOfUsePolicies>>}
 */
const getPoliciesByAgent = async (agentId, token) => {
  return axios.get(apiRequests.getPoliciesByAgent(agentId), {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    }

  });
};



/**
 * Create a new agent
 * @param agentDTO
 * @param token
 * @returns {Promise<axios.AxiosResponse<Agent>>}

 */
const createAgent = async (agentDTO, token) => {
  return axios.post(apiRequests.agents(), agentDTO, {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
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
