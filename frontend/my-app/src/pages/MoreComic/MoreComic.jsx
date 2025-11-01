import style from "./MoreComic.module.css";
import { useNavigate, useSearchParams } from "react-router-dom";
import { useState, useEffect } from "react";
import Rating from '@mui/material/Rating';
import ComicApi from "../../api/Comic";
import { useParams } from 'react-router-dom';
import { Sparkles, BookOpen, BookCheck } from "lucide-react";
import HideScrollbar from "../../hooks/HideScrollbar";
import CustomPagination from "../../components/CustomPagination";
import Spinner from '../../components/Spinner/Spinner';

export default function MoreComic() {
    HideScrollbar();
    const categories = [
        { title: "Truyện mới", icon: <Sparkles />, key: "new" },
        { title: "Truyện mới cập nhật", icon: <BookOpen />, key: "new-update" },
        { title: "Truyện đã hoàn thành", icon: <BookCheck />, key: "completed" },
    ];

    const navigate = useNavigate();
    const [Comics, setComics] = useState([]);
    const { key } = useParams();
    const [searchParams] = useSearchParams();
    const [page,setPage] = useState(Number(searchParams.get("page")) || 1);
    const currentCategories = categories.find((cat) => cat.key === key);
    const [countPages, setCountPages] = useState(0);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const fetchComics = async () => {
            try {
                let res;
                if (key === "new") {
                    res = await ComicApi.getNewComics(page);
                } else if (key === "new-update") {
                    res = await ComicApi.getNewUpdateComics(page);
                } else if (key === "completed") {
                    res = await ComicApi.getCompletedComics(page);
                }

                if (res) {
                    setComics(res.data.content);
                    setCountPages(res.data.totalPages);
                }
            } catch (error) {
                console.error("Lỗi tải comic:", error);
            }
        };
        fetchComics();
        setLoading(false);
    }, [key, page]);

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
            <div className={style.seeMorePage}>
                <Spinner visible={loading} />
                <div className={style.categoryTitle}>
                    <h2>{currentCategories?.icon} {currentCategories?.title}</h2>
                </div>
                <div className={style.comicContainer}>
                    {Comics.map((comic) => (
                        <div
                            key={comic.uuid || comic.originName}
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
                <CustomPagination count={countPages} page={page} onChange={handleChangePage} />
            </div>
        </>
    );
}