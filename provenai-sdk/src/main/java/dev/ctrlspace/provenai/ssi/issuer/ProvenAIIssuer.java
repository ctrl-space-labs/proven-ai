package dev.ctrlspace.provenai.ssi.issuer;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.ctrlspace.provenai.ssi.converters.AgentIDConverter;
import dev.ctrlspace.provenai.ssi.converters.PermissionOfUseConverter;
import dev.ctrlspace.provenai.ssi.model.vc.CredentialSubject;
import dev.ctrlspace.provenai.ssi.model.vc.VerifiableCredential;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.AIAgentCredentialSubject;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.PermissionOfUseCredentialSubject;
import dev.ctrlspace.provenai.utils.ContinuationObjectUtils;
import id.walt.credentials.vc.vcs.W3CVC;
import id.walt.crypto.keys.Key;
import kotlin.coroutines.Continuation;
import kotlinx.serialization.json.JsonElement;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
/**
 * ProvenAIIssuer class is used to generate and sign Verifiable Credentials for AI Agents.
 */
public class ProvenAIIssuer {

    /**
     * Generate a W3CVC object from a VerifiableCredential object.
     *
     * @param vc VerifiableCredential object
     * @return W3CVC standard credential object
     * @throws JSONException
     * @throws JsonProcessingException
     */
    public W3CVC generateUnsignedVC(VerifiableCredential<? extends CredentialSubject> vc) throws JSONException, JsonProcessingException {
        W3CVC w3cVC = new W3CVC();

        if (vc.getCredentialSubject() instanceof AIAgentCredentialSubject) {
            AgentIDConverter agentIDConverter = new AgentIDConverter();
            VerifiableCredential<AIAgentCredentialSubject> agentIdVC = (VerifiableCredential<AIAgentCredentialSubject>) vc;
            w3cVC = agentIDConverter.convertToW3CVC(agentIdVC);
        } else if (vc.getCredentialSubject() instanceof PermissionOfUseCredentialSubject) {
            PermissionOfUseConverter permissionOfUseConverter = new PermissionOfUseConverter();
            VerifiableCredential<PermissionOfUseCredentialSubject> permissionOfUseVC = (VerifiableCredential<PermissionOfUseCredentialSubject>) vc;
            w3cVC = permissionOfUseConverter.convertToW3CVC(permissionOfUseVC);
        }

        return w3cVC;
    }



    /**
     * Sign a W3CVC object with an issuer key.
     *
     * @param w3cVC The unsigned VC object
     * @param issuerKey The key of the issuer signing the VC
     * @param issuerDid The DID of the issuer signing the VC
     * @param subjectDid The DID of the subject of the VC
     * @return The signed verifiable credential in JWS format
     */
    public Object generateSignedVCJwt(W3CVC w3cVC, Key issuerKey, String issuerDid, String subjectDid) {

        Continuation<? super Object> continuationSuper = ContinuationObjectUtils.createSuperContinuation();

        Map<String, String> additionalJwtHeaders = new HashMap<>(); // Provide additional JWT headers if needed
        Map<String, JsonElement> additionalJwtOptions = new HashMap<>(); // Provide additional JWT options if needed

        return w3cVC.signJws(issuerKey, issuerDid, subjectDid,additionalJwtHeaders ,additionalJwtOptions, continuationSuper);
    }


//    /**
//     * Generate a signed Verifiable Credential from a VerifiableCredential object.
//     *
//     * @param vc The VC to be generated and signed
//     * @param issuerKey The key of the issuer signing the VC
//     * @param additionalSignVCParams  Additional parameters to sign the VC like JWT headers and options
//     * @return The signed verifiable credential in JWS format
//     * @throws JSONException
//     * @throws JsonProcessingException
//     */
//
//    public Object generateSignedVC(VerifiableCredential<? extends CredentialSubject> vc, Key issuerKey, AdditionalSignVCParams additionalSignVCParams) throws JSONException, JsonProcessingException {
//        W3CVC w3cVC = this.generateUnsignedVC(vc);
//        return this.sign(w3cVC, issuerKey, vc.getIssuer(), vc.getCredentialSubject().getId(), additionalSignVCParams);
//
//    }

}
