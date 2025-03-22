import { NavLink } from "react-router-dom";
import ChatBar from "./ChatBar";
import { getUser } from "../connectors/User.js";
import { useEffect, useState } from "react";

function NavBar({ chats, user }) {
  //const [user, setUser] = useState({username: "noUser"})

  

  return (<>
    <div className="border-box main-nav">
      <div className="Nav-User">
        {user.username}
      </div>
        {<nav> 
        <div className="links">
          <ul >
              <li>
              <NavLink to="/userProfile">Profile</NavLink>
              </li>
              <li>
              <NavLink to="/leaveChat">Leave chat</NavLink>
              </li>
              <li>
                <NavLink to="/newPrivateChat">Create new private chat</NavLink>
              </li>
          </ul>
        </div>
        <div className="Chat-Nav">
          <ChatBar 
            chats={chats}
            userName={user.username}/>
        </div>
        </nav>}
    </div>
    </>
  );
};

export default NavBar;