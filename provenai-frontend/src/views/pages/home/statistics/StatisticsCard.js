import React, { useState, useEffect } from "react";
import { useAuth } from "src/authentication/useAuth";
import ApexChartWrapper from "src/@core/styles/libs/react-apexcharts";
import ProvidedByOwnerDataPodsStats from "./ProvidedByOwnerDataPodStats";
import ConsumedByOwnerDataPodsStats from "./ConsumedByOwnerDataPodStats";
import ProvidedByDateTimeBucket from "./ProvidedByDateTimeBucket";
import ProvidedByProcessorAgent from "./ProvidedByProcessorAgent";
import ConsumedByProcessorAgent from "./ConsumedByProcessorAgent";
import ConsumedByDateTimeBucket from "./ConsumedByDateTimeBucker";
import Grid from "@mui/material/Grid";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";
import { useSelector, useDispatch } from "react-redux";
import { fetchUserDataForAnalytics } from "src/store/userDataForAnalytics/userDataForAnalytics";
import { useRouter } from "next/router";
import {localStorageConstants} from "src/utils/generalConstants";

const StatisticsCard = () => {
  const router = useRouter();
  const { organizationId } = router.query;
  const auth = useAuth();
  const token = window.localStorage.getItem(
    localStorageConstants.accessTokenKey
  );
  const dispatch = useDispatch();

  const permissionOfUseAnalytics = useSelector(
    (state) => state.permissionOfUseAnalytics
  );

  useEffect(() => {
    if (!permissionOfUseAnalytics) {
      return;
    }

    const {
      consumedDataTokensByOwnerDataPod,
      providedDataTokensByProcessorAgent,
    } = permissionOfUseAnalytics.graphData || {};
    const agentIdIn = Object.keys(
      providedDataTokensByProcessorAgent || {}
    ).filter((id) => id !== "unknown");
    const dataPodIdIn = Object.keys(
      consumedDataTokensByOwnerDataPod || {}
    ).filter((id) => id !== "unknown");
    const agentIdInStr = agentIdIn.join(",");
    const dataPodIdInStr = dataPodIdIn.join(",");

    dispatch(
      fetchUserDataForAnalytics({
        organizationId,
        token: token,
        agentIdIn: agentIdInStr,
        dataPodIdIn: dataPodIdInStr,
        permissionOfUseAnalytics: permissionOfUseAnalytics,
      })
    );
  }, [
    organizationId,
    token,
    permissionOfUseAnalytics.graphData,
    dispatch,
  ]);

  return (
    <Grid container spacing={6}>
      <Grid item xs={12} style={{ textAlign: "center" }}>
        <ApexChartWrapper>
          <Grid container spacing={6}>
            <Grid item xs={12}>
              <Card sx={{ backgroundColor: "action.hover"}}>
                <CardContent>
                  <Typography
                    variant="h4"
                    gutterBottom
                    sx={{ textAlign: "left", fontWeight: 600,  marginBottom: 6 }}
                  >
                    Provided Data
                  </Typography>
                  <Grid container spacing={6}>
                    <Grid item xs={12} md={8} >
                      <ProvidedByOwnerDataPodsStats />
                    </Grid>
                    <Grid item xs={12} sm={6} md={4}>
                      <ProvidedByProcessorAgent />
                    </Grid>
                    <Grid item xs={12} sm={6} md={12}>
                      <ProvidedByDateTimeBucket />
                    </Grid>
                  </Grid>
                </CardContent>
              </Card>
            </Grid>
            <Grid item xs={12}>
            <Card sx={{ backgroundColor: "action.hover"}}>
            <CardContent>
                  <Typography
                    variant="h4"
                    gutterBottom
                    sx={{ textAlign: "left", fontWeight: 600, marginBottom: 6 }}
                  >
                    Consumed Data
                  </Typography>
                  <Grid container spacing={6}>
                    <Grid item xs={12} md={8}>
                      <ConsumedByOwnerDataPodsStats />
                    </Grid>
                    <Grid item xs={12} sm={6} md={4}>
                      <ConsumedByProcessorAgent />
                    </Grid>
                    <Grid item xs={12} sm={6} md={12}>
                      <ConsumedByDateTimeBucket />
                    </Grid>
                  </Grid>
                </CardContent>
              </Card>
            </Grid>
          </Grid>
        </ApexChartWrapper>
      </Grid>
    </Grid>
  );
};

export default StatisticsCard;
