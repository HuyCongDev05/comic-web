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

    getComicDetail: (originName) => {
        return axiosClient.get('/name-comics', {
            params: {originName}
        })
    },

    getImageChapter: (uuid) => {
        return axiosClient.get(`/chapter/${uuid}`);
    },

    GetFollowComic: (uuid) => {
        return axiosClient.get('follow', {
            params: {uuid}
        })
    }
};

export default ComicApi;