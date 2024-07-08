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
  const allowList = new Map();
  const denyList = new Map();
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
    } else if (item.policyType.name === "ALLOW_LIST" && item.value) {
      if (!allowList.has(item.value)) {
        allowList.set(item.value, {
          policyTypeId: item.policyType.id || null,
          policyOptionId: item.policyOption.id || null,
          dataPodId: item.value || null,
          name: "",
        });
      }
    } else if (item.policyType.name === "DENY_LIST" && item.value) {
      if (!denyList.has(item.value)) {
        denyList.set(item.value, {
          policyTypeId: item.policyType.id,
          policyOptionId: item.policyOption.id,
          dataPodId: item.value,
          name: "",
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
    allowList: Array.from(allowList.values()),
    denyList: Array.from(denyList.values()),
    compensation: compensation,
    compensationType: compensationType,
  };
};

export default {
  toAgentDTO,
  toAgentPolicies,
};