import React, { useState } from 'react';
import { Form, Input, Button, Typography } from 'antd';
import { LoginOutlined } from '@ant-design/icons';
import { login } from '../../services/authService';
import ErrorMessage from '../common/ErrorMessage';

const { Title } = Typography;

/**
 * Login component
 * @param {Object} props - Component props
 * @param {Function} props.onLogin - Function to call when login is successful
 * @returns {JSX.Element} Login component
 */
const Login = ({ onLogin }) => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [form] = Form.useForm();

  /**
   * Handle form submission
   * @param {Object} values - Form values
   */
  const handleSubmit = async (values) => {
    setLoading(true);
    setError('');
    
    try {
      const data = await login(values);
      onLogin(data.username);
    } catch (err) {
      setError(typeof err === 'string' ? err : 'Login failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <Form
        form={form}
        className="auth-form"
        layout="vertical"
        onFinish={handleSubmit}
      >
        <Title level={3}>Login</Title>
        
        <ErrorMessage message={error} />
        
        <Form.Item
          name="username"
          label="Username"
          rules={[{ required: true, message: 'Please enter your username' }]}
        >
          <Input autoFocus placeholder="Username" />
        </Form.Item>
        
        <Form.Item
          name="password"
          label="Password"
          rules={[{ required: true, message: 'Please enter your password' }]}
        >
          <Input.Password placeholder="Password" />
        </Form.Item>
        
        <Form.Item>
          <Button
            type="primary"
            htmlType="submit"
            loading={loading}
            block
            icon={<LoginOutlined />}
          >
            Login
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
};

export default Login;