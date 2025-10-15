import { useState, useEffect } from "react";
import { Sparkles, BookCheck, BookOpen } from "lucide-react";
import './Home.css';
import ReusableButton from "../components/Button/Button";
import ComicApi from "../api/Comic";

export default function HomePage() {

  const categories = [
    { title: "Truyện mới", icon: <Sparkles />, key: "new" },
    { title: "Truyện mới cập nhật", icon: <BookOpen />, key: "newUpdate" },
    { title: "Truyện đã hoàn thành", icon: <BookCheck />, key: "completed" },
  ];

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


  const [newComics, setNewComics] = useState([]);
  const [newUpdateComics, setNewUpdateComics] = useState([]);
  const [completedComics, setCompletedComics] = useState([]);

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
      <div className="homepage">
        {categories.map((cat) => (
          <section key={cat.key}>
            <div className="category-title">
              <h2>{cat.icon} {cat.title}</h2>
              <ReusableButton text="Xem thêm" onClick={() => console.log("Clicked")} />
            </div>
            <div id={cat.key} className="comic-container">
              {(cat.key === "new" ? newComics :cat.key === "completed" ? completedComics :newUpdateComics).map((comic) => (
                <div key={comic.uuid} className="comic-wrapper">
                  <div className="comic-item">
                    <div className="comic-banner">
                      <span>🔥</span>
                      <span>{timeAgo(comic.updated)}</span>
                    </div>
                    <img src={comic.poster} alt={comic.name} className="comic-img" />
                    <div className="comic-name">
                      <p>{comic.name}</p>
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
