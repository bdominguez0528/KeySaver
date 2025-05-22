/*
Author: Bryant Dominguez(Leader)
		Bryan Edgell
		Enrique Camarena
Class: CS 121 Program and Algorithms in Java.
File Name: KeySaver.java
Date: 05/09/25 - 05/22/25
Description: This Program is a Password Manager that allows a user to store and list passwords.
*/

import java.util.Scanner;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class KeySaver {

// Main entry point for the KeySaver application. Handles directory setup, user authentication, and main menu navigation.
	public static void main(String[] args) {
		new File("settings").mkdirs();
	    new File("passwords").mkdirs();
		
		Scanner scnr = new Scanner(System.in);
		int choice = 0;
		String initials = "";
		String fullName = "";

		File userFile = new File("users.txt");
		boolean authenticated = false;

		while (true) {
			if (!userFile.exists()) {
				System.out.println("No users found. Please add a user.");
				UserManager.addUser(scnr, new ArrayList<>());
				continue;
			} else {
				ArrayList<User> users = UserManager.loadUsers("users.txt");
        		UserManager.promptAddUser(scnr, users);

				System.out.print("Enter User initials: ");
				initials = scnr.nextLine();
				System.out.print("Enter User password: ");
				String password = scnr.nextLine();

				try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
		            String line;
		            while ((line = br.readLine()) != null) {
					String[] parts = line.split(",", 3);
					if (parts.length >= 3 && parts[0].equals(initials)) {
						String decryptedStoredPassword = SimpleEncrypt.decrypt(parts[1]);
						if (decryptedStoredPassword.equals(password)) {
							authenticated = true;
							fullName = parts[2];
							break;
						}
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
				break;
			}
		}

		System.out.println("\nWelcome, " + fullName + "!");

		do{
			System.out.println("\n==== KeySaver Main Menu ====");
			System.out.println("1. User Manager");
			System.out.println("2. Password Manager");
			System.out.println("3. Exit Program");
			System.out.print("Select an option: ");

			if (!scnr.hasNextInt()) {
        	System.out.println("Invalid input! Please enter a number (1-3).");
        	scnr.nextLine();
        	continue;
    		}

			choice = scnr.nextInt();
			scnr.nextLine();
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
