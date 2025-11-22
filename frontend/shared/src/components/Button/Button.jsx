import styles from "./Button.module.css";

const ReusableButton = ({ text, onClick, className = "" }) => {
  return (
    <button
      className={`${styles.baseButton} ${className}`}
      onClick={onClick}
    >
      {text}
    </button>
  );
};

export default ReusableButton;