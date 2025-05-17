import java.util.Base64;

public class SimpleEncrypt {
    private static final char SECRET_KEY = 'K';

    public static String encrypt(String input) {
        byte[] inputBytes = input.getBytes();
        byte[] encrypted = new byte[inputBytes.length];
        for (int i = 0; i < inputBytes.length; i++) {
            encrypted[i] = (byte)(inputBytes[i] ^ SECRET_KEY);
        }
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String input) {
        byte[] encrypted = Base64.getDecoder().decode(input);
        byte[] decrypted = new byte[encrypted.length];
        for (int i = 0; i < encrypted.length; i++) {
            decrypted[i] = (byte)(encrypted[i] ^ SECRET_KEY);
        }
        return new String(decrypted);
    }
}