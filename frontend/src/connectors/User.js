import fetchData from "./fetchData";
import { host } from "../const";
function getUser() {
    return fetchData(host + "/api/user");
};

export { getUser };