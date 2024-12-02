# ProvenAI docker-compose scripts 

This module contains Docker Compose configurations for setting up and managing various services required by the ProvenAI ecosystem. It includes configurations for the backend, frontend, databases, and all the auxiliary services used, providing an integrated environment for local development and deployment. It contains three services:

- `dev-ci-proven-backend-installation`: This configuration sets up the Proven AI Backend with a build process from the local repository.
- `local-installation`: This configuration provides a full installation of the Proven AI Backend, Frontend, and all the auxiliary services required for local development.
- `provenai-sdk-docker-compose`: This configuration sets up the Proven AI SDK. More information about the provenAI SDK docker-compose and the services it provides can be found [here](https://ctrl-space-labs.github.io/proven-ai/docs/ProvenAI%20-%20SDK/Getting-Started-SDK).

The main scripts of this module are contained in `local-installation`. With these scripts, you can set up all the services required in the provenAI ecosystem. The services include:
- provenAI backend
- provenAI frontend
- provenAI SDK
- gendox backend
- gendox frontend 
- database container
- keycloak container

Also to set up the services we need to set the [environment variables](https://ctrl-space-labs.github.io/proven-ai/docs/Getting%20Started/Environment-Variables) required for their operation on the `env-local` file. More information on the installation process with the `local-installation` scripts can be found [here](https://ctrl-space-labs.github.io/proven-ai/docs/Getting%20Started/Installation#installation-1).
