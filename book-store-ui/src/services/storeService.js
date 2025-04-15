/**
 * Store service
 */
import { get, post, patch, del } from './api';
import config from '../config';

/**
 * Get all stores
 * @returns {Promise<Array>} Promise that resolves to an array of stores
 */
export const getAllStores = async () => {
  return get(config.endpoints.store.base);
};

/**
 * Get store by ID
 * @param {string|number} id - Store ID
 * @returns {Promise<Object>} Promise that resolves to the store data
 */
export const getStoreById = async (id) => {
  return get(config.endpoints.store.byId(id));
};

/**
 * Create a new store
 * @param {Object} store - Store data
 * @param {string} store.name - Store name
 * @param {string} [store.address] - Store address
 * @returns {Promise<Object>} Promise that resolves to the created store
 */
export const createStore = async (store) => {
  return post(config.endpoints.store.base, store);
};

/**
 * Update a store
 * @param {string|number} id - Store ID
 * @param {Object} store - Store data to update
 * @param {string} [store.name] - Store name
 * @param {string} [store.address] - Store address
 * @returns {Promise<Object>} Promise that resolves to the updated store
 */
export const updateStore = async (id, store) => {
  return patch(config.endpoints.store.byId(id), store);
};

/**
 * Delete a store
 * @param {string|number} id - Store ID
 * @returns {Promise<void>} Promise that resolves when the store is deleted
 */
export const deleteStore = async (id) => {
  return del(config.endpoints.store.byId(id));
};