import React from "react";
import {useNavigate} from "react-router-dom";
import useAuth from "../hooks/useAuth";
import "../styles/ProfileSidebar.css";


const ProfileSidebar = ({open, onClose}) => {
    const {logout, deleteAccount} = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate("/auth");
        onClose && onClose();
    };

    const handleDelete = async () => {
        if (window.confirm("Are you sure? This deletes your account forever.")) {
            try {
                await deleteAccount();
                navigate("/auth");
                onClose();
            } catch (e) {
                alert("Error: " + e.message);
            }
        }
    };

    return (
        <div className={`profile-sidebar${open ? " open" : ""}`}>
            <button className="close-btn" onClick={onClose}>×</button>
            <div className="profile-content">
                <h3>User Profile</h3>
                <ul>
                    <li>Settings</li>
                    <li>Change Password</li>
                    <li style={{cursor: "pointer", color: "red"}} onClick={handleDelete}>Delete Account</li>
                    <li style={{cursor: "pointer", color: "red"}} onClick={handleLogout}>Logout</li>
                </ul>
            </div>
        </div>
    );
};

export default ProfileSidebar;
