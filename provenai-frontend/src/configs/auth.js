


const localOidcConfig = {
  authority: "https://dev.gendox.ctrlspace.dev/idp/realms/gendox-idp-dev",
  client_id: "proven-pkce-public-client-local",
  redirect_uri: "http://localhost:3001/oidc-callback/",
  response_type: "code",
  scope: "openid profile email",
  post_logout_redirect_uri: "http://localhost:3001/login",
  silent_redirect_uri: "http://localhost:3001/silent-renew",
  automaticSilentRenew: true,
  pkceMethod: 'S256'
};

const devOidcConfig = {
  authority: "https://dev.gendox.ctrlspace.dev/idp/realms/gendox-idp-dev",
  client_id: "proven-pkce-public-client-dev",
  redirect_uri: "https://dev.provenai.ctrlspace.dev/oidc-callback/",
  response_type: "code",
  scope: "openid profile email",
  post_logout_redirect_uri: "https://dev.provenai.ctrlspace.dev/login",
  silent_redirect_uri: "https://dev.provenai.ctrlspace.dev/silent-renew",
  automaticSilentRenew: true,
  pkceMethod: 'S256'
};

const prodOidcConfig = {
  authority: "https://dev.gendox.ctrlspace.dev/idp/realms/gendox-idp-dev",
  client_id: "proven-pkce-public-client-prod",
  redirect_uri: "https://provenai.ctrlspace.dev/oidc-callback/",
  response_type: "code",
  scope: "openid profile email",
  post_logout_redirect_uri: "https://provenai.ctrlspace.dev/login",
  silent_redirect_uri: "https://provenai.ctrlspace.dev/silent-renew",
  automaticSilentRenew: true,
  pkceMethod: 'S256'
};

// let oidcConfig = prodOidcConfig;
let oidcConfig = localOidcConfig;
// let oidcConfig = devOidcConfig;

// if (process.env.NODE_ENV === 'development') {
//   oidcConfig = devOidcConfig;
// }



export default {
  registerEndpoint: '/jwt/register',
  storageTokenKeyName: 'accessToken',
  onTokenExpiration: 'refreshToken', // logout | refreshToken
  selectedOrganizationId: 'activeOrganizationId',
  selectedProjectId: 'activeProjectId',
  user: 'userData',
  oidcConfig: oidcConfig
}
