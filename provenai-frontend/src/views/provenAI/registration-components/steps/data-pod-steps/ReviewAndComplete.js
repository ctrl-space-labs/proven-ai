// ** React Imports
import React from "react";
import { useRef, useEffect } from "react";
import { Box, Grid, Typography, Button, Chip } from "@mui/material";
import { useTheme } from "@mui/material/styles";
import { useRouter } from "next/router";

const ReviewAndComplete = ({
  onSubmit,
  handleBack,
  userData,
  dataPodData,
  usePoliciesData,
  setActiveStep,
}) => {
  const theme = useTheme();
  const router = useRouter();
  const isFirstRender = useRef(true);

  useEffect(() => {
    if (isFirstRender.current) {
      isFirstRender.current = false;
      return;
    }
    setActiveStep(0);
  }, [router.query.dataPodId]);

  return (
    <Box>
      <Typography
        variant="h4"
        sx={{ fontWeight: 800, color: "text.primary", mb: 2 }}
      >
        Review and Complete
      </Typography>
      <Typography variant="subtitle2" component="p" sx={{ mb: 4 }}>
        Review all the information you provided and click Submit to complete.
      </Typography>

      <Grid container spacing={5} sx={{ mt: 2 }}>
        {/* User Information Section */}
        <Grid item xs={12} sx={{ p: 2, mb: 2 }}>
          <Typography
            variant="h5"
            sx={{ fontWeight: 700, color: theme.palette.primary.light, mb: 2 }}
          >
            User Information
          </Typography>
          {userData.selectedOrganizationType === "natural-person" ? (
            <>
              <Typography variant="body1">
                Organization: {userData.organizationName || "N/A"}
              </Typography>
              <Typography variant="body1">
                First Name: {userData.firstName}
              </Typography>
              <Typography variant="body1">
                Last Name: {userData.familyName}
              </Typography>
              <Typography variant="body1">
                Date of Birth: {userData.dateOfBirth}
              </Typography>
              <Typography variant="body1">Gender: {userData.gender}</Typography>
              <Typography variant="body1">
                Nationality: {userData.nationality}
              </Typography>
            </>
          ) : (
            <>
              <Typography variant="body1">
                Organization: {userData.organizationName || "N/A"}
              </Typography>
              <Typography variant="body1">
                Legal Person Identifier: {userData.legalPersonIdentifier}
              </Typography>
              <Typography variant="body1">
                Legal Name: {userData.legalName}
              </Typography>
              <Typography variant="body1">
                Legal Address: {userData.legalAddress}
              </Typography>
              <Typography variant="body1">
                Country: {userData.country}
              </Typography>
              <Typography variant="body1">
                Tax Reference: {userData.taxReference}
              </Typography>
              <Typography variant="body1">
                VAT Number: {userData.vatNumber}
              </Typography>
            </>
          )}
          <Typography variant="body1">
            Profile Link: {userData.profileLink}
          </Typography>
        </Grid>

        {/* Data POD Information Section */}
        <Grid item xs={12} sx={{ p: 2, mb: 2 }}>
          <Typography
            variant="h5"
            sx={{ fontWeight: 700, color: theme.palette.primary.light, mb: 3 }}
          >
            Data Pod Information
          </Typography>
          <Box sx={{ display: "flex", flexWrap: "wrap", gap: 2, mb: 3 }}>
            <Typography variant="body1">Data Pod:</Typography>
            {dataPodData.dataPodName || "N/A"}
          </Box>
          <Box sx={{ display: "flex", flexWrap: "wrap", gap: 2, mb: 3 }}>
            <Typography variant="body1">Agent Purpose:</Typography>
            {dataPodData.agentPurpose?.map((p) => (
              <Chip key={p.name} label={p.name} />
            ))}
          </Box>
          <Box sx={{ display: "flex", flexWrap: "wrap", gap: 2, mb: 3 }}>
            <Typography variant="body1">Deny List:</Typography>
            {dataPodData.denyList.map((agent) => (
              <Chip
                key={agent.agentId}
                label={agent.name}
                sx={{
                  backgroundColor: "red",
                }}
              />
            ))}
          </Box>
          <Box sx={{ display: "flex", flexWrap: "wrap", gap: 2, mb: 2 }}>
            <Typography variant="body1">Allow List:</Typography>
            {dataPodData.allowList.map((agent) => (
              <Chip
                key={agent.agentId}
                label={agent.name}
                sx={{
                  backgroundColor: "green",
                }}
              />
            ))}
          </Box>
        </Grid>

        {/* Data Use Policy Section */}
        <Grid item xs={12} sx={{ p: 4, mb: 4 }}>
          <Typography
            variant="h5"
            sx={{ fontWeight: 700, color: theme.palette.primary.light, mb: 2 }}
          >
            Data Use Policy
          </Typography>
          <Box sx={{ display: "flex", flexWrap: "wrap", gap: 1, mb: 1 }}>
            <Typography variant="body1">Attribution Policies:</Typography>
            {usePoliciesData.attributionPolicies?.map((p) => (
              <Chip
                key={p.name}
                label={p.name}
                color="primary"
                variant="outlined"
              />
            ))}
          </Box>
          <Box sx={{ display: "flex", flexWrap: "wrap", gap: 1, mb: 1 }}>
            <Typography variant="body1">Compensation Policies:</Typography>
            {usePoliciesData.compensationPolicies?.map((p) => (
              <Chip
                key={p.name}
                label={p.name}
                color="secondary"
                variant="outlined"
              />
            ))}
          </Box>
        </Grid>

        {/* Action Buttons */}
        <Grid
          item
          xs={12}
          sx={{ display: "flex", justifyContent: "space-between" }}
        >
          <Button
            size="large"
            variant="outlined"
            color="secondary"
            onClick={handleBack}
          >
            Previous
          </Button>
          <Button size="large" variant="contained" onClick={onSubmit}>
            Submit
          </Button>
        </Grid>
      </Grid>
    </Box>
  );
};

export default ReviewAndComplete;
