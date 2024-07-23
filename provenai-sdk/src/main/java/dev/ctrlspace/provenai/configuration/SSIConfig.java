package dev.ctrlspace.provenai.configuration;

public class SSIConfig {

    // Utility method to get environment variable
    public static String getEnvVar(String key) {
        return System.getenv(key);
    }

    // Method to get the Issuer DID environment variable
    public static String getIssuerDid() {
        return getEnvVar("ISSUER_DID");
    }

    // Method to get the Issuer Private JWK environment variable
    public static String getIssuerPrivateJwk() {
        return getEnvVar("ISSUER_PRIVATE_JWK");
    }

}
