// src/store/apps/organization/organizationSlice.js

import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import agentService from "src/provenAI-sdk/agentService";
import dataPodsService from "src/provenAI-sdk/dataPodsService";

export const fetchUserDataForAnalytics = createAsyncThunk(
  "userDataForAnalytics/fetchUserDataForAnalytics",
  async ({
    organizationId,
    token,
    agentIdIn,
    dataPodIdIn,
    permissionOfUseAnalytics,
  }) => {
    const promises = [
      agentService.getAgentsByOrganization(organizationId, token),
      dataPodsService.getDataPodsByOrganization(organizationId, token),
    ];

    if (agentIdIn) {
      promises.push(agentService.getAgentsByIdIn(agentIdIn, token));
    } else {
      promises.push(Promise.resolve({ data: [] }));
    }

    if (dataPodIdIn) {
      promises.push(dataPodsService.getDataPodsByIdIn(dataPodIdIn, token));
    } else {
      promises.push(Promise.resolve({ data: [] }));
    }

    const [
      agentsByOrgResponse,
      dataPodsByOrgResponse,
      agentsIdInResponse,
      dataPodIdInResponse,
    ] = await Promise.all(promises);


    const providedByOwnerDataPods = toApexChartData(
      "dataPod",
      dataPodsByOrgResponse.data.content,
      permissionOfUseAnalytics.graphData.providedDataTokensByOwnerDataPod
    );
    
    const providedByProcessorAgents = toApexChartData(
      "agent",
      agentsByOrgResponse.data.content,
      permissionOfUseAnalytics.graphData.providedDataTokensByProcessorAgent
    );

    const consumedByOwnerDataPods = toApexChartData(
      "dataPod",
      dataPodIdInResponse.data.content,
      permissionOfUseAnalytics.graphData.consumedDataTokensByOwnerDataPod
    );

    const consumedByProcessorAgents = toApexChartData(
      "agent",
      agentsIdInResponse.data.content,
      permissionOfUseAnalytics.graphData.consumedDataTokensByProcessorAgent
    );

    return {
      userData: {
        agentsByOrgId: agentsByOrgResponse.data,
        dataPodsByOrgId: dataPodsByOrgResponse.data,
        agentsIdIn: agentsIdInResponse.data,
        dataPodIdIn: dataPodIdInResponse.data,
      },
      analyticsData: {
        providedByOwnerDataPods,
        providedByProcessorAgents,
        consumedByOwnerDataPods,
        consumedByProcessorAgents,
      }
    };
  }
);

const toApexChartData = (type, userData, graphData) => {
  return userData
    .filter((data) => Object.keys(graphData).includes(data.id))
    .map((data) => ({
      id: data.id,
      name: type === 'dataPod' ? data.podUniqueName : type === 'agent' ? data.agentName : "",
      data: [graphData[data.id]?.totalSumTokens || 0],
      active: true,
    }));
};

const userDataForAnalyticsSlice = createSlice({
  name: "userDataForAnalytics",
  initialState: {
    userData: {
      organizationId: null,
      agentsByOrgId: [],
      dataPodsByOrgId: [],
      agentsIdIn: [],
      dataPodIdIn: [],
    },
    analyticsData: {
      providedByOwnerDataPods: [],
      providedByProcessorAgents: [],
      providedByDateTimeBuckets: [],
      consumedByOwnerDataPods: [],
      consumedByProcessorAgents: [],
      consumedByDateTimeBuckets: [],
    },
    status: "idle",
    error: null,
    
  },
  reducers: {
    setOrganizationId: (state, action) => {
      state.organizationId = action.payload;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchUserDataForAnalytics.pending, (state) => {
        state.status = "loading";
      })
      .addCase(fetchUserDataForAnalytics.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.userData = action.payload.userData;
        state.analyticsData = action.payload.analyticsData;
        
      })
      .addCase(fetchUserDataForAnalytics.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.error.message;
      });
  },
});

export const { setOrganizationId } = userDataForAnalyticsSlice.actions;

export default userDataForAnalyticsSlice.reducer;
