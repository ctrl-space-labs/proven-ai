/**
 * @typedef {Object} AuditPermissionOfUseCriteria
 * @property {string} searchId - The search ID.
 * @property {string} documentIscc - The document ISCC.
 * @property {string} ownerOrganizationId - The ID of the owner's organization.
 * @property {string} processorOrganizationId - The ID of the processor's organization.
 * @property {string[]} ownerDataPodIdIn - The list of owner's datapod IDs.
 * @property {string[]} processorAgentIdIn - The list of processor agent IDs.
 * @property {string} embeddingModel - The embedding model.
 * @property {number} timeIntervalInSeconds - The time interval in seconds.
 * @property {Date} from - The start time.
 * @property {Date} to - The end time.
 */

/**
 * @typedef {Object} AuditPermissionOfUse
 * @property {string} ownerDatapodId - The ID of the owner's datapod.
 * @property {string} ownerOrganizationId - The ID of the owner's organization.
 * @property {string} processorAgentId - The ID of the processor agent.
 * @property {string} processorOrganizationId - The ID of the processor's organization.
 * @property {Date} bucketStart - The start time of the bucket.
 * @property {number} sumTokens - The sum of tokens.
 */