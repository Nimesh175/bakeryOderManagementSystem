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
import lk.ijse.bakeHouse.custom.GoToAnotherWindow;
import lk.ijse.bakeHouse.custom.IDGeneratorConnection;
import lk.ijse.bakeHouse.custom.MyNotifycation;
import lk.ijse.bakeHouse.db.DBConnection;
import lk.ijse.bakeHouse.model.Product;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author NIMESH
 */
public class ProductFormController extends SuperFormController implements Initializable {

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtUnitPrice;

    @FXML
    private Label lblID;

    @FXML
    private RadioButton rationButtonGrams;

    @FXML
    private RadioButton rationButtonKiloGrams;

    @FXML
    private ComboBox<String> comboType;

    @FXML
    private JFXTextField txtGrossWeight;

    @FXML
    private JFXButton btnSaveAndUpdate;

    @FXML
    private JFXComboBox<String> comboSelectField;

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private TableView<Product> tableViewProduct;

    @FXML
    private TableColumn<Product, String> columnProductID;

    @FXML
    private TableColumn<Product, String> coloumnName;

    @FXML
    private TableColumn<Product, String> coloumnType;

    @FXML
    private TableColumn<Product, Double> columnPrice;

    @FXML
    private TableColumn<Product, Double> coloumnWeight;

    @FXML
    private TableColumn<Product, String> columnWeightUnit;

    private final ObservableList<String> list = FXCollections.observableArrayList(
            "Product ID", "Product Name", "Product Type", "Unit Price", "Weight", "Unit");

    //to add product object
    public static ObservableList<Product> data;

    private final ObservableList<String> productTypeList = FXCollections.observableArrayList(
            "BREAD", "BISCUITS", "CAKE", "OTHER");

    //to get all product name
    private static ObservableList<String> allProduct;

    //send the all product Names
    public ObservableList<String> getProductNameCollection() throws ClassNotFoundException, SQLException {
        loadAllDataOnTable("SELECT * FROM PRODUCT;");
        return allProduct;

    }

    @FXML
    void goToRecipeForm(ActionEvent event) throws IOException {
        GoToAnotherWindow.getAnotherWindow().makeWindow(event, "/lk/ijse/bakeHouse/view/RecipeForm.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            //GENERATE ID
            lblID.setText(IDGeneratorConnection.getNewID("PRODUCT", "proID", "P"));

            //load table 
            customizeTable();
            loadAllDataOnTable("SELECT * FROM product;");
            tableViewProduct.setItems(data);

            //TITLE BAR TIME DATE AND USER NAME
            showTitleDateTimeAndUserInfo();

        } catch (Exception ex) {
            System.out.println(ex);
        }

        comboSelectField.setItems(list);
        comboType.setItems(productTypeList);
    }

    @Override
    public void searchBarWithTable(KeyEvent event) throws ClassNotFoundException, SQLException {

        try {

            String keyword = txtSearch.getText();
            String selectItem = comboSelectField.getValue();

            String combo = null;
            //?switch-column name and database table name is different
            switch (selectItem) {
                case "Product ID":
                    combo = "proID";
                    break;
                case "Product Name":
                    combo = "name";
                    break;
                case "Product Type":
                    combo = "type ";
                    break;
                case "Unit Price":
                    combo = "unit_price";
                    break;
                case "Weight":
                    combo = "weight";
                    break;
                case "Unit":
                    combo = "weight_unit";

            }

            loadAllDataOnTable("SELECT * FROM PRODUCT WHERE " + combo + " LIKE '%" + keyword + "%';");
            tableViewProduct.setItems(data);

        } catch (Exception ex) {
            TrayNotification tray = MyNotifycation.warningNotify("CHOICE THE COLUMN FIELD!");
            tray.showAndDismiss(Duration.seconds(2));
        }
    }

    @Override
    public void editButtonAction(ActionEvent event) {
        Product pro = tableViewProduct.getSelectionModel().getSelectedItem();
        if (pro != null) {
            //
            lblID.setText(pro.getId());
            txtName.setText(pro.getName());
            txtGrossWeight.setText(Double.toString(pro.getWeight()));
            txtUnitPrice.setText(Double.toString(pro.getUnitPrice()));
            comboType.setValue(pro.getType());

            switch (pro.getWeightUnit()) {
                case "grams":
                    rationButtonGrams.setSelected(true);
                    break;
                case "kilograms":
                    rationButtonKiloGrams.setSelected(true);

            }

            //change save button properties to update button
            btnSaveAndUpdate.setText("UPDATE");
            btnSaveAndUpdate.setStyle("-fx-background-color: #ff8000; -fx-text-fill: white;");
        }
    }

    @Override
    public void deleteButtonAction(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
        Product pro = tableViewProduct.getSelectionModel().getSelectedItem();

        if (pro != null) {

            //conformation Box
            boolean result = MyNotifycation.askWarning("Are Your sure?");
            if (result == false) {
                return;
            }

            modifyDataWithExecuteUpdate(""
                    + "DELETE FROM PRODUCT "
                    + "WHERE proID = '" + pro.getId() + "';");

            loadAllDataOnTable("SELECT * FROM PRODUCT; ");
            tableViewProduct.setItems(data);
            MyNotifycation.sucsessfullyNotify("DELETED COMPLETELY!");
            //GENERATE ID
            lblID.setText(IDGeneratorConnection.getNewID("PRODUCT", "proID", "P"));

        } else {
            MyNotifycation.infoNotify("SELECT THE ROW!");
        }
    }

