package lk.ijse.bakeHouse.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lk.ijse.bakeHouse.custom.GoToAnotherWindow;
import lk.ijse.bakeHouse.db.DBConnection;
import lk.ijse.bakeHouse.model.User;
import lk.ijse.bakeHouse.custom.MyNotifycation;

/**
 * FXML Controller class
 *
 * @author NIMESH
 */
public class LoginAndSignupFormController implements Initializable {

    @FXML
    private JFXTextField txtUName;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private Pane paneNotify;

    @FXML
    private Text txtUserNotify;

    @FXML
    private Text txtPassNotify;

    @FXML
    private AnchorPane logInAnchor;

    @FXML
    private AnchorPane signUpAnchor;

    @FXML
    private JFXTextField fNameTxt;

    @FXML
    private JFXTextField lNameTxt;

    @FXML
    private JFXComboBox<String> roleCombo;
    ObservableList<String> list = FXCollections.observableArrayList("Admin", "Guest");

    @FXML
    private JFXPasswordField newPassTxT;

    @FXML
    private JFXPasswordField confirmPassTxt;

    @FXML
    private JFXPasswordField adminPassTxt;

    private static User userInfo;

    //get the last login person
    public static User getUserLoginInfo() {

        return userInfo;
    }

    @FXML
    void closeApp(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    void loginMouseClicked(MouseEvent event) {
        FadeTransition tempTransition = new FadeTransition(Duration.millis(2500), logInAnchor);
        tempTransition.setFromValue(0.0);
        tempTransition.setToValue(1.0);
        tempTransition.play();

        logInAnchor.setVisible(true);
        signUpAnchor.setVisible(false);
    }

    @FXML
    void signupMousceClicked(MouseEvent event) {

        FadeTransition tempTransition = new FadeTransition(Duration.millis(2500), signUpAnchor);
        tempTransition.setFromValue(0.0);
        tempTransition.setToValue(1.0);
        tempTransition.play();

        logInAnchor.setVisible(false);
        signUpAnchor.setVisible(true);
    }

    @FXML
    void welcomeToDashboard(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
        
        System.gc();
        
        String user = txtUName.getText();
        String password = txtPassword.getText();
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stm = connection.prepareStatement("select * from user;");
        ResultSet rs = stm.executeQuery();
        int count = 0;
        while (rs.next()) {
            if (user.equals(rs.getString(1)) & password.equals(rs.getString(5))) {

                GoToAnotherWindow.getAnotherWindow().makeWindow(event, "/lk/ijse/bakeHouse/view/DashboardForm.fxml");
                MyNotifycation.sucsessfullyNotify();

                //add login info
                userInfo = new User(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5)
                );

                count++;
            }

        }

        if (count == 0) {
            MyNotifycation.unSucsessfullyNotify("LOGIN FAILED!");
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        roleCombo.setItems(list);
    }

    public void saveUserDetail() throws ClassNotFoundException, SQLException {
        
        System.gc();
        
        String fName = fNameTxt.getText();
        String lName = lNameTxt.getText();
        String role = roleCombo.getValue();
        String newPassword = newPassTxT.getText();
        String confirmPassword = confirmPassTxt.getText();
        String adminPassword = adminPassTxt.getText();

        //user ID generator
        if (!fName.isEmpty() & role != null & adminPassword.equalsIgnoreCase("ADMIN")) {
            Random ran = new Random();
            int x = ran.nextInt(10000);
            String userID = fName + "@" + role + "" + x;

            if (newPassword.equals(confirmPassword)) {

                Connection connection = DBConnection.getInstance().getConnection();
                PreparedStatement stm = connection.prepareStatement("INSERT INTO User VALUES(?,?,?,?,?)");
                stm.setString(1, userID);
                stm.setString(2, fName);
                stm.setString(3, lName);
                stm.setString(4, role);
                stm.setString(5, newPassword);

                if (stm.executeUpdate() > 0) {
                    MyNotifycation.sucsessfullyNotify();
                    paneNotify.setVisible(true);

                    FadeTransition tempTransition = new FadeTransition(Duration.millis(2000), paneNotify);
                    tempTransition.setFromValue(0.0);
                    tempTransition.setToValue(1.0);
                    tempTransition.play();

                    txtUserNotify.setText(userID);
                    txtPassNotify.setText(newPassword);
                } else {
                    MyNotifycation.unSucsessfullyNotify();
                }
            } else {
                MyNotifycation.warningNotify("PASSWORD DIDN'T MATCHED! ");
            }
        } else {
            MyNotifycation.warningNotify();
        }

    }//close method

    @FXML
    void reCreateAnotherAccountBack(MouseEvent event) {
        paneNotify.setVisible(false);

        signupMousceClicked(event);

        fNameTxt.clear();
        lNameTxt.clear();
        roleCombo.setValue(null);
        newPassTxT.clear();
        confirmPassTxt.clear();
        adminPassTxt.clear();
    }

}
