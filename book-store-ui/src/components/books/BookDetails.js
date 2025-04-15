import React, { useState, useEffect } from 'react';
import { Modal } from 'antd';
import { getBookById } from '../../services/bookService';
import { getAuthorById } from '../../services/authorService';
import { getStoreById } from '../../services/storeService';
import LoadingSpinner from '../common/LoadingSpinner';
import ErrorMessage from '../common/ErrorMessage';

/**
 * BookDetails component - Displays details of a book in a modal
 * @param {Object} props - Component props
 * @param {string|number} props.bookId - ID of the book to display
 * @param {Function} props.onClose - Function to call when the modal is closed
 * @returns {JSX.Element} BookDetails component
 */
const BookDetails = ({ bookId, onClose }) => {
  const [book, setBook] = useState(null);
  const [author, setAuthor] = useState(null);
  const [stores, setStores] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchBookDetails = async () => {
      setLoading(true);
      setError('');
      
      try {
        // Fetch book data
        const bookData = await getBookById(bookId);
        setBook(bookData);
        
        // Fetch author data if available
        if (bookData.authorId) {
          try {
            const authorData = await getAuthorById(bookData.authorId);
            setAuthor(authorData);
          } catch (err) {
            console.error('Error fetching author details:', err);
          }
        }
        
        // Fetch store data if available
        if (Array.isArray(bookData.storeIds) && bookData.storeIds.length > 0) {
          const storePromises = bookData.storeIds.map(storeId => getStoreById(storeId));
          const storeResults = await Promise.allSettled(storePromises);
          
          const validStores = storeResults
            .filter(result => result.status === 'fulfilled')
            .map(result => result.value);
            
          setStores(validStores);
        }
      } catch (err) {
        setError('Failed to load book details');
        console.error('Error fetching book details:', err);
      } finally {
        setLoading(false);
      }
    };

    if (bookId) {
      fetchBookDetails();
    }
  }, [bookId]);

  return (
    <Modal
      open={!!bookId}
      onCancel={onClose}
      footer={null}
      title="Book Details"
      destroyOnClose
    >
      {loading ? (
        <LoadingSpinner text="Loading book details..." />
      ) : error ? (
        <ErrorMessage message={error} />
      ) : book ? (
        <div>
          <p><b>ID:</b> {book.id}</p>
          <p><b>Title:</b> {book.title}</p>
          <p><b>ISBN:</b> {book.isbn}</p>
          
          {author && (
            <p><b>Author:</b> {author.name}</p>
          )}
          
          {stores.length > 0 && (
            <div>
              <p><b>Available at:</b></p>
              <ul>
                {stores.map(store => (
                  <li key={store.id}>{store.name}{store.address ? ` (${store.address})` : ''}</li>
                ))}
              </ul>
            </div>
          )}
        </div>
      ) : null}
    </Modal>
  );
};

export default BookDetails;