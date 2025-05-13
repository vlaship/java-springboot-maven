import React from 'react';
import { Typography } from 'antd';

const { Title } = Typography;

/**
 * Home component - Landing page of the application
 * @returns {JSX.Element} Home component
 */
const Home = () => {
  return (
    <div>
      <Title level={2}>Welcome to Book Store Facade UI</Title>
      <p>
        This application allows you to manage books, authors, and stores.
        Use the navigation menu above to access different sections of the application.
      </p>
      <div style={{ marginTop: '2rem' }}>
        <Title level={4}>Features:</Title>
        <ul style={{ fontSize: '16px', lineHeight: '1.6' }}>
          <li>Manage authors - Add, edit, and delete authors</li>
          <li>Manage books - Add, edit, and delete books, assign them to authors and stores</li>
          <li>Manage stores - Add, edit, and delete stores</li>
        </ul>
      </div>
    </div>
  );
};

export default Home;