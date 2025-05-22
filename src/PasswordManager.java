// Manages password-related operations, user settings, and menu display for the password manager.


import java.util.Scanner;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class PasswordManager {
    private static boolean requireUppercase;
    private static boolean requireLowercase;
    private static boolean requireNumbers;
    private static boolean requireSymbols;

    private static String currentUserIntials;

    // Displays the password manager menu and handles user choices.
    public static void displayMenu(String initials) {
        Scanner scnr = new Scanner(System.in);
        currentUserIntials = initials;
        loadSettings();
        
        System.out.println("\n==== Password Manager ====");
        System.out.println("Please select one of the following options:");
        try{
            while (true) {
            System.out.println("1. Create Password");
            System.out.println("2. Generate Password");
            System.out.println("3. List Passwords");
            System.out.println("4. Modify Existing Passwords");
            System.out.println("5. Settings");
            System.out.println("6. Return to Main Menu");

            if(!scnr.hasNextInt()){
                System.out.println("Invalid input! Please enter a number.");
                scnr.nextLine();
                continue; 
            }

            int choice = scnr.nextInt();
            scnr.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    PasswordEntry.createPassword(scnr, currentUserIntials, requireUppercase, requireLowercase, requireNumbers, requireSymbols);
                    break;
                case 2:
                    PasswordEntry.generatePassword(scnr, currentUserIntials, requireUppercase, requireLowercase, requireNumbers, requireSymbols);
                    break;
                case 3:
                    PasswordTableFormatter.printPasswordTable(currentUserIntials);
                    break;
                case 4:
                    PasswordEntry.modifyPassword(scnr, currentUserIntials, requireUppercase, requireLowercase, requireNumbers, requireSymbols);
                    break;
                case 5:
                    settings(scnr);
                    break;
                case 6:
                    System.out.println("Returning...");
                    return;
                default:
                    System.out.println("Invalid option! PLease pick from options 1-4.");
                    break;
                }
            }
        }catch (Exception e){
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    // Displays and allows toggling of password requirement settings for the user.
    public static void settings(Scanner scnr) {
        while (true) {
            System.out.println("\nPassword Requirements");
            System.out.println("1. Require Uppercase Letters: " + (requireUppercase ? "ON" : "OFF"));
            System.out.println("2. Require Lowercase Letters: " + (requireLowercase ? "ON" : "OFF"));
            System.out.println("3. Require Numbers: " + (requireNumbers ? "ON" : "OFF"));
            System.out.println("4. Require Symbols: " + (requireSymbols ? "ON" : "OFF"));
            System.out.println("5. Back to Menu");
            System.out.print("Choose an option to toggle (1-4) or 5 to go back: ");
    
            if (!scnr.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number.");
                scnr.nextLine();
                continue;
            }
    
            int choice = scnr.nextInt();
            scnr.nextLine();
    
            switch (choice) {
                case 1:
                    requireUppercase = !requireUppercase;
                    saveSettings();
                    break;
                case 2:
                    requireLowercase = !requireLowercase;
                    saveSettings();
                    break;
                case 3:
                    requireNumbers = !requireNumbers;
                    saveSettings();
                    break;
                case 4:
                    requireSymbols = !requireSymbols;
                    saveSettings();
                    break;
                case 5:
                    System.out.println("Returning to menu...");
                    return;
                default:
                    System.out.println("Invalid option! Please pick from options 1-5.");
                    break;
            }
        }
    }

    // Saves the current user's password requirement settings to a file.
    private static void saveSettings() {
        String settingsFile = "settings/" + currentUserIntials + ".txt";
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(settingsFile))) {
            writer.println(requireUppercase);
            writer.println(requireLowercase);
            writer.println(requireNumbers);
            writer.println(requireSymbols);
        } catch (Exception e) {
            System.out.println("Error saving settings: " + e.getMessage());
        }
    }

    // Loads the current user's password requirement settings from a file.
    private static void loadSettings() {
        String settingsFile = "settings/" + currentUserIntials + ".txt";
        File file = new File(settingsFile);
        if (!file.exists()) {
            requireUppercase = false;
            requireLowercase = false;
            requireNumbers = false;
            requireSymbols = false;
            return;
        }
        try (Scanner fileScanner = new Scanner(file)) {
            if (fileScanner.hasNextLine()) requireUppercase = Boolean.parseBoolean(fileScanner.nextLine());
            if (fileScanner.hasNextLine()) requireLowercase = Boolean.parseBoolean(fileScanner.nextLine());
            if (fileScanner.hasNextLine()) requireNumbers = Boolean.parseBoolean(fileScanner.nextLine());
            if (fileScanner.hasNextLine()) requireSymbols = Boolean.parseBoolean(fileScanner.nextLine());
        } catch (Exception e) {
            System.out.println("Error loading settings: " + e.getMessage());
        }
    }

    //Saves a password entry to the user's password file, encrypting the password.
    public static void saveToFile(String website, String username, String password, String currentUserIntials){
        PrintWriter writer = null;
        String userPasswordFile = "passwords/" + currentUserIntials + ".txt";
        try {
            writer = new PrintWriter(new FileOutputStream(userPasswordFile, true));
            writer.println(website + "," + username + "," + SimpleEncrypt.encrypt(password));
        }
        catch (Exception e){
            System.out.println("Error saving password: " + e.getMessage());
        }
        finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}