
# Environment Variables

## Mandatory Environment Variables

ProvenAI uses environment variables to configure the services. The environment variables are set in the `.env-local` file in the `docker-compose` directory. There are some mandatory ennvironment variables the user must create fin order for the provenAI app to operate.
The user has to generate:
- At least one LLM key. Default is openAI key.
- A dicord token.
- AWS secret key and secret access key.
  These are presented below:

| Environment Variable         | Example Value                                              | Description                                                                                     | Documentation Link                                                                                                      |
|------------------------------|-----------------------------------------------------------|-------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------|
| `AWS_ACCESS_KEY`             | `AKIAIOSFODNN7EXAMPLE`                                   | The access key ID used to authenticate requests to AWS services.                               | [How to create an AWS access key](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_access-keys.html)    |
| `AWS_SECRET_ACCESS_KEY`      | `wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY`              | The secret access key associated with the access key ID, used to sign requests to AWS services.| [How to create an AWS access key](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_credentials_access-keys.html)    |
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
With thi svariable we set the active spring profile.

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
| `GROQ_KEY`                                | `change_it` | Key for GROQ API access.                     |
| `COHERE_KEY`                              | `change_it` | Key for Cohere API access.                   |
| `OPENAI_KEY`                              | `sk-*****`  | OpenAI API key (keep private).               |
| `RSA_KEY_PATH`                            | `****` | Path to the RSA private key for JWT.        |
| `PROVEN_AI_ENABLED`                        | `true`      | Flag to enable Proven AI integration.        |
| `PROVEN_AI_SDK_ISCC_ENABLED`              | `false`     | Flag to enable ISCC SDK for Proven AI.      |

You will need to configure one LLM key for the chat functionality. By setting the  `PROVEN_AI_ENABLED` flag to true, search on provenAI data pods is enabled.


## ProvenAI Backend Environment Variables
The mandatory Environment Variables are:

| Environment Variable        | Example Value                                             | Description                                                                                     |
|-----------------------------|----------------------------------------------------------|-------------------------------------------------------------------------------------------------|
| `DATABASE_URL`              | `jdbc:postgresql://localhost:5432/postgres`             |                                               |
| `DATABASE_USERNAME`         | `postgres`                                               |                                               |
| `DATABASE_PASSWORD`         | `*****`                                                  |                                              |
| `ISSUER_DID`                | `did:key:*********`                                     | Decentralized Identifier (DID) of the issuer for Verifiable Credentials (VC).                 |
| `ISSUER_PRIVATE_JWK`        | `{"kty":"EC","d":"***","crv":"P-256"...}`              | JSON Web Key (JWK) of the issuer's private key for signing Verifiable Credentials.             |
| `KEYCLOAK_CLIENT_ID`        | `gendox-*****`                                          | Client ID used to authenticate the application with the Keycloak server.                       |
| `KEYCLOAK_CLIENT_SECRET`    | `**********`                                            | Client secret used for OAuth authentication with Keycloak.                                     |
| `spring.profiles.active`    | `local`                                                 |                           |

:::note
Default `ISSUER_DID` and `ISSUER_PRIVATE_JWK` values are provided in the `.env-local`. These are for testing only as they are not private values, to facilitate the provenAI setup.
:::
## Gendox Backend Environment Variables

ProvenAI has been integrated with Gendox. While ProvenAI can run independently, it is quite handy to have Gendox running alongside it, to better evaluate the solution. 
The environment variables for Gendox are set in the `.env` file in the `docker-compose` directory.
The mandatory Environment Variables are:


| Environment Variable               | Example value                                              | Description                                                                 |
|------------------------------------|------------------------------------------------------------|-----------------------------------------------------------------------------|
| `DATABASE_URL`                     | `jdbc:postgresql://localhost:5432/postgres`                |                         |
| `DATABASE_USERNAME`                | `postgres`                                                 |                           |
| `DATABASE_PASSWORD`                | `*****`                                                    |                          |
| `OPENAI_KEY`                       | `sk-*****`                                                 | API key used to authenticate with the OpenAI API for AI model integration.  |
| `COHERE_KEY`                       | `*****`                                                    | API key used to authenticate with the Cohere AI model.                      |
| `GROQ_KEY`                         | `*****`                                                    | API key used to authenticate with the Groq AI model.                        |
| `RSA_KEY_PATH`                     | `file:/*****`                                              | Path to the RSA private key file, used for signing or encrypting data.      |
| `PROVEN_AI_ENABLED`                | `true`                                                     | Flag to enable or disable Proven AI .     |
| `PROVEN_AI_SDK_ISCC_ENABLED`       | `true`                                                     | Flag to enable or disable ISCC SDK functionalities for Proven AI.           |
| `GENDOX_SPRING_EMAIL_HOST`         | `<email host>`                                             | The SMTP server host for sending emails in the application.                 |
| `GENDOX_SPRING_EMAIL_PASSWORD`     | `*****`                                                    | The password for the email account used to send emails.                     |
| `GENDOX_SPRING_EMAIL_PORT`         | `200`                                                      | The port used for SMTP communication with the email server.                 |
| `GENDOX_SPRING_EMAIL_USERNAME`     | `<email>`                                                  | The username or email address used for SMTP authentication.                 |
| `KEYCLOAK_CLIENT_ID`               | `gendox-*****`                                             | Client ID used to authenticate the application with the Keycloak server.    |
| `KEYCLOAK_CLIENT_SECRET`           | `**********`                                               | Client secret used for OAuth authentication with Keycloak.                  |
| `DISCORD_TOKEN`                    | `**********`                                               | Token used to authenticate the application with [Discord API](https://discord.com/developers/docs/topics/oauth2#bot-users).                |
| `spring.profiles.active`           | `dev, openai-integration`                                  |       |
| `AWS_ACCESS_KEY`                    | `AKIAIOSFODNN7EXAMPLE`                                     |Token used to authenticate the application with AWS services.    |
| `AWS_SECRET_ACCESS_KEY`           | `/K7MDENG/bPxRfiCYEXAMPLEKEY`                                |Secret key used in conjunction with the access key to authenticate with AWS services.    |

The `spring.profiles.active` defines the enviroment. There are three environments set: local, dev, prod.

