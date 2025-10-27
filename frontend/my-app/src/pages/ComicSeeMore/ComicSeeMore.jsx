import style from "./ComicSeeMore.module.css";
import { useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";
import Rating from '@mui/material/Rating';
import ComicApi from "../../api/Comic";
import { useParams } from 'react-router-dom';
import { Sparkles, BookOpen, BookCheck } from "lucide-react";

export default function ComicSeeMore() {

    const categories = [
        { title: "Truyện mới", icon: <Sparkles />, key: "new" },
        { title: "Truyện mới cập nhật", icon: <BookOpen />, key: "new-update" },
        { title: "Truyện đã hoàn thành", icon: <BookCheck />, key: "completed" },
    ];

    const navigate = useNavigate();
    const [Comics, setComics] = useState([]);
    const { key } = useParams();
    const currentCategories = categories.find((cat) => cat.key === key);

    useEffect(() => {
        const fetchComics = async () => {
            try {
                let res;
                if (key === "new") {
                    res = await ComicApi.getNewComics(1);
                } else if (key === "new-update") {
                    res = await ComicApi.getNewUpdateComics(1);
                } else if (key === "completed") {
                    res = await ComicApi.getCompletedComics(1);
                }

                if (res) setComics(res.data);
            } catch (error) {
                console.error("Lỗi tải comic:", error);
            }
        };
        fetchComics();
    }, [key]);

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
            <div className={style.seeMorePage}>
                <>
                    <div className={style.categoryTitle}>
                        <h2>{currentCategories.icon} {currentCategories.title}</h2>
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
                </>
            </div>
        </>
    );
}