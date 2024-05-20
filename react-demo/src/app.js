import React from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import NavigationBar from './navigation-bar';
import Home from './home/home';
import UserDevices from './client/user';
import UserList from './admin/user-list';
import DeviceList from './admin/device-list';
import AdminPage from './admin/admin';
import UserInsert from './admin/new-user';
import ErrorPage from './commons/errorhandling/error-page';
import styles from './commons/styles/project-style.css';
import DeviceInsert from './admin/new-device';
import UserPage from "./client/client";
import UserNotifications from "./client/user-notifications";
import EnergyConsumptionPage from "./client/energy-consumption";
import ChatWindow from "./chat/chat";
class App extends React.Component {
  componentDidMount() {
    const isLoggedIn = sessionStorage.getItem('token') !== null;
    if (isLoggedIn && window.location.pathname === '/') {
      this.handleLogout();
    }
  }

    handleLogout = async () => {
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
        window.location.href = '/';
      } else {
        console.error('An error has occured');
      }
    }
  };

  render() {
    const isLoggedIn = sessionStorage.getItem('token') !== null;
    const userRole = sessionStorage.getItem('role');

    return (
      <div className={styles.back}>
        <Router>
          <div>
            <NavigationBar />
            <Routes>
              <Route path='/' element={<Home />} />

              {isLoggedIn && userRole === 'CLIENT' && (
                <Route path='/user' element={<UserPage />} />
              )}
              {isLoggedIn && userRole === 'CLIENT' && (
                  <Route path='/user/devices' element={<UserDevices />} />
              )}
              {isLoggedIn && userRole === 'CLIENT' && (
                  <Route path='/user/notifications' element={<UserNotifications />} />
              )}
              {isLoggedIn && userRole === 'CLIENT' && (
                  <Route path='/user/history' element={<EnergyConsumptionPage />} />
              )}
              {isLoggedIn && userRole === 'CLIENT' && (
                  <Route path='/user/chat' element={<ChatWindow />} />
              )}
              {isLoggedIn && userRole === 'ADMIN' && (
                <Route path='/admin' element={<AdminPage />} />
              )}
              {isLoggedIn && userRole === 'ADMIN' && (
                <Route path='/admin/users' element={<UserList />} />
              )}
              {isLoggedIn && userRole === 'ADMIN' && (
                <Route path='/admin/newUser' element={<UserInsert />} />
              )}
              {isLoggedIn && userRole === 'ADMIN' && (
                <Route path='/admin/devices' element={<DeviceList />} />
              )}
              {isLoggedIn && userRole === 'ADMIN' && (
                <Route path='/admin/newDevice' element={<DeviceInsert />} />
              )}
              {isLoggedIn && userRole === 'ADMIN' && (
                  <Route path='/admin/chat' element={<ChatWindow />} />
              )}


              <Route path='/error' element={<ErrorPage />} />

              {!isLoggedIn && (
                <Route path='/user/*' element={<Navigate to='/' />} />
              )}
              {!isLoggedIn && (
                <Route path='/admin/*' element={<Navigate to='/' />} />
              )}

              {isLoggedIn && userRole === 'CLIENT' && (
                <Route
                  path='/admin/*'
                  element={<Navigate to='/error' />}
                />
              )}

              {isLoggedIn && userRole === 'ADMIN' && (
                <Route
                  path='/user/*'
                  element={<Navigate to='/error' />}
                />
              )}
            </Routes>
          </div>
        </Router>
      </div>
    );
  }
}

export default App;
