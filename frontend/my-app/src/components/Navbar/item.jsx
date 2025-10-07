import "./navbar.css";
import { Link } from "react-router-dom";

export default function NavItem({ name }) {
  const links = {
    "Trang chủ": "/",
    "Theo dõi": "/follow",
    "Thể loại": "/category",
    "Lịch sử": "/history",
  };

  const url = links[name] || "#";

  return (
    <li>
      <Link to={url}>{name}</Link>
    </li>
  );
}