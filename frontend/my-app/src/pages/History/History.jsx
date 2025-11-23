import {timeAgo} from "../../utils/timeAgo.jsx";
import style from "./History.module.css";
import Rating from "@mui/material/Rating";
import {useNavigate, useSearchParams} from "react-router-dom";
import {HideScrollbar, Loading, Notification, useAuth} from "@comics/shared";
import {useEffect, useState} from "react";
import AccountApi from "../../api/Account.jsx";
import {CustomPagination} from "@comics/shared";

export default function History() {
    HideScrollbar();
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const { user } = useAuth();
    const checkLogin = !!user;
    const [comics, setComics] = useState([]);
    const [page, setPage] = useState(Number(searchParams.get("page")) || 1);
    const [countPages, setCountPages] = useState(0);
    const [, setLoadedCount] = useState(0);
    const [loading, setLoading] = useState(true);
    const [notification, setNotification] = useState(false);

    useEffect(() => {
        if (!checkLogin) {
            setLoading(false);
        }
    }, []);

    if (localStorage.getItem("accessToken") && localStorage.getItem("user")) {
        useEffect(() => {
            const fetchComics = async () => {
                try {
                    const res = await AccountApi.history({ account_uuid: user.uuid, page: page })
                    if (res) {
                        setComics(res.data.content);
                        setCountPages(res.data.totalPages);
                    }
                } catch {
                    setNotification({
                        key: Date.now(),
                        success: false,
                        title: "Yêu cầu thất bại !!!",
                        message: "Đã có lỗi, vui lòng báo cáo admin",
                    });
                }
            }
            fetchComics();
        }, [user]);
    }

    const handleChangePage = (_event, value) => {
        setLoading(true);
        navigate(`?page=${value}`);
        setPage(value);
    };

    const totalImages = comics?.length || 0;
    useEffect(() => {
        if (totalImages === 0) {
            setLoading(false);
        }
    }, [totalImages]);

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
            {notification && (
                <Notification
                    key={notification.key}
                    success={notification.success}
                    title={notification.title}
                    message={notification.message}
                />
            )}
            <div className={`${style.historyPage} ${loading ? style.hiddenContent : ""}`}>
                {!checkLogin ? (
                    <div className={style.loginNotice}>
                        <p className={style.Title}>Vui lòng đăng nhập để xem</p>
                    </div>
                ) : (
                    <>
                        <div className={style.historyTitle}>
                            <h2>Lịch sử</h2>
                        </div>
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
                                ))
                            ) : (
                                <p className="w-full text-center text-gray-400 text-[1rem] py-8">
                                    Chưa có lịch sử đọc
                                </p>
                            )}
                        </div>
                        <CustomPagination count={countPages} page={page} onChange={handleChangePage} />
                    </>
                )}
            </div>
        </>
    );
};