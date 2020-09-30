/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.bakeHouse.controller;

import com.jfoenix.controls.JFXButton;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import lk.ijse.bakeHouse.controller.superFormController.SuperFormController;
import lk.ijse.bakeHouse.custom.MyNotifycation;
import lk.ijse.bakeHouse.db.DBConnection;
import lk.ijse.bakeHouse.model.User;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author NIMESH
 */
public class LoginMemberFormController extends SuperFormController implements Initializable {

    @FXML
    private JFXTextField txtLastName;

    @FXML
    private Label lblUserID;

    @FXML
    private RadioButton rationBtnGuest;

    @FXML
    private RadioButton rationBtnAdmin;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXTextField txtFirstName;

    @FXML
    private JFXPasswordField txtPasswordConfirm;

    @FXML
    private JFXPasswordField txtPasswordNew;

    @FXML
    private TableView<User> tableView;

    @FXML
    private TableColumn<User, String> columnUserID;

    @FXML
    private TableColumn<User, String> columnFirstName;

    @FXML
    private TableColumn<User, String> columnLastName;

    @FXML
    private TableColumn<User, String> columnRole;

    @FXML
    private TableColumn<User, String> columnPassword;

    @FXML
    private JFXComboBox<String> comboBoxSelectField;

    @FXML
    private JFXTextField txtSearch;

    //to add Material object
    private static ObservableList<User> list;

    //select Item combobox for search
    private final ObservableList<String> selectItemField = FXCollections.observableArrayList("User Name", "First Name", "Last Name", "Role", "Password");

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {

            lblUserID.setVisible(false);

            //load table
            customizeTable();
            loadAllDataOnTable("SELECT * FROM User order by role asc;");
            tableView.setItems(list);

            //set selectItemField list
            comboBoxSelectField.setItems(selectItemField);

            //TITLE BAR TIME DATE AND USER NAME
            showTitleDateTimeAndUserInfo();

        } catch (Exception ex) {
            System.out.println("ex = " + ex);
        }

    }

    @Override
    public void searchBarWithTable(KeyEvent event) throws ClassNotFoundException, SQLException {

        String keyword = txtSearch.getText();
        String selectItem = comboBoxSelectField.getValue();

        try {
            String combo = null;
            //?switch-column name and database table name is different
            switch (selectItem) {
                case "User Name":
                    combo = "userID";
                    break;
                case "First Name":
                    combo = "fName";
                    break;
                case "Last Name":
                    combo = "lName ";
                    break;
                case "Role":
                    combo = "role";
                    break;
                case "Password":
                    combo = "uPassword";

            }

            loadAllDataOnTable("SELECT * FROM USER WHERE " + combo + " LIKE '%" + keyword + "%';");
            tableView.setItems(list);

        } catch (Exception ex) {
            TrayNotification tray = MyNotifycation.warningNotify("CHOICE THE COLUMN FIELD!");
            tray.showAndDismiss(Duration.seconds(2));
        }

    }

    @Override
    public void editButtonAction(ActionEvent event) {

        User user = tableView.getSelectionModel().getSelectedItem();

        if (user != null) {

            lblUserID.setVisible(true);

            lblUserID.setText(user.getUserID());
            txtFirstName.setText(user.getFirstName());
            txtLastName.setText(user.getLastName());//brand Name is displaying

            if (user.getRole().equalsIgnoreCase("ADMIN")) {
                rationBtnAdmin.setSelected(true);
            } else {
                rationBtnGuest.setSelected(true);
            }

            txtPasswordNew.setText(user.getUserPassword());
            txtPasswordConfirm.setText(user.getUserPassword());

            //change save button properties to update button
            btnSave.setText("UPDATE");
            btnSave.setStyle("-fx-background-color: #ff8000; -fx-text-fill: white;");
        }
    }

    @Override
    public void deleteButtonAction(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {

        User user = tableView.getSelectionModel().getSelectedItem();
        if (user != null) {

            //conformation Box
            boolean result = MyNotifycation.askWarning("Are Your Sure?");
            if (result == false) {
                return;
            }

            modifyDataWithExecuteUpdate(""
                    + "DELETE FROM USER "
                    + "WHERE userID = '" + user.getUserID() + "';");

            loadAllDataOnTable(""
                    + "SELECT * FROM User "
                    + "order by role asc;");

            tableView.setItems(list);
            MyNotifycation.sucsessfullyNotify("DELETED.");

        } else {
            MyNotifycation.infoNotify("SELECT THE ROW!");
        }
    }

    @Override
    public void loadAllDataOnTable(String sql) throws ClassNotFoundException, SQLException {
        list = FXCollections.observableArrayList();

        Connection connection = DBConnection.getInstance().getConnection();

        PreparedStatement ptm = connection.prepareStatement(sql);
        ResultSet rs = ptm.executeQuery();
        while (rs.next()) {
            String userName = rs.getString(1);
            String firstName = rs.getString(2);
            String lastName = rs.getString(3);
            String role = rs.getString(4);
            String password = rs.getString(5);
            //make user object -using database row details
            User user = new User(userName, firstName, lastName, role, password);
            //add user object to customer ObserverbleList
            list.add(user);

        }
    }

    @Override
    public void customizeTable() {

        //columns add to table
        tableView.getColumns().setAll(columnUserID, columnFirstName, columnLastName, columnRole, columnPassword);

        tableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("userID"));
        tableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableView.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("role"));
        tableView.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("userPassword"));

    }

    @Override
    public void btnSaveUpdateAction(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {

        String userName = "";
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String role = "DEFAULT";
        String password = txtPasswordNew.getText();

        if (rationBtnAdmin.isSelected()) {
            role = rationBtnAdmin.getText();
        } else if (rationBtnGuest.isSelected()) {
            role = rationBtnGuest.getText();
        }

        if (txtPasswordNew.getText().equals(txtPasswordConfirm.getText()) & !password.isEmpty()) {

            if (!firstName.isEmpty() & role != null) {

                Random ran = new Random();
                int x = ran.nextInt(10000);
                //generate id
                userName = firstName + "@" + role + "" + x;

                //conformation Box
                boolean result = MyNotifycation.askQuestion("Are Your Sure?");
                if (result == false) {
                    return;
                }

                Connection connection = DBConnection.getInstance().getConnection();
                PreparedStatement stm = connection.prepareStatement("INSERT INTO User VALUES(?,?,?,?,?)");
                stm.setString(1, userName);
                stm.setString(2, firstName);
                stm.setString(3, lastName);
                stm.setString(4, role);
                stm.setString(5, password);

                if (stm.executeUpdate() > 0) {
                    MyNotifycation.sucsessfullyNotify();

                    //clear the select field
                    txtFirstName.clear();
                    txtLastName.clear();
                    txtPasswordConfirm.clear();
                    txtPasswordNew.clear();

                    //load all data on table
                    loadAllDataOnTable("SELECT * FROM User order by role asc;");
                    tableView.setItems(list);

                } else {
                    MyNotifycation.unSucsessfullyNotify();
                }

            } else {
                MyNotifycation.warningNotify("INVALID DETAILS! ");
            }

        } else {

            MyNotifycation.warningNotify("PASSWORD DIDN'T MATCHED! ");

        }//end

    }

}
