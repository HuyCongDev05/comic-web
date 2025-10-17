/* eslint-disable no-undef */
/* eslint-disable react-hooks/exhaustive-deps */
import { useState, useEffect } from "react";
import { Sparkles, BookCheck, BookOpen } from "lucide-react";
import style from './Home.module.css';
import ReusableButton from "./../../components/Button/Button";
import ComicApi from "./../../api/Comic";
import Rating from '@mui/material/Rating';
import Stack from '@mui/material/Stack';

export default function HomePage() {

  const categories = [
    { title: "Truyện mới", icon: <Sparkles />, key: "new" },
    { title: "Truyện mới cập nhật", icon: <BookOpen />, key: "newUpdate" },
    { title: "Truyện đã hoàn thành", icon: <BookCheck />, key: "completed" },
  ];

  const [newComics, setNewComics] = useState([]);
  const [newUpdateComics, setNewUpdateComics] = useState([]);
  const [completedComics, setCompletedComics] = useState([]);

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

  useEffect(() => {
    const fetchComics = async () => {
      try {
        const dataNewComic = await ComicApi.getNewComics(1);
        const dataNewUpdateComic = await ComicApi.getNewUpdateComics(1);
        const dataCompletedComic = await ComicApi.getCompletedComics(1);

        setNewComics(dataNewComic.data);
        setNewUpdateComics(dataNewUpdateComic.data);
        setCompletedComics(dataCompletedComic.data);

      } catch (error) {
        console.error("Failed to fetch comics:", error);
      }
    };
    fetchComics();
  }, []);

  useEffect(() => {
    const interval = setInterval(() => {
      categories.forEach((cat) => {
        const container = document.getElementById(cat.key);
        if (container) {
          const maxScroll = container.scrollWidth - container.clientWidth;
          let newScroll = container.scrollLeft + 250;
          if (newScroll >= maxScroll) newScroll = 0;
          container.scrollTo({ left: newScroll, behavior: "smooth" });
        }
      });
    }, 4000);
    return () => clearInterval(interval);
  }, []);

  return (
    <>
      <div className={style.homepage}>
        {categories.map((cat) => (
          <section key={cat.key}>
            <div className={style.categoryTitle}>
              <h2>{cat.icon} {cat.title}</h2>
              <ReusableButton text="Xem thêm" onClick={() => console.log("Clicked")} />
            </div>
            <div id={cat.key} className={style.comicContainer}>
              {(cat.key === "new" ? newComics :cat.key === "completed" ? completedComics :newUpdateComics).map((comic) => (
                <div key={comic.uuid} className={style.comicWrapper}>
                  <div className={style.comicItem}>
                    <div className={style.comicBanner}>
                      <span>🔥</span>
                      <span>{timeAgo(comic.updated)}</span>
                    </div>
                    <img src={comic.poster} alt={comic.name} className={style.comicImg} />
                    <div className={style.comicName}>
                      <p className="!text-[15px] leading-none m-0">{comic.name}</p>
                      <p className="!text-[10px] leading-none m-0">Chapter {comic.lastChapter}</p>
                      <Stack spacing={1} className="leading-none m-0">
                        <Rating name="half-rating-read" defaultValue={comic.rating} precision={0.1} readOnly sx={{fontSize:16, stroke:"#fff"}} />
                      </Stack>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </section>
        ))}
      </div>
    </>
  );
}
