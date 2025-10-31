import axiosClient from "./axiosClient";

const ComicApi = {
    getNewComics: (page) => {
        return axiosClient.get('/comics/new', {
            params: { page },
        });
    },

    getNewUpdateComics: (page) => {
        return axiosClient.get('/comics/new-update', {
            params: { page },
        });
    },

    getCompletedComics: (page) => {
        return axiosClient.get('/comics/completed', {
            params: { page },
        });
    },

    getComicDetail: (originName) => {
        return axiosClient.get('/comics', {
            params: {originName}
        })
    },

    getImageChapter: (uuid) => {
        return axiosClient.get(`/comics/chapters/${uuid}`);
    },

    getFollowComic: (account_uuid) => {
        return axiosClient.get('/comics/follows', {
            params: { account_uuid },
            requireAuth: true
        })
    },

    followComic: (data) => {
        return axiosClient.post('/comics/follows', data,
            { requireAuth: true }
        );
    },

    unfollowComic: (data) => {
        return axiosClient.post('/comics/unfollows', data,
            { requireAuth: true }
        )
    },

    searchComics: (keyword, page) => {
        return axiosClient.get('/comics/search', {
            params: { keyword , page}
        });
    },

    searchComicsByCategories: (categories, page) => {
        return axiosClient.get(`/comics/${categories}`, {
            params: { page }
        });
    }
};

export default ComicApi;