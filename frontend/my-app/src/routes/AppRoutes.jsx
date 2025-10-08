import { Routes, Route } from "react-router-dom";
import Home from "../pages/Home";
import Follow from "../pages/Follow";
import Category from "../pages/Category";
import History from "../pages/History";

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/Home" element={<Home/>} />
      <Route path="/Follow" element={<Follow />} />
      <Route path="/Category" element={<Category />} />
      <Route path="/History" element={<History />} />
    </Routes>
  );
}
