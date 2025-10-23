package ru.praktikum.utils;

import ru.praktikum.models.User;

import java.security.SecureRandom;

public class DataFactory {

    private static final SecureRandom RND = new SecureRandom();
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*";

    private String randomFrom(String alphabet, int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(alphabet.charAt(RND.nextInt(alphabet.length())));
        }
        return sb.toString();
    }

    private String shuffle(String s) {
        char[] a = s.toCharArray();
        for (int i = a.length - 1; i > 0; i--) {
            int j = RND.nextInt(i + 1);
            char t = a[i]; a[i] = a[j]; a[j] = t;
        }
        return new String(a);
    }

    public String uniqueEmail() {
        String local = "qa_" + randomFrom(LOWER, 8) + "_" + (1000 + RND.nextInt(9000));
        return local + "@example.com";
    }

    public String validName() {
        String base = randomFrom(LOWER, 6);
        return Character.toUpperCase(base.charAt(0)) + base.substring(1);
    }

    public String validPassword() {
        int len = 10 + RND.nextInt(5); // 10..14
        String must = ""
                + randomFrom(LOWER,1)
                + randomFrom(UPPER,1)
                + randomFrom(DIGITS,1)
                + randomFrom(SYMBOLS,1);

        String all = LOWER + UPPER + DIGITS + SYMBOLS;
        String tail = randomFrom(all, len - 4);

        return shuffle(must + tail);
    }

    public User newUser() {
        return new User(uniqueEmail(), validPassword(), validName());
    }
}
