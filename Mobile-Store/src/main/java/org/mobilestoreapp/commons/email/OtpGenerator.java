package org.mobilestoreapp.commons.email;


import java.security.SecureRandom;


public class OtpGenerator {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public int sixDigit() {
        return SECURE_RANDOM.nextInt(1_000_000); // 0..999999
    }

    public static String toCode(int otp) {
        return String.format("%06d", otp);
    }
}
