import {alpha} from "@mui/material/styles";

const stepperOverrides = (theme) => ({
  MuiStepper: {
    styleOverrides: {
      // Override the root of the Stepper and style its descendants:
      root: {
        // Styles for the step elements
        '& .MuiStep-root': {
          // Styles applied to a descendant with a custom class "step-label"
          '& .step-label': {
            display: 'flex',
            alignItems: 'center'
          },
          // Styles applied to a descendant with the custom class "step-title"
          '& .step-title': {
            color: theme.palette.text.primary,
            fontWeight: 600,
            fontSize: '1.5rem'
          },
          // When an error occurs, override the color of several nested elements
          '& .Mui-error': {
            '& .MuiStepLabel-labelContainer, & .step-number, & .step-title, & .step-subtitle': {
              color: theme.palette.error.main
            }
          }
        },
        // Styles for the Step Connector inside the Stepper
        '& .MuiStepConnector-root': {
          // When active or completed, change the connector line color
          '&.Mui-active, &.Mui-completed': {
            '& .MuiStepConnector-line': {
              borderColor: theme.palette.primary.main
            }
          },
          // When disabled, use an opacity-adjusted primary color
          '&.Mui-disabled .MuiStepConnector-line': {
            borderColor: alpha(theme.palette.primary.main, 0.5)
          },
          // Default styling for the connector line
          '& .MuiStepConnector-line': {
            borderWidth: 3,
            borderRadius: 3
          }
        },
        // Responsive behavior for horizontal steppers on medium or smaller screens:
        [theme.breakpoints.down('md')]: {
          '&.MuiStepper-horizontal': {
            flexDirection: 'column',
            alignItems: 'flex-start'
          }
        }
      }
    }
  }
});

export default stepperOverrides;
