import { useEffect, useRef } from "react";
import {useAuth} from "../context/AuthContext";
import AccountApi from "../api/Account";

export default function useAutoTimer() {
    const { user, logout } = useAuth();
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

        const getTokenExpiryTime = () => {
            const token = localStorage.getItem("accessToken");
            if (!token) return null;

            try {
                const payload = JSON.parse(atob(token.split('.')[1]));
                return payload.exp * 1000;
            } catch (err) {
                return null;
            }
        };

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

                    scheduleNextRefresh();
                }
            } catch (err) {
                if (err.name === "AbortError") {
                } else {
                    logout();
                }
            } finally {
                abortRef.current = null;
            }
        };

        const scheduleNextRefresh = () => {
            const expiryTime = getTokenExpiryTime();

            if (!expiryTime) {
                timerRef.current = setTimeout(refreshAccessToken, 13 * 60 * 1000);
                return;
            }

            const now = Date.now();
            const timeUntilExpiry = expiryTime - now;

            const refreshTime = Math.max(timeUntilExpiry - 60 * 1000, 0);

            if (user) {
                timerRef.current = setTimeout(refreshAccessToken, refreshTime);
            }
        };

        scheduleNextRefresh();

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