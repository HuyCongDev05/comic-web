import AppRoutes from "./routes/AppRoutes";
import {ScrollToTop} from "@comics/shared";
import useAutoTimer from "./hooks/RefreshToken.jsx";

function App() {
    useAutoTimer();
  return (
    <>
      <ScrollToTop />
      <AppRoutes />
    </>
  );
}
export default App;
