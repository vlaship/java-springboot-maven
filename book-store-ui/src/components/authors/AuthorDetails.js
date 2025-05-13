import React, { useState, useEffect } from 'react';
import { Modal } from 'antd';
import { getAuthorById } from '../../services/authorService';
import LoadingSpinner from '../common/LoadingSpinner';
import ErrorMessage from '../common/ErrorMessage';

/**
 * AuthorDetails component - Displays details of an author in a modal
 * @param {Object} props - Component props
 * @param {string|number} props.authorId - ID of the author to display
 * @param {Function} props.onClose - Function to call when the modal is closed
 * @returns {JSX.Element} AuthorDetails component
 */
const AuthorDetails = ({ authorId, onClose }) => {
  const [author, setAuthor] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchAuthorDetails = async () => {
      setLoading(true);
      setError('');
      
      try {
        const data = await getAuthorById(authorId);
        setAuthor(data);
      } catch (err) {
        setError('Failed to load author details');
        console.error('Error fetching author details:', err);
      } finally {
        setLoading(false);
      }
    };

    if (authorId) {
      fetchAuthorDetails();
    }
  }, [authorId]);

  return (
    <Modal
      open={!!authorId}
      onCancel={onClose}
      footer={null}
      title="Author Details"
      destroyOnClose
    >
      {loading ? (
        <LoadingSpinner text="Loading author details..." />
      ) : error ? (
        <ErrorMessage message={error} />
      ) : author ? (
        <div>
          <p><b>ID:</b> {author.id}</p>
          <p><b>Name:</b> {author.name}</p>
          {/* Add more author fields here if available */}
        </div>
      ) : null}
    </Modal>
  );
};

export default AuthorDetails;