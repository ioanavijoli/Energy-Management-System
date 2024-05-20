import React, { Component } from 'react';
import './user.css';

class UserDevices extends Component {
    constructor(props) {
        super(props);
        this.state = {
            devices: [],
        };
    }

    componentDidMount() {
        this.getDevices();
    }

    getDevices = () => {
        fetch('http://localhost:8081/user/devices', {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + sessionStorage.getItem('token')
            },
        })
            .then((response) => response.json())
            .then((data) => {
                this.setState((prevState) => ({
                    devices: [...prevState.devices, ...data],
                }));
            })
            .catch((error) => {
                console.error('Error fetching user devices:', error);
            });
    };


    render() {
        return (
            <div className="user-devices-container">
                <h2>User Devices</h2>
                <div className="table-container">
                    <table className="user-devices-table">
                        <thead>
                        <tr>
                            <th>Device ID</th>
                            <th>Name</th>
                            <th>Description</th>
                            <th>Address</th>
                            <th>Maximum Energy Consumption</th>
                        </tr>
                        </thead>
                        <tbody>
                        {this.state.devices.map((device) => (
                            <tr key={device.id}>
                                <td>{device.id}</td>
                                <td>{device.name}</td>
                                <td>{device.description}</td>
                                <td>{device.address}</td>
                                <td>{device.maximumEnergyConsumption}</td>
                            </tr>
                        ))}

                        </tbody>
                    </table>
                </div>
            </div>
        );
    }
}

export default UserDevices;
