const BASE = "http://localhost:8080/api/auth";
const USER_BASE = "http://localhost:8080/api/users";

export async function login(email, password) {
    const res = await fetch(`${BASE}/login`, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({email, password}),
    });
    if (!res.ok) {
        let message = "Login failed";
            message = (await res.json()).error ||
            message
        throw new Error(message);
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
        let message = "Register failed";

        const body = await res.json();
        message = body.fields
            ? Object.values(body.fields).join(", ")
            : (body.error || message);

        throw new Error(message);
    }
    return res.json();
}

export async function verifyEmail(token) {
    const res = await fetch(`${BASE}/verify?token=${token}`);
    if (!res.ok) {
        const body = await res.json();
        throw new Error(body.error || "Verification failed");
    }
    return res.json();
}
export async function deleteAccount() {
    const token = localStorage.getItem("token");
    const res = await fetch(`${USER_BASE}/me`, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
    });

    if (!res.ok) {
        throw new Error("Could not delete account" + res.status);
    }
    return true;
}

export default {login, register, verifyEmail, deleteAccount};
