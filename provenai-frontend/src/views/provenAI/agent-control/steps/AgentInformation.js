// ** React Imports
import React from "react";
import { useEffect, useState } from "react";

import {
  Grid,
  Box,
  Typography,
  InputLabel,
  Select,
  FormControl,
  MenuItem,
  TextField,
  Button,
  Chip,
  Autocomplete,
} from "@mui/material";
import CustomRadioIcons from "src/@core/components/custom-radio/icons";
import { useForm, Controller } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";

// ** Validation Schema and Default Values
import { agentSchema } from "../utils/validationSchemas";

import policyService from "src/provenAI-sdk/policyService";
import agentService from "src/provenAI-sdk/agentService";
import authConfig from "src/configs/auth";

const AgentInformation = ({
  onSubmit,
  handleBack,
  agentData,
  setAgentData,
}) => {
  const [usagePolicies, setUsagePolicies] = useState([]);
  const [compensationPolicies, setCompensationPolicies] = useState([]);
  const [selectedCompensation, setSelectedCompensation] = useState(
    agentData.compensation && agentData.compensation.length > 0 ? "Paid" : "Free"
  );
  const storedToken = window.localStorage.getItem(
    authConfig.storageTokenKeyName
  );

  console.log("agentData", agentData);

  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm({
    defaultValues: agentData,
    resolver: yupResolver(agentSchema),
  });

  const Compensation = [
    {
      value: "Paid",
      title: "Paid",
      content: "Anyone can use",
      icon: "mdi:currency-usd",
      iconProps: { fontSize: "2rem", style: { marginBottom: 8 } },
    },
    {
      value: "Free",
      title: "Free",
      content: "Only within team",
      icon: "mdi:currency-usd-off",
      iconProps: { fontSize: "2rem", style: { marginBottom: 8 } },
    },
  ];

  const handleCompensationChange = (prop) => {
    setSelectedCompensation(
      typeof prop === "string" ? prop : prop.target.value
    );
  };

  useEffect(() => {
    const fetchUsagePolicies = async () => {
      try {
        const usagePolicies = await policyService.getPolicyOptions(
          "USAGE_POLICY",
          storedToken
        );
        setUsagePolicies(usagePolicies.data);
      } catch (error) {
        console.error("Error fetching policy options:", error);
      }
    };

    const fetchCompensationPolicies = async () => {
      try {
        const compensationPolicies = await policyService.getPolicyOptions(
          "COMPENSATION_POLICY",
          storedToken
        );
        setCompensationPolicies(compensationPolicies.data);
      } catch (error) {
        console.error("Error fetching policy options:", error);
      }
    };

    fetchUsagePolicies();
    fetchCompensationPolicies();
  }, [storedToken]);

  const handleFormSubmit = (data) => {
    setAgentData(data);
    onSubmit();
  };

  return (
    <form onSubmit={handleSubmit(handleFormSubmit)}>
      <Grid container spacing={5}>
        <Grid item xs={12}>
          <Typography
            variant="h4"
            sx={{ fontWeight: 800, color: "text.primary" }}
          >
            Agent Information
          </Typography>
          <Typography variant="subtitle2" component="p">
            Enter Your Agent Details
          </Typography>
        </Grid>

        <Grid item xs={12}>
          <FormControl fullWidth>
            <Typography
              variant="filled"
              sx={{ fontWeight: 600, color: "text.primary" }}
            >
              What is the purpose of use?
            </Typography>

            <Controller
              name="agentPurpose"
              control={control}
              render={({ field: { value, onChange } }) => (
                <Autocomplete
                  multiple
                  sx={{ width: "40%", mt: 2 }}
                  id="autocomplete-multiple-filled-agentPurpose"
                  value={value ? value.map((purpose) => purpose.name) : []}
                  onChange={(event, newValue) => {
                    // Map the new values back to the full objects
                    const updatedValues = newValue.map((name) =>
                      usagePolicies.find((policy) => policy.name === name)
                    );
                    onChange(updatedValues);
                  }}
                  options={usagePolicies.map((usagePolicy) => usagePolicy.name)}
                  renderInput={(params) => (
                    <TextField
                      {...params}
                      variant="filled"
                      placeholder="Select purpose"
                      sx={{ mb: 2, mt: 2 }}
                      error={!!errors.agentPurpose}
                      helperText={
                        errors.agentPurpose ? errors.agentPurpose.message : ""
                      }
                    />
                  )}
                  renderTags={(value, getTagProps) =>
                    value.map((usagePolicy, index) => (
                      <Chip
                        variant="outlined"
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
            <Typography
              variant="filled"
              sx={{ fontWeight: 600, color: "text.primary" }}
            >
              Compensation
            </Typography>

            <Controller
              name="compensation"
              control={control}
              render={({ field: { value, onChange } }) => (
                <Grid container spacing={4} item xs={12} sm={12} mt={2}>
                  {Compensation.map((item, index) => (
                    <CustomRadioIcons
                    key={index}
                      data={Compensation[index]}
                      selected={selectedCompensation}
                      icon={Compensation[index].icon}
                      name="custom-radios-icons"
                      handleChange={(value) => {
                        handleCompensationChange(value);
                        onChange(value);
                      }}
                      gridProps={{ sm: 4, xs: 12 }}
                      iconProps={Compensation[index].iconProps}
                    />
                  ))}
                </Grid>
              )}
            />
          </FormControl>
        </Grid>

        {selectedCompensation === "Paid" && (
        <Grid item xs={12} sm={6}>
          <FormControl fullWidth>
            <Typography
              variant="filled"
              sx={{ fontWeight: 600, color: "text.primary" }}
            >
              Compensation Type
            </Typography>
            <Controller
              name="compensationType"
              control={control}
              render={({ field }) => (                
                <Select
                  labelId="compensation-type"
                  {...field}
                  value={field.value}
                    onChange={(e) => {
                      field.onChange(e);
                    }}
                  label="Compensation Type"
                >
                  {compensationPolicies.map((policy) => (
                      <MenuItem key={policy.id} value={policy.name}>
                        {policy.name}
                      </MenuItem>
                    ))}
                </Select>
              )}
            />
          </FormControl>
        </Grid>
        )}


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

export default AgentInformation;
