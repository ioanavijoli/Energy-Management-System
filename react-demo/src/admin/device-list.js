import React, { Component } from 'react';
import './user-list.css';

class DeviceList extends Component {
  constructor(props) {
    super(props);
    this.state = {
      devices: [],
      users: [], 
      editableDeviceId: null,
      successMessage: '',
      errorMessage: '',
    };
  }

  componentDidMount() {
    this.getDevices();
    this.getUsers(); 
  }

  getDevices = () => {
    fetch('http://localhost:8081/device', {
      method: 'GET',
      headers: {
        'Authorization': 'Bearer ' + sessionStorage.getItem('token'),
      },
    })
      .then((response) => response.json())
      .then((data) => {
        this.setState({ devices: data });
      })
      .catch((error) => {
        console.error('Error fetching devices:', error);
      });
  };

  getUsers = () => {
    fetch('http://localhost:8081/user/all', {
      method: 'GET',
      headers: {
        'Authorization': 'Bearer ' + sessionStorage.getItem('token'),
      },
    })
      .then((response) => response.json())
      .then((data) => {
        this.setState({ users: data });
      })
      .catch((error) => {
        console.error('Error fetching users:', error);
      });
  };

  handleEditClick = (deviceId) => {
    this.setState({ editableDeviceId: deviceId });
  };

  handleSaveClick = (device) => {
    const deviceId = device.id;
    fetch(`http://localhost:8081/device/${deviceId}`, {
      method: 'PUT',
      headers: {
        'Authorization': 'Bearer ' + sessionStorage.getItem('token'),
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(device),
    })
      .then((response) => {
        if (response.status === 200) {
          window.alert('Device saved successfully');
          this.setState({ editableDeviceId: null });
          this.getDevices();
        } else {
          window.alert('Failed to save device');
        }
      })
      .catch((error) => {
        console.error('Error saving device:', error);
      });
  };

  handleDeleteClick = (device) => {
    const deviceId = device.id;
    fetch(`http://localhost:8081/device/${deviceId}`, {
      method: 'DELETE',
      headers: {
        'Authorization': 'Bearer ' + sessionStorage.getItem('token'),
      },
    })
      .then((response) => {
        if (response.status === 200) {
          window.alert('Device deleted successfully');
          this.getDevices();
        } else {
          window.alert('Failed to delete device');
        }
      })
      .catch((error) => {
        console.error('Error deleting device:', error);
      });
  };

  render() {
    const filteredUsers = this.state.users.filter(user => user.username !== sessionStorage.getItem('username'));
    return (
      <div className="user-list-container">
        <h2>Device List</h2>
        <div className="table-container">
          <table className="user-list-table">
            <thead>
              <tr>
                <th>Device ID</th>
                <th>Name</th>
                <th>Description</th>
                <th>Address</th>
                <th>Maximum Energy Consumption</th>
                <th>User</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {this.state.devices.map((device) => (
                <tr key={device.id}>
                  <td>{device.id}</td>
                  <td>
                    {this.state.editableDeviceId === device.id ? (
                      <input
                        type="text"
                        value={device.name}
                        onChange={(e) => this.handleDeviceEditChange(device, 'name', e.target.value)}
                      />
                    ) : (
                      device.name
                    )}
                  </td>
                  <td>
                    {this.state.editableDeviceId === device.id ? (
                      <input
                        type="text"
                        value={device.description}
                        onChange={(e) =>
                          this.handleDeviceEditChange(device, 'description', e.target.value)
                        }
                      />
                    ) : (
                      device.description
                    )}
                  </td>
                  <td>
                    {this.state.editableDeviceId === device.id ? (
                      <input
                        type="text"
                        value={device.address}
                        onChange={(e) =>
                          this.handleDeviceEditChange(device, 'address', e.target.value)
                        }
                      />
                    ) : (
                      device.address
                    )}
                  </td>
                  <td>
                    {this.state.editableDeviceId === device.id ? (
                      <input
                        type="text"
                        value={device.maximumEnergyConsumption}
                        onChange={(e) =>
                          this.handleDeviceEditChange(device, 'maximumEnergyConsumption', e.target.value)
                        }
                      />
                    ) : (
                      device.maximumEnergyConsumption
                    )}
                  </td>
                  <td>
                  {this.state.editableDeviceId === device.id ? (
    <select
      style={{ background: 'transparent', borderRadius: '8px', color: 'white', fontWeight: 'bold' }}
      value={device.user ? device.user.username : '-'}
      onChange={(e) => this.handleUserEditChange(device, e.target.value)}
    >
      {filteredUsers.map((user) => (
        <option
          key={user.id}
          value={user.username}
          style={{ color: 'black' }}
        >
          {user.username}
        </option>
      ))}
    </select>
  ) : (
    device.user ? device.user.username : '-' 
  )}
                  </td>
                  <td>
                    {this.state.editableDeviceId === device.id ? (
                      <button className="round-blue-button" onClick={() => this.handleSaveClick(device)}>
                        Save
                      </button>
                    ) : (
                      <button className="round-blue-button" onClick={() => this.handleEditClick(device.id)}>
                        Edit
                      </button>
                    )}
                    <button className="round-blue-button" onClick={() => this.handleDeleteClick(device)}>
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    );
  }

  handleDeviceEditChange = (device, field, value) => {
    const updatedDevice = { ...device, [field]: value };
    this.setState((prevState) => ({
      devices: prevState.devices.map((d) => (d.id === device.id ? updatedDevice : d)),
    }));
  };

  handleUserEditChange = (device, selectedUsername) => {
    const selectedUser = this.state.users.find((user) => user.username === selectedUsername);

    const updatedDevice = {
      ...device,
      user: selectedUser, 
    };

    this.setState((prevState) => ({
      devices: prevState.devices.map((d) => (d.id === device.id ? updatedDevice : d)),
    }));
  };
}

export default DeviceList;