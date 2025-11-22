import AppRoutes from "./routes/appRoutes";
import {ScrollToTop} from "@comics/shared";
import useAutoTimer from "./hooks/RefreshToken";

function App() {
    useAutoTimer();
    return (
        <>
            <ScrollToTop/>
            <AppRoutes/>
        </>
    );
}

export default App;
