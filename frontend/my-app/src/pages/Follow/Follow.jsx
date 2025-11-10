import style from "./Follow.module.css";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { useState, useEffect } from "react";
import AccountApi from "../../api/Account";
import Rating from '@mui/material/Rating';


export default function Follow() {

    const navigate = useNavigate();
    const { user } = useAuth();
    const [checkLogin, setCheckLogin] = useState(false);
    const [comics, setComics] = useState([]);

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
        setCheckLogin(!!user);
    }, [user]);

    useEffect(() => {
        const fetchFollowComic = async () => {
            try {
                const resFollowComic = await AccountApi.followComic(user.uuid);
                setComics(resFollowComic.data);
            } catch { }
        }
        fetchFollowComic();
    }, [])

    return (
        <>
            <div className={style.followPage}>
                {!checkLogin ? (
                    <div className={style.loginNotice}>
                        <p className={style.Title}>Vui lòng đăng nhập để xem</p>
                    </div>
                ) : (
                    <>
                        <div className={style.comicContainer}>
                            {comics && comics.length > 0 ? (
                                comics.map((comic) => (
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
                                ))
                            ) : (
                                <p className="w-full text-center text-gray-400 text-[1rem] py-8">
                                    Chưa có truyện theo dõi
                                </p>
                            )}
                        </div>
                    </>

                )}
            </div>
        </>
    );
}