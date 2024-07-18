// src/views/provenAI/home/ProvenAIHome.js
import React, { useEffect } from "react";
import { useRouter } from "next/router";
import { styled } from "@mui/material/styles";
import authConfig from "src/configs/auth";
import { useDispatch } from "react-redux";
import { Card, Box, Grid } from "@mui/material";
import StatisticsCart from "src/views/provenAI/home/statistics/StatisticsCard";
import AnalyticsFilterForm from "src/views/provenAI/home/statistics/AnalyticsFilterForm";
import { fetchAnalytics } from "src/store/apps/permissionOfUseAnalytics/permissionOfUseAnalytics";
import Typography from "@mui/material/Typography";
import CardContent from "@mui/material/CardContent";
import useRedirectOr404ForHome from "src/utils/useRedirectOr404ForHome";

const StyledCardContent = styled(CardContent)(({ theme }) => ({
  paddingTop: `${theme.spacing(10)} !important`,
  paddingBottom: `${theme.spacing(8)} !important`,
  [theme.breakpoints.up("sm")]: {
    paddingLeft: `${theme.spacing(10)} !important`,
    paddingRight: `${theme.spacing(10)} !important`,
  },
}));

const ProvenAIHome = () => {
  const router = useRouter();
  const dispatch = useDispatch();
  const { organizationId } = router.query;
  const storedToken = window.localStorage.getItem(
    authConfig.storageTokenKeyName
  );
  useRedirectOr404ForHome(organizationId);

  useEffect(() => {
    dispatch(
      fetchAnalytics({
        organizationId,
        storedToken,
      })
    );
  }, [organizationId, storedToken, dispatch]);

  return (
    <Card sx={{ backgroundColor: "transparent", boxShadow: "none" }}>
      <StyledCardContent sx={{ backgroundColor: "background.paper" }}>
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
              storedToken={storedToken}
            />
          </Grid>
        </Grid>
      </StyledCardContent>
      <Box sx={{ height: 20 }} />
      <StatisticsCart />
    </Card>
  );
};

export default ProvenAIHome;
