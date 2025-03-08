import { Stack, CircularProgress } from '@mui/material';

const ProvenAiPageLoader = ({ sx }) => {
  return (
    <Stack
      sx={{ height: '100vh', ...sx }}
      direction="column"
      alignItems="center"
      justifyContent="center"
      spacing={6} // uses theme spacing (6 * 8px = 48px)
    >
      <img src="/images/provenAILogo.svg" alt="ProvenAI Logo" />
      <CircularProgress />
    </Stack>
  );
};

export default ProvenAiPageLoader;
