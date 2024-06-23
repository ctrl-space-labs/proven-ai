import axios from "axios";
import apiRequests from "../configs/apiRequest";
import jwt from 'jsonwebtoken';

/**
 * Get VC offered by user in the Verifier API using offer session id
 */
const getVcOffered = async (offerId) => {

    return axios.get(apiRequests.ssi.getVcFromOffer(offerId), {
        headers: {
            "Content-Type": "application/json",
        },

    });
};


const getVerifiedVcSignaturePolicy = (verifiedVP) => {
    let signaturePolicy = verifiedVP.policyResults?.results
        ?.filter((policy) => policy.credential === "VerifiablePresentation")[0]
        ?.policies
        ?.filter((policy) => policy.policy === "signature")[0]
        ?.result;

    return signaturePolicy;
};

const getVerifiedVcCredentialType = (verifiedVP) => {
    let credentialType = verifiedVP.presentation_submission.descriptor_map[0].id;

    return credentialType;
};


const getVerifiedVcJwtPayload = (verifiedVP) => {


    return jwt.decode(getVerifiedVcSignaturePolicy(verifiedVP).vp.verifiableCredential[0]);
};

const getVerifiedVcCredentialSubject = (verifiedVP) => {

    return getVerifiedVcJwtPayload(verifiedVP).vc.credentialSubject;
}


export default {
    getVcOffered,
    getVerifiedVcSignaturePolicy,
    getVerifiedVcCredentialType,
    getVerifiedVcJwtPayload,
    getVerifiedVcCredentialSubject
};
