import React from 'react';
import logo from './commons/images/icon.png';
import { Navbar } from 'reactstrap';

const buttonStyle = {
  backgroundColor: 'transparent',
  border: 'none',
  color: 'white',
  cursor: 'pointer',
  fontSize: '20px',
  marginLeft: 'auto',
};

const NavigationBar = () => {
  const currentPath = window.location.pathname;

  const handleLogout = async () => {
    const token = sessionStorage.getItem('token');

    if (token) {
      const response = await fetch('http://localhost:8080/auth/logout', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });

      if (response.ok) {
        sessionStorage.clear();
        console.log('Logout successful');
        window.alert('Logout successful');
        window.location.href = '/';
      } else {
        console.error('Logout failed');
      }
    }
  };

  const insertNewUser = async () => {
    window.location.href = '/admin/newUser';
  };

  const insertNewDevice = async () => {
    window.location.href = '/admin/newDevice';
  };

  const navigateHome = () => {
    if (currentPath === '/admin/users' || currentPath === '/admin/devices' || currentPath === '/admin/newUser') {
      window.location.href = '/admin';
    } else if (currentPath.startsWith('/user')) {
      window.location.href = '/user';
    }
  };

  return (
    <div>
      <Navbar color="dark" light expand="md">
        <button onClick={navigateHome}>
          <img src={logo} width="50" height="35" alt="Logo" />
        </button>
        {currentPath === '/admin/users' ? (
          <button style={buttonStyle} onClick={insertNewUser}>
            Insert new user
          </button>
        ) : null}
        {currentPath === '/admin/devices' ? (
          <button style={buttonStyle} onClick={insertNewDevice}>
            Insert new device
          </button>
        ) : null}
        {currentPath === '/user' || currentPath.startsWith('/admin') ? (
          <button style={buttonStyle} onClick={handleLogout}>
            Logout
          </button>
        ) : null}
      </Navbar>
    </div>
  );
};

export default NavigationBar;
