import {axiosClient} from "@comics/shared";

const CategoriesApi = {
    getCategories: () => {
        return axiosClient.get('/categories');
    },
}
export default CategoriesApi;