import { Fragment, useState, useEffect, useRef, use } from 'react'
import { useRouter } from 'next/router'
import { useSelector, useDispatch } from 'react-redux'
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
import { useAuth } from 'src/authentication/useAuth'
import UserInformation from 'src/views/pages/data-pods-control/registration-components/steps/UserInformation'
import DataPodInformation from 'src/views/pages/data-pods-control/registration-components/steps/data-pod-steps/DataPodInformation'
import UsePolicy from 'src/views/pages/data-pods-control/registration-components/steps/data-pod-steps/UsePolicies'
import ReviewAndComplete from 'src/views/pages/data-pods-control/registration-components/steps/data-pod-steps/ReviewAndComplete'
import { localStorageConstants } from 'src/utils/generalConstants'
import organizationService from 'src/provenAI-sdk/organizationService'
import dataPodsService from 'src/provenAI-sdk/dataPodsService'
import aclPoliciesService from 'src/provenAI-sdk/aclPoliciesService'
import aclPoliciesConverter from 'src/converters/aclPoliciesConverter'
import organizationConverter from 'src/converters/organizationConverter'
import dataPodConverter from 'src/converters/dataPodConverter'

import {
  dataPodSteps,
  defaultUserInformation,
  defaultDataPodInformation,
  defaultDataUse
} from 'src/utils/defaultValues'

import ssiService from 'src/provenAI-sdk/ssiService'
import CredentialsWithQrCodeComponent from 'src/views/pages/data-pods-control/registration-components/CredentialsWithQrCodeComponent'
import ProgressStepIcon from 'src/views/custom-components/ProgressStepIcon'

