import "./navbar.css";
import logo from "../../assets/icons/logo.png";
import NavItem from "./item";
import { Link } from "react-router-dom";

export default function Navbar() {
  return (
    <div className="navbar">
        <Link to="/" className="logo-link">
            <img src={logo} alt="logo" className="logo" />
        </Link>
        <ul className="menu">
            <NavItem name="Trang chủ" />
            <NavItem name="Theo dõi" />
            <NavItem name="Thể loại" />
            <NavItem name="Lịch sử" />
        </ul>
    </div>
  );
}
