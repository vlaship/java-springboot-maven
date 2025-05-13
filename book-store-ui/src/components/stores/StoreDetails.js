import React, { useState, useEffect } from 'react';
import { Modal } from 'antd';
import { getStoreById } from '../../services/storeService';
import LoadingSpinner from '../common/LoadingSpinner';
import ErrorMessage from '../common/ErrorMessage';

/**
 * StoreDetails component - Displays details of a store in a modal
 * @param {Object} props - Component props
 * @param {string|number} props.storeId - ID of the store to display
 * @param {Function} props.onClose - Function to call when the modal is closed
 * @returns {JSX.Element} StoreDetails component
 */
const StoreDetails = ({ storeId, onClose }) => {
  const [store, setStore] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchStoreDetails = async () => {
      setLoading(true);
      setError('');
      
      try {
        const data = await getStoreById(storeId);
        setStore(data);
      } catch (err) {
        setError('Failed to load store details');
        console.error('Error fetching store details:', err);
      } finally {
        setLoading(false);
      }
    };

    if (storeId) {
      fetchStoreDetails();
    }
  }, [storeId]);

  return (
    <Modal
      open={!!storeId}
      onCancel={onClose}
      footer={null}
      title="Store Details"
      destroyOnClose
    >
      {loading ? (
        <LoadingSpinner text="Loading store details..." />
      ) : error ? (
        <ErrorMessage message={error} />
      ) : store ? (
        <div>
          <p><b>ID:</b> {store.id}</p>
          <p><b>Name:</b> {store.name}</p>
          {store.address && <p><b>Address:</b> {store.address}</p>}
          {/* Add more store fields here if available */}
        </div>
      ) : null}
    </Modal>
  );
};

export default StoreDetails;