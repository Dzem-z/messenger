import fetchData from "./fetchData";
import { host } from "../const";
function getUser() {
    return fetchData("http://" + host + "/api/user");
};

export { getUser };