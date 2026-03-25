import {useEffect, useState, useRef} from "react";
import {useNavigate, useSearchParams} from "react-router-dom";
import useAuth from "../hooks/useAuth";

export default function VerifyEmail() {
    const [status, setStatus] = useState("Verifying your email...");
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const auth = useAuth();
    const hasRun = useRef(false);

    useEffect(() => {
        if (hasRun.current) return;
        hasRun.current = true;

        const token = searchParams.get("token");
        if(!token) {
            return;
        }
        auth.verify(token)
            .then(() => {
                setStatus("Email Verified! Welcome to MyHappyPlants");
                setTimeout(() => navigate("/user-library"), 2000);
            })
            .catch(err => setStatus(err.message || "Invalid or expired link"));
    }, []);

    return (
        <div style={{
            minHeight: "100vh",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
        }}>
            <p>{status}</p>
        </div>
    );
}






