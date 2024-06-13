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

  getProvenOrganizationById: (organizationId) => {
    return `${provenUrl}organizations/${organizationId}`;    
  },

  getDataPodsByOrganization: () =>
    `${provenUrl}data-pods`,

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

  getProfile: url + "profile",
  deleteProfileCaches: () => `${url}profile/caches`,

  getAllUsers:() => `${url}users`,

  getProjectById: (organizationId, projectId) =>
    `${url}organizations/${organizationId}/projects/${projectId}`,
  getProjectsByOrganization: (organizationId) =>
    `${url}organizations/${organizationId}/projects`,

  getDocumentsByProject: (organizationId, projectId) =>
    `${url}organizations/${organizationId}/projects/${projectId}/documents`,

  getUsersInOrganizationByOrgId: (organizationId) =>
    `${url}organizations/${organizationId}/users`,

  getAllProjectMembers: (organizationId, projectId) =>
    `${url}organizations/${organizationId}/projects/${projectId}/users`,

  updateProject: (organizationId, projectId) =>
    `${url}organizations/${organizationId}/projects/${projectId}`,

  updateOrganization: (organizationId) =>
    `${url}organizations/${organizationId}`,

  createProject: (organizationId) =>
    `${url}organizations/${organizationId}/projects`,

  createOrganization: () => `${url}organizations`,

  postCompletionModel: (projectId) =>
    `${url}messages/semantic-completion?projectId=${projectId}`,

  // getThreadsByCriteria: (projectIdIn) => {
  //   const projectIds = projectIdIn.join(",");
  //   return `${url}threads?projectIdIn=${projectIds}`;
  // },

  getThreadsByCriteria: (projectIdIn) => {
    const projectIds = projectIdIn.join(",");
    return `${url}threads?projectIdIn=${projectIds}&size=100&sort=createdAt,desc`;
  },

  getThreadMessagesByCriteria: (
    threadId,
    page = 0,
    size = 10,
    sort = "createdAt,desc"
  ) =>
    `${url}threads/${threadId}/messages?page=${page}&size=${size}&sort=${sort}`,

  documentSections: (documentId) => `${url}documents/${documentId}/sections`,

  documentSection: (documentId, sectionId) =>
    `${url}documents/${documentId}/sections/${sectionId}`,

  getDocumentById: (documentId) => `${url}documents/${documentId}`,

  uploadDocument: (organizationId, projectId) =>
    `${url}organizations/${organizationId}/projects/${projectId}/documents/upload`,

  triggerJobs: (organizationId, projectId) =>
    `${url}organizations/${organizationId}/projects/${projectId}/splitting/training`,

  getOrganizationById: (organizationId) =>
    `${url}organizations/${organizationId}`,

  addProjectMember: (organizationId, projectId) =>
    `${url}organizations/${organizationId}/projects/${projectId}/members`,

  addOrganizationMember: (organizationId) =>
    `${url}organizations/${organizationId}/users`,

  getAiModelByCategory: (organizationId, projectId) =>
    `${url}organizations/${organizationId}/projects/${projectId}/ai-models/categories`,

  getAiModels: (organizationId, projectId) =>
    `${url}organizations/${organizationId}/projects/${projectId}/ai-models`,

};
