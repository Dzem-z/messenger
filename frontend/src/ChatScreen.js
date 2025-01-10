import {connect, disconnect, sendName, setConnectionCallback} from "./connectors/stompClient.js";
import { useState, useCallback, useEffect } from 'react';
import fetchData from "./connectors/fetchData.js";

export default function ChatScreen({ chat }) {
    const [messages, setMessages] = useState([]);
    const [message, setMessage] = useState("");

    function handleChangeMessage(e) {
        console.log(e);
        setMessage(e.target.value);
    }

    console.log(chat);
    useEffect(() => {
        fetchData(chat._links.messages.href)
            .then(result => {
                if(result._embedded != undefined) {
                    console.log(result._embedded.messageDtoes.map(message => message.content));
                    setMessages(result._embedded.messageDtoes.map(message => message.content));
                } else {
                    setMessages([]);
                }
            });
        setMessage("");
    }, [chat]);

    function handleSendMessage(e) {
        e.preventDefault();
        
        const form = e.target;
        const formData = new FormData(form);
    
        const formJson = Object.fromEntries(formData.entries());
    
        sendName(chat.idToken, formJson.message);
    
        //console.log(formJson);
    }

    const recieveMessage = useCallback((message) => {
        setMessages((prevMessages) => prevMessages.concat(JSON.parse(message.body).content));
        
    }, []);

    const connectStomp = useCallback(() => {
        setConnectionCallback(recieveMessage);
        disconnect();
        connect(chat.idToken);
    }, [chat]);

    useEffect(connectStomp, [connectStomp]);

    return <div className="border-box main">
                <form className="border-box message-box" method="post" onSubmit={handleSendMessage}>
                    <div className="send-form">
                        <div className="send-form-block">
                            <input 
                                type="text" 
                                name="message" 
                                className="send-form-input" 
                                placeholder="enter your message..."
                                value={message}
                                onChange={handleChangeMessage}></input>
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
                            {messages.map((mes, index) => 
                                <tr key={index}>
                                    <td>{mes}</td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </div>
            </div>;
}