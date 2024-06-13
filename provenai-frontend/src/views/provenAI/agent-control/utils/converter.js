/**
 * Converts a organization to a user Information.
 *
 * @param {Organization} organization - The organization to convert.
 * @returns {UserInformation} The converted  user information.
 */
const toUserInformation = (organization) => {
  if (organization.naturalPerson) {
    return {
      selectedOrganizationType: "natural-person",
      firstName: organization.firstName,
      familyName: organization.familyName,
      gender: organization.gender,
      dateOfBirth: organization.dateOfBirth,
      nationality: organization.nationality,
    };
  } else if (!organization.naturalPerson) {
    return {
      selectedOrganizationType: "legal-entity",
      legalPersonIdentifier: organization.legalPersonIdentifier,
      legalName: organization.legalName,
      legalAddress: organization.legalAddress,
      country: organization.country,
      taxReference: organization.taxReference,
      vatNumber: organization.vatNumber,
    };
  } else {
    return null;
  }
};

/**
 * Converts activeDataPod to agent policies.
 *
 * @param {Array} activeDataPod - The active data pod to convert.
 * @returns {Object} The converted agent policies including agent purposes, allow list, and deny list.
 */
const toAgentPolicies = (activeAgent) => {
  const agentPurposes = new Map();
  const compensation = new Map();
  

  activeAgent.forEach((item) => {
    if (item.policyType.name === "USAGE_POLICY" && item.policyOption) {
      if (!agentPurposes.has(item.policyOption.id)) {
        agentPurposes.set(item.policyOption.id, {
          id: item.policyOption.id,
          name: item.policyOption.name,
          description: item.policyOption.description,
        });
      }
    } 
    if (item.policyType.name === "COMPENSATION_POLICY" && item.policyOption) {
      if (!compensation.has(item.policyOption.id)) {
        compensation.set(item.policyOption.id, {
          id: item.policyOption.id,
          name: item.policyOption.name,
          description: item.policyOption.description,
        });
      }
    }
  });

  return {
    agentPurposes: Array.from(agentPurposes.values()),
    compensation: Array.from(compensation.values()),
    
  };
};




export default {
  toUserInformation,
  toAgentPolicies,
  
};