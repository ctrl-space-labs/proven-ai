import React from "react";
import { useState, useEffect } from "react";
import { styled, useTheme } from "@mui/material/styles";
import { Grid, Typography, Box, FormControl, InputLabel, Select, MenuItem, TextField, Button, FormHelperText } from "@mui/material";
import { useForm, Controller } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { userSchema, defaultUserInformation } from "../utils/validationSchemas";
// ** Icon Imports
import Icon from "src/@core/components/icon";

const UserInformation = ({ onSubmit, handleBack, userData, setUserData }) => {
  const theme = useTheme();  
  const { control, handleSubmit, watch, setValue, formState: { errors }, reset } = useForm({    
    defaultValues: userData,
    resolver: yupResolver(userSchema),
  });

  const selectedOrganizationType = watch("selectedOrganizationType");
  

  useEffect(() => {
    Object.keys(userData).forEach((key) => {
      setValue(key, userData[key]);
    });
  }, [userData, setValue]);

  useEffect(() => {
    Object.keys(userData).forEach((key) => {
      if (key === 'dateOfBirth' && userData[key]) {
        const date = new Date(userData[key]);
        const formattedDate = date.toISOString().split('T')[0]; // Convert to yyyy-MM-dd format
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



  return (
    <form onSubmit={handleSubmit(handleFormSubmit)}>
      <Grid container spacing={5}>
        <Grid item xs={12} sm={6}>
          <Typography variant="body2" sx={{ fontWeight: 600, color: "text.primary" }}>
            User Information
          </Typography>
          <Typography variant="caption" component="p">
            Enter Your Account Details
          </Typography>
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
                      helperText={errors.firstName ? "This field is required" : ""}
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
                      helperText={errors.familyName ? "This field is required" : ""}
                    />
                  )}
                />
              </FormControl>
            </Grid>


            <Grid item xs={12} sm={4}>
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
                      helperText={errors.dateOfBirth ? "This field is required" : ""}
                      
                    />
                  )}
                />
              </FormControl>
            </Grid>


            <Grid item xs={12} sm={4}>
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
            <Grid item xs={12} sm={4}>
              <FormControl fullWidth>
                <InputLabel id="nationality-label">Nationality</InputLabel>
                <Controller
                  name="nationality"
                  control={control}
                  render={({ field }) => (
                    <Select
                      labelId="nationality-label"
                      {...field}
                      label="Nationality"
                      error={Boolean(errors.nationality)}
                    >
                      <MenuItem value="usa">USA</MenuItem>
                      <MenuItem value="greece">Greece</MenuItem>
                      <MenuItem value="other">Other</MenuItem>
                    </Select>
                  )}
                />
                {errors.nationality && (
                  <FormHelperText sx={{ color: "error.main" }}>
                    This field is required
                  </FormHelperText>
                )}
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
                      helperText={errors.legalPersonIdentifier ? "This field is required" : ""}
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
                      helperText={errors.legalName ? "This field is required" : ""}
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
                      helperText={errors.legalAddress ? "This field is required" : ""}
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
                      helperText={errors.country ? "This field is required" : ""}
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
                      helperText={errors.taxReference ? "This field is required" : ""}
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
                      helperText={errors.vatNumber ? "This field is required" : ""}
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
                  helperText={errors.profileLink ? "This field is required" : ""}
                />
              )}
            />
          </FormControl>
        </Grid>
        

        <Grid item xs={12} sx={{ display: "flex", justifyContent: "space-between" }}>
          <Button size="large" variant="outlined" color="secondary" >
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



