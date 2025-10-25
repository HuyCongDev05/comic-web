import axiosClient from "./axiosClient"

const message = {
    getMessages: (account_uuid) => {
        return axiosClient.get('messages', {
            params:{account_uuid},
        })
    },

    getComments: () => {
        return axiosClient.get('comments');
    }
}
export default message;