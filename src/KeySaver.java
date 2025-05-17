import java.util.Scanner;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;

public class KeySaver {

	public static void main(String[] args) {
		Scanner scnr = new Scanner(System.in);
		int choice = 0;
		String initials = "";
		String fullName = ""; // Add this line

		File userFile = new File("users.txt");
		boolean authenticated = false;

		while (true) {
			if (!userFile.exists()) {
				System.out.println("No users found. Please add a user.");
				UserManager.addUser(scnr, new java.util.ArrayList<>());
				continue;
			} else {
				java.util.ArrayList<User> users = UserManager.loadUsers("users.txt");
        		UserManager.promptAddUser(scnr, users);

				System.out.print("Enter User initials: ");
				initials = scnr.nextLine();
				System.out.print("Enter User password: ");
				String password = scnr.nextLine();

				// Check credentials and get full name
				try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
		            String line;
		            while ((line = br.readLine()) != null) {
		                String[] parts = line.trim().split(",", 3);
		                if (parts.length >= 3 && parts[0].equals(initials) && parts[1].equals(password)) {
		                    authenticated = true;
		                    fullName = parts[2]; // Get the full name
		                    break;
		                }
		            }
		        } catch (Exception e) {
		            System.out.println("Error reading users file.");
		        }

				if (!authenticated) {
					System.out.println("Invalid credentials. Exiting.");
					scnr.close();
					return;
				}
				break; // Exit the loop if authenticated
			}
		}

		// Print welcome message with full name
		System.out.println("\nWelcome, " + fullName + "!");

		do{
			System.out.println("\n==== KeySaver Main Menu ====");
			System.out.println("1. User Manager");
			System.out.println("2. Password Manager");
			System.out.println("3. Exit Program");
			System.out.print("Select an option: ");

			if (!scnr.hasNextInt()) {
        	System.out.println("Invalid input! Please enter a number (1-3).");
        	scnr.nextLine(); // Consume the invalid input
        	continue;
    	}

			choice = scnr.nextInt();
			scnr.nextLine(); // Consume the newline character
			switch (choice) {
				case 1:
					UserManager.printMenu(initials);
					break;
				case 2:
					PasswordManager.displayMenu(initials);
					break;
				case 3:
					System.out.println("Exiting... Goodbye!");
					break;
				default:
					System.out.println("Invalid option! Please choose from options 1-3.\n");
			}
		} while (choice != 3);
		scnr.close();
	}

}
