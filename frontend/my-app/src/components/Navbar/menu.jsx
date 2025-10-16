import "./navbar.module.css";
import { Link, useLocation } from "react-router-dom";

export default function NavItem({ name }) {
  const links = {
    "Trang chủ": "/",
    "Theo dõi": "/follow",
    "Thể loại": "/category",
    "Lịch sử": "/history",
  };

  const url = links[name] || "#";
  const location = useLocation();
  const isActive =
    (name === "Trang chủ" &&
      (location.pathname === "/" || location.pathname === "/home")) ||
    location.pathname === url;

  return (
    <li>
      <Link to={url} className={`nav-link ${isActive ? "active" : ""}`}>
        {name}
      </Link>
    </li>
  );
}