const DataPodStepper = ({ organizationId, dataPodId, activeStep, setActiveStep, vcOfferSessionId }) => {
  const router = useRouter()
  const auth = useAuth()
  const dispatch = useDispatch()
  const token = window.localStorage.getItem(localStorageConstants.accessTokenKey)
  const previousDataPodId = useRef(dataPodId)

  const { activeOrganization } = useSelector(state => state.organizations)
  const { activeDataPod, dataPodPolicies } = useSelector(state => state.dataPods)

  const [userErrors, setUserErrors] = useState({})
  const [dataPodErrors, setDataPodErrors] = useState({})

  const [usePoliciesErrors, setUsePoliciesErrors] = useState({})
  const [dataPodUpdated, setDataPodUpdated] = useState(false)
  const [isSubmitComplete, setIsSubmitComplete] = useState(false)

  const [userData, setUserData] = useState(defaultUserInformation)
  const [dataPodData, setDataPodData] = useState(defaultDataPodInformation)
  const [usePoliciesData, setUsePoliciesData] = useState(defaultDataUse)

  useEffect(() => {
    // When the dataPodId changes, reset the state
    if (activeStep > 2 && previousDataPodId.current !== dataPodId) {
      setActiveStep(0) // Reset to the first step
      previousDataPodId.current = dataPodId // Update the ref with the new dataPodId
    }
  }, [dataPodId]) // This effect depends on agentId changes

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
    if (dataPodPolicies && dataPodPolicies.length > 0) {
      const agentPolicies = dataPodConverter.toDataPodPolicies(dataPodPolicies)
      setDataPodData(prevAgentData => ({
        ...agentPolicies
      }))

      const usePolicies = dataPodConverter.toUsePolicies(dataPodPolicies)
      setUsePoliciesData(prevUsePoliciesData => ({
        ...usePolicies
      }))
    } else {
      setDataPodData(prevData => ({
        ...defaultDataPodInformation,
        dataPodName: prevData.dataPodName
      }))
      setUsePoliciesData(defaultDataUse)
    }
  }, [dataPodPolicies])

  const handleBack = () => {
    setActiveStep(prevActiveStep => prevActiveStep - 1)
  }

  const refreshPage = () => {
    const url = `/provenAI/data-pods-control/?organizationId=${activeOrganization.id}&dataPodId=${activeDataPod.id}`
    router.reload(url)
  }

  const getVcOfferUrl = async () => {
    try {
    const offer = await organizationService.getVcOfferUrl(token, organizationId, router.asPath)
    return offer.data.credentialVerificationUrl
    } catch (error) {
      toast.error('Failed to fetch VC offer URL')
    }
  }

  async function submitDatapodForm() {
    try {
      const organizationDTO = organizationConverter.toOrganizationDTO(organizationId, userData)

      if (Object.keys(activeOrganization).length !== 0) {
        await organizationService.updateOrganization(organizationDTO, token)
        toast.success('Organization updated successfully!')
      } else {
        await organizationService.createOrganization(organizationDTO, token)
        toast.success('Organization registration successfully!')
      }

      if (Object.keys(activeDataPod).length === 0) {
        const dataPodDTO = dataPodConverter.toDataPodDTO(dataPodData, organizationId, dataPodId)

        await dataPodsService.createDataPod(dataPodDTO, token)
        toast.success('Data Pod created successfully!')
      }

      // Convert and compare policies
      const { aclPoliciesToCreate, aclPolicyIdsToDelete } = aclPoliciesConverter.convertAndComparePolicies(
        dataPodData,
        usePoliciesData,
        dataPodPolicies,
        dataPodId
      )

      // Create new ACL policies
      for (const aclPolicyDTO of aclPoliciesToCreate) {
        await aclPoliciesService.createAclPolicy(aclPolicyDTO, token)

        setDataPodUpdated(true)

        toast.success('Policy created successfully!')
      }

      // Delete obsolete ACL policies
      if (aclPolicyIdsToDelete.length > 0) {
        await aclPoliciesService.deleteAclPolicies(dataPodId, aclPolicyIdsToDelete, token)
        setDataPodUpdated(true)

        toast.success('Policies deleted successfully!')
      }

      if (dataPodUpdated && toast.success) {
        const dataPodOfferVc = await getDataPodOfferVc()
      }

      setIsSubmitComplete(true)
      toast.success('ACL policies updated successfully!')
    } catch (error) {
      console.error('Error updating ACL policies:', error)
      toast.error('Failed to update ACL policies!')
    }
  }

  const onSubmit = async () => {
    setActiveStep(prevActiveStep => prevActiveStep + 1)
    const isLastStep = activeStep === dataPodSteps.length - 1
    if (!isLastStep) {
      return
    }

    await submitDatapodForm()
  }

  const getDataPodOfferVc = async () => {
    try {
    const dataPodOfferVcResponse = await ssiService.getDataPodIdCredentialOffer(dataPodId, token)
    return dataPodOfferVcResponse.data
    } catch (error) {
      toast.error('Failed to fetch Data Pod Offer VC')
    }
  }



  const getDataPodOfferVcUrl = async () => {
    try {
      const dataPodOfferVcResponse = await ssiService.getDataPodIdCredentialOffer(dataPodId, token);
      return dataPodOfferVcResponse.data.credentialOfferUrl;
    } catch (error) {
      toast.error('Failed to fetch Data Pod Offer VC URL');
      return '';
    }
  };

  const getStep = step => {
    if (step === 0) {
      return (
        <UserInformation
          onSubmit={onSubmit}
          handleBack={handleBack}
          userData={userData}
          setUserData={setUserData}
          secondFieldOnUrl={(activeDataPod && Object.keys(activeDataPod).length) || vcOfferSessionId}
          activeOrganization={activeOrganization}
          getVcOfferUrl={getVcOfferUrl}
          vcOfferSessionId={vcOfferSessionId}
          setUserErrors={setUserErrors}
        />
      )
    } else if (step === 1) {
      return (
        <DataPodInformation
          onSubmit={onSubmit}
          handleBack={handleBack}
          dataPodData={dataPodData}
          setDataPodData={setDataPodData}
          activeDataPod={activeDataPod}
          organizationId={organizationId}
          setActiveStep={setActiveStep}
          setDataPodErrors={setDataPodErrors}
        />
      )
    } else if (step === 2) {
      return (
        <UsePolicy
          onSubmit={onSubmit}
          handleBack={handleBack}
          usePoliciesData={usePoliciesData}
          setUsePoliciesData={setUsePoliciesData}
          setActiveStep={setActiveStep}
          setUsePoliciesErrors={setUsePoliciesErrors}
        />
      )
    } else if (step === 3) {
      return (
        <ReviewAndComplete
          onSubmit={onSubmit}
          handleBack={handleBack}
          userData={userData}
          dataPodData={dataPodData}
          usePoliciesData={usePoliciesData}
          setActiveStep={setActiveStep}
        />
      )
    } else {
      return null
    }
  }

  const getFormContent = () => {
    if (activeStep === dataPodSteps.length && isSubmitComplete) {
      return (
        <Fragment>
          <Box sx={{ textAlign: 'center', mt: 4 }}>
            <Typography variant='h4' gutterBottom>
              All steps are completed!
            </Typography>
            <Typography variant='subtitle1' color='textSecondary' gutterBottom>
              You can now receive your Data Pod Ownership Credential by scanning the QR code below.
            </Typography>
          </Box>
          <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
            <CredentialsWithQrCodeComponent
              title={'Receive your Data Ownership Credential'}
              handleCredentialsClose={null}
              getURL={getDataPodOfferVcUrl}
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
      1: [dataPodErrors.agentPurpose],
      2: [usePoliciesErrors.compensationPolicies, usePoliciesErrors.attributionPolicies]
    }
    return stepErrors
  }

  return (
    <Card sx={{ backgroundColor: 'action.hover' }}>
      <CardContent>
        <Stepper activeStep={activeStep}>
          {dataPodSteps.map((step, index) => {
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

export default DataPodStepper
