
import { useState } from "react";
import { NavLink } from "react-router-dom";

function ChatBar({ chats, userName }) {
    const [selectedChat, setSelectedChat] = useState(null);

    console.log(chats);
    
    

    return <ul>
        {chats.map((chat, index) =>
            <li key={index}>
                
                    <NavLink to={"/chat/" + chat.idToken} onClick={() => setSelectedChat(index)}>
                        <div className={`tile chat ${selectedChat == index ? "selected" : ""}`}>
                            {chat.name}
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