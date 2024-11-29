# ProvenAI: Provenance in AI
**by** [Ctrl+Space Labs](https://www.ctrlspace.dev/)

ProvenAI transforms AI collaboration by empowering contributors with unprecedented control over their knowledge data through innovative traceability features, ensuring transparency and ownership in every contribution.

## Description 
ProvenAI is at the forefront of reshaping AI collaboration and knowledge management with a visionary approach. Rooted in the belief that contributors deserve control and recognition for their intellectual contributions, the project introduces groundbreaking features focused on contribution, traceability, and data control.

Central to ProvenAI's mission is the development of modularized software components that redefine how contributors interact with AI systems. The project pioneers decentralised identities, ensuring secure and personalised attribution for every contribution. This not only safeguards contributors' intellectual property but also provides them with a sense of ownership and control over their knowledge data.

Innovative traceability features, leveraging blockchain technology and Merkle trees, set ProvenAI apart in the AI landscape. Contributors can now trace the journey of their contributions within AI-generated answers, introducing unprecedented transparency. By adapting the International Standard Content Code (ISCC) to tag specific sub-sections of documents, ProvenAI ensures granular yet cohesive traceability, a revolutionary stride away from traditional methods.

The heart of ProvenAI lies in fair compensation models, where contributors are recognized and rewarded for their impact. Tokenized metrics provide a standardised and replicable approach to compensating contributors, fostering a circular economy of knowledge creation. This approach not only motivates contributors but also enhances the quality of AI-generated content.
Incorporating Next-Generation Internet (NGI) technologies, such as self-sovereign identity (SSI), ProvenAI integrates cutting-edge solutions for semantic search and credential management. ProvenAI envisions a future where contributors have the tools and transparency they need to shape the trajectory of AI knowledge collaboratively.

## Use Cases 

ProvenAI will target firstly tutors contributing their knowledge to AI agents. This will enable a hands-on exploration of how its features, such as traceability and fair compensation, enhance data provenance in the realm of AI-driven education. 
Imagine an educational platform where tutors are a vital part of the AI-driven learning experience. With ProvenAI, the traceability features show how their insights impact AI-generated content, and transparent tokenized metrics calculate fair compensation, creating a dynamic knowledge economy. 

![ProvenAI - Concept Diagram](https://github.com/ctrl-space-labs/proven-ai/assets/75636288/48b27e74-3674-49f7-b12c-77ed92c73011)

## Main Functionalities of ProvenAI
ProvenAI offers a secure and user-friendly platform for Generative AI and data interactions, focusing on simplicity, compliance, and fair compensation and recognition. It is designed to make AI interactions with data straightforward, secure, and compliant with GDPR, featuring intuitive dashboards for seamless management of data and policies. Through blockchain technology, it assures auditable data usage and fosters a fair compensation mechanism, paving the way for a transparent and equitable AI knowledge economy.
Here's a quick overview of its core functionalities:

### AI Agents Identification
- **Purpose Registration:** AI agents join the platform by detailing their intended use of data, specifying their operational domain (e.g., education, arts, corporate assistance), and whether they serve corporations or individuals. This process ensures alignment with data owners' policies.
- **Verifiable Credentials (VCs):** AI agents receive a unique Verifiable AI Agent ID credential upon registration. This ID is required for platform authentication, enabling secure and transparent interactions.
### AI Data Governance
- **Access Control and Monitoring:** Data owners can precisely define how their data is accessed and used by AI systems through access control policies. These policies dictate the conditions under which data can be engaged, ensuring owners maintain control and visibility.
- **Data Use Transparency:** The platform generates VCs based on user-defined access policies, assigning unique International Standard Content Code (ISCC) identifiers to document subsections. This links AI data usage directly to its source, enhancing data provenance and integrity.
### Data Provenance and Tracking in AI Systems
- **Verifiable Data Usage:** When AI agents access data, a "Data Access Credential" VC is created, marking the AI agent's DID as the subject. This credential grants permission to use the data, with each usage being blockchain-registered to ensure a traceable data lineage.

### Installation

#### Step 1: Clone the repository
```bash
git clone https://github.com/ctrl-space-labs/proven-ai.git
```

#### Step 2: Set up environment variables

In `./proven-ai/provenai-compose-scripts/local-installation/.env-local`, and set up the environment variables according to [Environment Variables](https://ctrl-space-labs.github.io/proven-ai/docs/Getting%20Started/Environment-Variables) documentation.


>The `.env-local` file contains some environment variables are comented out. These are mandatory variables that the user must create for the provenAI app to operate correctly. More information on the mandatory variables created by the developer [here](../Getting%20Started/Environment-Variables#mandatory-environment-variables).
> 
> **AWS Keys** for S3 integration are **mandatory**.


#### Step 3: Run docker compose
```bash
cd ./proven-ai/provenai-compose-scripts/local-installation
docker-compose --env-file .env-local pull
docker-compose --env-file .env-local up -d
```

This command builds all the containers for the necessary services in the provenAI ecosystem:
- ProvenAI Backend
- ProvenAI Grontend
- ProvenAI SDK
- Gendox Backend
- Gendox Frontend
- Postgres Database Container
- Keycloak Container

Also to set up all the environment variables and properties needed the following .env files are needed:
- `.env-local`

#### Step 4: Get Keycloak client keys

ProvenAI uses Keycloak for authentication. You need to get the client keys for the services to interact with Keycloak. For more info about how to set up the Keycloak server and configure keycloak settings see [Keycloak Configuration](https://ctrl-space-labs.github.io/proven-ai/docs/Getting%20Started/Keycloak-Configuration). After running the docker compose you will need to configure the keycloak settings in order to run the app.


## Code & Documentation
All related code can the found in the [Github Repository](https://github.com/ctrl-space-labs/proven-ai) of the project. 

For detailed documentation on the project, please visit the [ProvenAI Documentation](https://ctrl-space-labs.github.io/proven-ai/)

The OpenAPI documentation can be found in [ProveAI's Swagger](https://dev.proven-ai.ctrlspace.dev/proven-ai/api/v1/swagger-ui/index.html#/) page


## Repository Structure
This reposiroty follows the [mono-repo](https://www.sonarsource.com/learn/monorepo/) approach, different modules are under the subdirectories of the repository. More details for each module can be found here:

| Module                                   | README file                          | Description                                    |
|------------------------------------------|--------------------------------------|------------------------------------------------|
| `provenai-backend`                       | [provenAI backend README](provenai-backend/README.md) | Backend ProvenAI services.        |
| `provenai-blockchain`                    | [provenAI blockchain README](provenai-blockchain/README.md) | Blockchain integration for ProvenAI.           |
| `provenai-compose-scripts`               | [provenAI compose-scripts README](provenai-compose-scripts/README.md) | Scripts for managing ProvenAI services via docker-compose.        |
| `provenai-database`                      | [provenAI database README](provenai-database/README.md) | ProvenAI database setup and configuration.           |
| `provenai-documentation`                 | [provenAI documentation README](provenai-documentation/README.md) | Documentation for ProvenAI.              |
| `provenai-frontend`                      | [provenAI frontend README](provenai-frontend/README.md) | Frontend for interacting with ProvenAI platform.        |
| `provenai-sdk`                           | [provenAI SDK README](provenai-sdk/README.md) | SDK for integrating auxiliary data provenance services with ProvenAI ecosystem. |



## Contact information
If you have any questions, don't hesitate to contact us:


Contact [contact@ctrlspace.dev](mailto:contact@ctrlspace.dev)

## Funded By
![logo-NGI_TRUSTCHAIN-negative-white](https://github.com/ctrl-space-labs/proven-ai/assets/75636288/e2e36cd5-2ed0-46d2-a131-2bfc4a36f971)


The development work and innovation process has been funded by European Union’s Horizon 2020 research and innovation programme under grant agreement No. 101093274
