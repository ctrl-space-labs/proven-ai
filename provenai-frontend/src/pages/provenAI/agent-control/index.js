import React, { useEffect, useState } from "react";
import { useRouter } from "next/router";
import { useAuth } from "src/hooks/useAuth";
import authConfig from "src/configs/auth";

// MUI components
import { styled } from "@mui/material/styles";
import Typography from "@mui/material/Typography";
import CardContent from "@mui/material/CardContent";
import Card from "@mui/material/Card";
import Box from "@mui/material/Box";

import organizationService from "src/provenAI-sdk/organizationService";
import agentService from "src/provenAI-sdk/agentService";

import AgentStepperLinearWithValidation from "src/views/provenAI/agent-control/AgentStepperLinearWithValidation";

const StyledCardContent = styled(CardContent)(({ theme }) => ({
  paddingTop: `${theme.spacing(10)} !important`,
  paddingBottom: `${theme.spacing(8)} !important`,
  [theme.breakpoints.up("sm")]: {
    paddingLeft: `${theme.spacing(20)} !important`,
    paddingRight: `${theme.spacing(20)} !important`,
  },
}));

const AgentControl = () => {
  const router = useRouter();
  const { organizationId, agentId } = router.query;
  const auth = useAuth();

  const userOrganizations = auth?.user?.organizations;
  const [activeOrganization, setActiveOrganization] = useState({});
  const [activeAgent, setActiveAgent] = useState({});
  const [agentPolicies, setAgentPolicies] = useState({});

  const storedToken = window.localStorage.getItem(
    authConfig.storageTokenKeyName
  );

  console.log("activeOrganization00", activeOrganization);
  console.log("activeAgent00", activeAgent);
  console.log("agentPolicies00", agentPolicies);

  useEffect(() => {
    if (!organizationId) {
      setActiveOrganization({});
    }

    if (!agentId) {
      setActiveAgent({});
    }

    const fetchOrganization = async () => {
      try {
        const organization =
          await organizationService.getProvenOrganizationById(
            organizationId,
            storedToken
          );
        setActiveOrganization(organization.data);
      } catch (error) {
        console.error("Error fetching organization:", error);
      }
    };

    const fetchAgent = async () => {
      try {
        const agent = await agentService.getAgentById(agentId, storedToken);
        setActiveAgent(agent.data);
      } catch (error) {
        console.error("Error fetching agent:", error);
      }
    };

    const fetchAgentPolicies = async () => {
      try {
        const agent = await agentService.getPoliciesByAgent(
          agentId,
          storedToken
        );
        setAgentPolicies(agent.data.content);
      } catch (error) {
        console.error("Error fetching data pod:", error);
      }
    };

    if (organizationId) {
      fetchOrganization();
    }

    if (agentId) {
      fetchAgent();
      fetchAgentPolicies();
    }
    
  }, [storedToken, organizationId, agentId]);

  return (
    <Card sx={{ backgroundColor: "transparent", boxShadow: "none" }}>
      <StyledCardContent sx={{ backgroundColor: "background.paper" }}>
        <Box
          sx={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
          }}
        >
          <Typography
            variant="h3"
            sx={{ mb: 3, fontWeight: 600, textAlign: "left" }}
          >
            Agent Control Policies
          </Typography>
        </Box>
      </StyledCardContent>
      <Box sx={{ height: 20 }} />

      <AgentStepperLinearWithValidation
        userOrganizations={userOrganizations}
        activeOrganization={activeOrganization}
        activeAgent={activeAgent}
        agentPolicies={agentPolicies}
      />
    </Card>
  );
};

export default AgentControl;
