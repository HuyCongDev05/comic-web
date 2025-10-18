import { Routes, Route } from "react-router-dom";
import Home from "../pages/Home/Home";
import Follow from "../pages/Follow";
import Category from "../pages/Category";
import History from "../pages/History";
import MainLayout from "../layouts/MainLayout";
import Footer from "../components/Footer/Footer";
import NotFound from "../pages/404";
import Login from "../layouts/Auth/Login";

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<MainLayout><Home/><Footer/></MainLayout>} />
      <Route path="/Home" element={<MainLayout><Home/></MainLayout>} />
      <Route path="/Follow" element={<MainLayout><Follow/></MainLayout>} />
      <Route path="/Category" element={<MainLayout><Category/></MainLayout>} />
      <Route path="/History" element={<MainLayout><History /></MainLayout>} />
      <Route path="*" element={<NotFound />} />
      <Route path="/Login" element={<Login/>}/>
    </Routes>
  );
}
