import {useEffect, useRef, useState} from 'react';
import styles from './EmailVerify.module.css';
import iconEmail from '../../../assets/images/mail.png';
import {Notification, Spinner, useAuth} from "@comics/shared";
import EmailVerifyApi from '../../../api/EmailVerify';
import {useLocation, useNavigate} from 'react-router-dom';
import AccountApi from "../../../api/Account.jsx";

export default function TwoStep() {
  const location = useLocation();
  const navigate = useNavigate();
  const email = location.state?.email || "";
  const password = location.state?.password || "";
  const username = location.state?.username || "";
  const [code, setCode] = useState(new Array(6).fill(''));
  const [focusedIndex, setFocusedIndex] = useState(0);
  const inputRefs = useRef([]);
  const [loading, setLoading] = useState(false);
  const [notification, setNotification] = useState(false);
  const redirectTo = location.state?.redirectTo || '/';
  const updatedTemp = location.state?.updatedTemp;
  const {user, setUser} = useAuth();

  useEffect(() => {
    if (!email) {
      navigate("*");
    }
  }, [email]);

  const DURATION = 90;
  const [timeLeft, setTimeLeft] = useState(0);
  const [resendOtp, setResendOtp] = useState(false);
  const intervalRef = useRef(null);

  const updateTime = () => {
    const end = Number(localStorage.getItem("otp_end") || 0);
    const diff = Math.ceil((end - Date.now()) / 1000);

    if (diff <= 0) {
      clearInterval(intervalRef.current);
      intervalRef.current = null;
      localStorage.removeItem("otp_end");
      setTimeLeft(0);
      setResendOtp(true);
    } else {
      setTimeLeft(diff);
    }
  };

  useEffect(() => {
    const savedEnd = Number(localStorage.getItem("otp_end") || 0);
    const now = Date.now();

    if (savedEnd > now) {
      setResendOtp(false);
      updateTime();
      intervalRef.current = setInterval(updateTime, 500);
    } else {
      const newEnd = now + DURATION * 1000;
      localStorage.setItem("otp_end", String(newEnd));
      setResendOtp(false);
      setTimeLeft(DURATION);
      intervalRef.current = setInterval(updateTime, 500);
    }

    return () => clearInterval(intervalRef.current);
  }, []);

  const reset = () => {
    const newEnd = Date.now() + DURATION * 1000;
    localStorage.setItem("otp_end", String(newEnd));
    setTimeLeft(DURATION);
    setResendOtp(false);
    clearInterval(intervalRef.current);
    intervalRef.current = setInterval(updateTime, 500);
  };


  const handleChange = (element, index) => {
    if (isNaN(Number(element.value)) || element.value === ' ') {
      element.value = '';
      return;
    }
    const newCode = [...code];
    newCode[index] = element.value;
    setCode(newCode);

    if (element.value && index < 5) {
      inputRefs.current[index + 1]?.focus();
    }
    if (newCode.every(c => c !== '')) {
      const fullOtp = newCode.join('');
      setLoading(true);
      setTimeout(() => {
        fetchVerifyOtp(fullOtp);
      }, 200);
    }
  };

  const fetchVerifyOtp = async (otp) => {
    try {
        setLoading(true);
        const resVerify = await EmailVerifyApi.Verify({email: email, otp: otp});
        if (resVerify) {
          if (redirectTo === '/profile' && updatedTemp) {
            const res = await AccountApi.updateAccount(updatedTemp, user.uuid);
            if (res) {
              const updatedUser = { ...user, ...updatedTemp };
              localStorage.setItem('user', JSON.stringify(updatedUser));
              setUser(updatedUser);
            }
          } else if (redirectTo === '/login') {
            await AccountApi.register({ username: username, password: password, email: email })
          }
          navigate(redirectTo, { replace: true, state:{status: true} });
        }
    } catch {
      if (timeLeft === 0) {
        setNotification({
          key: Date.now(),
          success: false,
          title: "Yêu cầu thất bại !!!",
          message: "Mã xác thực đã hết hạn",
        });
      }
      setNotification({
        key: Date.now(),
        success: false,
        title: "Yêu cầu thất bại !!!",
        message: "Mã xác thực không đúng",
      });
    } finally { setLoading(false) }
  };

  const handleKeyDown = (e, index) => {
    if (e.key === 'Backspace' && !code[index] && index > 0) {
      inputRefs.current[index - 1]?.focus();
    }
  };

  const handlePaste = (e) => {
    e.preventDefault();
    const pasteData = e.clipboardData.getData('text').slice(0, 6);
    if (!/^\d+$/.test(pasteData)) return;
    const newCode = new Array(6).fill('');
    for (let i = 0; i < pasteData.length; i++) newCode[i] = pasteData[i];
    setCode(newCode);
    inputRefs.current[Math.min(pasteData.length - 1, 5)]?.focus();
  };

  useEffect(() => {
    inputRefs.current[0]?.focus();
  }, []);

  const fetchResendOtp = async () => {
    try {
      await EmailVerifyApi.SendOtp({email});
      setNotification({
        key: Date.now(),
        success: true,
        title: "Yêu cầu thành công !!!",
        message: "Đã gửi lại mã xác thực",
      });
      reset();
    } catch (error) {
      setNotification({
        key: Date.now(),
        success: false,
        title: "Yêu cầu thất bại !!!",
        message: "Đã có lỗi, vui lòng báo cáo admin",
      });
    }
  }

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
      <div className={styles.card}>
        <div className={styles.logoBox}>
          <img
            src={iconEmail}
            alt="Logo"
            className={styles.logo}
          />
        </div>

        <h1 className={styles.title}>Xác thực email</h1>
        <p className={styles.subtitle}>
          Chúng tôi đã gửi mã gồm 6 chữ số đến {email}
        </p>
        <p className={styles.label}>Nhập mã bạn nhận được</p>
        <div className={styles.inputGroup} onPaste={handlePaste}>
          {code.map((data, index) => (
            <input
              key={index}
              ref={(el) => (inputRefs.current[index] = el)}
              type="tel"
              maxLength={1}
              value={data}
              placeholder="•"
              onChange={(e) => handleChange(e.target, index)}
              onKeyDown={(e) => handleKeyDown(e, index)}
              onFocus={(e) => {
                e.target.select();
                setFocusedIndex(index);
              }}
              onBlur={() => setFocusedIndex(-1)}
              className={`${styles.input} ${focusedIndex === index ? styles.inputFocus : ''
                }`}
            />
          ))}
        </div>
        <p className={styles.resend}>
          Không nhận được mã?
          {resendOtp ? (
              <button
                  className={styles.resendBtn}
                  onClick={() => {
                      fetchResendOtp();
                      reset();
                  }}
              >
                  Gửi lại mã
              </button>
          ) : (
            <button className={styles.resendBtn}>Gửi lại mã sau {timeLeft}s</button>
          )}
        </p>
      </div>
    </div>
  );
}
