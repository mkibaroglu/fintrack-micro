import { useState } from "react";
import axios from "../api/axios";
import { useNavigate } from "react-router-dom";
import PageLayout from "../layout/PageLayout";

export default function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const login = async () => {
        try {
            const res = await axios.post("/api/auth/login", { username, password });
            localStorage.setItem("token", res.data.token);
            localStorage.setItem("username", username);
            navigate("/dashboard");
        } catch {
            alert("Login failed");
        }
    };

    return (
        <PageLayout>
            <div style={box}>
                <h2>Login</h2>
                <input placeholder="Username" onChange={e => setUsername(e.target.value)} />
                <input type="password" placeholder="Password" onChange={e => setPassword(e.target.value)} />
                <button onClick={login}>Login</button>
            </div>
        </PageLayout>
    );
}

const box = {
    display: "flex",
    flexDirection: "column",
    gap: 12,
    maxWidth: 400,
    margin: "0 auto",
};
