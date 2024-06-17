import React, { useEffect, useState } from "react";
import { useRouter } from "next/router";
import {useDispatch, useSelector} from "react-redux";
import { useAuth } from "src/hooks/useAuth";
import authConfig from "src/configs/auth";

// MUI components
import { styled } from "@mui/material/styles";
import Typography from "@mui/material/Typography";
import CardContent from "@mui/material/CardContent";
import Card from "@mui/material/Card";
import Box from "@mui/material/Box";
import FormControl from "@mui/material/FormControl";
import InputLabel from "@mui/material/InputLabel";
import Select from "@mui/material/Select";
import MenuItem from "@mui/material/MenuItem";

// Custom components
import useRedirectOr404ForHome from "src/utils/useRedirectOr404ForHome";
import StatisticsCart from "src/views/provenAI/home/statistics/StatisticsCard";
import {fetchAnalytics} from "../../../store/apps/permissionOfUseAnalytics/permissionOfUseAnalytics";

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

  const dispatch = useDispatch();


  const auth = useAuth();
  const [selectedPeriod, setSelectedPeriod] = useState(["Last 24 Hours"]);

  const project = useSelector((state) => state.activeProject.projectDetails);
  useRedirectOr404ForHome(organizationId);

  const storedToken = window.localStorage.getItem(
    authConfig.storageTokenKeyName
  );

  useEffect(() => {
    dispatch(
        fetchAnalytics({
          organizationId: 'c83a1c61-4c79-4c49-8b3e-249e8c40a39f',
          storedToken,
        })
    );
  },[])


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
          </Box>
        </Box>
      </StyledCardContent>
      <Box sx={{ height: 20 }} />
      
        <StatisticsCart />
      
      
    </Card>
  );
};

export default ProvenAIHome;
