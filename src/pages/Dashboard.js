import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import API from "../services/api";

function Dashboard() {
    const [journals, setJournals] = useState([]);
    const [showJournals, setShowJournals] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        if (showJournals && journals.length === 0) {
            fetchJournals();
        }
    }, [showJournals]);

    const fetchJournals = async () => {
        try {
            const res = await API.get("/journal/me");
            setJournals(res.data);
        } catch (err) {
            console.error("Failed to fetch journals:", err?.response || err.message || err);
            // avoid uncaught exception; keep journals as empty array
        }
    };

    const handleAddJournal = () => {
        // Navigate to create page if available; fallback to console message
        try {
            navigate("/create");
        } catch (e) {
            window.location.href = "/create";
        }
    };

    const handleLogout = () => {
        try {
            // remove auth token and navigate to login
            localStorage.removeItem("token");
            localStorage.removeItem("rememberUser");
        } catch (e) {
            // ignore
        }
        navigate("/login");
    };

    const handleToggleShow = () => setShowJournals(prev => !prev);

    return (
        <div className="container">
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                <h2 style={{ margin: 0 }}>My Journals</h2>
                <button onClick={handleLogout} style={{ padding: "8px 12px", borderRadius: 6, border: "none", background: "#ef4444", color: "white", cursor: "pointer" }}>
                    Logout
                </button>
            </div>
            <div style={{ margin: "10px 0", display: "flex", gap: "10px" }}>
                <button onClick={handleAddJournal} style={{ padding: "8px 12px", borderRadius: 6, border: "none", background: "#4b7bec", color: "white", cursor: "pointer" }}>
                    Add Journal Entry
                </button>
                <button onClick={handleToggleShow} style={{ padding: "8px 12px", borderRadius: 6, border: "1px solid #ccc", background: "white", color: "#222", cursor: "pointer" }}>
                    {showJournals ? "Hide Journal Entries" : "Show Journal Entries"}
                </button>
            </div>

            {showJournals && journals.map((journal) => (
                <div key={journal.id} style={{ background: "#2c2c54", padding: "15px", margin: "10px", borderRadius: "10px" }}>
                    <h3>{journal.title}</h3>
                    <p>{journal.content}</p>
                    <small>{journal.sentiment}</small>
                </div>
            ))}
        </div>
    );
}

export default Dashboard;
