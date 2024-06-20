import React from "react";
import { styled, useTheme } from "@mui/material/styles";

import {
  Box,
  Button,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Grid,
  IconButton,
  Typography,
} from "@mui/material";
import Icon from "src/@core/components/icon";
import QRCodeComponent from "src/views/provenAI/gq-code-component/QRCodeComponent";

const CredentialsWithQrCodeComponent = ({ handleCredentialsClose }) => {
  const theme = useTheme();

  return (
    <Box
      sx={{
         border: "2px solid ",
         borderColor: "primary.main",
         borderRadius: "2px",
      }}>
      <Box
        sx={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",          
          px: { xs: theme.spacing(5), sm: theme.spacing(15) },
          pt: { xs: theme.spacing(5), sm: theme.spacing(8) },
          // pb: { xs: theme.spacing(5), sm: theme.spacing(8) },
        }}
      >
        <DialogTitle
          id="credentials"
          sx={{
            textAlign: "left",
            fontSize: "1.5rem",
            padding: 0,           
          }}
        >
          Offer your Credential
        </DialogTitle>
        <IconButton
          sx={{ color: "primary.main" }}
          onClick={handleCredentialsClose}
        >
          <Icon icon="mdi:close" />
        </IconButton>
      </Box>

      <DialogContent
        sx={{
          pb: (theme) => `${theme.spacing(8)} !important`,
          px: { xs: theme.spacing(5), sm: theme.spacing(15) },
          pt: { xs: theme.spacing(5), sm: theme.spacing(12, 5) },
          // backgroundColor: "rgba(234, 234, 255, 0.06)",
          display: "flex",
          justifyContent: "center",
        }}
      >
        <Box sx={{ textAlign: "center" }}>
          <QRCodeComponent
            value="https://example.com/&q=http://localhost:3001/provenAI/data-pods-control/?"
            size={256}
            fgColor={theme.palette.primary.dark}
            logo="/images/provenAILogo.svg"
          />
        </Box>
      </DialogContent>
      <DialogActions
        sx={{
          justifyContent: "space-between",
          px: { xs: theme.spacing(5), sm: theme.spacing(15) },
          pb: { xs: theme.spacing(8), sm: theme.spacing(12.5) },
          display: "flex",
          width: "100%",
        }}
      >
        <Button variant="outlined" onClick={handleCredentialsClose}>
          COPY URL
        </Button>
        <Button
          variant="contained"
          sx={{ mr: 2 }}
          onClick={handleCredentialsClose}
        >
          UPLOAD VC
        </Button>
      </DialogActions>
    </Box>
  );
};

export default CredentialsWithQrCodeComponent;
