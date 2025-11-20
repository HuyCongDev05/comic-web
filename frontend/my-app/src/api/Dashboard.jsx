import axiosAdmin from "./AxiosAdmin.jsx";

const Dashboard = {
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
    }
}
export default Dashboard;