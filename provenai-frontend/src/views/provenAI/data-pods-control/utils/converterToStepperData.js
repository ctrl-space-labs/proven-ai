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
 * Converts user information to an organization DTO.
 *
 * @param {UserInformation} userInfo - The user information to convert.
 * @returns {OrganizationDTO} The converted organization DTO.
 */
const toOrganizationDTO = (organizationId, userInfo) => {
  const formatDate = (date) => {
    const d = new Date(date);
    return d.toISOString();
  };

  if (userInfo.selectedOrganizationType === "natural-person") {
    return {
      id: organizationId,
      isNaturalPerson: true,
      firstName: userInfo.firstName,
      familyName: userInfo.familyName,
      gender: userInfo.gender,
      dateOfBirth: formatDate(userInfo.dateOfBirth),
      nationality: userInfo.nationality,
      personalIdentifier: userInfo.personalIdentifier,
    };
  } else if (userInfo.selectedOrganizationType === "legal-entity") {
    return {
      id: organizationId,
      isNaturalPerson: false,
      legalPersonIdentifier: userInfo.legalPersonIdentifier,
      legalName: userInfo.legalName,
      legalAddress: userInfo.legalAddress,
      country: userInfo.country,
      taxReference: userInfo.taxReference,
      vatNumber: userInfo.vatNumber,
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
const toAgentPolicies = (activeDataPod) => {
  console.log(activeDataPod);
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
          policyTypeId: item.policyType.id||null,
          policyOptionId: item.policyOption.id||null ,
          agentId: item.value||null,
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
  toUserInformation,
  toAgentPolicies,
  toUsePolicies,
  toOrganizationDTO,
};
