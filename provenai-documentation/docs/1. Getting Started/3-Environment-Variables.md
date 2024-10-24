
# Environment Variables

## Mandatory Environment Variables

ProvenAI uses environment variables to configure the services. The environment variables are set in the `.env-local` file in the `docker-compose` directory. There are some mandatory ennvironment variables the user must create fin order for the provenAI app to operate.
The user has to generate:
- At least one LLM key. Default is openAI key.

These are presented below:

| Environment Variable         | Example Value                                              | Description                                                                                     | Documentation Link                                                                                                      |
|------------------------------|-----------------------------------------------------------|-------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------|
| `OPENAI_KEY`                 | `sk-*****`                                               | API key used to authenticate with the OpenAI API for AI model integration.                     | [How to get an OpenAI API key](https://platform.openai.com/docs/quickstart)                                           |
| `COHERE_KEY`                 | `*****`                                                  | API key used to authenticate with the Cohere AI model.                                          | [How to get a Cohere API key](https://docs.cohere.com/v2/docs/rate-limits)                                                   |
| `GROQ_KEY`                   | `*****`                                                  | API key used to authenticate with the Groq AI model.                                            | [How to get a Groq API key](https://console.groq.com/docs/quickstart)                                                                   |

## API Ports

| Variable                                   | Value                         |
|--------------------------------------------|-------------------------------|
| `DATABASE_PORT`                            | `5433`                        |    
| `GENDOX_BACKEND_PORT`                      | `8080`                        |       
| `GENDOX_FRONTEND_PORT`                     | `3000`                        | 
| `KEYCLOAK_HTTPS_PORT`                      | `8443`                        |       
| `KEYCLOAK_HTTP_PORT`                       | `8880`                        | 
| `PROVEN_AI_BACKEND_PORT`                   | `8082`                        | 
| `PROVEN_AI_FRONTEND_PORT`                  | `3001`                        |

## Spring Profiles
With this variable we set the active spring profile.

| Variable                                   | Value                           | 
|--------------------------------------------|---------------------------------|
| `SPRING_PROFILES_ACTIVE`                   | `local,openai-integration`     | 

## Database Configuration
Environment variables for connecting to the database.

| Variable                                   | Value                                   | 
|--------------------------------------------|-----------------------------------------|
| `DATABASE_NAME`                            | `postgres`                             | 
| `DATABASE_USERNAME`                        | `gendox_user`                          | 
| `DATABASE_PASSWORD`                        | `root`                                 |
| `DATABASE_URL`                            | `jdbc:postgresql://gendox-database:5432/postgres` | 

## Gendox Core API Environment Variables

This section contains variables related to email configuration for the Gendox Core API. To enable features requiring email address like email verification and user invitation to projects you need to configure the following variables. By default these variables are not required and are commented out.

| Variable                                   | Example Value      | Description                                  |
|--------------------------------------------|------------|----------------------------------------------|
| `GENDOX_SPRING_EMAIL_HOST`                 | `<email host>`      | SMTP host for sending emails.               |
| `GENDOX_SPRING_EMAIL_PASSWORD`              | `*****`      | Password for the SMTP email account.        |
| `GENDOX_SPRING_EMAIL_PORT`                  | `200`      | SMTP port for sending emails.               |
| `GENDOX_SPRING_EMAIL_USERNAME`               | `<email>`      | Username for the SMTP email account.        |

## External Service Keys

Lists API keys used for accessing external services, ensuring secure communication with these services.

| Variable                                   | Example Value       | Description                                    |
|--------------------------------------------|-------------|------------------------------------------------|
| `GROQ_KEY`                                | `change_it` | API key used to authenticate with the Groq AI model.                     |
| `COHERE_KEY`                              | `change_it` | API key used to authenticate with the Cohere AI model.                   |
| `OPENAI_KEY`                              | `sk-*****`  |  API key used to authenticate with the OpenAI API for AI model integration.               |
| `RSA_KEY_PATH`                            | `****` | Path to the RSA private key for JWT.        |
| `PROVEN_AI_ENABLED`                        | `true`      | Flag to enable Proven AI integration.        |
| `PROVEN_AI_SDK_ISCC_ENABLED`              | `false`     | Flag to enable ISCC SDK for Proven AI.      |

You will need to configure one LLM key for the chat functionality. By setting the  `PROVEN_AI_ENABLED` flag to true, search on provenAI data pods is enabled. Also by setting 
`PROVEN_AI_SDK_ISCC_ENABLED` to true, when a document instance is created an ISCC code is assgined to it. Otherwise a random UUID is assigned.

## AWS Configuration
Configuration settings for AWS services, specifically for accessing S3 storage. These variables are not mandatory, you need to configure them only if you want s3 integration. If these variables are not set, any files uploaded will be stored in your local filesystem on your home folder.

The path of the documents stored will be:
`{user.home}/gendox/documents`.  

| Variable                                   | Value        | Description                                   |
|--------------------------------------------|--------------|-----------------------------------------------|
| `AWS_ACCESS_KEY`                          | `****`       | Token used to authenticate the application with AWS services(e.g. S3 storage).             |
| `AWS_SECRET_ACCESS_KEY`                   | `******`     | Secret key used in conjunction with the access key to authenticate with AWS services.       |

## Keycloak Configuration
These are the environment variables required for Keycloak, including client credentials and endpoints for authentication.

| Variable                                   | Value                          | Description                                    |
|--------------------------------------------|--------------------------------|------------------------------------------------|
| `KEYCLOAK_ADMIN`                          | `gendox_admin`                 | Admin username for Keycloak.                  |
| `KEYCLOAK_ADMIN_PASSWORD`                 | `changeit`                     | Admin password for Keycloak.                  |
| `KEYSTORE_PASSWORD`                        | `changeit`                     | Password for the Keycloak keystore.           |
| `KEYCLOAK_HTTP_RELATIVE_PATH`             | `idp`                          | Relative path for Keycloak HTTP access.       |
| `KEYCLOAK_CLIENT_ID`                      | `gendox-private-client`        | Client ID for Gendox Keycloak client.         |
| `KEYCLOAK_PROVEN_AI_CLIENT_ID`            | `proven-ai-private-client`     | Client ID for Proven AI Keycloak client.      |
| `KEYCLOAK_CLIENT_SECRET`                  | `your-gendox-secret`           | Client secret for Gendox Keycloak client.     |
| `KEYCLOAK_PROVEN_AI_CLIENT_SECRET`        | `your-proven-secret`           | Client secret for Proven AI Keycloak client.  |


## Gendox Frontend Configuration

Lists the environment variables for the Gendox frontend application, including OIDC settings and backend URLs.

| Variable                                   | Value                                          | Description                                    |
|--------------------------------------------|------------------------------------------------|------------------------------------------------|
| `NEXT_PUBLIC_OIDC_AUTHORITY`               | `https://dev.gendox.ctrlspace.dev/idp/realms/gendox-idp-dev` | OIDC authority for authentication.         |
| `NEXT_PUBLIC_GENDOX_OIDC_AUTHORITY`        | `http://localhost:8443/idp/realms/gendox-idp-dev` | Local OIDC authority for authentication.    |
| `NEXT_PUBLIC_GENDOX_OIDC_CLIENT_ID`        | `gendox-pkce-public-client-local`             | Public client ID for Gendox OIDC.          |
| `NEXT_PUBLIC_GENDOX_OIDC_REDIRECT_URI`     | `http://localhost:3000/oidc-callback/`       | Redirect URI after OIDC authentication.     |
| `NEXT_PUBLIC_GENDOX_OIDC_POST_LOGOUT_REDIRECT_URI` | `http://localhost:3000/login`         | Redirect URI after OIDC logout.             |
| `NEXT_PUBLIC_GENDOX_OIDC_SILENT_REDIRECT_URI` | `http://localhost:3000/silent-renew`    | Silent redirect URI for OIDC.               |
| `NEXT_PUBLIC_GENDOX_BACKEND_URL`           | `http://localhost:8080/gendox/api/v1/`       | Backend URL for Gendox API.                 |
| `NEXT_PUBLIC_PROVEN_AI_FRONTEND_URL`       | `http://localhost:3001/`                     | Frontend URL for Proven AI.                  |

## Proven AI Git Configuration

Configuration settings for accessing the Proven AI Git repository, including repository details and branch information.

| Variable                                   | Value                                    | Description                                  |
|--------------------------------------------|------------------------------------------|----------------------------------------------|
| `PROVEN_AI_GIT_REPOSITORY`                | `github.com/ctrl-space-labs/proven-ai.git` | Repository URL for Proven AI.               |
| `PROVEN_AI_GIT_BRANCH`                    | `dev`                                   | Branch name for Proven AI.                   |

## Proven AI Backend Configuration

| Variable                                   | Value                                                                                       | Description                                    |
|--------------------------------------------|---------------------------------------------------------------------------------------------|------------------------------------------------|
| `ISSUER_DID`                              | `did:did:key:z*******`                           | Decentralized Identifier (DID) of the issuer for Verifiable Credentials (VC)                           |
| `ISSUER_PRIVATE_JWK`                      | `{"kty":"EC","d":"****"}` | JSON Web Key (JWK) of the issuer's private key for signing Verifiable Credentials.                    |

:::note
Default `ISSUER_DID` and `ISSUER_PRIVATE_JWK` values are provided in the `.env-local`. These are for testing only as they are not private values, to facilitate the provenAI setup.
:::

## Proven AI Frontend Configuration

This section specifies the environment variables related to the Proven AI frontend.

| Variable                                        | Value                                                      | Description                                                  |
|-------------------------------------------------|------------------------------------------------------------|--------------------------------------------------------------|
| `NEXT_PUBLIC_PROVEN_AI_OIDC_AUTHORITY`          | `http://localhost:8880/idp/realms/gendox-idp-dev`       | The OIDC authority URL for authentication.                   |
| `NEXT_PUBLIC_PROVEN_AI_OIDC_CLIENT_ID`          | `proven-pkce-public-client-local`                        | The client ID for the OIDC application.                      |
| `NEXT_PUBLIC_PROVEN_AI_OIDC_REDIRECT_URI`      | `http://localhost:3001/oidc-callback/`                  | Redirect URI after successful OIDC authentication.           |
| `NEXT_PUBLIC_PROVEN_AI_OIDC_POST_LOGOUT_REDIRECT_URI` | `http://localhost:3001/login`                         | Redirect URI after logout from OIDC.                         |
| `NEXT_PUBLIC_PROVEN_AI_OIDC_SILENT_REDIRECT_URI` | `http://localhost:3001/silent-renew`                   | Silent renew redirect URI for OIDC authentication.           |
| `NEXT_PUBLIC_PROVEN_AI_BACKEND_URL`             | `http://localhost:8082/proven-ai/api/v1/`               | Backend URL for the Proven AI API.                           |
| `NEXT_PUBLIC_GENDOX_FRONTEND_URL`               | `http://localhost:3000/`                                 | URL for the Gendox frontend application.                     |
| `NEXT_PUBLIC_VERIFIER_URL`                       | `http://localhost:7003/`                                 | URL for the Verifier service.                                |

## ISCC App Configuration

This section specifies the configuration for the ISCC application, including environment settings and operational parameters.

| Variable                                        | Value                       | Description                                                  |
|-------------------------------------------------|-----------------------------|--------------------------------------------------------------|
| `ISCC_WEB_ENVIRONMENT`                           | `production`                |        |
| `ISCC_WEB_SITE_EMAIL`                           | `example@example.com`       | Email address for the website contact.                      |
| `ISCC_WEB_SITE_ADDRESS`                         | `http://localhost:8970`     | Address of the ISCC web application.                        |
| `ISCC_WEB_SITE_PORT`                            | `8971`                      | Port on which the ISCC web application runs.                |
| `ISCC_WEB_PRIVATE_FILES`                        | `true`                     | Indicates if private file storage is enabled.               |
| `ISCC_WEB_MAX_UPLOAD_SIZE`                      | `1073741824`                | Maximum upload size in bytes (1 GB).                       |
| `ISCC_WEB_STORAGE_EXPIRY`                       | `3600`                      | Expiration time for stored files in seconds.                |
| `ISCC_WEB_CLEANUP_INTERVAL`                     | `600`                       | Cleanup interval for stored files in seconds.               |
| `ISCC_WEB_LOG_LEVEL`                            | `INFO`                      | Logging level for the application.                          |
| `ISCC_WEB_IO_READ_SIZE`                         | `2097152`                  | Size for IO read operations in bytes (2 MB).               |
| `FORWARDED_ALLOW_IPS`                           | `*`                         | Allowed IPs for forwarded requests.                          |
| `ISCC_SDK_GRANULAR`                             | `false`                    | Flag for granular SDK features are enabled.             |

## Wallet, Issuer, Verifier Ports

| Variable                                        | Value   | Description                                             |
|-------------------------------------------------|---------|---------------------------------------------------------|
| `WALLET_BACKEND_PORT`                           | `7001`  | Port for the Wallet backend service.                    |
| `ISSUER_API_PORT`                               | `7002`  | Port for the Issuer API service.                        |
| `VERIFIER_API_PORT`                             | `7004`  | Port for the Verifier API service.                      |
| `WALLET_FRONTEND_PORT`                          | `7101`  | Port for the Wallet frontend application.               |
| `WEB_PORTAL_PORT`                               | `7102`  | Port for the Web Portal application.                    |
| `VC_REPO_PORT`                                  | `7103`  | Port for the Verifiable Credentials repository service. |