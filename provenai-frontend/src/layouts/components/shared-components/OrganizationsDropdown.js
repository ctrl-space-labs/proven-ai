import React, { useState, useEffect, useCallback, Fragment } from "react";
import { useDispatch } from "react-redux";
import { useRouter } from "next/router";
import Box from "@mui/material/Box";
import AvatarGroup from "@mui/material/AvatarGroup";
import Menu from "@mui/material/Menu";
import MenuItem from "@mui/material/MenuItem";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import Tooltip from "@mui/material/Tooltip";
import Link from "next/link";

import Icon from "src/views/custom-components/mui/icon/icon";

import { useAuth } from "src/authentication/useAuth";
import { fetchProject } from "src/store//activeProject/activeProject";
import { fetchOrganizationById } from 'src/store/organizationsSlice/organizationsSlice.js'
import { sortByField } from "src/utils/orderUtils";
import CustomAvatar from "src/views/custom-components/mui/avatar/index";
import {localStorageConstants} from "src/utils/generalConstants";

const OrganizationsDropdown = ({ settings }) => {
  const router = useRouter();
  const dispatch = useDispatch();
  const auth = useAuth();
  const [anchorEl, setAnchorEl] = useState(null);
  const [activeOrganizationId, setActiveOrganizationId] = useState(null);
  const [visibleCount, setVisibleCount] = useState(4);
  const token = window.localStorage.getItem(
    localStorageConstants.accessTokenKey
  );


  useEffect(() => {
    const { organizationId } = router.query;
    if (organizationId) {
      setActiveOrganizationId(organizationId);
    }
  }, [router.query]);

  const handleDropdownOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleDropdownClose = useCallback(() => setAnchorEl(null), []);

  const handleOrganizations = useCallback(
    (organization) => {
      const { projects } = organization;
      const newProjectId = projects?.[0]?.id ?? null;
      handleDropdownClose();

      dispatch(
        fetchOrganizationById({
          organizationId: organization.id,
          token,
        })
      );

      dispatch(
        fetchProject({
          organizationId: organization.id,
          projectId: newProjectId,
          token,
        })
      );

      localStorage.setItem(localStorageConstants.selectedOrganizationId, organization.id);
      localStorage.setItem(localStorageConstants.selectedProjectId, newProjectId);
      setActiveOrganizationId(organization.id);

      const newPath =
        router.pathname === "/provenAI/data-pods-control"
          ? `/provenAI/data-pods-control/?organizationId=${organization.id}`
          : router.pathname === "/provenAI/agent-control"
          ? `/provenAI/agent-control/?organizationId=${organization.id}`
          : `/provenAI/home/?organizationId=${organization.id}`;
      router.push(newPath);
    },
    [dispatch, handleDropdownClose, router]
  );

  const sortedOrganizations = sortByField([...auth.user.provenOrgs], "name", activeOrganizationId);

  const visibleOrganizations = sortedOrganizations.slice(0, visibleCount);
  const overflowCount = sortedOrganizations.length - visibleCount;

  return (
    <Fragment>
      <AvatarGroup
        max={visibleCount + 1}
        sx={{ ml: 2, cursor: "pointer" }}
        slotProps={{
          additionalAvatar: {
            onClick: handleDropdownOpen,
          },
        }}
      >
        {visibleOrganizations.map((organization, index) => (
          <Tooltip title={organization.name} key={organization.id}>
            <CustomAvatar
              color="primary"
              sx={{
                width: 40,
                height: 40,
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                color: "white",
                borderRadius: "50%",
                fontSize: "16px",
                boxShadow:
                  organization.id === activeOrganizationId
                    ? "0 0 8px gold"
                    : "none",
                transition: "transform 0.3s ease",
                "&:hover": {
                  transform: "scale(1.2)",
                },
              }}
              onClick={() => handleOrganizations(organization)}
            >
              {organization.name.substring(0, 4)}
            </CustomAvatar>
          </Tooltip>
        ))}
        {overflowCount > 0 && (
          <Tooltip title="More" key="overflow-avatar">
            <CustomAvatar
              sx={{
                width: 40,
                height: 40,
                display: "flex",
                justifyContent: "center",
                alignItems: "center",
                backgroundColor: "gray",
                color: "white",
                borderRadius: "50%",
                fontSize: "16px",
                border: "2px solid primary.dark",
              }}
              onClick={handleDropdownOpen}
            >
              +{overflowCount}
            </CustomAvatar>
          </Tooltip>
        )}
      </AvatarGroup>

      <Menu
        anchorEl={anchorEl}
        open={Boolean(anchorEl)}
        onClose={handleDropdownClose}
        disableAutoFocus
        disableEnforceFocus
        sx={{ "& .MuiMenu-paper": { width: 230, mt: 4 } }}
        anchorOrigin={{
          vertical: "bottom",
          horizontal: settings.direction === "ltr" ? "right" : "left",
        }}
        transformOrigin={{
          vertical: "top",
          horizontal: settings.direction === "ltr" ? "right" : "left",
        }}
      >
        {sortByField([...auth.user.provenOrgs], "name", activeOrganizationId)
          .map((organization) => {
          const href = `/provenAI/home/?organizationId=${organization.id}`;
          return (
            <Link
              href={href}
              passHref
              key={organization.id}
              style={{ textDecoration: "none" }}
            >
              <MenuItem
                sx={{ p: 0 }}
                onClick={(e) => {
                  e.preventDefault();
                  handleOrganizations(organization);
                }}
                selected={organization.id === activeOrganizationId}
              >
                <Box
                  sx={{
                    py: 2,
                    px: 4,
                    width: "100%",
                    display: "flex",
                    alignItems: "center",
                    textDecoration: "none",
                    backgroundColor:
                      organization.id === activeOrganizationId
                        ? "primary.light"
                        : "inherit",
                    "& svg": {
                      mr: 2,
                      fontSize: "1.375rem",
                    },
                  }}
                >
                  <ListItemIcon sx={{ color: "primary.main" }}>
                    <Icon icon="mdi:domain" fontSize={20} />
                  </ListItemIcon>
                  <ListItemText primary={organization.name} />
                </Box>
              </MenuItem>
            </Link>
          );
        })}
      </Menu>
    </Fragment>
  );
};

export default OrganizationsDropdown;
