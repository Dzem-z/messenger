import { useEffect, useState } from "react"
import fetchData from "./connectors/fetchData";
import { getUser } from "./connectors/User";
import processChatName from "./process/ProcessChatName";
import axios from "axios";
import { host } from "./const";

export default function LeaveChatScreen({ user, chats }) {
    const [chat, setChat] = useState({name: "", members: []});
    const [left, setLeft] = useState(false);

    function leaveChat() {
        return httpDelete(chat._links.leave.href)
        .then(res => {
            setLeft(true);
            setTimeout(() => window.location.replace("http://"  + host + "/index.html"),1e3)
        });
    }

    return <div className="border-box main">
        <div className="border-box scroll-chats">
            <ul>
                {chats.map(
                    chat => 
                    <li className="search-wrap">
                        <button className="tile search-result" onClick={() => setChat(chat)}>
                            {processChatName(chat, user.username)}
                        </button>
                        <div className="vertical-margin-10-tile"></div>
                    </li>
                )}
            </ul>
        </div>
        <div className="vertical-margin-4-chat"></div>
        <div className="select-info">
            <div className="text-block find-block">   
                Selected Chat:
            </div>
            <div className="text-block selected-user-block">
                {processChatName(chat, user.username)}
            </div>
            <button onClick={leaveChat} className="fancy-button create-chat-button">Leave</button>
        </div>
        {left ? <div>Left chat {processChatName(chat, user.username)} </div> : <></>}
    </div>
}

function httpDelete(path, data) {
    return axios.get("/csrf")
            .then(tokenResp => {
                console.log(tokenResp);
                let config = {
                    headers: {
                        'X-CSRF-TOKEN': tokenResp.data.token,
                    }
                  }
                return axios.delete(path, data, config);
            })
            .then(res => res.data)
}