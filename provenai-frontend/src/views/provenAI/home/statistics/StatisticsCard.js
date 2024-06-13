import React, { useState, useEffect } from "react";
import { formatDistanceToNow, parseISO } from "date-fns";
import ApexChartWrapper from 'src/@core/styles/libs/react-apexcharts'
import DataPodsStats from "./card-widgets/DataPodsStats";
import WeeklyOverview from "./card-widgets/WeeklyOverview";
import AgentWordStatistics from "./card-widgets/AgentWordStatistics";
import AgentVisitStatistics from "./card-widgets/AgentVisitStatistics";
import Sales from "./card-widgets/Sales";
import MonthlyBudget from "./card-widgets/MonthlyBudget";



// ** MUI Imports
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import Grid from "@mui/material/Grid";
import { styled } from "@mui/material/styles";
import { Button } from "@mui/material";
import Divider from "@mui/material/Divider";

// ** Next Import
import Link from "next/link";

// ** Icon Imports
import Icon from "src/@core/components/icon";
import CustomAvatar from "src/@core/components/mui/avatar";

import { useSelector, useDispatch } from "react-redux";
import { useRouter } from "next/router";
import { formatDocumentTitle } from "src/utils/documentUtils";
import authConfig from "src/configs/auth";

const Documents = ({ documents }) => {
  const router = useRouter();
  const dispatch = useDispatch();

  return (
    <Grid container spacing={6}>
      <Grid item xs={12} style={{ textAlign: "center" }}>
        <ApexChartWrapper>
          <Grid container spacing={6}>
            <Grid item xs={12} md={8}>
              <DataPodsStats />
            </Grid>
            <Grid item xs={12} sm={6} md={4}>
              <WeeklyOverview />
            </Grid>
            <Grid item xs={12} sm={6} md={4}>
              <AgentWordStatistics />
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

export default Documents;
