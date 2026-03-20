import React, {createContext, useEffect, useState} from "react";
import authService from "../services/authService";

const AuthContext = createContext(null);

export function AuthProvider({children}) {
    const [token, setToken] = useState(() => localStorage.getItem("token") || null);
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (token) localStorage.setItem("token", token);
        else localStorage.removeItem("token");
    }, [token]);

    const login = async (email, password) => {
        setLoading(true);
        try {
            const data = await authService.login(email, password);
            setToken(data.token);
            setUser({email});
            setLoading(false);
            return data;
        } catch (e) {
            setLoading(false);
            throw e;
        }
    };

    const register = async (username, email, password) => {
        setLoading(true);
        try {
            const data = await authService.register(username, email, password);
            setToken(data.token);
            setUser({username, email});
            setLoading(false);
            return data;
        } catch (e) {
            setLoading(false);
            throw e;
        }
    };

    const deleteAccount = async () => {
        setLoading(true);
        try {
            await authService.deleteAccount();
            logout(); //
            setLoading(false);
        } catch (e) {
            setLoading(false);
            throw e;
        }
    };

    const logout = () => {
        setToken(null);
        setUser(null);
    };

    const verify = async (token) => {
        setLoading(true);
        try {
            const data = await authService.verifyEmail(token);
            setToken(data.token);
            setLoading(false);
            return data;
        } catch (e) {
            setLoading(false);
            throw e;
        }
    }

    return (
        <AuthContext.Provider value={{token, user, loading, login, register, logout, deleteAccount, verify}}>
            {children}
        </AuthContext.Provider>
    );
}

export default AuthContext;
