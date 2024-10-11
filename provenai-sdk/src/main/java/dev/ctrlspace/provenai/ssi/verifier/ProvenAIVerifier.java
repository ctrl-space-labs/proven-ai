package dev.ctrlspace.provenai.ssi.verifier;

import id.walt.credentials.verification.models.PolicyRequest;
import id.walt.credentials.verification.models.PresentationVerificationResponse;
import id.walt.credentials.verification.policies.ExpirationDatePolicy;
import id.walt.credentials.verification.policies.JwtSignaturePolicy;
import id.walt.credentials.verification.policies.NotBeforeDatePolicy;
import id.walt.credentials.verification.policies.vp.HolderBindingPolicy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Class to verify a presentation against a set of policies
 */
public class ProvenAIVerifier {

    public Boolean verifyVPJwt(String vpJwt) throws InterruptedException, ExecutionException {
//      Initialize presentationVerifier
        PresentationVerifier presentationVerifier = new PresentationVerifier();

//        Initialize Policies to be checked
        HolderBindingPolicy holderBindingPolicy = new HolderBindingPolicy();
        JwtSignaturePolicy jwtSignaturePolicy = new JwtSignaturePolicy();
        NotBeforeDatePolicy notBeforeDatePolicy = new NotBeforeDatePolicy();
        ExpirationDatePolicy expirationDatePolicy = new ExpirationDatePolicy();
//        pass null for args
//      Initialize Policy Types
        List<PolicyRequest> vpPolicies = new ArrayList<>();
        List<PolicyRequest> globalVcPolicies = new ArrayList<>();
        HashMap<String, List<PolicyRequest>> specificCredentialPolicies = new HashMap<>();
        HashMap<String, Object> presentationContext = new HashMap<>();

        vpPolicies.add(new PolicyRequest(holderBindingPolicy, null));
        vpPolicies.add(new PolicyRequest(holderBindingPolicy, null));

        globalVcPolicies.add(new PolicyRequest(expirationDatePolicy, null));
        globalVcPolicies.add(new PolicyRequest(notBeforeDatePolicy, null));
//        globalVcPolicies.add(new PolicyRequest(jwtSignaturePolicy, null));


        PresentationVerificationResponse verificationResponse = presentationVerifier.verifyPresentationBlocking(vpJwt, vpPolicies,
                globalVcPolicies, specificCredentialPolicies, presentationContext);

 ;

        return verificationResponse.overallSuccess();
    }
}
