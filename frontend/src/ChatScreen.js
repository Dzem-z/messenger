import {connect, disconnect, sendName, setConnectionCallback} from "./connectors/stompClient.js";
import { useState, useCallback, useEffect } from 'react';
import fetchData from "./connectors/fetchData.js";

export default function ChatScreen({ chat }) {
    const [messages, setMessages] = useState([]);
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

    return <div className="Panel-1-Chat">
                <form className="Panel-2-Send" method="post" onSubmit={handleSendMessage}>
                    <div className="send-form">
                        <div className="send-form-block">
                            <input type="text" name="message" className="send-form-input" placeholder="enter your message..."></input>
                            <button id="send" className="send-message" type="submit">Send</button>
                        </div>
                    </div>
                    
                </form>
                <div className="vertical-margin-2-chat"></div>
                <div className="Panel-2-Messages">
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