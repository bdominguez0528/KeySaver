import java.util.Random;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

public class PasswordEntry {
    // Prompts the user to create a password entry and saves it if valid.
    public static void createPassword(Scanner scnr, String currentUserIntials, boolean requireUppercase, boolean requireLowercase, boolean requireNumbers, boolean requireSymbols) {
        System.out.println("\nEnter a website");
        String website = scnr.nextLine();
        System.out.println("\nEnter a username");
        String username = scnr.nextLine();
        System.out.println("\nEnter a password");
        String password = scnr.nextLine();

        if (validatePassword(password, requireUppercase, requireLowercase, requireNumbers, requireSymbols)) {
            PasswordManager.saveToFile(website, username, password, currentUserIntials);
            System.out.println("Password was saved successfully.\n");
        } else {
            System.out.println("Password does not meet the requirements. Check settings for requirements.\n");
        }
    }

    // Generates a random password, saves it, and displays it to the user.
    public static void generatePassword(Scanner scnr, String currentUserIntials, boolean requireUppercase, boolean requireLowercase, boolean requireNumbers, boolean requireSymbols) {
        System.out.println("\nEnter a website");
        String website = scnr.nextLine();
        System.out.println("\nEnter a username");
        String username = scnr.nextLine();

        String password = generateRandomPassword(requireUppercase, requireLowercase, requireNumbers, requireSymbols);
        PasswordManager.saveToFile(website, username, password, currentUserIntials);
        System.out.println("Generated password: " + password);
        System.out.println("Password was saved successfully.\n");
    }
    
    // Modifies an existing password entry for a given website and username.
    public static void modifyPassword(Scanner scnr, String currentUserIntials, boolean requireUppercase, boolean requireLowercase, boolean requireNumbers, boolean requireSymbols) {
        System.out.println("\nEnter website to modify:");
        String website = scnr.nextLine();
        System.out.println("Enter username to modify:");
        String username = scnr.nextLine();
        String file = "passwords/" + currentUserIntials + ".txt";
        List<String> lines = new ArrayList<>();
        boolean found = false;
        boolean passwordChanged = false;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length == 3 && parts[0].equals(website) && parts[1].equals(username)) {
                    found = true;
                    System.out.println("Enter new password:");
                    String newPassword = scnr.nextLine();
                    if (validatePassword(newPassword, requireUppercase, requireLowercase, requireNumbers, requireSymbols)) {
                        line = website + "," + username + "," + SimpleEncrypt.encrypt(newPassword);
                        passwordChanged = true;
                    } else {
                        System.out.println("Password does not meet requirements. Not changed.\n");
                    }
                }
                lines.add(line);
            }
        } catch (Exception e) {
            System.out.println("Error reading password file: " + e.getMessage());
            return;
        }
        try (PrintWriter writer = new PrintWriter(file)) {
            for (String l : lines) writer.println(l);
        } catch (Exception e) {
            System.out.println("Error writing password file: " + e.getMessage());
            return;
        }
        if (passwordChanged){
            System.out.println("Password updated successfully.\n");
        }
        else if (!found){
            System.out.println("Entry not found.\n");
        }
    }

    // Generates a random password based on the required character types.
    public static String generateRandomPassword(boolean requireUppercase, boolean requireLowercase, boolean requireNumbers, boolean requireSymbols) {
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String symbols = "!@#$%^&*()";
        StringBuilder characterPool = new StringBuilder();

        if (requireUppercase) {
            characterPool.append(upperCase);
        }
        if (requireLowercase) {
            characterPool.append(lowerCase);
        }
        if (requireNumbers) {
            characterPool.append(numbers);
        }
        if (requireSymbols) {
            characterPool.append(symbols);
        }

        if (characterPool.length() == 0) {
            throw new IllegalArgumentException("At least one character type must be selected.");
        }

        Random random = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            password.append(characterPool.charAt(random.nextInt(characterPool.length())));
        }
        return password.toString();
    }

    // Validates a password against the required character types.
    private static boolean validatePassword(String password, boolean requireUppercase, boolean requireLowercase, boolean requireNumbers, boolean requireSymbols) {
        if (requireUppercase && !password.matches(".*[A-Z].*")) {
            return false;
        }
        if (requireLowercase && !password.matches(".*[a-z].*")) {
            return false;
        }
        if (requireNumbers && !password.matches(".*[0-9].*")) {
            return false;
        }
        if (requireSymbols && !password.matches(".*[!@#$%^&*()].*")) {
            return false;
        }
        return true;
    }
}
