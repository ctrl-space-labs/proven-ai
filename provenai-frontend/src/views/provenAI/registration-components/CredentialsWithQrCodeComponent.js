import React from "react";
import { styled, useTheme } from "@mui/material/styles";

import { useState, useEffect } from "react";

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
import useClipboard from "../../../@core/hooks/useClipboard";

const CredentialsWithQrCodeComponent = ({
  handleCredentialsClose,
  getURL,
  title,
}) => {
  const theme = useTheme();

  const clipboard = useClipboard();

  const [url, setUrl] = useState(null);
  const [loading, setLoading] = useState(false);
  const [copyButtonText, setCopyButtonText] = useState("Copy URL");

  useEffect(() => {
    const fetchURL = async () => {
      setLoading(true);
      const url = await getURL();
      setUrl(url);
      setLoading(false);
    };

    fetchURL();
  }, []);

  const copyValue = (value) => {
    clipboard.copy(value);
    // toast.success('The source code has been copied to your clipboard.', {
    //     duration: 2000
    // })
  };

  function copyUrl() {
    return () => {
      copyValue(url);
      setCopyButtonText("Copied!");
    };
  }

  return (
    <Box
      sx={{
        border: "2px solid ",
        borderColor: "primary.main",
        borderRadius: "2px",
        backgroundColor: "#FFFFFF",
      }}
    >
      <Box
        sx={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          px: { xs: theme.spacing(5), sm: theme.spacing(15) },
          pt: { xs: theme.spacing(5), sm: theme.spacing(8) },
        }}
      >
        <DialogTitle
          id="credentials"
          sx={{
            textAlign: "left",
            fontSize: "1.5rem",
            padding: 0,
            color: theme.palette.primary.dark,
          }}
        >
          {title}
        </DialogTitle>
        {title !== "Receive your Agent ID Credential" &&
          title !== "Receive your Data Ownership Credential" && (
            <IconButton
              sx={{ color: "primary.main" }}
              onClick={handleCredentialsClose}
            >
              <Icon icon="mdi:close" />
            </IconButton>
          )}
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
          {/*  if offerURL is null show a loader else show the QRCodeComponent*/}
          {loading ? (
            <Typography variant="h6">Loading...</Typography>
          ) : (
            <QRCodeComponent
              value={url}
              size={256}
              fgColor={theme.palette.primary.light}
              logo="/images/provenAILogo.svg"
            />
          )}
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
        <Button variant="outlined" onClick={copyUrl()}>
          <Icon icon="mdi:content-copy" fontSize={20} />
          {copyButtonText}
        </Button>

        {title !== "Receive your Agent ID Credential" &&
          title !== "Receive your Data Ownership Credential" && (
            <Button
              variant="contained"
              sx={{ mr: 2 }}
              onClick={handleCredentialsClose}
            >
              I don't have wallet :'(
            </Button>
          )}
      </DialogActions>
    </Box>
  );
};

export default CredentialsWithQrCodeComponent;
