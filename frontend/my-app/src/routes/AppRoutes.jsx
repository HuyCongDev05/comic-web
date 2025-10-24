import { Routes, Route } from "react-router-dom";
import Home from "../pages/Home/Home";
import Follow from "../pages/Follow/Follow";
import Category from "../pages/Category";
import History from "../pages/History/History";
import MainLayout from "../layouts/MainLayout";
import NotFound from "../pages/NotFound/NotFound";
import Login from "../pages/Auth/Account/Login";
import Register from "../pages/Auth/Account/Register";
import EmailVerify from "../pages/Auth/EmailVerify/EmailVerify";
import ComicDetail from "../pages/ComicDetail/ComicDetail";
import ComicReader from "../pages/ComicReader/ComicReader";
import { AuthProvider } from "../context/AuthContext";
import Comment from "../components/Comment/Comment";

export default function AppRoutes() {
  return (
    <AuthProvider>
      <Routes>
        <Route element={<MainLayout />}>
          <Route path="/" element={<Home />} />
          <Route path="/home" element={<Home />} />
          <Route path="/follow" element={<Follow />} />
          <Route path="/category" element={<Category />} />
          <Route path="/history" element={<History />} />
          <Route path="/comic/:originName" element={
            <>
              <ComicDetail />
              <Comment />
            </>
          } />
          <Route path="/chapter/:chapter_uuid" element={<ComicReader />} />
        </Route>

        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/email/verify" element={<EmailVerify />} />

        <Route path="*" element={<NotFound />} />
      </Routes>
    </AuthProvider>
  );
}
