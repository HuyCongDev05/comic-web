import style from "./CategoriesDetail.module.css";
import { useNavigate, useSearchParams } from "react-router-dom";
import { useEffect, useState } from "react";
import ComicApi from "../../api/Comic";
import Rating from '@mui/material/Rating';
import { CustomPagination, timeAgo, HideScrollbar, Loading } from "@comics/shared";

export default function () {
    HideScrollbar();
    const navigate = useNavigate();
    const [comics, setComics] = useState([]);
    const categories = decodeURIComponent(window.location.pathname.split('/').pop());
    const [countPages, setCountPages] = useState(0);
    const [searchParams] = useSearchParams();
    const [page, setPage] = useState(Number(searchParams.get("page")) || 1);
    const [loading, setLoading] = useState(true);
    const [, setLoadedCount] = useState(0);

    useEffect(() => {
        const fetchComics = async () => {
            try {
                const response = await ComicApi.searchComicsByCategories(categories, page);
                setComics(response.data.content);
                setCountPages(response.data.totalPages);
            } catch (error) {
                console.error("Error fetching comics:", error);
            }
        };
        fetchComics();
    }, [page, categories]);

    const handleChangePage = (_event, value) => {
        setLoading(true);
        navigate(`?page=${value}`);
        setPage(value);
    };

    const totalImages = comics?.length || 0;

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
            <div className={`${style.comicPage} ${loading ? style.hiddenContent : ""}`}>
                <div className={style.categoryTitle}>
                    <h2>Thể loại: {categories}</h2>
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
            </div>
            <CustomPagination count={countPages} page={page} onChange={handleChangePage} />
        </>
    );
}