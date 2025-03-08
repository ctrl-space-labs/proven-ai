import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import dataPodsService from "src/provenAI-sdk/dataPodsService";


export const fetchDataPodById = createAsyncThunk(
  "dataPods/fetchById",
  async ({dataPodId, token}, { rejectWithValue }) => {
    try {
      console.log("dataPodId", dataPodId);
      const response = await dataPodsService.getDataPodById(dataPodId, token);
      return response.data;
    } catch (error) {
      return rejectWithValue(error.response ? error.response.data : error.message);
    }
  }
);

export const fetchDataPodPolicies = createAsyncThunk(
  "dataPods/fetchPolicies",
  async ({dataPodId, token}, { rejectWithValue }) => {
    try {
      const response = await dataPodsService.getAclPoliciesByDataPod(dataPodId, token);
      return response.data.content;
    } catch (error) {
      return rejectWithValue(error.response ? error.response.data : error.message);
    }
  }
);

const dataPodsSlice = createSlice({
  name: "dataPods",
  initialState: {
    activeDataPod: {},
    dataPodPolicies: [],
    loading: false,
    error: null,
  },
  reducers: {
    clearDataPodState: (state) => {
      state.activeDataPod = {};
      state.dataPodPolicies = [];
      state.loading = false;
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchDataPodById.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchDataPodById.fulfilled, (state, action) => {
        state.activeDataPod = action.payload;
        state.loading = false;
      })
      .addCase(fetchDataPodById.rejected, (state, action) => {
        state.error = action.payload;
        state.loading = false;
      })
      .addCase(fetchDataPodPolicies.fulfilled, (state, action) => {
        state.dataPodPolicies = action.payload;
      });
  },
});

export const { clearDataPodState } = dataPodsSlice.actions;

export default dataPodsSlice.reducer;
