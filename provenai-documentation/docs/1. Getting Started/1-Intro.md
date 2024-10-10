# Introduction

### TL;DR
> ProvenAI gives you control over your knowledge data in AI systems. With traceability features, it ensures transparency and ownership for every input.
> Jump to the [Installation Guide](../Getting%20Started/Installation) to get started.


## Description

ProvenAI is an open-source project that provides control and recognition for your contributions to AI systems. It focuses on features like contribution tracking, traceability, and data control.

It develops modular software components to change how you interact with AI. By using decentralized identities, it ensures secure and personalized attribution for each input. This protects your intellectual property and gives you ownership over your knowledge data.

Its traceability features use blockchain technology and Merkle trees. You can trace how your inputs are used in AI-generated content. It uses the International Standard Content Code (ISCC) to tag specific parts of documents, providing granular traceability.

ProvenAI also includes fair compensation models, so you're recognized and rewarded. It uses tokenized metrics to standardize compensation, encouraging a sustainable knowledge economy. This not only motivates contributors but also improves the quality of AI-generated content.

It incorporates technologies like self-sovereign identity (SSI) for semantic search and credential management. The goal is to provide you with tools and transparency to shape AI knowledge collaboratively.

## Use Cases

The initial target is tutors who contribute their knowledge to AI agents. This allows exploration of how features like traceability and fair compensation improve data provenance in AI-driven education.

Imagine an educational platform where tutors play a key role in AI-enhanced learning. With ProvenAI, tutors can see how their insights affect AI-generated content, and fair compensation models reward them appropriately.

![ProvenAI - Concept Diagram](https://github.com/ctrl-space-labs/proven-ai/assets/75636288/48b27e74-3674-49f7-b12c-77ed92c73011)

## Main Functionalities of ProvenAI

ProvenAI provides a secure platform for interactions between Generative AI and data, focusing on simplicity, compliance, and fair recognition. It aims to make AI data interactions straightforward, secure, and GDPR-compliant, with intuitive dashboards for managing data and policies. Using blockchain technology, it ensures auditable data usage and supports fair compensation, leading to a transparent AI knowledge economy.

Here's an overview of the core functionalities:

### AI Agent Identification

- **Purpose Registration:** AI agents register on the platform by specifying their intended data use, operational domain (e.g., education, arts), and whether they serve corporations or individuals. This ensures alignment with data owners' policies.
- **Verifiable Credentials (VCs):** Upon registration, AI agents receive a unique Verifiable AI Agent ID credential. This ID is required for authentication, enabling secure and transparent interactions.

### AI Data Governance

- **Access Control and Monitoring:** Data owners can define how their data is accessed and used by AI systems through access control policies. These policies set the conditions for data engagement, ensuring owners maintain control and visibility.
- **Data Use Transparency:** The platform generates VCs based on user-defined access policies, assigning unique ISCC identifiers to document subsections. This links AI data usage directly to its source, enhancing data provenance and integrity.

### Data Provenance and Tracking in AI Systems

- **Verifiable Data Usage:** When AI agents access data, a "Data Access Credential" VC is created, with the AI agent's DID as the subject. This credential grants permission to use the data, and each usage is registered on the blockchain to ensure a traceable data lineage.
