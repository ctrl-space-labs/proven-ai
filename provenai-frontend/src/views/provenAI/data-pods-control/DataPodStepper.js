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
import UserInformation from "../registration-components/steps/UserInformation";
import DataPodInformation from "../registration-components/steps/data-pod-steps/DataPodInformation";
import UsePolicy from "../registration-components/steps/data-pod-steps/UsePolicies";
import ReviewAndComplete from "../registration-components/steps/data-pod-steps/ReviewAndComplete";
import authConfig from "src/configs/auth";
import organizationService from "src/provenAI-sdk/organizationService";
import dataPodsService from "src/provenAI-sdk/dataPodsService";
import aclPoliciesService from "src/provenAI-sdk/aclPoliciesService";
import aclPoliciesConverter from "src/converters/aclPoliciesConverter";
import organizationConverter from "src/converters/organizationConverter";
import dataPodConverter from "src/converters/dataPodConverter";

import {
  dataPodSteps,
  defaultUserInformation,
  defaultDataPodInformation,
  defaultDataUse,
} from "src/utils/defaultValues";

const DataPodStepper = ({
  activeDataPod,
  activeOrganization,
  dataPodPolicies,
  userDataPods,
  userOrganizations,
  organizationId,
  dataPodId,
  activeStep,
  setActiveStep,
  vcOfferSessionId,
}) => {
  const router = useRouter();

  const storedToken = window.localStorage.getItem(
    authConfig.storageTokenKeyName
  );

  const [userErrors, setUserErrors] = useState({});
  const [dataPodErrors, setDataPodErrors] = useState({});
  const [usePoliciesErrors, setUsePoliciesErrors] = useState({});

  // Form data states
  const [userData, setUserData] = useState(defaultUserInformation);
  const [dataPodData, setDataPodData] = useState(defaultDataPodInformation);
  const [usePoliciesData, setUsePoliciesData] = useState(defaultDataUse);


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
    if (dataPodPolicies && dataPodPolicies.length > 0) {
      const agentPolicies = dataPodConverter.toDataPodPolicies(dataPodPolicies);
      setDataPodData((prevAgentData) => ({
        ...agentPolicies,
      }));

      const usePolicies = dataPodConverter.toUsePolicies(dataPodPolicies);
      setUsePoliciesData((prevUsePoliciesData) => ({
        ...usePolicies,
      }));
    } else {
      setDataPodData((prevData) => ({
        ...defaultDataPodInformation,
        dataPodName: prevData.dataPodName,
      }));
      setUsePoliciesData(defaultDataUse);
    }
  }, [dataPodPolicies]);

  const handleBack = () => {
    setActiveStep((prevActiveStep) => prevActiveStep - 1);
  };

  const refreshPage = () => {
    const url = `/provenAI/data-pods-control?organizationId=${activeOrganization.id}&dataPodId=${activeDataPod.id}`;
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
    if (activeStep === dataPodSteps.length - 1) {
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

        if (Object.keys(activeDataPod).length === 0) {
          const dataPodDTO = dataPodConverter.toDataPodDTO(
            dataPodData,
            organizationId,
            dataPodId
          );

          await dataPodsService.createDataPod(dataPodDTO, storedToken);
          toast.success("Data Pod created successfully!");
        }

        // Convert and compare policies
        const { aclPoliciesToCreate, aclPolicyIdsToDelete } =
          aclPoliciesConverter.convertAndComparePolicies(
            dataPodData,
            usePoliciesData,
            dataPodPolicies,
            dataPodId
          );

        // Create new ACL policies
        for (const aclPolicyDTO of aclPoliciesToCreate) {
          await aclPoliciesService.createAclPolicy(aclPolicyDTO, storedToken);
          toast.success("Policy created successfully!");
        }

        // Delete obsolete ACL policies
        if (aclPolicyIdsToDelete.length > 0) {
          await aclPoliciesService.deleteAclPolicies(
            dataPodId,
            aclPolicyIdsToDelete,
            storedToken
          );
          toast.success("Policies deleted successfully!");
        }

        toast.success("ACL policies updated successfully!");
      } catch (error) {
        console.error("Error updating ACL policies:", error);
        toast.error("Failed to update ACL policies!");
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
            userOrganizations={userOrganizations}
            secondFieldOnUrl={
              Object.keys(activeDataPod).length || vcOfferSessionId
            }
            activeOrganization={activeOrganization}
            getVcOfferUrl={getVcOfferUrl}
            vcOfferSessionId={vcOfferSessionId}
            setUserErrors={setUserErrors}
          />
        );
      case 1:
        return (
          <DataPodInformation
            onSubmit={onSubmit}
            handleBack={handleBack}
            dataPodData={dataPodData}
            userDataPods={userDataPods}
            setDataPodData={setDataPodData}
            activeDataPod={activeDataPod}
            organizationId={organizationId}
            setActiveStep={setActiveStep}
            setDataPodErrors={setDataPodErrors}
          />
        );
      case 2:
        return (
          <UsePolicy
            onSubmit={onSubmit}
            handleBack={handleBack}
            usePoliciesData={usePoliciesData}
            setUsePoliciesData={setUsePoliciesData}
            setActiveStep={setActiveStep}
            setUsePoliciesErrors={setUsePoliciesErrors}
          />
        );
      case 3:
        return (
          <ReviewAndComplete
            onSubmit={onSubmit}
            handleBack={handleBack}
            userData={userData}
            dataPodData={dataPodData}
            usePoliciesData={usePoliciesData}
            setActiveStep={setActiveStep}
          />
        );
      default:
        return null;
    }
  };

  const renderContent = () => {
    if (activeStep === dataPodSteps.length) {
      return (
        <Fragment>
          <Box sx={{ textAlign: "center", mt: 4 }}>
            <Typography variant="h4" gutterBottom>
              All steps are completed!
            </Typography>
            <Typography variant="subtitle1" color="textSecondary" gutterBottom>
              Thank you for completing all the steps. You can now proceed
              further.
            </Typography>
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
            {dataPodSteps.map((step, index) => {
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

                // Data Pod information errors
                if (activeStep === 1 && dataPodErrors.agentPurpose ) {
                  labelProps.error = true;
                }

                // Data use policy errors
                if (activeStep === 2 && usePoliciesErrors.compensationPolicies || usePoliciesErrors.attributionPolicies) {
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

export default DataPodStepper;
