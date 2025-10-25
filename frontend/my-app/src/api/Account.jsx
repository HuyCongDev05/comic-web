import axiosClient from "./axiosClient";

const AccountApi = {
    login: (data) => {
        return axiosClient.post('/auth/login', data);
    },

    register: (data) => {
        return axiosClient.post('auth/register', data);
    },

    refreshToken: () => {
        return axiosClient.get('/token/refresh');
    }
}
export default AccountApi;