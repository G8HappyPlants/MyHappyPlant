const BASE = "http://localhost:8080/api/auth";

export async function login(email, password) {
    const res = await fetch(`${BASE}/login`, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({email, password}),
    });
    if (!res.ok) {
        const body = await res.json();
        console.log("Server error response:", body);
        throw new Error(body.error || "Login Failed");
    }
    return res.json();
}

export async function register(username, email, password) {
    const res = await fetch(`${BASE}/register`, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({username, email, password}),
    });
    if (!res.ok) {
        const body = await res.json();
        console.log("Server error response:", body);
        throw new Error(body.error || "Register failed");
    }
    return res.json();
}

export default {login, register};
