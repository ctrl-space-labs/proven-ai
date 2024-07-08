import React, { useEffect, useState, forwardRef } from "react";
import { useRouter } from "next/router";
import { useDispatch, useSelector } from "react-redux";
import { useAuth } from "src/hooks/useAuth";
import authConfig from "src/configs/auth";
import DatePicker from "react-datepicker";

// MUI components
import { styled } from "@mui/material/styles";
import Typography from "@mui/material/Typography";
import CardContent from "@mui/material/CardContent";
import Card from "@mui/material/Card";
import TextField from "@mui/material/TextField";
import InputAdornment from "@mui/material/InputAdornment";
import Icon from "src/@core/components/icon";
import Box from "@mui/material/Box";
import FormControl from "@mui/material/FormControl";
import InputLabel from "@mui/material/InputLabel";
import Select from "@mui/material/Select";
import MenuItem from "@mui/material/MenuItem";
import Chip from "@mui/material/Chip";
import Button from "@mui/material/Button";


// Custom components
import useRedirectOr404ForHome from "src/utils/useRedirectOr404ForHome";
import agentService from "src/provenAI-sdk/agentService";
import StatisticsCart from "src/views/provenAI/home/statistics/StatisticsCard";
import { fetchAnalytics } from "../../../store/apps/permissionOfUseAnalytics/permissionOfUseAnalytics";
import { fetchUserDataForAnalytics } from "../../../store/apps/userDataForAnalytics/userDataForAnalytics";

const period = [
  "Last 24 Hours",
  "Last 7 Days",
  "Last 30 Days",
  "Last 90 Days",
  "Last 180 Days",
  "Last 365 Days",
  "Last 2 Years",
  "Last 5 Years",
  "All Time",
];

const StyledCardContent = styled(CardContent)(({ theme }) => ({
  paddingTop: `${theme.spacing(10)} !important`,
  paddingBottom: `${theme.spacing(8)} !important`,
  [theme.breakpoints.up("sm")]: {
    paddingLeft: `${theme.spacing(20)} !important`,
    paddingRight: `${theme.spacing(20)} !important`,
  },
}));

const ProvenAIHome = () => {
  const router = useRouter();
  const { organizationId } = router.query;
  const auth = useAuth();
  const storedToken = window.localStorage.getItem(
    authConfig.storageTokenKeyName
  );

  const dispatch = useDispatch();

  const [selectedPeriod, setSelectedPeriod] = useState(["Last 24 Hours"]);
  const [endDate, setEndDate] = useState(null);
  const [startDate, setStartDate] = useState(null);
  const [userDataPods, setUserDataPods] = useState([]);
  const [userAgents, setUserAgents] = useState([{}]);
  const [excludedPods, setExcludedPods] = useState([]);
  const [excludedAgents, setExcludedAgents] = useState([]);

  useEffect(() => {
    dispatch(
      fetchAnalytics({
        organizationId: organizationId,
        storedToken,
      })
    );    
    
  }, [organizationId, storedToken]);

  



 

  const CustomInput = forwardRef((props, ref) => {
    const startDate =
      props.start !== null ? format(props.start, "MM/dd/yyyy") : "";
    const endDate =
      props.end !== null ? ` - ${format(props.end, "MM/dd/yyyy")}` : null;
    const value = `${startDate}${endDate !== null ? endDate : ""}`;

    return (
      <TextField
        {...props}
        size="small"
        value={value}
        inputRef={ref}
        InputProps={{
          startAdornment: (
            <InputAdornment position="start">
              <Icon icon="mdi:bell-outline" />
            </InputAdornment>
          ),
          endAdornment: (
            <InputAdornment position="end">
              <Icon icon="mdi:chevron-down" />
            </InputAdornment>
          ),
        }}
      />
    );
  });

  const handleOnChange = (dates) => {
    const [start, end] = dates;
    setStartDate(start);
    setEndDate(end);
  };

  const handlePodClick = (pod) => {
    setExcludedPods((prev) =>
      prev.includes(pod.id)
        ? prev.filter((id) => id !== pod.id)
        : [...prev, pod.id]
    );
  };

  return (
    <Card sx={{ backgroundColor: "transparent", boxShadow: "none" }}>
      <StyledCardContent sx={{ backgroundColor: "background.paper" }}>
        <Box
          sx={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
          }}
        >
          <Typography
            variant="h3"
            sx={{ mb: 3, fontWeight: 600, textAlign: "left" }}
          >
            Analytics
          </Typography>

          <Box sx={{ display: "flex", alignItems: "center" }}>
            <FormControl fullWidth>
              <InputLabel id="period-name-label">Period</InputLabel>
              <Select
                label="period-name-label"
                value={selectedPeriod}
                onChange={(e) => setSelectedPeriod(e.target.value)}
                id="period-name-label"
                labelId="period-name-label"
              >
                {period.map((period) => (
                  <MenuItem key={period} value={period}>
                    {period}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>

            <DatePicker
              selectsRange
              endDate={endDate}
              // id='apexchart-area'
              selected={startDate}
              startDate={startDate}
              onChange={handleOnChange}
              placeholderText="Click to select a date"
              customInput={<CustomInput start={startDate} end={endDate} />}
            />
          </Box>
        </Box>
       
      </StyledCardContent>
      <Box sx={{ height: 20 }} />

      <StatisticsCart />
    </Card>
  );
};

export default ProvenAIHome;
