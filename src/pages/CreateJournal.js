import React, { useState } from "react";
import API from "../services/api";
import { useNavigate } from "react-router-dom";

function CreateJournal() {
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const navigate = useNavigate();

    const createJournal = async () => {
        // Prevent saving completely empty (null) entries
        const titleTrim = (title || "").trim();
        const contentTrim = (content || "").trim();
        if (!titleTrim && !contentTrim) {
            alert("Please enter a title or content before saving.");
            return;
        }

        const endpoints = ["/journal/me", "/journal", "/journals"];
        const errors = [];
        for (const ep of endpoints) {
            try {
                await API.post(ep, { title, content });
                navigate("/dashboard");
                return;
            } catch (err) {
                console.warn(`POST ${ep} failed:`, err?.response || err.message || err);
                errors.push({ endpoint: ep, error: err?.response || err.message || err });
            }
        }
        console.error("All journal create endpoints failed:", errors);
        alert("Failed to save journal. Check console for endpoint errors (404/401/CORS).");
    };

    return (
        <div className="container">
            <h2>Create Journal</h2>
            <div className="journal-row">
                <input
                    className="journal-input"
                    value={title}
                    placeholder="Title"
                    onChange={(e) => setTitle(e.target.value)}
                />

                <textarea
                    className="journal-textarea"
                    value={content}
                    placeholder="Content"
                    onChange={(e) => setContent(e.target.value)}
                />

                <button
                    onClick={createJournal}
                    disabled={!(title && title.trim()) && !(content && content.trim())}
                    style={{ padding: "14px 22px", borderRadius: 12, cursor: (!(title && title.trim()) && !(content && content.trim())) ? "not-allowed" : "pointer", fontSize: 18, fontWeight: 600, minWidth: 110, opacity: (!(title && title.trim()) && !(content && content.trim())) ? 0.65 : 1 }}
                >
                    Save
                </button>
            </div>
        </div>
    );
}

export default CreateJournal;
