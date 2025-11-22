import {axiosClient} from "@comics/shared"

const message = {
    postComments: (data, comicUuid) => {
        return axiosClient.post(`comics/${comicUuid}/comments`, data,{
            requireAuth: true
        });
        
    },

    getComments: (data) => {
        return axiosClient.get(`/comics/${data}/comments`);
    },

    saveMessage: (data) => {

    }
}
export default message;