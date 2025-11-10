import style from "./Navbar.module.css";
import logo from "../../assets/icons/logo.png";
import NavItem from "./components/Menu/Menu";
import CategoriesApi from "../../api/Categories";
import {useEffect, useRef, useState} from "react";
import User from "../Navbar/components/User/User";
import {Link, useNavigate} from "react-router-dom";
import Rating from '@mui/material/Rating';
import ComicApi from "../../api/Comic";

export default function Navbar() {

  const [categories, setCategories] = useState([]);
  const [searchResults, setSearchResults] = useState([]);
  const [searchQuery, setSearchQuery] = useState('');
  const navigate = useNavigate();
  const searchBoxRef = useRef(null);
  const intervalRef = useRef(null);
  const stopTimeoutRef = useRef(null);

  useEffect(() => {
    const effectCategories = async () => {
      try {
        const dataCategories = await CategoriesApi.getCategories(1);
        setCategories(dataCategories.data)
      } catch (error) {
        console.error("Failed to fetch categories:", error);
      }
    };
    effectCategories();
  }, []);

  useEffect(() => {
    if (searchQuery.length < 3) {
      clearInterval(intervalRef.current);
      clearTimeout(stopTimeoutRef.current);
      setSearchResults([]);
      return;
    }

    clearInterval(intervalRef.current);
    clearTimeout(stopTimeoutRef.current);

    fetchSearchResults(searchQuery);

    intervalRef.current = setInterval(() => {
      fetchSearchResults(searchQuery);
    }, 400);

    stopTimeoutRef.current = setTimeout(() => {
      clearInterval(intervalRef.current);
    }, 300);

    return () => {
      clearInterval(intervalRef.current);
      clearTimeout(stopTimeoutRef.current);
    };
  }, [searchQuery]);

  const fetchSearchResults = async (text) => {
    try {
      const res = await ComicApi.searchComics(text, 1);
      setSearchResults(res.data.content || []);
      console.log(res.data.content);
    } catch (error) {
      console.error("Lỗi tìm kiếm:", error);
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter" && searchQuery.trim() !== "") {
      e.preventDefault();

      document.body.style.overflow = "";
      document.documentElement.style.overflow = "";

      const newUrl = `/comics/search/${encodeURIComponent(searchQuery)}?page=1`;

      if (location.pathname + location.search === newUrl) {
        window.location.href = newUrl;
      } else {
        navigate(newUrl);
      }

      setTimeout(() => {
        setSearchQuery("");
        setSearchResults([]);
      }, 200);
    }
  };

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

  return (
    <>
      <div className={style.navbar}>
        <div className={style.left}>
          <p className={style.logoLink}>
            <img className={style.logo} src={logo} alt="logo" />
          </p>
        </div>
        <div className={style.center}>
          <ul className={style.menuFunction}>
            <NavItem name="Trang chủ" />
            <NavItem name="Theo dõi" />
            <NavItem name="Lịch sử" />
            <li className={style.category}
              onMouseEnter={() => {
                document.body.style.overflow = 'hidden';
                document.documentElement.style.overflow = 'hidden';
              }}
              onMouseLeave={() => {
                document.body.style.overflow = '';
                document.documentElement.style.overflow = '';
              }}
            >
              <i className={style.categories}>Thể loại</i>
              <div className={style.dropdownCategories}>
                <div className={style.listCategories}>
                  {categories.map((item, i) => (
                      <Link
                          key={i}
                          to={`/comics/categories/${item.originName}`}
                          state={{ categories: item.name }}
                          className={style.item}
                      >
                          {item.name}
                      </Link>
                  ))}
                </div>
              </div>
            </li>
          </ul>
        </div>
        <div className={style.right}>
          <ul className={style.iconsFunction}>
            <li className={style.searchBox} ref={searchBoxRef}>
              <input
                type="text"
                placeholder="Tìm kiếm..."
                className={style.searchInput}
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                onKeyDown={handleKeyDown}
              />
              <i className={`fi fi-rs-search ${style.searchIcon}`}></i>

              {searchQuery.length >= 3 && (
                <div
                  className={`${style.dropdownSearchs} ${style.show}`}
                  onMouseEnter={() => {
                    document.body.style.overflow = 'hidden';
                    document.documentElement.style.overflow = 'hidden';
                  }}
                  onMouseLeave={() => {
                    document.body.style.overflow = '';
                    document.documentElement.style.overflow = '';
                    setSearchQuery('');
                    setSearchResults([]);
                  }}
                >
                  {searchResults.length > 0 ? (
                    searchResults.slice(0, 8).map((comic) => (
                      <div
                        key={comic.uuid}
                        className={style.comicWrapper}
                        onClick={() => {
                          setSearchQuery('');
                          setSearchResults([]);
                          document.body.style.overflow = '';
                          document.documentElement.style.overflow = '';
                          navigate(`/comic/${comic.originName}`);
                        }}
                      >
                        <div className={style.comicItem}>
                          <div className={style.comicBanner}>
                            <span>🔥</span>
                            <span>{timeAgo(comic.updated)}</span>
                          </div>
                          <img
                            src={comic.poster}
                            alt={comic.name}
                            className={style.comicImg}
                            loading="lazy"
                          />
                          <div className={style.comicName}>
                            <p className="!text-[15px] leading-none m-0">{comic.name}</p>
                            <p className="!text-[10px] leading-none m-0">
                              Chapter {comic.lastChapter}
                            </p>
                            <Rating
                              name="half-rating-read"
                              defaultValue={comic.rating}
                              precision={0.1}
                              readOnly
                              sx={{ fontSize: 16, stroke: '#fff' }}
                            />
                          </div>
                        </div>
                      </div>
                    ))
                  ) : (
                    <div className={style.noResult}>Không tìm thấy truyện 😢</div>
                  )}
                </div>
              )}
            </li>
            <li><Link to="/messages"><i className="fi fi-rr-messages"></i></Link></li>
            <User />
          </ul>
        </div>
      </div>
    </>
  );
}
