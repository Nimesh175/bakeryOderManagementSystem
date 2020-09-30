/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.bakeHouse.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import lk.ijse.bakeHouse.controller.superFormController.SuperFormController;
import lk.ijse.bakeHouse.custom.IDGeneratorConnection;
import lk.ijse.bakeHouse.custom.MyNotifycation;
import lk.ijse.bakeHouse.custom.NavigationOnDashboard;
import lk.ijse.bakeHouse.db.DBConnection;
import lk.ijse.bakeHouse.model.Route;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author NIMESH
 */
public class RouteFormController extends SuperFormController implements Initializable {

    @FXML
    private Label lblRouteID;

    @FXML
    private JFXButton btnSaveAndUpdate;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextArea txtAreaDescription;

    @FXML
    private TableView<Route> tableViewRoute;

    @FXML
    private TableColumn<Route, String> columnDescription;

    @FXML
    private TableColumn<Route, String> columnName;

    @FXML
    private TableColumn<Route, String> columID;

    @FXML
    private JFXComboBox<String> comboBoxSelectField;

    @FXML
    private JFXTextField txtSearch;

    //to add Route object
    private static ObservableList<Route> data;

    //route name collection
    ObservableList<String> routeName;

    //select Item combobox for search
    private final ObservableList<String> selectItemField = FXCollections.observableArrayList("Route ID", "Route Name");

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            //GENERATE ID
            lblRouteID.setText(IDGeneratorConnection.getNewID("ROUTE", "route_ID", "R"));

            //load table 
            customizeTable();
            loadAllDataOnTable("SELECT * FROM ROUTE;");
            tableViewRoute.setItems(data);

            //setup comboBox for search
            comboBoxSelectField.setItems(selectItemField);

