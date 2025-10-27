import axiosClient from "./axiosClient"

const message = {
    getPrivateMessages: (account_uuid) => {
        return axiosClient.get('messages', account_uuid);
    },

    postComments: (data, comicUuid) => {
        return axiosClient.post(`comics/${comicUuid}/comments`, data,{
            requireAuth: true
        });
        
    },

    getComments: (data) => {
        return axiosClient.get(`/comics/${data}/comments`);
    }
}
export default message;