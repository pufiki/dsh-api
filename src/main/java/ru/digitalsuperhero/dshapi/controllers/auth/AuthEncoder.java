package ru.digitalsuperhero.dshapi.controllers.auth;

import java.util.Base64;

public class AuthEncoder {
    public static String encode(String password) {
        String encodedPassword = Base64.getEncoder().encodeToString(password.getBytes());
        return encodedPassword;
    }

    public static String decodePassword(String encodedPassword) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedPassword);
        return new String(decodedBytes);
    }
}
