import styles from "./ComicDetail.module.css";
import ComicApi from "../../api/Comic";
import {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import BackToTop from "../../components/Button/BackToTop/BackToTop";
import {useAuth} from "../../context/AuthContext";
import Notification from "../../components/Notification/Notification";
import HideScrollbar from "../../hooks/HideScrollbar";
import Comment from "../../components/Comment/Comment";
import {useApp} from "../../context/AppContext";
import Rating from '@mui/material/Rating';
import AccountApi from "../../api/Account.jsx";

export default function ComicDetail() {
  HideScrollbar();
  const { user } = useAuth();
  const { originName } = useParams();
  const navigate = useNavigate('');
  const [ComicDetail, setComicDetail] = useState('');
  const [ComicFollowList, setComicFollowList] = useState([]);
  const [checkFollow, setCheckFollow] = useState(false);
  const [notification, setNotification] = useState(false);
  const { setSharedData } = useApp();
 
  
  useEffect(() => {
    const fetchComicDetail = async () => {
      try {
        const resComicDetail = await ComicApi.getComicDetail(originName);
        setComicDetail(resComicDetail.data);
        setSharedData({ comicUuid: resComicDetail.data.uuid });
        const resFollowComic = await ComicApi.getFollowComic(user.uuid);
        setComicFollowList(resFollowComic.data);

      } catch (error) {
        console.log(error)
      }
    };
    fetchComicDetail();
  }, [navigate, originName]);

    useEffect(() => {
        const timer = setTimeout(async () => {
            if (user) {
                try {
                    await AccountApi.saveHistory({
                        accountUuid: user.uuid,
                        comicUuid: ComicDetail.uuid
                    });
                } catch (error) {
                    console.log(error);
                }
            }
        }, 200);
        return () => clearTimeout(timer);
    }, [user, ComicDetail]);

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
          const resFollowComic = await ComicApi.followComic({ accountUuid: user.uuid, comicUuid: ComicDetail.uuid })
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
        const resFollowComic = await ComicApi.unfollowComic({ accountUuid: user.uuid, comicUuid: ComicDetail.uuid })
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

  useEffect(() => {
        if (ComicDetail?.uuid && Array.isArray(ComicFollowList)) {
            const isFollowed = ComicFollowList.some(
                comic => comic.uuid === ComicDetail.uuid
            );
            setCheckFollow(isFollowed);
        } else {
            setCheckFollow(false);
        }
    }, [ComicDetail, ComicFollowList]);

  const firstChapter = ComicDetail.chapters?.[0]?.chapter_uuid;

  return (
    <div className={styles.container}>
      <div className={styles.topSection}>
        <img
          src={ComicDetail.poster}
          alt={ComicDetail.name}
          className={styles.cover}
        />

        <div className={styles.info}>
          <h1 className={styles.title}>{ComicDetail.name}</h1>
          <div className={styles.infoRow}>
            <span className={styles.label}>Tác giả:</span>
            <span className={styles.value}>Đang cập nhật</span>
          </div>
          <div className={styles.infoRow}>
            <span className={styles.label}>Tình trạng:</span>
            <span className={styles.value}>{ComicDetail.status}</span>
          </div>

          <div className={styles.stats}>
              <div><i className="fi fi-rr-heart"></i> {ComicDetail.totalUserFollow}</div>
              <Rating
                  name="half-rating-read"
              value={ComicDetail.rating || 0}
              precision={0.1}
              readOnly
                  sx={{fontSize: 16, stroke: "#fff", padding: "3px 0 0 70px"}}
            />
          </div>

          <div className={styles.tags}>
            {ComicDetail.categories?.map((categories, index) => (
              <button
                key={index}
                onClick={() => navigate(`/comics/categories/${categories.originName}`, {state: {categories: categories.categoriesName}})}
                className={styles.tag}
              >
                {categories.categoriesName}
              </button>
            ))}
          </div>

          <div className={styles.buttons}>
              <button className={styles.read} onClick={() => {
                  navigate(`/chapter/${firstChapter}`)
              }}><i className="fi fi-rr-book-alt"></i> Đọc từ đầu
              </button>
              {!checkFollow ? (
                  <button className={styles.follow} onClick={handleFollow}>
                      <i className="fi fi-rr-heart"></i> Theo dõi
              </button>
            ) : (
              <button className={styles.follow} onClick={handleUnFollow}>
                  Đang theo dõi
              </button>
            )}

            {notification && (
              <Notification
                key={notification.key}
                success={notification.success}
                title={notification.title}
                message={notification.message}
              />
            )}
          </div>
        </div>
      </div>

      <div className={styles.section}>
          <h3>Giới thiệu</h3>
        <p>{ComicDetail.intro}</p>
      </div>

      <div className={styles.section}>
          <h3> Danh sách chương</h3>
        <ul className={styles.chapterList}>
          {Array.isArray(ComicDetail.chapters) &&
            [...ComicDetail.chapters].reverse().map((ch, index) => (
              <li
                key={index}
                onClick={() => {
                  navigate(`/chapter/${ch.chapter_uuid}`);
                }}>
                <span>Chương {ch.chapter}</span>
                <span>
                  {ch.updated
                    ? new Date(ch.updated).toLocaleDateString("vi-VN")
                    : "—"}
                </span>
              </li>
            ))}
        </ul>
      </div>
      <BackToTop />
      <Comment/>
    </div>
  );
}
