
import { useState } from "react";
import { NavLink } from "react-router-dom";

function ChatBar({ chats, userName }) {
    const [selectedChat, setSelectedChat] = useState(null);

    console.log(chats);
    
    function processChatName(chat, userName) {
        console.log(userName);
        console.log(chat);
        if(chat.members.length == 1) {
            return chat.members[0].username;
        } else if (chat.members.length == 2) {
            return chat.members.filter((user) => user.username != userName)[0].username;
        } else {
            return chat.name
        }
            
        
    }

    return <ul>
        {chats.map((chat, index) =>
            <li key={index}>
                
                    <NavLink to={"/chat/" + chat.idToken} onClick={() => setSelectedChat(index)}>
                        <div className={`tile chat ${selectedChat == index ? "selected" : ""}`}>
                            {processChatName(chat, userName)}
                        </div>
                    </NavLink>
                
            </li>)}
            </ul>;
};

var chats = [
    {id:1, name:"chat1"},
    {id:2, name:"chat2"}
];




export default ChatBar;