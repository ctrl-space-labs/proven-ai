import React from "react";
import { useState, useEffect } from "react";
import { styled, useTheme } from "@mui/material/styles";
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
  FormHelperText,
} from "@mui/material";
import { useForm, Controller } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { userSchema } from "src/utils/validationSchemas";
// ** Icon Imports
import Icon from "src/@core/components/icon";
import { useRouter } from "next/router";
import CredentialsWithQrCodeComponent from "src/views/provenAI/registration-components/CredentialsWithQrCodeComponent";
import ssiService from "src/provenAI-sdk/ssiService";
import { countries } from "src/utils/countries";

const UserInformation = ({
  onSubmit,
  handleBack,
  userData,
  setUserData,
  userOrganizations,
  activeOrganization,
  secondFieldOnUrl,
  getVcOfferUrl,
  vcOfferSessionId,
}) => {
  const theme = useTheme();

  const {
    control,
    handleSubmit,
    watch,
    setValue,
    formState: { errors },
    reset,
  } = useForm({
    defaultValues: userData,
    resolver: yupResolver(userSchema),
  });

  const router = useRouter();
  const selectedOrganizationType = watch("selectedOrganizationType");
  const [openCredentials, setOpenCredentials] = useState(false);

  // Handle Edit dialog
  const handleCredentialsOpen = () => setOpenCredentials(true);
  const handleCredentialsClose = () => setOpenCredentials(false);

  useEffect(() => {
    Object.keys(userData).forEach((key) => {
      setValue(key, userData[key]);
    });
  }, [userData, setValue]);

  useEffect(() => {
    const fetchVcOfferFlow = async () => {
      try {
        if (vcOfferSessionId) {
          const {
            offeredVP,
            organizationDid,
            vcCredentialSubject,
            vcCredentialType,
          } = await ssiService.handleVcOfferFlow(vcOfferSessionId);

          let selectedOrganizationType = "natural-person";
          if (vcCredentialType === "LegalEntityVerifiableID") {
            selectedOrganizationType = "legal-entity";
          }

          setUserData((prevData) => ({
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
            vatNumber: vcCredentialSubject.VATRegistration,
          }));
        }
      } catch (error) {
        console.error("Failed to fetch VC offer flow:", error);
      }
    };

    fetchVcOfferFlow();
  }, [vcOfferSessionId, ssiService]);

  useEffect(() => {
    if (activeOrganization?.id) {
      const activeOrgName =
        userOrganizations.find((org) => org.id === activeOrganization.id)
          ?.name || "new-organization";
      setValue("organizationName", activeOrgName);
    }
  }, [activeOrganization, userOrganizations]);

  useEffect(() => {
    Object.keys(userData).forEach((key) => {
      if (key === "dateOfBirth" && userData[key]) {
        const date = new Date(userData[key]);
        const formattedDate = date.toISOString().split("T")[0]; // Convert to yyyy-MM-dd format
        setValue(key, formattedDate);
      } else {
        setValue(key, userData[key]);
      }
    });
  }, [userData, setValue]);

  const handleFormSubmit = (data) => {
    setUserData(data);
    onSubmit();
  };

  const handleMenuItemClick = (org) => {
    setUserData((prevData) => ({
      ...prevData,
      organizationName: org.name,
    }));
    updateShallowQueryParams({ organizationId: org.id });
    console.log(`Clicked on organization: `, org);
  };

  const updateShallowQueryParams = (params) => {
    router.push(
      {
        pathname: router.pathname,
        query: {
          ...router.query,
          ...params,
        },
      },
      undefined,
      { shallow: true }
    );
  };

  return (
    <form onSubmit={handleSubmit(handleFormSubmit)}>
      <Grid container spacing={5}>
        <Grid item xs={12} sm={6}>
          <Typography
            variant="h5"
            sx={{ fontWeight: 800, color: "text.primary" }}
          >
            User Information
          </Typography>
          <Typography variant="caption" component="p">
            Enter Your Account Details
          </Typography>
        </Grid>
        <Grid item xs={12} sm={6}>
          {(!Object.keys(activeOrganization).length || !secondFieldOnUrl) && (
            <FormControl fullWidth>
              <InputLabel id="user-organization-label">
                Select Organization
              </InputLabel>
              <Controller
                name="organizationName"
                control={control}
                render={({ field }) => (
                  <Select
                    labelId="user-organization-label"
                    {...field}
                    label="User Organization"
                  >
                    {/* <MenuItem value="new-organization">
                      New Organization
                    </MenuItem> */}
                    {userOrganizations.map((org) => (
                      <MenuItem
                        key={org.id}
                        value={org.name}
                        onClick={() => handleMenuItemClick(org)}
                      >
                        {org.name}
                      </MenuItem>
                    ))}
                  </Select>
                )}
              />
            </FormControl>
          )}
        </Grid>
        {/* <Grid item xs={12} sm={3}>
          {watch("organizationName") === "new-organization" && (
            <Button
              variant="contained"
              onClick={() =>
                (window.location.href = "https://your-new-site.com")
              }
            >
              Create Organization
            </Button>
          )}
        </Grid> */}
        <Grid item xs={12}>
          {" "}
        </Grid>

        <Grid item xs={12} sm={6}>
          <Box sx={{ display: "flex", alignItems: "center" }}>
            <FormControl fullWidth>
              <InputLabel id="organization-type">Organization Type</InputLabel>
              <Controller
                name="selectedOrganizationType"
                control={control}
                render={({ field }) => (
                  <Select
                    labelId="organization-type"
                    {...field}
                    label="Organization Type"
                  >
                    <MenuItem value="natural-person">Natural Person</MenuItem>
                    <MenuItem value="legal-entity">Legal Entity</MenuItem>
                  </Select>
                )}
              />
            </FormControl>
          </Box>
        </Grid>
        <Grid item xs={12} sm={6}></Grid>
        {selectedOrganizationType === "natural-person" && (
          <>
            <Grid item xs={12} sm={6}>
              <FormControl fullWidth>
                <Controller
                  name="firstName"
                  control={control}
                  render={({ field }) => (
                    <TextField
                      {...field}
                      label="First Name"
                      error={Boolean(errors.firstName)}
                      helperText={
                        errors.firstName ? "This field is required" : ""
                      }
                    />
                  )}
                />
              </FormControl>
            </Grid>
            <Grid item xs={12} sm={6}>
              <FormControl fullWidth>
                <Controller
                  name="familyName"
                  control={control}
                  render={({ field }) => (
                    <TextField
                      {...field}
                      label="Last Name"
                      error={Boolean(errors.familyName)}
                      helperText={
                        errors.familyName ? "This field is required" : ""
                      }
                    />
                  )}
                />
              </FormControl>
            </Grid>

            <Grid item xs={12} sm={3}>
              <FormControl fullWidth>
                <Controller
                  name="personalIdentifier"
                  control={control}
                  render={({ field }) => (
                    <TextField
                      {...field}
                      label="Personal Identifier"
                      error={Boolean(errors.personalIdentifier)}
                      helperText={
                        errors.personalIdentifier
                          ? "This field is required"
                          : ""
                      }
                    />
                  )}
                />
              </FormControl>
            </Grid>

            <Grid item xs={12} sm={3}>
              <FormControl fullWidth>
                <Controller
                  name="dateOfBirth"
                  control={control}
                  render={({ field }) => (
                    <TextField
                      {...field}
                      label="Date of Birth"
                      type="date"
                      InputLabelProps={{ shrink: true }}
                      error={Boolean(errors.dateOfBirth)}
                      helperText={
                        errors.dateOfBirth ? "This field is required" : ""
                      }
                    />
                  )}
                />
              </FormControl>
            </Grid>

            <Grid item xs={12} sm={3}>
              <FormControl fullWidth>
                <InputLabel id="gender-label">Gender</InputLabel>
                <Controller
                  name="gender"
                  control={control}
                  render={({ field }) => (
                    <Select
                      labelId="gender-label"
                      {...field}
                      label="Gender"
                      error={Boolean(errors.gender)}
                      // Normalize the value to lowercase for comparison
                      value={field.value ? field.value.toLowerCase() : ""}
                      onChange={(e) =>
                        field.onChange(e.target.value.toLowerCase())
                      }
                    >
                      <MenuItem value="male">Male</MenuItem>
                      <MenuItem value="female">Female</MenuItem>
                      <MenuItem value="other">Other</MenuItem>
                    </Select>
                  )}
                />
                {errors.gender && (
                  <FormHelperText sx={{ color: "error.main" }}>
                    This field is required
                  </FormHelperText>
                )}
              </FormControl>
            </Grid>
            <Grid item xs={12} sm={3}>
              <FormControl fullWidth>
                <Controller
                  name="nationality"
                  control={control}
                  render={({ field: { value, onChange } }) => (
                    <Autocomplete
                      id="autocomplete-nationality"
                      options={countries}
                      getOptionLabel={(option) => option.name || ""}
                      value={
                        countries.find((country) => country.code === value) || null
                      }
                      onChange={(event, newValue) =>{
                        onChange(newValue ? newValue.code : "");
                      }}
                       
                      renderInput={(params) => (
                        <TextField
                          {...params}
                          label="Nationality"
                          error={Boolean(errors.nationality)}
                          helperText={
                            errors.nationality ? "This field is required" : ""
                          }
                        />
                      )}
                    />
                  )}
                />

                {/* {errors.nationality && (
                  <FormHelperText sx={{ color: "error.main" }}>
                    This field is required
                  </FormHelperText>
                )} */}
              </FormControl>
            </Grid>
          </>
        )}

        {selectedOrganizationType === "legal-entity" && (
          <>
            <Grid item xs={12} sm={6}>
              <FormControl fullWidth>
                <Controller
                  name="legalPersonIdentifier"
                  control={control}
                  render={({ field }) => (
                    <TextField
                      {...field}
                      label="Legal Person Identifier"
                      error={Boolean(errors.legalPersonIdentifier)}
                      helperText={
                        errors.legalPersonIdentifier
                          ? "This field is required"
                          : ""
                      }
                    />
                  )}
                />
              </FormControl>
            </Grid>
            <Grid item xs={12} sm={6}>
              <FormControl fullWidth>
                <Controller
                  name="legalName"
                  control={control}
                  render={({ field }) => (
                    <TextField
                      {...field}
                      label="Legal Name"
                      error={Boolean(errors.legalName)}
                      helperText={
                        errors.legalName ? "This field is required" : ""
                      }
                    />
                  )}
                />
              </FormControl>
            </Grid>
            <Grid item xs={12} sm={6}>
              <FormControl fullWidth>
                <Controller
                  name="legalAddress"
                  control={control}
                  render={({ field }) => (
                    <TextField
                      {...field}
                      label="Legal Address"
                      error={Boolean(errors.legalAddress)}
                      helperText={
                        errors.legalAddress ? "This field is required" : ""
                      }
                    />
                  )}
                />
              </FormControl>
            </Grid>
            <Grid item xs={12} sm={6}>
              <FormControl fullWidth>
                <Controller
                  name="country"
                  control={control}
                  render={({ field }) => (
                    <TextField
                      {...field}
                      label="Country"
                      error={Boolean(errors.country)}
                      helperText={
                        errors.country ? "This field is required" : ""
                      }
                    />
                  )}
                />
              </FormControl>
            </Grid>
            <Grid item xs={12} sm={6}>
              <FormControl fullWidth>
                <Controller
                  name="taxReference"
                  control={control}
                  render={({ field }) => (
                    <TextField
                      {...field}
                      label="Tax Reference"
                      error={Boolean(errors.taxReference)}
                      helperText={
                        errors.taxReference ? "This field is required" : ""
                      }
                    />
                  )}
                />
              </FormControl>
            </Grid>
            <Grid item xs={12} sm={6}>
              <FormControl fullWidth>
                <Controller
                  name="vatNumber"
                  control={control}
                  render={({ field }) => (
                    <TextField
                      {...field}
                      label="VAT Number"
                      error={Boolean(errors.vatNumber)}
                      helperText={
                        errors.vatNumber ? "This field is required" : ""
                      }
                    />
                  )}
                />
              </FormControl>
            </Grid>
          </>
        )}

        <Grid item xs={12}>
          <FormControl fullWidth>
            <Controller
              name="profileLink"
              control={control}
              render={({ field }) => (
                <TextField
                  {...field}
                  label="Profile Link"
                  placeholder="https://yourprofilelink.com"
                  error={Boolean(errors.profileLink)}
                  helperText={
                    errors.profileLink ? "This field is required" : ""
                  }
                />
              )}
            />
          </FormControl>
        </Grid>

        <Grid item xs={8}>
          <Box
            sx={{
              display: "flex",
              alignItems: "center",
              justifyContent: "space-between",
              padding: "16px",
              border: `2px solid ${theme.palette.primary.dark}`,
              borderRadius: "4px",
              mt: 4,
              mb: 4,
            }}
          >
            <Icon icon="mdi:account-outline" fontSize={50} />
            <Typography sx={{ flexGrow: 1, mx: 2, textAlign: "center" }}>
              Authenticate using W3C Verifiable ID
            </Typography>
            <Button variant="outlined" onClick={handleCredentialsOpen}>
              Verifiable ID
            </Button>
          </Box>
        </Grid>
        <Dialog
          open={openCredentials}
          onClose={handleCredentialsClose}
          sx={{ "& .MuiPaper-root": { width: "100%", maxWidth: 650 } }}
        >
          <CredentialsWithQrCodeComponent
            title={"Offer your Credential"}
            handleCredentialsClose={handleCredentialsClose}
            getURL={getVcOfferUrl}
          />
        </Dialog>

        <Grid
          item
          xs={12}
          sx={{ display: "flex", justifyContent: "space-between" }}
        >
          <Button size="large" variant="outlined" color="secondary">
            Previous
          </Button>
          <Button size="large" type="submit" variant="contained">
            Next
          </Button>
        </Grid>
      </Grid>
    </form>
  );
};

export default UserInformation;
