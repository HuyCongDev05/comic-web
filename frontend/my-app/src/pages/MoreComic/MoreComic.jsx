import style from "./MoreComic.module.css";
import {useNavigate, useParams, useSearchParams} from "react-router-dom";
import {useEffect, useState} from "react";
import Rating from '@mui/material/Rating';
import ComicApi from "../../api/Comic";
import {BookCheck, BookOpen, Sparkles} from "lucide-react";
import {HideScrollbar, Loading} from "@comics/shared";
import {CustomPagination} from "@comics/shared";
import {timeAgo} from "../../utils/timeAgo.jsx";

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
    const [page, setPage] = useState(Number(searchParams.get("page")) || 1);
    const currentCategories = categories.find((cat) => cat.key === key);
    const [countPages, setCountPages] = useState(0);
    const [loading, setLoading] = useState(true);
    const [, setLoadedCount] = useState(0);


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
    }, [key, page]);

    const handleChangePage = (_event, value) => {
        setLoading(true);
        navigate(`?page=${value}`);
        setPage(value);
    };

    const totalImages = Comics?.length || 0;

    const handleImageLoaded = () => {
        setLoadedCount((prev) => {
            const next = prev + 1;
            if (totalImages > 0 && next >= totalImages) {
                setLoading(false);
            }
            return next;
        });
    };

    return (
        <>
            <Loading visible={loading} />
            <div className={`${style.seeMorePage} ${loading ? style.hiddenContent : ""}`}>
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
                                    <span><i className="fi fi-rr-fire-flame-curved"></i></span>
                                    <span>{timeAgo(comic.updated)}</span>
                                </div>
                                <img
                                    src={comic.poster}
                                    alt={comic.name}
                                    className={style.comicImg}
                                    onLoad={handleImageLoaded}
                                    onError={handleImageLoaded}
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