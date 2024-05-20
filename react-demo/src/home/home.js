import React, { Component } from 'react';
import { Container, Row, Col, Button } from 'reactstrap';
import BackgroundImg from '../commons/images/texture.jpg';
import { Modal } from 'react-bootstrap';

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

const textStyle = {
  color: 'white',
  fontSize: '36px',
  textAlign: 'center',
  margin: '40px 0',
};


const formStyle = {
  backgroundColor: 'rgba(0, 0, 0, 0.5)',
  padding: '40px',
  borderRadius: '10px',
};

const buttonStyle = {
  marginTop: '20px',
};

class Login extends Component {

  constructor() {
    super();
    this.state = {
      username: '',
      password: '',
      showErrorModal: false, 
      errorMessage: '', 
    };
  }

  openErrorModal = (message) => {
    this.setState({ showErrorModal: true, errorMessage: message });
  };

  closeErrorModal = () => {
    this.setState({ showErrorModal: false });
  };

  handleLogin = async () => {
    const { username, password } = this.state;

    const response = await fetch('http://localhost:8080/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ username, password }),
    });

    if (response.ok) {
      const data = await response.json();
      const { token, role } = data;
  
      sessionStorage.setItem('token', token);
      sessionStorage.setItem('role', role);
  
      if (role === 'ADMIN') {
        window.location.href = '/admin';
      } else {
        window.location.href = '/user';
      }
    } else {
      this.openErrorModal('Login failed. Please check your credentials.');
      sessionStorage.removeItem('token');
      sessionStorage.removeItem('role');
    }
  };

  render() {
    return (
      <div style={backgroundStyle}>
        <h1 style={textStyle}>Energy Management System</h1>
        <Container>
          <Row className="justify-content-center">
            <Col md={6}>
              <div style={formStyle}>
                <h2 className="text-white mb-4">Login</h2>
                <form>
                  <div className="mb-3">
                    <label htmlFor="username" className="form-label text-white">Username</label>
                    <input
                      type="text"
                      className="form-control"
                      id="username"
                      onChange={(e) => this.setState({ username: e.target.value })}
                    />
                  </div>
                  <div className="mb-3">
                    <label htmlFor="password" className="form-label text-white">Password</label>
                    <input
                      type="password"
                      className="form-control"
                      id="password"
                      onChange={(e) => this.setState({ password: e.target.value })}
                    />
                  </div>
                  <Button color="primary" block onClick={this.handleLogin} style={buttonStyle}>
                    Login
                  </Button>
                </form>
              </div>
            </Col>
          </Row>
        </Container>

        <Modal show={this.state.showErrorModal} onHide={this.closeErrorModal}>
          <Modal.Header closeButton>
            <Modal.Title>Error</Modal.Title>
          </Modal.Header>
          <Modal.Body>{this.state.errorMessage}</Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={this.closeErrorModal}>
              Close
            </Button>
          </Modal.Footer>
        </Modal>
      </div>
    );
  }
}

export default Login;
