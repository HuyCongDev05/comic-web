import axiosClient from "./axiosClient";

const AccountApi = {
    login: (data) => {
        return axiosClient.post('/auth/login', data,
            { withCredentials: true }
        );
    },

    logout: () => {
        return axiosClient.get('/auth/logout',
            { requireAuth: true },
            {withCredentials: true}
        );  
    },

    register: (data) => {
        return axiosClient.post('auth/register', data);
    },

    refreshToken: () => {
        return axiosClient.get('auth/token/refresh',
            { withCredentials: true }
        );
    }
}
export default AccountApi;