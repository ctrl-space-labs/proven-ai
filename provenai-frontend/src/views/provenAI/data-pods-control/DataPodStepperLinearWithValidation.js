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

// ** Step Components Imports
import UserInformation from "./steps/UserInformation";
import AgentInformation from "./steps/AgentInformation";
import UsePolicy from "./steps/UsePolicies";
import ReviewAndComplete from "./steps/ReviewAndComplete";

import authConfig from "src/configs/auth";
import organizationService from "src/provenAI-sdk/organizationService";
import dataPodsService from "src/provenAI-sdk/dataPodsService";

import converter from "src/views/provenAI/data-pods-control/utils/converter";


import {
  steps,
  defaultUserInformation,
  defaultAgentInformation,
  defaultDataUse,
} from "src/views/provenAI/data-pods-control/utils/defaultValues";

const StepperLinearWithValidation = () => {
  const theme = useTheme();
  const router = useRouter();
  const { organizationId, dataPodId } = router.query;
  const storedToken = window.localStorage.getItem(
    authConfig.storageTokenKeyName
  );
  const [activeStep, setActiveStep] = useState(0);
  const [activeOrganization, setActiveOrganization] = useState({});
  const [activeDataPod, setActiveDataPod] = useState({});
  const [userErrors, setUserErrors] = useState({});
  const [agentErrors, setAgentErrors] = useState({});
  const [dataUseErrors, setDataUseErrors] = useState({});

  // Form data states
  const [userData, setUserData] = useState(defaultUserInformation);
  const [agentData, setAgentData] = useState(defaultAgentInformation);
  const [usePoliciesData, setUsePoliciesData] = useState(defaultDataUse);

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

    const fetchDataPod = async () => {
      try {
        const dataPod = await dataPodsService.getAclPoliciesByDataPod(
          dataPodId,
          storedToken
        );
        setActiveDataPod(dataPod.data.content);
      } catch (error) {
        console.error("Error fetching data pod:", error);
      }
    };

    fetchOrganization();
    fetchDataPod();
  }, [storedToken, organizationId, dataPodId]);

  useEffect(() => {
    if (Object.keys(activeOrganization).length !== 0) {
      const userInfo = converter.toUserInformation(activeOrganization);
      setUserData(userInfo);
    }
  }, [activeOrganization]);

 

  useEffect(() => {
    if (activeDataPod && activeDataPod.length > 0) {
      const agentPolicies = converter.toAgentPolicies(activeDataPod);
      setAgentData((prevAgentData) => ({
        ...prevAgentData,
        agentPurpose: agentPolicies.agentPurposes,
        allowList: agentPolicies.allowList,
        denyList: agentPolicies.denyList,
      }));

      const usePolicies = converter.toUsePolicies(activeDataPod);
      setUsePoliciesData((prevUsePoliciesData) => ({
        ...prevUsePoliciesData,
        ...usePolicies,
      }));
    }
  }, [activeDataPod]);

  // console.log("userData", userData);
  // console.log("agentData", agentData);
  // console.log("dataUseData", dataUseData);
  // console.log("activeDataPod", activeDataPod);

  const handleBack = () => {
    setActiveStep((prevActiveStep) => prevActiveStep - 1);
  };

  const handleReset = () => {
    setActiveStep(0);
    // Reset logic here
  };

  const onSubmit = () => {
    setActiveStep((prevActiveStep) => prevActiveStep + 1);
    if (activeStep === steps.length - 1) {
      toast.success("Form Submitted");
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
          <UsePolicy
            onSubmit={onSubmit}
            handleBack={handleBack}
            usePoliciesData={usePoliciesData}
            setUsePoliciesData={setUsePoliciesData}
          />
        );
      case 3:
        return (
          <ReviewAndComplete
            onSubmit={onSubmit}
            handleBack={handleBack}
            userData={userData}
            agentData={agentData}
            usePoliciesData={usePoliciesData}
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
            <Button size="large" variant="contained" onClick={handleReset}>
              Reset
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
