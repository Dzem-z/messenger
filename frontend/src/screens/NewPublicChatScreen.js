import { useEffect, useState } from "react";
import fetchData from "../connectors/fetchData";
import { getUser } from "../connectors/User";
import axios from "axios";
import { host } from "../const";


export default function NewPublicChatScreen() {
    const [selectedUsers, setSelectedUsers] = useState([]);
    const [userPrefix, setUserPrefix] = useState("");
    const [chatName, setChatName] = useState("");
    const [foundUsers, setFoundUsers] = useState([]);
    const [created, setCreated] = useState(false);

    useEffect(() => {
        fetchData("http://" + host + "/api/users?prefix=" + userPrefix)
            .then(userList => {
                console.log(userList);
                setFoundUsers(userList._embedded?.userDtoes ?? [])
            })
    }, [userPrefix]);

    function createChat() {
        getUser().then(
            currentUser => {
                console.log({
                    name: chatName,
                    members: selectedUsers.concat(currentUser),
                    isPrivate: false,
                });
                return post("http://" + host + "/api/chats/create", 
                    {
                        name: chatName,
                        members: selectedUsers.concat(currentUser),
                        isPrivate: false,
                    }
                )
            }
        ).then(res => {
            setCreated(true);
            setTimeout(() => window.location.replace("http://"  + host + "/index.html"),1e3)
        });
    }

    function selectUser(user) {
        setSelectedUsers(previousUsers => {
            if(previousUsers
                    .find(listUser => user.username == listUser.username)) {
                return previousUsers.filter(listUser => user.username != listUser.username); }
            else {
                return previousUsers.concat(user);}})
    }

    return <div className="border-box main">
        <div className="border-box find-user">
            <div className="send-form-block">
                <div className="text-block find-user-block">
                    find user:
                </div>
                <input type="text" name="search" className="send-form-input" placeholder="search for user..."
                    value={userPrefix}
                    onChange={e => setUserPrefix(e.target.value)}
                />
            </div>
        </div>
        <div className="vertical-margin-4-chat"></div>
        <div className="border-box public-chat-search-results">
            <ul>
                {foundUsers.map(
                    user => 
                    <li className="search-wrap">
                        <button className={
                                `tile search-result ${selectedUsers.find(listUser => user.username == listUser.username) ? "selected" : ""}`}
                            onClick={() => selectUser(user)}>
                            {user.username}
                        </button>
                        <div className="vertical-margin-10-tile"></div>
                    </li>
                )}
            </ul>
        </div>
        <div className="vertical-margin-4-chat"></div>
        <div className="select-info">
            <div className="text-block new-chat-name-block">
                    chat name:
            </div>
                <input type="text" name="search" className="send-form-input" placeholder="enter chat name"
                    value={chatName}
                    onChange={e => setChatName(e.target.value)}
                />
        </div>
        <div className="vertical-margin-4-chat"></div>
        <div className="select-info">
            <div className="text-block find-user-block">   
                Selected users:
            </div>
            <div className="text-block selected-user-block">
                {selectedUsers.slice(0,-1)
                    .reduce((acc, user) => user.username + ", " + acc, selectedUsers.at(-1)?.username ?? "")} 
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