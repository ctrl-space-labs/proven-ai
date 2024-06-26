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
import convertToAgentPurposeOfUsePolicies from "src/converters/agentPurposeOfUsePoliciesConverter";

import {
  agentSteps,
  defaultUserInformation,
  defaultAgentInformation,
} from "src/utils/defaultValues";
import ssiService from "../../../provenAI-sdk/ssiService";
import CredentialsWithQrCodeComponent from "../registration-components/CredentialsWithQrCodeComponent";

const AgentStepperLinearWithValidation = ({
  userOrganizations,
  activeOrganization,
  activeAgent,
  agentPolicies,
  activeStep,
  setActiveStep,
  organizationId,
  agentId,
  userAgents,
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

  // console.log("USER AGENTS", userAgents);
  // console.log("User Data", userData);
  // console.log("Agent Data", agentData);
  // console.log("User Agents--->", userAgents);
  // console.log("Active Agent--->", activeAgent);

  useEffect(() => {
    if (vcOfferSessionId) {
      handleVcOfferFlow();
    }
  }, [vcOfferSessionId]);

  useEffect(() => {
    if (Object.keys(activeOrganization).length !== 0) {
      const userInfo =
      organizationConverter.toUserInformation(activeOrganization);
      setUserData(userInfo);
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
      const agentDataPolicies =
        agentConverter.toAgentPolicies(agentPolicies);
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

  /**
   * Handle successful VC offer
   *
   * @param organizationId
   * @return {Promise<boolean>}   true if VC offer flow completed successfully
   */
  const handleVcOfferFlow = async () => {
    let offeredVP = await ssiService.getVcOffered(vcOfferSessionId);
    if (offeredVP.data.policyResults.success !== true) {
      throw new Error("VC offer failed");
    }
    // offeredVP.data.policyResults -> this is an array. we are looking for the element that has value .credential === "VerifiablePresentation"
    // the in this element, has a array 'policies', we are looking for the element that has value .policy === "signature"
    let organizationDid = ssiService.getVerifiedVcSignaturePolicy(
      offeredVP.data
    ).sub;
    let vcCredentialSubject = ssiService.getVerifiedVcCredentialSubject(
      offeredVP.data
    );
    console.log(
      "VC CredentialSubject: ",
      ssiService.getVerifiedVcCredentialSubject(offeredVP.data)
    );
    console.log("organizationDid", organizationDid);

    setUserData((prevData) => ({
      ...prevData,
      organizationVpJwt: offeredVP.data.tokenResponse.vp_token,
      organizationDid: organizationDid,
    }));
  };

  const onSubmit = async () => {
    setActiveStep((prevActiveStep) => prevActiveStep + 1);
    let agentUpdated = false;
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
          agentUpdated = true;
          toast.success("Agent created successfully!");
        }

        const { policiesToCreate, policyIdsToDelete } =
          convertToAgentPurposeOfUsePolicies.convertAndComparePolicies(
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
          agentUpdated = true;

          console.log("Creating policy:", policy);
          toast.success("Policy created successfully!");
        }

        // Delete obsolete policies
        if (policyIdsToDelete.length > 0) {
          await agentPurposeOfUsePoliciesService.deleteAgentPurposeOfUsePolicies(
            policyIdsToDelete,
            storedToken
          );
          agentUpdated = true;
          console.log("Deleting policy:", policyIdsToDelete);
          toast.success("Policy deleted successfully!");
        }

        toast.success("Agent purpose of use policies updated successfully!");
        if (agentUpdated) {
          console.log(getAgentOfferVc());
        }
      } catch (error) {
        console.error("Error updating Agent purpose of use policies:", error);
        toast.error("Failed to update Agent purpose of use policies!");
      }
    }
  };

  const getAgentOfferVc = async () => {
    const agentOfferVcResponse = await ssiService.getAiAgentIdCredentialOffer(agentId, storedToken);
    return agentOfferVcResponse.data;
  }

  const getAgentOfferVcUrl = async () => {
    const agentOfferVcResponse = await ssiService.getAiAgentIdCredentialOffer(agentId, storedToken);
    return agentOfferVcResponse.data.credentialOfferUrl;
  }

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
            secondFieldOnUrl={Object.keys(activeAgent).length}
            userOrganizations={userOrganizations}
            getVcOfferUrl={getVcOfferUrl}
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
    if (activeStep === agentSteps.length) {
      return (
        <Fragment>
          <Typography>All steps are completed!</Typography>
          <CredentialsWithQrCodeComponent
              title={"Receive your Agent ID Credential"}
              handleCredentialsClose={null}
              getURL={getAgentOfferVcUrl} />
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
            {agentSteps.map((step, index) => {
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

export default AgentStepperLinearWithValidation;
