import { useState } from "react";
import axios from "../api/axios";
import { useNavigate } from "react-router-dom";

export default function Register() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const register = async () => {
        try {
            await axios.post("/api/auth/register", {
                username,
                password,
            });

            alert("Kayıt başarılı! Giriş yapabilirsin.");
            navigate("/");
        } catch (err) {
            alert("Register failed");
        }
    };

    return (
        <div>
            <h2>Register</h2>

            <input
                placeholder="Username"
                onChange={(e) => setUsername(e.target.value)}
            />

            <input
                placeholder="Password"
                type="password"
                onChange={(e) => setPassword(e.target.value)}
            />

            <button onClick={register}>Register</button>
        </div>
    );
}