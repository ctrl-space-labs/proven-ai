// ** React Imports
import React from "react";
import { useEffect, useState } from "react";
import { useRef } from "react";
import { useRouter } from "next/router";
import {
  Grid,
  Typography,
  FormControl,
  TextField,
  Button,
  Chip,
  Autocomplete,
  MenuItem,
  InputLabel,
  Select,
  FormHelperText,
} from "@mui/material";
import { useForm, Controller } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { dataPodSchema } from "src/utils/validationSchemas";
import policyService from "src/provenAI-sdk/policyService";
import agentService from "src/provenAI-sdk/agentService";
import authConfig from "src/configs/auth";
import { set } from "nprogress";

const DataPodInformation = ({
  onSubmit,
  handleBack,
  dataPodData,
  setDataPodData,
  userDataPods,
  activeDataPod,
  organizationId,
  setActiveStep,
  setDataPodErrors,
}) => {
  const router = useRouter();
  const isFirstRender = useRef(true);
  const [usagePolicies, setUsagePolicies] = useState([]);
  const [agents, setAgents] = useState([]);
  const [selectNewDataPod, setSelectNewDataPod] = useState(false);

  const storedToken = window.localStorage.getItem(
    authConfig.storageTokenKeyName
  );

  const {
    control,
    handleSubmit,
    watch,
    setValue,
    formState: { errors },
  } = useForm({
    defaultValues: dataPodData,
    resolver: yupResolver(dataPodSchema),
  });

  useEffect(() => {
    Object.keys(dataPodData).forEach((key) => {
      setValue(key, dataPodData[key]);
    });
  }, [dataPodData, setValue]);

  useEffect(() => {
    setDataPodErrors(errors);
  }, [errors, setDataPodErrors]);

  useEffect(() => {
    if (isFirstRender.current) {
      isFirstRender.current = false;
      return;
    }
    if (!selectNewDataPod) {
      setActiveStep(0);
    }
  }, [router.query.dataPodId]);

  useEffect(() => {
    const fetchPolicyOptions = async () => {
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
        console.error("Error fetching policy options:", error);
      }
    };

    const fetchAgents = async () => {
      try {
        const agents = await agentService.getPublicAgents(storedToken);
        setAgents(agents.data.content);
      } catch (error) {
        console.error("Error fetching agents:", error);
      }
    };

    fetchPolicyOptions();
    fetchAgents();
  }, [storedToken]);

  useEffect(() => {
    if (activeDataPod?.id) {
      const activeDataPodName =
        userDataPods.find((dp) => dp.id === activeDataPod.id)?.name || "";
      setValue("dataPodName", activeDataPodName);
    }
  }, [activeDataPod, userDataPods, setValue]);

  const updateAgentData = async (data) => {
    if (agents.length > 0) {
      let allowPolicies, denyPolicies;
      try {
        allowPolicies = await policyService.getPolicyOptions(
          "ALLOW_LIST",
          storedToken
        );        
      } catch (error) {
        console.error("Error fetching allow policy options:", error);
      }

      try {
        denyPolicies = await policyService.getPolicyOptions(
          "DENY_LIST",
          storedToken
        );        
      } catch (error) {
        console.error("Error fetching deny policy options:", error);
      }

      const allowAgentPolicy = allowPolicies?.data.find(
        (policy) => policy.name === "ALLOW_AGENT_NAME"
      );
      const denyAgentPolicy = denyPolicies?.data.find(
        (policy) => policy.name === "DENY_AGENT_NAME"
      );

      const updatedAgentData = {
        ...data,
        allowList: data.allowList.map((allow) => {
          const agent = agents.find((agent) => agent.id === allow.agentId);
          return agent
            ? {
                ...allow,
                name: agent.agentName,
                policyOptionId: allowAgentPolicy?.id || "",
                policyTypeId: allowAgentPolicy?.policyTypeId || "",
              }
            : allow;
        }),
        denyList: data.denyList.map((deny) => {
          const agent = agents.find((agent) => agent.id === deny.agentId);
          return agent
            ? {
                ...deny,
                name: agent.agentName,
                policyOptionId: denyAgentPolicy?.id || "",
                policyTypeId: denyAgentPolicy?.policyTypeId || "",
              }
            : deny;
        }),
      };
      return updatedAgentData;
    }
  };

  const handleFormSubmit = async (data) => {
    const updatedData = await updateAgentData(data);
    setDataPodData(updatedData);
    onSubmit();
  };

  const handleMenuItemClick = (daPod) => {
    setSelectNewDataPod(true);
    setDataPodData((prevData) => ({
      ...prevData,
      dataPodName: daPod.name,
    }));
    set;
    updateShallowQueryParams({ organizationId, dataPodId: daPod.id });
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
            Data Pod Information
          </Typography>
          <Typography variant="subtitle2" component="p">
            Enter your references to sort your data pods
          </Typography>
        </Grid>

        <Grid item xs={12} sm={6}>
          {(!Object.keys(activeDataPod).length > 0 || selectNewDataPod) && (
            <FormControl fullWidth>
              <InputLabel id="user-data-pod-label">Select Data Pod</InputLabel>
              <Controller
                name="dataPodName"
                control={control}
                render={({ field }) => (
                  <Select
                    labelId="user-data-pod-label"
                    {...field}
                    label="Data Pod"
                  >
                    {userDataPods.map((dp) => (
                      <MenuItem
                        key={dp.id}
                        value={dp.name}
                        onClick={() => handleMenuItemClick(dp)}
                      >
                        {dp.name}
                      </MenuItem>
                    ))}
                  </Select>
                )}
              />
              {errors.dataPodName && (
                <FormHelperText error>
                  {errors.dataPodName.message}
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
              variant="body2"
              sx={{ fontWeight: 600, color: "text.primary" }}
            >
              Deny list
            </Typography>

            <Controller
              name="denyList"
              control={control}
              render={({ field: { value, onChange } }) => (
                <Autocomplete
                  multiple
                  sx={{
                    width: "80%",
                    mt: 2,
                    "& .MuiOutlinedInput-root": {
                      "& fieldset": {
                        borderColor: "red",
                      },
                      "&:hover fieldset": {
                        borderColor: "red",
                      },
                      "&.Mui-focused fieldset": {
                        borderColor: "red",
                      },
                    },
                  }}
                  id="autocomplete-multiple-filled-deny"
                  value={
                    value
                      ? value.map(
                          (deny) =>
                            agents.find((agent) => agent.id === deny.agentId)
                              ?.agentName || deny.name
                        )
                      : []
                  }
                  onChange={(event, newValue) => {
                    const updatedValues = newValue.map((name) => {
                      const agent = agents.find(
                        (agent) => agent.agentName === name
                      );
                      return agent
                        ? { agentId: agent.id, name: agent.agentName }
                        : { agentId: name, name };
                    });
                    onChange(updatedValues);
                  }}
                  options={agents.map((agent) => agent.agentName)}
                  renderInput={(params) => (
                    <TextField
                      {...params}
                      sx={{ mb: 2, mt: 2 }}
                      placeholder="Select agents"
                      error={!!errors.denyList}
                      helperText={
                        errors.denyList ? errors.denyList.message : ""
                      }
                    />
                  )}
                  renderTags={(value, getTagProps) =>
                    value.map((agent, index) => (
                      <Chip
                        variant="outlined"
                        label={agent}
                        {...getTagProps({ index })}
                        key={index}
                        sx={{
                          mr: 1,
                          mb: 1,
                          mt: 1,
                          backgroundColor: "red",
                        }}
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
              variant="body2"
              sx={{ fontWeight: 600, color: "text.primary" }}
            >
              Allow list
            </Typography>

            <Controller
              name="allowList"
              control={control}
              render={({ field: { value, onChange } }) => (
                <Autocomplete
                  multiple
                  sx={{
                    width: "80%",
                    mt: 2,
                    "& .MuiOutlinedInput-root": {
                      "& fieldset": {
                        borderColor: "green",
                      },
                      "&:hover fieldset": {
                        borderColor: "green",
                      },
                      "&.Mui-focused fieldset": {
                        borderColor: "green",
                      },
                    },
                  }}
                  id="autocomplete-multiple-filled-allow"
                  value={
                    value
                      ? value.map(
                          (allow) =>
                            agents.find((agent) => agent.id === allow.agentId)
                              ?.agentName || allow.name
                        )
                      : []
                  }
                  onChange={(event, newValue) => {
                    const updatedValues = newValue.map((name) => {
                      const agent = agents.find(
                        (agent) => agent.agentName === name
                      );
                      return agent
                        ? { agentId: agent.id, name: agent.agentName }
                        : { agentId: name, name };
                    });
                    onChange(updatedValues);
                  }}
                  options={agents.map((agent) => agent.agentName)}
                  renderInput={(params) => (
                    <TextField
                      {...params}
                      sx={{ mb: 2, mt: 2 }}
                      placeholder="Select agents"
                      error={!!errors.allowList}
                      helperText={
                        errors.allowList ? errors.allowList.message : ""
                      }
                    />
                  )}
                  renderTags={(value, getTagProps) =>
                    value.map((agent, index) => (
                      <Chip
                        variant="outlined"
                        label={agent}
                        {...getTagProps({ index })}
                        key={index}
                        sx={{
                          mr: 1,
                          mb: 1,
                          mt: 1,
                          backgroundColor: "green",
                        }}
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

export default DataPodInformation;
