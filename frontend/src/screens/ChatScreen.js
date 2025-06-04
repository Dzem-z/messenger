import {connect, disconnect, sendMessage, setConnectionCallback} from "../connectors/stompClient.js";
import { useState, useCallback, useEffect, useRef } from 'react';
import { ReactComponent as AttachmentIcon } from '../assets/svg/attachment.svg';
import { ReactComponent as SendIcon } from '../assets/svg/send.svg';
import { ReactComponent as CancelIcon } from '../assets/svg/cancel.svg';
import fetchData from "../connectors/fetchData.js";
import { MAX_FILE_SIZE, host } from "../const.js";
import uploadFile from "../connectors/uploadFile.js";

export default function ChatScreen({ chat, user }) {
    const [messages, setMessages] = useState([]);
    const [message, setMessage] = useState("");
    const [file, setFile] = useState(null);
    const [error, setError] = useState('');
    const [chatName, setChatName] = useState("");

    const fileInputRef = useRef(null);

    const handleFileClick = () => {
        fileInputRef.current.click();
    };

    const handleChangeFile = (event) => {
        const selectedFile = event.target.files[0];

        if (selectedFile) {
          if (selectedFile.size > MAX_FILE_SIZE) {
            setError('File size exceeds 4MB. Please choose a smaller file.');
            setFile(null); 
          } else {
            setFile(selectedFile);
            setError('');
          }
        }
    
    };

    const handleUpload = async (e) => {
        e.preventDefault();
        if (!file) return;
        try {
            const result = await uploadFile(file, chat.idToken);
            setFile(null); 
        } catch (err) {
            console.log('Upload failed' + err);
        }
    };

    function handleChangeMessage(e) {
        setMessage(e.target.value);
    }

    useEffect(() => {

        console.log(window.location.pathname)
        if (chat && chat.members && chat.isPrivate !== undefined && chat.isPrivate !== null) {
            if (chat.isPrivate) {
                setChatName(chat.members.find(member => member.username !== user.username)?.username || "Your Private Chat");
            } else {
                setChatName(chat.name || "Public Chat");
            }
        } 
        fetchData(chat._links.messages.href)
            .then(result => {
                if(result._embedded != undefined) {
                    const messages = result._embedded.messageDtoes || [];

                    const files = chat?.files || [];

                    const combined = [...messages, ...files].sort(
                        (a, b) => new Date(a.dateOfPosting) - new Date(b.dateOfPosting)
                    );  
                    
                    setMessages(combined);
                } else {
                    setMessages([]);
                }
            });
        setMessage("");
        setFile(null);
    }, [chat]);

    function handleSendMessage(e) {
        e.preventDefault();
        
        const form = e.target;
        const formData = new FormData(form);
        
        if (file)
            formData.append('file', file, file.name); 

        const formJson = Object.fromEntries(formData.entries());
        setMessage("");
        sendMessage(chat.idToken, formJson.message);
    
        console.log(formJson);
    }

    const recieveMessage = useCallback((message) => {
        setMessages((prevMessages) => prevMessages.concat(JSON.parse(message.body)));
    }, []);

    const connectStomp = useCallback(() => {
        setConnectionCallback(recieveMessage);
        disconnect();
        connect(chat.idToken);
    }, [chat]);

    const isFile = (message) => {
        return message.idToken != null && message.idToken != undefined;
    }

    const fileUploaded = () => {
        return file != null && file != undefined;
    }

    const handleFileCancel = () => {
        setFile(null);
    }
    
    function getExtension(filename) {
        const parts = filename.split('.');
        if (parts.length <= 1 || parts[parts.length - 1] === '') return '';
        return parts.pop().toLowerCase();
    }

    function isImageExtension(filename) {
        const imageExtensions = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp', 'svg', 'ico', 'tiff'];
        const ext = getExtension(filename);
        return imageExtensions.includes(ext);
    }

    useEffect(connectStomp, [connectStomp]);

    return <div className="chat-container">
                <div className="chat-header">
                    <div className="avatar-circle">
                        {chatName.charAt(0).toUpperCase()}
                    </div>
                    {chatName}
                </div>
                <div className="conversation-container">
                    {messages.map((message, index) => 
                        <div key={index} className={`message-container ${message.author.username === user.username ? 'user-alligment' : 'other-alligment'}`}>
                            {message.author.username !== user.username && 
                                <div style={{width:'100%', fontSize: '10px', color: 'lightgray', textAlign: 'right', paddingRight: '10px'}}>
                                    {message.author.username}
                                </div>
                            }
                            <div className="message-wrapper">
                                {message.author.username !== user.username && 
                                    <div className="avatar-circle">
                                        {message.author.username.charAt(0).toUpperCase()}
                                    </div>
                                }
                                <div className={`message-content ${message.author.username === user.username ? 'user-message' : 'other-message'} ${isFile(message) ? 'file-message' : ''}  ${isImageExtension(message.content) ? 'image-message' : ''}`}>
                                    {isFile(message) ? (
                                        isImageExtension(message.content) ? (
                                            <a 
                                                href={`${host}/api/files/one/${message.idToken}`} 
                                                target="_blank" 
                                                rel="noopener noreferrer"
                                            >
                                                <img 
                                                    src={`${host}/api/files/one/${message.idToken}`} 
                                                    alt={message.content} 
                                                    style={{ width: '100%', borderRadius: '10px', cursor: 'pointer' }}
                                                />
                                            </a>    
                                        ) : (
                                            <a 
                                                href={`${host}/api/files/one/${message.idToken}`} 
                                                target="_blank" 
                                                rel="noopener noreferrer"
                                            >
                                                {message.content}
                                            </a>
                                        )
                                    ) : (
                                    message.content
                                )}
                                </div>
                            </div>
                            
                        </div>
                    )}
                </div>
                <div className="chat-input">
                    <form method="post" className="chat-input-container" onSubmit={fileUploaded() ? handleUpload : handleSendMessage}> 
                        <button type="button" id="send-file" className="input-button" onClick={handleFileClick}>
                              <AttachmentIcon width={35} height={35} />
                        </button>
                        {error && <p style={{ color: 'red' }}>{error}</p>}
                        {file &&
                            <>
                                <b style={{paddingInline: '5px'}}>{file.name}</b>
                                <button type="button" id="cancel-file" className="input-button" onClick={handleFileCancel}>
                                    <CancelIcon width={20} height={20} />
                                </button>
                            </>
                        }
                        <input 
                            type="text" 
                            name="message"  
                            placeholder={file ? "" : "Enter your message..."}
                            value={message}
                            className="chat-input-field"
                            onChange={handleChangeMessage}
                            disabled={file != null}
                            autocomplete="off"
                            required
                        />
                        <input
                            type="file"
                            ref={fileInputRef}
                            style={{ display: 'none' }}
                            onChange={handleChangeFile}
                        />
                        <button id="send" type="submit" className="input-button">
                            <SendIcon width={35} height={35} />
                        </button>
                    </form>
                </div>
                
            </div>
}