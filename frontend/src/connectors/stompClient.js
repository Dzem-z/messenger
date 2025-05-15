import { Client } from "@stomp/stompjs";
import { host } from "../const";
console.log(Client);


const stompClient = new Client({
    brokerURL: 'ws://' + host + '/ws'
});



stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnectionCallback(messageCallback, fileCallback) {
    stompClient.onConnect = (frame) => {
        setConnected(true);
        console.log('Connected: ' + frame);
        console.log("Message destintation: " + '/topic/messages/' + stompClient.chatId);
        console.log("File destination: " + '/topic/files/' + stompClient.chatId);
        stompClient.subscribe('/topic/messages/' + stompClient.chatId, messageCallback);
        stompClient.subscribe('/topic/files/' + stompClient.chatId, fileCallback);
    };
}

function setConnected(connected) {
}

function connect(chatId) {
    stompClient.chatId = chatId;
    stompClient.activate();
}

function disconnect() {
    stompClient.chatId = null;
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage(chatId, message) {
    stompClient.publish({
        destination: "/app/chat/sendMessage/" + chatId,
        body: JSON.stringify({'content': message})
    });
}

export {setConnected, setConnectionCallback, connect, disconnect, sendMessage}