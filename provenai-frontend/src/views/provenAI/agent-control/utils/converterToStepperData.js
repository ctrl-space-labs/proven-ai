/**
 * Converts a organization to a user Information.
 *
 * @param {Organization} organization - The organization to convert.
 * @returns {UserInformation} The converted  user information.
 */
const toUserInformation = (organization) => {
  if (organization.isNaturalPerson) {
    return {
      selectedOrganizationType: "natural-person",
      firstName: organization.firstName,
      familyName: organization.familyName,
      gender: organization.gender,
      dateOfBirth: organization.dateOfBirth,
      nationality: organization.nationality,
      personalIdentifier: organization.personalIdentifier,
    };
  } else if (!organization.isNaturalPerson) {
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
  toUserInformation,
  toAgentPolicies,
};
