import React, { Component } from 'react';
import './user-list.css';

class UserList extends Component {
  constructor(props) {
    super(props);
    this.state = {
      users: [],
      editableUserId: null,
      successMessage: '',
      errorMessage: '',
      loggedInUsername: '', 
    };
  }

  componentDidMount() {
    this.getLoggedInUsername();
  }

  getLoggedInUsername = () => {
    fetch('http://localhost:8080/auth/username', {
      method: 'GET',
      headers: {
        'Authorization': 'Bearer ' + sessionStorage.getItem('token'),
      },
    })
      .then((response) => response.text())
      .then((username) => {
        this.setState({ loggedInUsername: username }, () => {
          this.getUsers();
        });
      })
      .catch((error) => {
        console.error('Error fetching logged-in username:', error);
      });
  };

  getUsers = () => {
    fetch('http://localhost:8080/user/all', {
      method: 'GET',
      headers: {
        'Authorization': 'Bearer ' + sessionStorage.getItem('token'),
      },
    })
      .then((response) => response.json())
      .then((data) => {
        const filteredUsers = data.filter((user) => user.username !== this.state.loggedInUsername);
        this.setState({ users: filteredUsers });
      })
      .catch((error) => {
        console.error('Error fetching users:', error);
      });
  };

  handleEditClick = (userId) => {
    this.setState({ editableUserId: userId });
  };

  handleSaveClick = (user) => {
    const userId = user.id;
    fetch(`http://localhost:8080/user/${userId}`, {
      method: 'PUT',
      headers: {
        'Authorization': 'Bearer ' + sessionStorage.getItem('token'),
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(user),
    })
      .then((response) => {
        if (response.status === 200) {
          window.alert('User saved successfully');
          this.setState({ editableUserId: null });
          this.getUsers();
        } else {
          window.alert('Failed to save user');
        }
      })
      .catch((error) => {
        console.error('Error saving user:', error);
      });
  };

  handleDeleteClick = (user) => {
    const userId = user.id;
    fetch(`http://localhost:8080/user/${userId}`, {
      method: 'DELETE',
      headers: {
        'Authorization': 'Bearer ' + sessionStorage.getItem('token'),
      },
    })
      .then((response) => {
        if (response.status === 200) {
          window.alert('User deleted successfully');
          this.getUsers();
        } else {
          window.alert('Failed to delete user');
        }
      })
      .catch((error) => {
        console.error('Error deleting user:', error);
      });
  };

  render() {
    return (
      <div className="user-list-container">
        <h2>User List</h2>
        <div className="table-container">
          <table className="user-list-table">
            <thead>
              <tr>
                <th>User ID</th>
                <th>Email</th>
                <th>Username</th>
                <th>Age</th>
                <th>Role</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {this.state.users.map((user) => (
                <tr key={user.id}>
                  <td>{user.id}</td>
                  <td>
                    {this.state.editableUserId === user.id ? (
                      <input
                        type="text"
                        value={user.email}
                        onChange={(e) => this.handleUserEditChange(user, 'email', e.target.value)}
                        style={{ background: 'transparent', borderRadius: '8px', color: 'white', fontWeight: 'bold' }}
                      />
                    ) : (
                      user.email
                    )}
                  </td>
                  <td>
                    {this.state.editableUserId === user.id ? (
                      <input
                        type="text"
                        value={user.username}
                        onChange={(e) => this.handleUserEditChange(user, 'username', e.target.value)}
                        style={{ background: 'transparent', borderRadius: '8px', color: 'white', fontWeight: 'bold' }}
                      />
                    ) : (
                      user.username
                    )}
                  </td>
                  <td>
                    {this.state.editableUserId === user.id ? (
                      <input
                        type="number"
                        value={user.age}
                        onChange={(e) => this.handleUserEditChange(user, 'age', e.target.value)}
                        min="18" 
                        style={{ background: 'transparent', borderRadius: '8px', color: 'white', fontWeight: 'bold' }}
                      />
                    ) : (
                      user.age
                    )}
                  </td>
                  <td>
                    {this.state.editableUserId === user.id ? (
                      <select
                        value={user.role}
                        onChange={(e) => this.handleUserEditChange(user, 'role', e.target.value)}
                        style={{ background: 'transparent', borderRadius: '8px', color: 'white', fontWeight: 'bold' }}
                      >
                        <option value="ADMIN" style={{ color: 'black' }}>ADMIN</option>
                        <option value="CLIENT" style={{ color: 'black' }}>CLIENT</option>
                      </select>
                    ) : (
                      user.role
                    )}
                  </td>
                  <td>
                    {this.state.editableUserId === user.id ? (
                      <button className="round-blue-button" onClick={() => this.handleSaveClick(user)}>
                        Save
                      </button>
                    ) : (
                      <button className="round-blue-button" onClick={() => this.handleEditClick(user.id)}>
                        Edit
                      </button>
                    )}
                    <button className="round-blue-button" onClick={() => this.handleDeleteClick(user)}>
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

  handleUserEditChange = (user, field, value) => {
    const updatedUser = { ...user, [field]: value };
    this.setState((prevState) => ({
      users: prevState.users.map((u) => (u.id === user.id ? updatedUser : u)),
    }));
  };
}

export default UserList;