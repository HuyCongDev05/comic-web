import { useEffect, useState } from "react";
import styles from "./Comment.module.css";
import ReusableButton from "./../../components/Button/Button";
import MessageApi from "../../api/Message";
import Notification from "../Notification/Notification";
import { useAuth } from "../../context/AuthContext";

export default function Comment() {
    const [comments, setComments] = useState([]);
    const { user } = useAuth();
    const [notification, setNotification] = useState(false);

    const handleComment = () => {
        if (!user) {
            setNotification({
                key: Date.now(),
                success: false,
                title: "Yêu cầu thất bại !!!",
                message: "Bạn chưa đăng nhập",
            });
            return;
        }

        if (!newComment.message.trim()) {
            setNotification({
                key: Date.now(),
                success: false,
                title: "Yêu cầu thất bại !!!",
                message: "Vui lòng nhập nội dung bình luận",
            });
            return;
        }

        // Nếu hợp lệ, tiếp tục xử lý bình luận
        // ... phần xử lý gửi bình luận của bạn ở đây ...
    };


    useEffect(() => {
        const fetchComments = async () => {
            try {
                const resComment = await MessageApi.getComments();
                if (resComment.data) {
                    setComments(resComment.data);
                }
            } catch (error) {

            }
        }
        fetchComments();
    }, []);

    function timeAgo(isoString) {
        const now = new Date();
        const past = new Date(isoString);
        const diffMs = now - past;
        const diffSec = Math.floor(diffMs / 1000);
        const diffMin = Math.floor(diffSec / 60);
        const diffHour = Math.floor(diffMin / 60);
        const diffDay = Math.floor(diffHour / 24);

        if (diffSec < 10) return "vừa xong";
        if (diffSec < 60) return `${diffSec} giây trước`;
        if (diffMin < 60) return `${diffMin} phút trước`;
        if (diffHour < 24) return `${diffHour} giờ trước`;
        if (diffDay < 7) return `${diffDay} ngày trước`;
        return past.toLocaleDateString("vi-VN");
    }

    const [newComment, setNewComment] = useState({ name: "", message: "" });


    return (
        <div className={styles.container}>
            {notification && (
                <Notification
                    key={notification.key}
                    success={notification.success}
                    title={notification.title}
                    message={notification.message}
                />
            )}

            <div className={styles.commentList}>
                <h2 className={styles.title}>Bình luận</h2>
                <div className={styles.scrollArea}>
                    {comments.length === 0 ? (
                        <p className={styles.noComment}>
                            Chưa có bình luận nào, hãy là người bình luận đầu tiên
                        </p>
                    ) : (
                        [...comments]
                            .sort((a, b) => new Date(b.time) - new Date(a.time)) // mới → cũ
                            .map((c) => (
                                <div key={c.id} className={styles.commentCard}>
                                    <img
                                        src={
                                            c.avatar ||
                                            "https://i.pinimg.com/736x/7d/b9/56/7db956d51da0e02f621e011879fcef37.jpg"
                                        }
                                        alt="avatar"
                                        className={styles.avatar}
                                    />
                                    <div>
                                        <div className={styles.name}>{c.userSend}</div>
                                        <div className={styles.message}>{c.message}</div>
                                        <div className={styles.time}>{timeAgo(c.time)}</div>
                                    </div>
                                </div>
                            ))
                    )}
                </div>
            </div>

            <div className={styles.commentForm}>
                <h2 className={styles.formTitle}>Viết bình luận</h2>
                <form onSubmit={(e) => {
                    e.preventDefault();
                    handleComment();
                }}>
                    <textarea
                        placeholder="Lời nhắn của bạn"
                        value={newComment.message}
                        onChange={(e) =>
                            setNewComment({ ...newComment, message: e.target.value })
                        }
                        className={styles.textarea}
                    />
                    <ReusableButton className={styles.submitBtn} text="Gửi bình luận" type="submit" />
                </form>
            </div>
        </div>
    );
};
