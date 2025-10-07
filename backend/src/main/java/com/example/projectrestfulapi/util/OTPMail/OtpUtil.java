package com.example.projectrestfulapi.util.OTPMail;

import java.util.Random;

public class OtpUtil {
    public static String generateOTP() {
        Random random = new Random();
        int token = random.nextInt(1_000_000);
        return String.format("%06d", token);
    }
}
