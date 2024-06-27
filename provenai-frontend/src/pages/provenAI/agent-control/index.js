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
import AgentStepper from "src/views/provenAI/agent-control/AgentStepper";

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
  const { organizationId, agentId, vcOfferSessionId } = router.query;
  const auth = useAuth();
  const userOrganizations = auth?.user?.organizations;
  const [activeOrganization, setActiveOrganization] = useState({});
  const [activeAgent, setActiveAgent] = useState({});
  const [agentPolicies, setAgentPolicies] = useState({});
  const [userAgents, setUserAgents] = useState([]);
  const [activeStep, setActiveStep] = useState(0);

  const storedToken = window.localStorage.getItem(
    authConfig.storageTokenKeyName
  );

  useEffect(() => {
    setActiveStep(0);
  }, [organizationId]);

  useEffect(() => {
    if (!organizationId) {
      setActiveOrganization({});
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
        if (error.response.status === 404) {
          setActiveOrganization({});
          setActiveAgent({});
          setAgentPolicies({});
        }
      }


      try {
        const userAgents = await agentService.getUserAgentsByOrganizationId(
          organizationId,
          storedToken
        );
        setUserAgents(userAgents.data.content);
      } catch (error) {
        console.error("Error fetching user agents:", error);
      }
    };

    if (organizationId) {
      fetchOrganization();
    }
  }, [organizationId, storedToken]);

  useEffect(() => {
    if (!agentId) {
      setActiveAgent({});
      setAgentPolicies({});
    }

    const fetchAgent = async () => {
      try {
        const agent = await agentService.getAgentById(agentId, storedToken);
        setActiveAgent(agent.data);
      } catch (error) {
        console.error("Error fetching agent:", error);
        if (error.response.status === 404) {
          setActiveAgent({});
        }
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
        console.error("Error fetching Agent Policies:", error);
        if (error.response.status === 404) {
          setAgentPolicies({});
        }
      }
    };

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

      <AgentStepper
        userOrganizations={userOrganizations}
        activeOrganization={activeOrganization}
        activeAgent={activeAgent}
        agentPolicies={agentPolicies}
        userAgents={userAgents}
        activeStep={activeStep}
        setActiveStep={setActiveStep}
        organizationId={organizationId}
        agentId={agentId}
        vcOfferSessionId={vcOfferSessionId}
      />
    </Card>
  );
};

export default AgentControl;
