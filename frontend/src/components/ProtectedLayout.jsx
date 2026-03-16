import {useState} from "react";
import Navbar from "../components/Navbar";
import ProfileSidebar from "../components/ProfileSidebar";
import "../styles/Navbar.css";
import "../styles/ProfileSidebar.css";

const ProtectedLayout = ({children}) => {
    const [sidebarOpen, setSidebarOpen] = useState(false);
    return (
        <div className="main-layout">
            <Navbar onProfileToggle={() => setSidebarOpen((v) => !v)}/>
            {sidebarOpen && <ProfileSidebar open={sidebarOpen} onClose={() => setSidebarOpen(false)}/>}
            <main className="main-content">{children}</main>
        </div>
    );
};

export default ProtectedLayout;
