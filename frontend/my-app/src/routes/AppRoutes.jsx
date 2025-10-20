import { Routes, Route } from "react-router-dom";
import Home from "../pages/Home/Home";
import Follow from "../pages/Follow";
import Category from "../pages/Category";
import History from "../pages/History";
import MainLayout from "../layouts/MainLayout";
import NotFound from "../pages/404/404";
import Login from "../pages/Auth/Login";
import Register from "../pages/Auth/Register";

export default function AppRoutes() {
  return (
    <Routes>
      <Route element ={<MainLayout/>}>
        <Route path="/" element={<Home />} />
        <Route path="/home" element={<Home />} />
        <Route path="/follow" element={<Follow />} />
        <Route path="/category" element={<Category />} />
        <Route path="/history" element={<History />} />
      </Route>

      <Route>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register/>}/>
      </Route>
        
      <Route path="*" element={<NotFound />} />
    </Routes>
  );
}
