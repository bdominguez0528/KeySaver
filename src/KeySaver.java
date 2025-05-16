import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
public class KeySaver {

	public static void main(String[] args) {
		Scanner scnr = new Scanner(System.in);
		int choice;

		File userFile = new File("users.txt");
		boolean authenticated = false;
		
		if (!userFile.exists()){
			System.out.println("No users found. Please add a user.");
			UserManager.printMenu();
		} else{
            System.out.print("Enter your initials: ");
            String initials = scnr.nextLine();
            System.out.print("Enter your password: ");
            String password = scnr.nextLine();

            // Check credentials
            try (Scanner fileScanner = new Scanner(userFile)) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine().trim();
                    if (line.equals(initials + "," + password)) {
                        authenticated = true;
                        break;
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("Error reading users file.");
            }

            if (!authenticated) {
                System.out.println("Invalid credentials. Exiting.");
                scnr.close();
                return;
            }		
		}

		do{
			System.out.println("\n==== KeySaver Main Menu ====");
			System.out.println("1. Add User");
			System.out.println("2. Password Manager");
			System.out.println("3. Exit");
			System.out.print("Select an option: ");

			choice = scnr.nextInt();
			scnr.nextLine(); // Consume the newline character
			switch (choice) {
				case 1:
					UserManager.printMenu();
					break;
				case 2:
					PasswordManager.displayMenu();
					break;
				case 3:
					System.out.println("Exiting... Goodbye!");
					break;
				default:
					System.out.println("Invalid option! Please choose from options 1-3.");
			}
		} while (choice != 3);
		scnr.close();
	}

}
