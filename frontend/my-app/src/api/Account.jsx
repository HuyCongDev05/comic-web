import axiosClient from "./axiosClient";

const AccountApi = {
    Login: (data) => {
        return axiosClient.post('/auth/login', data)
    },
    Register: (data) => {
        return axiosClient.post('auth/register', data)
    }
}
export default AccountApi;