import axios from 'axios';

const axiosCrawl = axios.create({
    baseURL: 'http://localhost:8000/api/v1/dashboard', // bỏ http://localhost:8000 khi mở tunnel
    headers: {
        'Content-Type': 'application/json',
    },
    withCredentials: true,
});

axiosCrawl.interceptors.request.use((config) => {
    const accessToken = localStorage.getItem('accessToken');
    if (config.requireAuth && accessToken) {
        config.headers['Authorization'] = `Bearer ${accessToken}`;
    }
    return config;
});

axiosCrawl.interceptors.response.use(
    (response) => response.data,
    (error) => {
        console.error('API Error:', error);
        throw error;
        //return Promise.reject(error);
    }
);

export default axiosCrawl;