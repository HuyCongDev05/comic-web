'use client';
import React, { useState } from 'react';
import styles from './Login.module.css';

const MailIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor" viewBox="0 0 24 24">
    <path d="M20 4H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2Zm0 2v.01L12 13 4 6.01V6Zm-16 12V8.236l8 6.5 8-6.5V18Z"/>
  </svg>
);

const KeyIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
    <circle cx="7.5" cy="15.5" r="5.5"></circle>
    <path d="m21 2-9.6 9.6"></path>
    <path d="m15.5 7.5 3 3L22 7l-3-3"></path>
  </svg>
);

const EyeIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor" viewBox="0 0 24 24">
    <path d="M12 5C7 5 2.73 8.11 1 12c1.73 3.89 6 7 11 7s9.27-3.11 11-7c-1.73-3.89-6-7-11-7Zm0 12a5 5 0 1 1 0-10 5 5 0 0 1 0 10Zm0-8a3 3 0 1 0 0 6 3 3 0 0 0 0-6Z"/>
  </svg>
);

const EyeOffIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor" viewBox="0 0 24 24">
    <path d="m2 4 1.5 1.5L5.3 7.3A11.91 11.91 0 0 0 1 12c1.73 3.89 6 7 11 7 2.1 0 4.05-.53 5.8-1.46L18.5 20.5 20 19 3.5 2.5 2 4Zm10 13a5 5 0 0 1-5-5c0-.64.12-1.26.34-1.82l1.54 1.54A3 3 0 0 0 12 15a3 3 0 0 0 1.28-.29l1.53 1.53A4.97 4.97 0 0 1 12 17Zm9-5c-.7 1.56-2.05 3.03-3.82 4.05l-1.46-1.46A5 5 0 0 0 15 12c0-.64-.12-1.26-.34-1.82l1.46-1.46C18.95 9 20.3 10.44 21 12Z"/>
  </svg>
);

const GoogleIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 48 48">
    <path fill="#fbc02d" d="M43.6 20.5H42V20H24v8h11.3C34.7 32 30.1 36 24 36c-6.6 0-12-5.4-12-12s5.4-12 12-12c3.1 0 5.9 1.2 8.1 3.1l5.7-5.7C34.3 6.2 29.4 4 24 4 12.9 4 4 12.9 4 24s8.9 20 20 20c11.3 0 19.5-8 19.5-19.3 0-1.3-.1-2.2-.3-3.2z"/>
    <path fill="#e53935" d="M6.3 14.7l6.6 4.8C14.3 16.3 18.8 12 24 12c3.1 0 5.9 1.2 8.1 3.1l5.7-5.7C34.3 6.2 29.4 4 24 4 16 4 9.1 8.5 6.3 14.7z"/>
    <path fill="#4caf50" d="M24 44c5.9 0 10.9-1.9 14.6-5.1l-6.7-5.4C30.3 34.5 27.4 36 24 36c-6.1 0-10.7-4-11.3-9.5l-6.6 5.1C9 39.5 15.9 44 24 44z"/>
    <path fill="#1565c0" d="M43.6 20.5H42V20H24v8h11.3c-.5 3.5-3.7 8-11.3 8-6.1 0-10.7-4-11.3-9.5l-6.6 5.1C9 39.5 15.9 44 24 44c11.3 0 19.5-8 19.5-19.3 0-1.3-.1-2.2-.3-3.2z"/>
  </svg>
);

const FacebookIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="#1877F2" viewBox="0 0 48 48">
    <path d="M24 4C12.954 4 4 12.954 4 24c0 9.991 7.339 18.257 17 19.8V30h-5v-6h5v-4.5c0-5 3-7.8 7.5-7.8 2.2 0 4.5.4 4.5.4v5h-2.5c-2.5 0-3.3 1.5-3.3 3V24h5.6l-.9 6h-4.7v13.8C36.661 42.257 44 33.991 44 24 44 12.954 35.046 4 24 4z"/>
  </svg>
);

function Login() {
  const [showPassword, setShowPassword] = useState(false);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  return (
    <div className={styles.container}>
      <div className={styles.box}>
        <form className={styles.form}>
          <div className={styles.group}>
            <label>Email Address</label>
            <div className={styles.inputWrap}>
              <MailIcon />
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="Enter your email" />
            </div>
          </div>

          <div className={styles.group}>
            <label>Password</label>
            <div className={styles.inputWrap}>
              <KeyIcon />
              <input
                type={showPassword ? 'text' : 'password'}
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Enter your password" />
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                className={styles.toggleBtn}
              >
                {showPassword ? <EyeOffIcon /> : <EyeIcon />}
              </button>
            </div>
          </div>

          <div className={styles.options}>
            <label className={styles.remember}>
              <input type="checkbox" /> Remember me
            </label>
            <a href="#">Forgot password?</a>
          </div>

          <button type="submit" className={styles.submitBtn}>
            Sign In
          </button>
        </form>

        <div className={styles.divider}>
          <span>Or continue with</span>
        </div>

        <div className={styles.socials}>
          <button><GoogleIcon /><span>Google</span></button>
          <button><FacebookIcon /><span>Facebook</span></button>
        </div>

        <p className={styles.signup}>
          Don’t have an account? <a href="#">Sign up for free</a>
        </p>
      </div>
    </div>
  );
}

export default Login;
