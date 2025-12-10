import axios from "axios";

const instance = axios.create({
    baseURL: "http://localhost:8083",
});

instance.interceptors.request.use((config) => {
    const token = localStorage.getItem("token");
    const username = localStorage.getItem("username");

    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }

    if (username) {
        config.headers["X-User-Name"] = username;
    }

    return config;
});

export default instance;
