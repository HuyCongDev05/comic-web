import axiosAdmin from "./AxiosAdmin.jsx";

const Dashboard = {
    home: () => {
        return axiosAdmin.get('/dashboard/home', {
            requireAuth: true
        });
    },
}
export default Dashboard;