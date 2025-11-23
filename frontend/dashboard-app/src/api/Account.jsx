import { axiosClient } from "@comics/shared";
const AccountApi = {
    me: () => {
        return axiosClient.get('/auth/me', {
            requireAuth: true
        });
    },
    
    login: (data) => {
        return axiosClient.post('/auth/login', data,
            { withCredentials: true }
        );
    },

    logout: () => {
        return axiosClient.get('/auth/logout', {
            requireAuth: true,
            withCredentials: true
        });
    },

    refreshToken: () => {
        return axiosClient.get('auth/token/refresh',
            {
                withCredentials: true,
                requireAuth: true
            }
        );
    },
}
export default AccountApi;