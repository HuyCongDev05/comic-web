import axiosClient from "./AxiosClient.jsx";

const CategoriesApi = {
    getCategories: () => {
        return axiosClient.get('/categories');
    },
}
export default CategoriesApi;