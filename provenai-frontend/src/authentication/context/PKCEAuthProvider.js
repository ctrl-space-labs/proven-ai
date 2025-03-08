import React, { createContext, useState, useEffect } from "react";
import { useDispatch } from "react-redux";
import { useRouter } from "next/router";
import axios from "axios";
import authConfig from "src/configs/auth";
import {localStorageConstants} from "src/utils/generalConstants";

import apiRequests from "src/configs/apiRequest.js";
import { userDataActions } from "src/store/userData/userData";
import { fetchOrganizationById } from 'src/store/organizationsSlice/organizationsSlice.js'
import { fetchProject } from "src/store/activeProject/activeProject";
import userManager from "src/services/authService";
import {AuthContext} from "./AuthContext";
import organizationService from "../../provenAI-sdk/organizationService";


const PKCEAuthProvider = ({ children, defaultProvider }) => {
  const [user, setUser] = useState(defaultProvider.user);
  const [loading, setLoading] = useState(defaultProvider.loading);
  const router = useRouter();
  const dispatch = useDispatch();
  const [authState, setAuthState] = React.useState({
    user: null,
    isLoading: true,
  });

  /**
   * Handles login redirect
   *
   * @param returnUrl - the url to redirect to after login
   */
  const handleLogin = (returnUrl) => {
    let args = {};
    if (returnUrl) {
      args = {
        redirect_uri: `${
          authConfig.oidcConfig.redirect_uri
        }?returnUrl=${encodeURIComponent(returnUrl)}`,
      };
    }
    userManager.signinRedirect(args);
  };

  const handleLogout = () => {
    // TODO call to /logout
    clearAuthState();
    userManager.signoutRedirect();
  };

  const clearAuthState = () => {
    setUser(null);
    window.localStorage.removeItem(localStorageConstants.userDataKey);
    window.localStorage.removeItem(localStorageConstants.accessTokenKey);
    window.localStorage.removeItem(localStorageConstants.refreshTokenKey);
    window.localStorage.removeItem(localStorageConstants.selectedOrganizationId);
    window.localStorage.removeItem(localStorageConstants.selectedProjectId);
    window.localStorage.removeItem(authConfig.oidcConfig);
  };

  const loadUser = (user) => {
    setAuthState({ user, isLoading: false });
  };

  const unloadUser = () => {
    setAuthState({ user: null, isLoading: false });
  };

  const removeUser = () => {
    // Here you can clear your application's session and redirect the user to the login page
    userManager.removeUser();
  };

  const initAuthOIDC = () => {
    userManager.getUser().then((user) => {
      if (user && !user.expired) {
        setAuthState({ user, isLoading: false });
      } else {
        // no user data found or user expired, loadUserProfileFromAuthState will handle cleanup
        console.log("initAuthOIDC - user is null or expired: ", user);
        setAuthState({ user: null, isLoading: false });
      }
    });

    // Adding an event listener for when new user data is loaded
    userManager.events.addUserLoaded(loadUser);
    userManager.events.addUserSignedOut(removeUser);
    userManager.events.addUserUnloaded(unloadUser);

    return () => {
      userManager.events.removeUserLoaded(loadUser);
      userManager.events.removeUserUnloaded(unloadUser);
      userManager.events.removeUserSignedOut(removeUser);
    };
  };

  const loadUserProfileFromAuthState = async (authState) => {
    if (authState.isLoading) {
      return;
    }
    setLoading(true);
    if (!authState.user || authState.user === null) {
      setLoading(false);
      clearAuthState();
      return;
    }
    let user = authState.user;
    window.localStorage.setItem(
      localStorageConstants.accessTokenKey,
      user.access_token
    );

    // Set refresh token in local storage
    window.localStorage.setItem(
      localStorageConstants.refreshTokenKey,
      user.refresh_token
    );

    // Fetch user data from getProfile
    setLoading(true);
    await axios
      .get(apiRequests.getProfile, {
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + user.access_token,
        },
      })
      .then(async (userDataResponse) => {
        const organizationIds = userDataResponse.data.organizations.map(
          (org) => org.id
        );
        let organizations;
        // console.log("user", user);
        // console.log("userDataResponse", userDataResponse);
        // console.log("organizationIds", organizationIds);
        try{
          organizations = await organizationService.getOrganizationsByCriteria(
            organizationIds,
            user.access_token
          );
        } catch (error) {
          organizations = { data: { content: [] } };
        }
        // Add 'role': 'admin' to the userDataResponse.data object
        userDataResponse.data.role = "ROLE_ADMIN";
        userDataResponse.data.provenOrgs = organizations.data.content;
        setUser(userDataResponse.data);
        window.localStorage.setItem(
          localStorageConstants.userDataKey,
          JSON.stringify(userDataResponse.data)
        );


        const selectedOrganization =
          organizations.data.content.length > 0
            ? organizations.data.content[0]
            : userDataResponse.data.organizations[0];

        console.log("selectedOrganization", selectedOrganization);

        // Store userData, actives project and organization
        dispatch(userDataActions.getUserData(userDataResponse.data));
        dispatch(
          fetchOrganizationById({
            organizationId: selectedOrganization.id,
            token: user.access_token,
          })
        );
        dispatch(
          fetchProject({
            organizationId: selectedOrganization.id,
            projectId: userDataResponse.data.organizations[0].projects[0].id,
            token: user.access_token,
          })
        );

        window.localStorage.setItem(
          localStorageConstants.selectedOrganizationId,
          selectedOrganization.id
        );
        window.localStorage.setItem(
          localStorageConstants.selectedProjectId,
          userDataResponse.data.organizations[0].projects[0].id
        );

        setLoading(false);
      })

      .catch((userDataError) => {
        setLoading(false);
        console.error(
          "Error occurred while fetching user data:",
          userDataError
        );
      });

    setLoading(false);
  };

  useEffect(() => {
    // initAuth_old();
    return initAuthOIDC();
  }, []);

  useEffect(() => {
    loadUserProfileFromAuthState(authState);
  }, [authState]);

  useEffect(() => {
    if (user && router.pathname.includes("oidc-callback")) {
      let homeUrl = "/provenAI/home";

      //oidc-callback might contain a returnUrl query param to redirect to after login,
      // like ../oidc-callback?returnUrl=%2Fgendox%2Fhome....
      const { returnUrl } = router.query;
      if (returnUrl) {
        homeUrl = decodeURIComponent(returnUrl);
      }

      window.location.href = homeUrl;
    }
  }, [user]);

  useEffect(() => {
    const { organizationId, projectId } = router.query;
    const token = window.localStorage.getItem(
      localStorageConstants.accessTokenKey
    );

    if (user && user.organizations) {
      const updatedActiveOrganization = user.organizations.find(
        (org) => org.id === organizationId
      );
      if (updatedActiveOrganization) {
        dispatch(
          fetchOrganizationById({
            organizationId: updatedActiveOrganization.id,
            token,
          })
        );
        window.localStorage.setItem(
          localStorageConstants.selectedOrganizationId,
          updatedActiveOrganization.id
        );
        const updatedActiveProject = updatedActiveOrganization.projects.find(
          (proj) => proj.id === projectId
        );
        if (updatedActiveProject) {
          dispatch(
            fetchProject({
              organizationId: updatedActiveOrganization.id,
              projectId: updatedActiveProject.id,
              token,
            })
          );
          window.localStorage.setItem(
            localStorageConstants.selectedProjectId,
            updatedActiveProject.id
          );
        }
      }
    }
  }, [user, router]);

  const values = {
    user,
    loading,
    setUser,
    setLoading,
    login: handleLogin,
    logout: handleLogout,
    oidcAuthState: authState,
  };

  return <AuthContext.Provider value={values}>{children}</AuthContext.Provider>;
};

export { PKCEAuthProvider };
