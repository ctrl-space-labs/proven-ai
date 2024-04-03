package dev.ctrlspace.provenai.ssi;

import id.walt.credentials.verification.Verifier;
import id.walt.credentials.verification.models.PolicyRequest;
import kotlin.Result;
import kotlin.coroutines.Continuation;

import java.util.List;
import java.util.Map;

public class CredentialVerifier {


        public static void verifyVerifiableCredential(String signedCredential,
                                            List<PolicyRequest> policyRequests,
                                            Map<String, Object> context,
                                            Continuation<? super Object> continuation) {
            Verifier.INSTANCE.verifyCredential(signedCredential, policyRequests, context, continuation);
        }


    public static void verifyPresentation(String presentationJwt,
                                          List<PolicyRequest> vpPolicies,
                                          List<PolicyRequest> globalVcPolicies,
                                          Map<String, List<PolicyRequest>> specificCredentialPolicies,
                                          Map<String, Object> presentationContext,
                                          Continuation<? super Object> continuation) {
        Verifier.INSTANCE.verifyPresentation(presentationJwt, vpPolicies, globalVcPolicies, specificCredentialPolicies, presentationContext, continuation);
    }



}
