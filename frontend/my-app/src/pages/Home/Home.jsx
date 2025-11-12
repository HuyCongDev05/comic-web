import {useEffect, useState} from "react";
import {BookCheck, BookOpen, Sparkles} from "lucide-react";
import style from './Home.module.css';
import ReusableButton from "./../../components/Button/Button";
import ComicApi from "./../../api/Comic";
import Rating from '@mui/material/Rating';
import {useNavigate} from "react-router-dom";
import HideScrollbar from "../../hooks/HideScrollbar";
import {timeAgo} from "../../utils/timeAgo.jsx";
import Messages from "../../components/Message/Message.jsx";


export default function HomePage() {

  HideScrollbar();
  const categories = [
    { title: "Truyện mới", icon: <Sparkles />, key: "new" },
    { title: "Truyện mới cập nhật", icon: <BookOpen />, key: "new-update" },
    { title: "Truyện đã hoàn thành", icon: <BookCheck />, key: "completed" },
  ];

  const [newComics, setNewComics] = useState([]);
  const [newUpdateComics, setNewUpdateComics] = useState([]);
  const [completedComics, setCompletedComics] = useState([]);
  const navigate = useNavigate('');

  useEffect(() => {
    const fetchComics = async () => {
      try {
        const dataNewComic = await ComicApi.getNewComics(1);
        const dataNewUpdateComic = await ComicApi.getNewUpdateComics(1);
        const dataCompletedComic = await ComicApi.getCompletedComics(1);

        setNewComics(dataNewComic.data.content);
        setNewUpdateComics(dataNewUpdateComic.data.content);
        setCompletedComics(dataCompletedComic.data.content);

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
  }, [categories]);

  return (
    <>
      <div className={style.homepage}>
        {categories.map((cat) => (
          <section key={cat.key}>
            <div className={style.categoryTitle}>
              <h2>{cat.icon} {cat.title}</h2>
              <ReusableButton text="Xem thêm" onClick={() => navigate(`/comics/${cat.key}?page=1`)} />
            </div>
            <div id={cat.key} className={style.comicContainer}>
              {(cat.key === "new" ? newComics :cat.key === "completed" ? completedComics :newUpdateComics).map((comic) => (
                <div key={comic.uuid} className={style.comicWrapper} onClick={() => { navigate(`/comic/${comic.originName}`)}}>
                  <div className={style.comicItem}>
                    <div className={style.comicBanner}>
                        <span><i className="fi fi-rr-fire-flame-curved"></i></span>
                        <span>{timeAgo(comic.updated)}</span>
                    </div>
                    <img src={comic.poster} alt={comic.name} className={style.comicImg} loading="lazy" />
                    <div className={style.comicName}>
                      <p className="!text-[15px] leading-none m-0">{comic.name}</p>
                      <p className="!text-[10px] leading-none m-0">Chapter {comic.lastChapter}</p>
                      <Rating name="half-rating-read" defaultValue={comic.rating} precision={0.1} readOnly sx={{fontSize:16, stroke:"#fff"}} />
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </section>
        ))}
          <Messages/>
      </div>
    </>
  );
}
