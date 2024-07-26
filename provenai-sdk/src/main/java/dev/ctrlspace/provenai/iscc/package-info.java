/**
 * This package contains the main class for the ISCC algorithm.
 * <p>
 *     The ISCC code is a unique identifier for the content and it is used to verify the content's authenticity.
 *     The ISCC algorithm is responsible for generating the ISCC code for the given content.
 *     In the context of ProvenAI, the ISCC code is used to identify the sections of the content uploaded to the ProvenAI platform.
 *     Then Verifiable credentials are generated for each section of the content and the ISCC code is used as the identifier for the Verifiable credentials.
 *
 *     Some of the supported functionalities of this package are:
 *     - Generate ISCC code using external service
 *     - Generate ISCC code using internal service (not implemented yet)
 *     - Generate ISCC code using an AWS Lambda function (not implemented yet)
 *
 * </p>
 *
 * @Since 1.0
 * @Author Chris Sekas
 * @Version 1.0
 */
package dev.ctrlspace.provenai.iscc;