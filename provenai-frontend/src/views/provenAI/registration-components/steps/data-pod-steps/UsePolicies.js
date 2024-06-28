// ** React Imports
import React from "react";
import { useEffect, useState } from "react";

import {
  Grid,
  Typography,
  FormControl,
  TextField,
  Button,
  FormHelperText,
  Chip,
  Autocomplete,
} from "@mui/material";
import { useForm, Controller } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";

// ** Validation Schema and Default Values
import { dataUseSchema } from "src/utils/validationSchemas";

import policyService from "src/provenAI-sdk/policyService";
import authConfig from "src/configs/auth";

const UsePolicy = ({
  onSubmit,
  handleBack,
  usePoliciesData,
  setUsePoliciesData,
}) => {

  const [attributionDefaultPolicies, setAttributionDefaultPolicies] = useState(
    []
  );
  const [compensationDefaultPolicies, setCompensationDefaultPolicies] =
    useState([]);
  const storedToken = window.localStorage.getItem(
    authConfig.storageTokenKeyName
  );

  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm({
    defaultValues: usePoliciesData,
    resolver: yupResolver(dataUseSchema),
  });

 

  const handleFormSubmit = (data) => {
    setUsePoliciesData(data);
    onSubmit();
  };

  useEffect(() => {
    const fetchAttributionPolicyOptions = async () => {
      try {
        const policies = await policyService.getPolicyOptions(
          "ATTRIBUTION_POLICY",
          storedToken
        );
        const transformedPolicies = policies.data.map(policy => ({
          ...policy,
          policyOptionId: policy.id, 
        }));
        setAttributionDefaultPolicies(transformedPolicies);
      } catch (error) {
        console.error("Error fetching policy options:", error);
      }
    };

    const fetchCompensationPolicyOptions = async () => {
      try {
        const policies = await policyService.getPolicyOptions(
          "COMPENSATION_POLICY",
          storedToken
        );
        const transformedPolicies = policies.data.map(policy => ({
          ...policy,
          policyOptionId: policy.id, 
        }));        
        setCompensationDefaultPolicies(transformedPolicies);
      } catch (error) {
        console.error("Error fetching policy options:", error);
      }
    };

    fetchAttributionPolicyOptions();
    fetchCompensationPolicyOptions();
  }, [storedToken]);

  return (
    <form onSubmit={handleSubmit(handleFormSubmit)}>
      <Grid container spacing={5}>
        <Grid item xs={12}>
          <Typography
            variant="h5"
            sx={{ fontWeight: 800, color: "text.primary" }}
          >
            Data Use Policy
          </Typography>
          <Typography variant="subtitle2" component="p">
            Enter Your Data Use Details
          </Typography>
        </Grid>

        <Grid item xs={12} sm={6}>
          <FormControl fullWidth>
            <Typography
              variant="filled"
              sx={{ fontWeight: 600, color: "text.primary" }}
            >
              Attribution Policy
            </Typography>

            <Controller
              name="attributionPolicies"
              control={control}
              render={({ field: { value, onChange } }) => (
                <Autocomplete
                  multiple
                  sx={{ width: "80%", mt: 2 }}
                  id="attributionPolicies"
                  value={value ? value.map((policy) => policy.name) : []}
                  onChange={(event, newValue) => {
                    // Map the new values back to the full objects
                    const updatedValues = newValue.map((name) =>
                      attributionDefaultPolicies.find(
                        (policy) => policy.name === name
                      )
                    );
                    onChange(updatedValues);
                  }}
                  options={attributionDefaultPolicies.map(
                    (attributionPolicy) => attributionPolicy.name
                  )}
                  renderInput={(params) => (
                    <TextField
                      {...params}
                      variant="filled"
                      placeholder="Attribution Policy"
                      sx={{ mb: 2, mt: 2 }}
                      error={!!errors.attributionPolicies}
                      helperText={
                        errors.attributionPolicies
                          ? errors.attributionPolicies.message
                          : ""
                      }
                    />
                  )}
                  renderTags={(value, getTagProps) =>
                    value.map((attributionPolicy, index) => (
                      <Chip
                        variant="outlined"
                        label={attributionPolicy}
                        color='primary'
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
            <Typography
              variant="filled"
              sx={{ fontWeight: 600, color: "text.primary" }}
            >
              Compensation Policy
            </Typography>

            <Controller
              name="compensationPolicies"
              control={control}
              render={({ field: { value, onChange } }) => (
                <Autocomplete
                  // freeSolo
                  multiple
                  sx={{ width: "80%", mt: 2 }}
                  id="autocomplete-multiple-filled-compensationPolicies"
                  value={value ? value.map((policy) => policy.name) : []} 
                  onChange={(event, newValue) => {
                    // Map the new values back to the full objects
                    const updatedValues = newValue.map((name) =>
                      compensationDefaultPolicies.find(
                        (policy) => policy.name === name
                      )
                    );
                    onChange(updatedValues);
                  }}
                  options={compensationDefaultPolicies.map(
                    (compensationPolicy) => compensationPolicy.name
                  )}
                  renderInput={(params) => (
                    <TextField
                      {...params}
                      variant="filled"
                      placeholder="Compensation Policy"
                      sx={{ mb: 2, mt: 2 }}
                      error={!!errors.compensationPolicies}
                      helperText={
                        errors.compensationPolicies
                          ? errors.compensationPolicies.message
                          : ""
                      }
                    />
                  )}
                  renderTags={(value, getTagProps) =>
                    value.map((compensationPolicy, index) => (
                      <Chip
                        variant="outlined"
                        color='primary'
                        label={compensationPolicy}
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

        <Grid
          item
          xs={12}
          sx={{ display: "flex", justifyContent: "space-between" }}
        >
          <Button
            size="large"
            variant="outlined"
            color="secondary"
            onClick={handleBack}
          >
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

export default UsePolicy;
