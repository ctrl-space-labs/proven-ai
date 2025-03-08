import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import agentService from "src/provenAI-sdk/agentService";

/**
 * Async thunk to fetch an agent by its ID
 */
export const fetchAgentById = createAsyncThunk(
  "agents/fetchById",
  async ({ agentId, token }, { rejectWithValue }) => {
    try {
      const response = await agentService.getAgentById(agentId, token);
      return response.data;
    } catch (error) {
      return rejectWithValue(error.response ? error.response.data : error.message);
    }
  }
);

/**
 * Async thunk to fetch agent policies
 */
export const fetchAgentPolicies = createAsyncThunk(
  "agents/fetchPolicies",
  async ({ agentId, token }, { rejectWithValue }) => {
    try {
      const response = await agentService.getPoliciesByAgent(agentId, token);
      return response.data.content;
    } catch (error) {
      return rejectWithValue(error.response ? error.response.data : error.message);
    }
  }
);

/**
 * Agent slice to manage agent-related state
 */
const agentSlice = createSlice({
  name: "agents",
  initialState: {
    activeAgent: {},
    agentPolicies: [],
    loading: false,
    error: null,
  },
  reducers: {
    clearAgentState: (state) => {
      state.activeAgent = {};
      state.agentPolicies = [];
      state.loading = false;
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchAgentById.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchAgentById.fulfilled, (state, action) => {
        state.activeAgent = action.payload;
        state.loading = false;
      })
      .addCase(fetchAgentById.rejected, (state, action) => {
        state.error = action.payload;
        state.loading = false;
      })
      .addCase(fetchAgentPolicies.fulfilled, (state, action) => {
        state.agentPolicies = action.payload;
      });
  },
});

export const { clearAgentState } = agentSlice.actions;
export default agentSlice.reducer;
