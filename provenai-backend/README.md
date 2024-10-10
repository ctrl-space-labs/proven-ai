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

