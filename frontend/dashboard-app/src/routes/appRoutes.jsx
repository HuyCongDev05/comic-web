import { Route, Routes } from "react-router-dom";
import Dashboard from "../pages/Dashboard/Dashboard";
import LoginDashboard from "../pages/Dashboard/Auth/Account/Login";
import ProtectedRoute from "../components/ProtectedRoute";

export default function AppRoutes() {
    return (
        <Routes>
            <Route path="/"
                element={
                    <ProtectedRoute>
                        <Dashboard />
                    </ProtectedRoute>
                } />
            <Route path="/dashboard"
                element={
                    <ProtectedRoute>
                        <Dashboard />
                    </ProtectedRoute>
                } />
            <Route path="/dashboard/login" element={<LoginDashboard />} />
        </Routes>
    );
}
