/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.bakeHouse.controller;

import com.jfoenix.controls.JFXButton;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import lk.ijse.bakeHouse.controller.superFormController.SuperFormController;
import lk.ijse.bakeHouse.custom.IDGeneratorConnection;
import lk.ijse.bakeHouse.custom.MyNotifycation;
import lk.ijse.bakeHouse.custom.NavigationOnDashboard;
import lk.ijse.bakeHouse.db.DBConnection;
import lk.ijse.bakeHouse.model.AvailaleProduct;
import lk.ijse.bakeHouse.model.Product;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author NIMESH
 */
public class AvailableItemFormController extends SuperFormController implements Initializable {

    @FXML
    private TableView<Product> tableViewProduct;

    @FXML
    private TableColumn<Product, String> columnSelectProductID;

    @FXML
    private TableColumn<Product, String> columnSelectProductName;

    @FXML
    private TableColumn<Product, String> columnSelectProductType;

    @FXML
    private TableView<AvailaleProduct> tableViewAvailable;

    @FXML
    private TableColumn<AvailaleProduct, String> columnItemID;

    @FXML
    private TableColumn<AvailaleProduct, String> columnProductName;

    @FXML
    private TableColumn<AvailaleProduct, Integer> columnQty;

    @FXML
    private TableColumn<AvailaleProduct, String> columnAction;

    @FXML
    private Label txtProductName;

    @FXML
    private JFXTextField txtQty;

    @FXML
    private JFXButton btnEdit;

    private static ObservableList<Product> list;

    private static ObservableList<AvailaleProduct> data;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            customizeTable();
            loadAllDataOnTable("SELECT * FROM PRODUCT;");
            tableViewProduct.setItems(list);

            tableViewProduct.setOnMouseClicked(e -> getSelectRowOnProductTable());

            loadAllAvalilableData(""
                    + "SELECT a.item_ID,p.name,a.stock "
                    + "FROM available_item a"
                    + " LEFT JOIN product p "
                    + "ON a.proID=p.proID; ");

            tableViewAvailable.setItems(data);

