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
import UserInformation from "../registration-components/steps/UserInformation";
import AgentInformation from "../registration-components/steps/agent-steps/AgentInformation";
import ReviewAndComplete from "../registration-components/steps/agent-steps/ReviewAndComplete";
import authConfig from "src/configs/auth";
import organizationService from "src/provenAI-sdk/organizationService";
import agentService from "src/provenAI-sdk/agentService";
import agentPurposeOfUsePoliciesService from "src/provenAI-sdk/agentPurposeOfUsePoliciesService";
import organizationConverter from "src/converters/organizationConverter";
import agentConverter from "src/converters/agentConverter";
import AgentPurposeOfUsePoliciesConverter from "src/converters/agentPurposeOfUsePoliciesConverter";

import {
  agentSteps,
  defaultUserInformation,
  defaultAgentInformation,
} from "src/utils/defaultValues";
import ssiService from "../../../provenAI-sdk/ssiService";
import CredentialsWithQrCodeComponent from "../registration-components/CredentialsWithQrCodeComponent";

const AgentStepper = ({
  activeAgent,
  activeOrganization,
  agentPolicies,
  userAgents,
  userOrganizations,
  organizationId,
  agentId,
  activeStep,
  setActiveStep,
  vcOfferSessionId,
}) => {
  const theme = useTheme();
  const router = useRouter();
  const storedToken = window.localStorage.getItem(
    authConfig.storageTokenKeyName
  );

  // Form data states
  const [userData, setUserData] = useState(defaultUserInformation);
  const [agentData, setAgentData] = useState(defaultAgentInformation);
  const [agentUpdated, setAgentUpdated] = useState(false);
  const [isSubmitComplete, setIsSubmitComplete] = useState(false);

  const [userErrors, setUserErrors] = useState({});
  const [agentErrors, setAgentErrors] = useState({});


  useEffect(() => {
    if (Object.keys(activeOrganization).length !== 0) {
      const userInfo =
        organizationConverter.toUserInformation(activeOrganization);
      setUserData((prevData) => {
        const updatedUserInfo = { ...userInfo };

        // check if any field is empty, if so, keep the previous value
        Object.keys(userInfo).forEach((key) => {
          if (
            userInfo[key] === "" ||
            userInfo[key] === null ||
            userInfo[key] === undefined
          ) {
            updatedUserInfo[key] = prevData[key];
          }
        });

        return updatedUserInfo;
      });
    } else {
      // new organization
      setUserData((prevData) => ({
        ...defaultUserInformation,
        organizationName: prevData.organizationName,
      }));
    }
  }, [activeOrganization]);

  useEffect(() => {
    if (agentPolicies && agentPolicies.length > 0) {
      const agentDataPolicies = agentConverter.toAgentPolicies(agentPolicies);
      setAgentData((prevAgentData) => ({
        ...agentDataPolicies,
      }));
    } else {
      setAgentData((prevAgentData) => ({
        ...defaultAgentInformation,
        agentName: prevAgentData.agentName,
        agentUserId: prevAgentData.agentUserId,
      }));
    }
  }, [agentPolicies]);

  const handleBack = () => {
    setActiveStep((prevActiveStep) => prevActiveStep - 1);
  };

  const refreshPage = () => {
    const url = `/provenAI/agent-control?organizationId=${organizationId}&agentId=${agentId}`;
    router.reload(url);
  };

  const getVcOfferUrl = async () => {
    const offer = await organizationService.getVcOfferUrl(
      storedToken,
      organizationId,
      router.asPath
    );
    return offer.data.credentialVerificationUrl;
  };

  const onSubmit = async () => {
    setActiveStep((prevActiveStep) => prevActiveStep + 1);
    if (activeStep === agentSteps.length - 1) {
      try {
        const organizationDTO = organizationConverter.toOrganizationDTO(
          organizationId,
          userData
        );
        if (Object.keys(activeOrganization).length !== 0) {
          await organizationService.updateOrganization(
            organizationDTO,
            storedToken
          );
          toast.success("Organization updated successfully!");
        } else {
          await organizationService.createOrganization(
            organizationDTO,
            storedToken
          );
          toast.success("Organization registration successfully!");
        }

        if (Object.keys(activeAgent).length === 0) {
          const agentDTO = agentConverter.toAgentDTO(
            agentData,
            organizationId,
            agentId
          );
          await agentService.createAgent(agentDTO, storedToken);
          setAgentUpdated(true);
          toast.success("Agent created successfully!");
        }

        const { policiesToCreate, policyIdsToDelete } =
          AgentPurposeOfUsePoliciesConverter.convertAndComparePolicies(
            agentData,
            agentPolicies,
            agentId
          );

        // Create new policies
        for (const policy of policiesToCreate) {
          await agentPurposeOfUsePoliciesService.createAgentPurposeOfUsePolicy(
            policy,
            storedToken
          );

          setAgentUpdated(true);

          toast.success("Policy created successfully!");
        }

        // Delete obsolete policies
        if (policyIdsToDelete.length > 0) {
          await agentPurposeOfUsePoliciesService.deleteAgentPurposeOfUsePolicies(
            policyIdsToDelete,
            storedToken
          );
          setAgentUpdated(true);
          toast.success("Policy deleted successfully!");
        }

        if (agentUpdated && toast.success) {
          const agentOfferVc = await getAgentOfferVc();
        }

        setIsSubmitComplete(true);
        toast.success("Agent purpose of use policies updated successfully!");
      } catch (error) {
        console.error("Error updating Agent purpose of use policies:", error);
        toast.error("Failed to update Agent purpose of use policies!");
      }
    }
  };

  const getAgentOfferVc = async () => {
    const agentOfferVcResponse = await ssiService.getAiAgentIdCredentialOffer(
      agentId,
      storedToken
    );
    return agentOfferVcResponse.data;
  };

  const getAgentOfferVcUrl = async () => {
    const agentOfferVcResponse = await ssiService.getAiAgentIdCredentialOffer(
      agentId,
      storedToken
    );
    return agentOfferVcResponse.data.credentialOfferUrl;
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
            activeOrganization={activeOrganization}
            secondFieldOnUrl={
              Object.keys(activeAgent).length || vcOfferSessionId
            }
            userOrganizations={userOrganizations}
            getVcOfferUrl={getVcOfferUrl}
            vcOfferSessionId={vcOfferSessionId}
            setUserErrors={setUserErrors}
          />
        );
      case 1:
        return (
          <AgentInformation
            onSubmit={onSubmit}
            handleBack={handleBack}
            agentData={agentData}
            setAgentData={setAgentData}
            activeAgent={activeAgent}
            userAgents={userAgents}
            organizationId={organizationId}
            setActiveStep={setActiveStep}
            setAgentErrors={setAgentErrors}
          />
        );
      case 2:
        return (
          <ReviewAndComplete
            onSubmit={onSubmit}
            handleBack={handleBack}
            userData={userData}
            agentData={agentData}
            setActiveStep={setActiveStep}
          />
        );
      default:
        return null;
    }
  };

  const renderContent = () => {
    if (activeStep === agentSteps.length && isSubmitComplete) {
      return (
        <Fragment>
          <Box sx={{ textAlign: "center", mt: 4 }}>
            <Typography variant="h4" gutterBottom>
              All steps are completed!
            </Typography>
            <Typography variant="subtitle1" color="textSecondary" gutterBottom>
              You can now receive your Agent ID Credential by scanning the QR
              code below.
            </Typography>
          </Box>
          <Box sx={{ display: "flex", justifyContent: "center", mt: 4 }}>
            <CredentialsWithQrCodeComponent
              title={"Receive your Agent ID Credential"}
              handleCredentialsClose={null}
              getURL={getAgentOfferVcUrl}
            />
          </Box>
          <Box sx={{ mt: 4, display: "flex", justifyContent: "center" }}>
            <Button
              size="large"
              variant="contained"
              color="primary"
              onClick={refreshPage}
            >
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
            {agentSteps.map((step, index) => {
              const labelProps = {};

              if (index === activeStep) {
                labelProps.error = false;

                // User information errors
                if (activeStep === 0) {
                  if (
                    userErrors.firstName ||
                    userErrors.familyName ||
                    userErrors.dateOfBirth ||
                    userErrors.gender ||
                    userErrors.nationality ||
                    userErrors.profileLink ||
                    userErrors.legalPersonIdentifier ||
                    userErrors.legalName ||
                    userErrors.legalAddress ||
                    userErrors.country ||
                    userErrors.taxReference ||
                    userErrors.vatNumber
                  ) {
                    labelProps.error = true;
                  }
                }

                // Agent information errors
                if (activeStep === 1 && agentErrors.agentPurpose || agentErrors.compensationType) {
                  labelProps.error = true;
                }
                
              }
              
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

export default AgentStepper;
