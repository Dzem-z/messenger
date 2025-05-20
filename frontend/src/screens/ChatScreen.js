import {connect, disconnect, sendMessage, setConnectionCallback} from "../connectors/stompClient.js";
import { useState, useCallback, useEffect, useRef } from 'react';
import fetchData from "../connectors/fetchData.js";
import { MAX_FILE_SIZE } from "../const.js";
import uploadFile from "../connectors/uploadFile.js";

export default function ChatScreen({ chat }) {
    const [messages, setMessages] = useState([]);
    const [message, setMessage] = useState("");
    const [file, setFile] = useState(null);
    const [error, setError] = useState('');

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
        } catch (err) {
            console.log('Upload failed' + err);
        }
    };

    function handleChangeMessage(e) {
        setMessage(e.target.value);
    }

    useEffect(() => {
        console.log("Links: ", chat._links);
        Promise.all([fetchData(chat._links.messages.href),
                     fetchData(chat._links.files.href)])
            .then(([result1, result2]) => {
                const combined = [];

                console.log("results", result1, result2);

                if(result1._embedded != undefined) {
                    const messages = result1._embedded.messageDtoes || [];
                    combined.push(...messages); 
                }
                if(result2._embedded != undefined) {
                    const files = result2._embedded.fileDtoes || [];
                    combined.push(...files);
                }

                setMessages(combined.sort(
                    (a, b) => new Date(a.dateOfPosting) - new Date(b.dateOfPosting)
                ));

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
    
    useEffect(connectStomp, [connectStomp]);

    return <div className="border-box main">
                <form className="border-box message-box" method="post" onSubmit={fileUploaded() ? handleUpload : handleSendMessage}>
                    <div className="send-form">
                        <div className="send-form-block">
                            <input 
                                type="text" 
                                name="message" 
                                className="send-form-input" 
                                placeholder="enter your message..."
                                value={message}
                                onChange={handleChangeMessage}
                                disabled={file != null}
                            />
                            <button type="button" id="send-file" className="fancy-button send-message" onClick={handleFileClick}>Upload file</button>
                            <input
                                type="file"
                                ref={fileInputRef}
                                style={{ display: 'none' }}
                                onChange={handleChangeFile}
                            />
                            {error && <p style={{ color: 'red' }}>{error}</p>}
                            {file && <p><b>{file.name}</b></p>}
                            <button id="send" className="fancy-button send-message" type="submit">Send</button>
                        </div>
                    </div>
                </form>
                <div className="vertical-margin-4-chat"></div>
                <div className="border-box messages">
                    <table id="conversation" className="table table-striped">
                        <thead>
                        <tr>
                            <th>Messages</th>
                        </tr>
                        </thead>
                        <tbody id="greetings">
                            {messages.map((message, index) => 
                                <tr key={index}>
                                    <td>
                                        <b>{isFile(message) ? "📁" : "💬"} {message.author.username}:</b>{" "}
                                        {isFile(message) ? (
                                            <a 
                                                href={`http://localhost:8081/api/files/one/${message.idToken}`} 
                                                target="_blank" 
                                                rel="noopener noreferrer"
                                            >
                                                {message.content}
                                            </a>
                                        ) : (
                                            message.content
                                        )}
                                    </td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </div>
            </div>;
}