            //TITLE BAR TIME DATE
            showTitleDateTimeAndUserInfo();

        } catch (Exception ex) {

        }

    }

    void getSelectRowOnProductTable() {
        Product pro = tableViewProduct.getSelectionModel().getSelectedItem();

        if (pro != null) {
            txtProductName.setText(pro.getName());
        }
    }

    @Override
    public void goBacKHome(ActionEvent event) throws IOException {
        NavigationOnDashboard.getInstance().goBackHome(event);
    }

    @Override
    public void searchBarWithTable(KeyEvent event) throws ClassNotFoundException, SQLException {
        //
    }

    @Override
    public void editButtonAction(ActionEvent event) {
        try {

            AvailaleProduct orderList = tableViewAvailable.getSelectionModel().getSelectedItem();

            if (orderList == null) {
                return;
            }//not select the row

            String itemId = orderList.getItemId();
            String proName = orderList.getProID();
            int qty = orderList.getQty();

            if (btnEdit.getText().equalsIgnoreCase("EDIT")) {

                txtProductName.setText(proName);
                txtQty.setText(Integer.toString(qty));

                //change btn update properties
                btnEdit.setText("UPDATE");
                btnEdit.setStyle("-fx-background-color: #ff8000; -fx-text-fill: white;");

            } else {

                //new qty 
                int stock = Integer.parseInt(txtQty.getText());

                Connection connection = DBConnection.getInstance().getConnection();
                String sql = "UPDATE available_item  SET stock=" + stock + " WHERE item_ID= '" + itemId + "' ; ";
                PreparedStatement pttm2 = connection.prepareStatement(sql);

                if (pttm2.executeUpdate() > 0) {

                    sucsessNotifyWithReloadAvailableTable();
                } else {
                    TrayNotification tray = MyNotifycation.unSucsessfullyNotify("FAILED!");
                    tray.showAndDismiss(Duration.seconds(2));

                }

                btnEdit.setText("EDIT");
                btnEdit.setStyle("-fx-background-color: #0080ff; -fx-text-fill: white;");

            }//close

        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

    @Override
    public void deleteButtonAction(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
        if (btnEdit.getText().equalsIgnoreCase("UPDATE")) {
            return;
        }

        //conformation Box
        boolean result = MyNotifycation.askWarning("Are Your Sure?");
        if (result == false) {
            return;
        }

        //add to yes no massage
        modifyDataWithExecuteUpdate("DELETE FROM available_item;");

        //REFRESH THE TABLE
        loadAllAvalilableData(""
                + "SELECT a.item_ID,p.name,a.stock "
                + "FROM available_item a "
                + "LEFT JOIN product p "
                + "ON a.proID=p.proID; ");

        tableViewAvailable.setItems(data);

    }

    @Override
    public void loadAllDataOnTable(String sql) throws ClassNotFoundException, SQLException {
        list = FXCollections.observableArrayList();

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ptm = connection.prepareStatement(sql);
        ResultSet rs = ptm.executeQuery();

        while (rs.next()) {
            String id = rs.getString(1);
            String name = rs.getString(2);
            String type = rs.getString(3);
            double unitPrice = rs.getDouble(4);
            double weight = rs.getDouble(5);
            String weightUnit = rs.getString(6);

            //make product object -using database row details
            Product product = new Product(id, name, type, unitPrice, weight, weightUnit);

            //add product object to customer ObserverbleList
            list.add(product);
            // allProduct.add(id+"  |  "+name);

        }
    }

    //tableViewAvailable load
    void loadAllAvalilableData(String sql) throws ClassNotFoundException, SQLException {
        data = FXCollections.observableArrayList();

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ptm = connection.prepareStatement(sql);
        ResultSet rs = ptm.executeQuery();

        while (rs.next()) {
            String itemCode = rs.getString(1);
            String productID = rs.getString(2);
            int qty = rs.getInt(3);

            //add button into row
            Button button = new Button("DELETE");
            button.setStyle("-fx-background-color: #ff9999;");
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    try {
                        Connection connection = DBConnection.getInstance().getConnection();
                        PreparedStatement ptm = connection.prepareStatement(""
                                + "DELETE FROM available_item"
                                + " WHERE item_ID='" + itemCode + "'; ");

                        //conformation Box
                        boolean result = MyNotifycation.askWarning("Are Your Sure?");
                        if (result == false) {
                            return;
                        }

                        if (ptm.executeUpdate() > 0) {

                            TrayNotification tray = MyNotifycation.sucsessfullyNotify("ROW DELETED!.");
                            tray.showAndDismiss(Duration.seconds(2));
                            loadAllAvalilableData(""
                                    + "SELECT a.item_ID,p.name,a.stock "
                                    + "FROM available_item a "
                                    + "LEFT JOIN product p "
                                    + "ON a.proID=p.proID; ");

                            tableViewAvailable.setItems(data);

                        } else {
                            TrayNotification tray = MyNotifycation.unSucsessfullyNotify("FAILED!");
                            tray.showAndDismiss(Duration.seconds(2));
                        }

                        txtProductName.setText("-");
                        txtQty.clear();

                    } catch (Exception ex) {
                        System.out.println("ERROR ON button delete on table");
                        System.out.println(ex);
                    }
                }
            });

            //close button 
            //make product object -using database row details
            AvailaleProduct orderList = new AvailaleProduct(itemCode, productID, qty, button);
            //add product object to customer ObserverbleList
            data.add(orderList);
            // allProduct.add(id+"  |  "+name);

        }

    }

    @Override
    public void customizeTable() {
        tableViewProduct.getColumns().setAll(columnSelectProductID, columnSelectProductName, columnSelectProductType);
        tableViewAvailable.getColumns().setAll(columnItemID, columnProductName, columnQty, columnAction);

        tableViewProduct.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tableViewProduct.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tableViewProduct.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("type"));

        tableViewAvailable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("itemId"));
        tableViewAvailable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("proID"));
        tableViewAvailable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tableViewAvailable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("button"));

    }

    @Override
    public void btnSaveUpdateAction(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {

        if (btnEdit.getText().equalsIgnoreCase("UPDATE") | txtQty.getText().equalsIgnoreCase("")) {
            return;
        }

        try {
            if (!(txtProductName.getText().equalsIgnoreCase("-") && txtQty.getText().equalsIgnoreCase(""))) {

                //get generated id
                String itemID = IDGeneratorConnection.getNewID("available_item", "item_ID", "ID");
                String proName = txtProductName.getText();
                int qty = Integer.parseInt(txtQty.getText());

                //get product IDe
                Connection connection = DBConnection.getInstance().getConnection();
                PreparedStatement ptm = connection.prepareStatement(""
                        + "SELECT proID "
                        + "FROM product "
                        + "WHERE name='" + proName + "';");

                ResultSet rs = ptm.executeQuery();
                rs.next();

                String proID = rs.getString(1);

                int number = 0;
                PreparedStatement ptmm = connection.prepareStatement(""
                        + "SELECT a.item_ID,p.name,a.stock "
                        + "FROM available_item a "
                        + "LEFT JOIN product p "
                        + "ON a.proID=p.proID;");

                ResultSet set = ptmm.executeQuery();

                while (set.next()) {

                    if (set.getString(2).equalsIgnoreCase(proName)) {
                        number++;
                    }
                }

                if (number == 0) {
                    String sql = "INSERT INTO available_item VALUES(?,?,?);";
                    PreparedStatement pstm = connection.prepareStatement(sql);
                    pstm.setString(1, itemID);
                    pstm.setString(2, proID);
                    pstm.setInt(3, qty);

                    //conformation Box
                    boolean result = MyNotifycation.askQuestion("Are Your Sure?");
                    if (result == false) {
                        return;
                    }

                    if (pstm.executeUpdate() > 0) {

                        sucsessNotifyWithReloadAvailableTable();
                        txtProductName.setText("-");
                        txtQty.clear();
                    } else {
                        TrayNotification tray = MyNotifycation.unSucsessfullyNotify("FAILED!");
                        tray.showAndDismiss(Duration.seconds(2));

                    }
                }//
                else {

                    //number not equel to ZERO   go to UPDATE QUERY
                    String getStock = ""
                            + "SELECT stock "
                            + "FROM available_item "
                            + "WHERE proID= '" + proID + "' ; ";

                    PreparedStatement pttm = connection.prepareStatement(getStock);
                    ResultSet set2 = pttm.executeQuery();
                    set2.next();

                    int stock = set2.getInt(1) + qty;

                    String sql = ""
                            + "UPDATE available_item  "
                            + "SET stock=" + stock + " "
                            + "WHERE proID= '" + proID + "' ; ";
                    PreparedStatement pttm2 = connection.prepareStatement(sql);

                    //conformation Box
                    boolean result = MyNotifycation.askQuestion("Are Your Sure?");
                    if (result == false) {
                        return;
                    }

                    if (pttm2.executeUpdate() > 0) {

                        sucsessNotifyWithReloadAvailableTable();

                    } else {
                        TrayNotification tray = MyNotifycation.unSucsessfullyNotify("FAILED!");
                        tray.showAndDismiss(Duration.seconds(2));

                    }

                    txtProductName.setText("-");
                    txtQty.clear();
                }

            }//end

        } catch (Exception ex) {
            TrayNotification tray = MyNotifycation.unSucsessfullyNotify("Exception ex!");
            tray.showAndDismiss(Duration.seconds(2));
            System.out.println(ex);
        }

    }

    void sucsessNotifyWithReloadAvailableTable() throws ClassNotFoundException, SQLException {
        TrayNotification tray = MyNotifycation.sucsessfullyNotify("AVAILABLE ITEM ADDED SUCSESSFULLY.!.");
        tray.showAndDismiss(Duration.seconds(2));

        loadAllAvalilableData(""
                + "SELECT a.item_ID,p.name,a.stock "
                + "FROM available_item a "
                + "LEFT JOIN product p "
                + "ON a.proID=p.proID; ");

        tableViewAvailable.setItems(data);

    }

}
