import "./navbar.css";
import logo from "../../assets/icons/logo.png";
import NavItem from "./menu";
import { Link } from "react-router-dom";

export default function Navbar() {
  return (
    <div className="navbar">
      <Link to="/" className="logo-link">
        <img className = "logo" src={logo} alt="logo"/>
      </Link>
      <ul className="menu-function">
        <NavItem name="Trang chủ" />
        <NavItem name="Theo dõi" />
        <NavItem name="Thể loại" />
        <NavItem name="Lịch sử" />
      </ul>
      <ul className="icons-function">
        <li>
          <Link to="/Help"><i className="fi fi-rr-interrogation"></i></Link>
        </li>
        <li>
          <Link to="/Language"><i className="fi fi-rs-globe"></i></Link>
        </li>
        <li className="dropdown">
          <Link to="/Account"><i className="fi fi-rr-user"></i></Link>
          <ul className="dropdown-menu">
            <li>
              <Link to = "/Register">Đăng ký</Link>
            </li>
            <li>
              <Link to="/Login">Đăng nhập</Link>
            </li>
            <li>
              <Link to="/Profile">Thông tin cá nhân</Link>
            </li>
          </ul>
        </li>
      </ul>
    </div>
  );
}
