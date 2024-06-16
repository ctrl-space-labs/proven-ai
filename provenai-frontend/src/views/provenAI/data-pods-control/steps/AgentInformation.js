// ** React Imports
import React from "react";
import { useEffect, useState } from "react";

import {
  Grid,
  Typography,
  FormControl,
  TextField,
  Button,  
  Chip,
  Autocomplete,
} from "@mui/material";
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
  const [agents, setAgents] = useState([]);

  const storedToken = window.localStorage.getItem(
    authConfig.storageTokenKeyName
  );

  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm({
    defaultValues: agentData,
    resolver: yupResolver(agentSchema),
  });

  useEffect(() => {
    const fetchPolicyOptions = async () => {
      try {
        const policies = await policyService.getPolicyOptions(
          "USAGE_POLICY",
          storedToken
        );
        const transformedPolicies = policies.data.map(policy => ({
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
        const agents = await agentService.getAgentWithoutVc(storedToken);
        setAgents(agents.data.content);
      } catch (error) {
        console.error("Error fetching agents:", error);
      }
    };

    fetchPolicyOptions();
    fetchAgents();
  }, [storedToken]);

  const updateAgentDataWithNames = (data) => {
    if (agents.length > 0) {
      const updatedAgentData = {
        ...data,
        allowList: data.allowList.map((allow) => {
          const agent = agents.find((agent) => agent.id === allow.agentId);
          return agent ? { ...allow, name: agent.agentName } : allow;
        }),
        denyList: data.denyList.map((deny) => {
          const agent = agents.find((agent) => agent.id === deny.agentId);
          return agent ? { ...deny, name: agent.agentName } : deny;
        }),
      };
      return updatedAgentData;
    }
  };
  

  const handleFormSubmit = (data) => {
    const updatedData = updateAgentDataWithNames(data);
    setAgentData(updatedData);
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
                  // value={value? value.map((deny) => deny.name) : []}
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
                    <TextField {...params} sx={{ mb: 2, mt: 2 }} />
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

export default AgentInformation;
