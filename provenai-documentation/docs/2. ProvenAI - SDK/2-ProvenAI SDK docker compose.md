#  ProvenAI Docker Compose

The ProvenAI SDK can be installed following the provenAI installation instructions as described [here](../Getting%20Started/Installation). It also can be set up for local development when running the [provenAI backend](../Getting%20Started/Installation#running-the-provenai-backend). It containms the `proven-ai-sdk-docker-compose` to set the configuration settings for the docker containers of the microservices used within the provenAI app.

## Issuer API
This package contains the configuration settings for the issuer API provided from waltid-identity [docker-compose issuer api](https://github.com/walt-id/waltid-identity/tree/main/docker-compose). The `config` package contains all necessary configuration settings. 

#### `credential-issuer-metadata.conf`
This file defines the types of verifiable credentials that can be issued by the service, and it is provided from the waltid identity issuer api [docker-compose](https://github.com/walt-id/waltid-identity/tree/main/docker-compose/issuer-api). Each credential type is listed along with its supported format. To support the credentials our provenAI app issues we expanded this file by introducing two types of credetials:
- Verifiable Agent ID: `VerifiableAgentID = [VerifiableCredential, VerifiableAgentID]`
- Verifiable Data Ownership Credential: `VerifiableDataOwnershipCredential = [VerifiableCredential, VerifiableDataOwnershipCredential]`

#### `issuer-service.conf`
This file sets the base URL for the issuer service. The issuer service is responsible for issuing verifiable credentials.
- `baseUrl`: The URL where the issuer API is hosted.

#### `web.conf`
This file configures the web host and port for the issuer service.
- `webHost`: The host address to bind the service. Set to 0.0.0.0.
- `webPort`: The port number to expose the service. Set to 7002.

## Verifier API
This package contains the configuration settings for waltid' s the verifier api configuration provided from [docker-compose verifier api](https://github.com/walt-id/waltid-identity/tree/main/docker-compose/verifier-api). The `config` package contains all necessary configuration settings. 

#### `verifier-service.conf`
This file sets the base URL for the verifier service. The verifier service is responsible for verifying issued credentials.
- `baseUrl`: The URL where the verifier API is hosted.

#### `verifier-service-dev.conf`
This file sets the base URL for the verifier service for the development environment. The verifier service is responsible for verifying issued credentials.
- `baseUrl`: The URL where the verifier API is hosted in the development environment.

#### `verifier-service-dev.conf`
This file sets the base URL for the verifier service for the local environment. The verifier service is responsible for verifying issued credentials.
- `baseUrl`: The URL where the verifier API is hosted in the local environment.

#### `web.conf`
This file configures the web host and port for the verifier service.
- `webHost`: The host address to bind the service. Set to 0.0.0.0.
- `webPort`: The port number to expose the service. Set to 7003.

## Environment Variables
ProvenAI SDK uses environment variables to configure the services. The environment variables are set in the `.env` file in the `provenai-sdk-docker-compose` directory. The services configured in the provenAI SDK is the iscc code generation and the waltid identity issuer and verifier. Therefore there are environent variables specific to each of these services and they are configured based on the services' documentation and docker-compose files.

- For the ISCC code generation [configuration](https://github.com/iscc/iscc-web)

| **Variable**               | **Description**                                   | **Default Value**       |
|----------------------------|---------------------------------------------------|-------------------------|
| `ISCC_WEB_ENVIRONMENT`      | Environment mode (`production`, `development`).   | `development`            |
| `ISCC_WEB_SITE_EMAIL`       | Site administrator email address.                | `<email adress>`  |
| `ISCC_WEB_SITE_ADDRESS`     | Public site address.                             | `http://localhost:8080` |
| `ISCC_WEB_SITE_PORT`        | Port for the ISCC web service.                   | `8080`                  |
| `ISCC_WEB_PRIVATE_FILES`    |  restrict file downloads to original uploader.   | `true`                  |
| `ISCC_WEB_MAX_UPLOAD_SIZE`  | Maximum upload file size (bytes).                | `1073741824`            |
| `ISCC_WEB_STORAGE_EXPIRY`   | Storage expiration time (seconds).               | `3600`                  |
| `ISCC_WEB_CLEANUP_INTERVAL` | Interval for cleanup operations (seconds).       | `600`                   |
| `ISCC_WEB_LOG_LEVEL`        | Log level.                                       | `DEBUG`                  |
| `ISCC_WEB_IO_READ_SIZE`     | File read chunk size (bytes).                    | `2097152`               |
| `FORWARDED_ALLOW_IPS`       | Allowed IPs for forwarding.                      | `*`                     |
| `ISCC_SDK_GRANULAR`         | Activate generation of granular fingerprints .   | `false`                 |


- For the waltID identity issuer and verifier [configuration](https://github.com/walt-id/waltid-identity/blob/main/docker-compose)


| **Variable**               | **Description**                                   | **Default Value**       |
|----------------------------|---------------------------------------------------|-------------------------|
| `WALLET_BACKEND_PORT`       | Port for the wallet backend service.             | `7001`                  |
| `ISSUER_API_PORT`           | Port for the Issuer API service.                 | `7002`                  |
| `VERIFIER_API_PORT`         | Port for the Verifier API service.               | `7003`                  |
| `WALLET_FRONTEND_PORT`      | Port for the wallet frontend service.            | `7101`                  |
| `WEB_PORTAL_PORT`           | Port for the web portal service.                 | `7102`                  |
| `VC_REPO_PORT`              | Port for the VC repository service.              | `7103`                  |
| `VERSION_TAG`               | Version tag for Docker images.                   | `1.0.2406231424-SNAPSHOT` |

## Caddyfile

This file is used to configure reverse proxies for the services.
- **Web Service**: Proxies requests for the ISCC web service (port 8970).
- **Issuer API**: Proxies requests for the Issuer API (port 7002).
- **Verifier API**: Proxies requests for the Verifier API (port 7003).
- **Global Configuration**:
    - `auto_https off`: Disables automatic HTTPS setup by Caddy.
    - `admin off`: Disables the Caddy admin API.
    - `email {$ISCC_WEB_SITE_EMAIL}`: Contact email for notifications, such as certificate expiration.

## Docker Compose Configuration

### Volumes
- **caddy-config**: Volume for Caddy configuration files.
- **caddy-data**: Volume for Caddy data storage.

### Services

#### 1. ISCC Application `iscc-app`
- **image**: The Docker image for the ISCC Web application, sourced from GitHub Container Registry. Set to `ghcr.io/iscc/iscc-web:main`.
- **init**: If set to `true`, enables the init system for the container.
- **env_file**: Loads environment variables from the `.env` file.

#### 2. Caddy Reverse Proxy `caddy`
- **image**: The Docker image for the Caddy reverse proxy, set to `caddy:2.6.1-alpine`.
- **restart**: Defines the restart policy. Set to `unless-stopped`, which means it will restart unless explicitly stopped.
- **env_file**: Loads environment variables from the `.env` file.
- **environment**: Defines environment variables for the service.
  - **ISCC_SDK_GRANULAR**: Set from the `${ISCC_SDK_GRANULAR}` environment variable.
- **volumes**: 
  - `./Caddyfile:/etc/caddy/Caddyfile`: Mounts the local Caddyfile to the container.
  - `caddy-config:/config`: Mounts the Caddy configuration volume.
  - `caddy-data:/data`: Mounts the Caddy data volume.
- **ports**: 
  - Defines the published ports for ISCC Web, Issuer API, and Verifier API.
    - **ISCC Web**: 
      - `target`: `$ISCC_WEB_SITE_PORT`
      - `published`: `$ISCC_WEB_SITE_PORT`
      - `protocol`: `tcp`
      - `mode`: `host`
    - **Issuer API**: 
      - `target`: `$ISSUER_API_PORT`
      - `published`: `$ISSUER_API_PORT`
      - `protocol`: `tcp`
      - `mode`: `host`
    - **Verifier API**: 
      - `target`: `$VERIFIER_API_PORT`
      - `published`: `$VERIFIER_API_PORT`
      - `protocol`: `tcp`
      - `mode`: `host`
- **depends_on**: Specifies the dependency on the `iscc-app` service, ensuring it starts before this service.

#### 3. Issuer API (`issuer-api`)
- **platform**: Specifies the architecture of the container.
- **image**: The Docker image for the WaltID Issuer API, sourced from Docker Hub. Set to `docker.io/waltid/issuer-api:${VERSION_TAG:-latest}`.
- **pull_policy**: Defines the pull policy for the image. Set to `always`, ensuring the latest version is always pulled.
- **depends_on**: Specifies the dependency on the `caddy` service.
- **env_file**: Loads environment variables from the `.env` file.
- **volumes**: 
  - `./issuer-api/config:/waltid-issuer-api/config`: Mounts the local Issuer API configuration directory to the container.

#### 4. Verifier API (`verifier-api`)
- **platform**: Specifies the architecture of the container.
- **image**: The Docker image for the WaltID Verifier API, sourced from Docker Hub. Set to `docker.io/waltid/verifier-api:${VERSION_TAG:-latest}`.
- **pull_policy**: Defines the pull policy for the image. Set to `always`, ensuring the latest version is always pulled.
- **depends_on**: Specifies the dependency on the `caddy` service.
- **env_file**: Loads environment variables from the `.env` file.
- **volumes**: 
  - `./verifier-api/config:/waltid-verifier-api/config`: Mounts the local Verifier API configuration directory to the container.

## Dockerfile

### Base Image
- **FROM**: Specifies the base image for the Docker image. Set to `docker:latest`.

### Install Dependencies
- **RUN apk add --no-cache**: Installs necessary packages without caching the intermediate data.
- **pip3 install --no-cache-dir docker-compose**: Installs Docker Compose using Python's package installer.

### Copy Docker Compose File
  - **COPY docker-compose.yml /app/docker-compose.yml**: Copies The Docker Compose configuration file to the destination path in the Docker image where the `docker-compose.yml` file will be placed.


## WaltID Identity Additional Files
These files are also inlcuded as they are defined in the waltid identity docker-compose. 
### `api-test.py`
This file configures the tests conducted for the docker-compose.
### `podman.yaml`
This files configures the settings for podman deployment.