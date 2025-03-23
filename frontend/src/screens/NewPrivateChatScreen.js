import { useEffect, useState } from "react"
import fetchData from "../connectors/fetchData";
import { getUser } from "../connectors/User";
import axios from "axios";
import { host } from "../const";

export default function NewPrivateChatScreen() {
    const [user, setUser] = useState({username: ""});
    const [prefix, setPrefix] = useState("");
    const [foundUsers, setFoundUsers] = useState([]);
    const [created, setCreated] = useState(false);

    useEffect(() => {
        fetchData("http://" + host + "/api/users?prefix=" + prefix)
            .then(userList => {
                console.log(userList);
                setFoundUsers(userList._embedded?.userDtoes ?? [])
            })
    }, [prefix]);

    function createChat() {
        getUser().then(
            currentUser => {
                console.log({
                    name: currentUser.username + "-" + user.username,
                    members: [
                        currentUser,
                        user
                    ],
                    isPrivate: true,
                });
                return post("http://" + host + "/api/chats/create", 
                    {
                        name: currentUser.username + "-" + user.username,
                        members: [
                            currentUser,
                            user
                        ],
                        isPrivate: true,
                    }
                )
            }
        ).then(res => {
            setCreated(true);
            setTimeout(() => window.location.replace("http://"  + host + "/index.html"),1e3)
        });
    }

    return <div className="border-box main">
        <div className="border-box find-user">
            <div className="send-form-block">
                <div className="text-block find-user-block">
                    find user:
                </div>
                <input type="text" name="search" className="send-form-input" placeholder="search for user..."
                    value={prefix}
                    onChange={e => setPrefix(e.target.value)}
                />
            </div>
        </div>
        <div className="vertical-margin-4-chat"></div>
        <div className="border-box private-chat-search-results">
            <ul>
                {foundUsers.map(
                    user => 
                    <li className="search-wrap">
                        <button className="tile search-result" onClick={() => setUser(user)}>
                            {user.username}
                        </button>
                        <div className="vertical-margin-10-tile"></div>
                    </li>
                )}
            </ul>
        </div>
        <div className="vertical-margin-4-chat"></div>
        <div className="select-info">
            <div className="text-block find-user-block">   
                Selected user:
            </div>
            <div className="text-block selected-user-block">
                {user.username}
            </div>
            <button onClick={createChat} className="fancy-button create-chat-button">Create</button>
        </div>
        {created ? <div>Chat succesfully created! </div> : <></>}
    </div>
}

function post(path, data) {
    return axios.get("/csrf")
            .then(tokenResp => {
                console.log(tokenResp);
                let config = {
                    headers: {
                        'X-CSRF-TOKEN': tokenResp.data.token,
                    }
                  }
                return axios.post(path, data, config);
            })
            .then(res => res.data)
}