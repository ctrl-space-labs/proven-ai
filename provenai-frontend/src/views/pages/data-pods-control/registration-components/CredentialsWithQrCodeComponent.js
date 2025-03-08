import React from "react";
import { useTheme } from "@mui/material/styles";

import { useState, useEffect } from "react";

import {
  Box,
  Button,
  DialogActions,
  DialogContent,
  DialogTitle,
  IconButton,
  Typography,
} from "@mui/material";
import Icon from "src/views/custom-components/mui/icon/icon";
import QRCodeComponent from "src/views/custom-components/gq-code-component/QRCodeComponent";

const CredentialsWithQrCodeComponent = ({
  handleCredentialsClose,
  getURL,
  title,
}) => {
  const theme = useTheme();


  const [url, setUrl] = useState(null);
  const [loading, setLoading] = useState(false);
  const [copyButtonText, setCopyButtonText] = useState("Copy URL");

  const fetchOfferURL = async () => {
    setLoading(true);
    const fetchedUrl = await getURL();
    setUrl(fetchedUrl);
    setLoading(false);
  };

  useEffect(() => {
    fetchOfferURL();
  }, [getURL]);



  async function copyToClipboard(text) {
    try {
      await navigator.clipboard.writeText(text);
      // console.log("Text copied to clipboard!");
      return true;
    } catch (err) {
      // console.error("Failed to copy text: ", err);
      return false;
    }
  }

  async function copyUrl() {
    const success = await copyToClipboard(url);
    if (success) {
      setCopyButtonText("Copied!");
    }
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
          ) : url? (
            <QRCodeComponent
              value={url}
              size={256}
              fgColor={theme.palette.primary.light}
              logo="/images/provenAILogo.svg"
            />
          ) : (
            // If URL is empty or null, show an error message and a Retry button
            <Box>
              <Typography variant="h6" color="error">
                Credential Offer URL is not available.
              </Typography>
              <Button variant="outlined" onClick={fetchOfferURL}>
                Retry
              </Button>
            </Box>
          )}
        </Box>
      </DialogContent>
      {url && (
      <DialogActions
        sx={{
          justifyContent: "space-between",
          px: { xs: theme.spacing(5), sm: theme.spacing(15) },
          pb: { xs: theme.spacing(8), sm: theme.spacing(12.5) },
          display: "flex",
          width: "100%",
        }}
      >
        <Button variant="outlined" onClick={copyUrl}>
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
      )}
    </Box>
  );
};

export default CredentialsWithQrCodeComponent;
