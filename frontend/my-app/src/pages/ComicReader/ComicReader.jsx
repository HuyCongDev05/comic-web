import styles from "./ComicReader.module.css";
import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import ReusableButton from "./../../components/Button/Button";
import ComicApi from "../../api/Comic";
import BackToTop from "../../components/Button/BackToTop/BackToTop";
import Notification from "../../components/Notification/Notification";
import { useAuth } from "../../context/AuthContext";

export default function ComicReader() {
  const {user} = useAuth();
  const { chapter_uuid } = useParams();
  const navigate = useNavigate('');
  const [imageChapter, setImageChapter] = useState([]);
  const [currentChapter, setCurrentChapter] = useState('');
  const [ComicDetail, setComicDetail] = useState([]);
  const [ComicFollowList, setComicFollowList] = useState([]);
  const [checkFollow, setCheckFollow] = useState(false);
  const [notification, setNotification] = useState(false);
  
  useEffect(() => {
    const fetchImageChapter = async () => {
      try {
        const resImage = await ComicApi.getImageChapter(chapter_uuid);
        if (!resImage.data) return navigate('*');

        const originName = resImage.data.origin_name;
        const resComicDetail = await ComicApi.getComicDetail(originName);
        if (user) {
          const resFollowComic = await ComicApi.GetFollowComic(user.uuid);
          setComicFollowList(resFollowComic.data);
        }
        setComicDetail(resComicDetail.data.chapters || []);
        setImageChapter(resImage.data.chapters || []);
        setCurrentChapter(String(resImage.data.chapter_number));
      } catch {
        navigate('*');
      }
    };

    fetchImageChapter();
  }, [chapter_uuid, navigate]);

  const reversedChapters = Array.isArray(ComicDetail) ? [...ComicDetail].reverse() : [];
  const currentIndex = reversedChapters.findIndex(
    (ch) => String(ch.chapter) === currentChapter
  );

  const isFollowed = ComicFollowList.some(comic => comic.uuid === ComicDetail.uuid);
  if (isFollowed) {
    setCheckFollow(true);
  }

  const handleFollow = () => {
    if (!user) {
      setNotification({
        key: Date.now(),
        success: false,
        title: "Yêu cầu thất bại !!!",
        message: "Bạn chưa đăng nhập",
      });
      return;
    }
    setCheckFollow(!checkFollow);
  };

  return (
    <div className={styles.readerContainer}>
      {notification && (
        <Notification
          key={notification.key}
          success={notification.success}
          title={notification.title}
          message={notification.message}
        />
      )}
      <div className={styles.topNav}>
        <ReusableButton
          text="⬅ Chap trước"
          disabled={currentIndex >= reversedChapters.length - 1}
          onClick={() => {
            if (currentIndex >= reversedChapters.length - 1) return;
            const prev = reversedChapters[currentIndex + 1];
            setCurrentChapter(String(prev.chapter));
            window.location.href = `/chapter/${prev.chapter_uuid}`;
          }}
          style={{ opacity: currentIndex >= reversedChapters.length - 1 ? 0.5 : 1 }}
        />

        <ReusableButton
          text="Chap sau ➡"
          disabled={currentIndex <= 0}
          onClick={() => {
            if (currentIndex <= 0) return;
            const next = reversedChapters[currentIndex - 1];
            setCurrentChapter(String(next.chapter));
            window.location.href = `/chapter/${next.chapter_uuid}`;
          }}
          style={{ opacity: currentIndex <= 0 ? 0.5 : 1 }}
        />
      </div>

      <div className={styles.imageWrapper}>
        {imageChapter.map((img) => (
          <img
            key={img.image_number}
            src={img.image_url}
            alt={`page-${img.image_number}`}
            className={styles.chapterImage}
          />
        ))}
      </div>

      <div className={styles.bottomNav}>
        <button className={styles.controllerChapter} onClick={() => navigate("/home")}>
          <i className="fi fi-rs-home"></i>
        </button>

        <button
          className={styles.controllerChapter}
          disabled={currentIndex >= reversedChapters.length - 1}
          style={{ opacity: currentIndex >= reversedChapters.length - 1 ? 0.5 : 1 }}
          onClick={() => {
            if (currentIndex >= reversedChapters.length - 1) return;
            const prev = reversedChapters[currentIndex + 1];
            setCurrentChapter(String(prev.chapter));
            window.location.href = `/chapter/${prev.chapter_uuid}`;
          }}
        > ⬅ </button>

        <select
          value={currentChapter}
          onChange={(e) => {
            const selected = e.target.value;
            setCurrentChapter(selected);

            const found = ComicDetail.find(
              (ch) => String(ch.chapter) === selected
            );
            if (found) window.location.href = `/chapter/${found.chapter_uuid}`;
          }}
        >
          {reversedChapters.map((ch) => (
            <option key={ch.chapter} value={String(ch.chapter)}>
              Chương {ch.chapter}
            </option>
          ))}
        </select>

        <button
          className={styles.controllerChapter}
          disabled={currentIndex <= 0}
          style={{ opacity: currentIndex <= 0 ? 0.5 : 1 }}
          onClick={() => {
            if (currentIndex <= 0) return;
            const next = reversedChapters[currentIndex - 1];
            setCurrentChapter(String(next.chapter));
            window.location.href = `/chapter/${next.chapter_uuid}`;
          }}
        > ➡ </button>
        {!checkFollow ? (
          <button className={styles.followBtn} onClick={handleFollow}>❤️ Theo dõi</button>
        ) : (
          <button className={styles.followBtn} onClick={handleFollow}>❤️ Đang theo dõi</button>
        )}
      </div>
      <BackToTop />
    </div>
  );
}