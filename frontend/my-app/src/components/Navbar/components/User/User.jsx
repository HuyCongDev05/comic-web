import { useState, useEffect, useRef } from "react";
import styles from "./User.module.css";
import { useAuth } from "../../../../context/AuthContext";
import { PageLocation } from '../../../../hooks/PageLocation';
import AccountApi from "../../../../api/Account";

const User = (props) => (
  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24"
    fill="none" stroke="currentColor" strokeWidth="2"
    strokeLinecap="round" strokeLinejoin="round" {...props}>
    <path d="M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2" />
    <circle cx="12" cy="7" r="4" />
  </svg>
);

const LogOut = (props) => (
  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24"
    fill="none" stroke="currentColor" strokeWidth="2"
    strokeLinecap="round" strokeLinejoin="round" {...props}>
    <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4" />
    <polyline points="16 17 21 12 16 7" />
    <line x1="21" x2="9" y1="12" y2="12" />
  </svg>
);

const LoginIcon = (props) => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    width="16"
    height="16"
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    strokeWidth="2"
    strokeLinecap="round"
    strokeLinejoin="round"
    {...props}
  >
    <path d="M15 3h4a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2h-4" />
    <polyline points="10 17 15 12 10 7" />
    <line x1="15" y1="12" x2="3" y2="12" />
  </svg>
);

const RegisterIcon = (props) => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    width="16"
    height="16"
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    strokeWidth="2"
    strokeLinecap="round"
    strokeLinejoin="round"
    {...props}
  >
    <circle cx="12" cy="7" r="4" />
    <path d="M5.5 21a7 7 0 0 1 13 0" />
    <line x1="19" y1="8" x2="19" y2="14" />
    <line x1="22" y1="11" x2="16" y2="11" />
  </svg>
);

const DropdownMenu = ({ children, trigger }) => {
  const [isOpen, setIsOpen] = useState(false);
  const dropdownRef = useRef(null);

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setIsOpen(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  const handleTriggerClick = (e) => {
    e.stopPropagation();
    setIsOpen(!isOpen);
  };

  return (
    <div className={styles.dropdownContainer} ref={dropdownRef}>
      <div onClick={handleTriggerClick} className={styles.trigger}>
        {trigger}
      </div>
      {isOpen && (
        <div className={styles.dropdownMenu} role="menu" aria-orientation="vertical">
          {children}
        </div>
      )}
    </div>
  );
};

const DropdownMenuItem = ({ children, onClick }) => (
  <a
    href="#"
    onClick={(e) => {
      e.preventDefault();
      if (onClick) onClick();
    }}
    className={styles.menuItem}
    role="menuitem"
  >
    {children}
  </a>
);

const DropdownMenuSeparator = () => <div className={styles.separator} />;

export default function UserProfileDropdown() {

  const { user, logout } = useAuth();
  const { handleLoginClick, handleRegisterClick } = PageLocation();
  const handLogout = async () => {
    try {
      const fetchLogout = await AccountApi.logout();
      if (fetchLogout) {
        logout();
      }
    } catch {
    }
  };


  return (
    <div className={styles.wrapper}>
      <DropdownMenu
        trigger={
          <button className={styles.userButton}>
            <div className={styles.avatarSmall}>
              <img
                src={user?.avatar || "https://i.pinimg.com/736x/7d/b9/56/7db956d51da0e02f621e011879fcef37.jpg"}
                alt="avatar"
              />
            </div>
            <div className={styles.userInfo}>
              {!user ? (
                <>
                  <div className={styles.userName}>Đăng nhập</div>
                  <div className={styles.userEmail}>user@example.com</div>
                </>
              ) : (
                <>
                  <div className={styles.userName}>{user.firstName + " " + user.lastName}</div>
                  <div className={styles.userEmail}>{user.email}</div>
                </>
              )}

            </div>
          </button>
        }
      >
        <div className={styles.profileHeader}>
          <div className={styles.profileRow}>
            <div className={styles.avatarSmall}>
                <img
                  src={user?.avatar || "https://i.pinimg.com/736x/7d/b9/56/7db956d51da0e02f621e011879fcef37.jpg"}
                  alt="avatar"
                />
            </div>
            <div>
              {!user ? (
                <>
                  <div className={styles.userName}>Đăng nhập</div>
                  <div className={styles.userEmail}>user@example.com</div>
                  <div className={styles.userPlan}>Trạng thái tài khoản</div>
                </>
              ) : (
                <>
                  <div className={styles.userName}>{user.firstName + " " + user.lastName}</div>
                  <div className={styles.userEmail}>{user.email}</div>
                  <div
                    className={styles.userPlan}
                    style={{
                      color: user.status === 'NORMAL' ? 'green' : user.status === 'WARNING' ? 'yellow' : 'red'
                    }}
                  >
                    {user.status === 'NORMAL' ? 'Bình thường' : user.status === 'WARNING' ? 'Cảnh báo' : 'Không xác định'}
                  </div>
                </>
              )}
            </div>
          </div>
        </div>
        {!user ? (
          <>
            <div>
              <DropdownMenuItem onClick={handleLoginClick}>
                <LoginIcon className={styles.icon} /> Đăng nhập
              </DropdownMenuItem>
              <DropdownMenuItem onClick={handleRegisterClick}>
                <RegisterIcon className={styles.icon} /> Đăng ký
              </DropdownMenuItem>
            </div>
          </>
        ) : (
          <>
            <div>
              <DropdownMenuItem>
                <User className={styles.icon} /> Thông tin cá nhân
              </DropdownMenuItem>
            </div>
            <DropdownMenuSeparator />
            <div>
                <DropdownMenuItem onClick={handLogout} >
                  <LogOut className={styles.icon}/> Đăng xuất
              </DropdownMenuItem>
            </div>
          </>
        )}
      </DropdownMenu>
    </div>
  );
}
