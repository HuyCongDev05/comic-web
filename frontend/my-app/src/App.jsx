import AppRoutes from "./routes/AppRoutes";
import ScrollToTop from "./components/ScrollToTop";
import useAutoTimerClient from "./hooks/RefreshTokenClient";
import useAutoTimerAdmin from "./hooks/RefreshTokenAdmin";
function App() {
  useAutoTimerClient();
  useAutoTimerAdmin();
  return (
    <>
      <ScrollToTop />
      <AppRoutes />
    </>
  );
}
export default App;
