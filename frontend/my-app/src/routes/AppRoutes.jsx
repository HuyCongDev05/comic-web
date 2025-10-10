import { Routes, Route } from "react-router-dom";
import Home from "../pages/Home";
import Follow from "../pages/Follow";
import Category from "../pages/Category";
import History from "../pages/History";
import MainLayout from "../layouts/MainLayout";

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<MainLayout><Home/></MainLayout>} />
      <Route path="/Home" element={<MainLayout><Home/></MainLayout>} />
      <Route path="/Follow" element={<MainLayout><Follow/></MainLayout>} />
      <Route path="/Category" element={<MainLayout><Category/></MainLayout>} />
      <Route path="/History" element={<MainLayout><History/></MainLayout>} />
    </Routes>
  );
}
