import axios from "axios";

const avatarApi = {
    UploadAvatar: async (file) => {
        const CLOUD_NAME = process.env.REACT_APP_CLOUD_NAME;
        const formData = new FormData();
        formData.append("file", file);
        formData.append("upload_preset", "avatar_preset");

        const response = await axios.post(
            `https://api.cloudinary.com/v1_1/${CLOUD_NAME}/upload`,
            formData,
            {
                headers: {
                    "Content-Type": "multipart/form-data",
                },
            }
        );
        return response.data;
    },
};

export default avatarApi;
