export default function Button({ children ,click}) {
    return (
        <button onClick={click} className="btn">{children}</button>
    );
};
