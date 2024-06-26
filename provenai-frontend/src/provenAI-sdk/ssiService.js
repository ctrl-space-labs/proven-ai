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
    let credentialType = verifiedVP.tokenResponse.presentation_submission.descriptor_map[0].id;

    return credentialType;
};


const getVerifiedVcJwtPayload = (verifiedVP) => {


    return jwt.decode(getVerifiedVcSignaturePolicy(verifiedVP).vp.verifiableCredential[0]);
};

const getVerifiedVcCredentialSubject = (verifiedVP) => {

    return getVerifiedVcJwtPayload(verifiedVP).vc.credentialSubject;
}

/**
   * Handle successful VC offer
   *
   * @param vcOfferSessionId
   * @return {Promise<boolean>}   true if VC offer flow completed successfully
   */
const handleVcOfferFlow = async (vcOfferSessionId) => {
    let offeredVP = await getVcOffered(vcOfferSessionId);
    if (offeredVP.data.policyResults.success !== true) {
      throw new Error("VC offer failed");
    }
    // offeredVP.data.policyResults -> this is an array. we are looking for the element that has value .credential === "VerifiablePresentation"
    // the in this element, has a array 'policies', we are looking for the element that has value .policy === "signature"
    let organizationDid = getVerifiedVcSignaturePolicy(
      offeredVP.data
    ).sub;
    let vcCredentialSubject = getVerifiedVcCredentialSubject(
      offeredVP.data
    );
    let vcCredentialType = getVerifiedVcCredentialType(offeredVP.data);
    console.log("credentialType", vcCredentialType);
    console.log("VC CredentialSubject: ", vcCredentialSubject);
    console.log("organizationDid", organizationDid);

    return { offeredVP, organizationDid, vcCredentialSubject, vcCredentialType}
  };


export default {
    getVcOffered,
    getVerifiedVcSignaturePolicy,
    getVerifiedVcCredentialType,
    getVerifiedVcJwtPayload,
    getVerifiedVcCredentialSubject,
    handleVcOfferFlow
};
