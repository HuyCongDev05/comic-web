import axiosClient from "./AxiosClient.jsx";

const AccountApi = {
    login: (data) => {
        return axiosClient.post('/auth/login', data,
            { withCredentials: true }
        );
    },

    loginGoogle: (data) => {
        return axiosClient.post('/auth/google', data,);
    },

    loginFacebook: (data) => {
        return axiosClient.post('/auth/facebook', data,)
    },

    logout: () => {
        return axiosClient.get('/auth/logout', {
            requireAuth: true,
            withCredentials: true
        });
    },

    register: (data) => {
        return axiosClient.post('auth/register', data);
    },

    refreshToken: () => {
        return axiosClient.get('auth/token/refresh',
            { withCredentials: true }
        );
    },

    followComic: (account_uuid) => {
        return axiosClient.get('/comics/follows', {
            params: {account_uuid},
            requireAuth: true
        });
    },

    updateAccount: (data, account_uuid) => {
        return axiosClient.put('/users', data, {
            params: {uuid: account_uuid},
            requireAuth: true
        });
    },

    saveHistory: (data) => {
        return axiosClient.post('accounts/history', data, {
                requireAuth: true
            }
        );
    },

    history: (data) => {
        return axiosClient.get('comics/history', {
            params: data,
            requireAuth: true
        })
    }
}
export default AccountApi;