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
import DataPodInformation from "./steps/DataPodInformation";
import UsePolicy from "./steps/UsePolicies";
import ReviewAndComplete from "./steps/ReviewAndComplete";
import authConfig from "src/configs/auth";
import organizationService from "src/provenAI-sdk/organizationService";
import dataPodsService from "src/provenAI-sdk/dataPodsService";
import aclPoliciesService from "src/provenAI-sdk/aclPoliciesService";
import aclPoliciesConverter from "src/views/provenAI/data-pods-control/utils/convertToAclPolicies";
import converter from "src/views/provenAI/data-pods-control/utils/converterToStepperData";

import {
  steps,
  defaultUserInformation,
  defaultDataPodInformation,
  defaultDataUse,
} from "src/views/provenAI/data-pods-control/utils/defaultValues";
import ssiService from "../../../provenAI-sdk/ssiService";

const StepperLinearWithValidation = ({
  activeDataPod,
  activeOrganization,
  dataPodPolicies,
  userDataPods,
  userOrganizations,
  organizationId,
  dataPodId,
  activeStep,
  setActiveStep,
  vcOfferSessionId
}) => {
  const router = useRouter();
  // const activeOrganization = useSelector((state) => state.activeOrganization.activeOrganization);

  const storedToken = window.localStorage.getItem(
    authConfig.storageTokenKeyName
  );

  const [userErrors, setUserErrors] = useState({});
  const [agentErrors, setAgentErrors] = useState({});
  const [dataUseErrors, setDataUseErrors] = useState({});

  // Form data states
  const [userData, setUserData] = useState(defaultUserInformation);
  const [dataPodData, setDataPodData] = useState(defaultDataPodInformation);
  const [usePoliciesData, setUsePoliciesData] = useState(defaultDataUse);

  // console.log("ACTIVE ORGANIZATION", activeOrganization);
  // console.log("ACTIVE DATA POD", activeDataPod);
  // console.log("DATA POD ID id id id id ", dataPodId);
  // console.log("DATA POD POLICIES", dataPodPolicies);
  // console.log("USER DATA PODS", userDataPods);
  // console.log("USER ORGANIZATIONS", userOrganizations);
  // console.log("USER DATA", userData);
  // console.log("DATA POD DATA", dataPodData);
  // console.log("USE POLICIES DATA", usePoliciesData);

  useEffect(() => {
    if (vcOfferSessionId) {
      handleVcOfferFlow();
    }
  }, [vcOfferSessionId]);

  useEffect(() => {
    if (Object.keys(activeOrganization).length !== 0) {
      const userInfo = converter.toUserInformation(activeOrganization);
      setUserData(userInfo);
    } else {
      // new organization
      setUserData(prevData => ({
        ...defaultUserInformation,
        organizationName: prevData.organizationName,
      }));
    }
  }, [activeOrganization]);

  useEffect(() => {
    if (dataPodPolicies && dataPodPolicies.length > 0) {
      const agentPolicies = converter.toAgentPolicies(dataPodPolicies);
      setDataPodData((prevAgentData) => ({
        ...agentPolicies,
      }));

      const usePolicies = converter.toUsePolicies(dataPodPolicies);
      setUsePoliciesData((prevUsePoliciesData) => ({
        ...usePolicies,
      }));
    } else {      
      setDataPodData(prevData => ({
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
    const offer = await organizationService.getVcOfferUrl(storedToken, organizationId, router.asPath);
    return offer.data.credentialVerificationUrl;
  }

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
    let organizationDid = ssiService.getVerifiedVcSignaturePolicy(offeredVP.data).sub;
    let vcCredentialSubject = ssiService.getVerifiedVcCredentialSubject(offeredVP.data);
    console.log("VC CredentialSubject: ", ssiService.getVerifiedVcCredentialSubject(offeredVP.data))
    console.log("organizationDid", organizationDid);



    setUserData(prevData => (
        {
          ...prevData,
          organizationVpJwt: offeredVP.data.tokenResponse.vp_token,
          organizationDid: organizationDid
        }));
  }

  const onSubmit = async () => {
    setActiveStep((prevActiveStep) => prevActiveStep + 1);
    if (activeStep === steps.length - 1) {
      try {
        const organizationDTO = converter.toOrganizationDTO(
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
          const dataPodDTO = converter.toDataPodDTO(
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
            activeDataPod={activeDataPod}
            activeOrganization={activeOrganization}
            getVcOfferUrl={getVcOfferUrl}
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
            dataPodData={dataPodData}
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
