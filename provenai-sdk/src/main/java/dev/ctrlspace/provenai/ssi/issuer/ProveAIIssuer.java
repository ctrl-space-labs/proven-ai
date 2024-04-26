package dev.ctrlspace.provenai.ssi.issuer;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.ctrlspace.provenai.ssi.converters.AgentIDConverter;
import dev.ctrlspace.provenai.ssi.model.vc.CredentialSubject;
import dev.ctrlspace.provenai.ssi.model.vc.VerifiableCredential;
import dev.ctrlspace.provenai.ssi.model.vc.attestation.AIAgentCredentialSubject;
import id.walt.credentials.vc.vcs.W3CVC;
import org.json.JSONException;

public class ProveAIIssuer {


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

//    public W3CVC sign(W3CVC w3cVC) {
//        return w3cVC.signJws(.....);
//    }

//    public W3CVC generateSignedVC(VerifiableCredential<? extends CredentialSubject> vc) throws JSONException, JsonProcessingException {
//        W3CVC w3cVC = this.generateUnsignedVC(vc);
//        return this.sign(w3cVC);
//
//    }

}
