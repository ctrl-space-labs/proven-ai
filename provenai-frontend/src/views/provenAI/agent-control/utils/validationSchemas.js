import * as yup from "yup";

export const userSchema = yup.object().shape({
  selectedOrganizationType: yup.string().required(),
  // familyName: yup.string().required(),
  // Add other validation fields
});

export const agentSchema = yup.object().shape({
  agentPurpose: yup.array().min(1, "At least one purpose is required").required("Purpose is required"),
});

export const dataUseSchema = yup.object().shape({
  // attributionPolicies: yup.array().min(1, "At least one purpose is required").required("Purpose is required"),
});

export const defaultUserInformation = {
  firstName: "",
  familyName: "",
  selectedOrganizationType: "natural-person",
  profileLink: "",
};

export const defaultAgentInformation = {
  agentPurpose: [],
};

export const defaultDataUse = {
  attributionPolicy: [],
};

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
    subtitle: "Data Use Policy",
  },
];
