import axios from 'axios';

const axiosClient = axios.create({
   baseURL: 'http://localhost:8080/api/v1',
   headers: {
     'Content-Type': 'application/json',
   },
});

axiosClient.interceptors.request.use((config) => {
  const accessToken = localStorage.getItem('accessToken');
  if (config.requireAuth && accessToken) {
    config.headers['Authorization'] = `Bearer ${accessToken}`;
  }
  return config;
});

axiosClient.interceptors.response.use(
  (response) => response.data,
  (error) => {
    console.error('API Error:', error);
    throw error;
    //return Promise.reject(error);
  }
);

export default axiosClient;