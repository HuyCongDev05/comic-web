import style from "./Follow.module.css";
import { useNavigate } from "react-router-dom";
import { Stack, Rating } from "@mui/material";
import { useAuth } from "../../context/AuthContext";
import { useState, useEffect } from "react";


export default function Follow() {

    const navigate = useNavigate();
    const {user} = useAuth();
    const [checkLogin, setCheckLogin] = useState(false);

    const comics = [
        {
            uuid: "1",
            name: "Attack on Titan",
            originName: "attack-on-titan",
            poster: "https://cdn.myanimelist.net/images/manga/2/37846.jpg",
            lastChapter: 139,
            updated: "2025-10-24T09:00:00Z",
            rating: 4.8,
        },
        {
            uuid: "2",
            name: "One Piece",
            originName: "one-piece",
            poster: "https://cdn.myanimelist.net/images/manga/3/55539.jpg",
            lastChapter: 1100,
            updated: "2025-10-23T12:00:00Z",
            rating: 4.9,
        },
        {
            uuid: "3",
            name: "Death Note",
            originName: "death-note",
            poster: "https://cdn.myanimelist.net/images/manga/2/54453.jpg",
            lastChapter: 108,
            updated: "2025-09-15T10:00:00Z",
            rating: 4.7,
        },
        {
            uuid: "4",
            name: "Jujutsu Kaisen",
            originName: "jujutsu-kaisen",
            poster: "https://cdn.myanimelist.net/images/manga/3/207312.jpg",
            lastChapter: 250,
            updated: "2025-10-22T15:00:00Z",
            rating: 4.6,
        },
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

    useEffect(() => {
        setCheckLogin(!!user);
    }, [user]);

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
                            {comics.map((comic) => (
                                <div
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
                                            <Stack spacing={1} className="leading-none m-0">
                                                <Rating
                                                    name="half-rating-read"
                                                    defaultValue={comic.rating}
                                                    precision={0.1}
                                                    readOnly
                                                    sx={{ fontSize: 16, stroke: "#fff" }}
                                                />
                                            </Stack>
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </>
                )}
            </div>
        </>
    );
}