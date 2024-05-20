import React from 'react';
import BackgroundImg from '../commons/images/texture.jpg';

const backgroundStyle = {
  backgroundPosition: 'center',
  backgroundSize: 'cover',
  backgroundRepeat: 'no-repeat',
  width: '100%',
  height: '100vh',
  backgroundImage: `url(${BackgroundImg})`,
  margin: '0',
  padding: '0',
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'flex-start',
  alignItems: 'center',
};

const labelStyle = {
  fontSize: '48px',  
  color: 'white',
  textAlign: 'center',
  margin: '40px 0',
};

const buttonContainerStyle = {
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
};

const buttonStyle = {
  backgroundColor: '#00008B',  
  color: 'white',
  fontSize: '24px',  
  padding: '10px 20px',  
  borderRadius: '20px',  
  margin: '10px',  
  cursor: 'pointer',
};

const AdminPage = () => {
  const handleUserAdmin = () => {
    window.location.href = '/admin/users';
  };

  const handleDeviceAdmin = () => {
    window.location.href = '/admin/devices';
  };
  const handleOpenChat = () => {
    window.location.href = '/admin/chat';
  };


  return (
    <div style={backgroundStyle}>
      <label style={labelStyle}>Hello admin, what do you want to do today?</label>
      <div style={buttonContainerStyle}>
        <button style={buttonStyle} onClick={handleUserAdmin}>
          Administrate users
        </button>
        <button style={buttonStyle} onClick={handleDeviceAdmin}>
          Administrate devices
        </button>
        <button style={buttonStyle} onClick={handleOpenChat}>
          Chat with clients
        </button>
      </div>
    </div>
  );
};

export default AdminPage;