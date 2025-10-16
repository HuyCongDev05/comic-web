import "./Button.module.css";

const ReusableButton = ({ text, onClick, className = "" }) => {
  return (
    <button
      className={`base-button ${className}`}
      onClick={onClick}
    >
      {text}
    </button>
  );
};
export default ReusableButton;