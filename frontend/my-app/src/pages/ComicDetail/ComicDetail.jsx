import styles from "./ComicDetail.module.css";
import ComicApi from "../../api/Comic";
import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import BackToTop from "../../components/Button/BackToTop/BackToTop"

export default function ComicDetail() {

  const {originName} = useParams();
  const navigate = useNavigate('');
  const [ComicDetail, setComicDetail] = useState('');

  useEffect(() => {
    const fetchComicDetail = async () => {
      try {
        const res = await ComicApi.getComicDetail(originName);
        setComicDetail(res.data);
      } catch {
        navigate('*');
      }
    };
    fetchComicDetail();
  }, [navigate, originName]);

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
            <span className={styles.value}>đang cập nhật</span>
          </div>
          <div className={styles.infoRow}>
            <span className={styles.label}>Tình trạng:</span>
            <span className={styles.value}>{ComicDetail.status}</span>
          </div>

          <div className={styles.stats}>
            <div>❤️ 1050</div>
            <div>👁️ 2,361,435</div>
          </div>

          <div className={styles.tags}>
            {ComicDetail.categories?.map((category, index) => (
              <button
                key={index}
                onClick={() => navigate(`/category/${category.originName}`)}
                className={styles.tag}
              >
                {category.categoryName}
              </button>
            ))}
          </div>

          <div className={styles.buttons}>
            <button className={styles.read} onClick={() => {navigate(`/chapter/${firstChapter}`)}}>📗 Đọc từ đầu</button>
            <button className={styles.follow}>❤️ Theo dõi</button>
          </div>
        </div>
      </div>

      <div className={styles.section}>
        <h3>🛈 Giới thiệu</h3>
        <p>{ComicDetail.intro}</p>
      </div>

      <div className={styles.section}>
      <h3>📜 Danh sách chương</h3>
      <ul className={styles.chapterList}>
        {Array.isArray(ComicDetail.chapters) &&
          [...ComicDetail.chapters].reverse().map((ch, index) => (
            <li
              key={index}
              onClick={() => {navigate(`/chapter/${ch.chapter_uuid}`);
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
    </div>
  );
}
