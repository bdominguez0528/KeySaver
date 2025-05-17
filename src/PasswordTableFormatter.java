import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;

public class PasswordTableFormatter {
    
    public static void printPasswordTable(String userInitials){
        String passwordFile = "passwords_" + userInitials + ".txt";
        File file = new File(passwordFile);

        if (!file.exists()) {
            System.out.println("No passwords found for user: " + userInitials);
            return;
        }

        System.out.printf("%-25s | %-20s | %-20s%n", "Website", "Username", "Password");
        System.out.println("---------------------------------------------------------------");

        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length == 3){
                    String decryptedPassword = SimpleEncrypt.decrypt(parts[2].replaceAll("^\\s+|\\s+$", ""));
                    System.out.printf("%-25s | %-20s | %-30s%n", parts[0], parts[1], decryptedPassword);
                    found = true;
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading password file: " + e.getMessage());
        }

        if (!found) {
            System.out.println("No passwords found for user: " + userInitials);
        }
    }
}
