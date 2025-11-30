import { axiosClient } from "@comics/shared";
const Comics = {
    getComics: (page = 1, type = "default") => {
        const endpointMap = {
            default: "/dashboard/comics",
            update: "/dashboard/comics/update",
            completed: "/dashboard/comics/completed"
        };

        return axiosClient.get(endpointMap[type], {
            params: { page },
            requireAuth: true
        });
    },

    updateComicStatus: (uuidComics, status) => {
        return axiosClient.post('/dashboard/comics', {
            uuidComics,
            status
        }, {
            requireAuth: true
        });
    }

};

export default Comics;