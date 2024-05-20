import React, { Component } from 'react';
import './user-list.css';

class DeviceInsert extends Component {
  constructor(props) {
    super(props);
    this.state = {
      newDevice: {
        name: '',
        description: '',
        address: '',
        maximumEnergyConsumption: 100,
        user: null,
      },
      users: [],
    };
  }

  componentDidMount() {
    this.getUsers();
  }

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

  handleNewDeviceChange = (field, value) => {
    const { newDevice } = this.state;
    newDevice[field] = value;
    this.setState({ newDevice });
  };

  handleUserChange = (value) => {
    const { newDevice } = this.state;
    newDevice.user = value;
    this.setState({ newDevice });
  };

  handleInsertDevice = () => {
    const { newDevice } = this.state;

    if (
      newDevice.name.trim() === '' ||
      newDevice.description.trim() === '' ||
      newDevice.address.trim() === ''
    ) {
      window.alert('Please fill in all required fields.');
      return;
    }

    fetch('http://localhost:8081/device', {
      method: 'POST',
      headers: {
        'Authorization': 'Bearer ' + sessionStorage.getItem('token'),
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(newDevice),
    })
      .then((response) => {
        if (response.status === 201 || response.status === 200) {
          window.location.href = '/admin/devices';
          window.alert('Device inserted successfully');
          this.setState({
            newDevice: {
              name: '',
              description: '',
              address: '',
              maximumEnergyConsumption: 100,
              user: null,
            },
          });
        } else {
          window.alert('Failed to insert the device');
        }
      })
      .catch((error) => {
        console.error('Error inserting device:', error);
      });
  };

  render() {
    const { newDevice, users } = this.state;
    return (
      <div className="insert-user-container">
        <h3>Insert New Device</h3>
       < input
          type="text"
          placeholder="Name"
          value={newDevice.name}
          onChange={(e) => this.handleNewDeviceChange('name', e.target.value)}
          style={{ marginBottom: '20px', padding: '10px' }}
        />
        <input
          type="text"
          placeholder="Description"
          value={newDevice.description}
          onChange={(e) => this.handleNewDeviceChange('description', e.target.value)}
          style={{ marginBottom: '20px', padding: '10px' }}
        />
        <input
          type="text"
          placeholder="Address"
          value={newDevice.address}
          onChange={(e) => this.handleNewDeviceChange('address', e.target.value)}
          style={{ marginBottom: '20px', padding: '10px' }}
        />
        <input
          type="number"
          placeholder="Maximum Energy Consumption"
          value={newDevice.maximumEnergyConsumption}
          onChange={(e) =>
            this.handleNewDeviceChange('maximumEnergyConsumption', e.target.value)
          }
          style={{ marginBottom: '20px', padding: '10px' }}
          min="100"
          max="100000"
        />
        <select
          value={newDevice.user ? newDevice.user.id : ''}
          onChange={(e) => this.handleUserChange(users.find((user) => user.id === e.target.value))}
          style={{ marginBottom: '20px', padding: '10px' }}
        >
          <option value="">Select a User</option>
          {users.map((user) => (
            <option key={user.id} value={user.id}>
              {user.username}
            </option>
          ))}
        </select>
        <button className="round-blue-button" onClick={this.handleInsertDevice}>
          Insert Device
        </button>
      </div>
    );
  }
}

export default DeviceInsert;
