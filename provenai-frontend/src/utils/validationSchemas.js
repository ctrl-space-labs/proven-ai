export const validateUserSchema = selectedOrganizationType => {
  return {
    organizationName: { required: 'Organization name is required' },
    selectedOrganizationType: { required: 'Organization type is required' },
    firstName: selectedOrganizationType === 'natural-person' ? { required: 'First name is required' } : {},
    familyName: selectedOrganizationType === 'natural-person' ? { required: 'Family name is required' } : {},
    legalPersonIdentifier:
      selectedOrganizationType === 'legal-entity' ? { required: 'Legal person identifier is required' } : {},
    legalName: selectedOrganizationType === 'legal-entity' ? { required: 'Legal name is required' } : {}
  }
}

export const validateDataPodSchema = formValues => {
  return {
    dataPodName: { required: 'Data pod name is required' },
    agentPurpose: {
      required: 'Purpose is required',
      validate: value => (value && value.length > 0) || 'At least one purpose is required'
    },
    denyList: {
      validate: value => {
        const allowList = formValues?.allowList || []
        const denyList = value || []
        const duplicateItems = denyList.filter(deny => allowList.some(allow => allow.agentId === deny.agentId))

        return (
          duplicateItems.length === 0 || `Duplicate items in both lists: ${duplicateItems.map(i => i.name).join(', ')}`
        )
      }
    },
    allowList: {
      validate: value => {
        const denyList = formValues?.denyList || []
        const allowList = value || []
        const duplicateItems = allowList.filter(allow => denyList.some(deny => deny.agentId === allow.agentId))

        return (
          duplicateItems.length === 0 || `Duplicate items in both lists: ${duplicateItems.map(i => i.name).join(', ')}`
        )
      }
    }
  }
}

export const validateAgentSchema = (formValues, compensationType) => {
  return {
    agentName: { required: 'Agent name is required' },
    agentPurpose: {
      required: 'Purpose is required',
      validate: value => (value && value.length > 0) || 'At least one purpose is required'
    },
    compensationType: { required: 'Compensation type is required' },
    compensation: compensationType === 'paid' ? { required: 'Compensation is required' } : {},
    denyList: {
      validate: value => {
        const allowList = formValues?.allowList || []
        const denyList = value || []
        const duplicateItems = denyList.filter(deny => allowList.some(allow => allow.dataPodId === deny.dataPodId))

        return (
          duplicateItems.length === 0 || `Duplicate items in both lists: ${duplicateItems.map(i => i.name).join(', ')}`
        )
      }
    },
    allowList: {
      validate: value => {
        const denyList = formValues?.denyList || []
        const allowList = value || []
        const duplicateItems = allowList.filter(allow => denyList.some(deny => deny.dataPodId === allow.dataPodId))

        return (
          duplicateItems.length === 0 || `Duplicate items in both lists: ${duplicateItems.map(i => i.name).join(', ')}`
        )
      }
    }
  }
}
