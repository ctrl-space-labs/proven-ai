// ** React Imports
import React from "react";
import { useEffect, useState, useRef } from "react";
import { useRouter } from "next/router";

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
  FormHelperText,
} from "@mui/material";
import CustomRadioIcons from "src/@core/components/custom-radio/icons";
import { useForm, Controller } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";

// ** Validation Schema and Default Values
import { agentSchema } from "src/utils/validationSchemas";

import policyService from "src/provenAI-sdk/policyService";
import authConfig from "src/configs/auth";
import { set } from "nprogress";

const AgentInformation = ({
  onSubmit,
  handleBack,
  agentData,
  setAgentData,
  activeAgent,
  userAgents,
  organizationId,
  setActiveStep,
  setAgentErrors,
}) => {
  const router = useRouter();
  const isFirstRender = useRef(true);

  const {
    control,
    handleSubmit,
    watch,
    setValue,
    formState: { errors },
  } = useForm({
    defaultValues: agentData,
    resolver: yupResolver(agentSchema),
  });

  const [usagePolicies, setUsagePolicies] = useState([]);
  const [compensationPolicies, setCompensationPolicies] = useState([]);
  const [selectedCompensation, setSelectedCompensation] = useState(
    agentData.compensationType
  );
  const [selectNewAgent, setSelectNewAgent] = useState(false);
  const storedToken = window.localStorage.getItem(
    authConfig.storageTokenKeyName
  );

  const compensationTypes = [
    {
      value: "paid",
      title: "Paid",
      content: "Anyone can use",
      icon: "mdi:currency-usd",
      iconProps: { fontSize: "2rem", style: { marginBottom: 8 } },
    },
    {
      value: "free",
      title: "Free",
      content: "Only within team",
      icon: "mdi:currency-usd-off",
      iconProps: { fontSize: "2rem", style: { marginBottom: 8 } },
    },
  ];

  useEffect(() => {
    Object.keys(agentData).forEach((key) => {
      setValue(key, agentData[key]);
    });
  }, [agentData, setValue]);

  useEffect(() => {
    if (isFirstRender.current) {
      isFirstRender.current = false;
      return;
    }
    if (!selectNewAgent) {
      setActiveStep(0);
    }
  }, [router.query.agentId]);

  useEffect(() => {
    setAgentErrors(errors);
  }, [errors, setAgentErrors]);

  useEffect(() => {
    const fetchUsagePolicies = async () => {
      try {
        const policies = await policyService.getPolicyOptions(
          "USAGE_POLICY",
          storedToken
        );
        const transformedPolicies = policies.data.map((policy) => ({
          ...policy,
          policyOptionId: policy.id,
        }));
        setUsagePolicies(transformedPolicies);
      } catch (error) {
        console.error("Error fetching USAGE_POLICY options:", error);
      }
    };

    const fetchCompensationPolicies = async () => {
      try {
        const policies = await policyService.getPolicyOptions(
          "COMPENSATION_POLICY",
          storedToken
        );
        const transformedPolicies = policies.data.map((policy) => ({
          ...policy,
          policyOptionId: policy.id,
        }));
        setCompensationPolicies(transformedPolicies);
      } catch (error) {
        console.error("Error fetching COMPENSATION_POLICY options:", error);
      }
    };

    fetchUsagePolicies();
    fetchCompensationPolicies();
  }, [storedToken]);

  useEffect(() => {
    if (activeAgent?.id) {
      const activeAgentName =
        userAgents.find((agent) => agent.id === activeAgent.id)?.agentName ||
        "";
      setValue("agentName", activeAgentName);
      setValue("agentUserId", activeAgent.userId);
    }
  }, [activeAgent, userAgents, setValue]);

  const handleCompensationChange = (value) => {
    setSelectedCompensation(value);
  };

  const handleFormSubmit = (data) => {
    setAgentData(data);
    onSubmit();
  };

  const handleMenuItemClick = (agent) => {
    setSelectNewAgent(true);
    setAgentData((prevData) => ({
      ...prevData,
      agentName: agent.agentName,
      agentUserId: agent.userId,
    }));
    set;
    updateShallowQueryParams({ organizationId, agentId: agent.id });
    console.log(`Clicked on Agent: `, agent);
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
            Agent Information
          </Typography>
          <Typography variant="subtitle2" component="p">
            Enter your references to sort your agents
          </Typography>
        </Grid>

        <Grid item xs={12} sm={6}>
          {(!Object.keys(activeAgent).length > 0 || selectNewAgent) && (
            <FormControl fullWidth>
              <InputLabel id="user-agents-label">Select Agent</InputLabel>
              <Controller
                name="agentName"
                control={control}
                render={({ field }) => (
                  <Select labelId="user-agent-label" {...field} label="Agent">
                    {userAgents.map((agent) => (
                      <MenuItem
                        key={agent.id}
                        value={agent.agentName}
                        onClick={() => handleMenuItemClick(agent)}
                      >
                        {agent.agentName}
                      </MenuItem>
                    ))}
                  </Select>
                )}
              />
              {errors.agentName && (
                <FormHelperText error>
                  {errors.agentName.message}
                </FormHelperText>
              )}
            </FormControl>
          )}
        </Grid>

        <Grid item xs={12}>
          {" "}
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
              name="compensationType"
              control={control}
              render={({ field: { value, onChange } }) => (
                <>
                  <Grid container spacing={4} item xs={12} sm={12} mt={2}>
                    {compensationTypes.map((type, index) => (
                      <CustomRadioIcons
                        key={index}
                        icon={type.icon}
                        name="custom-radios-icons"
                        data={compensationTypes[index]}
                        selected={selectedCompensation}
                        handleChange={(selectedValue) => {
                          handleCompensationChange(selectedValue);
                          onChange(selectedValue === "paid" ? "paid" : "free");
                        }}
                        gridProps={{ sm: 4, xs: 12 }}
                        iconProps={type.iconProps}
                      />
                    ))}
                  </Grid>
                  {errors.compensationType && (
                    <FormHelperText error>
                      {errors.compensationType.message}
                    </FormHelperText>
                  )}
                </>
              )}
            />
          </FormControl>
        </Grid>

        {selectedCompensation === "paid" && (
          <Grid item xs={12} sm={6}>
            <FormControl fullWidth>
              <Typography
                variant="filled"
                sx={{ fontWeight: 600, color: "text.primary" }}
              >
                Compensation Type
              </Typography>
              <Controller
                name="compensation"
                control={control}
                render={({ field: { value, onChange } }) => (
                  <>
                    <Select
                      labelId="compensation-type"
                      value={value?.name || ""}
                      onChange={(event) => {
                        const selectedPolicy = compensationPolicies.find(
                          (policy) => policy.name === event.target.value
                        );
                        onChange(selectedPolicy);
                      }}
                      label="Compensation Type"
                    >
                      {compensationPolicies.map((policy) => (
                        <MenuItem key={policy.id} value={policy.name}>
                          {policy.name}
                        </MenuItem>
                      ))}
                    </Select>
                    {errors.compensation && (
                      <FormHelperText error>
                        {errors.compensation.message}
                      </FormHelperText>
                    )}
                  </>
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
