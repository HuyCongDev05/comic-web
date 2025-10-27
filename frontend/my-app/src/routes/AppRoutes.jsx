import { Routes, Route } from "react-router-dom";
import Home from "../pages/Home/Home";
import Follow from "../pages/Follow/Follow";
import Categories from "../pages/Categories";
import History from "../pages/History/History";
import MainLayout from "../layouts/MainLayout";
import NotFound from "../pages/NotFound/NotFound";
import Login from "../pages/Auth/Account/Login";
import Register from "../pages/Auth/Account/Register";
import EmailVerify from "../pages/Auth/EmailVerify/EmailVerify";
import ComicDetail from "../pages/ComicDetail/ComicDetail";
import ComicReader from "../pages/ComicReader/ComicReader";
import ComicSeeMore from "../pages/ComicSeeMore/ComicSeeMore";

export default function AppRoutes() {
  return (
    <Routes>
      <Route element={<MainLayout />}>
        <Route path="/" element={<Home />} />
        <Route path="/home" element={<Home />} />
        <Route path="/follow" element={<Follow />} />
        <Route path="/categories" element={<Categories />} />
        <Route path="/history" element={<History />} />
        <Route path="/comic/:originName" element={<ComicDetail />} />
        <Route path="/chapter/:chapter_uuid" element={<ComicReader />} />
        <Route path="/comics/:key" element={<ComicSeeMore />} />
      </Route>

      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route path="/email/verify" element={<EmailVerify />} />

      <Route path="*" element={<NotFound />} />
    </Routes>
  );
}
