import React, { useState, useEffect } from 'react';
import SockJsClient from 'react-stomp';
import './user-notifications.css';

const SOCKET_URL = 'http://localhost:8082/websocket';

const UserNotifications = () => {
    const [notifications, setNotifications] = useState([]);
    const [userId, setUserId] = useState('');

    useEffect(() => {
        getLoggedInUsername();
    }, []);

    const getLoggedInUsername = () => {
        fetch('http://localhost:8080/auth/username', {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + sessionStorage.getItem('token'),
            },
        })
            .then((response) => response.text())
            .then((username) => {
                console.info("Logged-in Username: " + username);
                getUserIdFromUsername(username);
            })
            .catch((error) => {
                console.error('Error fetching logged-in username:', error);
            });
    };

    const getUserIdFromUsername = (username) => {
        fetch(`http://localhost:8080/user/id?username=${username}`, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + sessionStorage.getItem('token'),
            },
        })
            .then((response) => response.json())
            .then((data) => {
                setUserId(data);
            })
            .catch((error) => {
                console.error('Error fetching userId:', error);
            });
    };

    const topic = userId ? `/topic/message/${userId}` : '';

    const onConnected = () => {
        console.log('Connected!!');
    };

    const onMessageReceived = (msg) => {
        setNotifications(prevNotifications => [...prevNotifications, msg.message]);
    };

    return (
        <div className="container">
            {userId && (
                <SockJsClient
                    url={SOCKET_URL}
                    topics={[topic]}
                    onConnect={onConnected}
                    onDisconnect={() => console.log('Disconnected!')}
                    onMessage={(msg) => onMessageReceived(msg)}
                    debug={false}
                />
            )}
            <div className="notifications">
                <h3>New notifications:</h3>
                <ul>
                    {notifications.map((notification, index) => (
                        <li key={index}>{notification}</li>
                    ))}
                </ul>
            </div>
        </div>
    );
};

export default UserNotifications;
