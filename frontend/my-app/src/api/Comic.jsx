import axiosClient from "./axiosClient";

const ComicApi = {
    getNewComics: (page = 1) => {
        return axiosClient.get('/new-comics', {
            params: { page },
        });
    },

    getNewUpdateComics: (page = 1) => {
        return axiosClient.get('/new-update-comics', {
            params: { page },
        });
    },

    getCompletedComics: (page = 1) => {
        return axiosClient.get('/completed-comics', {
            params: { page },
        });
    },
};

export default ComicApi;