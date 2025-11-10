import {useEffect, useState} from 'react';
import styles from './index.module.css';
import AccountApi from '../../../api/Account';
import {useLocation, useNavigate} from 'react-router-dom';
import Spinner from '../../../components/Spinner/Spinner';
import {useAuth} from "../../../context/AuthContext";
import {PageLocation} from "../../../hooks/PageLocation";
import Notification from "../../../components/Notification/Notification";
import {useGoogleLogin} from '@react-oauth/google';

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

const GoogleIcon = () => (
  <svg 
      xmlns="http://www.w3.org/2000/svg" 
      viewBox="0 0 24 24"
      className={styles.iconGoogle}
    >
      <path d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z" fill="#4285F4"></path>
      <path d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z" fill="#34A853"></path>
      <path d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l3.66-2.84z" fill="#FBBC05"></path>
      <path d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z" fill="#EA4335"></path>
  </svg>
);

const FacebookIcon = () => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    viewBox="0 0 24 24"
    className={styles.iconFacebook}
  >
    <path
      fill="#1877F2"
      d="M24 12.073C24 5.404 18.627 0 12 0S0 5.404 0 12.073c0 5.991 4.388 10.954 10.125 11.854v-8.385H7.078v-3.469h3.047V9.414c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953h-1.513c-1.49 0-1.953.928-1.953 1.879v2.247h3.328l-.532 3.469h-2.796v8.385C19.612 23.027 24 18.064 24 12.073z"
    />
  </svg>
);

export default function Login() {
  const navigate = useNavigate();
  const location = useLocation();
  const status = location.state?.status ?? false;
  const { from } = PageLocation();
  const [showPassword, setShowPassword] = useState(false);
  const [username, setUserName] = useState('');
  const [password, setPassword] = useState('');
  const { login } = useAuth();
  const [loading, setLoading] = useState(false);
  const [notification, setNotification] = useState(false);
  const [isProcessing, setIsProcessing] = useState(false);

  useEffect(() => {
        if (window.FB) {
            window.FB.init({
                appId: import.meta.env.VITE_FACEBOOK_CLIENT_ID,
                cookie: true,
                xfbml: true,
                version: "v21.0",
            });
            return;
        }

        const script = document.createElement("script");
        script.src = "https://connect.facebook.net/en_US/sdk.js";
        script.async = true;
        script.defer = true;
        script.crossOrigin = "anonymous";
        script.onload = () => {
            window.FB.init({
                appId: import.meta.env.VITE_FACEBOOK_CLIENT_ID,
                cookie: true,
                xfbml: true,
                version: "v21.0",
            });
        };
        document.body.appendChild(script);
  }, []);

  useEffect(() => {
        if (status) {
            setNotification({
                key: Date.now(),
                success: true,
                title: "Yêu cầu thành công !!!",
                message: "Đăng ký thành công",
            });
        }
    }, [status]);

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
          navigate(from, { replace: true });
            login({
                uuid: res?.data?.uuid || "",
                firstName: res?.data?.firstName || "",
                lastName: res?.data?.lastName || "",
                email: res?.data?.email || "",
                phone: res?.data?.phone || "",
                address: res?.data?.address || "",
                avatar: res?.data?.avatar || "",
                status: res?.data?.status || "",
            });
            localStorage.setItem('accessToken', res.data.accessToken);
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

  const handleLoginGoogle = useGoogleLogin({
        onSuccess: async (codeResponse) => {
            if (isProcessing) return;
            setIsProcessing(true);
            try {
                setLoading(true);
                const res = await AccountApi.loginGoogle({code: codeResponse.code});
                if (res && res.data) {
                    localStorage.setItem("accessToken", res.data.accessToken);

                    login({
                        uuid: res?.data?.uuid || "",
                        firstName: res?.data?.firstName || "",
                        lastName: res?.data?.lastName || "",
                        email: res?.data?.email || "",
                        phone: res?.data?.phone || "",
                        address: res?.data?.address || "",
                        avatar: res?.data?.avatar || "",
                        status: res?.data?.status || "",
                    })
                    setLoading(false);
                    navigate(from, {replace: true});
                }
            } catch {
                setNotification({
                    key: Date.now(),
                    success: false,
                    title: "Yêu cầu thất bại !!!",
                    message: "Email đã được đăng ký",
                });
            } finally {
                setIsProcessing(false);
                setLoading(false);
            }
        },
        flow: 'auth-code',
    });

    const handleLoginFacebook = () => {
        FB.login(function (response) {
            if (response.status === "connected" && response.authResponse) {
                FB.getLoginStatus(function (statusResponse) {
                    if (statusResponse.status === "connected") {
                        handleFacebookResponse(statusResponse.authResponse.accessToken);
                    }
                });
            }
        }, { scope: "public_profile,email" });
    };

  const handleFacebookResponse = async (accessToken) => {
        try {
            setLoading(true);
            const res = await AccountApi.loginFacebook({ accessToken });

            if (res) {
                localStorage.setItem("accessToken", res.data.accessToken);

                login({
                    uuid: res?.data?.uuid || "",
                    firstName: res?.data?.firstName || "",
                    lastName: res?.data?.lastName || "",
                    email: res?.data?.email || "",
                    phone: res?.data?.phone || "",
                    address: res?.data?.address || "",
                    avatar: res?.data?.avatar || "",
                    status: res?.data?.status || "",
                });

                setLoading(false);
                navigate(from, { replace: true });
            }
        } catch {
            setNotification({
                key: Date.now(),
                success: false,
                title: "Yêu cầu thất bại !!!",
                message: "Email đã được đăng ký",
            });
        }finally {
            setLoading(false);
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
          <h1>Chào mừng trở lại</h1>
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
        <div className={styles.divider}>
          <span>Hoặc tiếp tục với</span>
        </div>
        <div className={styles.social}>
            <button className={styles.socialBtn} onClick={() => {handleLoginGoogle()}}>
                <span><GoogleIcon /></span>
            </button>
            <button className={styles.socialBtn} onClick={handleLoginFacebook}>
                <span><FacebookIcon /></span>
            </button>
        </div>
        <div className={styles.footer}>
          <p>Bạn chưa có tài khoản ? <a href="/Register">Đăng ký ngay</a></p>
          <a href="#">Quên mật khẩu?</a>
        </div>
      </div>
    </div>
  );
}
