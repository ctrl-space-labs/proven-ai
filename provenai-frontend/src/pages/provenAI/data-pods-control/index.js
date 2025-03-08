import React, { useEffect, useState } from 'react'
import { useRouter } from 'next/router'
import { useDispatch } from 'react-redux'
import { fetchOrganizationById } from 'src/store/organizationsSlice/organizationsSlice.js'
import { fetchDataPodById, fetchDataPodPolicies, clearDataPodState } from 'src/store/dataPodsSlice/dataPodsSlice.js'
import { localStorageConstants } from 'src/utils/generalConstants'
import Typography from '@mui/material/Typography'
import Card from '@mui/material/Card'
import Box from '@mui/material/Box'
import DataPodStepper from 'src/views/pages/data-pods-control/DataPodStepper'
import { ResponsiveCardContent } from '../../../utils/responsiveCardContent'

const DataPodsControl = () => {
  const router = useRouter()
  const dispatch = useDispatch()
  const token = window.localStorage.getItem(localStorageConstants.accessTokenKey)

  const { organizationId, dataPodId, vcOfferSessionId } = router.query
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
    if (dataPodId) {
      dispatch(fetchDataPodById({ dataPodId, token }))
      dispatch(fetchDataPodPolicies({ dataPodId, token }))
    } else {
      dispatch(clearDataPodState())
    }
  }, [dataPodId, organizationId, dispatch])

  return (
    <Card sx={{ backgroundColor: 'transparent', boxShadow: 'none' }}>
      <ResponsiveCardContent sx={{ backgroundColor: 'background.paper' }}>
        <Box
          sx={{
            justifyContent: 'space-between',
            alignItems: 'center'
          }}
        >
          <Typography variant='h3' sx={{ fontWeight: 600, textAlign: 'left' }}>
            Data Access Control Policies
          </Typography>
        </Box>
      </ResponsiveCardContent>
      <Box sx={{ height: 20 }} />

      <DataPodStepper
        organizationId={organizationId}
        dataPodId={dataPodId}
        activeStep={activeStep}
        setActiveStep={setActiveStep}
        vcOfferSessionId={vcOfferSessionId}
      />
    </Card>
  )
}

export default DataPodsControl
