/**
 * Authentication service
 */
import { post, setToken, removeToken } from './api';
import config from '../config';

/**
 * Login user
 * @param {Object} credentials - User credentials
 * @param {string} credentials.username - Username
 * @param {string} credentials.password - Password
 * @returns {Promise<Object>} Promise that resolves to the user data
 */
export const login = async (credentials) => {
  try {
    const data = await post(config.endpoints.auth.login, credentials, false);
    if (data && data.token) {
      setToken(data.token);
      return data;
    }
    throw new Error('Invalid response from server');
  } catch (error) {
    throw error;
  }
};

/**
 * Logout user
 */
export const logout = () => {
  removeToken();
};

/**
 * Check if user is authenticated
 * @returns {boolean} True if user is authenticated, false otherwise
 */
export const isAuthenticated = () => {
  return !!localStorage.getItem('token');
};