import React from 'react';

/**
 * Loading spinner component
 * @param {Object} props - Component props
 * @param {string} [props.size='default'] - Size of the spinner (small, default, large)
 * @param {string} [props.text='Loading...'] - Text to display
 * @returns {JSX.Element} Loading spinner component
 */
const LoadingSpinner = ({ size = 'default', text = 'Loading...' }) => {
  const getSize = () => {
    switch (size) {
      case 'small':
        return { width: '16px', height: '16px' };
      case 'large':
        return { width: '32px', height: '32px' };
      default:
        return { width: '24px', height: '24px' };
    }
  };

  const spinnerStyle = {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    flexDirection: 'column',
    padding: '16px',
  };

  const spinnerSizeStyle = getSize();

  return (
    <div style={spinnerStyle}>
      <div
        style={{
          border: '3px solid #f3f3f3',
          borderTop: '3px solid #667eea',
          borderRadius: '50%',
          animation: 'spin 1s linear infinite',
          marginBottom: '8px',
          ...spinnerSizeStyle,
        }}
      />
      {text && <div>{text}</div>}
      <style jsx="true">{`
        @keyframes spin {
          0% { transform: rotate(0deg); }
          100% { transform: rotate(360deg); }
        }
      `}</style>
    </div>
  );
};

export default LoadingSpinner;