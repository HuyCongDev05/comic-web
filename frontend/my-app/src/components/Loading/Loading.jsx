import styles from './Loading.module.css';

const Loading = ({ visible = false }) => {
  if (!visible) return null;

  return (
      <div className={styles.wrapper}>
          <div className={`${styles.dot} ${styles.dot1}`}></div>
          <div className={`${styles.dot} ${styles.dot2}`}></div>
          <div className={`${styles.dot} ${styles.dot3}`}></div>
      </div>
  );
};

export default Loading;
