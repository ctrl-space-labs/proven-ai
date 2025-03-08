import axios from "axios";
import apiRequests from "src/configs/apiRequest.js";

/**
 * Get all proven AI organization by organizationId
 * @param String policyTypeName
 * @param token
 * @returns {Promise<axios.AxiosResponse<List<getPolicyOptions>>}
 */
const getPolicyOptions = async (policyTypeName, token) => {
  return axios.get(apiRequests.getPolicyOptions(policyTypeName), {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    },

  });
};




export default {
  getPolicyOptions
};
