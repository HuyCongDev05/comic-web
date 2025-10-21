import styles from './Spinner.module.css';

const Spinner = ({ visible = false }) => {
  if (!visible) return null;

  return (
    <div className={styles.wrapper}>
      <div className={styles.spinner}></div>
    </div>
  );
};

export default Spinner;
