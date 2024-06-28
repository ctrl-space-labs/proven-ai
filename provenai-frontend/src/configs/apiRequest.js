const gendoxUrl = "http://localhost:5000/gendox/api/v1/"; // Local Environment
//const url= 'https://gendox-api.ctrlspace.dev/gendox/api/v1/' // Production Environment (AWS)
// const url= 'http://localhost:8080/gendox/api/v1/' // Local Environment
const url = 'https://dev.gendox.ctrlspace.dev/gendox/api/v1/' // Development Environment (Hetzner)
const provenUrl = 'http://localhost:8082/proven-ai/api/v1/' // Local Environment

const verifierUrl = 'https://proven-ai-dev.ctrlspace.dev/verifier/' // Local Environment


export default {

  getOrganizationsByCriteria: (organizationIdIn) => {
    const organizationIds = organizationIdIn.join(",");    
    return `${provenUrl}organizations?organizationIdIn=${organizationIds}`;
  },

  provenOrganization: () => {
    return `${provenUrl}organizations`;
  },

  organizationRegistration: () => {
    return `${provenUrl}organizations/registration`;
  },

  provenOrganizationById: (organizationId) => {
    return `${provenUrl}organizations/${organizationId}`;    
  },

  provenGetVcOfferUrl: (organizationId, redirectURL) => {
    let url = `${provenUrl}organizations/${organizationId}/verify-vp`;
    if (redirectURL) {
      url += `?redirectPath=${btoa(redirectURL)}`;
    }
    return url;
  },

  dataPods: () => {
   return `${provenUrl}data-pods`;
  },

  getDataPodById: (dataPodId) =>
    `${provenUrl}data-pods/${dataPodId}`,

  agents: () =>
    `${provenUrl}agents`,

  getAgentById: (agentId) =>
    `${provenUrl}agents/${agentId}`,

  getPolicyOptions: (policyTypeName) =>
    `${provenUrl}policy-options?policyTypeName=${policyTypeName}`,  

  getAgentWithoutVc: (page = 0, size = 200) =>
    `${provenUrl}agents/no-vc?page=${page}&size=${size}`,  

  getAclPoliciesByDataPod: (dataPodId) =>
    `${provenUrl}data-pods/${dataPodId}/acl-policies`,  

  getPoliciesByAgent: (agentId) =>
    `${provenUrl}agents/${agentId}/policies`,

  aclPolicies:() =>
    `${provenUrl}acl-policies`,

  agentPurposeOfUsePolicies:() =>
    `${provenUrl}agent-purpose-of-use-policies`,

  userAgents: () => {
    return `${gendoxUrl}project-agents`;
  },


  getProfile: url + "profile",

  getPermissionOfUseAnalytics: () => `${provenUrl}permission-of-use-analytics`,



  ssi: {
    getVcFromOffer: (offerId) => `${verifierUrl}openid4vc/session/${offerId}`,
    getAiAgentIdCredentialOffer: (agentId) => `${provenUrl}agents/${agentId}/credential-offer`,
  }


};
