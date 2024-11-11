import { Routes, Route, Navigate } from 'react-router-dom';
import './Styles/App.css';
import ChatScreen from './ChatScreen';
import NavBar from "./Components/NavBar";
import Blank from "./Components/Blank";

function App() {
  return (
    <div className="Panel-0">
      <NavBar />
      <Routes>
        <Route path="/" element={<Navigate replace to="/home" />} />
        <Route path="/home" element={<ChatScreen />} />
        <Route path="/profile" element={<Blank />} />
      </Routes>
    </div>
  );
}

export default App;
