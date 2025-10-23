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
      className={`${styles.notification} ${closing ? styles.slideOut : ""} ${success ? styles.success : styles.error
        }`}
    >
      <div className={styles.icon}>{success ? "✔" : "✖"}</div>
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
