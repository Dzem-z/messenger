

export default function processChatName(chat, userName) {
        console.log(userName);
        console.log(chat);
        if(chat.members.length == 1) {
            return chat.members[0].username;
        } else if (chat.members.length == 2) {
            return chat.members.filter((user) => user.username != userName)[0].username;
        } else {
            return chat.name
        }
            
        
}