import { useNavigate, useLocation } from 'react-router-dom';

export const PageLocation = () => {
    const navigate = useNavigate();
    const location = useLocation();

    const handleLoginClick = () => {
        if (location.pathname !== '/login') {
            navigate('/login', { state: { from: location } });
        }
    };

    const handleRegisterClick = () => {
        if (location.pathname !== '/register') {
            navigate('/register', { state: { from: location } });
        }
    };

    const from = location.state?.from?.pathname || '/';

    return { handleLoginClick, handleRegisterClick, from };
};
