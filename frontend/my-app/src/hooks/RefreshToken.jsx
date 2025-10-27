import { useEffect, useRef } from "react";
import {useAuth} from "../context/AuthContext";
import AccountApi from "../api/Account";

export default function useAutoTimer() {
    const { user,logout } = useAuth();
    const timerRef = useRef(null);
    const abortRef = useRef(null);

    useEffect(() => {
        if (!user) {
            if (timerRef.current) {
                clearTimeout(timerRef.current);
                timerRef.current = null;
            }
            if (abortRef.current) {
                abortRef.current.abort();
                abortRef.current = null;
            }
            return;
        }

        const refreshAccessToken = async () => {
            if (!user) return;

            const controller = new AbortController();
            abortRef.current = controller;

            try {
                const res = await AccountApi.refreshToken({
                    signal: controller.signal,
                });
                if (res.data?.accessToken) {
                    localStorage.setItem("accessToken", res.data.accessToken);
                }
            } catch (err) {
                if (err.name === "AbortError") {
                } else {
                    logout();
                }
            } finally {
                abortRef.current = null;
                if (user) {
                    timerRef.current = setTimeout(refreshAccessToken, 13 * 60 * 1000);
                }
            }
        };

        refreshAccessToken();

        return () => {
            if (timerRef.current) {
                clearTimeout(timerRef.current);
                timerRef.current = null;
            }
            if (abortRef.current) {
                abortRef.current.abort();
                abortRef.current = null;
            }
        };
    }, [user]);
}