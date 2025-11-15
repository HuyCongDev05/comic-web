import {Route, Routes} from "react-router-dom";
import Home from "../pages/Home/Home";
import Follow from "../pages/Follow/Follow";
import CategoriesDetail from "../pages/CategoriesDetail/CategoriesDetail";
import History from "../pages/History/History";
import MainLayout from "../layouts/MainLayout";
import NotFound from "../pages/NotFound/NotFound";
import Login from "../pages/Auth/Account/Login";
import Register from "../pages/Auth/Account/Register";
import EmailVerify from "../pages/Auth/EmailVerify/EmailVerify";
import ComicDetail from "../pages/ComicDetail/ComicDetail";
import ComicReader from "../pages/ComicReader/ComicReader";
import MoreComic from "../pages/MoreComic/MoreComic";
import SearchComic from "../pages/SearchComic/SearchComic";
import SupportAndSocial from "../pages/SupportAndSocial/SupportAndSocial";
import Profile from "../pages/Profile/Profile";
import Dashboard from "../pages/Dashboard/Dashboard";

export default function AppRoutes() {
  return (
    <Routes>
      <Route element={<MainLayout />}>
        <Route path="/" element={<Home />} />
        <Route path="/home" element={<Home />} />
        <Route path="/follow" element={<Follow />} />
        <Route path="/comics/categories/:categories" element={<CategoriesDetail />} />
        <Route path="/history" element={<History />} />
        <Route path="/comic/:originName" element={<ComicDetail />} />
        <Route path="/chapter/:chapter_uuid" element={<ComicReader />} />
        <Route path="/comics/:key" element={<MoreComic />} />
        <Route path="/comics/search/:keyword" element={<SearchComic />} />
        <Route path="/support-and-social" element={<SupportAndSocial />} />
        <Route path="/profile" element={<Profile />} />
      </Route>

      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route path="/email/verify" element={<EmailVerify />} />
      <Route path="/dashboard" element={<Dashboard />} />

      <Route path="*" element={<NotFound />} />
    </Routes>
  );
}
