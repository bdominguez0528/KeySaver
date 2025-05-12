import java.util.Scanner;
import java.io.FileOutputStream;
import java.util.Random;
import java.io.PrintWriter;

public class PasswordManager {
    private static boolean requireUppercase;
    private static boolean requireLowercase;
    private static boolean requireNumbers;
    private static boolean requireSymbols;

    private static final String passwordFile = "passwords.txt";

    public static void displayMenu() {
        Scanner scnr = new Scanner(System.in);
        
        System.out.println("Please select one of the following options:");
        try{
            while (true) {
            System.out.println("1. Create Password");
            System.out.println("2. Generate Password");
            System.out.println("3. Settings");
            System.out.println("4. Exit");

            if(!scnr.hasNextInt()){
                System.out.println("Invalid input! Please enter a number.");
                scnr.nextLine();
                continue; 
            }

            int choice = scnr.nextInt();
            scnr.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    createPassword(scnr);
                    break;
                case 2:
                    generatePassword(scnr);
                    break;
                case 3:
                    settings(scnr);
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return; // Exit the loop and end the program
                default:
                    System.out.println("Invalid option! PLease pick from options 1-4.");
                    break; // Returns user to menu.

                }
            }
        }catch (Exception e){
            System.out.println("An error occurred: " + e.getMessage());
        }
        finally {
            scnr.close();
        }
    }


    public static void createPassword(Scanner scnr){
        System.out.println("Enter a website");
        String website = scnr.nextLine();
        System.out.println("\nEnter a username");
        String username = scnr.nextLine();
        System.out.println("\nEnter a password");
        String password = scnr.nextLine();

        if(validatePassword(password)){
            saveToFile(website, username, password);
            System.out.println("Password saved successfully.");
            }
            
        else{
            System.out.println("Password does not meet the requirements.");
        }
    }

    public static void generatePassword(Scanner scnr){
        System.out.println("Enter a website");
        String website = scnr.nextLine();
        System.out.println("\nEnter a username");
        String username = scnr.nextLine();

        String password = generateRandomPassword();
        saveToFile(website, username, password);
        System.out.println("Generated password: " + password);
        System.out.println("Password was saved successfully.");

    }

    public static void settings(Scanner scnr) {
        while (true) {
            System.out.println("Password Requirements");
            System.out.println("1. Require Uppercase Letters: " + (requireUppercase ? "ON" : "OFF"));
            System.out.println("2. Require Lowercase Letters: " + (requireLowercase ? "ON" : "OFF"));
            System.out.println("3. Require Numbers: " + (requireNumbers ? "ON" : "OFF"));
            System.out.println("4. Require Symbols: " + (requireSymbols ? "ON" : "OFF"));
            System.out.println("5. Back to Menu");
            System.out.print("Choose an option to toggle (1-4) or 5 to go back: ");
    
            if (!scnr.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number.");
                scnr.nextLine(); // Consume invalid input
                continue;
            }
    
            int choice = scnr.nextInt();
            scnr.nextLine(); // Consume the newline character
    
            switch (choice) {
                case 1:
                    requireUppercase = !requireUppercase;
                    break;
                case 2:
                    requireLowercase = !requireLowercase;
                    break;
                case 3:
                    requireNumbers = !requireNumbers;
                    break;
                case 4:
                    requireSymbols = !requireSymbols;
                    break;
                case 5:
                    System.out.println("Returning to menu...");
                    return; // Exit the loop and return to the main menu
                default:
                    System.out.println("Invalid option! Please pick from options 1-5.");
                    break;
            }
        }
    }

    private static boolean validatePassword(String password){
        if(requireUppercase && !password.matches(".*[A-Z].*")){
            return false;
        }
        if(requireLowercase && !password.matches(".*[a-z].*")){
            return false;
        }
        if(requireNumbers && !password.matches(".*[0-9].*")){
            return false;
        }
        if(requireSymbols && !password.matches(".*[!@#$%^&*()].*")){
            return false;
        }
        return true;
    }

    private static String generateRandomPassword(){
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String symbols = "!@#$%^&*()";
        StringBuilder characterPool = new StringBuilder();

        if (requireUppercase){
            characterPool.append(upperCase);
        }
        if (requireLowercase){
            characterPool.append(lowerCase);
        }
        if (requireNumbers){
            characterPool.append(numbers);
        }
        if (requireSymbols){
            characterPool.append(symbols);
        }

        if (characterPool.length() == 0){
            throw new IllegalArgumentException("At least one character type must be selected.");
        }

        Random random = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 12; i++){
            password.append(characterPool.charAt(random.nextInt(characterPool.length())));
        }
        return password.toString();
    }

    private static void saveToFile(String website, String username, String password){
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileOutputStream(passwordFile, true));
            writer.println(username + ", " + password + ", " + website);
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

