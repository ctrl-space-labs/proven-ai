// src/views/provenAI/home/ProvenAIHome.js
import React, { useEffect } from "react";
import { useRouter } from "next/router";
import {localStorageConstants} from "src/utils/generalConstants";
import { useDispatch } from "react-redux";
import { Card, Box, Grid } from "@mui/material";
import StatisticsCart from "src/views/pages/home/statistics/StatisticsCard";
import AnalyticsFilterForm from "src/views/pages/home/statistics/AnalyticsFilterForm";
import { fetchAnalytics } from "src/store/permissionOfUseAnalytics/permissionOfUseAnalytics";
import Typography from "@mui/material/Typography";
import useRedirectOr404ForHome from "src/utils/useRedirectOr404ForHome";
import { useAuth } from "src/authentication/useAuth";
import {ResponsiveCardContent} from "src/utils/responsiveCardContent";


const ProvenAIHome = () => {
  const auth = useAuth();
  const router = useRouter();
  const dispatch = useDispatch();
  const { organizationId } = router.query;
  const token = window.localStorage.getItem(
    localStorageConstants.accessTokenKey
  );
  useRedirectOr404ForHome(organizationId);


  useEffect(() => {
    dispatch(
      fetchAnalytics({
        organizationId,
        token,
      })
    );
  }, [organizationId, token, dispatch]);

  return (
    <Card sx={{ backgroundColor: "transparent", boxShadow: "none" }}>
      <ResponsiveCardContent sx={{ backgroundColor: "background.paper" }}>
        <Grid container spacing={2} alignItems={"center"}>
          <Grid item xs={12} md={7} sm={12}>
            <Typography
              variant="h3"
              sx={{ fontWeight: 600, textAlign: "left" }}
            >
              Analytics
            </Typography>
          </Grid>
          <Grid item xs={12} md={5} sm={12}>
            <AnalyticsFilterForm
              organizationId={organizationId}
              token={token}
            />
          </Grid>
        </Grid>
      </ResponsiveCardContent>
      <Box sx={{ height: 20 }} />
      <StatisticsCart />
    </Card>
  );
};

export default ProvenAIHome;
