import styles from './EmailVerify.module.css'
import { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import Spinner from '../../../components/Spinner/Spinner';
import EmailVerifyApi from '../../../api/EmailVerify';

export default function EmailVerify() {
  const navigate = useNavigate();
  const location = useLocation();
  const email = location.state?.email || "";
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const [otp, setOtp] = useState('');
  const newErrors = {};

  const validate = () => {
    if (!otp.trim()) {
      newErrors.otp = "Không được để trống mã xác thực";
    } else {
      const regex = /^\d{6}$/;
      if (!regex.test(otp)) {
        newErrors.otp = "Mã xác thực phải có 6 chữ số";
      }

    }
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (validate()) {
      try {
        setLoading(true);
        const res = await EmailVerifyApi.Verify({ email, otp });
        if (res.success) {
          navigate('/');
          setLoading(false);
        }
      } catch {
        // console.clear();
        newErrors.otp = "Mã xác thực không hợp lệ";
      } finally { setLoading(false); }
    }
  };

  return (
      <div className={styles.container}>
      <Spinner visible = {loading}/>
      <div className={styles.box}>

        <div className={styles.header}>
          <h1>Xác thực email</h1>
          <p>Nhập mã xác thực đã gửi về email của bạn để hoàn tất đăng ký</p>
        </div>
        <form className={styles.form}>
          <div>
            <input
              type="text"
              value={otp}
              onChange={e => setOtp(e.target.value)}
              placeholder="Nhập mã xác thực của bạn"
            />
            {errors.otp && <p className={styles.error}>{errors.otp}</p>}
          </div>
          <button
            type="submit"
            className={styles.btnVerify}
            onClick={handleSubmit}
          >
            <span>Xác thực</span>
          </button>
        </form>
      </div>
    </div>
  );
}