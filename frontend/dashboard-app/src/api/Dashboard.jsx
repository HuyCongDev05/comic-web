import {axiosClient} from "@comics/shared";

const Dashboard = {
    me: () => {
        return axiosClient.get('/auth/me', {
            requireAuth: true
        });
    },

    refreshToken: () => {
        return axiosClient.get('auth/token/refresh',
            { withCredentials: true }
        );
    },

    home: () => {
        return axiosClient.get('/dashboard/home', {
            requireAuth: true
        });
    },

    crawl: () => {
        return axiosClient.get('/dashboard/crawl', {
            requireAuth: true
        })
    },

    crawlBySlug:(originName) => {
        return axiosClient.get(`/dashboard/crawl/${originName}`, {
            requireAuth: true
        })
    },

    listAccounts: (page) => {
        return axiosClient.get('/dashboard/accounts', {
            params: { page },
            requireAuth: true
        });
    },

    updateStatusAcocunts: (account_uuid, status) => {
        return axiosClient.post('/dashboard/accounts',{ status },{
            params: { account_uuid },
            requireAuth: true
        })
    }
}
export default Dashboard;