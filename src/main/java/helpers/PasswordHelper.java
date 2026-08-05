package helpers;

import java.util.Random;

public class PasswordHelper {

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBER = "0123456789";
    private static final String SPECIAL = "!@#$%^&*";

    public static String generateValidPassword() {
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        // wajib ada masing-masing 1
        password.append(UPPER.charAt(random.nextInt(UPPER.length())));
        password.append(LOWER.charAt(random.nextInt(LOWER.length())));
        password.append(NUMBER.charAt(random.nextInt(NUMBER.length())));
        password.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

        // sisanya random (biar > 8 char)
        String allChars = UPPER + LOWER + NUMBER + SPECIAL;
        for (int i = 0; i < 4; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }
        return password.toString();
    }
}