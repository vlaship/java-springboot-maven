import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { Layout, Menu, Button, Space } from 'antd';
import {
  UserOutlined,
  BookOutlined,
  ShopOutlined,
  HomeOutlined,
  LogoutOutlined,
  LoginOutlined,
} from '@ant-design/icons';

const { Header: AntHeader } = Layout;

/**
 * Header component
 * @param {Object} props - Component props
 * @param {string|null} props.user - Logged in username or null if not logged in
 * @param {Function} props.onLogout - Logout handler function
 * @returns {JSX.Element} Header component
 */
const Header = ({ user, onLogout }) => {
  const location = useLocation();
  
  // Navigation items
  const navItems = [
    { label: <Link to="/">Home</Link>, key: "home", icon: <HomeOutlined /> },
    { label: <Link to="/authors">Authors</Link>, key: "authors", icon: <UserOutlined /> },
    { label: <Link to="/books">Books</Link>, key: "books", icon: <BookOutlined /> },
    { label: <Link to="/stores">Stores</Link>, key: "stores", icon: <ShopOutlined /> },
  ];

  return (
    <AntHeader style={{ background: "#fff", boxShadow: "0 2px 8px #0001", padding: 0 }}>
      <div style={{ 
        display: "flex", 
        alignItems: "center", 
        justifyContent: "space-between", 
        maxWidth: 1200, 
        margin: "0 auto" 
      }}>
        <Menu
          mode="horizontal"
          selectedKeys={[location.pathname.split("/")[1] || "home"]}
          items={navItems}
          style={{ 
            flex: 1, 
            fontWeight: 600, 
            fontSize: 16, 
            borderBottom: "none", 
            background: "none" 
          }}
        />
        
        {user ? (
          <Space style={{ marginRight: 24 }}>
            <span>Welcome, <b>{user}</b></span>
            <Button icon={<LogoutOutlined />} onClick={onLogout}>
              Logout
            </Button>
          </Space>
        ) : (
          <Button icon={<LoginOutlined />} type="primary" style={{ marginRight: 24 }}>
            <Link to="/login">Login</Link>
          </Button>
        )}
      </div>
    </AntHeader>
  );
};

export default Header;