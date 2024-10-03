
# Environment Variables

ProvenAI uses environment variables to configure the services. The environment variables are set in the `.env` file in the `docker-compose` directory.

## ProvenAI Environment Variables
The mandatory Environment Variables are:

| Environment Variable      | Example value                                              |
|--------------------------|------------------------------------------------------------|
| `DATABASE_URL`            | `jdbc:postgresql://localhost:5432/postgres`                |
| `DATABASE_USERNAME`       | `postgres`                                                 |
| `DATABASE_PASSWORD`       | `*****`                                                    |
| `ISSUER_DID`              | `did:key:*********`                                        |
| `ISSUER_PRIVATE_JWK`      | `{"kty":"EC","d":"***","crv":"P-256","kid":"***","x":"***","y":"***"}` |
| `KEYCLOAK_CLIENT_ID`      | `gendox-*****`                                             |
| `KEYCLOAK_CLIENT_SECRET`  | `**********`                                               |
| `spring.profiles.active`  | `dev`                                                    |



## Gendox Environment Variables

ProvenAI has been integrated with Gendox. While ProvenAI can run independently, it is quite handy to have Gendox running alongside it, to better evaluate the solution. 
The environment variables for Gendox are set in the `.env` file in the `docker-compose` directory.
The mandatory Environment Variables are:

| Environment Variable      | Example value                                              |
|--------------------------|------------------------------------------------------------|
| `DATABASE_URL`            | `jdbc:postgresql://localhost:5432/postgres`                |
| `DATABASE_USERNAME`       | `postgres`                                                 |
| `DATABASE_PASSWORD`       | `*****`                                                    |
| `OPENAI_KEY`              | `sk-*****`                                                 |
| `COHERE_KEY`              | `*****`                                                    |
| `GROQ_KEY`                | `*****`                                                    |
| `RSA_KEY_PATH`            | `file:/*****`                                              |
| `PROVEN_AI_ENABLED`       | `true`                                                    |
| `PROVEN_AI_SDK_ISCC_ENABLED`            | `true`                |
| `GENDOX_SPRING_EMAIL_HOST`       | `<email host>`                                                 |
| `GENDOX_SPRING_EMAIL_PASSWORD` | `*****`                                                    |
| `GENDOX_SPRING_EMAIL_PORT`     | `200`                                        |
| `GENDOX_SPRING_EMAIL_USERNAME`      | `<email>` |
| `KEYCLOAK_CLIENT_ID`      | `gendox-*****`                                             |
| `KEYCLOAK_CLIENT_SECRET`  | `**********`                                               |
| `DISCORD_TOKEN`           | `**********`                                                    |
| `spring.profiles.active`  | `dev, openai-integration`                                                    |

The `spring.profiles.active` defines the enviroment. There are three environments set: local, dev, prod.

