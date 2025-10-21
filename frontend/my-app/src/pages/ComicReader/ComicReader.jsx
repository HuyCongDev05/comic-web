import styles from "./ComicReader.module.css";
import ReusableButton from "./../../components/Button/Button";

export default function ComicReader() {
  const chapters = Array.from({ length: 50 }, (_, i) => i + 1);
  const currentChap = 23;

  return (
    <div className={styles.readerContainer}>
      <div className={styles.topNav}>
        <ReusableButton text="⬅ Chap trước" onClick={() => console.log("Clicked")} />
        <ReusableButton text="Chap sau ➡" onClick={() => console.log("Clicked")} />
      </div>

      <div className={styles.imageWrapper}>
        <img
          src="https://example.com/your-image.jpg"
          alt="chapter"
          className={styles.chapterImage}
        />
      </div>

      <div className={styles.bottomNav}>
        <button className={styles.controllerChapter}><i class="fi fi-rs-home"></i></button>
        <button className={styles.controllerChapter}>⬅</button>

        <select defaultValue={currentChap}>
          {chapters.reverse().map((ch) => (
            <option key={ch} value={ch}>
              Chương {ch}
            </option>
          ))}
        </select>
        <button className={styles.controllerChapter}>➡</button>
        <button className={styles.followBtn}>❤️ Theo dõi</button>
      </div>
    </div>
  );
}
