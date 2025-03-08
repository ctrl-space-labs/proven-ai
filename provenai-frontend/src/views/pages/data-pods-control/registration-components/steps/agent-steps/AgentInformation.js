// ** React Imports
import React from 'react'
import { useEffect, useState, useRef } from 'react'
import { useRouter } from 'next/router'
import { useAuth } from 'src/authentication/useAuth'

import {
  Grid,
  Box,
  Tooltip,
  IconButton,
  Typography,
  InputLabel,
  Select,
  FormControl,
  MenuItem,
  TextField,
  Button,
  Chip,
  Autocomplete,
  FormHelperText
} from '@mui/material'
import { ToggleButtonGroup, ToggleButton } from '@mui/material'
import { useForm, Controller } from 'react-hook-form'
import Icon from 'src/views/custom-components/mui/icon/icon'
import { validateAgentSchema } from 'src/utils/validationSchemas'
import policyService from 'src/provenAI-sdk/policyService'
import dataPodsService from 'src/provenAI-sdk/dataPodsService'
import { localStorageConstants } from 'src/utils/generalConstants'

const AgentInformation = ({
  onSubmit,
  handleBack,
  agentData,
  setAgentData,
  activeAgent,
  organizationId,
  setActiveStep,
  setAgentErrors
}) => {
  const router = useRouter()
  const auth = useAuth()
  const isFirstRender = useRef(true)

  const userAgents = auth.user.organizations.find(org => org.id === organizationId)?.projectAgents

  const [usagePolicies, setUsagePolicies] = useState([])
  const [dataPods, setDataPods] = useState([])
  const [compensationPolicies, setCompensationPolicies] = useState([])
  const [selectedCompensation, setSelectedCompensation] = useState(agentData.compensationType)
  const [selectNewAgent, setSelectNewAgent] = useState(false)
  const token = window.localStorage.getItem(localStorageConstants.accessTokenKey)

  const {
    control,
    handleSubmit,
    watch,
    setValue,
    formState: { errors }
  } = useForm()

  const formValues = watch()
  const validationRules = validateAgentSchema(formValues, formValues?.compensationType)

  const compensationTypes = [
    {
      value: 'paid',
      title: 'Paid',
      content: 'Anyone can use',
      icon: 'mdi:currency-usd',
      iconProps: { fontSize: '2rem', style: { marginBottom: 8 } }
    },
    {
      value: 'free',
      title: 'Free',
      content: 'Only within team',
      icon: 'mdi:currency-usd-off',
      iconProps: { fontSize: '2rem', style: { marginBottom: 8 } }
    }
  ]

  useEffect(() => {
    Object.keys(agentData).forEach(key => {
      setValue(key, agentData[key])
    })
  }, [agentData, setValue])

  useEffect(() => {
    if (isFirstRender.current) {
      isFirstRender.current = false
      return
    }
    if (!selectNewAgent) {
      setActiveStep(0)
    }
  }, [router.query.agentId])

  useEffect(() => {
    setAgentErrors(errors)
  }, [errors, setAgentErrors])

  useEffect(() => {
    const fetchUsagePolicies = async () => {
      try {
        const policies = await policyService.getPolicyOptions('USAGE_POLICY', token)
        const transformedPolicies = policies.data.map(policy => ({
          ...policy,
          policyOptionId: policy.id
        }))
        setUsagePolicies(transformedPolicies)
      } catch (error) {
        console.error('Error fetching USAGE_POLICY options:', error)
      }
    }

    const fetchCompensationPolicies = async () => {
      try {
        const policies = await policyService.getPolicyOptions('COMPENSATION_POLICY', token)
        const transformedPolicies = policies.data.map(policy => ({
          ...policy,
          policyOptionId: policy.id
        }))
        setCompensationPolicies(transformedPolicies)
      } catch (error) {
        console.error('Error fetching COMPENSATION_POLICY options:', error)
      }
    }

    const fetchDataPods = async () => {
      try {
        const dataPods = await dataPodsService.getAllDataPods(token)
        setDataPods(dataPods.data.content)
      } catch (error) {
        console.error('Error fetching data pods:', error)
      }
    }

    fetchUsagePolicies()
    fetchCompensationPolicies()
    fetchDataPods()
  }, [token])

  useEffect(() => {
    if (activeAgent?.id) {
      const activeAgentName = userAgents.find(agent => agent.id === activeAgent.id)?.agentName || ''
      const agentUserId = userAgents.find(agent => agent.id === activeAgent.id)?.userId || ''

      setValue('agentName', activeAgentName)
      setValue('agentUserId', agentUserId)
    }
  }, [activeAgent, userAgents, setValue])

  const handleCompensationChange = value => {
    setSelectedCompensation(value)
  }

  const updateDataPod = async data => {
    if (!(dataPods.length > 0)) {
      return data
    }

    let allowPolicies, denyPolicies

    try {
      allowPolicies = await policyService.getPolicyOptions('ALLOW_LIST', token)
    } catch (error) {
      console.error('Error fetching allow policy options:', error)
    }

    try {
      denyPolicies = await policyService.getPolicyOptions('DENY_LIST', token)
    } catch (error) {
      console.error('Error fetching deny policy options:', error)
    }

    const allowAgentPolicy = allowPolicies?.data.find(policy => policy.name === 'ALLOW_DATA_POD_NAME')
    const denyAgentPolicy = denyPolicies?.data.find(policy => policy.name === 'DENY_DATA_POD_NAME')

    const updatedDataPods = {
      ...data,
      allowList: data.allowList.map(allow => {
        const dataPod = dataPods.find(pod => pod.id === allow.dataPodId)
        return dataPod
          ? {
              ...allow,
              name: dataPod.podUniqueName,
              policyOptionId: allowAgentPolicy?.id || '',
              policyTypeId: allowAgentPolicy?.policyTypeId || ''
            }
          : allow
      }),
      denyList: data.denyList.map(deny => {
        const dataPod = dataPods.find(pod => pod.id === deny.dataPodId)
        return dataPod
          ? {
              ...deny,
              name: dataPod.podUniqueName,
              policyOptionId: denyAgentPolicy?.id || '',
              policyTypeId: denyAgentPolicy?.policyTypeId || ''
            }
          : deny
      })
    }
    return updatedDataPods
  }

  const handleFormSubmit = async data => {
    const updatedData = await updateDataPod(data)
    setAgentData(updatedData)
    onSubmit()
  }

  const handleDropdownClick = agent => {
    setSelectNewAgent(true)
    setAgentData(prevData => {
      return {
        ...prevData,
        agentName: agent.agentName,
        agentUserId: agent.userId
      }
    })
    updateShallowQueryParams({ organizationId, agentId: agent.id })
  }

  const updateShallowQueryParams = params => {
    router.push(
      {
        pathname: router.pathname,
        query: {
          ...router.query,
          ...params
        }
      },
      undefined,
      { shallow: true }
    )
  }

  return (
    <form onSubmit={handleSubmit(handleFormSubmit)}>
      <Grid container spacing={5}>
        <Grid item xs={12} sm={6}>
          <Typography variant='h5' sx={{ fontWeight: 800, color: 'text.primary' }}>
            Agent Information
          </Typography>
          <Typography variant='subtitle2' component='p'>
            Enter your references to sort your agents
          </Typography>
        </Grid>

        <Grid item xs={12} sm={6}>
          {(!Object.keys(activeAgent).length > 0 || selectNewAgent) && (
            <FormControl fullWidth>
              <InputLabel id='user-agents-label'>Select Agent</InputLabel>
              <Controller
                name='agentName'
                control={control}
                rules={validationRules.agentName}
                render={({ field }) => (
                  <Select labelId='user-agent-label' {...field} label='Agent'>
                    {[...userAgents] // Create a shallow copy to avoid mutating the original array
                      .sort((a, b) => a.agentName.localeCompare(b.agentName))
                      .map(agent => (
                        <MenuItem key={agent.id} value={agent.agentName} onClick={() => handleDropdownClick(agent)}>
                          {agent.agentName}
                        </MenuItem>
                      ))}
                  </Select>
                )}
              />
              {errors.agentName && <FormHelperText error>{errors.agentName.message}</FormHelperText>}
            </FormControl>
          )}
        </Grid>

        <Grid item xs={12}>
          {' '}
        </Grid>

        <Grid item xs={12}>
          <FormControl fullWidth>
            <Box sx={{ display: 'flex', alignItems: 'center' }}>
              <Typography variant='filled' sx={{ fontWeight: 600, color: 'text.primary' }}>
                For what purpose is this used?{' '}
              </Typography>
              <Tooltip title='Select the purpose for which this agent will be used. This helps categorize the agent based on its intended usage.'>
                <IconButton>
                  <Icon icon='mdi:information-slab-circle-outline' fontSize='inherit' />
                </IconButton>
              </Tooltip>
            </Box>

            <Controller
              name='agentPurpose'
              control={control}
              rules={validationRules.agentPurpose}
              render={({ field: { value, onChange } }) => (
                <Autocomplete
                  multiple
                  sx={{ width: '40%', mt: 2 }}
                  id='autocomplete-multiple-filled-agentPurpose'
                  value={value ? value.map(purpose => purpose.name) : []}
                  onChange={(event, newValue) => {
                    // Map the new values back to the full objects
                    const updatedValues = newValue.map(name => usagePolicies.find(policy => policy.name === name))
                    onChange(updatedValues)
                  }}
                  options={usagePolicies.map(usagePolicy => usagePolicy.name)}
                  renderInput={params => (
                    <TextField
                      {...params}
                      variant='filled'
                      placeholder='Select purpose'
                      sx={{ mb: 2, mt: 2 }}
                      error={!!errors.agentPurpose}
                      helperText={errors.agentPurpose?.message}
                    />
                  )}
                  renderTags={(value, getTagProps) =>
                    value.map((usagePolicy, index) => (
                      <Chip
                        variant='outlined'
                        label={usagePolicy}
                        {...getTagProps({ index })}
                        key={index}
                        sx={{ mr: 1, mb: 1, mt: 1 }}
                      />
                    ))
                  }
                />
              )}
            />
          </FormControl>
        </Grid>

        <Grid item xs={12} sm={6}>
          <FormControl fullWidth>
            <Box sx={{ display: 'flex', alignItems: 'center' }}>
              <Typography variant='filled' sx={{ fontWeight: 600, color: 'text.primary' }}>
                Compensation
              </Typography>
              <Tooltip title='Select the type of compensation for this agent. Choose between paid or free based on how this agent will be used.'>
                <IconButton>
                  <Icon icon='mdi:information-slab-circle-outline' fontSize='inherit' />
                </IconButton>
              </Tooltip>
            </Box>

            <Controller
              name='compensationType'
              control={control}
              rules={validationRules.compensationType}
              render={({ field: { value, onChange } }) => (
                <>
                  <ToggleButtonGroup
                    value={value}
                    exclusive
                    onChange={(event, newValue) => {
                      if (newValue !== null) {
                        onChange(newValue)
                        handleCompensationChange(newValue)
                      }
                    }}
                    sx={{ mt: 2 }}
                  >
                    {compensationTypes.map(type => (
                      <ToggleButton
                        key={type.value}
                        value={type.value}
                        sx={{
                          display: 'flex',
                          flexDirection: 'column',
                          alignItems: 'center',
                          gap: 1,
                          textTransform: 'none',
                          p: 2
                        }}
                      >
                        <Icon icon={type.icon} {...type.iconProps} />
                        <Typography variant='body1'>{type.title}</Typography>
                        <Typography variant='body2' color='text.secondary'>
                          {type.content}
                        </Typography>
                      </ToggleButton>
                    ))}
                  </ToggleButtonGroup>
                  {errors.compensationType && <FormHelperText error>{errors.compensationType.message}</FormHelperText>}
                </>
              )}
            />
          </FormControl>
        </Grid>
        <Grid item xs={12} sm={6}>
          {selectedCompensation === 'paid' && (
            <FormControl fullWidth>
              <Box sx={{ display: 'flex', alignItems: 'center' }}>
                <Typography variant='filled' sx={{ fontWeight: 600, color: 'text.primary' }}>
                  Compensation Type
                </Typography>
                <Tooltip title='Select the type of compensation for this agent.'>
                  <IconButton>
                    <Icon icon='mdi:information-slab-circle-outline' fontSize='inherit' />
                  </IconButton>
                </Tooltip>
              </Box>
              <Controller
                name='compensation'
                control={control}
                rules={validationRules.compensation}
                render={({ field: { value, onChange } }) => (
                  <>
                    <Select
                      labelId='compensation-type'
                      value={value?.name || ''}
                      onChange={event => {
                        const selectedPolicy = compensationPolicies.find(policy => policy.name === event.target.value)
                        onChange(selectedPolicy)
                      }}
                      label='Compensation Type'
                    >
                      {compensationPolicies.map(policy => (
                        <MenuItem key={policy.id} value={policy.name}>
                          {policy.name}
                        </MenuItem>
                      ))}
                    </Select>
                    {errors.compensation && <FormHelperText error>{errors.compensation.message}</FormHelperText>}
                  </>
                )}
              />
            </FormControl>
          )}
        </Grid>

        <Grid item xs={12} sm={6}>
          <FormControl fullWidth>
            <Box sx={{ display: 'flex', alignItems: 'center' }}>
              <Typography variant='body2' sx={{ fontWeight: 600, color: 'text.primary' }}>
                Deny list
              </Typography>
              <Tooltip title='Select the data pods that this agent is not allowed to access. The deny list defines which resources are restricted.'>
                <IconButton>
                  <Icon icon='mdi:information-slab-circle-outline' fontSize='inherit' />
                </IconButton>
              </Tooltip>
            </Box>

            <Controller
              name='denyList'
              control={control}
              rules={validationRules.denyList}
              render={({ field: { value, onChange } }) => (
                <Autocomplete
                  multiple
                  sx={{
                    width: '80%',
                    mt: 2,
                    '& .MuiOutlinedInput-root': {
                      '& fieldset': {
                        borderColor: 'red'
                      },
                      '&:hover fieldset': {
                        borderColor: 'red'
                      },
                      '&.Mui-focused fieldset': {
                        borderColor: 'red'
                      }
                    }
                  }}
                  id='autocomplete-multiple-filled-deny'
                  value={
                    value
                      ? value.map(deny => dataPods.find(pod => pod.id === deny.dataPodId)?.podUniqueName || deny.name)
                      : []
                  }
                  onChange={(event, newValue) => {
                    const updatedValues = newValue.map(name => {
                      const dataPod = dataPods.find(pod => pod.podUniqueName === name)
                      return dataPod
                        ? { dataPodId: dataPod.id, name: dataPod.podUniqueName }
                        : { dataPodId: name, name }
                    })
                    onChange(updatedValues)
                  }}
                  options={[...dataPods]
                    .sort((a, b) => a.podUniqueName.localeCompare(b.podUniqueName))
                    .map(pod => pod.podUniqueName)}
                  renderInput={params => (
                    <TextField
                      {...params}
                      sx={{ mb: 2, mt: 2 }}
                      placeholder='Select data pods'
                      error={!!errors.denyList}
                      helperText={errors.denyList?.message}
                    />
                  )}
                  renderTags={(value, getTagProps) =>
                    value.map((dataPod, index) => (
                      <Chip
                        variant='outlined'
                        label={dataPod}
                        {...getTagProps({ index })}
                        key={index}
                        sx={{
                          mr: 1,
                          mb: 1,
                          mt: 1,
                          backgroundColor: 'red'
                        }}
                      />
                    ))
                  }
                />
              )}
            />
          </FormControl>
        </Grid>

        <Grid item xs={12} sm={6}>
          <FormControl fullWidth>
            <Box sx={{ display: 'flex', alignItems: 'center' }}>
              <Typography variant='body2' sx={{ fontWeight: 600, color: 'text.primary' }}>
                Allow list
              </Typography>
              <Tooltip title='Select the data pods that this agent is allowed to access. The allow list defines the permitted resources.'>
                <IconButton>
                  <Icon icon='mdi:information-slab-circle-outline' fontSize='inherit' />
                </IconButton>
              </Tooltip>
            </Box>

            <Controller
              name='allowList'
              control={control}
              rules={validationRules.allowList}
              render={({ field: { value, onChange } }) => (
                <Autocomplete
                  multiple
                  sx={{
                    width: '80%',
                    mt: 2,
                    '& .MuiOutlinedInput-root': {
                      '& fieldset': {
                        borderColor: 'green'
                      },
                      '&:hover fieldset': {
                        borderColor: 'green'
                      },
                      '&.Mui-focused fieldset': {
                        borderColor: 'green'
                      }
                    }
                  }}
                  id='autocomplete-multiple-filled-allow'
                  value={
                    value
                      ? value.map(
                          allow => dataPods.find(pod => pod.id === allow.dataPodId)?.podUniqueName || allow.name
                        )
                      : []
                  }
                  onChange={(event, newValue) => {
                    const updatedValues = newValue.map(name => {
                      const dataPod = dataPods.find(pod => pod.podUniqueName === name)
                      return dataPod
                        ? { dataPodId: dataPod.id, name: dataPod.podUniqueName }
                        : { dataPodId: name, name }
                    })
                    onChange(updatedValues)
                  }}
                  options={[...dataPods]
                    .sort((a, b) => a.podUniqueName.localeCompare(b.podUniqueName))
                    .map(pod => pod.podUniqueName)}
                  renderInput={params => (
                    <TextField
                      {...params}
                      sx={{ mb: 2, mt: 2 }}
                      placeholder='Select data pods'
                      error={!!errors.allowList}
                      helperText={errors.allowList?.message}
                    />
                  )}
                  renderTags={(value, getTagProps) =>
                    value.map((dataPod, index) => (
                      <Chip
                        variant='outlined'
                        label={dataPod}
                        {...getTagProps({ index })}
                        key={index}
                        sx={{
                          mr: 1,
                          mb: 1,
                          mt: 1,
                          backgroundColor: 'green'
                        }}
                      />
                    ))
                  }
                />
              )}
            />
          </FormControl>
        </Grid>

        <Grid item xs={12} sx={{ display: 'flex', justifyContent: 'space-between' }}>
          <Button size='large' variant='outlined' color='secondary' onClick={handleBack}>
            Previous
          </Button>
          <Button size='large' type='submit' variant='contained'>
            Next
          </Button>
        </Grid>
      </Grid>
    </form>
  )
}

export default AgentInformation
