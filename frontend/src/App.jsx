import { useState } from "react";
import "./App.css";
import "./Auth.css";

function App() {
  const [activeTab, setActiveTab] = useState("login");

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
                <input type="email" placeholder="din@email.se" />
              </div>

              <div className="form-group">
                <label>Lösenord</label>
                <input type="password" placeholder="••••••••" />
              </div>

              <button className="submit-btn">Logga in</button>
            </>
          ) : (
            <>
              <h2>Skapa konto</h2>

              <div className="form-group">
                <label>Användarnamn</label>
                <input type="text" placeholder="Ditt användarnamn" />
              </div>

              <div className="form-group">
                <label>E-post</label>
                <input type="email" placeholder="din@email.se" />
              </div>

              <div className="form-group">
                <label>Lösenord</label>
                <input type="password" placeholder="Välj ett lösenord" />
              </div>

              <button className="submit-btn">Registrera</button>
            </>
          )}
        </div>
      </div>
    </div>
  );
}

export default App;
