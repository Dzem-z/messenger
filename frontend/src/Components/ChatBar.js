
import { useState } from "react";
import { NavLink } from "react-router-dom";
import processChatName from "../process/ProcessChatName";

function ChatBar({ chats, userName }) {
    const [selectedPrivateChat, setSelectedPrivateChat] = useState(null);
    const [selectedPublicChat, setSelectedPublicChat] = useState(null);

    console.log(chats);
    
    

    return <div>
            <div className="text-block navChat-block">Private chats</div>
            <ul>
            {chats.filter((chat) => chat.isPrivate).map((chat, index) =>
                <li key={index}>
                    <NavLink to={"/chat/" + chat.idToken} onClick={() => {
                        setSelectedPrivateChat(index)
                        setSelectedPublicChat(null)}}>
                        <div className={`tile chat ${selectedPrivateChat == index ? "selected" : ""}`}>
                            {processChatName(chat, userName)}
                        </div>
                    </NavLink>
                </li>)}
            </ul>
            <div className="text-block navChat-block">Public chats</div>
            <ul>
                
            {chats.filter((chat) => !chat.isPrivate).map((chat, index) =>
                <li key={index}>
                    <NavLink to={"/chat/" + chat.idToken} onClick={() => {
                        setSelectedPublicChat(index)
                        setSelectedPrivateChat(null)}}>
                        <div className={`tile chat ${selectedPublicChat == index ? "selected" : ""}`}>
                            {chat.name}
                        </div>
                    </NavLink>
                
                </li>)}
            </ul>
        </div>;
};

var chatsV = [
    {id:1, name:"chat1"},
    {id:2, name:"chat2"}
];




export default ChatBar;