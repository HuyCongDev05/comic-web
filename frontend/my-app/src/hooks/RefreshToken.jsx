import { useEffect, useRef } from "react";
import { useAuth } from "../context/AuthContext";
import AccountApi from "../api/Account";

export default function useAutoTimer(actionCallback) {
    const { user } = useAuth();
    const timerRef = useRef(null);
    const userRef = useRef(user);
    const firstRunRef = useRef(true);

    useEffect(() => {
        userRef.current = user;
    }, [user]);

    const startTimer = () => {
        timerRef.current = setTimeout(async () => {
            if (!userRef.current) {
                timerRef.current = null;
                return;
            }

            if (firstRunRef.current) {
                firstRunRef.current = false;
            } else {
                try {
                    const refreshToken = await AccountApi.refreshToken();
                    if (refreshToken.data) {
                        localStorage.setItem('accessToken', refreshToken.data.accessToken);
                    }
                } catch (error) {
                    console.log('Refresh token error:', error);
                }

                actionCallback?.();
            }
            startTimer();
        }, 13 * 60 * 1000);
    };

    useEffect(() => {
        if (!userRef.current) {
            if (timerRef.current) {
                clearTimeout(timerRef.current);
                timerRef.current = null;
            }
            return;
        }

        if (!timerRef.current) {
            startTimer();
        }

        return () => {
            if (timerRef.current) clearTimeout(timerRef.current);
        };
    }, []);
}
