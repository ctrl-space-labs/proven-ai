
const gendoxUrl = process.env.NEXT_PUBLIC_GENDOX_URL;
const provenUrl = process.env.NEXT_PUBLIC_PROVEN_URL;
const verifierUrl = process.env.NEXT_PUBLIC_VERIFIER_URL;


export default {

  gendoxUrl: gendoxUrl,

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

  publicDataPods: (page = 0, size = 200) => {
    return `${provenUrl}data-pods/public?page=${page}&size=${size}`;
  },

  getDataPodById: (dataPodId) =>
    `${provenUrl}data-pods/${dataPodId}`,

  agents: () =>
    `${provenUrl}agents`,

  publicAgents: (page = 0, size = 200) => {
   return `${provenUrl}agents/public?page=${page}&size=${size}`;
  },

  getAgentById: (agentId) =>
    `${provenUrl}agents/${agentId}`,

  getPolicyOptions: (policyTypeName) =>
    `${provenUrl}policy-options?policyTypeName=${policyTypeName}`,  

  getAclPoliciesByDataPod: (dataPodId) =>
    `${provenUrl}data-pods/${dataPodId}/acl-policies`,  

  getPoliciesByAgent: (agentId) =>
    `${provenUrl}agents/${agentId}/policies`,

  aclPolicies: (dataPodId) =>
    `${provenUrl}data-pods/${dataPodId}/acl-policies`,

  agentPurposeOfUsePolicies:(agentId) =>
    `${provenUrl}agents/${agentId}/agent-purpose-of-use-policies`, 


  getProfile: gendoxUrl + "profile",

  getPermissionOfUseAnalytics: () => `${provenUrl}permission-of-use-analytics`,



  ssi: {
    getVcFromOffer: (offerId) => `${verifierUrl}openid4vc/session/${offerId}`,
    getAiAgentIdCredentialOffer: (agentId) => `${provenUrl}agents/${agentId}/credential-offer`,
  }


};
