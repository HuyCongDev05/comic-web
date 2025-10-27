import AppRoutes from "./routes/AppRoutes";
import ScrollToTop from "./components/ScrollToTop";
import useAutoTimer from "./hooks/RefreshToken";
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
