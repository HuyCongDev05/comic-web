export default function NotFound() {
    return (
        <>
            <main className="grid min-h-full place-items-center px-6 py-24 sm:py-32 lg:px-8">
                <div className="text-center">
                <p className="text-base font-semibold text-[#d46fff]">404</p>
                <h1 className="mt-4 text-5xl font-semibold tracking-tight text-balance text-white sm:text-7xl">
                    Không tìm thấy trang
                </h1>
                <p className="mt-6 text-lg font-medium text-pretty text-gray-400 sm:text-xl/8">
                    Rất tiếc, chúng tôi không tìm thấy trang bạn đang tìm kiếm.
                </p>
                <div className="mt-10 flex items-center justify-center gap-x-6">
                    <a
                    href="#"
                    className="rounded-md bg-[#d46fff] px-3.5 py-2.5 text-sm font-semibold text-white shadow-xs hover:bg-indigo-400 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-500"
                    >
                    Quay lại trang chủ
                    </a>
                    <a href="#" className="text-sm font-semibold text-white">
                    Liên hệ hỗ trợ <span aria-hidden="true">&rarr;</span>
                    </a>
                </div>
                </div>
            </main>
        </>
    );
};