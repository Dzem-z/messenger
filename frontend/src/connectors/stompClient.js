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

function setConnectionCallback(callback) {
    stompClient.onConnect = (frame) => {
        setConnected(true);
        console.log('Connected: ' + frame);
        console.log("destintation: " + '/topic/messages/' + stompClient.chatId);
        stompClient.subscribe('/topic/messages/' + stompClient.chatId, callback); //(greeting) => {
           //console.log("logging: " + greeting);
            //showGreeting(JSON.parse(greeting.body).message);
        //});
    };
}

function setConnected(connected) {
    //$("#connect").prop("disabled", connected);
    //$("#disconnect").prop("disabled", !connected);
    /*if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");*/
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

function sendName(chatId, message) {
    stompClient.publish({
        destination: "/app/chat/sendMessage/" + chatId,
        body: JSON.stringify({'content': message})
    });
}

function showGreeting(message) {
    //$("#greetings").append("<tr><td>" + message + "</td></tr>");
}

/*$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendName());
});*/

export {setConnected, setConnectionCallback, connect, disconnect, sendName, showGreeting}