import React, {useState} from 'react';
import BackgroundImg from '../commons/images/texture.jpg';
import ChatWindow from "../chat/chat";

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

const UserPage = () => {

    const handleOpenChat = () => {
        window.location.href = '/user/chat';
    };


    const handleNotificationUser = () => {
        window.location.href = '/user/notifications';
    };

    const handleDeviceUser = () => {
        window.location.href = '/user/devices';
    };
    const handleHistoryUser = () => {
        window.location.href = '/user/history';
    };

    return (
        <div style={backgroundStyle}>
            <label style={labelStyle}>Hello client, what do you want to do today?</label>
            <div style={buttonContainerStyle}>
                <button style={buttonStyle} onClick={handleNotificationUser}>
                    View notifications
                </button>
                <button style={buttonStyle} onClick={handleDeviceUser}>
                    View devices
                </button>
                <button style={buttonStyle} onClick={handleHistoryUser}>
                    View historical energy consumption
                </button>
                <button style={buttonStyle} onClick={handleOpenChat}>
                    Chat with admins
                </button>
            </div>
        </div>
    );
};

export default UserPage;