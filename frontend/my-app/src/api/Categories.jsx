import axiosClient from "./axiosClient";

const CategoriesApi = {
    getCategories: () => {
        return axiosClient.get('/categories');
    },
}
export default CategoriesApi;