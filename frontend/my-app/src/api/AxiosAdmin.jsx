import axios from 'axios';

const axiosAdmin = axios.create({
    baseURL: 'http://localhost:8080/api/v1', // bỏ http://localhost:8080 khi mở tunnel
    headers: {
        'Content-Type': 'application/json',
    },
    withCredentials: true,
});

axiosAdmin.interceptors.request.use((config) => {
    const accessTokenAdmin = localStorage.getItem('accessTokenAdmin');
    if (config.requireAuth && accessTokenAdmin) {
        config.headers['Authorization'] = `Bearer ${accessTokenAdmin}`;
    }
    return config;
});

axiosAdmin.interceptors.response.use(
    (response) => response.data,
    (error) => {
        console.error('API Error:', error);
        throw error;
        //return Promise.reject(error);
    }
);

export default axiosAdmin;