import axiosAdmin from "./AxiosAdmin.jsx";

const Dashboard = {
    me: () => {
        return axiosAdmin.get('/auth/me', {
            requireAuth: true
        });
    },

    refreshToken: () => {
        return axiosAdmin.get('auth/token/refresh',
            { withCredentials: true }
        );
    },

    home: () => {
        return axiosAdmin.get('/dashboard/home', {
            requireAuth: true
        });
    },

    crawl: () => {
        return axiosAdmin.get('/dashboard/crawl', {
            requireAuth: true
        })
    },

    crawlBySlug:(originName) => {
        return axiosAdmin.get(`/dashboard/crawl/${originName}`, {
            requireAuth: true
        })
    },

    listAccounts: (page) => {
        return axiosAdmin.get('/dashboard/accounts', {
            params: { page },
            requireAuth: true
        });
    },

    updateStatusAcocunts: (account_uuid, status) => {
        return axiosAdmin.post('/dashboard/accounts',{ status },{
            params: { account_uuid },
            requireAuth: true
        })
    }
}
export default Dashboard;