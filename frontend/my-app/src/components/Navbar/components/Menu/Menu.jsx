import styles from "./Menu.module.css";
import { Link, useLocation } from "react-router-dom";

export default function NavItem({ name }) {
  const links = {
    "Trang chủ": "/",
    "Theo dõi": "/follow?page=1",
    "Lịch sử": "/history?page=1",
  };

  const url = links[name] || "#";
  const location = useLocation();

  const isActive =
    (name === "Trang chủ" &&
      (location.pathname === "/" || location.pathname === "/home")) ||
    location.pathname === url;

  return (
    <li>
      <Link
        to={url}
        className={`${styles.navLink} ${isActive ? styles.active : ""}`}
      >
        {name}
      </Link>
    </li>
  );
}
