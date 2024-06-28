const convertToAclPolicies = (data, dataPodId) => {
  return data.map(item => ({
    dataPodId: dataPodId,
    policyTypeId: item.policyTypeId,
    policyOptionId: item.policyOptionId,
    value: null, 
  }));
};

const convertToAllowListAclPolicies = (data, dataPodId) => {
  return data.map(item => ({
    dataPodId: dataPodId,
    policyTypeId: "adb70b6d-cfc6-4cd0-b89b-353bd00cf53f",
    policyOptionId: "e00a6abf-0bc1-4128-9dbe-91907ffb3d9f",
    value: item.agentId, 
  }));
};

const convertToDenyListAclPolicies = (data, dataPodId) => {
  return data.map(item => ({
    dataPodId: dataPodId,
    policyTypeId: "5838bac5-193c-47bb-8a6c-c47e0b1a2d75",
    policyOptionId: "86504a0c-9038-4c27-9a1a-520948a8e8dc",
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
