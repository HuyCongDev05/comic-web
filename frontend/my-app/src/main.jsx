import {createRoot} from 'react-dom/client'
import "@flaticon/flaticon-uicons/css/all/all.css";
import './index.css'
import App from './App.jsx'
import {BrowserRouter} from 'react-router-dom';
import {AuthProvider} from "@comics/shared";
import {AppProvider} from './context/AppContext.jsx';
import {GoogleOAuthProvider} from '@react-oauth/google';

createRoot(document.getElementById('root')).render(
    <GoogleOAuthProvider clientId={import.meta.env.VITE_GOOGLE_CLIENT_ID}>
        <BrowserRouter>
            <AuthProvider>
                <AppProvider>
                    <App />
                </AppProvider>
            </AuthProvider>
        </BrowserRouter>
    </GoogleOAuthProvider>
)
