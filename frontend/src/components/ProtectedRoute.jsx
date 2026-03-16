import React from "react";
import {Navigate} from "react-router-dom";
import useAuth from "../hooks/useAuth";

export default function ProtectedRoute({children}) {
    const {token, loading} = useAuth() || {};
    if (loading) return <div style={{padding: 20}}>Loading...</div>;
    if (!token) return <Navigate to="/auth" replace/>;
    return children;
}
