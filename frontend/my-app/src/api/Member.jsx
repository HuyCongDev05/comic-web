import axiosClient from './axiosClient';

const MemberApi = {
    getAllMember: () => {
        return axiosClient.get('/members');
    }
};

export default MemberApi;
