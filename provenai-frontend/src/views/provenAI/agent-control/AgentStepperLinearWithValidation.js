// ** React Imports
import { Fragment, useState, useEffect } from "react";
import { styled, useTheme } from "@mui/material/styles";
import { useRouter } from "next/router";

// ** MUI Imports
import Box from "@mui/material/Box";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import Step from "@mui/material/Step";
import Stepper from "@mui/material/Stepper";
import StepLabel from "@mui/material/StepLabel";
import Divider from "@mui/material/Divider";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";

// ** Third Party Imports
import toast from "react-hot-toast";

// ** Custom Components Imports
import StepperCustomDot from "./StepperCustomDot";
import StepperWrapper from "src/@core/styles/mui/stepper";
import UserInformation from "./steps/UserInformation";
import AgentInformation from "./steps/AgentInformation";
import ReviewAndComplete from "./steps/ReviewAndComplete";
import authConfig from "src/configs/auth";
import organizationService from "src/provenAI-sdk/organizationService";
import agentService from "src/provenAI-sdk/agentService";
import agentPurposeOfUsePoliciesService from "src/provenAI-sdk/agentPurposeOfUsePoliciesService";
import converterToStepperData from "src/views/provenAI/agent-control/utils/converterToStepperData";
import convertToAgentPurposeOfUsePolicies from "src/views/provenAI/agent-control/utils/convertToAgentPurposeOfUsePolicies";

import {
  steps,
  defaultUserInformation,
  defaultAgentInformation,
} from "src/views/provenAI/agent-control/utils/defaultValues";

