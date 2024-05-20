import React, {useEffect, useState} from 'react'
import {over} from 'stompjs';
import SockJS from 'sockjs-client';
import './chat.css';

let stompClient = null;
const ChatWindow = () => {
    const [privateChats, setPrivateChats] = useState(new Map());
    const [publicChats, setPublicChats] = useState([]);
    const [tab, setTab] = useState("CHATROOM");
    const [from, setFrom] = useState("");
    const [to, setTo] = useState("");
    const [isOtherTyping, setIsOtherTyping] = useState(false);
    const [sentBy, setSentBy] = useState("");
    const [seenBy, setSeenBy] = useState("");
    const [publicMessageSeenBy, setPublicMessageSeenBy] = useState([]);
    const [userData, setUserData] = useState({
        username: '',
        receivername: '',
        connected: false,
        message: ''
    });
    let [clients, admins] = [];
    let role;
    let headers = {
        Authorization: sessionStorage.getItem('token'),
    };
    let typingTimer;

    useEffect(() => {
        handleUsername();
    }, []);

    const connect = () => {
        let Sock = new SockJS('http://localhost:8083/ws');
        stompClient = over(Sock);
        stompClient.connect(headers, onConnected, onError);
    }
    let usersList;
    const onConnected = async () => {
        setUserData({...userData, "connected": true});
        stompClient.subscribe('/chatroom/public', onMessageReceived);
        await fetchUsers();
        stompClient.subscribe('/user/' + userData.username + '/private', onPrivateMessage);
        if (role === 'CLIENT')
            usersList = admins;
        else
            usersList = clients;
        usersList.forEach(user => {
            if (!privateChats.get(user)) {
                privateChats.set(user, []);
                setPrivateChats(new Map(privateChats));
            }
        })

    }
    const fetchUsers = async () => {
        try {
            const token = sessionStorage.getItem('token');
            role = sessionStorage.getItem('role');
            let response;

            if (role === 'ADMIN') {
                response = await fetch('http://localhost:8080/user/chat-clients', {
                    method: 'GET',
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    },
                });
            } else {
                response = await fetch('http://localhost:8080/user/chat-admins', {
                    method: 'GET',
                    headers: {
                        'Authorization': 'Bearer ' + token,
                    },
                });
            }

            if (!response.ok) {
                console.error('Failed to fetch users for chat');
                return [];
            }

            const responseData = await response.json();

            if (role === 'ADMIN') {
                clients = responseData;
            } else {
                admins = responseData;
            }

            return responseData;
        } catch (error) {
            console.error('Error fetching users:', error);
            return [];
        }
    };


    const onMessageReceived = (payload) => {
        const payloadData = JSON.parse(payload.body);
        switch (payloadData.status) {
            case "MESSAGE":
                publicChats.push(payloadData);
                setPublicChats([...publicChats]);
                break;
            case "TYPING": {
                setIsOtherTyping(true);
                clearTimeout(typingTimer);
                typingTimer = setTimeout(resetTypingStatus, 2000);
                setFrom(payloadData.senderName);
                setTo("CHATROOM");
                break;
            }
            case "READ": {
                const seenBy = payloadData.senderName;
                setPublicMessageSeenBy(prevSeenBy => {
                    if (!prevSeenBy.includes(seenBy)) {
                        return [...prevSeenBy, seenBy];
                    }
                    return prevSeenBy;
                });
                break;
            }

        }
    }

    const onPrivateMessage = (payload) => {
        const payloadData = JSON.parse(payload.body);
        switch (payloadData.status) {
            case "MESSAGE": {

                if (privateChats.get(payloadData.senderName)) {
                    privateChats.get(payloadData.senderName).push(payloadData);
                    setPrivateChats(new Map(privateChats));
                } else {
                    let list = [];
                    list.push(payloadData);
                    privateChats.set(payloadData.senderName, list);
                    setPrivateChats(new Map(privateChats));
                }
                break;
            }
            case "TYPING": {
                setIsOtherTyping(true);
                clearTimeout(typingTimer);
                typingTimer = setTimeout(resetTypingStatus, 2000);
                setFrom(payloadData.senderName);
                setTo(payloadData.receiverName);
                break;
            }
            case "READ": {
                setSeenBy(payloadData.senderName);
                setSentBy(payloadData.receiverName);
                break;
            }
        }

    }

    const onError = (err) => {
        console.log(err);

    }
    const resetTypingStatus = () => {
        setIsOtherTyping(false);
    };
    const handlePublicMessage = (event) => {
        const {value} = event.target;
        setUserData({...userData, "message": value});
        if (stompClient) {
            const chatMessage = {
                senderName: userData.username,
                status: "TYPING"
            };
            console.log(chatMessage);
            stompClient.send("/app/message", headers, JSON.stringify(chatMessage));
        }

    }
    const handlePrivateMessage = (event) => {
        const {value} = event.target;
        setUserData({...userData, "message": value});
        if (stompClient) {
            const chatMessage = {
                senderName: userData.username,
                receiverName: tab,
                status: "TYPING"
            };
            stompClient.send("/app/private-message", headers, JSON.stringify(chatMessage));
        }
    }
    const markAsRead = (tab) => {
        if (stompClient) {
            switch (tab) {
                case "CHATROOM": {
                    const chatMessage = {
                        senderName: userData.username,
                        status: "READ"
                    };
                    stompClient.send("/app/message", headers, JSON.stringify(chatMessage));
                    break;
                }
                default: {
                    const chatMessage = {
                        senderName: userData.username,
                        receiverName: tab,
                        status: "READ"
                    };
                    stompClient.send("/app/private-message", headers, JSON.stringify(chatMessage));
                }

            }

        }
    }

    const sendValue = () => {
        if (stompClient) {
            const chatMessage = {
                senderName: userData.username,
                message: userData.message,
                status: "MESSAGE"
            };
            console.log(chatMessage);
            stompClient.send("/app/message", headers, JSON.stringify(chatMessage));
            setUserData({...userData, "message": ""});
            setPublicMessageSeenBy([]);
        }
    }

    const sendPrivateValue = () => {
        if (stompClient) {
            const chatMessage = {
                senderName: userData.username,
                receiverName: tab,
                message: userData.message,
                status: "MESSAGE"
            };

            if (userData.username !== tab) {
                privateChats.get(tab).push(chatMessage);
                setPrivateChats(new Map(privateChats));
            }
            stompClient.send("/app/private-message", headers, JSON.stringify(chatMessage));
            setUserData({...userData, "message": ""});
            setSeenBy("");
            setSentBy("");
        }
    }
    const getLoggedInUsername = async () => {
        try {
            const response = await fetch('http://localhost:8080/auth/username', {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + sessionStorage.getItem('token'),
                },
            });

            if (!response.ok) {
                throw new Error('Network response was not ok.');
            }

            const username = await response.text();
            console.info("Logged-in Username:" + username);
            return username;
        } catch (error) {
            console.error('Error fetching logged-in username:', error);
            throw error;
        }
    };

    const handleUsername = () => {
        getLoggedInUsername().then((loggedUser) => setUserData({...userData, "username": loggedUser}));
    }

    const registerUser = () => {
        connect();
    }

    return (
        <div className="container">
            {userData.connected ?
                <div className="chat-box" onClick={() => markAsRead(tab)}>
                    <div className="member-list">
                        <ul>
                            <li onClick={() => {
                                setTab("CHATROOM")
                            }} className={`member ${tab === "CHATROOM" && "active"}`}>Chatroom
                            </li>
                            {[...privateChats.keys()].map((name, index) => (
                                <li onClick={() => {
                                    setTab(name);
                                }} className={`member ${tab === name && "active"}`} key={index}>{name}</li>
                            ))}
                        </ul>
                    </div>
                    {tab === "CHATROOM" && <div className="chat-content">
                        <ul className="chat-messages">
                            {publicChats.map((chat, index) => (
                                <li className={`message ${chat.senderName === userData.username && "self"}`}
                                    key={index}>
                                    {chat.senderName !== userData.username &&
                                        <div className="avatar">{chat.senderName}</div>}
                                    <div className="message-data">{chat.message}</div>
                                    {chat.senderName === userData.username &&
                                        <div className="avatar self">me</div>}
                                </li>
                            ))}
                        </ul>
                        <div className="read-indicator">
                            {publicChats?.length > 0 && publicMessageSeenBy.length > 1 &&
                             publicChats[publicChats.length - 1].senderName === userData.username ?
                                (
                                    <p>
                                        Seen by:
                                        {publicMessageSeenBy.filter(username => username !== userData.username).map((username, index) => (
                                            <span
                                                key={index}>{`${username}${index !== publicMessageSeenBy.length - 2 ? ', ' : ''}`}</span>
                                        ))}
                                    </p>
                                ) : null}
                        </div>
                        <div className="typing-indicator">
                            {isOtherTyping && (to === "CHATROOM") && (userData.username !== from) ?
                                <p>{from} is typing...</p> : null}
                        </div>
                        <div className="send-message">
                            <input type="text" className="input-message" placeholder="enter the message"
                                   value={userData.message} onChange={handlePublicMessage}/>
                            <button type="button" className="send-button" onClick={sendValue}>send</button>
                        </div>
                    </div>}
                    {tab !== "CHATROOM" && <div className="chat-content">
                        <ul className="chat-messages">
                            {[...privateChats.get(tab)].map((chat, index) => (
                                <li className={`message ${chat.senderName === userData.username && "self"}`}
                                    key={index}
                                >
                                    {chat.senderName !== userData.username &&
                                        <div className="avatar">{chat.senderName}</div>}
                                    <div className="message-data">{chat.message}</div>
                                    {chat.senderName === userData.username &&
                                        <div className="avatar self">me</div>}
                                </li>
                            ))}
                            <div className="read-indicator">
                                {privateChats.get(tab)?.length > 0 &&
                                privateChats.get(tab)[privateChats.get(tab).length - 1].senderName !== tab &&
                                seenBy === tab && sentBy === userData.username ? (
                                    <div className="read-indicator">
                                        <p>seen ✅︎</p>
                                    </div>
                                ) : null}
                            </div>
                        </ul>
                        <div className="typing-indicator">
                            {isOtherTyping && (userData.username === to) && (tab === from) ? <p>Typing...</p> : null}
                        </div>

                        <div className="send-message">
                            <input type="text" className="input-message" placeholder="enter the message"
                                   value={userData.message} onChange={handlePrivateMessage}/>
                            <button type="button" className="send-button" onClick={sendPrivateValue}>send</button>
                        </div>

                    </div>}
                </div>
                :
                <div className="register">
                    <p style={{
                        color: 'white',
                        fontSize: '20px',
                        fontWeight: 'bold',
                        textAlign: 'center',
                        marginRight: '20px',
                        marginTop: '20px'
                    }}>
                        You are logged in as: {userData.username}
                    </p>
                    <button type="button" onClick={registerUser}>
                        Connect to chat
                    </button>
                </div>}
        </div>
    )
}

export default ChatWindow