package com.example.focus;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtils {

    /**
     * Hashes a password using SHA-256 and returns the hexadecimal representation of the hash. @para -> password we are hashing
     * @return a hex string
     */
    public static String hashPassword(String password) {
        try {
            // Create MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Perform the hashing
            byte[] hash = digest.digest(password.getBytes());
            // Convert byte array into signum representation
            BigInteger number = new BigInteger(1, hash);
            StringBuilder hexString = new StringBuilder(number.toString(16)); //to hex value

            while (hexString.length() < 64) {
                hexString.insert(0, '0');
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

