/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.bakeHouse.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import lk.ijse.bakeHouse.controller.superFormController.SuperFormController;
import lk.ijse.bakeHouse.db.DBConnection;
import lk.ijse.bakeHouse.custom.MyNotifycation;
import lk.ijse.bakeHouse.custom.IDGeneratorConnection;
import lk.ijse.bakeHouse.model.Customer;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author NIMESH
 */
public class CustomerFormController extends SuperFormController implements Initializable {

    @FXML
    private JFXTextField txtFirstName;

    @FXML
    private JFXTextField txtLastName;

    @FXML
    private JFXTextField txtNIC;

    @FXML
    private Label lblCustomerID;

    @FXML
    private RadioButton rationBtnStandard;

    @FXML
    private RadioButton rationBtnTemp;

    @FXML
    private RadioButton rationBtnMale;

    @FXML
    private RadioButton rationBtnFemale;

    @FXML
    private RadioButton rationBtnOther;

    @FXML
    private ComboBox<String> comboBoxRoute;

    @FXML
    private JFXTextField txtTelephone;

    @FXML
    private JFXButton btnSave;

    @FXML
    private TableView<Customer> tableViewCustomer;

    @FXML
    private TableColumn<Customer, String> columnParticipation;

    @FXML
    private TableColumn<Customer, String> columnID;

    @FXML
    private TableColumn<Customer, String> columnFName;

    @FXML
    private TableColumn<Customer, String> columnLName;

    @FXML
    private TableColumn<Customer, String> columnGender;

    @FXML
    private TableColumn<Customer, String> columnNIC;

    @FXML
    private TableColumn<Customer, Integer> columnTelephone;

    @FXML
    private TableColumn<Customer, String> columnRoute;

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private JFXComboBox<String> comboBoxSelectField;

    //for combobox comboBoxSelectField
    private final ObservableList<String> list = FXCollections.observableArrayList(
            "Participation", "Customer ID", "First Name", "Last Name", "NIC", "Telephone", "Route", "Gender");

    //to add customer object
    public static ObservableList<Customer> data;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // Set next customer number using IDGeneratorConnection.java
            lblCustomerID.setText(IDGeneratorConnection.getNewID("customer", "cusID", "C"));

            //load table 
            loadAllDataOnTable(getSelectAllQuery());
            customizeTable();
            tableViewCustomer.setItems(data);

            // Default Selected RationButton
            rationBtnTemp.setSelected(true);
            rationBtnMale.setSelected(true);

            //set combobox field for search
            comboBoxSelectField.setItems(list);

            //ADD ROUTE TO COMBO
            comboBoxRoute.setItems(new RouteFormController().getAllRouteObject());

