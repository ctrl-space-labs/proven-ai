import axios from "axios";
import apiRequests from "src/configs/apiRequest.js";

/**
 * getGendoxUser
    * @param {string} userId
    * @param {string} token
    * @returns {Promise<axios.AxiosResponse<GendoxUser>>}
    *

 */
const getGendoxUser = async (userId, token) => {
    return axios.get(apiRequests.getGendoxUser(userId), {
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + token,
      },
    });
  }


  export default {
    getGendoxUser,
  };
