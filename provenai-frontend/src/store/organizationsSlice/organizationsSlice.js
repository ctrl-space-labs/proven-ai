import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import organizationService from "src/provenAI-sdk/organizationService";


export const fetchOrganizationById = createAsyncThunk(
  "organizations/fetchById",
  async ({organizationId, token}, { rejectWithValue }) => {
    try {
      const response = await organizationService.getProvenOrganizationById(organizationId, token);
      return response.data;
    } catch (error) {
      return rejectWithValue(error.response ? error.response.data : error.message);
    }
  }
);

const organizationsSlice = createSlice({
  name: "organizations",
  initialState: {
    activeOrganization: {},
    userOrganizations: [],
    loading: false,
    error: null,
  },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchOrganizationById.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(fetchOrganizationById.fulfilled, (state, action) => {
        state.activeOrganization = {
          ...action.payload,
          selectedOrganizationType: action.payload.selectedOrganizationType || '',
          organizationName: action.payload.organizationName || '',
        };
        state.loading = false;
      })
      .addCase(fetchOrganizationById.rejected, (state, action) => {
        state.error = action.payload;
        state.loading = false;
      });
  },
});

export default organizationsSlice.reducer;
