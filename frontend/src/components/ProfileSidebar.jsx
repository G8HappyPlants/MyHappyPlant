import React from "react";
import "../styles/ProfileSidebar.css";

//TODO - note: Nothing here works yet. It is merely a framework to apply things to
const ProfileSidebar = ({ open, onClose }) => {
  return (
    <div className={`profile-sidebar${open ? " open" : ""}`}> 
      <button className="close-btn" onClick={onClose}>×</button>
      <div className="profile-content">
        <h3>User Profile</h3>
        <ul>
          <li>Settings</li>
          <li>Change Password</li>
          <li>Logout</li>
        </ul>
      </div>
    </div>
  );
};

export default ProfileSidebar;
