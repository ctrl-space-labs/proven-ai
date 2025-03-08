import React, { useState, useEffect, useMemo } from 'react'
import { useTheme } from '@mui/material/styles'
import { useAuth } from 'src/authentication/useAuth'
import { useDispatch } from 'react-redux'
import { fetchOrganizationById } from 'src/store/organizationsSlice/organizationsSlice.js'
import {
  Autocomplete,
  Grid,
  Typography,
  Box,
  Dialog,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  TextField,
  Button,
  FormHelperText
} from '@mui/material'
import { useForm, Controller } from 'react-hook-form'
import { validateUserSchema } from 'src/utils/validationSchemas'
import Icon from 'src/views/custom-components/mui/icon/icon'
import { useRouter } from 'next/router'
import CredentialsWithQrCodeComponent from 'src/views/pages/data-pods-control/registration-components/CredentialsWithQrCodeComponent'
import ssiService from 'src/provenAI-sdk/ssiService'
import { countries } from 'src/utils/countries'
import { localStorageConstants } from 'src/utils/generalConstants'

const UserInformation = ({
  onSubmit,
  userData,
  setUserData,
  activeOrganization,
  secondFieldOnUrl,
  getVcOfferUrl,
  vcOfferSessionId,
  setUserErrors
}) => {
  const theme = useTheme()
  const router = useRouter()
  const auth = useAuth()
  const dispatch = useDispatch()
  const token = window.localStorage.getItem(localStorageConstants.accessTokenKey)

  const [openCredentials, setOpenCredentials] = useState(false)
  const userOrganizations = auth?.user?.organizations



  const {
    control,
    handleSubmit,
    watch,
    setValue,
    formState: { errors }
  } = useForm({
    defaultValues: userData
  })

  const selectedOrganizationType = watch('selectedOrganizationType', '')



  useEffect(() => {
    Object.entries(userData).forEach(([key, value]) => {
      if (key === 'dateOfBirth' && value) {
        const date = new Date(value)
        setValue(key, date.toISOString().split('T')[0])
      } else {
        setValue(key, value)
      }
    })
  }, [userData, setValue])

  useEffect(() => {
    setUserErrors(errors)
  }, [errors, setUserErrors])

  useEffect(() => {
    const fetchVcOfferFlow = async () => {
      if (vcOfferSessionId) {
        try {
          const { offeredVP, organizationDid, vcCredentialSubject, vcCredentialType } =
            await ssiService.handleVcOfferFlow(vcOfferSessionId)

          let selectedOrganizationType = 'natural-person'
          if (vcCredentialType === 'LegalEntityVerifiableID') {
            selectedOrganizationType = 'legal-entity'
          }

          setUserData(prevData => ({
            ...prevData,
            selectedOrganizationType: selectedOrganizationType,
            organizationVpJwt: offeredVP.data.tokenResponse.vp_token,
            organizationDid: organizationDid,
            firstName: vcCredentialSubject.firstName,
            familyName: vcCredentialSubject.familyName,
            gender: vcCredentialSubject.gender,
            dateOfBirth: vcCredentialSubject.dateOfBirth,
            personalIdentifier: vcCredentialSubject.personalIdentifier,
            legalPersonIdentifier: vcCredentialSubject.legalPersonIdentifier,
            legalName: vcCredentialSubject.legalName,
            legalAddress: vcCredentialSubject.legalAddress,
            taxReference: vcCredentialSubject.taxReference,
            vatNumber: vcCredentialSubject.VATRegistration
          }))
        } catch (error) {
          console.error('Failed to fetch VC offer flow:', error)
        }
      }
    }

    fetchVcOfferFlow()
  }, [vcOfferSessionId, ssiService])

  useEffect(() => {
    if (activeOrganization?.id && userOrganizations.length) {
      const org = userOrganizations.find(org => org.id === activeOrganization.id)
      if (org) {
        setValue('organizationName', org.name)
        setUserData(prev => ({ ...prev, organizationName: org.name }))
      }
    }
  }, [activeOrganization, userOrganizations, setValue, setUserData])

  const validationRules = validateUserSchema(selectedOrganizationType)

  // Handle Edit dialog
  const handleCredentialsOpen = () => setOpenCredentials(true)
  const handleCredentialsClose = () => setOpenCredentials(false)

  const handleFormSubmit = data => {
    setUserData(data)
    onSubmit()
  }

  const handleOrganizationChange = (e, field) => {
    field.onChange(e)
    const selectedOrg = userOrganizations.find(org => org.name === e.target.value)
    if (selectedOrg) {
      setUserData(prev => ({
        ...prev,
        organizationName: selectedOrg.name
      }))
      dispatch(fetchOrganizationById({ organizationId: selectedOrg.id, token }))
      router.push(
        {
          pathname: router.pathname,
          query: { ...router.query, organizationId: selectedOrg.id }
        },
        undefined,
        { shallow: true }
      )
    }
  }

  return (
    <form onSubmit={handleSubmit(handleFormSubmit)}>
      <Grid container spacing={5}>
        <Grid item xs={12} sm={6}>
          <Typography variant='h5' sx={{ fontWeight: 800, color: 'text.primary' }}>
            User Information
          </Typography>
          <Typography variant='caption' component='p'>
            Enter Your Account Details
          </Typography>
        </Grid>
        <Grid item xs={12} sm={6}>
          {(!activeOrganization?.id || !secondFieldOnUrl) && (
            <FormControl fullWidth>
              <InputLabel id='organization-label'>Select Organization</InputLabel>
              <Controller
                name='organizationName'
                control={control}
                rules={validationRules.organizationName}
                render={({ field }) => (
                  <Select
                    {...field}
                    labelId='organization-label'
                    label='Organization'
                    oonChange={e => handleOrganizationChange(e, field)}
                  >
                    {[...userOrganizations]
                      .sort((a, b) => a.name.localeCompare(b.name))
                      .map(org => (
                        <MenuItem key={org.id} value={org.name}>
                          {org.name}
                        </MenuItem>
                      ))}
                  </Select>
                )}
              />
              {errors.organizationName && <FormHelperText error>{errors.organizationName.message}</FormHelperText>}
            </FormControl>
          )}
        </Grid>

        <Grid item xs={12}>
          {' '}
        </Grid>

        <Grid item xs={12} sm={6}>
          <Box sx={{ display: 'flex', alignItems: 'center' }}>
            <FormControl fullWidth>
              <InputLabel id='org-type-label'>Organization Type</InputLabel>
              <Controller
                name='selectedOrganizationType'
                control={control}
                rules={validationRules.selectedOrganizationType}
                render={({ field }) => (
                  <Select {...field} labelId='org-type-label' label='Organization Type'>
                    <MenuItem value='natural-person'>Natural Person</MenuItem>
                    <MenuItem value='legal-entity'>Legal Entity</MenuItem>
                  </Select>
                )}
              />
              {errors.selectedOrganizationType && (
                <FormHelperText error>{errors.selectedOrganizationType.message}</FormHelperText>
              )}
            </FormControl>
          </Box>
        </Grid>

        <Grid item xs={12} sm={6}></Grid>
        {selectedOrganizationType === 'natural-person' && (
          <>
            <Grid item xs={12} sm={6}>
              <FormControl fullWidth>
                <Controller
                  name='firstName'
                  control={control}
                  rules={validationRules.firstName}
                  render={({ field }) => (
                    <TextField
                      {...field}
                      label='First Name'
                      fullWidth
                      error={!!errors.firstName}
                      helperText={errors.firstName?.message}
                    />
                  )}
                />
              </FormControl>
            </Grid>
            <Grid item xs={12} sm={6}>
              <FormControl fullWidth>
                <Controller
                  name='familyName'
                  control={control}
                  rules={validationRules.familyName}
                  render={({ field }) => (
                    <TextField
                      {...field}
                      label='Last Name'
                      fullWidth
                      error={!!errors.familyName}
                      helperText={errors.familyName?.message}
                    />
                  )}
                />
              </FormControl>
            </Grid>
            <Grid item xs={12} sm={3}>
              <FormControl fullWidth>
                <Controller
                  name='personalIdentifier'
                  control={control}
                  render={({ field }) => (
                    <TextField
                      {...field}
                      label='Personal Identifier'
                      fullWidth
                      error={!!errors.personalIdentifier}
                      helperText={errors.personalIdentifier?.message}
                    />
                  )}
                />
              </FormControl>
            </Grid>
            <Grid item xs={12} sm={3}>
              <FormControl fullWidth>
                <Controller
                  name='dateOfBirth'
                  control={control}
                  render={({ field }) => (
                    <TextField
                      {...field}
                      label='Date of Birth'
                      type='date'
                      fullWidth
                      InputLabelProps={{ shrink: true }}
                      error={!!errors.dateOfBirth}
                      helperText={errors.dateOfBirth?.message}
                    />
                  )}
                />
              </FormControl>
            </Grid>
            <Grid item xs={12} sm={3}>
              <FormControl fullWidth>
                <InputLabel id='gender-label'>Gender</InputLabel>
                <Controller
                  name='gender'
                  control={control}
                  render={({ field }) => (
                    <Select {...field} labelId='gender-label' label='Gender' value={field.value || ''}>
                      <MenuItem value='male'>Male</MenuItem>
                      <MenuItem value='female'>Female</MenuItem>
                      <MenuItem value='other'>Other</MenuItem>
                    </Select>
                  )}
                />
                {errors.gender && <FormHelperText error>{errors.gender.message}</FormHelperText>}
              </FormControl>
            </Grid>
            <Grid item xs={12} sm={3}>
              <FormControl fullWidth>
                <Controller
                  name='nationality'
                  control={control}
                  render={({ field: { value, onChange } }) => {
                    // Create a sorted copy of the countries array.
                    const sortedCountries = useMemo(
                      () => [...countries].sort((a, b) => a.name.localeCompare(b.name)),
                      []
                    )

                    return (
                      <Autocomplete
                        options={sortedCountries}
                        getOptionLabel={option => option.name || ''}
                        value={sortedCountries.find(country => country.isoCode === value) || null}
                        onChange={(_, newValue) => onChange(newValue ? newValue.isoCode : '')}
                        renderInput={params => (
                          <TextField
                            {...params}
                            label='Nationality'
                            error={!!errors.nationality}
                            helperText={errors.nationality?.message}
                          />
                        )}
                      />
                    )
                  }}
                />
              </FormControl>
            </Grid>
          </>
        )}

        {selectedOrganizationType === 'legal-entity' && (
          <>
            <Grid item xs={12} sm={6}>
              <FormControl fullWidth>
                <Controller
                  name='legalPersonIdentifier'
                  control={control}
                  rules={validationRules.legalPersonIdentifier}
                  render={({ field }) => (
                    <TextField
                      {...field}
                      label='Legal Representative Name'
                      fullWidth
                      error={!!errors.legalPersonIdentifier}
                      helperText={errors.legalPersonIdentifier?.message}
                    />
                  )}
                />
              </FormControl>
            </Grid>
            <Grid item xs={12} sm={6}>
              <Controller
                name='legalName'
                control={control}
                rules={validationRules.legalName}
                render={({ field }) => (
                  <TextField
                    {...field}
                    label='Legal Name'
                    fullWidth
                    error={!!errors.legalName}
                    helperText={errors.legalName?.message}
                  />
                )}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <Controller
                name='legalAddress'
                control={control}
                render={({ field }) => (
                  <TextField
                    {...field}
                    label='Legal Address'
                    fullWidth
                    error={!!errors.legalAddress}
                    helperText={errors.legalAddress?.message}
                  />
                )}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <Controller
                name='country'
                control={control}
                render={({ field: { value, onChange } }) => {
                  // Create a sorted copy of the countries array.
                  const sortedCountries = useMemo(() => [...countries].sort((a, b) => a.name.localeCompare(b.name)), [])

                  return (
                    <Autocomplete
                      options={sortedCountries}
                      getOptionLabel={option => option.name || ''}
                      value={sortedCountries.find(country => country.isoCode === value) || null}
                      onChange={(_, newValue) => onChange(newValue ? newValue.isoCode : '')}
                      renderInput={params => (
                        <TextField
                          {...params}
                          label='Country'
                          error={!!errors.country}
                          helperText={errors.country?.message}
                        />
                      )}
                    />
                  )
                }}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <Controller
                name='taxReference'
                control={control}
                render={({ field }) => (
                  <TextField
                    {...field}
                    label='Tax Reference'
                    fullWidth
                    error={!!errors.taxReference}
                    helperText={errors.taxReference?.message}
                  />
                )}
              />
            </Grid>
            <Grid item xs={12} sm={6}>
              <Controller
                name='vatNumber'
                control={control}
                render={({ field }) => (
                  <TextField
                    {...field}
                    label='VAT Number'
                    fullWidth
                    error={!!errors.vatNumber}
                    helperText={errors.vatNumber?.message}
                  />
                )}
              />
            </Grid>
          </>
        )}

        <Grid item xs={12}>
          <Controller
            name='profileLink'
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                label='Profile Link'
                placeholder='https://yourprofilelink.com'
                fullWidth
                error={!!errors.profileLink}
                helperText={errors.profileLink?.message}
              />
            )}
          />
        </Grid>

        <Grid item xs={8}>
          <Box
            sx={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'space-between',
              p: 2,
              border: `2px solid ${theme.palette.primary.dark}`,
              borderRadius: 1,
              mt: 4,
              mb: 4
            }}
          >
            <Icon icon='mdi:account-outline' fontSize={50} />
            <Typography sx={{ flexGrow: 1, mx: 2, textAlign: 'center' }}>
              Authenticate using W3C Verifiable ID
            </Typography>
            <Button variant='outlined' onClick={handleCredentialsOpen}>
              Verifiable ID
            </Button>
          </Box>
        </Grid>

        <Dialog
          open={openCredentials}
          onClose={handleCredentialsClose}
          sx={{ '& .MuiPaper-root': { width: '100%', maxWidth: 650 } }}
        >
          <CredentialsWithQrCodeComponent
            title='Offer your Credential'
            handleCredentialsClose={handleCredentialsClose}
            getURL={getVcOfferUrl}
          />
        </Dialog>

        <Grid item xs={12} sx={{ display: 'flex', justifyContent: 'space-between', mt: 2 }}>
          <Button size='large' variant='outlined' color='secondary'>
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

export default UserInformation
