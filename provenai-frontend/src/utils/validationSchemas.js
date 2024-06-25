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


