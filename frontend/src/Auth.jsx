import { useState } from "react";
import "./Auth.css"; // här lägger du din auth-container CSS

export default function Auth() {
  const [activeTab, setActiveTab] = useState("login");

  return (
    <div className="auth-container">
      <div className="auth-tabs">
        <button
          className={activeTab === "login" ? "active" : ""}
          onClick={() => setActiveTab("login")}
        >
          Logga in
        </button>
        <button
          className={activeTab === "register" ? "active" : ""}
          onClick={() => setActiveTab("register")}
        >
          Registrera
        </button>
      </div>

      <div className="auth-form-wrapper">
        {activeTab === "login" ? (
          <>
            <h2>Logga in</h2>
            <div className="form-group">
              <label>E-post</label>
              <input type="email" />
            </div>
            <div className="form-group">
              <label>Lösenord</label>
              <input type="password" />
            </div>
            <button className="submit-btn">Logga in</button>
          </>
        ) : (
          <>
            <h2>Registrera konto</h2>
            <div className="form-group">
              <label>E-post</label>
              <input type="email" />
            </div>
            <div className="form-group">
              <label>Lösenord</label>
              <input type="password" />
            </div>
            <button className="submit-btn">Skapa konto</button>
          </>
        )}
      </div>
    </div>
  );
}
