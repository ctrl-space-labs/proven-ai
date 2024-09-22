import axios from "axios";
import apiRequests from "src/configs/apiRequest.js";

/**
 * getGendoxUser
    * @param {string} userId
    * @param {string} storedToken
    * @returns {Promise<axios.AxiosResponse<GendoxUser>>}
    * 
 
 */
const getGendoxUser = async (userId, storedToken) => {
    return axios.get(apiRequests.getGendoxUser(userId), {
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + storedToken,
      },
    });
  }
  
  
  export default {
    getGendoxUser,
  };