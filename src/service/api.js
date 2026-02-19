import axios from "axios";

const API = axios.create({
    baseURL: "http://localhost:8080",
});

API.interceptors.request.use((req) => {
    const token = localStorage.getItem("token");
    if (token) {
        req.headers.Authorization = `Bearer ${token}`;
    }
    return req;
});

// log response errors with details to help debug Network / CORS issues
API.interceptors.response.use(
    (res) => res,
    (error) => {
        try {
            console.error("API response error:", {
                message: error.message,
                config: error.config,
                request: error.request,
                response: error.response,
            });
        } catch (e) {
            console.error("Error logging API error", e);
        }
        return Promise.reject(error);
    }
);

export default API;
