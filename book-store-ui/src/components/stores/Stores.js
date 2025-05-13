import React, { useState, useCallback, useEffect } from 'react';
import { Table, Button, Form, Input, Modal, Space, Typography, message } from 'antd';
import {
  EditOutlined,
  DeleteOutlined,
  PlusOutlined,
  SaveOutlined,
  CloseOutlined,
} from '@ant-design/icons';
import { getAllStores, createStore, updateStore, deleteStore } from '../../services/storeService';
import StoreDetails from './StoreDetails';
import LoadingSpinner from '../common/LoadingSpinner';
import ErrorMessage from '../common/ErrorMessage';
import config from '../../config';

const { Title } = Typography;

/**
 * Stores component - Manages the list of stores
 * @returns {JSX.Element} Stores component
 */
const Stores = () => {
  const [stores, setStores] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [modalVisible, setModalVisible] = useState(false);
  const [form] = Form.useForm();
  const [editId, setEditId] = useState(null);
  const [editName, setEditName] = useState('');
  const [detailsId, setDetailsId] = useState(null);

  // Fetch stores on component mount
  const fetchStores = useCallback(async () => {
    setLoading(true);
    setError('');
    
    try {
      const data = await getAllStores();
      setStores(data);
    } catch (err) {
      setError('Failed to load stores');
      console.error('Error fetching stores:', err);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchStores();
  }, [fetchStores]);

  // Add a new store
  const handleAdd = async (values) => {
    try {
      await createStore({ name: values.name });
      form.resetFields();
      setModalVisible(false);
      fetchStores();
      message.success('Store added');
    } catch (err) {
      message.error(typeof err === 'string' ? err : 'Failed to add store');
    }
  };

  // Delete a store
  const handleDelete = (id) => {
    Modal.confirm({
      title: 'Delete Store',
      content: 'Are you sure you want to delete this store?',
      okType: 'danger',
      onOk: async () => {
        try {
          await deleteStore(id);
          fetchStores();
          message.success('Store deleted');
        } catch (err) {
          message.error('Failed to delete store');
        }
      },
    });
  };

  // Set up edit mode
  const handleEdit = (id, name) => {
    setEditId(id);
    setEditName(name);
  };

  // Update a store
  const handleUpdate = async (id) => {
    try {
      await updateStore(id, { name: editName });
      setEditId(null);
      setEditName('');
      fetchStores();
      message.success('Store updated');
    } catch (err) {
      message.error(typeof err === 'string' ? err : 'Failed to update store');
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
            minLength={config.validation.store.name.min}
            maxLength={config.validation.store.name.max}
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
                editName.length < config.validation.store.name.min || 
                editName.length > config.validation.store.name.max
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

  if (loading && stores.length === 0) {
    return <LoadingSpinner text="Loading stores..." />;
  }

  return (
    <div>
      <Title level={2}>Stores</Title>
      
      {error && <ErrorMessage message={error} />}
      
      <Button
        type="primary"
        icon={<PlusOutlined />}
        style={{ marginBottom: 16 }}
        onClick={() => setModalVisible(true)}
      >
        Add Store
      </Button>
      
      <Table
        dataSource={stores}
        columns={columns}
        rowKey="id"
        loading={loading}
        pagination={false}
        bordered
        style={{ background: "#fff", borderRadius: 8 }}
      />
      
      {/* Add Store Modal */}
      <Modal
        title="Add Store"
        open={modalVisible}
        onCancel={() => setModalVisible(false)}
        footer={null}
        destroyOnClose
      >
        <Form form={form} layout="vertical" onFinish={handleAdd}>
          <Form.Item
            name="name"
            label="Store Name"
            rules={[
              { required: true, message: "Please enter store name" },
              { 
                min: config.validation.store.name.min, 
                max: config.validation.store.name.max, 
                message: `Name must be ${config.validation.store.name.min}-${config.validation.store.name.max} characters` 
              },
            ]}
          >
            <Input autoFocus placeholder="Store name" />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" block>
              Add
            </Button>
          </Form.Item>
        </Form>
      </Modal>
      
      {/* Store Details Modal */}
      {detailsId && (
        <StoreDetails storeId={detailsId} onClose={() => setDetailsId(null)} />
      )}
    </div>
  );
};

export default Stores;