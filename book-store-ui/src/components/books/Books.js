import React, { useState, useCallback, useEffect } from 'react';
import { Button, Form, Input, Modal, Select, Typography, message } from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import { getBooksByAuthorId, createBook, updateBook, deleteBook } from '../../services/bookService';
import { getAllAuthors } from '../../services/authorService';
import { getAllStores } from '../../services/storeService';
import BookDetails from './BookDetails';
import StoreDetails from '../stores/StoreDetails';
import LoadingSpinner from '../common/LoadingSpinner';
import ErrorMessage from '../common/ErrorMessage';

const { Title } = Typography;

/**
 * Books component - Manages the list of books
 * @returns {JSX.Element} Books component
 */
const Books = () => {
  // State for data
  const [authors, setAuthors] = useState([]);
  const [selectedAuthor, setSelectedAuthor] = useState('');
  const [books, setBooks] = useState([]);
  const [stores, setStores] = useState([]);
  
  // UI state
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [storeDetailsId, setStoreDetailsId] = useState(null);
  const [bookDetailsId, setBookDetailsId] = useState(null);
  const [bookModalVisible, setBookModalVisible] = useState(false);
  const [editingBook, setEditingBook] = useState(null);
  const [bookForm] = Form.useForm();

  // Fetch dropdown data (authors and stores) on component mount
  useEffect(() => {
    const fetchDropdowns = async () => {
      setLoading(true);
      setError('');
      
      try {
        // Fetch authors
        const authorsData = await getAllAuthors();
        setAuthors(authorsData);
        
        // Fetch stores
        const storesData = await getAllStores();
        setStores(storesData);
      } catch (err) {
        setError('Error loading dropdown data');
        console.error('Error fetching dropdown data:', err);
      } finally {
        setLoading(false);
      }
    };
    
    fetchDropdowns();
  }, []);

  // Fetch books when a filter is selected
  const fetchBooks = useCallback(async () => {
    if (!selectedAuthor) {
      setBooks([]);
      return;
    }
    
    setLoading(true);
    setError('');
    
    try {
      const booksData = await getBooksByAuthorId(selectedAuthor);
      
      // Enhance books with author and store names for display
      const enhancedBooks = booksData.map(book => {
        // Find author name
        const author = authors.find(a => a.id === book.authorId);
        
        // Find store names
        const bookStores = Array.isArray(book.storeIds) 
          ? book.storeIds.map(storeId => {
              const store = stores.find(s => s.id === storeId);
              return store ? { id: store.id, name: store.name } : null;
            }).filter(Boolean)
          : [];
          
        return {
          ...book,
          authorName: author ? author.name : '',
          stores: bookStores
        };
      });
      
      setBooks(enhancedBooks);
    } catch (err) {
      setError('Failed to load books');
      console.error('Error fetching books:', err);
      setBooks([]);
    } finally {
      setLoading(false);
    }
  }, [selectedAuthor, authors, stores]);

  useEffect(() => {
    fetchBooks();
  }, [fetchBooks]);

  // Handle book form submission (add/edit)
  const handleBookFormSubmit = async () => {
    try {
      const values = await bookForm.validateFields();
      
      if (editingBook) {
        // Update book
        await updateBook(editingBook.id, values);
        message.success('Book updated');
      } else {
        // Add book
        await createBook(values);
        message.success('Book added');
      }
      
      // Reset form and state
      setBookModalVisible(false);
      setEditingBook(null);
      bookForm.resetFields();
      
      // Refresh books list
      fetchBooks();
    } catch (err) {
      if (err.errorFields) {
        // Form validation error
        return;
      }
      message.error(typeof err === 'string' ? err : 'Failed to save book');
    }
  };

  // Delete a book
  const handleDeleteBook = (id) => {
    Modal.confirm({
      title: 'Delete Book',
      content: 'Are you sure you want to delete this book?',
      okType: 'danger',
      onOk: async () => {
        try {
          await deleteBook(id);
          fetchBooks();
          message.success('Book deleted');
        } catch (err) {
          message.error('Failed to delete book');
        }
      },
    });
  };

  // Dropdown styles
  const dropdownStyle = {
    minWidth: 220,
    padding: '8px',
    borderRadius: '6px',
    border: '1px solid #ccc',
    fontSize: '1rem',
    background: '#fafbfc',
    marginRight: '18px',
    marginBottom: '0',
    boxShadow: '0 1px 2px rgba(0,0,0,0.03)',
    outline: 'none',
    transition: 'border .2s',
  };

  // Set up book for editing
  const handleEditBook = (book) => {
    setEditingBook(book);
    setBookModalVisible(true);
    
    // Set form values
    setTimeout(() => {
      bookForm.setFieldsValue({
        title: book.title,
        isbn: book.isbn,
        authorId: book.authorId,
        storeIds: book.storeIds || []
      });
    }, 0);
  };

  if (loading && !books.length && !authors.length) {
    return <LoadingSpinner text="Loading data..." />;
  }

  return (
    <div>
      <Title level={2}>Books</Title>
      
      {error && <ErrorMessage message={error} />}
      
      <div style={{ display: 'flex', gap: '2rem', marginBottom: '1.5rem', alignItems: 'center' }}>
        <div>
          <label style={{marginRight: 8, fontWeight: 500}}>Author:</label>
          <select
            style={dropdownStyle}
            value={selectedAuthor}
            onChange={e => { setSelectedAuthor(e.target.value); }}
          >
            <option value="">Select Author</option>
            {authors.map(author => (
              <option key={author.id} value={author.id}>{author.name}</option>
            ))}
          </select>
        </div>
        <Button 
          type="primary" 
          icon={<PlusOutlined />}
          onClick={() => {
            setEditingBook(null);
            setBookModalVisible(true);
            bookForm.resetFields();
          }}
        >
          Add Book
        </Button>
      </div>
      
      {!selectedAuthor && <div style={{marginBottom: 16}}>Please select an author to view books.</div>}
      
      {selectedAuthor && !loading && books.length === 0 && <div>No books found for this author.</div>}
      
      {books.length > 0 && (
        <table style={{ borderCollapse: 'collapse', width: '100%', background: '#fff', borderRadius: 8, boxShadow: '0 2px 8px rgba(0,0,0,0.06)' }}>
          <thead style={{ background: '#f5f6fa' }}>
            <tr>
              <th style={{padding: '10px 14px', borderBottom: '2px solid #eee', textAlign: 'left'}}>Title</th>
              <th style={{padding: '10px 14px', borderBottom: '2px solid #eee', textAlign: 'left'}}>Author</th>
              <th style={{padding: '10px 14px', borderBottom: '2px solid #eee', textAlign: 'left'}}>Stores</th>
              <th style={{padding: '10px 14px', borderBottom: '2px solid #eee', textAlign: 'left'}}>ISBN</th>
              <th style={{padding: '10px 14px', borderBottom: '2px solid #eee', textAlign: 'left'}}>Actions</th>
            </tr>
          </thead>
          <tbody>
            {books.map(book => (
              <tr key={book.id}>
                <td style={{padding: '10px 14px', borderBottom: '1px solid #f0f0f0'}}>
                  <Button type="link" style={{padding: 0}} onClick={() => setBookDetailsId(book.id)}>
                    {book.title}
                  </Button>
                </td>
                <td style={{padding: '10px 14px', borderBottom: '1px solid #f0f0f0'}}>{book.authorName}</td>
                <td style={{padding: '10px 14px', borderBottom: '1px solid #f0f0f0'}}>
                  {book.stores && book.stores.length > 0 ? (
                    book.stores.map((store, idx) => (
                      <span key={idx}>
                        <Button 
                          type="link" 
                          style={{padding: 0, marginRight: 8}} 
                          onClick={() => setStoreDetailsId(store.id)}
                        >
                          {store.name}
                        </Button>
                        {idx < book.stores.length - 1 && ', '}
                      </span>
                    ))
                  ) : (
                    <span>No stores</span>
                  )}
                </td>
                <td style={{padding: '10px 14px', borderBottom: '1px solid #f0f0f0'}}>{book.isbn}</td>
                <td style={{padding: '10px 14px', borderBottom: '1px solid #f0f0f0'}}>
                  <Button type="link" onClick={() => handleEditBook(book)}>Edit</Button>
                  <Button type="link" danger onClick={() => handleDeleteBook(book.id)}>Delete</Button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
      
      {/* Book Add/Edit Modal */}
      <Modal
        open={bookModalVisible}
        onCancel={() => {
          setBookModalVisible(false);
          setEditingBook(null);
          bookForm.resetFields();
        }}
        onOk={handleBookFormSubmit}
        title={editingBook ? 'Edit Book' : 'Add Book'}
        okText={editingBook ? 'Update' : 'Add'}
        destroyOnClose
      >
        <Form form={bookForm} layout="vertical">
          <Form.Item 
            name="title" 
            label="Title" 
            rules={[{ required: true, message: 'Please enter the title' }]}
          > 
            <Input />
          </Form.Item>
          <Form.Item 
            name="isbn" 
            label="ISBN" 
            rules={[{ required: true, message: 'Please enter the ISBN' }]}
          > 
            <Input />
          </Form.Item>
          <Form.Item 
            name="authorId" 
            label="Author" 
            rules={[{ required: true, message: 'Please select an author' }]}
          > 
            <Select style={{ width: '100%' }} placeholder="Select Author" allowClear>
              {authors.map(author => (
                <Select.Option key={author.id} value={author.id}>{author.name}</Select.Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item name="storeIds" label="Stores">
            <Select
              mode="multiple"
              style={{ width: '100%' }}
              allowClear
              placeholder="Select Stores"
              optionLabelProp="children"
            >
              {stores.map(store => (
                <Select.Option key={store.id} value={store.id}>{store.name}</Select.Option>
              ))}
            </Select>
          </Form.Item>
        </Form>
      </Modal>
      
      {/* Book Details Modal */}
      {bookDetailsId && (
        <BookDetails bookId={bookDetailsId} onClose={() => setBookDetailsId(null)} />
      )}
      
      {/* Store Details Modal */}
      {storeDetailsId && (
        <StoreDetails storeId={storeDetailsId} onClose={() => setStoreDetailsId(null)} />
      )}
    </div>
  );
};

export default Books;