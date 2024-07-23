



const oidcConfig = {
  authority: process.env.REACT_APP_OIDC_AUTHORITY,
  client_id: process.env.REACT_APP_OIDC_CLIENT_ID,
  redirect_uri: process.env.REACT_APP_OIDC_REDIRECT_URI,
  response_type: "code",
  scope: "openid profile email",
  post_logout_redirect_uri: process.env.REACT_APP_OIDC_POST_LOGOUT_REDIRECT_URI,
  silent_redirect_uri: process.env.REACT_APP_OIDC_SILENT_REDIRECT_URI,
  automaticSilentRenew: true,
  pkceMethod: 'S256'
};





export default {
  registerEndpoint: '/jwt/register',
  storageTokenKeyName: 'accessToken',
  onTokenExpiration: 'refreshToken', // logout | refreshToken
  selectedOrganizationId: 'activeOrganizationId',
  selectedProjectId: 'activeProjectId',
  user: 'userData',
  oidcConfig: oidcConfig
}
