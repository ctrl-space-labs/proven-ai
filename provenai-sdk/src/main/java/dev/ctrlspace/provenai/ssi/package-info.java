/**
 * This package contains the main classes for the SSI (Self-Sovereign Identity) module.
 * <p>
 *     The SSI module is responsible for managing the identity of the user (human & AI Agents) and the user's data.
 *     In the context of ProvenAI, the SSI module is used to generate:
 *     - AI Agent's DID
 *     - Issue AI Agent's ID VC
 *     - Present the Agent's ID VC
 *     - Issue the "Permission to Use" VC of a document, to be used by the AI Agent.
 *     - Verify VCs like the above (Agent's ID VC, Permission to Use VC)
 *
 *     Some of the main functionalities of the SSI module are:
 *     - Generate did:key
 *     - Generate DID document
 *     - Get supported DID methods
 *     - Generate Verifiable Credential
 *     - Get supported Verifiable Credential templates
 *     - Generate Verifiable Presentation
 *
 * </p>
 *
 * @Since 1.0
 * @Author Chris Sekas
 * @Version 1.0
 */
package dev.ctrlspace.provenai.ssi;