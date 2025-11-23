import {axiosClient} from "@comics/shared";

const Dashboard = {

    home: () => {
        return axiosClient.get('/dashboard/home', {
            requireAuth: true
        });
    },

    crawl: () => {
        return axiosClient.get('/dashboard/crawl', {
            requireAuth: true
        });
    },

    crawlBySlug:(originName) => {
        return axiosClient.get(`/dashboard/crawl/${originName}`, {
            requireAuth: true
        });
    },

    listAccounts: (page) => {
        return axiosClient.get('/dashboard/accounts', {
            params: { page },
            requireAuth: true
        });
    },

    updateStatusAccounts: (account_uuid, status) => {
        return axiosClient.post('/dashboard/accounts', { status }, {
            params: { account_uuid },
            requireAuth: true
        });
    },

    searchAccounts: (keyword, page) => {
        return axiosClient.get('dashboard/accounts/search', {
            params: { keyword, page },
            requireAuth: true
        });
    }
}
export default Dashboard;