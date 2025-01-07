
import { NavLink } from "react-router-dom";

function ChatBar({ chats }) {
    console.log(chats);
    return <ul>
        {chats.map((chat) =>
            <li>
                
                    <NavLink to={"/chat/" + chat.idToken}>
                        <div className="chat-tile">
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