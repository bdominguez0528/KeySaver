
public class User {
    String initials;
    String password;

    /* 
    public static void main(String[] args) {
        System.out.println("User class is now executable.");
    }
    */

    //Default constructor
    // This constructor initializes the initials and password to "No Entry"
    public User() {
        this.initials = "No Entry";
        this.password = "No Entry";
    }

    //Parameterized constructor for initializing initials and password
    public User (String initials, String password) {
        this.initials =  initials;
        this.password = password;
    }
    

//================================================================================//
    public void saveToFile(String filePath) {
        java.io.FileOutputStream fileOS = null;
            try {
                fileOS = new java.io.FileOutputStream(filePath, true);
                String data = initials + "," + password + System.lineSeparator();
                fileOS.write(data.getBytes());
            }
            catch (Exception e) {
                System.out.println("Error saving user: " + e.getMessage());
            }
            finally {
                try {
                        if (fileOS != null) {
                            fileOS.close();
                        }
                    } catch (Exception e) {
                        System.out.println("Error closing file; " + e.getMessage());
                    }
                }
            }
   
//================================================================================//
        @Override
        public String toString() {
            return "Initials: " + initials;
        }
        }

