import style from "./Navbar.module.css";
import logo from "../../assets/icons/logo.png";
import NavItem from "./Menu";
import { Link } from "react-router-dom";

export default function Navbar() {
  return (
    <>
      <div className={style.navbar}>
        <Link to="/" className={style.logoLink}>
          <img className={style.logo} src={logo} alt="logo"/>
        </Link>
        <ul className={style.menuFunction}>
          <NavItem name="Trang chủ" />
          <NavItem name="Theo dõi" />
          <NavItem className = {style.category} name="Thể loại" />
          <NavItem name="Lịch sử" />
        </ul>
        <ul className={style.iconsFunction}>
          <li className={style.searchBox}>
            <input type="text" placeholder="Tìm kiếm..." className={style.searchInput} />
            <i className={`fi fi-rs-search ${style.searchIcon}`}></i>
          </li>
          <li>
            <Link to="/Help"><i className="fi fi-rr-interrogation"></i></Link>
          </li>
          <li>
            <Link to="/Socal"><i className="fi fi-rs-globe"></i></Link>
          </li>
          <li className={style.dropdown}>
            <Link to="/Account"><i className="fi fi-rr-user"></i></Link>
            <ul className={style.dropdownMenu}>
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
    </>
  );
}
