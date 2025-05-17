import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;


public class User {
    String initials;
    String password;
    String fullName;

    //Default constructor
    // This constructor initializes the initials and password to "No Entry"
    public User() {
        this.initials = "No Entry";
        this.password = "No Entry";
    }

    //Parameterized constructor for initializing initials and password
    public User (String initials, String password, String fullName) {
        this.initials =  initials;
        this.password = password;
        this.fullName = fullName;
    }
    

//================================================================================//
    public void saveToFile(String filePath) {
        try (PrintWriter writer = new PrintWriter(new java.io.FileOutputStream(filePath, true))) {
            writer.println(initials + "," + password + "," + fullName);
        } catch (Exception e) {
            System.out.println("Error saving user: " + e.getMessage());
        }
    }

    // This method is used to read the user data from a file
    public static void readFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            System.out.println("Error reading user: " + e.getMessage());
        }
    }   
   
   
//================================================================================//
        @Override
        public String toString() {
            return "Initials: " + initials;
        }
        }

