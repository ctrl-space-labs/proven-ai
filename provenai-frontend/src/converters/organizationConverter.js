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



export default {
  toUserInformation,  
  toOrganizationDTO,  
};