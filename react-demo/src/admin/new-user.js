import React, { Component } from 'react';
import './user-list.css'; 

class UserInsert extends Component {
  constructor(props) {
    super(props);
    this.state = {
      newUser: { email: '', username: '', password: '', age: 18, role: 'CLIENT' }, 
    };
  }

  handleNewUserChange = (field, value) => {
    const { newUser } = this.state;
    newUser[field] = value;
    this.setState({ newUser });
  };

  handleInsertUser = () => {
    const { newUser } = this.state;
    fetch('http://localhost:8080/user', {
      method: 'POST',
      headers: {
        'Authorization': 'Bearer ' + sessionStorage.getItem('token'),
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(newUser),
    })
      .then((response) => {
        if (response.status === 201) {
          window.alert('User inserted successfully');
          this.setState({ newUser: { email: '', username: '', password: '', age: 18, role: 'CLIENT' } });
          window.location.href = '/admin/users';
        } else {
          window.alert('Failed to insert the user. Please verify the email and username to ensure they are not already in use.');
        }
      })
      .catch((error) => {
        console.error('Error inserting user: server problem.', error);
      });
  };

  render() {
    const { newUser } = this.state;

    return (
      <div className="insert-user-container"> 
        <h3>Insert New User</h3>
        <input
          type="text"
          placeholder="Email"
          value={newUser.email}
          onChange={(e) => this.handleNewUserChange('email', e.target.value)}
          style={{ marginBottom: '20px', padding: '10px' }}
        />
        <input
          type="text"
          placeholder="Username"
          value={newUser.username}
          onChange={(e) => this.handleNewUserChange('username', e.target.value)}
          style={{ marginBottom: '20px', padding: '10px' }}
        />
        <input
          type="password"
          placeholder="Password"
          value={newUser.password}
          onChange={(e) => this.handleNewUserChange('password', e.target.value)}
          style={{ marginBottom: '20px', padding: '10px' }}
        />
        <input
          type="number"
          placeholder="Age" 
          value={newUser.age}
          onChange={(e) => this.handleNewUserChange('age', e.target.value)}
          style={{ marginBottom: '20px', padding: '10px' }}
          min="18" 
        />
        <select
          value={newUser.role}
          onChange={(e) => this.handleNewUserChange('role', e.target.value)}
        >
          <option value="ADMIN">ADMIN</option>
          <option value="CLIENT">CLIENT</option>
        </select>
        <button className="round-blue-button" onClick={this.handleInsertUser}>
          Insert User
        </button>
      </div>
    );
  }
}

export default UserInsert;
