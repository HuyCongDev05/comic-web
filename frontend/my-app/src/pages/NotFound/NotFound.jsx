import styles from "./NotFound.module.css";

export default function NotFound() {
  return (
    <main className={`${styles.main}`}>
      <div className={styles.textCenter}>
        <p className={styles.code}>404</p>
        <h1 className={styles.title}>Không tìm thấy trang</h1>
        <p className={styles.desc}>
          Rất tiếc, chúng tôi không tìm thấy trang bạn đang tìm kiếm.
        </p>
        <div className={styles.buttons}>
          <a href="/" className={styles.homeBtn}>
            Quay lại trang chủ
          </a>
          <a href="/support-and-social" className={styles.support}>
            Liên hệ hỗ trợ <span aria-hidden="true">&rarr;</span>
          </a>
        </div>
      </div>
    </main>
  );
}
