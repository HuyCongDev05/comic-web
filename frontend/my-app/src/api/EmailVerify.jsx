import {axiosClient} from "@comics/shared";

const EmailVerifyApi = {
    SendOtp: (data) => {
        return axiosClient.post('auth/email/send-otp', data);
    },
    Verify: (data) => {
        return axiosClient.post('auth/email/verify', data);
    }
}
export default EmailVerifyApi;