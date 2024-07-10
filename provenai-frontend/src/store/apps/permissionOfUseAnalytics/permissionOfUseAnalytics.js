import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import permissionOfUseAnalyticsService from "src/provenAI-sdk/permissionOfUseAnalyticsService";

const aggregateDataByKey = (data, key) => {
  return data.reduce((acc, item) => {
    const keyValue = item[key] || "unknown";
    if (!acc[keyValue]) {
      acc[keyValue] = {
        totalSumTokens: 0,
        items: [],
      };
    }
    acc[keyValue].totalSumTokens += item.sumTokens;
    acc[keyValue].items.push(item);
    return acc;
  }, {});
};

const tokensByOwnerDataPod = (data) => {
  return aggregateDataByKey(data, "ownerDatapodId");
};

const tokensByProcessorAgent = (data) => {
  return aggregateDataByKey(data, "processorAgentId");
};

const tokensByDateTimeBucket = (data) => {
  return aggregateDataByKey(data, "bucketStart");
};

/**
 * This thunk will update the graph data based on the provided and consumed data tokens
 * It will be called upon:
 * - permissionOfUseAnalytics/fetchAnalytics
 * - permissionOfUseAnalytics/updateFilters
 * @returns an object that for each graph contains
 *          e.g the dataPodID, for this dataPod how many tokens consumed in total,
 *          and the list of items took part in the calculation
 * @type {AsyncThunk<{consumedDataTokensByProcessorAgent: *, providedDataTokensByOwnerDataPod: *, providedDataTokensByDateTimeBucket: *, consumedDataTokensByOwnerDataPod: *, consumedDataTokensByDateTimeBucket: *, providedDataTokensByProcessorAgent: *}, void, AsyncThunkConfig>}
 */
export const updateGraphData = createAsyncThunk(
  "permissionOfUseAnalytics/updateGraphData",
  async ({ providedDataTokens, consumedDataTokens }, thunkAPI) => {
    // TODO take in to account the filters (link only data from datapods: [ 'A', 'B'])

    const providedDataTokensByOwnerDataPod =
      tokensByOwnerDataPod(providedDataTokens);
    const providedDataTokensByProcessorAgent =
      tokensByProcessorAgent(providedDataTokens);
    const providedDataTokensByDateTimeBucket =
      tokensByDateTimeBucket(providedDataTokens);

    const consumedDataTokensByOwnerDataPod =
      tokensByOwnerDataPod(consumedDataTokens);
    const consumedDataTokensByProcessorAgent =
      tokensByProcessorAgent(consumedDataTokens);
    const consumedDataTokensByDateTimeBucket =
      tokensByDateTimeBucket(consumedDataTokens);

    return {
      providedDataTokensByOwnerDataPod,
      providedDataTokensByProcessorAgent,
      providedDataTokensByDateTimeBucket,
      consumedDataTokensByOwnerDataPod,
      consumedDataTokensByProcessorAgent,
      consumedDataTokensByDateTimeBucket,
    };
  }
);

export const fetchAnalytics = createAsyncThunk(
  "permissionOfUseAnalytics/fetchAnalytics",
  async ({ organizationId, storedToken }, thunkAPI) => {
    try {
      const { dispatch, getState } = thunkAPI;

      // Get the state
      const state = getState();
      const { filters } = state.permissionOfUseAnalytics;

      const providedDataPromise =
        permissionOfUseAnalyticsService.getAnalyticsByCriteria(
          { ownerOrganizationId: organizationId, ...filters.apiFilters },
          storedToken
        );
      const consumedDataPromise =
        permissionOfUseAnalyticsService.getAnalyticsByCriteria(
          { processorOrganizationId: organizationId, ...filters.apiFilters },
          storedToken
        );

      // Use Promise.all to wait for both promises to resolve
      const [providedData, consumedData] = await Promise.all([
        providedDataPromise,
        consumedDataPromise,
      ]);
      let data = {
        providedDataTokens: providedData.data,
        consumedDataTokens: consumedData.data,
      };

      await dispatch(updateGraphData(data));

      // Return combined data
      return data;
    } catch (error) {
      return thunkAPI.rejectWithValue(error.response.data);
    }
  }
);

const now = new Date();
const oneMonthAgo = new Date(now.getTime());
oneMonthAgo.setMonth(now.getMonth() - 1);

// Define the initial state
const initialAnalyticsState = {
  providedDataTokens: [],
  consumedDataTokens: [],
  filters: {
    apiFilters: {
      from: oneMonthAgo.toISOString(),
      to: now.toISOString(),
      timeIntervalInSeconds: 60 * 60 * 24, // one record per day
    },
    selectedPeriod: 30 * 24 * 60 * 60, // Default to "Last 30 days"
    pageFilters: null,
  },
  graphData: null,
  error: null,
  loading: false,
};

// Create the slice
const permissionOfUseAnalyticsSlice = createSlice({
  name: "permissionOfUseAnalytics",
  initialState: initialAnalyticsState,
  reducers: {
    updateFilters: (state, action) => {
      state.filters = action.payload;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchAnalytics.pending, (state) => {
        state.error = null;
        state.loading = true;
      })
      .addCase(fetchAnalytics.fulfilled, (state, action) => {
        state.providedDataTokens = action.payload.providedDataTokens;
        state.consumedDataTokens = action.payload.consumedDataTokens;
        state.loading = false;
      })
      .addCase(fetchAnalytics.rejected, (state, action) => {
        state.error = action.payload;
        state.loading = false;
      })
      .addCase(updateGraphData.pending, (state, action) => {
        state.loading = true;
      })
      .addCase(updateGraphData.fulfilled, (state, action) => {
        state.graphData = action.payload;
        state.loading = false;
      })
      .addCase(updateGraphData.rejected, (state, action) => {
        state.error = action.payload;
        state.loading = false;
      });
  },
});

// Export actions and reducer
export const { updateFilters } = permissionOfUseAnalyticsSlice.actions;

export const permissionOfUseAnalyticsActions =
  permissionOfUseAnalyticsSlice.actions;
export default permissionOfUseAnalyticsSlice.reducer;
