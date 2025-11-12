import axiosClient from './AxiosClient.jsx';

const MemberApi = {
    getAllMember: () => {
        return axiosClient.get('/members');
    }
};

export default MemberApi;
