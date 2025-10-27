import { useState, useRef, useEffect } from 'react';
import styles from './EmailVerify.module.css';
import iconEmail from '../../../assets/icons/mail.png';
import Notification from "../../../components/Notification/Notification";
import Spinner from '../../../components/Spinner/Spinner';
import { useFetcher } from 'react-router-dom';
import EmailVerifyApi from '../../../api/EmailVerify';
import { useLocation } from 'react-router-dom';

export default function TwoStep() {
  const [code, setCode] = useState(new Array(6).fill(''));
  const [focusedIndex, setFocusedIndex] = useState(0);
  const inputRefs = useRef([]);
  const [loading, setLoading] = useState(false);
  const [notification, setNotification] = useState(false);
  const [otp, setOtp] = useState();
  const location = useLocation();
  const email = location.state?.email || "";

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
    setOtp(newCode.join(''));
    setLoading(true);
  };

  useEffect(() => {
    const fetchVerifyOtp = async () => {
      try {
        await EmailVerifyApi.Verify({ email: email, otp: otp })

      } catch (error) {
        setNotification({
          success: false,
          title: "Yêu cầu thất bại !!!",
          message: "Mã xác thực đã hết hạn",
        });
      } finally {setLoading(false)}
    };
    fetchVerifyOtp();
  },[]);

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
          Chúng tôi đã gửi mã gồm 6 chữ số đến
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
          <button className={styles.resendBtn}>Gửi lại mã</button>
        </p>
      </div>
    </div>
  );
}