const StepperLinearWithValidation = () => {
  const theme = useTheme();
  const router = useRouter();
  const { organizationId, agentId } = router.query;
  const storedToken = window.localStorage.getItem(
    authConfig.storageTokenKeyName
  );

  const [activeStep, setActiveStep] = useState(0);
  const [activeOrganization, setActiveOrganization] = useState({});
  const [activeAgentPolicies, setActiveAgentPolicies] = useState({});

  // Form data states
  const [userData, setUserData] = useState(defaultUserInformation);
  const [agentData, setAgentData] = useState(defaultAgentInformation);

  useEffect(() => {
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

    const fetchAgentPolicies = async () => {
      try {
        const agent = await agentService.getPoliciesByAgent(
          agentId,
          storedToken
        );
        setActiveAgentPolicies(agent.data.content);
      } catch (error) {
        console.error("Error fetching data pod:", error);
      }
    };

    fetchOrganization();
    fetchAgentPolicies();
  }, [storedToken, organizationId, agentId]);

  useEffect(() => {
    if (Object.keys(activeOrganization).length !== 0) {
      const userInfo =
        converterToStepperData.toUserInformation(activeOrganization);
      setUserData(userInfo);
    }
  }, [activeOrganization]);

  useEffect(() => {
    if (activeAgentPolicies && activeAgentPolicies.length > 0) {
      const agentPolicies =
        converterToStepperData.toAgentPolicies(activeAgentPolicies);
      setAgentData((prevAgentData) => ({
        ...agentPolicies,
      }));
    }
  }, [activeAgentPolicies]);

  // console.log("userData106", userData);
  // console.log("agentData107", agentData);
  // console.log("ACTIVE AGENT POLICIES", activeAgentPolicies);

  const handleBack = () => {
    setActiveStep((prevActiveStep) => prevActiveStep - 1);
  };

  const refreshPage = () => {
    const url = `/provenAI/agent-control?organizationId=${organizationId}&agentId=${agentId}`;
    router.reload(url);
  };

  const onSubmit = async () => {
    setActiveStep((prevActiveStep) => prevActiveStep + 1);
    if (activeStep === steps.length - 1) {
      try {
        const organizationDTO = converterToStepperData.toOrganizationDTO(
          organizationId,
          userData
        );
        await organizationService.updateOrganization(
          organizationDTO,
          storedToken
        );
        toast.success("Organization updated successfully!");

        const { policiesToCreate, policyIdsToDelete } =
          convertToAgentPurposeOfUsePolicies.convertAndComparePolicies(
            agentData,
            activeAgentPolicies,
            agentId
          );

        // Create new policies
        for (const policy of policiesToCreate) {
          await agentPurposeOfUsePoliciesService.createAgentPurposeOfUsePolicy(
            policy,
            storedToken
          );

          console.log("Creating policy:", policy);
          toast.success("Policy created successfully!");
        }

        // Delete obsolete policies
        if (policyIdsToDelete.length > 0) {
          await agentPurposeOfUsePoliciesService.deleteAgentPurposeOfUsePolicies(
            policyIdsToDelete,
            storedToken
          );
          console.log("Deleting policy:", policyIdsToDelete);
          toast.success("Policy deleted successfully!");
        }

        toast.success("Agent purpose of use policies updated successfully!");
      } catch (error) {
        console.error("Error updating Agent purpose of use policies:", error);
        toast.error("Failed to update Agent purpose of use policies!");
      }
    }
  };

  const getStepContent = (step) => {
    switch (step) {
      case 0:
        return (
          <UserInformation
            onSubmit={onSubmit}
            handleBack={handleBack}
            userData={userData}
            setUserData={setUserData}
          />
        );
      case 1:
        return (
          <AgentInformation
            onSubmit={onSubmit}
            handleBack={handleBack}
            agentData={agentData}
            setAgentData={setAgentData}
          />
        );
      case 2:
        return (
          <ReviewAndComplete
            onSubmit={onSubmit}
            handleBack={handleBack}
            userData={userData}
            agentData={agentData}
          />
        );
      default:
        return null;
    }
  };

  const renderContent = () => {
    if (activeStep === steps.length) {
      return (
        <Fragment>
          <Typography>All steps are completed!</Typography>
          <Box sx={{ mt: 4, display: "flex", justifyContent: "flex-end" }}>
            <Button size="large" variant="contained" onClick={refreshPage}>
              Back
            </Button>
          </Box>
        </Fragment>
      );
    } else {
      return getStepContent(activeStep);
    }
  };

  return (
    <Card sx={{ backgroundColor: "action.hover" }}>
      <CardContent>
        <StepperWrapper>
          <Stepper activeStep={activeStep}>
            {steps.map((step, index) => {
              const labelProps = {};
              // if (index === activeStep) {
              //   labelProps.error = false;
              //   if (userInfo.selectedOrganizationType === "natural-person") {
              //     if (
              //       userErrors.firstName ||
              //       userErrors.familyName
              //       // ||
              //       // userErrors.dateOfBirth ||
              //       // userErrors.gender ||
              //       // userErrors.nationality ||
              //       // userErrors.profileLink
              //     ) {
              //       labelProps.error = true;
              //     }
              //   } else if (userInfo.selectedOrganizationType === "legal-entity") {
              //     if (
              //       userErrors.legalPersonIdentifier ||
              //       userErrors.legalName
              //       // ||
              //       // userErrors.legalAddress ||
              //       // userErrors.country ||
              //       // userErrors.taxReference ||
              //       // userErrors.vatNumber ||
              //       // userErrors.profileLink
              //     ) {
              //       labelProps.error = true;
              //     }
              //   } else if (agentErrors.agentPurpose && activeStep === 1) {
              //     labelProps.error = true;
              //   } else if (dataUseErrors.dataPurpose && activeStep === 2) {
              //     labelProps.error = true;
              //   } else {
              //     labelProps.error = false;
              //   }
              // }
              return (
                <Step key={index}>
                  <StepLabel
                    {...labelProps}
                    StepIconComponent={StepperCustomDot}
                  >
                    <div className="step-label">
                      <Typography className="step-number">{`0${
                        index + 1
                      }`}</Typography>
                      <div>
                        <Typography className="step-title">
                          {step.title}
                        </Typography>
                        <Typography className="step-subtitle">
                          {step.subtitle}
                        </Typography>
                      </div>
                    </div>
                  </StepLabel>
                </Step>
              );
            })}
          </Stepper>
        </StepperWrapper>
      </CardContent>
      <Divider sx={{ m: "0 !important" }} />
      <CardContent>{renderContent()}</CardContent>
    </Card>
  );
};

export default StepperLinearWithValidation;
