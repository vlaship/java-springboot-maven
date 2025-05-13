import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Layout } from 'antd';
import 'antd/dist/reset.css';
import './App.css';

// Layout components
import Header from './components/layout/Header';

// Page components
import Home from './components/home/Home';
import Authors from './components/authors/Authors';
import Books from './components/books/Books';
import Stores from './components/stores/Stores';
import Login from './components/auth/Login';

// Services
import { logout, isAuthenticated } from './services/authService';

const { Content } = Layout;

/**
 * Main App component
 * @returns {JSX.Element} App component
 */
const App = () => {
  const [user, setUser] = useState(isAuthenticated() ? localStorage.getItem('username') : null);

  /**
   * Handle user login
   * @param {string} username - Username of the logged in user
   */
  const handleLogin = (username) => {
    setUser(username);
    localStorage.setItem('username', username);
  };

  /**
   * Handle user logout
   */
  const handleLogout = () => {
    logout();
    setUser(null);
    localStorage.removeItem('username');
  };

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Header user={user} onLogout={handleLogout} />
      <Content className="main-content">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route 
            path="/authors" 
            element={user ? <Authors /> : <Navigate to="/login" />} 
          />
          <Route 
            path="/books" 
            element={user ? <Books /> : <Navigate to="/login" />} 
          />
          <Route 
            path="/stores" 
            element={user ? <Stores /> : <Navigate to="/login" />} 
          />
          <Route 
            path="/login" 
            element={user ? <Navigate to="/" /> : <Login onLogin={handleLogin} />} 
          />
          <Route path="*" element={<Navigate to="/" />} />
        </Routes>
      </Content>
    </Layout>
  );
};

/**
 * App with Router wrapper
 * @returns {JSX.Element} App with Router
 */
const AppWithRouter = () => {
  return (
    <Router>
      <App />
    </Router>
  );
};

export default AppWithRouter;