import { Fragment, useState, useEffect, useRef } from 'react'
import { styled, useTheme } from '@mui/material/styles'
import { useSelector, useDispatch } from 'react-redux'
import { useRouter } from 'next/router'
import Box from '@mui/material/Box'
import Card from '@mui/material/Card'
import CardContent from '@mui/material/CardContent'
import Step from '@mui/material/Step'
import Stepper from '@mui/material/Stepper'
import StepLabel from '@mui/material/StepLabel'
import Divider from '@mui/material/Divider'
import Typography from '@mui/material/Typography'
import Button from '@mui/material/Button'
import toast from 'react-hot-toast'
import UserInformation from 'src/views/pages/data-pods-control/registration-components/steps/UserInformation'
import AgentInformation from 'src/views/pages/data-pods-control/registration-components/steps/agent-steps/AgentInformation'
import ReviewAndComplete from 'src/views/pages/data-pods-control/registration-components/steps/agent-steps/ReviewAndComplete'
import { localStorageConstants } from 'src/utils/generalConstants'
import organizationService from 'src/provenAI-sdk/organizationService'
import agentService from 'src/provenAI-sdk/agentService'
import gendoxService from 'src/provenAI-sdk/gendoxService'
import agentPurposeOfUsePoliciesService from 'src/provenAI-sdk/agentPurposeOfUsePoliciesService'
import organizationConverter from 'src/converters/organizationConverter'
import agentConverter from 'src/converters/agentConverter'
import AgentPurposeOfUsePoliciesConverter from 'src/converters/agentPurposeOfUsePoliciesConverter'

import ssiService from 'src/provenAI-sdk/ssiService'
import CredentialsWithQrCodeComponent from 'src/views/pages/data-pods-control/registration-components/CredentialsWithQrCodeComponent'
import ProgressStepIcon from 'src/views/custom-components/ProgressStepIcon'

import { agentSteps, defaultUserInformation, defaultAgentInformation } from 'src/utils/defaultValues'

