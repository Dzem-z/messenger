import axios from "axios";
import { host } from "../const";
 
export default function ProfileScreen() {

    function handleLogout() {
        window.location.href = host + '/logout';
    }

    function handleDeleteAccount() {
        httpDelete(host + '/deleteAccount', null)
        .then(() => 
            {window.location.href = host + '/logout';})
        .catch(() =>
            {window.location.href = host + '/logout';});
        
    }

    return <div className="border-box main">
        <div className="select-info">
            <div className="text-block profile-block">Logout:</div>
            <button className="fancy-button profile" onClick={handleLogout}> Logout </button>
        </div>
        <div className="vertical-margin-4-chat"></div>
        <div className="select-info">
            <div className="text-block profile-block">Delete account:</div>
            <button className="fancy-button profile delete-account" onClick={handleDeleteAccount}> Delete </button>
        </div>
    </div>
}

function httpDelete(path, data) {
    return axios.get("/csrf")
            .then(tokenResp => {
                console.log(tokenResp);
                let config = {
                    headers: {
                        'X-CSRF-TOKEN': tokenResp.data.token,
                    },
                    withCredentials: true
                  }
                return axios.delete(path, config);
            })
            .then(res => res.data)
}