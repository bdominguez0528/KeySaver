import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;



public class UserManager {
    public static ArrayList<User> loadUsers(String filename){
        ArrayList<User> users = new ArrayList<>();
        try (Scanner fileScanner = new Scanner(new File(filename))){
            while (fileScanner.hasNextLine()){
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 2){
                    users.add(new User(parts[0], parts[1]));
                }
            }
        }catch (Exception e){
            System.out.println("Error loading users: File may not exist. " + e.getMessage());
        }
        return users;
    }

    public static void printMenu(){
         Scanner scnr = new Scanner(System.in);
         ArrayList<User> users = loadUsers("users.txt");
         int userChoice;

        do{
            System.out.println("Select one of the following options:");
            System.out.println("1. Add User");
            System.out.println("2. List Users");
            System.out.println("3. Exit");
            userChoice = scnr.nextInt();
            scnr.nextLine(); // Consume the newline character
            switch (userChoice){
                case 1:
                    addUser(scnr, users);
                    break;
                case 2:
                    listUsers(users);
                    break;
                case 3:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option! Please choose from options 1-3.");
            }
        }while(userChoice != 3);
    }

    public static void addUser(Scanner scnr, ArrayList<User> users){
        System.out.println("Enter your initials: ");
        String initials = scnr.nextLine();

        //FIXME: Add validation for initials
        //FIXME: Add Prompt for first name.

        System.out.println ("Enter your password: ");
        String password = scnr.nextLine();

        User newUser = new User(initials, password);
        users.add(newUser);
        newUser.saveToFile("users.txt");
        System.out.println("User added successfully.");
    }

    public static void listUsers(ArrayList<User> users){
        if(users.isEmpty()){
            System.out.println("No users available.");
            return;
        }
        System.out.println("\n--------User List--------");
        for (int i = 0; i < users.size(); i++){
            System.out.println((i + 1) + ". " + users.get(i));
        }
    }
}
