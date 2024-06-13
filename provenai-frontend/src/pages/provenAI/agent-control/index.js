import React, { useEffect, useState } from "react";
import { useRouter } from "next/router";
import { useSelector } from "react-redux";
import { useAuth } from "src/hooks/useAuth";
import authConfig from "src/configs/auth";

// MUI components
import { styled } from "@mui/material/styles";
import Typography from "@mui/material/Typography";
import CardContent from "@mui/material/CardContent";
import Card from "@mui/material/Card";
import Box from "@mui/material/Box";


// Custom components
import useRedirectOr404ForHome from "src/utils/useRedirectOr404ForHome";

import AgentStepperLinearWithValidation from "src/views/provenAI/agent-control/AgentStepperLinearWithValidation";




const StyledCardContent = styled(CardContent)(({ theme }) => ({
  paddingTop: `${theme.spacing(10)} !important`,
  paddingBottom: `${theme.spacing(8)} !important`,
  [theme.breakpoints.up("sm")]: {
    paddingLeft: `${theme.spacing(20)} !important`,
    paddingRight: `${theme.spacing(20)} !important`,
  },
}));

const AgentControl = () => {
  const router = useRouter();
  const { organizationId} = router.query;
  const auth = useAuth();
  

 
  useRedirectOr404ForHome(organizationId);

  const storedToken = window.localStorage.getItem(
    authConfig.storageTokenKeyName
  );

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
            Agent Control Policies
          </Typography>

          
        </Box>
      </StyledCardContent>
      <Box sx={{ height: 20 }} />
      
        <AgentStepperLinearWithValidation />
      
      
    </Card>
  );
};

export default AgentControl;
