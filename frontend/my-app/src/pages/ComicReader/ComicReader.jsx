import styles from "./ComicReader.module.css";
import {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import {Notification, ReusableButton, useAuth} from "@comics/shared";
import ComicApi from "../../api/Comic";
import BackToTop from "../../components/Button/BackToTop/BackToTop";

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
  const [comicUuid, setComicUuid] = useState();
  
  useEffect(() => {
    const fetchImageChapter = async () => {
      try {
        const resImage = await ComicApi.getImageChapter(chapter_uuid);
        if (!resImage.data) return navigate('*');

        const originName = resImage.data.origin_name;
        const resComicDetail = await ComicApi.getComicDetail(originName);
        if (user) {
          const resFollowComic = await ComicApi.getFollowComic(user.uuid);
          setComicFollowList(resFollowComic.data);
        }
        setComicDetail(resComicDetail.data.chapters || []);
        setComicUuid(resComicDetail.data.uuid);
        setImageChapter(resImage.data.chapters || []);
        setCurrentChapter(String(resImage.data.chapter_number));
      } catch {
        navigate('*');
      }
    };

    fetchImageChapter();
  }, [chapter_uuid, navigate]);

  useEffect(() => {
    if (ComicFollowList.length && ComicDetail.length) {
      const isFollowed = ComicFollowList.some(
        comic => comic.origin_name === ComicDetail[0].origin_name
      );
      setCheckFollow(isFollowed);
    }
  }, []);


  const reversedChapters = Array.isArray(ComicDetail) ? [...ComicDetail].reverse() : [];
  const currentIndex = reversedChapters.findIndex(
    (ch) => String(ch.chapter) === currentChapter
  );


  const handleFollow = () => {
    if (!user) {
      setNotification({
        key: Date.now(),
        success: false,
        title: "Yêu cầu thất bại !!!",
        message: "Bạn chưa đăng nhập",
      });
    } else {
      const fetchFollowComic = async () => {
        try {
          const resFollowComic = await ComicApi.followComic({ accountUuid: user.uuid, comicUuid: comicUuid})
          if (resFollowComic.data) {
            setCheckFollow(true);
            setNotification({
              key: Date.now(),
              success: true,
              title: "Yêu cầu thành công !!!",
              message: "Theo dõi thành công",
            });
          }
        } catch {
          setNotification({
            key: Date.now(),
            success: false,
            title: "Yêu cầu thất bại !!!",
            message: "Đã có lỗi, vui lòng báo cáo admin",
          });
        }
      }
      fetchFollowComic();
    }
  };

  const handleUnFollow = () => {
    const fetchFollowComic = async () => {
      try {
        const resFollowComic = await ComicApi.unfollowComic({ accountUuid: user.uuid, comicUuid: comicUuid })
        if (resFollowComic.data) {
          setCheckFollow(false);
          setNotification({
            key: Date.now(),
            success: true,
            title: "Yêu cầu thành công !!!",
            message: "Hủy theo dõi thành công",
          });
        }
      } catch (error) {
        console.error("FollowComic error:", error.response || error);
        setNotification({
          key: Date.now(),
          success: false,
          title: "Yêu cầu thất bại !!!",
          message: "Đã có lỗi, vui lòng báo cáo admin",
        });
      }
    }
    fetchFollowComic();
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
          text="Chap trước"
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
          text="Chap sau"
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
              style={{opacity: currentIndex >= reversedChapters.length - 1 ? 0.5 : 1}}
              onClick={() => {
                  if (currentIndex >= reversedChapters.length - 1) return;
                  const prev = reversedChapters[currentIndex + 1];
                  setCurrentChapter(String(prev.chapter));
                  window.location.href = `/chapter/${prev.chapter_uuid}`;
              }}
          ><i className="fi fi-rr-arrow-small-left"></i></button>

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
              style={{opacity: currentIndex <= 0 ? 0.5 : 1}}
              onClick={() => {
                  if (currentIndex <= 0) return;
                  const next = reversedChapters[currentIndex - 1];
                  setCurrentChapter(String(next.chapter));
                  window.location.href = `/chapter/${next.chapter_uuid}`;
              }}
          ><i className="fi fi-rr-arrow-small-right"></i></button>
          {!checkFollow ? (
              <button className={styles.followBtn} onClick={handleFollow}><i className="fi fi-rr-heart"></i> Theo dõi
            </button>
        ) : (
            <button className={styles.followBtn} onClick={handleUnFollow}><i className="fi fi-rr-heart"></i> Đang theo
                dõi</button>
        )}
      </div>
        <BackToTop />
    </div>
  );
}