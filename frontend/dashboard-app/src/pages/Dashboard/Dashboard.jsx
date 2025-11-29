import { useState, useEffect } from 'react';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, PieChart, Pie, Cell, } from 'recharts';
import { BookOpen, Users, Eye, TrendingUp, Clock, BookMarked, Zap, ArrowUp, ArrowDown, Home, Download, Settings, FileText, Menu, X } from 'lucide-react';
import styles from './Dashboard.module.css';
import { ReusableButton, timeAgo, useAuth, Spinner, Notification, CustomPagination } from "@comics/shared";
import DashboardApi from "../../api/Dashboard.jsx";
import { useNavigate } from 'react-router-dom';
import AccountApi from '../../api/Account.jsx';
import CrawlApi from '../../api/Crawl.jsx';
import { FormatUTC } from '../../utils/FormatUTC.jsx';

const Dashboard = () => {

    const navigate = useNavigate();
    const { user, login, logout } = useAuth();
    const [time, setTime] = useState(new Date());
    const [activeMenu, setActiveMenu] = useState('home');
    const [sidebarOpen, setSidebarOpen] = useState(true);
    const [dataHome, setDataHome] = useState();
    const [dataAccounts, setDataAccounts] = useState();
    const categoriesRatio = dataHome?.categoriesRatio ?? [];
    const today = new Date().toISOString().split("T")[0];
    const daysInMonth = new Date(...today.split("-").map(Number).slice(0, 2), 0).getDate();
    const [spinner, setSpinner] = useState(false);
    const [notification, setNotification] = useState(false);
    const [pageAccounts, setPageAccounts] = useState(1);
    const [pageCrawlError, setPageCrawlError] = useState(1);
    const [dataCrawlError, setDataCrawlError] = useState([]);
    const [searchAccounts, setSearchAccounts] = useState("");
    const [filteredAccounts, setFilteredAccounts] = useState([]);
    const [lastTimeCrawl, setLastTimeCrawl] = useState();
    const [crawlHistory, setCrawlHistory] = useState([]);
    const [pageCrawlHistory, setPageCrawlHistory] = useState(1);
    const [filterErrorCrawl, setFilterErrorCrawl] = useState("");
    const [countErrorIntro, setCountErrorIntro] = useState(0);
    const [countErrorChapter, setCountErrorChapter] = useState(0);
    const [comics, setComics] = useState([]);

    const filteredErrors = filterErrorCrawl === ""
        ? dataCrawlError.content
        : dataCrawlError.content.filter(error => error.type === filterErrorCrawl);

    useEffect(() => {
        const fetchHome = async () => {
            try {
                const resHome = await DashboardApi.home();
                setDataHome(resHome.data);
            } catch (err) {
                console.log(err);
            }
        }

        const fetchCrawl = async () => {
            try {
                const resLastTime = await CrawlApi.crawlLastTime();
                setLastTimeCrawl(resLastTime.data.crawl_last_time);
            } catch (error) {
                console.log(err);
            }
        }

        fetchHome();
        fetchCrawl();
    }, []);

    useEffect(() => {
        const fetchCrawlHistory = async () => {
            try {
                const resCrawlHistory = await CrawlApi.crawlHistory(pageCrawlHistory);
                setCrawlHistory(resCrawlHistory.data);
            } catch (error) {
                console.log(error);
            }
        }

        fetchCrawlHistory();
    }, [pageCrawlHistory]);

    useEffect(() => {
        const fetchCrawlError = async () => {
            try {
                const resCrawlError = await CrawlApi.crawlError(pageCrawlError);
                setDataCrawlError(resCrawlError.data);
            } catch (error) {
                console.log(error);
            }
        }

        fetchCrawlError();
    }, [pageCrawlError]);

    useEffect(() => {
        const fetchAccounts = async () => {
            try {
                const res = await DashboardApi.listAccounts(pageAccounts);
                setDataAccounts(res.data);
            } catch (err) {
                console.log(err);
            }
        }

        fetchAccounts();
    }, [pageAccounts]);

    useEffect(() => {
        const timer = setInterval(() => setTime(new Date()), 1000);
        return () => clearInterval(timer);
    }, []);

    const handleChangePageAccounts = (_event, value) => {
        setPageAccounts(value);
    };

    const handleChangePageCrawlHistory = (_event, value) => {
        setPageCrawlHistory(value);
    };

    const handleChangePageCrawlError = (_event, value) => {
        setPageCrawlError(value);
    };

    const handleSearchChange = async (e) => {
        const keyword = e.target.value;
        setSearchAccounts(keyword);
        const localResult = dataAccounts.content.filter(user =>
            user.fullName.toLowerCase().includes(keyword.toLowerCase()) ||
            user.email.toLowerCase().includes(keyword.toLowerCase())
        );

        if (localResult.length > 0) {
            setFilteredAccounts(localResult);
        } else if (keyword.trim() !== "") {
            try {
                const res = await DashboardApi.searchAccounts(keyword);
                setFilteredAccounts(res.data.content || []);
            } catch (error) {
                console.log(error);
                setFilteredAccounts([]);
            }
        } else {
            setFilteredAccounts([]);
        }
    };

    if (localStorage.getItem("accessToken")) {
        useEffect(() => {
            const fetchInfo = async () => {
                try {
                    const res = await AccountApi.me();
                    if (res.data) {
                        login(
                            {
                                uuid: res?.data?.uuid || "",
                                firstName: res?.data?.firstName || "",
                                lastName: res?.data?.lastName || "",
                                email: res?.data?.email || "",
                                phone: res?.data?.phone || "",
                                address: res?.data?.address || "",
                                avatar: res?.data?.avatar || "",
                                status: res?.data?.status || "",
                            }
                        );
                    }
                } catch (error) {
                    console.error("Failed to fetch user info:", error);
                }
            };
            fetchInfo();
        }, []);
    }

    const handleUserAction = async (uuid, action) => {
        try {
            setSpinner(true);

            let newStatus;
            switch (action) {
                case 'WARNING':
                    await DashboardApi.updateStatusAccounts(uuid, "WARNING");
                    newStatus = 'WARNING';
                    break;
                case 'remove-warning':
                    await DashboardApi.updateStatusAccounts(uuid, "NORMAL");
                    newStatus = 'NORMAL';
                    break;
                case 'BAN':
                    await DashboardApi.updateStatusAccounts(uuid, "BAN");
                    newStatus = 'BAN';
                    break;
                case 'remove-ban':
                    await DashboardApi.updateStatusAccounts(uuid, "NORMAL");
                    newStatus = 'NORMAL';
                    break;
                default:
                    setSpinner(false);
                    return;
            }

            setDataAccounts(prevState => ({
                ...prevState,
                content: prevState.content.map(user =>
                    user.uuid === uuid
                        ? { ...user, status: newStatus }
                        : user
                )
            }));

            setSpinner(false);
            setNotification({
                key: Date.now(),
                success: true,
                title: "Yêu cầu thành công !!!",
                message: "Cập nhật trạng thái thành công",
            });

        } catch (error) {
            console.error(error);
        } finally { setSpinner(false); }
    };

    const handleCrawlComics = async () => {
        try {
            setSpinner(true);
            await CrawlApi.crawlComic();
            setNotification({
                key: Date.now(),
                success: true,
                title: "Yêu cầu thành công !!!",
                message: "Đã bắt đầu crawl",
            });
        } catch (error) {
            console.log(error);
        } finally {
            setSpinner(false);
        }
    }

    const menuItems = [
        { id: 'home', name: 'Trang chủ', icon: Home },
        { id: 'crawl', name: 'Crawl Truyện', icon: Download },
        { id: 'comics', name: 'Quản lý Truyện', icon: BookOpen },
        { id: 'user', name: 'Người dùng', icon: Users },
        { id: 'report', name: 'Báo cáo', icon: FileText },
        { id: 'setting', name: 'Cài đặt', icon: Settings },
    ];

    const dateMap = (dataHome?.viewsAndVisits || []).reduce((acc, item) => {
        const day = new Date(item.date).getDate();
        acc[day] = item;
        return acc;
    }, {});

    const visitData = Array.from({ length: daysInMonth }, (_, i) => {
        const day = i + 1;
        const item = dateMap[day] || { requests: 0, views: 0 };
        return {
            day: day.toString(),
            visits: item.requests,
            reads: item.views
        };
    });


    const genreData = categoriesRatio.length === 0
        ? []
        : (() => {
            const top4 = categoriesRatio.slice(0, 4).map((c, i) => ({
                name: c.categoriesName,
                value: +c.ratio.toFixed(2),
                color: ['#3b82f6', '#ec4899', '#8b5cf6', '#f59e0b'][i],
            }));

            const sumTop4 = top4.reduce((s, c) => s + c.value, 0);

            return [
                ...top4,
                {
                    name: 'Khác',
                    value: +(100 - sumTop4).toFixed(2),
                    color: '#10b981',
                },
            ];
        })();

    const topStories = (dataHome?.viewsRatioComics || [])
        .sort((a, b) => b.views - a.views)
        .map(comic => ({
            title: comic.comicName,
            views: comic.views,
            trend: 'none',
            change: '+0'
        }));


    const stats = [
        {
            title: 'Tổng lượt truy cập',
            value: dataHome ? dataHome.totalVisits : '0',
            change: '+0',
            icon: Eye,
            colorClass: styles.statIconBlue,
            trend: 'none',
            subtext:
                'Hôm nay: ' +
                (
                    dataHome?.viewsAndVisits?.find(item => item.date === today)?.requests
                    ?? '0'
                ),
        },
        {
            title: 'Tổng số truyện',
            value: dataHome ? dataHome.totalComics : '0',
            change: '+0',
            icon: BookOpen,
            colorClass: styles.statIconPurple,
            trend: 'none',
            subtext: 'Tổng số truyện',
        },
        {
            title: 'Tổng số người dùng',
            value: dataHome ? dataHome.totalAccounts : '0',
            change: '+0',
            icon: Users,
            colorClass: styles.statIconPink,
            trend: 'none',
            subtext: 'Tổng số người dùng',
        },
        {
            title: 'Truyện đã hoàn thành',
            value: dataHome ? dataHome.totalCompletedComics : '0',
            change: '+0',
            icon: BookMarked,
            colorClass: styles.statIconOrange,
            trend: 'none',
            subtext: 'Truyện đã hoàn thành',
        },
    ];

    const renderHome = () => (
        <>
            <div className={styles.statsGrid}>
                {stats.map((stat, i) => (
                    <div key={i} className={styles.card}>
                        <div className={styles.cardHeader}>
                            <div className={`${styles.statIcon} ${stat.colorClass}`}>
                                <stat.icon size={22} />
                            </div>
                            {
                                (stat.trend === 'up' || stat.trend === 'down') && (
                                    <div
                                        className={`${styles.badgeTrend} ${stat.trend === 'up' ? styles.trendUp : styles.trendDown}`}
                                    >
                                        {stat.trend === 'up' && <ArrowUp size={16} />}
                                        {stat.trend === 'down' && <ArrowDown size={16} />}
                                        <span>{stat.change}</span>
                                    </div>
                                )
                            }
                        </div>
                        <div className={styles.cardBody}>
                            <div className={styles.cardLabel}>{stat.title}</div>
                            <div className={styles.cardValue}>{stat.value}</div>
                            <div className={styles.cardSub}>{stat.subtext}</div>
                        </div>
                    </div>
                ))}
            </div>

            <div className={styles.chartsRow}>
                <div className={`${styles.card} ${styles.chartCard}`}>
                    <div className={styles.cardTitleRow}>
                        <div className={styles.cardTitleLeft}>
                            <div className={styles.cardTitleIcon}>
                                <TrendingUp size={18} />
                            </div>
                            <h2 className={styles.cardTitle}>Lượt truy cập theo ngày (Tháng này)</h2>
                        </div>
                    </div>
                    <div className={styles.chartWrapper}>
                        <ResponsiveContainer width="100%" height={260}>
                            <AreaChart
                                data={visitData}
                                margin={{ top: 10, right: 25, left: 0, bottom: 0 }}
                            >
                                <defs>
                                    <linearGradient id="colorVisits" x1="0" y1="0" x2="0" y2="1">
                                        <stop offset="5%" stopColor="#6366f1" stopOpacity={0.8} />
                                        <stop offset="95%" stopColor="#6366f1" stopOpacity={0} />
                                    </linearGradient>
                                    <linearGradient id="colorReads" x1="0" y1="0" x2="0" y2="1">
                                        <stop offset="5%" stopColor="#ec4899" stopOpacity={0.8} />
                                        <stop offset="95%" stopColor="#ec4899" stopOpacity={0} />
                                    </linearGradient>
                                </defs>
                                <CartesianGrid strokeDasharray="3 3" stroke="#ffffff20" />
                                <XAxis
                                    dataKey="day"
                                    stroke="#a5b4fc"
                                    interval={0}
                                    label={{
                                        position: 'insideBottom',
                                        offset: -5,
                                        fill: '#a5b4fc',
                                    }}
                                />
                                <YAxis stroke="#a5b4fc" />
                                <Tooltip
                                    contentStyle={{
                                        backgroundColor: '#020617',
                                        border: '1px solid #6366f1',
                                        borderRadius: 8,
                                        color: '#fff',
                                    }}
                                />
                                <Legend />
                                <Area
                                    type="monotone"
                                    dataKey="visits"
                                    stroke="#6366f1"
                                    fill="url(#colorVisits)"
                                    fillOpacity={1}
                                    strokeWidth={2}
                                    name="Lượt truy cập"
                                />
                                <Area
                                    type="monotone"
                                    dataKey="reads"
                                    stroke="#ec4899"
                                    fill="url(#colorReads)"
                                    fillOpacity={1}
                                    strokeWidth={2}
                                    name="Lượt đọc"
                                />
                            </AreaChart>
                        </ResponsiveContainer>
                    </div>
                </div>

                <div className={styles.card}>
                    <div className={styles.cardTitleRow}>
                        <h2 className={styles.cardTitle}>Thể loại phổ biến</h2>
                    </div>
                    <div className={styles.chartWrapper}>
                        <ResponsiveContainer width="100%" height={220}>
                            <PieChart>
                                <Pie
                                    data={genreData}
                                    cx="50%"
                                    cy="50%"
                                    innerRadius={55}
                                    outerRadius={85}
                                    dataKey="value"
                                    paddingAngle={4}
                                >
                                    {genreData.map((g, i) => (
                                        <Cell key={i} fill={g.color} />
                                    ))}
                                </Pie>
                            </PieChart>
                        </ResponsiveContainer>
                    </div>
                    <div className={styles.genreList}>
                        {genreData.map((g, i) => (
                            <div key={i} className={styles.genreItem}>
                                <div className={styles.genreLeft}>
                                    <span
                                        className={styles.genreDot}
                                        style={{ backgroundColor: g.color }}
                                    />
                                    <span>{g.name}</span>
                                </div>
                                <span className={styles.genreValue}>{g.value}%</span>
                            </div>
                        ))}
                    </div>
                </div>
            </div>
            <div className={styles.trending}>
                <div className={`${styles.card} ${styles.trendingCard}`}>
                    <div className={styles.cardTitleRow}>
                        <div className={styles.cardTitleLeft}>
                            <div className={styles.cardTitleIconHighlight}>
                                <Zap size={18} />
                            </div>
                            <h2 className={styles.cardTitle}>Top 5 truyện hot nhất</h2>
                        </div>
                    </div>

                    <div className={styles.topStoriesList}>
                        {topStories.map((story, index) => (
                            <div key={index} className={styles.storyItem}>
                                <div className={styles.storyRank}>{index + 1}</div>

                                <div className={styles.storyInfo}>
                                    <div className={styles.storyTitle}>{story.title}</div>

                                    <div className={styles.storyViews}>
                                        <Eye size={14} />
                                        <span>{story.views.toLocaleString()} lượt đọc</span>
                                    </div>
                                </div>

                                {(story.trend === 'up' || story.trend === 'down') && (
                                    <div
                                        className={`${styles.badgeTrend} ${story.trend === 'up' ? styles.trendUp : styles.trendDown
                                            }`}
                                    >
                                        {story.trend === 'up' && <ArrowUp size={14} />}
                                        {story.trend === 'down' && <ArrowDown size={14} />}
                                        <span>{story.change}</span>
                                    </div>
                                )}

                            </div>
                        ))}
                    </div>
                </div>
            </div>
        </>
    );

    const renderCrawl = () => (
        <div className={styles.pageStack}>
            <div className={styles.card}>
                <div className={styles.cardTitleRow}>
                    <h2 className={styles.cardTitle}>Crawl truyện mới</h2>
                    <p>Thời gian crawl lần cuối: {FormatUTC(lastTimeCrawl)}</p>
                </div>
                <div className={styles.formGrid}>
                    <div className={styles.formActions}>
                        <ReusableButton
                            className={styles.primaryButton}
                            text={
                                <>
                                    <Download size={18} />
                                    Bắt đầu crawl
                                </>
                            }
                            onClick={handleCrawlComics}
                        />

                        <ReusableButton
                            className={styles.secondaryButton}
                            text={
                                <>
                                    <Clock size={18} />
                                    Đặt lịch
                                </>
                            }
                        />
                    </div>
                </div>
            </div>

            <div className={styles.summaryGrid}>
                <div className={styles.card}>
                    <div className={styles.summaryHeader}>
                        <span className={`${styles.statusDot} ${styles.statusRed}`} />
                        <span className={styles.summaryLabel}>Lỗi Intro</span>
                    </div>
                    <div className={styles.summaryValue}>{countErrorIntro}</div>
                </div>

                <div className={styles.card}>
                    <div className={styles.summaryHeader}>
                        <span className={`${styles.statusDot} ${styles.statusYellow}`} />
                        <span className={styles.summaryLabel}>Lỗi Chapter</span>
                    </div>
                    <div className={styles.summaryValue}>{countErrorChapter}</div>
                </div>

                <div className={styles.card}>
                    <div className={styles.summaryHeader}>
                        <span className={`${styles.statusDot} ${styles.statusGreen}`} />
                        <span className={styles.summaryLabel}>Thành công trong ngày hôm nay</span>
                    </div>
                    <div className={styles.summaryValue}>{crawlHistory.totalElements}</div>
                </div>
            </div>

            <div className={styles.card}>
                <div className={styles.cardTitleRow}>
                    <h2 className={styles.cardTitle}>Danh sách lỗi</h2>
                    <div className={styles.tableActions}>
                        <select
                            value={filterErrorCrawl}
                            onChange={(e) => setFilterErrorCrawl(e.target.value)}
                        >
                            <option value="">Tất cả loại lỗi</option>
                            <option value="intro">Lỗi Intro</option>
                            <option value="chapter">Lỗi Chapter</option>
                            <option value="comic">Lỗi Truyện</option>
                        </select>
                        <ReusableButton
                            className={styles.dangerButton}
                            text={
                                <>
                                    <Download size={16} />
                                    Xử lí tất cả lỗi
                                </>
                            }
                        />
                    </div>
                </div>
                <div className={styles.tableWrapper}>
                    <table className={styles.table}>
                        <thead>
                            <tr>
                                <th>tên gốc</th>
                                <th>Loại lỗi</th>
                                <th>Chi tiết</th>
                                <th>Thời gian</th>
                                <th className={styles.textRight}>Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            {filteredErrors.length > 0 ? (
                                filteredErrors.map(error => (
                                    <tr key={error._id}>
                                        <td>{error.originName}</td>
                                        <td>
                                            <span
                                                className={`${styles.badge} ${error.type === 'intro'
                                                    ? styles.badgeRed
                                                    : styles.badgeYellow
                                                    }`}
                                            >
                                                {error.type === 'intro' ? 'Lỗi Intro' : 'Lỗi Chapter'}
                                            </span>
                                        </td>
                                        <td className={styles.muted}>{error.messages}</td>
                                        <td className={styles.muted}>{timeAgo(error.created_at)}</td>
                                        <td className={styles.textRight}>
                                            <div className={styles.actionsRow}>
                                                <ReusableButton className={styles.secondaryButtonSm} text="Crawl lại" />
                                            </div>
                                        </td>
                                    </tr>
                                ))
                            ) : (
                                <tr>
                                    <td colSpan="5" style={{ textAlign: 'center', padding: '20px' }}>
                                        Không có lỗi.
                                    </td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </div>
                <div className={styles.paginationRow}>
                    <span className={styles.muted}>
                        Hiển thị {dataCrawlError.totalElements} lỗi gần đây
                    </span>
                    <div className={styles.paginationButtons}>
                        <CustomPagination count={dataCrawlError.totalPages} page={pageCrawlError} onChange={handleChangePageCrawlError} />
                    </div>
                </div>
            </div>
            <div className={styles.historyCrawlComic}>
                <div className={styles.card}>
                    <div className={styles.cardTitleRow}>
                        <h2 className={styles.cardTitle}>Lịch sử crawl gần đây</h2>
                    </div>
                    <div className={styles.x}>
                        {Object.values(crawlHistory.content || {}).map((itemString, i) => {

                            const lastCommaIndex = itemString.lastIndexOf(',');
                            const firstCommaIndex = itemString.indexOf(',');

                            const name = itemString.substring(firstCommaIndex + 1, lastCommaIndex);

                            const rawTime = itemString.substring(lastCommaIndex + 1);

                            return (
                                <div key={i} className={styles.historyItem}>
                                    <span className={`${styles.statusDot} ${styles.statusGreen}`} />

                                    <div className={styles.historyInfo}>
                                        <div className={styles.historyTitle}>{name}</div>
                                    </div>

                                    <div className={styles.historyTime}>{timeAgo(rawTime)}</div>
                                </div>
                            );
                        })}
                    </div>
                    <div className={styles.paginationRow}>
                        <span className={styles.muted}>
                            Hiển thị {crawlHistory.currentPageSize} truyện được crawl gần đây
                        </span>
                        <div className={styles.paginationButtons}>
                            <CustomPagination count={crawlHistory.totalPages} page={pageCrawlHistory} onChange={handleChangePageCrawlHistory} />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );

    const renderComics = () => (
        <div className={styles.pageStack}>
            <div className={styles.statsGrid}>

                <div className={styles.card}>
                    <div className={styles.cardLabel}>
                        <div className={styles.greenDot}></div>
                        <span>Đang tiếp tục</span>
                    </div>
                    <div className={styles.cardValue}>{comics.filter(s => s.status === 'dang-tiep-tuc').length}</div>
                </div>


                <div className={styles.card}>
                    <div className={styles.cardLabel}>
                        <div className={styles.blueDot}></div>
                        <span>Đã hoàn thành</span>
                    </div>
                    <div className={styles.cardValue}>{comics.filter(s => s.status === 'hoan-thanh').length}</div>
                </div>
            </div>

            <div className={styles.cardTitleRow}>
                <h2 className={styles.cardTitle}>Danh sách truyện tranh</h2>
                <div className={styles.tableActions}>
                    <select
                        value={filterErrorCrawl}
                        onChange={(e) => setFilterErrorCrawl(e.target.value)}
                    >
                        <option value="">Tất cả loại lỗi</option>
                        <option value="intro">Đã hoàn thành</option>
                        <option value="chapter">Đang tiếp tục</option>
                    </select>
                    <input
                        className={styles.searchInput}
                        placeholder="Tìm kiếm..."
                        type="text"
                        value={searchAccounts}
                        onChange={handleSearchChange}
                    />
                </div>
            </div>

            <div className={styles.tableWrapper}>
                {comics.map(story => (
                    <div key={story.uuid} className={styles.storyCard}>
                        <div className={styles.storyRow}>
                            <div className={styles.imageBox}>
                                <img src={story.image} alt={story.title} className={styles.image} />
                                <div className={styles.imageOverlay}>
                                    <div className={styles.viewCount}>
                                        <Eye size={12} />
                                        <span>{story.views.toLocaleString()}</span>
                                    </div>
                                </div>
                            </div>


                            <div className={styles.storyInfoWrapper}>
                                <div className={styles.storyInfo}>
                                    <h3 className={styles.storyTitle}>{story.title}</h3>


                                    <div className={styles.meta}>
                                        <span>{story.chapters} chương</span>
                                        <span>•</span>
                                        <span>{story.views.toLocaleString()} lượt xem</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>

    );

    const renderUsers = () => (
        <div className={styles.pageStack}>
            <div className={styles.statsGrid}>
                <div className={styles.card}>
                    <div className={styles.statRowHeader}>
                        <Users size={18} className={styles.iconBlue} />
                        <span className={styles.cardLabel}>Tổng người dùng</span>
                    </div>
                    <div className={styles.cardValue}>{dataAccounts.content.length}</div>
                </div>
                <div className={styles.card}>
                    <div className={styles.statRowHeader}>
                        <span className={`${styles.statusDot} ${styles.statusGreen}`} />
                        <span className={styles.cardLabel}>Đang hoạt động</span>
                    </div>
                    <div className={styles.cardValue}>
                        {dataAccounts.content.filter(u => u.status === 'NORMAL').length}
                    </div>
                </div>
                <div className={styles.card}>
                    <div className={styles.statRowHeader}>
                        <span className={`${styles.statusDot} ${styles.statusYellow}`} />
                        <span className={styles.cardLabel}>Bị cảnh báo</span>
                    </div>
                    <div className={styles.cardValue}>
                        {dataAccounts.content.filter(u => u.status === 'WARNING').length}
                    </div>
                </div>
                <div className={styles.card}>
                    <div className={styles.statRowHeader}>
                        <span className={`${styles.statusDot} ${styles.statusRed}`} />
                        <span className={styles.cardLabel}>Bị ban</span>
                    </div>
                    <div className={styles.cardValue}>
                        {dataAccounts.content.filter(u => u.status === 'BAN').length}
                    </div>
                </div>
            </div>

            <div className={styles.card}>
                <div className={styles.cardTitleRow}>
                    <h2 className={styles.cardTitle}>Danh sách người dùng</h2>
                    <div className={styles.tableActions}>
                        <input
                            className={styles.searchInput}
                            placeholder="Tìm kiếm..."
                            type="text"
                            value={searchAccounts}
                            onChange={handleSearchChange}
                        />
                    </div>

                </div>

                <div className={styles.tableWrapper}>
                    <table className={styles.table}>
                        <thead>
                            <tr>
                                <th>Tài khoản</th>
                                <th>Email</th>
                                <th>Quyền</th>
                                <th>Trạng thái</th>
                                <th>Ngày tham gia</th>
                                <th className={styles.textRight}>Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            {(searchAccounts ? filteredAccounts : dataAccounts.content).map(user => (
                                <tr key={user.uuid}>
                                    <td>
                                        <div className={styles.userCell}>
                                            <img alt="avatar" src={user.avatar} className={styles.userAvatar}></img>
                                            <span>{user.fullName}</span>
                                        </div>
                                    </td>
                                    <td className={styles.muted}>{user.email}</td>
                                    <td>
                                        <span
                                            className={`${styles.badge} ${user.role === 'staff' ? styles.badgeBlue : styles.badgeGray
                                                }`}
                                        >
                                            {user.role === 'staff' ? 'Nhân viên' : 'Người dùng'}
                                        </span>
                                    </td>
                                    <td>
                                        <div className={styles.statusCell}>
                                            <span
                                                className={`${styles.statusDot} ${user.status === 'NORMAL'
                                                    ? styles.statusGreen
                                                    : user.status === 'WARNING'
                                                        ? styles.statusYellow
                                                        : styles.statusRed
                                                    }`}
                                            />
                                            <span
                                                className={
                                                    user.status === 'NORMAL'
                                                        ? styles.statusTextGreen
                                                        : user.status === 'WARNING'
                                                            ? styles.statusTextYellow
                                                            : styles.statusTextRed
                                                }
                                            >
                                                {user.status === 'NORMAL'
                                                    ? 'Hoạt động'
                                                    : user.status === 'WARNING'
                                                        ? 'Cảnh báo'
                                                        : 'Bị ban'}
                                            </span>
                                        </div>
                                    </td>
                                    <td className={styles.muted}>{user.created}</td>
                                    <td className={styles.textRight}>
                                        <div className={styles.actionsRow}>
                                            {user.status === 'NORMAL' && (
                                                <button
                                                    onClick={() => handleUserAction(user.uuid, 'WARNING')}
                                                    className={styles.warningButtonSm}
                                                >
                                                    Cảnh báo
                                                </button>
                                            )}
                                            {user.status === 'WARNING' && (
                                                <>
                                                    <button
                                                        onClick={() =>
                                                            handleUserAction(user.uuid, 'remove-warning')
                                                        }
                                                        className={styles.successButtonSm}
                                                    >
                                                        Bỏ cảnh báo
                                                    </button>
                                                    <button
                                                        onClick={() => handleUserAction(user.uuid, 'BAN')}
                                                        className={styles.dangerButtonSm}
                                                    >
                                                        Ban
                                                    </button>
                                                </>
                                            )}
                                            {user.status === 'BAN' && (
                                                <button
                                                    onClick={() => handleUserAction(user.uuid, 'remove-ban')}
                                                    className={styles.successButtonSm}
                                                >
                                                    Gỡ ban
                                                </button>
                                            )}
                                        </div>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>

                <div className={styles.paginationRow}>
                    <span className={styles.muted}>
                        Hiển thị {dataAccounts.currentPageSize} người dùng
                    </span>
                    <div className={styles.paginationButtons}>
                        <CustomPagination count={pageAccounts.totalPages} page={pageAccounts} onChange={handleChangePageAccounts} />
                    </div>
                </div>
            </div>
        </div>
    );

    const renderPlaceholder = () => {
        const active = menuItems.find(m => m.id === activeMenu);
        const Icon = active?.icon || FileText;
        return (
            <div className={styles.placeholderCard}>
                <div className={styles.placeholderIcon}>
                    <Icon size={40} />
                </div>
                <h2 className={styles.placeholderTitle}>{active?.name}</h2>
                <p className={styles.placeholderText}>Trang này đang được phát triển…</p>
            </div>
        );
    };

    const handleLogout = () => {
        logout();
        navigate('/dashboard/login');
    };

    return (

        <div className={styles.dashboard}>
            {notification && (
                <Notification
                    key={notification.key}
                    success={notification.success}
                    title={notification.title}
                    message={notification.message}
                />
            )}
            <Spinner visible={spinner} />
            <aside
                className={`${styles.sidebar} ${sidebarOpen ? '' : styles.sidebarCollapsed
                    }`}
            >
                <div className={styles.sidebarHeader}>
                    <div
                        className={`${styles.logoArea} ${sidebarOpen ? '' : styles.logoHidden
                            }`}
                    >
                        <div className={styles.logoIcon}>
                            <BookOpen size={22} />
                        </div>
                        <span className={styles.logoText}>TheGioiTruyenTranh</span>
                    </div>
                    <button
                        className={styles.sidebarToggleButton}
                        onClick={() => setSidebarOpen(prev => !prev)}
                    >
                        {sidebarOpen ? <X size={18} /> : <Menu size={18} />}
                    </button>
                </div>

                <nav className={styles.menu}>
                    {menuItems.map(item => {
                        const Icon = item.icon;
                        const isActive = activeMenu === item.id;
                        return (
                            <button
                                key={item.id}
                                className={`${styles.menuItem} ${isActive ? styles.menuItemActive : ''
                                    }`}
                                onClick={() => setActiveMenu(item.id)}
                                title={!sidebarOpen ? item.name : undefined}
                            >
                                <Icon size={18} className={styles.menuIcon} />
                                {sidebarOpen && <span>{item.name}</span>}
                            </button>
                        );
                    })}
                </nav>

                <div className={styles.sidebarFooter}>
                    <div className={styles.userWrapper}>
                        <div className={styles.userBox}>
                            <img
                                src={
                                    user?.avatar ||
                                    "https://i.pinimg.com/736x/7d/b9/56/7db956d51da0e02f621e011879fcef37.jpg"
                                }
                                alt="avatar"
                                className={styles.userAvatar}
                            />
                            {sidebarOpen && (
                                <div className={styles.userInfo}>
                                    <span className={styles.userName}>
                                        {user?.firstName + " " + user?.lastName}
                                    </span>
                                    <span className={styles.userEmail}>{user?.email}</span>
                                </div>
                            )}
                            <div className={styles.actionGroup}>

                                <ReusableButton
                                    onClick={handleLogout}
                                    className={styles.logout}
                                    text={<i className={`fi fi-rs-sign-out-alt ${styles.iconLogout}`}></i>}
                                />
                            </div>
                        </div>
                    </div>
                </div>

            </aside>

            <main className={styles.main}>
                <header className={styles.header}>
                    <div>
                        <h1 className={styles.pageTitle}>
                            {menuItems.find(m => m.id === activeMenu)?.name || 'Dashboard'}
                        </h1>
                        <p className={styles.pageSubtitle}>
                            Thống kê và phân tích hoạt động website
                        </p>
                    </div>
                    <div className={styles.timeBox}>
                        <span className={styles.timeText}>
                            {time.toLocaleTimeString('vi-VN')}
                        </span>
                        <span className={styles.dateText}>
                            {time.toLocaleDateString('vi-VN', {
                                weekday: 'long',
                                year: 'numeric',
                                month: 'long',
                                day: 'numeric',
                            })}
                        </span>
                    </div>
                </header>

                <section className={styles.content}>
                    {activeMenu === 'home' && renderHome()}
                    {activeMenu === 'crawl' && renderCrawl()}
                    {activeMenu === 'comics' && renderComics()}
                    {activeMenu === 'user' && renderUsers()}
                    {activeMenu !== 'home' &&
                        activeMenu !== 'comics' &&
                        activeMenu !== 'crawl' &&
                        activeMenu !== 'user' &&
                        renderPlaceholder()}
                </section>
            </main>
        </div>
    );
};

export default Dashboard;
