/**
 * Book service
 */
import { get, post, patch, del } from './api';
import config from '../config';

/**
 * Get all books
 * @returns {Promise<Array>} Promise that resolves to an array of books
 */
export const getAllBooks = async () => {
  return get(config.endpoints.book.base);
};

/**
 * Get book by ID
 * @param {string|number} id - Book ID
 * @returns {Promise<Object>} Promise that resolves to the book data
 */
export const getBookById = async (id) => {
  return get(config.endpoints.book.byId(id));
};

/**
 * Get books by author ID
 * @param {string|number} authorId - Author ID
 * @returns {Promise<Array>} Promise that resolves to an array of books
 */
export const getBooksByAuthorId = async (authorId) => {
  return get(config.endpoints.book.byAuthor(authorId));
};

/**
 * Create a new book
 * @param {Object} book - Book data
 * @param {string} book.title - Book title
 * @param {string} book.isbn - Book ISBN
 * @param {string|number} book.authorId - Author ID
 * @param {Array<string|number>} [book.storeIds] - Array of store IDs
 * @returns {Promise<Object>} Promise that resolves to the created book
 */
export const createBook = async (book) => {
  return post(config.endpoints.book.base, book);
};

/**
 * Update a book
 * @param {string|number} id - Book ID
 * @param {Object} book - Book data to update
 * @param {string} [book.title] - Book title
 * @param {string} [book.isbn] - Book ISBN
 * @param {string|number} [book.authorId] - Author ID
 * @param {Array<string|number>} [book.storeIds] - Array of store IDs
 * @returns {Promise<Object>} Promise that resolves to the updated book
 */
export const updateBook = async (id, book) => {
  return patch(config.endpoints.book.byId(id), book);
};

/**
 * Delete a book
 * @param {string|number} id - Book ID
 * @returns {Promise<void>} Promise that resolves when the book is deleted
 */
export const deleteBook = async (id) => {
  return del(config.endpoints.book.byId(id));
};