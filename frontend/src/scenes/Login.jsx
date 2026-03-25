import {useState} from "react";
import {useNavigate} from "react-router-dom";
import useAuth from "../hooks/useAuth";
import { Link } from 'react-router-dom';
import "../styles/Auth.css";

export default function Login() {
    const [activeTab, setActiveTab] = useState("login");
    // Separate state for login and register forms
    const [loginEmail, setLoginEmail] = useState("");
    const [loginPassword, setLoginPassword] = useState("");
    const [registerUsername, setRegisterUsername] = useState("");
    const [registerEmail, setRegisterEmail] = useState("");
    const [registerPassword, setRegisterPassword] = useState("");
    const [error, setError] = useState(null);
    const navigate = useNavigate();
    const auth = useAuth();
    const [successMessage, setSuccessMessage] = useState(null);

    const handleLogin = async (e) => {
        e.preventDefault();
        setError(null);
        try {
            await auth.login(loginEmail, loginPassword);
            navigate("/user-library");
        } catch (err) {
            setError(err.message || "Login failed");
        }
    };

    const handleRegister = async (e) => {
        e.preventDefault();
        setError(null);
        try {
            await auth.register(registerUsername, registerEmail, registerPassword);
            setError(null);
            setSuccessMessage("Account created! Check your email to verify your account.");
        } catch (err) {
            setError(err.message || "Register failed");
        }
    };

    return (
        <div
            style={{
                minHeight: "100vh",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
            }}
        >
            <div className="auth-container">
                <div className="auth-tabs">
                    <button
                        className={activeTab === "login" ? "active" : ""}
                        onClick={() => setActiveTab("login")}
                    >
                        Log In
                    </button>
                    <button
                        className={activeTab === "register" ? "active" : ""}
                        onClick={() => setActiveTab("register")}
                    >
                        Create a account
                    </button>
                </div>

                <div className="auth-form-wrapper">
                    {error && <div className="error-message">{error}</div>}
                    {successMessage && <div className="success-message">{successMessage}</div>}
                    {activeTab === "login" ? (
                        <form onSubmit={handleLogin}>
                            <h2>Log In</h2>
                            <div className="form-group">
                                <label>E-post</label>
                                <input value={loginEmail} onChange={(e) => setLoginEmail(e.target.value)} type="email"
                                       autoComplete="username"/>
                            </div>
                            <div className="form-group">
                                <label>Password</label>
                                <input value={loginPassword} onChange={(e) => setLoginPassword(e.target.value)}
                                       type="password" autoComplete="current-password"/>
                            </div>
                            <button className="submit-btn" type="submit">Log In</button>
                            {}
                            <div style={{ textAlign: 'center', marginTop: '15px' }}>
                                <Link
                                    to="/forgot-password"
                                    style={{
                                        fontSize: '0.9rem',
                                        color: '#666',
                                        textDecoration: 'underline',
                                        cursor: 'pointer'
                                    }}
                                >
                                    Glömt lösenord?
                                </Link>
                            </div>
                        </form>
                    ) : (
                        <form onSubmit={handleRegister}>
                            <h2>Create account</h2>
                            <div className="form-group">
                                <label>User name</label>
                                <input value={registerUsername} onChange={(e) => setRegisterUsername(e.target.value)}
                                       type="text" autoComplete="new-username"/>
                            </div>
                            <div className="form-group">
                                <label>E-post</label>
                                <input value={registerEmail} onChange={(e) => setRegisterEmail(e.target.value)}
                                       type="email" autoComplete="new-email"/>
                            </div>
                            <div className="form-group">
                                <label>Password</label>
                                <input value={registerPassword} onChange={(e) => setRegisterPassword(e.target.value)}
                                       type="password" autoComplete="new-password"/>
                            </div>
                            <button className="submit-btn" type="submit">Create account</button>
                        </form>
                    )}
                </div>
            </div>
        </div>
    );
}