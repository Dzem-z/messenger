import { Routes, Route, Navigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import './Styles/App.css';
import ChatScreen from './screens/ChatScreen';
import EmptyChatScreen from './screens/EmptyChatScreen';
import NavBar from "./Components/NavBar";
import fetchData from './connectors/fetchData';
import NewPrivateChatScreen from './screens/NewPrivateChatScreen';
import { host } from "./const";
import { getUser } from './connectors/User';
import LeaveChatScreen from './screens/LeaveChatScreen';
import ProfileScreen from './screens/ProfileScreen';

function App() {
  const [chats, setChats] = useState([]);
  const [user, setUser] = useState({username: "No user"})
  useEffect(() => {
      fetchData("http://" + host + "/api/chats")
          .then((data) => {
              console.log(data);
              setChats(data._embedded.chatDtoes);
          })
  }, []);

  useEffect(() => {
    getUser().then (
      user => {
        setUser(user);
      }
    )
  }, [])


  return (
    <div className="Panel-0">
      <NavBar 
        chats={chats}
        user = {user}/>
      <Routes>
        <Route path="/" element={<Navigate replace to="/home" />} />
        { chats.length != 0 ?
          <Route path="/home" element={<ChatScreen chat={chats[0]} />} /> :
          <Route path="/home" element={<EmptyChatScreen />} />
        }
        <Route path="/userProfile" element={<ProfileScreen />} />
        <Route path="/leaveChat" element={<LeaveChatScreen user={user} chats={chats} />} />
        <Route path="/newPrivateChat" element={<NewPrivateChatScreen />} />
        {chats.map(chat => 
          <Route path={"/chat/" + chat.idToken} element={<ChatScreen chat={chat} />} />
        )}
      </Routes>
    </div>
  );
}



export default App;
