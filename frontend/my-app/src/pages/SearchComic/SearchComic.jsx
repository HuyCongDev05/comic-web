import style from './SearchComic.module.css';
import { useNavigate, useSearchParams } from "react-router-dom";
import { useEffect, useState } from "react";
import ComicApi from "../../api/Comic";
import Rating from '@mui/material/Rating';
import CustomPagination from "../../components/CustomPagination";
import Spinner from '../../components/Spinner/Spinner';
import HideScrollbar from "../../hooks/HideScrollbar";

export default function SearchComic() {
    HideScrollbar();
    const navigate = useNavigate();
    const [comics, setComics] = useState([]);
    const keyword = decodeURIComponent(window.location.pathname.split('/').pop());
    const [countPages, setCountPages] = useState(0);
    const [searchParams] = useSearchParams();
    const [page, setPage] = useState(Number(searchParams.get("page")) || 1);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const fetchComics = async () => {
            try {
                const response = await ComicApi.searchComics(keyword, page);
                setComics(response.data.content);
                setCountPages(response.data.totalPages);
            } catch (error) {
                console.error("Error fetching comics:", error);
            }
        };
        fetchComics();
        setLoading(false);
    }, [page, keyword]);

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

    const handleChangePage = (_event, value) => {
        setLoading(true);
        navigate(`?page=${value}`);
        setPage(value);
    };

    return (
        <>
            <div className={style.searchPage}>
                {(countPages === 0) ? (
                    <div className={style.searchNotice}>
                        <p className={style.Title}>Không tìm thấy truyện tên: {keyword}</p>
                    </div>
                ) : (
                    <>
                        <Spinner visible={loading} />
                        <div className={style.comicPage}>
                            <div className={style.categoryTitle}>
                                <h2>Tìm kiếm: {keyword}</h2>
                            </div>
                            <div className={style.comicContainer}>
                                {comics.map((comic) => (
                                    <div
                                        key={comic.uuid}
                                        className={style.comicWrapper}
                                        onClick={() => navigate(`/comic/${comic.originName}`)}
                                    >
                                        <div className={style.comicItem}>
                                            <div className={style.comicBanner}>
                                                <span>🔥</span>
                                                <span>{timeAgo(comic.updated)}</span>
                                            </div>
                                            <img src={comic.poster} alt={comic.name} className={style.comicImg} />
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
                                                    sx={{ fontSize: 16, stroke: "#fff" }}
                                                />
                                            </div>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    </>
                )}
            </div>
            <CustomPagination count={countPages} page={page} onChange={handleChangePage} />
        </>
    );
};