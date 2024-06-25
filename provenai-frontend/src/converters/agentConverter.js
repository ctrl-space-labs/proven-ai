/**
 * Convert agentData to agentDTO
 * 
 * @param {Object} agentData - The agent data to convert.
 * @returns {Object} The converted agent DTO.
 */
const toAgentDTO = (agentData, organizationId, agentId) => {
  return {
    id: agentId,
    organizationId: organizationId,
    agentName: agentData.agentName,
    agentUsername: agentData.agentName,
    usagePolicies:[]
  };
};

/**
 * Converts activeDataPod to agent policies.
 *
 * @param {Array} activeAgentPolicies - The active data pod to convert.
 * @returns {Object} The converted agent policies including agent purposes, allow list, and deny list.
 */
const toAgentPolicies = (activeAgentPolicies) => {
  const agentPurpose = new Map();
  let compensation = null;
  let compensationType = "free";

  activeAgentPolicies.forEach((item) => {
    if (item.policyType.name === "USAGE_POLICY" && item.policyOption) {
      if (!agentPurpose.has(item.policyOption.id)) {
        agentPurpose.set(item.policyOption.id, {
          policyTypeId: item.policyType.id,
          policyOptionId: item.policyOption.id,
          name: item.policyOption.name,
          description: item.policyOption.description,
        });
      }
    }
    if (item.policyType.name === "COMPENSATION_POLICY" && item.policyOption) {
      if (!compensation) {
        compensation = {
          policyTypeId: item.policyType.id,
          policyOptionId: item.policyOption.id,
          name: item.policyOption.name,
          description: item.policyOption.description,
        };
      }
    }
  });

  // If no compensation is set, set it to the default free type
  if (compensation) {
    compensationType = "paid";
  }

  return {
    agentPurpose: Array.from(agentPurpose.values()),
    compensation: compensation,
    compensationType: compensationType,
  };
};

export default {
  toAgentDTO,
  toAgentPolicies,
};