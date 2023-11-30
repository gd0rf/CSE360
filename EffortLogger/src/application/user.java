package application;


/*
 * 
 * User defines the information stored within the user object and handles the setting and retrieving of the data.
 * 
 */
public class user {
    private String username;
    private String password;

    public user(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and setters (optional)

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}