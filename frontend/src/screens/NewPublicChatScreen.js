import { useEffect, useState } from "react";
import fetchData from "../connectors/fetchData";
import { getUser } from "../connectors/User";
import axios from "axios";
import { host } from "../const";


export default function NewPublicChatScreen() {
    const [selectedUsers, setSelectedUsers] = useState([]);
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

    function createChat() {}
        getUser().then(
            currentUser => {
                console.log({
                    name: currentUser.username,
                    members: selectedUsers.concat(currentUser),
                    isPrivate: false,
                });
                return post("http://" + host + "/api/chats/create", 
                    {
                        name: currentUser.username,
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
                    value={prefix}
                    onChange={e => setPrefix(e.target.value)}
                />
            </div>
        </div>
        <div className="vertical-margin-4-chat"></div>
        <div className="border-box search-results">
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