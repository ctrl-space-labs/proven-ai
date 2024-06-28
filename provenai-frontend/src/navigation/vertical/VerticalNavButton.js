import React from "react";
import Button from "@mui/material/Button";
import { useRouter } from "next/router";
import { styled } from "@mui/material/styles";
import Icon from 'src/@core/components/icon'
import Tooltip from "@mui/material/Tooltip";
import Box from "@mui/material/Box";
import Link from "next/link";


const StyledButton = styled(Button)(({ theme, variant }) => ({
  margin: theme.spacing(2), // Adds space around the button
  width: `calc(95% - ${theme.spacing(2)})`, // Almost full width, adjust the spacing as needed
  ...(variant === 'contained' && {
    padding: theme.spacing(4), // Makes the button larger if the variant is 'contained'
    fontSize: '1.2rem',
    '& .MuiButton-startIcon': {
      marginRight: theme.spacing(5), // Adds space between icon and title for 'contained' variant
    }
  })
}));


// Button component for New Data Pod
const NewDataPodButton = () => {
  const router = useRouter();
  const { organizationId }  = router.query;
  

  return (
    <Box mt={3} mb={3}> {/* Add margin top and bottom */}
      <Tooltip title="New DataPod">
        <Link href={`/provenAI/data-pods-control?organizationId=${organizationId}`} passHref>
          <StyledButton            
            variant="outlined"
            startIcon={<Icon icon="mdi:plus" />}
          >
            {/* New Data Pods */}
          </StyledButton>
        </Link>
      </Tooltip>
    </Box>
  );
};




// Button component for New Agent
const NewAgentButton = () => {
  const router = useRouter();
  const { organizationId }  = router.query;
  

  return (
    <Box mt={3} mb={3}> {/* Add margin top and bottom */}
      <Tooltip title="New Agent">
        <Link href={`/provenAI/agent-control?organizationId=${organizationId}`} passHref>
          <StyledButton            
            variant="outlined"
            startIcon={<Icon icon="mdi:plus" />}
          >
            {/* New Agent */}
          </StyledButton>
        </Link>
      </Tooltip>
    </Box>
  );
};


export default {  
  NewAgentButton,
  NewDataPodButton
};
