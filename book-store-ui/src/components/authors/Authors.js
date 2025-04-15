import React, { useState, useCallback, useEffect } from 'react';
import { Table, Button, Form, Input, Modal, Space, Typography, message } from 'antd';
import {
  EditOutlined,
  DeleteOutlined,
  PlusOutlined,
  SaveOutlined,
  CloseOutlined,
} from '@ant-design/icons';
import { getAllAuthors, createAuthor, updateAuthor, deleteAuthor } from '../../services/authorService';
import AuthorDetails from './AuthorDetails';
import LoadingSpinner from '../common/LoadingSpinner';
import ErrorMessage from '../common/ErrorMessage';
import config from '../../config';

const { Title } = Typography;

/**
 * Authors component - Manages the list of authors
 * @returns {JSX.Element} Authors component
 */
const Authors = () => {
  const [authors, setAuthors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [modalVisible, setModalVisible] = useState(false);
  const [form] = Form.useForm();
  const [editId, setEditId] = useState(null);
  const [editName, setEditName] = useState('');
  const [detailsId, setDetailsId] = useState(null);

  // Fetch authors on component mount
  const fetchAuthors = useCallback(async () => {
    setLoading(true);
    setError('');
    
    try {
      const data = await getAllAuthors();
      setAuthors(data);
    } catch (err) {
      setError('Failed to load authors');
      console.error('Error fetching authors:', err);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchAuthors();
  }, [fetchAuthors]);

  // Add a new author
  const handleAdd = async (values) => {
    try {
      await createAuthor({ name: values.name });
      form.resetFields();
      setModalVisible(false);
      fetchAuthors();
      message.success('Author added');
    } catch (err) {
      message.error(typeof err === 'string' ? err : 'Failed to add author');
    }
  };

  // Delete an author
  const handleDelete = (id) => {
    Modal.confirm({
      title: 'Delete Author',
      content: 'Are you sure you want to delete this author?',
      okType: 'danger',
      onOk: async () => {
        try {
          await deleteAuthor(id);
          fetchAuthors();
          message.success('Author deleted');
        } catch (err) {
          message.error('Failed to delete author');
        }
      },
    });
  };

  // Set up edit mode
  const handleEdit = (id, name) => {
    setEditId(id);
    setEditName(name);
  };

  // Update an author
  const handleUpdate = async (id) => {
    try {
      await updateAuthor(id, { name: editName });
      setEditId(null);
      setEditName('');
      fetchAuthors();
      message.success('Author updated');
    } catch (err) {
      message.error(typeof err === 'string' ? err : 'Failed to update author');
    }
  };

  // Table columns configuration
  const columns = [
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
      render: (text, record) =>
        editId === record.id ? (
          <Input
            value={editName}
            minLength={config.validation.author.name.min}
            maxLength={config.validation.author.name.max}
            onChange={(e) => setEditName(e.target.value)}
            style={{ width: 180 }}
            autoFocus
          />
        ) : (
          <Button type="link" onClick={() => setDetailsId(record.id)}>{text}</Button>
        ),
    },
    {
      title: 'Actions',
      key: 'actions',
      width: 180,
      render: (_, record) =>
        editId === record.id ? (
          <Space>
            <Button
              icon={<SaveOutlined />}
              type="primary"
              onClick={() => handleUpdate(record.id)}
              disabled={
                !editName || 
                editName.length < config.validation.author.name.min || 
                editName.length > config.validation.author.name.max
              }
            >
              Save
            </Button>
            <Button icon={<CloseOutlined />} onClick={() => setEditId(null)}>
              Cancel
            </Button>
          </Space>
        ) : (
          <Space>
            <Button
              icon={<EditOutlined />}
              onClick={() => handleEdit(record.id, record.name)}
            >
              Edit
            </Button>
            <Button
              icon={<DeleteOutlined />}
              danger
              onClick={() => handleDelete(record.id)}
            >
              Delete
            </Button>
          </Space>
        ),
    },
  ];

  if (loading && authors.length === 0) {
    return <LoadingSpinner text="Loading authors..." />;
  }

  return (
    <div>
      <Title level={2}>Authors</Title>
      
      {error && <ErrorMessage message={error} />}
      
      <Button
        type="primary"
        icon={<PlusOutlined />}
        style={{ marginBottom: 16 }}
        onClick={() => setModalVisible(true)}
      >
        Add Author
      </Button>
      
      <Table
        dataSource={authors}
        columns={columns}
        rowKey="id"
        loading={loading}
        pagination={false}
        bordered
        style={{ background: "#fff", borderRadius: 8 }}
      />
      
      {/* Add Author Modal */}
      <Modal
        title="Add Author"
        open={modalVisible}
        onCancel={() => setModalVisible(false)}
        footer={null}
        destroyOnClose
      >
        <Form form={form} layout="vertical" onFinish={handleAdd}>
          <Form.Item
            name="name"
            label="Author Name"
            rules={[
              { required: true, message: "Please enter author name" },
              { 
                min: config.validation.author.name.min, 
                max: config.validation.author.name.max, 
                message: `Name must be ${config.validation.author.name.min}-${config.validation.author.name.max} characters` 
              },
            ]}
          >
            <Input autoFocus placeholder="Author name" />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" block>
              Add
            </Button>
          </Form.Item>
        </Form>
      </Modal>
      
      {/* Author Details Modal */}
      {detailsId && (
        <AuthorDetails authorId={detailsId} onClose={() => setDetailsId(null)} />
      )}
    </div>
  );
};

export default Authors;