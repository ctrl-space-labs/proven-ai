// const url = "http://localhost:5000/gendox/api/v1/"; // Local Environment
//const url= 'https://gendox-api.ctrlspace.dev/gendox/api/v1/' // Production Environment (AWS)

import { get } from "react-hook-form";

// const url= 'http://localhost:8080/gendox/api/v1/' // Local Environment
const url = 'https://dev.gendox.ctrlspace.dev/gendox/api/v1/' // Development Environment (Hetzner)
const provenUrl = 'http://localhost:8082/proven-ai/api/v1/' // Local Environment

const verifierUrl = 'http://localhost:7003/' // Local Environment


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

  getAgentsByOrganization: () =>
    `${provenUrl}agents`,

  getAgentById: (agentId) =>
    `${provenUrl}agents/${agentId}`,

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



  ssi: {
    getVcFromOffer: (offerId) => `${verifierUrl}openid4vc/session/${offerId}`,
  }


};
