import React, { useState } from "react";
import API from "../services/api";
import { useNavigate, Link } from "react-router-dom";
import "./Login.css";

function Login() {
    const [userName, setUserName] = useState("");
    const [password, setPassword] = useState("");
    const [remember, setRemember] = useState(false);
    const navigate = useNavigate();

    const handleLogin = async () => {
        try {
            const res = await API.post("/public/login", {
                userName,
                password,
            });

            const token = res?.data?.token ?? (typeof res?.data === "string" ? res.data : null);
            if (token) {
                localStorage.setItem("token", token);
                if (remember) {
                    localStorage.setItem("rememberUser", userName);
                }
                navigate("/dashboard");
            } else {
                console.error("Unexpected login response:", res.data);
                alert("Login failed: unexpected server response");
            }
        } catch (err) {
            const serverMsg = err?.response?.data || err.message;
            console.error("Login error:", serverMsg);
            alert(serverMsg?.message || serverMsg || "Invalid Credentials");
        }
    };

    return (
        <div className="auth-page">
            <div className="auth-wrapper" style={{ justifyContent: 'center' }}>
                <div className="login-card">
                    <div className="login-card-top">😄 Login</div>
                    <div className="login-card-body">
                        <label className="label">Username</label>
                        <input
                            className="input"
                            placeholder="Username"
                            value={userName}
                            onChange={(e) => setUserName(e.target.value)}
                        />

                        <label className="label">Password</label>
                        <input
                            className="input"
                            type="password"
                            placeholder="Password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />

                        <div className="login-row">
                            <label className="remember">
                                <input type="checkbox" checked={remember} onChange={(e) => setRemember(e.target.checked)} />
                                Remember me
                            </label>
                            <Link to="#" className="forgot">Forgot password?</Link>
                        </div>

                        <button className="btn-primary" onClick={handleLogin}>Login</button>



                        <div className="register">
                            Don't have an account? <Link to="/signup">Signup</Link>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Login;
