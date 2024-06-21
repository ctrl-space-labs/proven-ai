
export const steps = [
  {
    title: "User",
    subtitle: "User Information",
  },
  {
    title: "Agent",
    subtitle: "Agent Information",
  },
  {
    title: "Data Use",
    subtitle: "Data Use Policy",
  },
  {
    title: "Review and Complete",
    subtitle: "Review and Submit",
  },
];

export const defaultUserInformation = {
  firstName: "",
  familyName: "",
  selectedOrganizationType: "natural-person",
  organizationName: "",
  personalIdentifier: "",
  gender: "",
  dateOfBirth: "",
  nationality: "",
  profileLink: "",
  legalPersonIdentifier: "",
  legalName: "",
  legalAddress: "",
  country: "",
  taxReference: "",
  vatNumber: "",  
};

export const defaultAgentInformation = {
  agentPurpose: [],
  denyList: [],
  allowList : [],
};

export const defaultDataPodInformation = {
  dataPodName: "",  
  agentPurpose: [],
  denyList: [],
  allowList : [],
};

export const defaultDataUse = {
  attributionPolicies: [],
  compensationPolicies: [],
};
