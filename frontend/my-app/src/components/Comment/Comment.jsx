import { useState } from "react";
import styles from "./Comment.module.css";
import ReusableButton from "./../../components/Button/Button";

export default function Comment() {
    const [comments, setComments] = useState([
        {
            id: 1,
            name: "Huy Công",
            message: "Giao diện này đẹp quá!",
            avatar: "https://i.pravatar.cc/50?img=3",
            time: "5 phút trước",
        },
        {
            id: 2,
            name: "Linh",
            message: "Màu tím gradient nhìn chill vãi 😍",
            avatar: "https://i.pravatar.cc/50?img=12",
            time: "10 phút trước",
        },
    ]);

    const [newComment, setNewComment] = useState({ name: "", message: "" });

    const handleSubmit = (e) => {
        e.preventDefault();
        if (!newComment.name.trim() || !newComment.message.trim()) return;

        const comment = {
            id: comments.length + 1,
            ...newComment,
            avatar: "https://i.pravatar.cc/50?img=" + Math.floor(Math.random() * 70),
            time: "vừa xong",
        };

        setComments([comment, ...comments]);
        setNewComment({ name: "", message: "" });
    };

    return (
        <div className={styles.container}>
            <div className={styles.commentList}>
                <h2 className={styles.title}>Bình luận</h2>
                <div className={styles.scrollArea}>
                    {comments.length === 0 ? (
                        <p className={styles.noComment}>Chưa có bình luận nào</p>
                    ) : (
                        comments.map((c) => (
                            <div key={c.id} className={styles.commentCard}>
                                <img src={c.avatar} alt="avatar" className={styles.avatar} />
                                <div>
                                    <div className={styles.name}>{c.name}</div>
                                    <div className={styles.message}>{c.message}</div>
                                    <div className={styles.time}>{c.time}</div>
                                </div>
                            </div>
                        ))
                    )}
                </div>
            </div>

            <div className={styles.commentForm}>
                <h2 className={styles.formTitle}>Viết bình luận</h2>
                <form onSubmit={handleSubmit}>
                    <textarea
                        placeholder="Lời nhắn của bạn"
                        value={newComment.message}
                        onChange={(e) =>
                            setNewComment({ ...newComment, message: e.target.value })
                        }
                        className={styles.textarea}
                    />
                    <ReusableButton className={styles.submitBtn} text="Gửi bình luận" onClick={() => console.log("Clicked")} />
                </form>
            </div>
        </div>
    );
};
