export default function useAutoTimer() {
    const { user } = useAuth();
    const timerRef = useRef(null);

    useEffect(() => {
        if (!user) {
            if (timerRef.current) {
                clearTimeout(timerRef.current);
                timerRef.current = null;
            }
            return;
        }

        const refreshAccessToken = async () => {
            try {
                const res = await AccountApi.refreshToken();
                if (res.data) {
                    localStorage.setItem("accessToken", res.data.accessToken);
                    console.log("Access token refreshed");
                }
            } catch (err) {
                console.error("Refresh token error", err);
            } finally {
                timerRef.current = setTimeout(refreshAccessToken, 13 * 60 * 1000);
            }
        };

        refreshAccessToken();

        return () => {
            if (timerRef.current) clearTimeout(timerRef.current);
        };
    }, [user]);
}
