
package lk.ijse.bakeHouse.model;

/**
 *
 * @author NIMESH
 */
public class User {
    
    private String userID;
    private String firstName ;
    private String lastName;
    private String role;
    private String userPassword;

    public User() {
    }

    public User(String userID, String firstName, String lastName, String role, String userPassword) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.userPassword = userPassword;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public String toString() {
        return "User{" + "userID=" + userID + ", firstName=" + firstName + ", lastName=" 
                + lastName + ", role=" + role + ", userPassword=" + userPassword + '}';
    }

  
}
