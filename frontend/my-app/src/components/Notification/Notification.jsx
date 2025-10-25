import React, { useEffect, useState } from "react";
import styles from "./Notification.module.css";

const Notification = ({
  title = "",
  message = "",
  success = true,
  duration = 2000,
}) => {
  const [progress, setProgress] = useState(100);
  const [closing, setClosing] = useState(false);
  const [visible, setVisible] = useState(true);
  const Icon = success ? (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      xmlnsXlink="http://www.w3.org/1999/xlink"
      fill="#288f24ff"
      width="20px"
      height="20px"
      viewBox="0 0 36 36"
      version="1.1"
      preserveAspectRatio="xMidYMid meet"
    >
      <path
        className="clr-i-outline clr-i-outline-path-1"
        d="M18,2A16,16,0,1,0,34,18,16,16,0,0,0,18,2Zm0,30A14,14,0,1,1,32,18,14,14,0,0,1,18,32Z"
      />
      <path
        className="clr-i-outline clr-i-outline-path-2"
        d="M28,12.1a1,1,0,0,0-1.41,0L15.49,23.15l-6-6A1,1,0,0,0,8,18.53L15.49,26,28,13.52A1,1,0,0,0,28,12.1Z"
      />
      <rect x="0" y="0" width="20" height="20" fillOpacity="0" />
    </svg>
  ) : (
    <svg xmlns="http://www.w3.org/2000/svg" fill="none" width="20" height="20" viewBox="0 0 36 36">
      <circle cx="18" cy="18" r="16" stroke="red" strokeWidth="2" fill="none" />
      <line x1="12" y1="12" x2="24" y2="24" stroke="red" strokeWidth="2" strokeLinecap="round" />
      <line x1="24" y1="12" x2="12" y2="24" stroke="red" strokeWidth="2" strokeLinecap="round" />
    </svg>
  );


  useEffect(() => {
    const interval = setInterval(() => {
      setProgress((prev) => {
        const next = prev - 100 / (duration / 50);
        return next <= 0 ? 0 : next;
      });
    }, 50);

    const timer = setTimeout(() => {
      setClosing(true);
      setTimeout(() => setVisible(false), 500);
    }, duration);

    return () => {
      clearInterval(interval);
      clearTimeout(timer);
    };
  }, [duration]);

  if (!visible) return null;

  return (
    <div
      className={`${styles.notification} ${closing ? styles.slideOut : ""} ${success ? styles.success : styles.error}`}
    >
      <div className={styles.icon}>{Icon}</div>
      <div className={styles.text}>
        <strong className={styles.title}>{title}</strong>
        <p>{message}</p>
      </div>
      <div className={styles.progressBar}>
        <div
          className={styles.progress}
          style={{ width: `${progress}%` }}
        ></div>
      </div>
    </div>
  );
};

export default Notification;
