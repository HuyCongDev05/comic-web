import Navbar from "../components/Navbar/Navbar";

export default function Home() {
    return (
        <div className="home">
            <Navbar/>
            <div className="new-comics">
                <h1>Truyện mới</h1>
            </div>
            <div className="good-comics">
                <h1>Truyện hay</h1>
            </div>
        </div>
    );
};