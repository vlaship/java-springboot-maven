/**
 * Author service
 */
import { get, post, patch, del } from './api';
import config from '../config';

/**
 * Get all authors
 * @returns {Promise<Array>} Promise that resolves to an array of authors
 */
export const getAllAuthors = async () => {
  return get(config.endpoints.author.base);
};

/**
 * Get author by ID
 * @param {string|number} id - Author ID
 * @returns {Promise<Object>} Promise that resolves to the author data
 */
export const getAuthorById = async (id) => {
  return get(config.endpoints.author.byId(id));
};

/**
 * Create a new author
 * @param {Object} author - Author data
 * @param {string} author.name - Author name
 * @returns {Promise<Object>} Promise that resolves to the created author
 */
export const createAuthor = async (author) => {
  return post(config.endpoints.author.base, author);
};

/**
 * Update an author
 * @param {string|number} id - Author ID
 * @param {Object} author - Author data to update
 * @param {string} author.name - Author name
 * @returns {Promise<Object>} Promise that resolves to the updated author
 */
export const updateAuthor = async (id, author) => {
  return patch(config.endpoints.author.byId(id), author);
};

/**
 * Delete an author
 * @param {string|number} id - Author ID
 * @returns {Promise<void>} Promise that resolves when the author is deleted
 */
export const deleteAuthor = async (id) => {
  return del(config.endpoints.author.byId(id));
};