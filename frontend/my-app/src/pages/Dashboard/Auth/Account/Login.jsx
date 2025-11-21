import { useState } from 'react';
import styles from './login.module.css';
import AccountApi from '../../../../api/Account';
import {useNavigate } from 'react-router-dom';
import Spinner from '../../../../components/Spinner/Spinner';
import { useAuth } from "../../../../context/AuthContext";
import Notification from "../../../../components/Notification/Notification";
import { jwtDecode } from "jwt-decode";

const EyeIcon = () => (
    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" className={styles.iconEye}>
        <path d="M2 12s3-7 10-7 10 7 10 7-3 7-10 7-10-7-10-7Z" />
        <circle cx="12" cy="12" r="3" />
    </svg>
);

const EyeOffIcon = () => (
    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" className={styles.iconEye}>
        <path d="M9.88 9.88a3 3 0 1 0 4.24 4.24" />
        <path d="M10.73 5.08A10.43 10.43 0 0 1 12 5c7 0 10 7 10 7a13.16 13.16 0 0 1-1.67 2.68" />
        <path d="M6.61 6.61A13.526 13.526 0 0 0 2 12s3 7 10 7a9.74 9.74 0 0 0 5.39-1.61" />
        <line x1="2" x2="22" y1="2" y2="22" />
    </svg>
);

export default function LoginDashboard() {
    const navigate = useNavigate();
    const [showPassword, setShowPassword] = useState(false);
    const [username, setUserName] = useState('');
    const [password, setPassword] = useState('');
    const {loginAdmin} = useAuth();
    const [loading, setLoading] = useState(false);
    const [notification, setNotification] = useState(false);


    const validate = () => {
        if (!username.trim()) {
            setNotification({
                key: Date.now(),
                success: false,
                title: "Yêu cầu thất bại !!!",
                message: "Không được để trống tài khoản",
            });
            return false;
        }
        if (username.length < 6 || username.length > 20) {
            setNotification({
                key: Date.now(),
                success: false,
                title: "Yêu cầu thất bại !!!",
                message: "Tên người dùng phải ≥ 6 và < 20 kí tự",
            });
            return false;
        }

        if (!password.trim()) {
            setNotification({
                key: Date.now(),
                success: false,
                title: "Yêu cầu thất bại !!!",
                message: "Không được để trống mật khẩu",
            });
            return false;
        }

        const regexPassword = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{6,}$/;
        if (!regexPassword.test(password)) {
            setNotification({
                success: false,
                title: "Yêu cầu thất bại !!!",
                message: "Mật khẩu phải ≥ 6 ký tự, có chữ hoa, chữ thường và ký tự đặc biệt",
            });
            return false;
        }
        return true;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (validate()) {
            try {
                setLoading(true);
                const res = await AccountApi.login({ username, password });
                if (res.success) {
                    const role = jwtDecode(res.data.accessToken).authorities[0];
                    if (role !== "ADMIN") {
                        setNotification({
                            key: Date.now(),
                            success: false,
                            title: "Yêu cầu thất bại !!!",
                            message: "Không đủ quyền truy cập",
                        });
                        return;
                    }
                    loginAdmin({
                        uuid: res?.data?.uuid || "",
                        firstName: res?.data?.firstName || "",
                        lastName: res?.data?.lastName || "",
                        email: res?.data?.email || "",
                        phone: res?.data?.phone || "",
                        address: res?.data?.address || "",
                        avatar: res?.data?.avatar || "",
                        status: res?.data?.status || "",
                    });
                    localStorage.setItem('accessTokenAdmin', res.data.accessToken);
                    navigate('/dashboard');
                }
            } catch {
                setNotification({
                    key: Date.now(),
                    success: false,
                    title: "Yêu cầu thất bại !!!",
                    message: "Sai tài khoản hoặc mật khẩu",
                });
            } finally { setLoading(false); }
        }
    };

    return (
        <div className={styles.container}>
            <Spinner visible={loading} />
            {notification && (
                <Notification
                    key={notification.key}
                    success={notification.success}
                    title={notification.title}
                    message={notification.message}
                />
            )}
            <div className={styles.box}>

                <div className={styles.header}>
                    <h1>Dashboard</h1>
                    <p>Nhập thông tin của bạn để đăng nhập</p>
                </div>

                <form className={styles.form} onSubmit={handleSubmit}>
                    <div>
                        <label>Tài khoản</label>
                        <input
                            type="text"
                            value={username}
                            onChange={e => setUserName(e.target.value)}
                            placeholder="Nhập tài khoản của bạn"
                        />
                    </div>
                    <div>
                        <label>Mật khẩu</label>
                        <div className={styles.passwordWrapper}>
                            <input
                                type={showPassword ? 'text' : 'password'}
                                value={password}
                                onChange={e => setPassword(e.target.value)}
                                placeholder="Nhập mật khẩu của bạn"
                            />
                            <button
                                type="button"
                                onClick={() => setShowPassword(!showPassword)}
                                className={styles.eyeBtn}
                            >
                                {showPassword ? <EyeOffIcon /> : <EyeIcon />}
                            </button>
                        </div>
                    </div>
                    <button type="submit" className={styles.btnLoginAndRegister}>
                        <span>Đăng nhập</span>
                    </button>
                </form>
            </div>
        </div>
    );
}