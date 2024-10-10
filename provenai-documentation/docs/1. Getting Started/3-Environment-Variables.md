
# Environment Variables

ProvenAI uses environment variables to configure the services. The environment variables are set in the `.env-local` file in the `docker-compose` directory.

## ProvenAI Environment Variables
The mandatory Environment Variables are:

| Environment Variable      | Example value                                              | Description                                                                 |
|--------------------------|------------------------------------------------------------|-----------------------------------------------------------------------------|
| `DATABASE_URL`            | `jdbc:postgresql://localhost:5432/postgres`                |                       |
| `DATABASE_USERNAME`       | `postgres`                                                 |                         |
| `DATABASE_PASSWORD`       | `*****`                                                    |                         |
| `ISSUER_DID`              | `did:key:*********`                                        | Decentralized Identifier (DID) of the issuer for verifiable credentials.   |
| `ISSUER_PRIVATE_JWK`      | `{"kty":"EC","d":"***","crv":"P-256","kid":"***","x":"***","y":"***"}` | JSON Web Key (JWK) representing the issuer's private key used for signing. |
| `KEYCLOAK_CLIENT_ID`      | `gendox-*****`                                             | Client ID used to authenticate the application with the Keycloak server.    |
| `KEYCLOAK_CLIENT_SECRET`  | `**********`                                               | Client secret used for OAuth authentication with Keycloak.                  |
| `spring.profiles.active`  | `dev`                                                     |     |




## Gendox Environment Variables

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
| `DISCORD_TOKEN`                    | `**********`                                               | Token used to authenticate the application with Discord API.                |
| `spring.profiles.active`           | `dev, openai-integration`                                  |       |



The `spring.profiles.active` defines the enviroment. There are three environments set: local, dev, prod.

