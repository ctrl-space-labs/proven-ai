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
      agentsIdInResponse.data.content,
      permissionOfUseAnalytics.graphData.providedDataTokensByProcessorAgent
    );

    const consumedByOwnerDataPods = toApexChartData(
      "dataPod",
      dataPodIdInResponse.data.content,
      permissionOfUseAnalytics.graphData.consumedDataTokensByOwnerDataPod
    );

    const consumedByProcessorAgents = toApexChartData(
      "agent",
      agentsByOrgResponse.data.content,
      permissionOfUseAnalytics.graphData.consumedDataTokensByProcessorAgent
    );

    const transformDateTimeBuckets = (buckets) => {
      return Object.keys(buckets).map((key) => ({
        date: key,
        ...buckets[key],
        updatedTotalTokens: buckets[key].totalSumTokens,
      }));
    };

    const providedByDateTimeBuckets = transformDateTimeBuckets(
      permissionOfUseAnalytics.graphData.providedDataTokensByDateTimeBucket ||
        {}
    );
    const consumedByDateTimeBuckets = transformDateTimeBuckets(
      permissionOfUseAnalytics.graphData.consumedDataTokensByDateTimeBucket ||
        {}
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
        providedByDateTimeBuckets,
        consumedByOwnerDataPods,
        consumedByProcessorAgents,
        consumedByDateTimeBuckets,
      },
    };
  }
);

const toApexChartData = (type, userData, graphData) => {
  if (!graphData || Object.keys(graphData).length === 0) {
    return [];
  }


  const result = userData
    .filter((data) => Object.keys(graphData).includes(data.id))
    .map((data) => ({
      id: data.id,
      name:
        type === "dataPod"
          ? data.podUniqueName
          : type === "agent"
          ? data.agentName
          : "",
      data: [graphData[data.id]?.totalSumTokens || 0],
      active: true,
      items: graphData[data.id]?.items || [],
    }));


  // Check if there are entries with 'unknown' key in graphData and add them to the result
  if (graphData["unknown"]) {
    result.push({
      id: "unknown",
      name: "Unknown Agent",
      data: [graphData["unknown"].totalSumTokens || 0],
      active: true,
      items: graphData["unknown"].items || [],
    });
  }
  return result;
};

const updateAnalyticsData = (state, providedData, key, relatedKey) => {
  state.analyticsData[key] = providedData;

  state.analyticsData[relatedKey] = state.analyticsData[relatedKey].map(
    (item) => {
      const allItems = item.items.map((subItem) => {
        const isActive = providedData.some((data) => {
          if (key.endsWith("ProcessorAgents")) {
            return (
              (data.id === subItem.processorAgentId ||
                (subItem.processorAgentId === null && data.id === "unknown")) &&
              data.active
            );
          } else if (key.endsWith("OwnerDataPods")) {
            return (
              (data.id === subItem.ownerDatapodId ||
                (subItem.ownerDatapodId === null && data.id === "unknown")) &&
              data.active
            );
          }
          return false;
        });

        return {
          ...subItem,
          active: isActive,
        };
      });

      const activeItems = allItems.filter((subItem) => subItem.active);
      const newTotalSumTokens = activeItems.reduce(
        (sum, subItem) => sum + subItem.sumTokens,
        0
      );

      return {
        ...item,
        items: allItems,
        data: [newTotalSumTokens],
        active: newTotalSumTokens > 0,
      };
    }
  );

  let dateTimeBucket;

  if (key.startsWith("provided")) {
    dateTimeBucket = "providedByDateTimeBuckets";
  } else if (key.startsWith("consumed")) {
    dateTimeBucket = "consumedByDateTimeBuckets";
  }

  state.analyticsData[dateTimeBucket] = state.analyticsData[dateTimeBucket].map(
    (bucket) => {
      const updatedTotalSumTokens = bucket.items.reduce((sum, item) => {
        let isItemActive = false;

        if (key.endsWith("ProcessorAgents")) {
          isItemActive = providedData.some(
            (data) =>
              (data.id === item.processorAgentId ||
                (item.processorAgentId === null && data.id === "unknown")) &&
              data.active
          );
        } else if (key.endsWith("OwnerDataPods")) {
          isItemActive = providedData.some(
            (data) =>
              (data.id === item.ownerDatapodId ||
                (item.ownerDatapodId === null && data.id === "unknown")) &&
              data.active
          );
        }

        if (isItemActive) {
          return sum + item.sumTokens;
        }
        return sum;
      }, 0);

      return {
        ...bucket,
        updatedTotalTokens: updatedTotalSumTokens,
      };
    }
  );
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
    updateProvidedByProcessorAgents: (state, action) => {
      updateAnalyticsData(
        state,
        action.payload,
        "providedByProcessorAgents",
        "providedByOwnerDataPods"
      );
    },
    updateConsumedByProcessorAgents: (state, action) => {
      updateAnalyticsData(
        state,
        action.payload,
        "consumedByProcessorAgents",
        "consumedByOwnerDataPods"
      );
    },
    updateProvidedByOwnerDataPods: (state, action) => {
      updateAnalyticsData(
        state,
        action.payload,
        "providedByOwnerDataPods",
        "providedByProcessorAgents"
      );
    },
    updateConsumedByOwnerDataPods: (state, action) => {
      updateAnalyticsData(
        state,
        action.payload,
        "consumedByOwnerDataPods",
        "consumedByProcessorAgents"
      );
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

export const {
  setOrganizationId,
  updateProvidedByProcessorAgents,
  updateConsumedByProcessorAgents,
  updateProvidedByOwnerDataPods,
  updateConsumedByOwnerDataPods,
} = userDataForAnalyticsSlice.actions;

export default userDataForAnalyticsSlice.reducer;
