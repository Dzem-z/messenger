import { Routes, Route, Navigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import './Styles/App.css';
import ChatScreen from './ChatScreen';
import EmptyChatScreen from './EmptyChatScreen';
import NavBar from "./Components/NavBar";
import Blank from "./Components/Blank";
import fetchData from './connectors/fetchData';
import NewChatScreen from './NewChatScreen';
import { host } from "./const";

function App() {
  const [chats, setChats] = useState([]);
  useEffect(() => {
      fetchData("http://" + host + "/api/chats")
          .then((data) => {
              console.log(data);
              setChats(data._embedded.chatDtoes);
          })
  }, []);
  return (
    <div className="Panel-0">
      <NavBar 
        chats={chats}/>
      <Routes>
        <Route path="/" element={<Navigate replace to="/home" />} />
        { chats.length != 0 ?
          <Route path="/home" element={<ChatScreen chat={chats[0]} />} /> :
          <Route path="/home" element={<EmptyChatScreen />} />
        }
        <Route path="/userProfile" element={<Blank />} />
        <Route path="/newChat" element={<NewChatScreen />} />
        {chats.map(chat => 
          <Route path={"/chat/" + chat.idToken} element={<ChatScreen chat={chat} />} />
        )}
      </Routes>
    </div>
  );
}



export default App;
