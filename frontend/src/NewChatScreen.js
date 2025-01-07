import { useEffect, useState } from "react"
import fetchData from "./connectors/fetchData";
import { getUser } from "./connectors/User";
import axios from "axios";
import { host } from "./const";

export default function NewChatScreen() {
    const [user, setUser] = useState({username: ""});
    const [prefix, setPrefix] = useState("");
    const [foundUsers, setFoundUsers] = useState([]);
    const [created, setCreated] = useState(false);

    useEffect(() => {
        fetchData("http://" + host + "/api/users?prefix=" + prefix)
            .then(userList => {
                console.log(userList);
                setFoundUsers(userList._embedded.userDtoes)
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
                    ]
                });
                return post("http://" + host + "/api/chats/create", 
                    {
                        name: currentUser.username + "-" + user.username,
                        members: [
                            currentUser,
                            user
                        ]
                    }
                )
            }
        ).then(res => {
            setCreated(true);
            setTimeout(() => window.location.replace("http://"  + host + "/index.html"),1e3)
        });
    }

    return <div className="Panel-1-Chat">
        <div className="Panel-2-Find">
            <div className="send-form-block">
                <div className="find-user-block">
                    find user:
                </div>
                <input 
                    value={prefix}
                    onChange={e => setPrefix(e.target.value)}
                />
            </div>
        </div>
        <ul>
            {foundUsers.map(
                user => 
                <li>
                    <button className="Panel-2-Search-Tile" onClick={() => setUser(user)}>
                        {user.username}
                    </button>
                </li>
            )}
        </ul>
        <div>
            Selected user:
            {user.username}
            <button onClick={createChat}>submit</button>
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