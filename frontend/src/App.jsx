import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./context/AuthProvider";
import ProtectedRoute from "./components/ProtectedRoute";
import ProtectedLayout from "./components/ProtectedLayout";
import Login from "./scenes/Login";
import OwnLibrary from "./pages/OwnLibrary";
import DatabaseLibrary from "./pages/DatabaseLibrary";

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/auth" element={<Login />} />
          <Route
            path="/own-library"
            element={
              <ProtectedRoute>
                <ProtectedLayout>
                  <OwnLibrary />
                </ProtectedLayout>
              </ProtectedRoute>
            }
          />
          <Route
            path="/database"
            element={
              <ProtectedRoute>
                <ProtectedLayout>
                  <DatabaseLibrary />
                </ProtectedLayout>
              </ProtectedRoute>
            }
          />
          <Route path="/" element={<Navigate to="/auth" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
