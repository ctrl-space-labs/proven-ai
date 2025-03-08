import MuiBox from '@mui/material/Box'
import { alpha, styled, useTheme } from '@mui/material/styles'

// ** Custom Icon Import
import Icon from 'src/views/custom-components/mui/icon/icon'

// Styled component for the step dot
const ProgressStepDotRoot = styled('div')(({ theme, ownerState }) => ({
  width: 20,
  height: 20,
  borderRadius: '50%',
  borderStyle: 'solid',
  display: 'flex',
  justifyContent: 'center',
  alignItems: 'center',
  borderColor: ownerState.error
    ? 'transparent' // Error state: no border
    : ownerState.completed
      ? 'transparent' // Success state: no border
      : ownerState.active
        ? theme.palette.primary.main // Current tab: primary border w/out opacity
        : alpha(theme.palette.primary.main, 0.3), // Inactive: primary border w/ opacity

  backgroundColor: 'transparent'
}));

const ProgressStepIcon = (props) => {
  const { active, completed, error, className } = props;
  const theme = useTheme();

  const renderIcon = () => {
    if (error) {
      return (
        <Icon
          icon="mdi:alert"
          style={{
            color: theme.palette.error.main,
            transform: 'scale(1.7)',
          }}
        />
      );
    }
    if (completed) {
      return (
        <Icon
          icon="mdi:check-circle"
          style={{
            color: theme.palette.primary.main,
            transform: 'scale(1.7)',
          }}
        />
      );
    }
    return null;
  };

  return (
    <ProgressStepDotRoot
      ownerState={{ active, completed, error }}
      className={className}
    >
      {renderIcon()}
    </ProgressStepDotRoot>
  );
};

export default ProgressStepIcon
