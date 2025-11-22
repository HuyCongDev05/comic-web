import {axiosClient} from '@comics/shared';

const MemberApi = {
    getAllMember: () => {
        return axiosClient.get('/members');
    }
};

export default MemberApi;
