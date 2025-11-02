import style from './Profile.module.css';
import {useEffect, useRef, useState} from 'react';
import {Edit2, Mail, MapPin, Phone, Save, User, X} from 'lucide-react';
import HideScrollbar from "../../hooks/HideScrollbar";
import {useAuth} from "../../context/AuthContext";
import {useLocation, useNavigate} from 'react-router-dom';
import Notification from "../../components/Notification/Notification";
import EmailVerifyApi from '../../api/EmailVerify';
import Spinner from '../../components/Spinner/Spinner';
import AvatarApi from '../../api/UploadAvatar.jsx';
import AccountApi from "../../api/Account.jsx";

export default function PersonalProfile() {
    HideScrollbar();
    const location = useLocation();
    const status = location.state?.status ?? false;
    const [isEditing, setIsEditing] = useState(false);
    const {user, setUser} = useAuth();
    const navigate = useNavigate();
    const [notification, setNotification] = useState(false);
    const [loading, setLoading] = useState(false);
    const [selectedAvatarFile, setSelectedAvatarFile] = useState(null);
    if (user === null || user === undefined) {
        navigate('/');
    }

    useEffect(() => {
        if (status) {
            setNotification({
                key: Date.now(),
                success: true,
                title: "Yêu cầu thành công !!!",
                message: "Đã cập nhật thông tin thành công",
            });
        }
    }, [status]);

    const [profile, setProfile] = useState({
        firstName: user?.firstName || '',
        lastName: user?.lastName || '',
        email: user?.email || '',
        phone: user?.phone || '',
        avatar: user?.avatar || '',
        address: user?.address || '',
    });
    const fileInputRef = useRef(null);
    const [previewAvatar, setPreviewAvatar] = useState(null);
    const [tempProfile, setTempProfile] = useState({ ...profile });

    const handleEdit = () => {
        setIsEditing(true);
        setTempProfile({ ...profile });
    };

    const handleSave = async () => {
        setIsEditing(false);
        setLoading(true);

        try {
            let avatarUrl = profile.avatar;

            if (selectedAvatarFile) {
                try {
                    const data = await AvatarApi.UploadAvatar(selectedAvatarFile);
                    avatarUrl = data.secure_url;
                } catch {
                    setNotification({
                        key: Date.now(),
                        success: false,
                        title: "Yêu cầu thất bại !!!",
                        message: "Đã có lỗi, vui lòng báo cáo admin",
                    });
                }
            }
            const updatedTemp = {...tempProfile, avatar: avatarUrl};

            if (user.email !== updatedTemp.email) {
                const res = await EmailVerifyApi.SendOtp({email: updatedTemp.email});
                if (res) {
                    navigate('/email/verify', {state: {email: updatedTemp.email, redirectTo: '/profile', updatedTemp}});
                    return;
                }
            }else {
                await AccountApi.updateAccount(updatedTemp, user.uuid);
            }

            const updatedUser = {...user, ...updatedTemp};
            localStorage.setItem('user', JSON.stringify(updatedUser));
            setUser(updatedUser);
            setProfile(updatedUser);

            setNotification({
                key: Date.now(),
                success: true,
                title: "Yêu cầu thành công !!!",
                message: "Đã cập nhật thông tin thành công",
            });
        } catch (err) {
            setNotification({
                key: Date.now(),
                success: false,
                title: "Yêu cầu thất bại !!!",
                message: "Đã có lỗi, vui lòng báo cáo admin",
            });
        } finally {
            setLoading(false);
            setSelectedAvatarFile(null);
            setPreviewAvatar(null);
        }
    };

    const handleCancel = () => {
        setTempProfile({ ...profile });
        setIsEditing(false);
        setPreviewAvatar(null);
        if (fileInputRef.current) {
            fileInputRef.current.value = null;
        }
    };

    const handleChange = (field, value) => {
        setTempProfile({ ...tempProfile, [field]: value });
    };

    const handleAvatarChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            setSelectedAvatarFile(file);
            setPreviewAvatar(URL.createObjectURL(file));
        }
    };

    return (
        <div className={style.container}>
            <Spinner visible={loading} />
            {notification && (
                <Notification
                    key={notification.key}
                    success={notification.success}
                    title={notification.title}
                    message={notification.message}
                />
            )}
            <div className={style.background}>
                <div className={`${style.circle} ${style.circle1}`}></div>
                <div className={`${style.circle} ${style.circle2}`}></div>
                <div className={`${style.circle} ${style.circle3}`}></div>
            </div>

            <div className={style.wrapper}>
                <div className={style.card}>
                    <div className={style.header}>
                        <div className={style.headerContent}>
                            <div className={style.avatarWrapper}>
                                <img
                                    className={style.avatar}
                                    src={previewAvatar || user?.avatar || "https://i.pinimg.com/736x/7d/b9/56/7db956d51da0e02f621e011879fcef37.jpg"}
                                    alt="avatar"
                                    onClick={() => {
                                        if (isEditing) {
                                            fileInputRef.current.click();
                                        }
                                    }}
                                    style={{ cursor: isEditing ? "pointer" : "default", opacity: isEditing ? 1 : 0.8 }}
                                />

                                <input
                                    type="file"
                                    accept="image/*"
                                    ref={fileInputRef}
                                    onChange={handleAvatarChange}
                                    style={{ display: "none" }}
                                />

                            </div>

                            <div>
                                <div className={style.nameRow}>
                                    <h1 className={style.name}>{profile.firstName} {profile.lastName}</h1>
                                    <div className={style.verified}>
                                        <svg className={style.verifiedIcon} viewBox="0 0 24 24">
                                            <circle cx="12" cy="12" r="10" fill="#06b6d4" />
                                            <path
                                                d="M9 12l2 2 4-4"
                                                stroke="white"
                                                strokeWidth="2.5"
                                                strokeLinecap="round"
                                                strokeLinejoin="round"
                                            />
                                        </svg>
                                    </div>
                                </div>
                            </div>
                        </div>

                        {!isEditing ? (
                            <button onClick={handleEdit} className={style.editBtn}>
                                <Edit2 className={style.btnIcon} />
                                <span>Chỉnh sửa</span>
                            </button>
                        ) : (
                            <div className={style.editActions}>
                                <button onClick={handleSave} className={style.saveBtn}>
                                    <Save className={style.btnIcon} />
                                    <span>Lưu</span>
                                </button>
                                <button onClick={handleCancel} className={style.cancelBtn}>
                                    <X className={style.btnIcon} />
                                    <span>Hủy</span>
                                </button>
                            </div>
                        )}
                    </div>

                    <div className={style.content}>
                        <div className={style.grid}>
                            {[
                                {
                                    icon: <User className={style.iconPurple} />,
                                    label: "Họ",
                                    key: "firstName",
                                    color: "purple",
                                },
                                {
                                    icon: <User className={style.iconPurple} />,
                                    label: "Tên",
                                    key: "lastName",
                                    color: "purple",
                                },
                                {
                                    icon: <Mail className={style.iconCyan} />,
                                    label: "Email",
                                    key: "email",
                                    color: "cyan",
                                },
                                {
                                    icon: <Phone className={style.iconGreen} />,
                                    label: "Số điện thoại",
                                    key: "phone",
                                    color: "green",
                                },
                                {
                                    icon: <MapPin className={style.iconOrange} />,
                                    label: "Địa chỉ",
                                    key: "address",
                                    color: "orange",
                                },
                            ].map(({ icon, label, key, color }) => (
                                <div key={key} className={style.field}>
                                    <div className={`${style.fieldIcon} ${style[color]}`}>
                                        {icon}
                                    </div>
                                    <div className={style.fieldContent}>
                                        <label
                                            className={`${style.label} ${style[`label_${color}`]}`}
                                        >
                                            {label}
                                        </label>
                                        {isEditing ? (
                                            <input
                                                type="text"
                                                value={tempProfile[key]}
                                                onChange={(e) => handleChange(key, e.target.value)}
                                                className={style.input}
                                            />
                                        ) : (
                                            <p className={style.text}>{profile[key]}</p>
                                        )}
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}