/**
 * API service for making HTTP requests
 */
import config from '../config';

/**
 * Get the authentication token from localStorage
 * @returns {string|null} The authentication token or null if not found
 */
export const getToken = () => localStorage.getItem('token');

/**
 * Set the authentication token in localStorage
 * @param {string} token - The authentication token
 */
export const setToken = (token) => localStorage.setItem('token', token);

/**
 * Remove the authentication token from localStorage
 */
export const removeToken = () => localStorage.removeItem('token');

/**
 * Create headers for API requests
 * @param {boolean} includeAuth - Whether to include the Authorization header
 * @returns {Object} Headers object
 */
export const createHeaders = (includeAuth = true) => {
  const headers = {
    'Content-Type': 'application/json',
  };

  if (includeAuth) {
    const token = getToken();
    if (token) {
      headers.Authorization = `Bearer ${token}`;
    }
  }

  return headers;
};

/**
 * Handle API response
 * @param {Response} response - Fetch API response
 * @returns {Promise} Promise that resolves to the response data or rejects with an error
 */
export const handleResponse = async (response) => {
  if (!response.ok) {
    // Try to parse error response
    try {
      const errorData = await response.json();
      return Promise.reject(errorData.details || errorData.message || 'API request failed');
    } catch (e) {
      return Promise.reject('API request failed');
    }
  }

  // For successful responses, parse JSON if content exists
  if (response.status !== 204) { // 204 No Content
    return response.json();
  }
  
  return null;
};

/**
 * Make a GET request
 * @param {string} endpoint - API endpoint
 * @param {boolean} includeAuth - Whether to include the Authorization header
 * @returns {Promise} Promise that resolves to the response data
 */
export const get = async (endpoint, includeAuth = true) => {
  const response = await fetch(`${config.apiBaseUrl}${endpoint}`, {
    method: 'GET',
    headers: createHeaders(includeAuth),
  });
  
  return handleResponse(response);
};

/**
 * Make a POST request
 * @param {string} endpoint - API endpoint
 * @param {Object} data - Request body data
 * @param {boolean} includeAuth - Whether to include the Authorization header
 * @returns {Promise} Promise that resolves to the response data
 */
export const post = async (endpoint, data, includeAuth = true) => {
  const response = await fetch(`${config.apiBaseUrl}${endpoint}`, {
    method: 'POST',
    headers: createHeaders(includeAuth),
    body: JSON.stringify(data),
  });
  
  return handleResponse(response);
};

/**
 * Make a PATCH request
 * @param {string} endpoint - API endpoint
 * @param {Object} data - Request body data
 * @returns {Promise} Promise that resolves to the response data
 */
export const patch = async (endpoint, data) => {
  const response = await fetch(`${config.apiBaseUrl}${endpoint}`, {
    method: 'PATCH',
    headers: createHeaders(),
    body: JSON.stringify(data),
  });
  
  return handleResponse(response);
};

/**
 * Make a DELETE request
 * @param {string} endpoint - API endpoint
 * @returns {Promise} Promise that resolves to the response data
 */
export const del = async (endpoint) => {
  const response = await fetch(`${config.apiBaseUrl}${endpoint}`, {
    method: 'DELETE',
    headers: createHeaders(),
  });
  
  return handleResponse(response);
};