            //TITLE BAR TIME DATE AND USER NAME
            showTitleDateTimeAndUserInfo();

        } catch (Exception ex) {
        }
    }

    private String getSelectAllQuery() {

        return "SELECT C.participation,C.cusID,C.fName,C.lName,C.NIC,C.gender,C.telephone ,R.name "
                + "FROM customer C "
                + "LEFT JOIN ROUTE R "
                + "ON C.Route=R.route_ID ";
    }

    @Override
    public void searchBarWithTable(KeyEvent event) throws ClassNotFoundException, SQLException {

        try {
            String keyword = txtSearch.getText();
            String selectItem = comboBoxSelectField.getValue();

            String combo = null;
            //?switch-column name and database table name is different
            switch (selectItem) {
                case "Participation":
                    combo = "C.participation";
                    break;
                case "Customer ID":
                    combo = "C.cusID";
                    break;
                case "First Name":
                    combo = "C.fName";
                    break;
                case "Last Name":
                    combo = "C.lName";
                    break;
                case "NIC":
                    combo = "C.NIC";
                    break;
                case "Telephone":
                    combo = "C.telephone";
                    break;
                case "Route":
                    combo = "R.name";
                    break;
                case "Gender":
                    combo = "C.gender";
                    break;
            }

            // loadAllDataOnTable("SELECT * FROM CUSTOMER WHERE " + combo + " LIKE '%" + keyword + "%';");
            loadAllDataOnTable(getSelectAllQuery() + " WHERE " + combo + " LIKE '%" + keyword + "%';");
            tableViewCustomer.setItems(data);

        } catch (Exception ex) {
            TrayNotification tray = MyNotifycation.warningNotify("CHOICE THE COLUMN FIELD!");
            tray.showAndDismiss(Duration.seconds(2));
        }

    }

    @Override
    public void editButtonAction(ActionEvent event) {
        Customer cus = tableViewCustomer.getSelectionModel().getSelectedItem();
        if (cus != null) {
            //
            lblCustomerID.setText(cus.getCusID());
            txtFirstName.setText(cus.getFName());
            txtLastName.setText(cus.getLName());
            txtNIC.setText(cus.getNIC());
            txtTelephone.setText(Integer.toString(cus.getTelephone()));
            comboBoxRoute.setValue(cus.getRoute());

            switch (cus.getGender()) {
                case "Male":
                    rationBtnMale.setSelected(true);
                    break;
                case "Female":
                    rationBtnFemale.setSelected(true);
                    break;
                case "Other":
                    rationBtnOther.setSelected(true);

            }

            switch (cus.getParticipation()) {
                case "Standerd":
                    rationBtnStandard.setSelected(true);
                    break;
                case "Temporary":
                    rationBtnTemp.setSelected(true);

            }

            //change save button properties to update button
            btnSave.setText("UPDATE");
            btnSave.setStyle("-fx-background-color: #ff8000; -fx-text-fill: white;");
        }
    }

    @Override
    public void customizeTable() {
        //set column width
        columnParticipation.setMinWidth(90);
        columnID.setMinWidth(110);
        columnFName.setMinWidth(110);
        columnLName.setMinWidth(110);
        columnGender.setMinWidth(90);
        columnNIC.setMinWidth(90);
        columnTelephone.setMinWidth(100);
        columnRoute.setMinWidth(90);

        //columns add to table
        tableViewCustomer.getColumns().setAll(columnParticipation, columnID, columnFName,
                columnLName, columnGender, columnNIC, columnTelephone, columnRoute);

        tableViewCustomer.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("participation"));
        tableViewCustomer.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("cusID"));
        tableViewCustomer.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("fName"));
        tableViewCustomer.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("lName"));
        tableViewCustomer.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("gender"));//gender
        tableViewCustomer.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("NIC"));
        tableViewCustomer.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("telephone"));
        tableViewCustomer.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("Route"));

    }

    @Override
    public void deleteButtonAction(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
        Customer cus = tableViewCustomer.getSelectionModel().getSelectedItem();

        if (cus != null) {
            //conformation Box
            boolean result = MyNotifycation.askWarning("Are Your sure?");
            if (result == false) {
                return;
            }

            modifyDataWithExecuteUpdate(""
                    + "DELETE FROM CUSTOMER "
                    + "WHERE cusID = '" + cus.getCusID() + "';");

            loadAllDataOnTable(""
                    + "SELECT c.participation ,c.cusID ,c.fName"
                    + ",c.lName,c.gender,c.NIC,c.telephone"
                    + ", r.name "
                    + "FROM customer c "
                    + "LEFT JOIN route r "
                    + "ON c.Route=r.route_ID ;");

            tableViewCustomer.setItems(data);

            // Set next customer number using IDGeneratorConnection.java
            lblCustomerID.setText(IDGeneratorConnection.getNewID("customer", "cusID", "C"));

        } else {
            MyNotifycation.infoNotify("SELECT THE ROW!");
        }

    }

    @Override
    public void loadAllDataOnTable(String sql) throws ClassNotFoundException, SQLException {

        data = FXCollections.observableArrayList();

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ptm = connection.prepareStatement(sql);
        ResultSet rs = ptm.executeQuery();

        while (rs.next()) {
            String participation = rs.getString(1);
            String id = rs.getString(2);
            String fName = rs.getString(3);
            String lName = rs.getString(4);
            String gender = rs.getString(6);
            String nic = rs.getString(5);
            int telephone = rs.getInt(7);
            String route = rs.getString(8);

            //make customer object -using database row details
            Customer customer = new Customer(participation, id, fName, lName, gender, nic, telephone, route);

            //add customer object to customer ObserverbleList
            data.add(customer);

        }

    }

    @Override
    public void btnSaveUpdateAction(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
        Connection connection = DBConnection.getInstance().getConnection();

        String id = lblCustomerID.getText();
        String fName = txtFirstName.getText();
        String lName = txtLastName.getText();

        String participation = "DEFAULT";
        if (rationBtnStandard.isSelected()) {
            participation = rationBtnStandard.getText();
        } else if (rationBtnTemp.isSelected()) {
            participation = rationBtnTemp.getText();
        }

        String Gender = "DEFAULT";
        if (rationBtnMale.isSelected()) {
            Gender = rationBtnMale.getText();
        } else if (rationBtnFemale.isSelected()) {
            Gender = rationBtnFemale.getText();
        } else if (rationBtnOther.isSelected()) {
            Gender = rationBtnOther.getText();
        }

        String NIC = txtNIC.getText();
        // button save mode
        if (btnSave.getText().equalsIgnoreCase("SAVE")) {
            try {
                int telephone = Integer.parseInt(txtTelephone.getText());
                //add route no
                String route = comboBoxRoute.getValue();
                PreparedStatement pstm = connection.prepareStatement("Select route_ID from route where name='" + route + "'");
                ResultSet set = pstm.executeQuery();
                set.next();
                String rut = set.getString(1);

                if (!fName.equalsIgnoreCase(null)) {

                    //  Customer customer = new Customer(participation, id, fName, lName, NIC, Gender, telephone, route);
                    PreparedStatement stm = connection.prepareStatement(
                            "insert into customer values(?,?,?,?,?,?,?,?);");
                    //
                    stm.setString(1, participation);
                    stm.setString(2, id);
                    stm.setString(3, fName);
                    stm.setString(4, lName);
                    stm.setString(5, NIC);
                    stm.setString(6, Gender);
                    stm.setString(8, rut);
                    stm.setInt(7, telephone);

                    //conformation Box
                    boolean result = MyNotifycation.askQuestion("Are Your sure?");
                    if (result == false) {
                        return;
                    }

                    if (stm.executeUpdate() > 0) {
                        // set notification
                        TrayNotification tray = MyNotifycation.sucsessfullyNotify("CUSTOMER ADDED SUCSESSFULLY.");
                        tray.showAndDismiss(Duration.seconds(2));

                        // Set next customer number using IDGeneratorConnection.java
                        lblCustomerID.setText(IDGeneratorConnection.getNewID("customer", "cusID", "C"));
                        //clear the form
                        txtFirstName.clear();
                        txtLastName.clear();
                        txtNIC.clear();
                        txtTelephone.clear();

                        //load Table data after save
                        loadAllDataOnTable(getSelectAllQuery());
                        tableViewCustomer.setItems(FXCollections.observableArrayList(data));

                    } else {
                        TrayNotification tray = MyNotifycation.unSucsessfullyNotify();
                        tray.showAndDismiss(Duration.seconds(2));
                    }//close

                } else {
                    TrayNotification tray = MyNotifycation.warningNotify("TASK INCOMPLETED!");
                    tray.showAndDismiss(Duration.seconds(2));
                }//close

            } catch (Exception ex) {
                TrayNotification tray = MyNotifycation.warningNotify("INVALID INFORMATION!");
                tray.showAndDismiss(Duration.seconds(2));
            }

        } else {
            // button Updte mode
            try {

                int telephone = Integer.parseInt(txtTelephone.getText());
                String route = comboBoxRoute.getValue();

                //conformation Box
                boolean result = MyNotifycation.askQuestion("Are Your sure?");
                if (result == false) {
                    return;
                }

                String sqlQuery = "select route_ID FROM ROUTE WHERE name='" + route + "'; ";
                PreparedStatement ptm = connection.prepareStatement(sqlQuery);
                ResultSet rs = ptm.executeQuery();
                rs.next();

                String sql = "UPDATE CUSTOMER "
                        + "SET participation='" + participation + "',fName='" + fName + "'"
                        + ",lName='" + lName + "',NIC='" + NIC + "',gender='" + Gender + "',telephone='"
                        + telephone + "',Route='" + rs.getString(1) + "' "
                        + "WHERE cusID='" + id + "' ;";

                modifyDataWithExecuteUpdate(sql);
                loadAllDataOnTable(getSelectAllQuery());
                tableViewCustomer.setItems(data);

                //NOTIFICATION
                TrayNotification tray = MyNotifycation.sucsessfullyNotify("UPDATE SUCSESSFULLY.");
                tray.showAndDismiss(Duration.seconds(2));

                //BUTTON RESET UPDATE TO SAVE & ID GENERATE
                btnSave.setText("SAVE");
                btnSave.setStyle("-fx-background-color:  #019638; -fx-text-fill: white;");
                lblCustomerID.setText(IDGeneratorConnection.getNewID("customer", "cusID", "C"));

                //CLEAR THE TEXTFIELD
                txtFirstName.clear();
                txtLastName.clear();
                txtNIC.clear();
                txtTelephone.clear();

            } catch (Exception ex) {
                TrayNotification tray = MyNotifycation.warningNotify("INVALID INFORMATION!");
                tray.showAndDismiss(Duration.seconds(2));
            }

        }
    }
}
