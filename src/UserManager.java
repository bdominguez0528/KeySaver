import java.util.Scanner;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

// Handles loading, saving, and managing user accounts for the application.
public class UserManager {

    // Loads all users from the specified file and returns them as an ArrayList.
    public static ArrayList<User> loadUsers(String filename){
        ArrayList<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    users.add(new User(parts[0], parts[1], parts[2]));
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading users: File may not exist. " + e.getMessage());
        }
        return users;
    }

    // Displays the user manager menu and handles user choices for managing users.
    public static void printMenu(String loggedInInitials){
        Scanner scnr = new Scanner(System.in);
        ArrayList<User> users = loadUsers("users.txt");
        int userChoice = 0;

        do{
            System.out.println("\n==== User Manager Menu ====");
            System.out.println("====Select one of the following options:=====");
            System.out.println("1. List Users");
            System.out.println("2. Modify Current User");
            System.out.println("3. Delete Current User");
            System.out.println("4. Return to Main Menu");

            if (!scnr.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number (1-5).");
                scnr.nextLine();
                continue;
            }
            userChoice = scnr.nextInt();
            scnr.nextLine(); // Consume the newline character
            switch (userChoice){
                case 1:
                    listUsers(users);
                    break;
                case 2:
                    modifyCurrentUser(scnr, users, loggedInInitials);
                    break;
                case 3:
                    deleteCurrentUser(scnr, users, loggedInInitials);
                case 4:
                    System.out.println("Returning...");
                    break;
                default:
                    System.out.println("Invalid option! Please choose from options 1-4.");
            }
        }while(userChoice != 4);
        System.out.println();
    }

    // Prompts the user to add a new user if they choose to do so.
    public static void promptAddUser(Scanner scnr, ArrayList<User> users) {
        System.out.print("Would you like to add a new user? (yes/no): ");
        String response = scnr.nextLine().trim();
        if (response.equalsIgnoreCase("yes" ) || response.equalsIgnoreCase("y")) {
            addUser(scnr, users);
        }
    }

    // Adds a new user to the system after validating input and ensuring unique initials.
    public static void addUser(Scanner scnr, ArrayList<User> users){
        String initials;
        while (true) {
            System.out.println("Enter your initials (letters only): ");
            initials = scnr.nextLine().trim();

            // Check not empty
            if (initials.isEmpty()) {
                System.out.println("Initials cannot be empty. Please try again.");
                continue;
            }
            // Check for invalid characters
            if (initials.contains(",") || initials.contains(" ")) {
                System.out.println("Initials cannot contain commas or spaces. Please try again.");
                continue;
            }
            // Check for alphabetical characters only (for the base initials)
            if (!initials.matches("[a-zA-Z]{2}")) {
                System.out.println("Initials must contain only TWO letters (A-Z or a-z). Please try again.");
                continue;
            }

            String candidate = initials;
            int suffix = 1;
            boolean alreadyExists;
            do {
                alreadyExists = false;
                for (User user : users) {
                    if (user.initials.equalsIgnoreCase(candidate)) {
                        alreadyExists = true;
                        candidate = initials + suffix;
                        suffix++;
                        break;
                    }
                }
            } while (alreadyExists);
            if (!candidate.equals(initials)) {
                System.out.println("Initials already taken. Your new initials are: " + candidate);
            }
            initials = candidate;
            break; // Passed all checks and is unique
        }

        System.out.println("Enter your full name: ");
        String fullName= scnr.nextLine();

        System.out.println ("Enter your password: ");
        String password = scnr.nextLine();
        String encryptedPassword = SimpleEncrypt.encrypt(password);

        User newUser = new User(initials, encryptedPassword, fullName);
        users.add(newUser);
        newUser.saveToFile("users.txt");
        System.out.println("User added successfully.\n");
    }

    // Lists all users currently loaded in the system.
    public static void listUsers(ArrayList<User> users){
        if(users.isEmpty()){
            System.out.println("No users available.");
            return;
        }
        System.out.println("\n--------User List--------");
        for (int i = 0; i < users.size(); i++){
            System.out.println((i + 1) + ". " + users.get(i));
        }
        System.out.println();
    }

    // Allows the currently logged-in user to change their password.
    public static void modifyCurrentUser(Scanner scnr, ArrayList<User> users, String loggedInInitials) {
        boolean userFound = false;
        for (User user : users) {
            if (user.initials.equals(loggedInInitials)) {
                userFound = true;
                System.out.print("Enter new password (or press Enter to keep current): ");
                String newPassword = scnr.nextLine();
                if (!newPassword.isEmpty()) {
                    // Encrypt the new password before saving
                    user.password = SimpleEncrypt.encrypt(newPassword);
                }
                // Save all users back to file
                try (PrintWriter wr = new PrintWriter("users.txt")) {
                    for (User u : users) {
                        wr.println(u.initials + "," + u.password + "," + u.fullName);
                    }
                } catch (Exception e) {
                    System.out.println("Error saving users: " + e.getMessage());
                }
                System.out.println("User modified successfully.\n");
                break;
            }
        }
        if (!userFound) {
            System.out.println("Your user was not found.\n");
        }
    }

    // Deletes the currently logged-in user from the system and removes their files.
    public static void deleteCurrentUser(Scanner scnr, ArrayList<User> users, String loggedInInitials) {
        boolean userFound = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).initials.equals(loggedInInitials)) {
                userFound = true;
                System.out.print("Are you sure you want to delete your user? (yes/no): ");
                String confirm = scnr.nextLine();
                if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")) {
                    users.remove(i);
                    File pwFile = new File("passwords/" + loggedInInitials + ".txt");
                    if (pwFile.exists()) pwFile.delete();
                    File settingsFile = new File("settings/" + loggedInInitials + ".txt");
                    if (settingsFile.exists()) settingsFile.delete();
                    try (PrintWriter writer = new PrintWriter("users.txt")) {
                        for (User u : users) {
                            writer.println(u.initials + "," + u.password + "," + u.fullName);
                        }
                    } catch (Exception e) {
                        System.out.println("Error saving users: " + e.getMessage());
                    }
                    System.out.println("User was deleted successfully.\n");
                    System.out.println("You have been logged out.");
                    System.exit(0);
                } else {
                    System.out.println("User deletion cancelled.\n");
                }
                break;
            }
        }
        if (!userFound) {
            System.out.println("Your user was not found.\n");
        }
    }
}
