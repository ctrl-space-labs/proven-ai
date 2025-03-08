import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";

export const fetchProject = createAsyncThunk(
  "activeProject/fetchProject",
  async ({ organizationId, projectId, token }, thunkAPI) => {
    try {
      const projectPromise = projectService.getProjectById(organizationId, projectId, token);
      const membersPromise = projectService.getProjectMembers(organizationId, projectId, token);

      // Use Promise.all to wait for both promises to resolve
      const [projectData, membersData] = await Promise.all([projectPromise, membersPromise]);

      // Return combined data
      return { project: projectData.data, members: membersData.data };
    } catch (error) {
      return thunkAPI.rejectWithValue(error.response.data);
    }
  }
);


// Define the initial state
const initialActiveProjectState = {
  projectDetails: {},
  projectMembers: [],
  error: null,
};

// Create the slice
const activeProjectSlice = createSlice({
  name: "activeProject",
  initialState: initialActiveProjectState,
  reducers: {
    // standard reducer logic, with auto-generated action types per reducer
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchProject.pending, (state) => {
        state.error = null;
      })
      .addCase(fetchProject.fulfilled, (state, action) => {
        state.projectDetails = action.payload.project;
        state.projectMembers = action.payload.members;
      })
      .addCase(fetchProject.rejected, (state, action) => {
        state.error = action.payload;
      });
  },
});

export const activeProjectActions = activeProjectSlice.actions;
export default activeProjectSlice.reducer;
