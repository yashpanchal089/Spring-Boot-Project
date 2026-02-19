import React, { useState } from "react";
import API from "../services/api";
import { useNavigate, Link } from "react-router-dom";
import "./Login.css";

function Signup() {
    const [userName, setUserName] = useState("");
    const [password, setPassword] = useState("");
    const [role, setRole] = useState("USER");
    const navigate = useNavigate();

    const handleSignup = async () => {
        // send roles as an array to match backend user model
        await API.post("/public/signup", {
            userName,
            password,
            roles: [role],
            sentimentAnalysis: false
        });

        // after signup go to login page
        navigate("/login");
    };

    return (
        <div className="auth-page">
            <div className="auth-wrapper" style={{justifyContent: 'center'}}>
                <div className="login-card">
                    <div className="login-card-top">🎉 Signup</div>
                    <div className="login-card-body">
                        <label className="label">Email</label>
                        <input className="input" placeholder="Email" value={userName} onChange={(e)=>setUserName(e.target.value)} />

                        <label className="label">Create password</label>
                        <input className="input" type="password" placeholder="Create password" value={password} onChange={(e)=>setPassword(e.target.value)} />

                        <label className="label">Role</label>
                        <select className="input" value={role} onChange={(e) => setRole(e.target.value)}>
                            <option value="USER">User</option>
                            <option value="ADMIN">Admin</option>
                        </select>

                        <button className="btn-primary" onClick={handleSignup}>Signup</button>



                        <div className="register">
                            Already have an account? <Link to="/login">Login</Link>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Signup;
