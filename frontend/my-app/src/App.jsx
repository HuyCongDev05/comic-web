import AppRoutes from "./routes/AppRoutes";
import ScrollToTop from "./components/ScrollToTop";
import { useAuth } from "../../my-app/src/context/AuthContext";
import { useEffect } from "react";
import AccountApi from "./api/Account";

function App() {
  const { login,logout } = useAuth();
  useEffect(() => {
    const fetchAccessToken = async () => {
      try {
        const res = await AccountApi.refreshToken();
        if (res.data) {
          localStorage.setItem("accessToken", res.data.accessToken);
        }
      } catch (err) {
        logout();
      }
    };

    fetchAccessToken();
  }, []);

  return (
    <>
      <ScrollToTop />
      <AppRoutes />
    </>
  );
}
export default App;
