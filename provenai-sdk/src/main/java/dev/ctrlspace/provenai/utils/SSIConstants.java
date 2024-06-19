package dev.ctrlspace.provenai.utils;

public class SSIConstants {

    public static final String WALT_ID_ISSUER_API = "http://localhost:7002/openid4vc/jwt/issue";

    public static final String WALT_ID_VERIFIER_API = "http://localhost:7003/openid4vc/verify";

    public static final String VERIFIER_SUCCESS_URL = "https://dev.gendox.ctrlspace.dev/verifier/success?id=$id";

    public static final String VERIFIER_ERROR_URL = "https://dev.gendox.ctrlspace.dev/verifier/error?id=$id";

//    public static final String VERIFIER_SUCCESS_URL = "https://example.com/success?id=$id";
//
//    public static final String VERIFIER_ERROR_URL = "https://example.com/error?id=$id";


}
