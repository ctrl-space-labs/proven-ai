


const convertToAclPolicies = (data, dataPodId) => {
  return data.map(item => ({
    dataPodId: dataPodId,
    policyTypeId: item.policyTypeId,
    policyOptionId: item.policyOptionId,
    value: item.name 
  }));
};

const convertToAllowListAclPolicies = (data, dataPodId) => {
  return data.map(item => ({
    dataPodId: dataPodId,
    policyTypeId: item.policyTypeId,
    policyOptionId: item.policyOptionId,
    value: item.agentId, 
  }));
};

const convertToDenyListAclPolicies = (data, dataPodId) => {
  return data.map(item => ({
    dataPodId: dataPodId,
    policyTypeId: item.policyTypeId,
    policyOptionId: item.policyOptionId,
    value: item.agentId, 
  }));
};

const getAclPoliciesDiff = (currentPolicies, newPolicies) => {  
  
    const aclPoliciesToCreate = newPolicies.filter(newPolicy => {
      return !currentPolicies.some(currentPolicy =>
        newPolicy.policyTypeId === currentPolicy.policyTypeId &&
        newPolicy.policyOptionId === currentPolicy.policyOptionId &&
        newPolicy.value === currentPolicy.value
      );
    });

    const aclPolicyIdsToDelete = currentPolicies.filter(currentPolicy => {
      return !newPolicies.some(newPolicy =>
        newPolicy.policyTypeId === currentPolicy.policyTypeId &&
        newPolicy.policyOptionId === currentPolicy.policyOptionId &&
        newPolicy.value === currentPolicy.value
      );
    }).map(policy => policy.aclPolicyId);

  return { aclPoliciesToCreate, aclPolicyIdsToDelete };
};

const convertAndComparePolicies = (agentData, usePoliciesData, activeDataPodPolicies, dataPodId) => {
  // Convert agentData and usePoliciesData to aclPoliciesDTO
  const newAclPolicies = [
    ...convertToAclPolicies(agentData.agentPurpose, dataPodId),
    ...convertToAllowListAclPolicies(agentData.allowList, dataPodId),
    ...convertToDenyListAclPolicies(agentData.denyList, dataPodId),
    ...convertToAclPolicies(usePoliciesData.attributionPolicies, dataPodId),
    ...convertToAclPolicies(usePoliciesData.compensationPolicies, dataPodId)
  ];

  const currentAclPolicies = activeDataPodPolicies.map(policy => ({
    policyTypeId: policy.policyType.id,
    policyOptionId: policy.policyOption.id,
    value: policy.value,
    dataPodId: policy.dataPod.id,
    aclPolicyId: policy.id
  }));

  // Determine new and obsolete policies
  const { aclPoliciesToCreate, aclPolicyIdsToDelete } = getAclPoliciesDiff(currentAclPolicies, newAclPolicies);  

  return { aclPoliciesToCreate, aclPolicyIdsToDelete };
};

export default {
  convertAndComparePolicies,
};
