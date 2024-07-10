// src/components/AnalyticsFilterForm.js
import React, { useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { styled } from "@mui/material/styles";
import { useTheme } from "@mui/material/styles";
import Box from "@mui/material/Box";
import Grid from "@mui/material/Grid";
import Typography from "@mui/material/Typography";
import FormControl from "@mui/material/FormControl";

import InputLabel from "@mui/material/InputLabel";
import Select from "@mui/material/Select";
import MenuItem from "@mui/material/MenuItem";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import {
  fetchAnalytics,
  updateFilters,
} from "src/store/apps/permissionOfUseAnalytics/permissionOfUseAnalytics";

const periodOptions = [
  { label: "Last 1 Hour", value: 60 * 60 },
  { label: "Last 8 Hours", value: 8 * 60 * 60 },
  { label: "Last 24 Hours", value: 24 * 60 * 60 },
  { label: "Last 7 Days", value: 7 * 24 * 60 * 60 },
  { label: "Last 30 Days", value: 30 * 24 * 60 * 60 },
  { label: "Last 90 Days", value: 90 * 24 * 60 * 60 },
  { label: "Last 180 Days", value: 180 * 24 * 60 * 60 },
  { label: "Last 365 Days", value: 365 * 24 * 60 * 60 },
  { label: "Last 2 Years", value: 2 * 365 * 24 * 60 * 60 },
  { label: "Last 5 Years", value: 5 * 365 * 24 * 60 * 60 },
  { label: "All Time", value: Infinity },
];

const DEFAULT_PERIOD_INDEX = 4; // Index of "Last 30 Days"

const getDefaultDates = (periodInSeconds) => {
  const endDate = new Date();
  const startDate = isFinite(periodInSeconds)
    ? new Date(endDate.getTime() - periodInSeconds * 1000)
    : null;

  return {
    startDate: startDate ? startDate.toISOString().split("T")[0] : null,
    endDate: endDate.toISOString().split("T")[0],
  };
};

const CustomFormControl = styled(FormControl)(({ theme }) => ({
  minWidth: 150,
  marginRight: theme.spacing(2),
}));

const CustomButton = styled(Button)(({ theme }) => ({
  marginTop: theme.spacing(2),
  height: 40,
}));

const AnalyticsFilterForm = ({ organizationId, storedToken }) => {
  const theme = useTheme();
  const dispatch = useDispatch();
  const filters = useSelector(
    (state) => state.permissionOfUseAnalytics.filters
  );

  const { startDate: initialStartDate, endDate: initialEndDate } =
    getDefaultDates(periodOptions[DEFAULT_PERIOD_INDEX].value);

  const [selectedPeriod, setSelectedPeriod] = useState(
    periodOptions[DEFAULT_PERIOD_INDEX].value
  );
  const [customStartDate, setCustomStartDate] = useState(initialStartDate);
  const [customEndDate, setCustomEndDate] = useState(initialEndDate);

  const handlePeriodChange = (event) => {
    const selectedValue = event.target.value;
    setSelectedPeriod(selectedValue);

    const { startDate, endDate } = getDefaultDates(selectedValue);

    setCustomStartDate(startDate);
    setCustomEndDate(endDate);

    dispatch(
      updateFilters({
        ...filters,
        apiFilters: {
          ...filters.apiFilters,
          from: startDate ? new Date(startDate).toISOString() : undefined,
          to: new Date(endDate).toISOString(),
        },
      })
    );

    dispatch(
      fetchAnalytics({
        organizationId,
        storedToken,
      })
    );
  };

  const handleCustomDateChange = () => {
    if (customStartDate && customEndDate) {
      const startDate = new Date(customStartDate);
      const endDate = new Date(customEndDate);

      // Set the end date to the end of the selected day
      endDate.setHours(23, 59, 59, 999);
      dispatch(
        updateFilters({
          ...filters,
          apiFilters: {
            ...filters.apiFilters,
            from: startDate.toISOString(),
            to: endDate.toISOString(),
          },
        })
      );

      dispatch(
        fetchAnalytics({
          organizationId,
          storedToken,
        })
      );
    }
  };

  return (
    <Grid container spacing={2} alignItems="center">
      <Grid item xs={12} md={6}>
        <CustomFormControl fullWidth>
          <InputLabel id="period-name-label">Period</InputLabel>
          <Select
            labelId="period-name-label"
            value={selectedPeriod}
            onChange={handlePeriodChange}
            label="Period"
          >
            {periodOptions.map((option) => (
              <MenuItem key={option.label} value={option.value}>
                {option.label}
              </MenuItem>
            ))}
          </Select>
        </CustomFormControl>
      </Grid>
      <Grid item xs={12} md={6}>
      <Box
        sx={{
          border: '1px solid #08B68D',
          borderRadius: '8px',
          padding: 4,
        }}
      >
        <Grid container spacing={2} alignItems="center">
          <Grid item xs={12} sm={12} >
          <Box mb={4}>
            <TextField
              id="custom-start-date"
              label="Start Date"
              type="date"
              value={customStartDate || ""}
              onChange={(e) => setCustomStartDate(e.target.value)}
              InputLabelProps={{
                shrink: true,
              }}
              fullWidth
            />
          </Box>
          <Box mb={2}>
            <TextField
              id="custom-end-date"
              label="End Date"
              type="date"
              value={customEndDate || ""}
              onChange={(e) => setCustomEndDate(e.target.value)}
              InputLabelProps={{
                shrink: true,
              }}
              fullWidth
            />
          </Box>
            <CustomButton
              variant="contained"
              onClick={handleCustomDateChange}
              fullWidth
            >
              Apply
            </CustomButton>
          </Grid>
        </Grid>
        </Box>
      </Grid>
    </Grid>
  );
};

export default AnalyticsFilterForm;
