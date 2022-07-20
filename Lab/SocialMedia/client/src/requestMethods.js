import axios from "axios";

const BaseURL = "http://localhost:8080/CZone/api";

export const publicRequest = axios.create({
    baseURL: BaseURL,
    // headers: {
    //     'Access-Control-Allow-Origin': 'http://localhost:8080',
    // }
});
export const userRequest = (token) => axios.create({
    baseURL: BaseURL,
    headers: {
        token,
        //'Access-Control-Allow-Origin': true
    }
});


