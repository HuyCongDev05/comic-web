import axiosClient from "./axiosClient";

const CategoryApi = {
    getCategories: () => {
        return axiosClient.get('/categories');
    },
}
export default CategoryApi;