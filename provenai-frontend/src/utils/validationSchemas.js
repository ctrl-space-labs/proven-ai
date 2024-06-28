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

// import * as yup from "yup";

// export const userSchema = yup.object().shape({
//   organizationName: yup.string().required("Organization name is required"),
//   selectedOrganizationType: yup.string().required("Organization type is required"),
//   firstName: yup.string().when('selectedOrganizationType', {
//     is: 'natural-person',
//     then: yup.string().required("First name is required")
//   }),
//   familyName: yup.string().when('selectedOrganizationType', {
//     is: 'natural-person',
//     then: yup.string().required("Last name is required")
//   }),
//   personalIdentifier: yup.string().when('selectedOrganizationType', {
//     is: 'natural-person',
//     then: yup.string().required("Personal identifier is required")
//   }),
//   dateOfBirth: yup.date().when('selectedOrganizationType', {
//     is: 'natural-person',
//     then: yup.date().required("Date of birth is required")
//   }),
//   gender: yup.string().when('selectedOrganizationType', {
//     is: 'natural-person',
//     then: yup.string().required("Gender is required")
//   }),
//   nationality: yup.string().when('selectedOrganizationType', {
//     is: 'natural-person',
//     then: yup.string().required("Nationality is required")
//   }),
//   legalPersonIdentifier: yup.string().when('selectedOrganizationType', {
//     is: 'legal-entity',
//     then: yup.string().required("Legal person identifier is required")
//   }),
//   legalName: yup.string().when('selectedOrganizationType', {
//     is: 'legal-entity',
//     then: yup.string().required("Legal name is required")
//   }),
//   legalAddress: yup.string().when('selectedOrganizationType', {
//     is: 'legal-entity',
//     then: yup.string().required("Legal address is required")
//   }),
//   country: yup.string().when('selectedOrganizationType', {
//     is: 'legal-entity',
//     then: yup.string().required("Country is required")
//   }),
//   taxReference: yup.string().when('selectedOrganizationType', {
//     is: 'legal-entity',
//     then: yup.string().required("Tax reference is required")
//   }),
//   vatNumber: yup.string().when('selectedOrganizationType', {
//     is: 'legal-entity',
//     then: yup.string().required("VAT number is required")
//   }),
//   profileLink: yup.string().url("Must be a valid URL").required("Profile link is required"),
// });

// export const agentSchema = yup.object().shape({
//   dataPodName: yup.string().required("Data pod name is required"),
//   agentPurpose: yup.array().min(1, "At least one purpose is required").required("Purpose is required"),
//   denyList: yup.array().of(yup.object({
//     agentId: yup.string().required(),
//     name: yup.string().required()
//   })),
//   allowList: yup.array().of(yup.object({
//     agentId: yup.string().required(),
//     name: yup.string().required()
//   })),
// });

// export const dataUseSchema = yup.object().shape({
//   attributionPolicies: yup.array().min(1, "At least one attribution policy is required").required("Attribution policy is required"),
//   compensationPolicies: yup.array().min(1, "At least one compensation policy is required").required("Compensation policy is required"),
// });



