import React from 'react';

/**
 * Error message component
 * @param {Object} props - Component props
 * @param {string} props.message - Error message to display
 * @param {string} [props.type='error'] - Type of error (error, warning, info)
 * @returns {JSX.Element|null} Error message component or null if no message
 */
const ErrorMessage = ({ message, type = 'error' }) => {
  if (!message) return null;

  const getStyles = () => {
    const baseStyle = {
      padding: '10px 16px',
      borderRadius: '6px',
      marginBottom: '16px',
      fontSize: '14px',
      fontWeight: '500',
      display: 'flex',
      alignItems: 'center',
    };

    switch (type) {
      case 'warning':
        return {
          ...baseStyle,
          backgroundColor: '#fff3e0',
          color: '#e65100',
          border: '1px solid #ffcc80',
        };
      case 'info':
        return {
          ...baseStyle,
          backgroundColor: '#e3f2fd',
          color: '#0d47a1',
          border: '1px solid #90caf9',
        };
      case 'error':
      default:
        return {
          ...baseStyle,
          backgroundColor: '#ffeaea',
          color: '#e63946',
          border: '1px solid #ffb3b3',
        };
    }
  };

  return (
    <div style={getStyles()}>
      {message}
    </div>
  );
};

export default ErrorMessage;