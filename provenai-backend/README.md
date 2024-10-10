# ProvenAI Backend

ProvenAI Backend is a Spring Boot API Microservice responsible for managing the ProvenAI ecosystem. It provides the necessary APIs to manage the ProvenAI ecosystem, including the following functionalities:

### Semantic Search Access Control 
  This is a central component for ProvenAI. This component is called by the Agents to request data. It exposes APIs responsible for the following functionalities:
  -	Register Agent. Agent initiate the Registration flow and they provide information about the Purpose of Use. The System registers the Agent and generates the AIAgentID VC
  -	Authorize Agent. The Agent need to provide the AIAgentID VP, once it is verified, this component is generating an access token so that the Agent can perform all the rest of the protected APIs
  -	Search API. It exposes an API where different agents request information matching specific criteria. For each agent, the Purpose of Use is retrieved an the request is forwarded to the Data Holder platform
  -	Use Permission. Once document sections have been returned from data holders, this module package the sections in  Data Transfer VCs indicating the ISCC code of each section, the Agent the gets the permission and the Data Use Policies.


### Provenance Tracker API & Web Interface
This exposes all the APIs to be used by the web interfaces for ProvenAI users to:
-	Configure their options related to the ACL
-	View provenance of information per agent
-	Track how much they have contributed to Agents
-	Get and Verify Proofs of usage in the blockchain



## Getting Started


This application has been containerized using Docker. To run the application, you need to have Docker installed on your machine.
Docker Compose is also provide to run the application with all the required services.

### Prerequisites

- Docker
- Docker Compose
- Java 17 or higher
- Maven

### Installation 

#### Step 1: Clone the repository
```bash
git clone https://github.com/ctrl-space-labs/proven-ai.git
```

#### Step 2: Set up environment variables

In `./proven-ai/provenai-compose-scripts/local-installation/.env-local`, and set up the environment variables according to [Environment Variables](../Getting%20Started/Environment-Variables) documentation.


#### Step 3: Run docker compose
```bash
cd ./proven-ai/provenai-compose-scripts/local-installation
docker-compose --env-file .env-local up --build -d
docker-compose up
```

This command builds all the containers for the necessary services in the provenAI ecosystem:
- provenAI backend
- provenAI frontend
- provenAI SDK
- gendox backend
- gendox frontend
- database container
- keycloak container

Also to set up all the environment variables and properties needed the following .env files are needed:
- `.env-local`

#### Step 4: Get Keycloak client keys

ProvenAI uses Keycloak for authentication. You need to get the client keys for the services to interact with Keycloak. For more info about how to set up the Keycloak server and configure keycloak settings see [Keycloak Configuration](https://ctrl-space-labs.github.io/proven-ai/docs/Getting Started/Keycloak-Configuration). After running the docker compose you will need to configure the keycloak settings in order to run the app.