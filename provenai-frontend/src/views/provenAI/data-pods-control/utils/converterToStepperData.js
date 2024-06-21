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
      personalIdentifier: organization.personalIdentifier,
      organizationName: organization.name,
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
      organizationName: organization.name,

    };
  } else {
    return null;
  }
};

/**
 * Converts user information to an organization DTO.
 *
 * @param {UserInformation} userData - The user information to convert.
 * @returns {OrganizationDTO} The converted organization DTO.
 */
const toOrganizationDTO = (organizationId, userData) => {
  const formatDate = (date) => {
    const d = new Date(date);
    if (!isNaN(d)) {
      return d.toISOString();
    } else {
      return null;
    }  
  };

  if (userData.selectedOrganizationType === "natural-person") {
    return {
      id: organizationId,
      name: userData.organizationName,
      naturalPerson: true,
      firstName: userData.firstName,
      familyName: userData.familyName,
      gender: userData.gender,
      dateOfBirth: formatDate(userData.dateOfBirth),
      nationality: userData.nationality,
      personalIdentifier: userData.personalIdentifier,
    };
  } else if (userData.selectedOrganizationType === "legal-entity") {
    return {
      id: organizationId,
      name: userData.organizationName,
      naturalPerson: false,
      legalPersonIdentifier: userData.legalPersonIdentifier,
      legalName: userData.legalName,
      legalAddress: userData.legalAddress,
      country: userData.country,
      taxReference: userData.taxReference,
      vatNumber: userData.vatNumber,
    };
  } else {
    return null;
  }
};

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
const toAgentPolicies = (activeDataPod) => {
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
  toUserInformation,
  toAgentPolicies,
  toUsePolicies,
  toOrganizationDTO,
  toDataPodDTO,
};