const AgentStepper = ({ organizationId, agentId, activeStep, setActiveStep, vcOfferSessionId }) => {
  const router = useRouter()
  const token = window.localStorage.getItem(localStorageConstants.accessTokenKey)
  const previousAgentId = useRef(agentId)

  const { activeOrganization } = useSelector(state => state.organizations)
  const { activeAgent, agentPolicies } = useSelector(state => state.agents)

  // Form data states
  const [userErrors, setUserErrors] = useState({})
  const [agentErrors, setAgentErrors] = useState({})

  const [agentUpdated, setAgentUpdated] = useState(false)
  const [isSubmitComplete, setIsSubmitComplete] = useState(false)

  const [userData, setUserData] = useState(defaultUserInformation)
  const [agentData, setAgentData] = useState(defaultAgentInformation)

  useEffect(() => {
    // When the agentId changes, reset the state
    if (activeStep > 2 && previousAgentId.current !== agentId) {
      setActiveStep(0) // Reset to the first step
      previousAgentId.current = agentId // Update the ref with the new agentId
    }
  }, [agentId]) // This effect depends on agentId changes

  useEffect(() => {
    if (Object.keys(activeOrganization).length !== 0) {
      const userInfo = organizationConverter.toUserInformation(activeOrganization)
      setUserData(prevData => {
        const updatedUserInfo = { ...userInfo }

        // check if any field is empty, if so, keep the previous value
        Object.keys(userInfo).forEach(key => {
          if (userInfo[key] === '' || userInfo[key] === null || userInfo[key] === undefined) {
            updatedUserInfo[key] = prevData[key]
          }
        })

        return updatedUserInfo
      })
    } else {
      // new organization
      setUserData(prevData => ({
        ...defaultUserInformation,
        organizationName: prevData.organizationName
      }))
    }
  }, [activeOrganization])

  useEffect(() => {
    if (agentPolicies && agentPolicies.length > 0) {
      const agentDataPolicies = agentConverter.toAgentPolicies(agentPolicies)
      setAgentData(prevAgentData => ({
        ...agentDataPolicies
      }))
    } else {
      setAgentData(prevAgentData => ({
        ...defaultAgentInformation,
        agentName: prevAgentData.agentName,
        agentUserId: prevAgentData.agentUserId
      }))
    }
  }, [agentPolicies])

  const handleBack = () => {
    setActiveStep(prevActiveStep => prevActiveStep - 1)
  }

  const refreshPage = () => {
    const url = `/provenAI/agent-control/?organizationId=${organizationId}&agentId=${agentId}`
    router.reload(url)
  }

  const getVcOfferUrl = async () => {
    try {
      const offer = await organizationService.getVcOfferUrl(token, organizationId, router.asPath)
      return offer.data.credentialVerificationUrl
    } catch (error) {
      toast.error('Failed to get VC offer URL. Please try again.')
    }
  }

  async function submitAgentForm() {
    try {
      const organizationDTO = organizationConverter.toOrganizationDTO(organizationId, userData)
      if (Object.keys(activeOrganization).length !== 0) {
        await organizationService.updateOrganization(organizationDTO, token)
        toast.success('Organization updated successfully!')
      } else {
        await organizationService.createOrganization(organizationDTO, token)
        toast.success('Organization registration successfully!')
      }

      try {
        if (!Object.keys(activeAgent).length) {
          const agentUser = await gendoxService.getGendoxUser(agentData.agentUserId, token)

          const agentDTO = agentConverter.toAgentDTO(agentData, organizationId, agentId, agentUser.data.userName)

          await agentService.createAgent(agentDTO, token)

          setAgentUpdated(true)
          toast.success('Agent created successfully!')
        }
      } catch (error) {
        console.error('Error creating agent:', error)
        toast.error('Failed to create agent. Please try again.')
      }

      const { policiesToCreate, policyIdsToDelete } = AgentPurposeOfUsePoliciesConverter.convertAndComparePolicies(
        agentData,
        agentPolicies,
        agentId
      )

      // Create new policies
      for (const policy of policiesToCreate) {
        await agentPurposeOfUsePoliciesService.createAgentPurposeOfUsePolicy(policy, token)

        setAgentUpdated(true)

        toast.success('Policy created successfully!')
      }

      // Delete obsolete policies
      if (policyIdsToDelete.length > 0) {
        await agentPurposeOfUsePoliciesService.deleteAgentPurposeOfUsePolicies(agentId, policyIdsToDelete, token)
        setAgentUpdated(true)
        toast.success('Policy deleted successfully!')
      }

      if (agentUpdated && toast.success) {
        const agentOfferVc = await getAgentOfferVc()
      }

      setIsSubmitComplete(true)
      toast.success('Agent purpose of use policies updated successfully!')
    } catch (error) {
      console.error('Error updating Agent purpose of use policies:', error)
      toast.error('Failed to update Agent purpose of use policies!')
    }
  }

  const onSubmit = async () => {
    setActiveStep(prevActiveStep => prevActiveStep + 1)
    const isLastStep = activeStep === agentSteps.length - 1
    if (!isLastStep) {
      return
    }

    console.log('Submitting form')

    await submitAgentForm()
  }

  const getAgentOfferVc = async () => {
    try {
      const agentOfferVcResponse = await ssiService.getAiAgentIdCredentialOffer(agentId, token)
      return agentOfferVcResponse.data
    } catch (error) {
      toast.error('Failed to get Agent ID Credential offer. Please try again.')
    }
  }

  const getAgentOfferVcUrl = async () => {
    try {
      const agentOfferVcResponse = await ssiService.getAiAgentIdCredentialOffer(agentId, token)
      return agentOfferVcResponse.data.credentialOfferUrl
    } catch (error) {
      toast.error('Failed to get Agent ID Credential offer URL. Please try again.')
    }
  }

  const getStep = step => {
    if (step === 0) {
      return (
        <UserInformation
          onSubmit={onSubmit}
          handleBack={handleBack}
          userData={userData}
          setUserData={setUserData}
          activeOrganization={activeOrganization}
          secondFieldOnUrl={(activeAgent && Object.keys(activeAgent).length) || vcOfferSessionId}
          getVcOfferUrl={getVcOfferUrl}
          vcOfferSessionId={vcOfferSessionId}
          setUserErrors={setUserErrors}
        />
      )
    } else if (step === 1) {
      return (
        <AgentInformation
          onSubmit={onSubmit}
          handleBack={handleBack}
          agentData={agentData}
          setAgentData={setAgentData}
          activeAgent={activeAgent}
          organizationId={organizationId}
          setActiveStep={setActiveStep}
          setAgentErrors={setAgentErrors}
        />
      )
    } else if (step === 2) {
      return (
        <ReviewAndComplete
          onSubmit={onSubmit}
          handleBack={handleBack}
          userData={userData}
          agentData={agentData}
          setActiveStep={setActiveStep}
        />
      )
    } else {
      return null
    }
  }

  const getFormContent = () => {
    if (activeStep === agentSteps.length && isSubmitComplete && router.query.agentId === agentId) {
      return (
        <Fragment>
          <Box sx={{ textAlign: 'center', mt: 4 }}>
            <Typography variant='h4' gutterBottom>
              All steps are completed!
            </Typography>
            <Typography variant='subtitle1' color='textSecondary' gutterBottom>
              You can now receive your Agent ID Credential by scanning the QR code below.
            </Typography>
          </Box>
          <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
            <CredentialsWithQrCodeComponent
              title={'Receive your Agent ID Credential'}
              handleCredentialsClose={null}
              getURL={getAgentOfferVcUrl}
            />
          </Box>
          <Box sx={{ mt: 4, display: 'flex', justifyContent: 'center' }}>
            <Button size='large' variant='contained' color='primary' onClick={refreshPage}>
              Back
            </Button>
          </Box>
        </Fragment>
      )
    } else {
      return getStep(activeStep)
    }
  }

  function validateForms() {
    const stepErrors = {
      0: [
        userErrors.firstName,
        userErrors.familyName,
        userErrors.dateOfBirth,
        userErrors.gender,
        userErrors.nationality,
        userErrors.profileLink,
        userErrors.legalPersonIdentifier,
        userErrors.legalName,
        userErrors.legalAddress,
        userErrors.country,
        userErrors.taxReference,
        userErrors.vatNumber
      ],
      1: [agentErrors.agentPurpose, agentErrors.compensationType]
    }

    return stepErrors
  }

  return (
    <Card sx={{ backgroundColor: 'action.hover' }}>
      <CardContent>
        <Stepper activeStep={activeStep}>
          {agentSteps.map((step, index) => {
            const labelProps = { error: false }

            if (index === activeStep) {
              const stepErrors = validateForms()

              if (stepErrors[activeStep]?.some(error => error)) {
                labelProps.error = true
              }
            }

            return (
              <Step key={index}>
                <StepLabel {...labelProps} StepIconComponent={ProgressStepIcon}>
                  <div className='step-label'>
                    <Typography className='step-title'>{step.title}</Typography>
                  </div>
                </StepLabel>
              </Step>
            )
          })}
        </Stepper>
      </CardContent>
      <Divider sx={{ m: '0' }} />
      <CardContent>{getFormContent()}</CardContent>
    </Card>
  )
}

export default AgentStepper
