// ** React Imports
import React from "react";
import { useEffect, useState, useRef } from "react";
import { useRouter } from "next/router";
import {
  Box,
  Grid,
  Typography,
  FormControl,
  TextField,
  Button,
  FormHelperText,
  Tooltip,
  IconButton,
  Chip,
  Autocomplete,
} from "@mui/material";
import { useForm, Controller } from "react-hook-form";
import policyService from "src/provenAI-sdk/policyService";
import {localStorageConstants} from "src/utils/generalConstants";
import Icon from "src/views/custom-components/mui/icon/icon";

const UsePolicy = ({
  onSubmit,
  handleBack,
  usePoliciesData,
  setUsePoliciesData,
  setActiveStep,
  setUsePoliciesErrors
}) => {
  const router = useRouter();
  const isFirstRender = useRef(true);
  const [attributionDefaultPolicies, setAttributionDefaultPolicies] = useState(
    []
  );
  const [compensationDefaultPolicies, setCompensationDefaultPolicies] =
    useState([]);
  const token = window.localStorage.getItem(
    localStorageConstants.accessTokenKey
  );

  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm({
    defaultValues: usePoliciesData
  });

  const handleFormSubmit = (data) => {
    setUsePoliciesData(data);
    onSubmit();
  };

  useEffect(() => {
    if (isFirstRender.current) {
      isFirstRender.current = false;
      return;
    }
      setActiveStep(0);
  }, [router.query.dataPodId]);

  useEffect(() => {
    setUsePoliciesErrors(errors);
  }, [errors, setUsePoliciesErrors]);

  useEffect(() => {
    const fetchAttributionPolicyOptions = async () => {
      try {
        const policies = await policyService.getPolicyOptions(
          "ATTRIBUTION_POLICY",
          token
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
          token
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
  }, [token]);

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
          <Box sx={{ display: "flex", alignItems: "center" }}>

            <Typography
              variant="filled"
              sx={{ fontWeight: 600, color: "text.primary" }}
            >
              Attribution Policy
            </Typography>
            <Tooltip title="Select the appropriate attribution policy for this data pod. This defines how attribution is handled for data usage.">
                <IconButton>
                  <Icon
                    icon="mdi:information-slab-circle-outline"
                    fontSize="inherit"
                  />
                </IconButton>
              </Tooltip>
            </Box>

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
          <Box sx={{ display: "flex", alignItems: "center" }}>

            <Typography
              variant="filled"
              sx={{ fontWeight: 600, color: "text.primary" }}
            >
              Compensation Policy
            </Typography>

            <Tooltip title="Select the compensation policy for this data pod. This defines how compensation is managed for data access and use.">
                <IconButton>
                  <Icon
                    icon="mdi:information-slab-circle-outline"
                    fontSize="inherit"
                  />
                </IconButton>
              </Tooltip>
            </Box>

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
