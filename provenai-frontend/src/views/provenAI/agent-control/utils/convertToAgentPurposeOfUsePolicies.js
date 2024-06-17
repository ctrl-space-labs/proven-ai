const convertToAgentPurposeOfUsePolicies = (data, agentId) => {
  return data.map((item) => ({
    agentId: agentId,
    policyTypeId: item.policyTypeId,
    policyOptionId: item.policyOptionId,
    value: item.name,
  }));
};

const convertToCompensationPolicies = (data, agentId) => {
  console.log("data", data);
  return {
    agentId: agentId,
    policyTypeId: data.policyTypeId,
    policyOptionId: data.policyOptionId,
    value: data.name,
  }
}

const getAgentPurposeOfUsePoliciesDiff = (currentPolicies, newPolicies) => {

  const policiesToCreate = newPolicies.filter((newPolicy) => {
    return !currentPolicies.some((currentPolicy) =>
      newPolicy.policyTypeId === currentPolicy.policyTypeId &&
      newPolicy.policyOptionId === currentPolicy.policyOptionId      
    );
  });


  const policyIdsToDelete = currentPolicies.filter((currentPolicy) => {
    return !newPolicies.some((newPolicy) =>
      newPolicy.policyTypeId === currentPolicy.policyTypeId &&
      newPolicy.policyOptionId === currentPolicy.policyOptionId       
    );
  }).map((policy) => policy.id);

  console.log("currentPolicies", currentPolicies);
  console.log("newPolicies", newPolicies);

  console.log("policiesToCreate", policiesToCreate);
  console.log("policyIdsToDelete", policyIdsToDelete);

  return { policiesToCreate, policyIdsToDelete };

}



const convertAndComparePolicies = (agentData, activeAgentPolicies, agentId) => {
  
  const newAgentPurposeOfUsePolicies = [
    ...convertToAgentPurposeOfUsePolicies(agentData.agentPurpose, agentId),
    ...(agentData.compensationType === "paid" ? [convertToCompensationPolicies(agentData.compensation, agentId)] : [])
  ];

  const currentAgentPurposeOfUsePolicies = activeAgentPolicies.map((policy) => ({
    agentId: policy.agentId,
    policyTypeId: policy.policyType.id,
    policyOptionId: policy.policyOption.id,
    id: policy.id,    
  }));

  // Determine new and obsolete policies
  const { policiesToCreate, policyIdsToDelete } = getAgentPurposeOfUsePoliciesDiff(currentAgentPurposeOfUsePolicies, newAgentPurposeOfUsePolicies);

  return { policiesToCreate, policyIdsToDelete };

}

export default {
  convertAndComparePolicies,
};
