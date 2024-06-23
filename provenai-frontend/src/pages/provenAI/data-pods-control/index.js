import React, { useEffect, useState } from "react";
import { useRouter } from "next/router";
import { useDispatch, useSelector } from "react-redux";
import { useAuth } from "src/hooks/useAuth";
import authConfig from "src/configs/auth";

// MUI components
import { styled } from "@mui/material/styles";
import Typography from "@mui/material/Typography";
import CardContent from "@mui/material/CardContent";
import Card from "@mui/material/Card";
import Box from "@mui/material/Box";
import organizationService from "src/provenAI-sdk/organizationService";
import dataPodsService from "src/provenAI-sdk/dataPodsService";
import DataPodStepperLinearWithValidation from "src/views/provenAI/data-pods-control/DataPodStepperLinearWithValidation";


const StyledCardContent = styled(CardContent)(({ theme }) => ({
  paddingTop: `${theme.spacing(10)} !important`,
  paddingBottom: `${theme.spacing(8)} !important`,
  [theme.breakpoints.up("sm")]: {
    paddingLeft: `${theme.spacing(20)} !important`,
    paddingRight: `${theme.spacing(20)} !important`,
  },
}));

const DataPodsControl = () => {
  const router = useRouter();
  const state = useSelector((state) => state);
  const dispatch = useDispatch();
  const { organizationId, dataPodId, vcOfferSessionId } = router.query;
  const auth = useAuth();
  const userOrganizations = auth?.user?.organizations;
  const [userDataPods, setUserDataPods] = useState([]);
  const [activeOrganization, setActiveOrganization] = useState({});
  const [activeDataPod, setActiveDataPod] = useState({});
  const [dataPodPolicies, setDataPodPolicies] = useState({});
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

    if (!dataPodId) {
      setActiveDataPod({});
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
          setActiveDataPod({});
          setDataPodPolicies({});
        }
      }

    const activeOrgProjects = auth.user.organizations.find(
      (org) => org.id === organizationId
    )?.projects;

    if (activeOrgProjects) {
      setUserDataPods(activeOrgProjects);
    }
    };

    const fetchDataPod = async () => {
      try {
        const dataPod = await dataPodsService.getDataPodById(
          dataPodId,
          storedToken
        );
        setActiveDataPod(dataPod.data);
      } catch (error) {
        console.error("Error fetching data pod:", error);
      }
    };

    const fetchDataPodPolicies = async () => {
      try {
        const dataPodPolicies = await dataPodsService.getAclPoliciesByDataPod(
          dataPodId,
          storedToken
        );
        setDataPodPolicies(dataPodPolicies.data.content);
      } catch (error) {
        console.error("Error fetching data pod:", error);
      }
    };

    if (organizationId) {
      fetchOrganization();      
    }

    if (dataPodId) {
      fetchDataPod();
      fetchDataPodPolicies();
    }
  }, [storedToken, organizationId, dataPodId]);

  return (
    <Card sx={{ backgroundColor: "transparent", boxShadow: "none" }}>
      <StyledCardContent sx={{ backgroundColor: "background.paper" }}>
        <Box
          sx={{
            justifyContent: "space-between",
            alignItems: "center",
          }}
        >
          <Typography variant="h3" sx={{ fontWeight: 600, textAlign: "left" }}>
            Data Access Control Policies
          </Typography>
        </Box>
      </StyledCardContent>
      <Box sx={{ height: 20 }} />

      <DataPodStepperLinearWithValidation
        userOrganizations={userOrganizations}
        userDataPods={userDataPods}
        activeOrganization={activeOrganization}
        activeDataPod={activeDataPod}
        dataPodPolicies={dataPodPolicies}
        organizationId={organizationId}
        dataPodId={dataPodId}
        activeStep={activeStep}
        setActiveStep={setActiveStep}
        vcOfferSessionId={vcOfferSessionId}
      />
    </Card>
  );
};

export default DataPodsControl;
