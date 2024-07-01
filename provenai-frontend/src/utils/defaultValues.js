
export const dataPodSteps = [
  {
    title: "User",
    subtitle: "User Information",
  },
  {
    title: "Data Pod",
    subtitle: "Data Pod Information",
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

export const agentSteps = [
  {
    title: "User",
    subtitle: "User Information",
  },
  {
    title: "Agent",
    subtitle: "Agent Information",
  },
  
  {
    title: "Review and Complete",
    subtitle: "Review and Submit",
  },
];

export const defaultUserInformation = {
  firstName: "",
  familyName: "",
  selectedOrganizationType: "",
  organizationDid: "",
  organizationVpJwt: "",
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
  agentName: "",
  agentPurpose: [],
  denyList: [],
  allowList : [],
  compensation: "",
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
