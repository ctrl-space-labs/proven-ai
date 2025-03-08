import React, { useEffect, useState } from 'react'
import { useRouter } from 'next/router'
import { useDispatch } from 'react-redux'
import { fetchOrganizationById } from 'src/store/organizationsSlice/organizationsSlice.js'
import { fetchAgentById, fetchAgentPolicies, clearAgentState } from 'src/store/agentSlice/agentSlice'
import { localStorageConstants } from 'src/utils/generalConstants'
import Typography from '@mui/material/Typography'
import Card from '@mui/material/Card'
import Box from '@mui/material/Box'
import AgentStepper from 'src/views/pages/data-pods-control/AgentStepper'
import { ResponsiveCardContent } from '../../../utils/responsiveCardContent'

const AgentControl = () => {
  const router = useRouter()
  const dispatch = useDispatch()
  const token = window.localStorage.getItem(localStorageConstants.accessTokenKey)

  const { organizationId, agentId, vcOfferSessionId } = router.query
  const [activeStep, setActiveStep] = useState(0)

  useEffect(() => {
    setActiveStep(0)
  }, [organizationId])

  useEffect(() => {
    if (organizationId) {
      dispatch(fetchOrganizationById({ organizationId, token }))
    }
  }, [organizationId, dispatch])

  useEffect(() => {
    if (agentId) {
      dispatch(fetchAgentById({ agentId, token }))
      dispatch(fetchAgentPolicies({ agentId, token }))
    } else {
      dispatch(clearAgentState())
    }
  }, [organizationId, agentId, dispatch])

  return (
    <Card sx={{ backgroundColor: 'transparent', boxShadow: 'none' }}>
      <ResponsiveCardContent sx={{ backgroundColor: 'background.paper' }}>
        <Box
          sx={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center'
          }}
        >
          <Typography variant='h3' sx={{ mb: 3, fontWeight: 600, textAlign: 'left' }}>
            Agent Control Policies
          </Typography>
        </Box>
      </ResponsiveCardContent>
      <Box sx={{ height: 20 }} />

      <AgentStepper
        activeStep={activeStep}
        setActiveStep={setActiveStep}
        organizationId={organizationId}
        agentId={agentId}
        vcOfferSessionId={vcOfferSessionId}
      />
    </Card>
  )
}

export default AgentControl
