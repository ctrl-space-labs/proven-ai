import axios from "axios";
import apiRequests from "src/configs/apiRequest.js";

/**
 * Get all proven AI organization by organizationId
 * @param String policyTypeName
 * @param storedToken
 * @returns {Promise<axios.AxiosResponse<List<getPolicyOptions>>}
 */
const getPolicyOptions = async (policyTypeName, storedToken) => {  
  return axios.get(apiRequests.getPolicyOptions(policyTypeName), {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + storedToken,
    },
    // params: {
    //   policyTypeName: policyTypeName
    // },
    
  });
};




export default {
  getPolicyOptions
};
