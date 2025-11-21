import { Navigate } from "react-router-dom";
import { jwtDecode } from 'jwt-decode';

const ProtectedRoute = ({ children }) => {
    const token = localStorage.getItem('accessTokenAdmin');
    if (!token) return <Navigate to="/dashboard/login" />;

    const role = jwtDecode(token).authorities[0];
    if (role !== "ADMIN") return <Navigate to="/dashboard/login" />;

    return children;
};

export default ProtectedRoute;
