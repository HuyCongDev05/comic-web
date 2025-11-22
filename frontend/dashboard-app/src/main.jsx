import {createRoot} from 'react-dom/client'
import "@flaticon/flaticon-uicons/css/all/all.css";
import './index.css'
import App from './app.jsx'
import {BrowserRouter} from 'react-router-dom';
import {AuthProvider} from "@comics/shared";

createRoot(document.getElementById('root')).render(
    <BrowserRouter>
        <AuthProvider>
            <App/>
        </AuthProvider>
    </BrowserRouter>
)
