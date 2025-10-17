import React, { useState, useEffect, useRef } from "react";
import styles from "./User.module.css";

const User = (props) => (
  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24"
    fill="none" stroke="currentColor" strokeWidth="2"
    strokeLinecap="round" strokeLinejoin="round" {...props}>
    <path d="M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2" />
    <circle cx="12" cy="7" r="4" />
  </svg>
);

const HelpCircle = (props) => (
  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24"
    fill="none" stroke="currentColor" strokeWidth="2"
    strokeLinecap="round" strokeLinejoin="round" {...props}>
    <circle cx="12" cy="12" r="10" />
    <path d="M9.09 9a3 3 0 0 1 5.83 1c0 2-3 3-3 3" />
    <line x1="12" x2="12.01" y1="17" y2="17" />
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
  return (
    <div className={styles.wrapper}>
      <DropdownMenu
        trigger={
          <button className={styles.userButton}>
            <div className={styles.avatarSmall}>JD</div>
            <div className={styles.userInfo}>
              <div className={styles.userName}>John Doe</div>
              <div className={styles.userEmail}>john@example.com</div>
            </div>
          </button>
        }
      >
        <div className={styles.profileHeader}>
          <div className={styles.profileRow}>
            <div className={styles.avatarLarge}>JD</div>
            <div>
              <div className={styles.userName}>John Doe</div>
              <div className={styles.userEmail}>john@example.com</div>
              <div className={styles.userPlan}>Pro Plan</div>
            </div>
          </div>
        </div>

        <div>
          <DropdownMenuItem>
            <User className={styles.icon} /> Thông tin cá nhân
          </DropdownMenuItem>
        </div>
        <DropdownMenuSeparator />
        <div>
          <DropdownMenuItem>
            <HelpCircle className={styles.icon} /> Trợ giúp & Hỗ trợ
          </DropdownMenuItem>
          <DropdownMenuItem>
            <LogOut className={styles.icon} /> Đăng xuất
          </DropdownMenuItem>
        </div>
      </DropdownMenu>
    </div>
  );
}
