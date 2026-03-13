import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import {AuthProvider} from "./context/AuthProvider";
import ProtectedRoute from "./components/ProtectedRoute";
import ProtectedLayout from "./components/ProtectedLayout";
import Login from "./scenes/Login";
import UserLibrary from "./pages/UserLibrary";
import SpeciesLibrary from "./pages/SpeciesLibrary";

function App() {
    return (
        <AuthProvider>
            <BrowserRouter>
                <Routes>
                    <Route path="/auth" element={<Login/>}/>
                    <Route
                        path="/user-library"
                        element={
                            <ProtectedRoute>
                                <ProtectedLayout>
                                    <UserLibrary/>
                                </ProtectedLayout>
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/species"
                        element={
                            <ProtectedRoute>
                                <ProtectedLayout>
                                    <SpeciesLibrary/>
                                </ProtectedLayout>
                            </ProtectedRoute>
                        }
                    />
                    <Route path="/" element={<Navigate to="/auth" replace/>}/>
                </Routes>
            </BrowserRouter>
        </AuthProvider>
    );
}

export default App;
