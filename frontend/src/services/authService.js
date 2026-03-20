const BASE = "http://localhost:8080/api/auth";

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
}

export async function verifyEmail(token) {
    const res = await fetch(`${BASE}/verify?token=${token}`);
    if (!res.ok) {
        const body = await res.json();
        throw new Error(body.error || "Verification failed");
    }
    return res.json();
}


export default {login, register, verifyEmail};
