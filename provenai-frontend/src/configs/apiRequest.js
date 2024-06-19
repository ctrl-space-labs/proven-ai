// const url = "http://localhost:5000/gendox/api/v1/"; // Local Environment
//const url= 'https://gendox-api.ctrlspace.dev/gendox/api/v1/' // Production Environment (AWS)
// const url= 'http://localhost:8080/gendox/api/v1/' // Local Environment
const url = 'https://dev.gendox.ctrlspace.dev/gendox/api/v1/' // Development Environment (Hetzner)
const provenUrl = 'http://localhost:8082/proven-ai/api/v1/' // Local Environment


export default {

  getOrganizationsByCriteria: (organizationIdIn) => {
    const organizationIds = organizationIdIn.join(",");    
    return `${provenUrl}organizations?organizationIdIn=${organizationIds}`;
  },

  provenOrganization: (organizationId) => {
    return `${provenUrl}organizations/${organizationId}`;    
  },

  getDataPodsByOrganization: () =>
    `${provenUrl}data-pods`,

  getDataPodById: (dataPodId) =>
    `${provenUrl}data-pods/${dataPodId}`,

  getAgentsByOrganization: () =>
    `${provenUrl}agents`,

  getPolicyOptions: (policyTypeName) =>
    `${provenUrl}policy-options?policyTypeName=${policyTypeName}`,

  getAgentWithoutVc: () =>
    `${provenUrl}agents/no-vc`,

  getAclPoliciesByDataPod: (dataPodId) =>
    `${provenUrl}data-pods/${dataPodId}/acl-policies`,  

  getPoliciesByAgent: (agentId) =>
    `${provenUrl}agents/${agentId}/policies`,

  aclPolicies:() =>
    `${provenUrl}acl-policies`,

  agentPurposeOfUsePolicies:() =>
    `${provenUrl}agent-purpose-of-use-policies`,


  getProfile: url + "profile",

  getPermissionOfUseAnalytics: () => `${provenUrl}permission-of-use-analytics`,

};