            //TITLE BAR TIME DATE AND USER NAME
            showTitleDateTimeAndUserInfo();

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteButtonAction(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {

        Route pro = tableViewRoute.getSelectionModel().getSelectedItem();
        if (pro != null) {

            //conformation Box
            boolean result = MyNotifycation.askWarning("Are Your sure?");
            if (result == false) {
                return;
            }

            modifyDataWithExecuteUpdate(""
                    + "DELETE FROM ROUTE"
                    + " WHERE route_ID = '" + pro.getRouteID() + "';");

            loadAllDataOnTable("SELECT * FROM ROUTE; ");
            tableViewRoute.setItems(data);

            //GENERATE ID
            lblRouteID.setText(IDGeneratorConnection.getNewID("ROUTE", "route_ID", "R"));

        } else {
            MyNotifycation.infoNotify("SELECT THE ROW!");
        }
    }

    @Override
    public void customizeTable() {

        //columns add to table
        tableViewRoute.getColumns().setAll(columID, columnName, columnDescription);

        tableViewRoute.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("routeID"));
        tableViewRoute.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tableViewRoute.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("description"));

    }

    @Override
    public void searchBarWithTable(KeyEvent event) throws ClassNotFoundException, SQLException {
        try {

            String keyword = txtSearch.getText();
            String selectItem = comboBoxSelectField.getValue();

            String combo = null;
            //?switch-column name and database table name is different
            switch (selectItem) {
                case "Route ID":
                    combo = "route_ID";
                    break;
                case "Route Name":
                    combo = "name";

            }

            loadAllDataOnTable("SELECT * FROM ROUTE WHERE " + combo + " LIKE '%" + keyword + "%';");
            tableViewRoute.setItems(data);

        } catch (Exception ex) {
            TrayNotification tray = MyNotifycation.warningNotify("CHOICE THE COLUMN FIELD!");
            tray.showAndDismiss(Duration.seconds(2));
        }

    }

    @Override
    public void editButtonAction(ActionEvent event) {
        Route pro = tableViewRoute.getSelectionModel().getSelectedItem();
        if (pro != null) {
            //
            lblRouteID.setText(pro.getRouteID());
            txtName.setText(pro.getName());
            txtAreaDescription.setText(pro.getDescription());

            //change save button properties to update button
            btnSaveAndUpdate.setText("UPDATE");
            btnSaveAndUpdate.setStyle("-fx-background-color: #ff8000; -fx-text-fill: white;");
        }
    }

    @Override
    public void loadAllDataOnTable(String sql) throws ClassNotFoundException, SQLException {
        //observerble list
        data = FXCollections.observableArrayList();
        routeName = FXCollections.observableArrayList();

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ptm = connection.prepareStatement(sql);
        ResultSet rs = ptm.executeQuery();

        while (rs.next()) {
            String id = rs.getString(1);
            String name = rs.getString(2);
            String description = rs.getString(3);

            //make route object -using database row details
            Route route = new Route(id, name, description);

            //add route object to customer ObserverbleList
            data.add(route);
            //collect route name
            routeName.add(name);

        }//loop end
    }

    public ObservableList<String> getAllRouteObject() throws ClassNotFoundException, SQLException {
        loadAllDataOnTable("SELECT * FROM ROUTE; ");
        return routeName;
    }

    @Override
    public void btnSaveUpdateAction(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
        String id = lblRouteID.getText();
        String name = txtName.getText();
        String description = txtAreaDescription.getText();

        if (!name.equalsIgnoreCase("")) {

            if (btnSaveAndUpdate.getText().equalsIgnoreCase("SAVE")) {

                //  Route route = new Route(id,name,description);
                Connection connection = DBConnection.getInstance().getConnection();
                PreparedStatement stm = connection.prepareStatement(
                        "INSERT INTO ROUTE VALUES(?,?,?);");

                stm.setString(1, id);
                stm.setString(2, name);
                stm.setString(3, description);

                //conformation Box
                boolean result = MyNotifycation.askQuestion("Are Your sure?");
                if (result == false) {
                    return;
                }

                if (stm.executeUpdate() > 0) {
                    // set notification
                    TrayNotification tray = MyNotifycation.sucsessfullyNotify("ROUTE ADDED SUCSESSFULLY.");
                    tray.showAndDismiss(Duration.seconds(2));

                    // Set next customer number using IDGeneratorConnection.java
                    lblRouteID.setText(IDGeneratorConnection.getNewID("ROUTE", "route_ID", "R"));

                    //clear the form
                    txtName.clear();
                    txtAreaDescription.clear();

                    //load Table data after save
                    loadAllDataOnTable("SELECT * FROM ROUTE;");
                    tableViewRoute.setItems(data);

                } else {
                    TrayNotification tray = MyNotifycation.unSucsessfullyNotify();
                    tray.showAndDismiss(Duration.seconds(2));
                }
            } else {
                //button update mode
                try {

                    //conformation Box
                    boolean result = MyNotifycation.askQuestion("Are Your sure?");
                    if (result == false) {
                        return;
                    }

                    String sql = "UPDATE ROUTE "
                            + "SET name='" + name + "'"
                            + ",description='" + description + "'"
                            + "WHERE route_ID='" + id + "' ;";

                    int x = modifyDataWithExecuteUpdate(sql);

                    if (x == 0) {
                        return;
                    }

                    loadAllDataOnTable("SELECT * FROM ROUTE; ");
                    tableViewRoute.setItems(data);

                    //NOTIFICATION
                    TrayNotification tray = MyNotifycation.sucsessfullyNotify("UPDATE SUCSESSFULLY.");
                    tray.showAndDismiss(Duration.seconds(2));

                    //BUTTON RESET UPDATE TO SAVE & ID GENERATE
                    btnSaveAndUpdate.setText("SAVE");
                    btnSaveAndUpdate.setStyle("-fx-background-color:   #019638; -fx-text-fill: white;");
                    lblRouteID.setText(IDGeneratorConnection.getNewID("ROUTE", "route_ID", "R"));

                    //CLEAR THE TEXTFIELD
                    txtName.clear();
                    txtAreaDescription.clear();

                } catch (Exception ex) {
                    TrayNotification tray = MyNotifycation.warningNotify("INVALID INFORMATION!");
                    tray.showAndDismiss(Duration.seconds(2));

                    //CLEAR THE TEXTFIELD
                    txtName.clear();
                    txtAreaDescription.clear();
                }

            }//CLOSE ELSE

        } else {
            TrayNotification tray = MyNotifycation.unSucsessfullyNotify();
            tray.showAndDismiss(Duration.seconds(2));

        }//close
    }

}
