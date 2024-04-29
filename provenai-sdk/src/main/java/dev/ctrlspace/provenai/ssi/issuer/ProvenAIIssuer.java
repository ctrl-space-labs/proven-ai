package dev.ctrlspace.provenai.ssi.issuer;

import ch.qos.logback.core.net.ObjectWriter;
import com.fasterxml.jackson.core.JsonProcessingException;
import dev.ctrlspace.provenai.ssi.converters.AgentIDConverter;
import dev.ctrlspace.provenai.ssi.model.vc.AdditionalSignVCParams;
import dev.ctrlspace.provenai.ssi.model.vc.CredentialSubject;
import dev.ctrlspace.provenai.ssi.model.vc.VerifiableCredential;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.AIAgentCredentialSubject;
import dev.ctrlspace.provenai.utils.ContinuationObjectUtils;
import id.walt.credentials.vc.vcs.W3CVC;
import id.walt.crypto.keys.Key;
import kotlin.coroutines.Continuation;
import kotlinx.serialization.json.JsonElement;
import kotlinx.serialization.json.JsonObject;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class ProvenAIIssuer {


    public W3CVC generateUnsignedVC(VerifiableCredential<? extends CredentialSubject> vc) throws JSONException, JsonProcessingException {

        AgentIDConverter agentIDConverter = new AgentIDConverter();
        W3CVC w3cVC = new W3CVC();
        if (vc.getCredentialSubject() instanceof AIAgentCredentialSubject) {
            //cast param to VerifiableCredential<AIAgentCredentialSubject>
            VerifiableCredential<AIAgentCredentialSubject> agentIdVC = (VerifiableCredential<AIAgentCredentialSubject>) vc;

            w3cVC = agentIDConverter.convertToW3CVC(agentIdVC);
        }
        return w3cVC;
    }


    public Object sign(W3CVC w3cVC, Key issuerKey, String issuerDid, String subjectDid, AdditionalSignVCParams additionalSignVCParams) {

        Continuation<? super Object> continuationSuper = ContinuationObjectUtils.createSuperContinuation();

        return w3cVC.signJws(issuerKey, issuerDid, subjectDid, additionalSignVCParams.getAdditionalJwtHeaders(), additionalSignVCParams.getAdditionalJwtOptions(), continuationSuper);
    }





    public Object generateSignedVC(VerifiableCredential<? extends CredentialSubject> vc, Key issuerKey, AdditionalSignVCParams additionalSignVCParams) throws JSONException, JsonProcessingException {
        W3CVC w3cVC = this.generateUnsignedVC(vc);
        return this.sign(w3cVC, issuerKey, vc.getIssuer(), vc.getCredentialSubject().getId(), additionalSignVCParams);

    }

}
