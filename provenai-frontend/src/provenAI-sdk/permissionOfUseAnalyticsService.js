import axios from "axios";
import apiRequests from "src/configs/apiRequest.js";


/**
 *
 * @param {AuditPermissionOfUseCriteria} analyticsCriteria
 * @param token
 * @returns {Promise<axios.AxiosResponse<Array<AuditPermissionOfUse>>>}
 *
 */
const getAnalyticsByCriteria = async (analyticsCriteria, token) => {
    // add criteria as params in the url
    return axios.get(apiRequests.getPermissionOfUseAnalytics(), {
        headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token,
        },

        params: analyticsCriteria

    });
};

export default {
    getAnalyticsByCriteria,
}
