/* eslint-disable react-hooks/exhaustive-deps */
import { useEffect } from "react";
import { Flame, Sparkles, BookOpen } from "lucide-react";
import './Home.css';

export default function HomePage() {
  const categories = [
    { title: "Truyện mới", icon: <Sparkles />, key: "new" },
    { title: "Truyện hot", icon: <Flame />, key: "hot" },
    { title: "Truyện nhiều người thích", icon: <BookOpen />, key: "love" },
  ];

  const comics = Array.from({ length: 10 }).map((_, i) => ({
    id: i + 1,
    name: `Truyện #${i + 1}`,
    image: `https://picsum.photos/200/300?random=${i + 1}`,
    time: `${Math.floor(Math.random() * 24)} giờ trước`,
  }));

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
  <div className="homepage">
    {categories.map((cat) => (
      <section key={cat.key}>
        <div className="category-title">
          <h2>{cat.icon} {cat.title}</h2>
        </div>
        <div id={cat.key} className="comic-container">
          {comics.map((comic) => (
            <div key={comic.id} className="comic-wrapper">
              <div className="comic-item">
                <div className="comic-banner">
                  <span>🔥</span>
                  <span>{comic.time}</span>
                </div>
                <img src={comic.image} alt={comic.name} className="comic-img" />
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
);
}
