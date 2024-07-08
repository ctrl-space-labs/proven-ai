import React, { useState, useEffect } from "react";
import { useAuth } from "src/hooks/useAuth";
import ApexChartWrapper from "src/@core/styles/libs/react-apexcharts";
import ProvidedByOwnerDataPodsStats from "./card-widgets/ProvidedByOwnerDataPodStats";
import ConsumedByOwnerDataPodsStats from "./card-widgets/ConsumedByOwnerDataPodStats";
import ProvidedByDateTimeBucket from "./card-widgets/ProvidedByDateTimeBucket";
import ProvidedByProcessorAgent from "./card-widgets/ProvidedByProcessorAgent";
import ConsumedByProcessorAgent from "./card-widgets/ConsumedByProcessorAgent";
import AgentVisitStatistics from "./card-widgets/AgentVisitStatistics";
import ApexAreaChart from "./card-widgets/ApexAreaChart";
import ApexRadarChart from "./card-widgets/ApexRadarChart";
import Sales from "./card-widgets/Sales";
import MonthlyBudget from "./card-widgets/MonthlyBudget";
import Grid from "@mui/material/Grid";
import { useSelector, useDispatch } from "react-redux";
import { fetchUserDataForAnalytics } from "src/store/apps/userDataForAnalytics/userDataForAnalytics";
import { useRouter } from "next/router";
import authConfig from "src/configs/auth";



const StatisticsCard = () => {
  const router = useRouter();
  const { organizationId } = router.query;
  const auth = useAuth();
  const storedToken = window.localStorage.getItem(
    authConfig.storageTokenKeyName
  );
  const dispatch = useDispatch();

  const permissionOfUseAnalytics = useSelector(
    (state) => state.permissionOfUseAnalytics
  );

  useEffect(() => {
    if (!permissionOfUseAnalytics.graphData) {
      return;
    }
   
      const { consumedDataTokensByOwnerDataPod, consumedDataTokensByProcessorAgent } = permissionOfUseAnalytics.graphData || {};
      const agentIdIn = Object.keys(consumedDataTokensByProcessorAgent || {});
      const dataPodIdIn = Object.keys(consumedDataTokensByOwnerDataPod || {});
      const agentIdInStr = agentIdIn.join(",");
      const dataPodIdInStr = dataPodIdIn.join(",");
      
      
      dispatch(
        fetchUserDataForAnalytics({
          organizationId,
          token: storedToken,
          agentIdIn: agentIdInStr,
          dataPodIdIn: dataPodIdInStr,
          permissionOfUseAnalytics: permissionOfUseAnalytics,         
        })
      );   
  
    
  }, [ organizationId, storedToken, permissionOfUseAnalytics.graphData, dispatch]);

 

  

  return (
    <Grid container spacing={6}>
      <Grid item xs={12} style={{ textAlign: "center" }}>
        <ApexChartWrapper>
          <Grid container spacing={6}>

            
            
            <Grid item xs={12} sm={6} md={6}>
              <ApexAreaChart />
            </Grid>
            <Grid item xs={12} sm={6} md={6}>
              <ApexRadarChart />
            </Grid>

            <Grid item xs={12} md={8}>
              <ProvidedByOwnerDataPodsStats />
            </Grid>

            <Grid item xs={12} sm={6} md={4}>
              <ProvidedByProcessorAgent />
            </Grid>
            
            
            <Grid item xs={12} sm={6} md={12}>
              <ProvidedByDateTimeBucket />
            </Grid>
            <Grid item xs={12} md={8}>
              <ConsumedByOwnerDataPodsStats />
            </Grid>

            <Grid item xs={12} sm={6} md={4}>
              <ConsumedByProcessorAgent 
              />
            </Grid>
            
            <Grid item xs={12} sm={6} md={4}>
              <AgentVisitStatistics />
            </Grid>
            <Grid item xs={12} sm={6} md={4}>
              <Sales />
            </Grid>
            <Grid item xs={12} md={8}>
              <MonthlyBudget />
            </Grid>
          </Grid>
        </ApexChartWrapper>
      </Grid>
    </Grid>
  );
};

export default StatisticsCard;
