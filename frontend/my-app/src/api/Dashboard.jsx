import axiosClient from "./AxiosClient.jsx";

const Dashboard = {
    home: () => {
        return axiosClient.get('/dashboard/home', {
            requireAuth: true
        });
    },
}
export default Dashboard;