    @Override
    public void customizeTable() {
        //columns add to table
        tableViewProduct.getColumns().setAll(columnProductID, coloumnName, coloumnType,
                columnPrice, coloumnWeight, columnWeightUnit);

        tableViewProduct.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tableViewProduct.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tableViewProduct.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("type"));
        tableViewProduct.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tableViewProduct.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("weight"));
        tableViewProduct.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("weightUnit"));

    }

    @Override
    public void loadAllDataOnTable(String sql) throws ClassNotFoundException, SQLException {
        data = FXCollections.observableArrayList();
        allProduct = FXCollections.observableArrayList();

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
            data.add(product);
            // allProduct.add(id+"  |  "+name);
            allProduct.add(name);

        }
    }

    @Override
    public void btnSaveUpdateAction(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
        try {
            String id = lblID.getText();
            String name = txtName.getText();
            String comboType1 = this.comboType.getValue();
            double price = Double.parseDouble(txtUnitPrice.getText());

            String weightUnit = null;
            if (rationButtonGrams.isSelected()) {
                weightUnit = rationButtonGrams.getText();
            } else if (rationButtonKiloGrams.isSelected()) {
                weightUnit = rationButtonKiloGrams.getText();
            }

            double weight = Double.parseDouble(txtGrossWeight.getText());

            if (btnSaveAndUpdate.getText().equalsIgnoreCase("SAVE")) {

                if ((!name.equalsIgnoreCase(null)) & (!comboType1.equalsIgnoreCase(null))) {

                    //  Product product = new Product(id,name,comboType,price,weight,weightUnit);
                    Connection connection = DBConnection.getInstance().getConnection();
                    PreparedStatement stm = connection.prepareStatement(
                            "INSERT INTO PRODUCT VALUES(?,?,?,?,?,?);");

                    //
                    stm.setString(1, id);
                    stm.setString(2, name);
                    stm.setString(3, comboType1);
                    stm.setDouble(4, price);
                    stm.setDouble(5, weight);
                    stm.setString(6, weightUnit);

                    //conformation Box
                    boolean result = MyNotifycation.askQuestion("Are Your sure?");
                    if (result == false) {
                        return;
                    }

                    if (stm.executeUpdate() > 0) {
                        // set notification
                        TrayNotification tray = MyNotifycation.sucsessfullyNotify("PRODUCT ADDED SUCSESSFULLY.");
                        tray.showAndDismiss(Duration.seconds(2));

                        // Set next customer number using IDGeneratorConnection.java
                        lblID.setText(IDGeneratorConnection.getNewID("PRODUCT", "proID", "P"));

                        //clear the form
                        txtName.clear();
                        txtUnitPrice.clear();
                        txtGrossWeight.clear();

                        //load Table data after save
                        loadAllDataOnTable("SELECT * FROM PRODUCT;");
                        tableViewProduct.setItems(data);

                    } else {
                        TrayNotification tray = MyNotifycation.unSucsessfullyNotify();
                        tray.showAndDismiss(Duration.seconds(2));
                    }//close if

                } else {
                    TrayNotification tray = MyNotifycation.unSucsessfullyNotify();
                    tray.showAndDismiss(Duration.seconds(2));
                    //clear the form
                    txtName.clear();
                    txtUnitPrice.clear();
                    txtGrossWeight.clear();
                }

            } else {

                // button Updte mode
                try {

                    String sql = "UPDATE PRODUCT "
                            + "SET name='" + name + "'"
                            + ",type='" + comboType1 + "',unit_price='" + price + "'"
                            + ",weight='" + weight + "',weight_unit='" + weightUnit + "' "
                            + "WHERE proID='" + id + "' ;";

                    //conformation Box
                    boolean result = MyNotifycation.askQuestion("Are Your sure?");
                    if (result == false) {
                        return;
                    }

                    modifyDataWithExecuteUpdate(sql);
                    loadAllDataOnTable("SELECT * FROM PRODUCT; ");
                    tableViewProduct.setItems(data);

                    //NOTIFICATION
                    TrayNotification tray = MyNotifycation.sucsessfullyNotify("UPDATE SUCSESSFULLY.");
                    tray.showAndDismiss(Duration.seconds(2));

                    //BUTTON RESET UPDATE TO SAVE & ID GENERATE
                    btnSaveAndUpdate.setText("SAVE");
                    btnSaveAndUpdate.setStyle("-fx-background-color:  #019638; -fx-text-fill: white;");
                    lblID.setText(IDGeneratorConnection.getNewID("PRODUCT", "proID", "P"));

                    //CLEAR THE TEXTFIELD
                    txtName.clear();
                    txtUnitPrice.clear();
                    txtGrossWeight.clear();

                } catch (Exception ex) {
                    TrayNotification tray = MyNotifycation.warningNotify("INVALID INFORMATION!");
                    tray.showAndDismiss(Duration.seconds(2));
                }

            }

        } catch (Exception ex) {
            TrayNotification tray = MyNotifycation.unSucsessfullyNotify("WRONG INFORMATION.");
            tray.showAndDismiss(Duration.seconds(1));
        }//close try-catch

    }

}
