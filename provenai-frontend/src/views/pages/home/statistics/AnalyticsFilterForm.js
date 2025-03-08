import React, { useState, forwardRef } from "react";
import { useDispatch, useSelector } from "react-redux";
import { styled, useTheme } from "@mui/material/styles";
import Box from "@mui/material/Box";
import Grid from "@mui/material/Grid";
import FormControl from "@mui/material/FormControl";
import InputLabel from "@mui/material/InputLabel";
import Select from "@mui/material/Select";
import MenuItem from "@mui/material/MenuItem";
import TextField from "@mui/material/TextField";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import {
  fetchAnalytics,
  updateFilters,
} from "src/store/permissionOfUseAnalytics/permissionOfUseAnalytics";
import { useSettings } from "src/@core/hooks/useSettings";

const periodOptions = [
  // { label: "Last 1 Hour", value: 60 * 60 },
  { label: "Last 8 Hours", value: 8 * 60 * 60 },
  { label: "Last 24 Hours", value: 24 * 60 * 60 },
  { label: "Last 7 Days", value: 7 * 24 * 60 * 60 },
  { label: "Last 30 Days", value: 30 * 24 * 60 * 60 },
  { label: "Last 90 Days", value: 90 * 24 * 60 * 60 },
  { label: "Last 180 Days", value: 180 * 24 * 60 * 60 },
  { label: "Last 365 Days", value: 365 * 24 * 60 * 60 },
  { label: "Last 2 Years", value: 2 * 365 * 24 * 60 * 60 },
  { label: "Last 5 Years", value: 5 * 365 * 24 * 60 * 60 },
  // { label: "All Time", value: Infinity },
];

const DEFAULT_PERIOD_INDEX = 3; // Index of "Last 30 Days"

const getDefaultDates = (periodInSeconds) => {
  const endDate = new Date();
  const startDate = isFinite(periodInSeconds)
    ? new Date(endDate.getTime() - periodInSeconds * 1000)
    : null;

  return {
    startDate: startDate,
    endDate: endDate,
  };
};

const CustomInput = forwardRef(({ value, onClick, label }, ref) => (
  <TextField
    inputRef={ref}
    label={label || ""}
    value={value}
    onClick={onClick}
    readOnly
  />
));

const AnalyticsFilterForm = ({ organizationId, token }) => {
  const theme = useTheme();
  const { settings } = useSettings();
  const dispatch = useDispatch();
  const { direction } = theme;
  const filters = useSelector(
    (state) => state.permissionOfUseAnalytics.filters
  );

  const { startDate: initialStartDate, endDate: initialEndDate } =
    getDefaultDates(periodOptions[DEFAULT_PERIOD_INDEX].value);

  const [selectedPeriod, setSelectedPeriod] = useState(
    periodOptions[DEFAULT_PERIOD_INDEX].value
  );

  const [customStartDate, setCustomStartDate] = useState(
    new Date(initialStartDate)
  );
  const [customEndDate, setCustomEndDate] = useState(new Date(initialEndDate));
  const popperPlacement = direction === "ltr" ? "bottom-start" : "bottom-end";

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
        token,
      })
    );
  };

  const handleCustomDateChange = (dates) => {
    const [start, end] = dates;
    setCustomStartDate(start);
    setCustomEndDate(end);

    if (start && end) {
      const startDate = new Date(start);
      const endDate = new Date(end);

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
          token,
        })
      );
    }
  };

  return (
    <Grid container spacing={2} alignItems={"center"} justifyContent="flex-end">
      <Grid>
        <FormControl>
          <InputLabel id="period-name-label">Period</InputLabel>
          <Select
            id="period-name-label"
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
        </FormControl>

        <Box
          mt={4}
          sx={{
            display: "flex",
            justifyContent: "flex-start",
            flexWrap: "wrap",
          }}
        >
          <div>
            <DatePicker
              selectsRange
              monthsShown={2}
              endDate={customEndDate}
              selected={customStartDate}
              startDate={customStartDate}
              shouldCloseOnSelect={true}
              id="date-range-picker-months"
              onChange={handleCustomDateChange}
              popperPlacement={popperPlacement}
              customInput={<CustomInput label="Date Range" />}
              dateFormat="dd/MM/yyyy"
            />
          </div>
        </Box>
      </Grid>
    </Grid>
  );
};

export default AnalyticsFilterForm;
