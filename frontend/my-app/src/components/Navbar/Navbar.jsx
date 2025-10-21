import style from "./Navbar.module.css";
import logo from "../../assets/icons/logo.png";
import NavItem from "./components/Menu/Menu";
import { Link } from "react-router-dom";
import CategoryApi from "../../api/Category";
import { useEffect, useState } from "react";

export default function Navbar() {

  const [categories, setCategories] = useState([]);
  
  useEffect(() => {
    const effectCategories = async () => {
      try {
        const dataCategories = await CategoryApi.getCategories(1);
        setCategories(dataCategories.data)
      } catch (error) {
        console.error("Failed to fetch categories:", error)
      }
    };
    effectCategories();
  },[]);

  return (
    <>
      <div className={style.navbar}>
        <div className={style.left}>
          <Link to="/" className={style.logoLink}>
            <img className={style.logo} src={logo} alt="logo" />
          </Link>
        </div>
        <div className={style.center}>
          <ul className={style.menuFunction}>
            <NavItem name="Trang chủ" />
            <NavItem name="Theo dõi" />
            <NavItem name="Lịch sử" />
            <li className={style.category}>
              <p className={style.categories}>Thể loại</p>
              <div className={style.dropdownCategories}>
                <div className={style.list}>
                  {categories.map((item, i) => (
                    <a key={i} href="#" className={style.item}>
                      {item.name}
                    </a>
                  ))}
                </div>
              </div>
            </li>
          </ul>
        </div>

        <div className={style.right}>
          <ul className={style.iconsFunction}>
            <li className={style.searchBox}>
              <input type="text" placeholder="Tìm kiếm..." className={style.searchInput} />
              <i className={`fi fi-rs-search ${style.searchIcon}`}></i>
            </li>
            <li><Link to="/Help"><i className="fi fi-rr-interrogation"></i></Link></li>
            <li><Link to="/Socal"><i className="fi fi-rs-globe"></i></Link></li>
            <li className={style.dropdownAccount}>
              <Link><i className="fi fi-rr-user"></i></Link>
              <ul className={style.dropdownMenu}>
                <li><Link to="/Register">Đăng ký</Link></li>
                <li><Link to="/Login">Đăng nhập</Link></li>
              </ul>
            </li>
          </ul>
        </div>
      </div>
    </>
  );
}
