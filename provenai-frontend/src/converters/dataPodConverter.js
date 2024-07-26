/**
 * Convert DataPodData to  dataPodDTO
 *
 * @param {DataPodData} dataPodData - The data pod data to convert.
 * @returns {DataPodDTO} The converted data pod DTO.
 *
 */

const toDataPodDTO = (dataPodData, organizationId, dataPodId) => {
  return {
    id: dataPodId,
    organizationId: organizationId,
    podUniqueName: dataPodData.dataPodName,
    aclPolicies: []
  };
};



/**
 * Converts activeDataPod to agent policies.
 *
 * @param {Array} activeDataPod - The active data pod to convert.
 * @returns {Object} The converted agent policies including agent purposes, allow list, and deny list.
 */
const toDataPodPolicies = (activeDataPod) => {
  const agentPurpose = new Map();
  const allowList = new Map();
  const denyList = new Map();

  activeDataPod.forEach((item) => {
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
          agentId: item.value || null,
          name: "",
        });
      }
    } else if (item.policyType.name === "DENY_LIST" && item.value) {
      if (!denyList.has(item.value)) {
        denyList.set(item.value, {
          policyTypeId: item.policyType.id,
          policyOptionId: item.policyOption.id,
          agentId: item.value,
          name: "",
        });
      }
    }
  });

  return {
    agentPurpose: Array.from(agentPurpose.values()),
    allowList: Array.from(allowList.values()),
    denyList: Array.from(denyList.values()),
  };
};

/**
 * Converts activeDataPod to use policies.
 *
 * @param {Array} activeDataPod - The active data pod to convert.
 * @returns {Object} The converted use policies.
 */
const toUsePolicies = (activeDataPod) => {
  const attributionPolicies = new Map();
  const compensationPolicies = new Map();

  activeDataPod.forEach((item) => {
    if (item.policyType.name === "ATTRIBUTION_POLICY" && item.policyOption) {
      if (!attributionPolicies.has(item.policyOption.id)) {
        attributionPolicies.set(item.policyOption.id, {
          policyTypeId: item.policyType.id,
          policyOptionId: item.policyOption.id,
          name: item.policyOption.name,
          description: item.policyOption.description,
        });
      }
    }
    if (item.policyType.name === "COMPENSATION_POLICY" && item.policyOption) {
      if (!compensationPolicies.has(item.policyOption.id)) {
        compensationPolicies.set(item.policyOption.id, {
          policyTypeId: item.policyType.id,
          policyOptionId: item.policyOption.id,
          name: item.policyOption.name,
          description: item.policyOption.description,
        });
      }
    }
  });

  return {
    attributionPolicies: Array.from(attributionPolicies.values()),
    compensationPolicies: Array.from(compensationPolicies.values()),
  };
};

export default {  
  toDataPodPolicies,
  toUsePolicies,  
  toDataPodDTO,
};