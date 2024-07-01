
import * as yup from "yup";



const listNotInOtherList = (listAName, listBName) => {
  return yup.array().of(
    yup.object().shape({
      agentId: yup.string(),
      name: yup.string(),
    })
  ).test(`${listAName}-not-in-${listBName}`, function (listA) {
    const listB = this.parent[listBName];
    if (!Array.isArray(listA) || !Array.isArray(listB)) {
      return true;
    }
    const listBIds = listB.map(item => item.agentId);
    const duplicateAgents = listA.filter(item => listBIds.includes(item.agentId));
    
    if (duplicateAgents.length > 0) {
      const duplicateNames = duplicateAgents.map(agent => agent.name).join(", ");
      return this.createError({ path: `${listAName}`, message: `Duplicate agents found in both lists: ${duplicateNames}` });
    }

    return true;
  });
};


export const userSchema = yup.object().shape({
  organizationName: yup.string().required("Organization name is required"),
  selectedOrganizationType: yup.string().required("Organization type is required"),  
  firstName: yup.string().when('selectedOrganizationType', {
    is: "natural-person",
    then: userSchema => userSchema.required("First name is required"),
  }),
  familyName: yup.string().when('selectedOrganizationType', {
    is: "natural-person",
    then: userSchema => userSchema.required("Family name is required"),
    
  }),
  personalIdentifier: yup.string().when('selectedOrganizationType', {
    is: "natural-person",
    then: userSchema => userSchema.required("Personal identifier is required"),
    
  }),
  gender: yup.string().when('selectedOrganizationType', {
    is: "natural-person",
    then: userSchema => userSchema.required("Gender is required"),
    
  }),
  dateOfBirth: yup.string().when('selectedOrganizationType', {
    is: "natural-person",
    then: userSchema => userSchema.required("Date of birth is required"),
    
  }),
  nationality: yup.string().when('selectedOrganizationType', {
    is: "natural-person",
    then: userSchema => userSchema.required("Nationality is required"),
    
  }),
  legalPersonIdentifier: yup.string().when('selectedOrganizationType', {
    is: "legal-entity", 
    then: userSchema => userSchema.required("Legal person identifier is required"),
    
  }),
  legalName: yup.string().when('selectedOrganizationType', {
    is: "legal-entity",
    then: userSchema => userSchema.required("Legal name is required"),
    
  }),
  legalAddress: yup.string().when('selectedOrganizationType', {
    is: "legal-entity",
    then: userSchema => userSchema.required("Legal address is required"),
    
  }),
  country: yup.string().when('selectedOrganizationType', {
    is: "legal-entity",
    then: userSchema => userSchema.required("Country is required"),    
  }),

  taxReference: yup.string().when('selectedOrganizationType', {
    is: "legal-entity",
    then: userSchema => userSchema.required("Tax reference is required"),
    
  }),
  vatNumber: yup.string().when('selectedOrganizationType', {
    is: "legal-entity",
    then: userSchema => userSchema.required("VAT number is required"),
    
  }),
});


export const dataPodSchema = yup.object().shape({
  dataPodName: yup.string().required("Data pod name is required"),
  agentPurpose: yup.array().min(1, "At least one purpose is required").required("Purpose is required"),
  denyList: listNotInOtherList('denyList', 'allowList'),
  allowList: listNotInOtherList('allowList', 'denyList'),
});

export const dataUseSchema = yup.object().shape({
  attributionPolicies: yup.array().min(1, "At least one attribution policy is required").required("Attribution policy is required"),
  compensationPolicies: yup.array().min(1, "At least one compensation policy is required").required("Compensation policy is required"),
});

export const agentSchema = yup.object().shape({
  agentName: yup.string().required("Agent name is required"),
  agentPurpose: yup.array().min(1, "At least one purpose is required").required("Purpose is required"),
  compensationType: yup.string().required("Compensation type is required"),
  
  compensation: yup.object().when('compensationType', {
    is: "paid",
    then: agentSchema => agentSchema.required("Compensation is required"),
    otherwise: agentSchema => agentSchema.nullable().notRequired()
  }), 
  
  
});





