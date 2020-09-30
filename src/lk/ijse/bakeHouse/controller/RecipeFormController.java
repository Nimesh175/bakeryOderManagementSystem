/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.bakeHouse.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import lk.ijse.bakeHouse.controller.superFormController.SuperFormController;
import lk.ijse.bakeHouse.custom.IDGeneratorConnection;
import lk.ijse.bakeHouse.custom.MyNotifycation;
import lk.ijse.bakeHouse.db.DBConnection;
import lk.ijse.bakeHouse.model.Recipe;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author NIMESH
 */
public class RecipeFormController extends SuperFormController implements Initializable {

    @FXML
    private ComboBox<String> comboSelectProduct;

    @FXML
    private TextField txtMixSize;

    @FXML
    private JFXComboBox<String> comboUnit;

    @FXML
    private JFXComboBox<String> comboSelectMaerial;

    @FXML
    private JFXTextArea txtDescription;

    @FXML
    private JFXButton btnAddMaterial;

    @FXML
    private TableView<Recipe> tableView;

    @FXML
    private TableColumn<Recipe, String> columnMaterialName;

    @FXML
    private TableColumn<Recipe, String> columnNo;

    @FXML
    private TableColumn<Recipe, Double> columnMixSize;

    @FXML
    private TableColumn<Recipe, String> columnMixUnit;

    @FXML
    private TableColumn<Recipe, String> columnDescription;

    private static ObservableList<Recipe> data;

    private final ObservableList<String> unit = FXCollections.observableArrayList("G", "KG", "ML", "L");

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            comboSelectProduct.setItems(new ProductFormController().getProductNameCollection());
            comboSelectMaerial.setItems(new MaterialFormController().getMaterialNameCollection());
            comboUnit.setItems(unit);

            customizeTable();

            comboSelectProduct.setOnAction(e -> {
                try {

                    loadTableView();

                } catch (Exception ex) {
                }

            });
            tableView.setItems(data);

            //TITLE BAR TIME DATE AND USER NAME
            showTitleDateTimeAndUserInfo();

        } catch (Exception ex) {
            System.out.println("Exception ex = " + ex);
        }

    } //close   

    void loadTableView() throws ClassNotFoundException, SQLException {
        String proName = comboSelectProduct.getValue();
        Connection connection = DBConnection.getInstance().getConnection();

        PreparedStatement stm = connection.prepareStatement(""
                + "SELECT proID "
                + "FROM product "
                + "WHERE name='" + proName + "';");

        ResultSet rs = stm.executeQuery();
        rs.next();

        loadAllDataOnTable("SELECT r.primaryID , r.proID , r.material_ID   , m.name "
                + ", r.size,r.unit,r.description "
                + "FROM recipe r "
                + "LEFT JOIN material m  "
                + "ON r.material_ID =m.material_ID "
                + "WHERE r.proID ='" + rs.getString(1) + "'");

        tableView.setItems(data);

    }

    @Override
    public void searchBarWithTable(KeyEvent event) throws ClassNotFoundException, SQLException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void editButtonAction(ActionEvent event) {
        Recipe recipe = tableView.getSelectionModel().getSelectedItem();
        if (recipe != null) {
            try {
                String id = recipe.getMaterialName();
                String unit = recipe.getUnit();
                String description = recipe.getDescription();
                double size = recipe.getSize();
                String proID = recipe.getProID();

                comboSelectMaerial.setValue(id);
                txtMixSize.setText(Double.toString(size));
                comboUnit.setValue(unit);
                txtDescription.setText(description);
                comboSelectProduct.setValue(proID);

                //change save button properties to update button
                btnAddMaterial.setText("UPDATE");
            } catch (Exception ex) {
                System.out.println("ex = " + ex);
            }
        }
    }

    @Override
    public void deleteButtonAction(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
        Recipe recipe = tableView.getSelectionModel().getSelectedItem();
        String id = recipe.getPrimaryID();

        if (id != null) {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement stm = connection.prepareStatement(""
                    + "DELETE FROM RECIPE "
                    + "WHERE primaryID ='" + id + "';");

            //conformation Box
            boolean result = MyNotifycation.askWarning("Are Your Sure?");
            if (result == false) {
                return;
            }

            if (stm.executeUpdate() > 0) {
                TrayNotification tray = MyNotifycation.sucsessfullyNotify(" ROW DELETED.");
                tray.showAndDismiss(Duration.seconds(2));

                //refresh table
                loadTableView();

            } else {

                TrayNotification tray = MyNotifycation.unSucsessfullyNotify();
                tray.showAndDismiss(Duration.seconds(2));

            }
        }
    }

    @Override
    public void loadAllDataOnTable(String sql) throws ClassNotFoundException, SQLException {
        data = FXCollections.observableArrayList();

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ptm = connection.prepareStatement(sql);
        ResultSet rs = ptm.executeQuery();

        while (rs.next()) {
            String id = rs.getString(1);
            String proID = rs.getString(2);
            String materialID = rs.getString(3);
            String materialName = rs.getString(4);
            double size = rs.getDouble(5);
            String unit = rs.getString(6);
            String description = rs.getString(7);

            //make recipe object -using database row details
            Recipe recipe = new Recipe(id, proID, materialID, materialName, size, unit, description);
            //add product object to customer ObserverbleList
            data.add(recipe);

        }
    }

    @Override
    public void customizeTable() {
        //columns add to table
        tableView.getColumns().setAll(columnNo, columnMaterialName, columnMixSize, columnMixUnit, columnDescription);

        tableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("primaryID"));
        tableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("materialName"));
        tableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("size"));
        tableView.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unit"));
        tableView.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("description"));

    }

    @Override
    public void btnSaveUpdateAction(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
        try {
            if (btnAddMaterial.getText().equalsIgnoreCase("ADD")) {
                String proName = comboSelectProduct.getValue();
                String materialName = comboSelectMaerial.getValue();
                Connection connection = DBConnection.getInstance().getConnection();

                PreparedStatement stm = connection.prepareStatement(""
                        + "SELECT proID "
                        + "FROM product "
                        + "WHERE name='" + proName + "';");

                ResultSet set = stm.executeQuery();
                set.next();

                String primaryID = IDGeneratorConnection.getNewID("Recipe", "primaryID", "R");

                String proId = set.getString(1);

                PreparedStatement pstm = connection.prepareStatement(""
                        + "SELECT material_ID "
                        + "FROM material "
                        + "WHERE name='" + materialName + "';");

                ResultSet rs = pstm.executeQuery();
                rs.next();

                String materialID = rs.getString(1);
                double size = Double.parseDouble(txtMixSize.getText());
                String unitMix = comboUnit.getValue();
                String description = txtDescription.getText();

                PreparedStatement cheak = connection.prepareStatement("SELECT  proID ,material_ID FROM RECIPE;");
                ResultSet rsCheak = cheak.executeQuery();
                while (rsCheak.next()) {

                    String pro = rsCheak.getString(1);
                    String mat = rsCheak.getString(2);

                    if (pro.equalsIgnoreCase(proId) && mat.equalsIgnoreCase(materialID)) {

                        TrayNotification tray = MyNotifycation.infoNotify("MATERIAL DUPLICATED.");
                        tray.showAndDismiss(Duration.seconds(2));

                        return;
                    }

                }

                stm = connection.prepareStatement(""
                        + "INSERT INTO RECIPE "
                        + "VALUES(?,?,?,?,?,?);");

                stm.setString(1, primaryID);
                stm.setString(2, proId);
                stm.setString(3, materialID);
                stm.setDouble(4, size);
                stm.setString(5, unitMix);
                stm.setString(6, description);

                //conformation Box
                boolean result = MyNotifycation.askQuestion("Are Your Sure?");
                if (result == false) {
                    return;
                }

                if (stm.executeUpdate() > 0) {
                    TrayNotification tray = MyNotifycation.sucsessfullyNotify(" ADDED SUCSESSFULLY.");
                    tray.showAndDismiss(Duration.seconds(2));
                    //clear
                    txtMixSize.clear();
                    comboSelectMaerial.setValue(null);
                    comboUnit.setValue(null);
                    txtDescription.clear();

                    loadTableView();

                } else {
                    TrayNotification tray = MyNotifycation.unSucsessfullyNotify("INVALID INFO | TRY SAME MATERIAL ADDED!");
                    tray.showAndDismiss(Duration.seconds(2));

                }
            } else {
                //update mode
                updateAction();

            }

        } catch (Exception ex) {
            System.out.println("ex = " + ex);
            TrayNotification tray = MyNotifycation.unSucsessfullyNotify("INVALID INFO!");
            tray.showAndDismiss(Duration.seconds(2));
        }

    }

    void updateAction() throws ClassNotFoundException, SQLException {

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            //dont change pk    
            String proID = comboSelectProduct.getValue();//id
            String materialName = comboSelectMaerial.getValue();//name

            PreparedStatement stm = connection.prepareStatement(""
                    + "SELECT material_ID,NAME "
                    + "FROM material "
                    + "WHERE name='" + materialName + "';");

            ResultSet rs = stm.executeQuery();
            rs.next();

            // String materialID = rs.getString(1);
            String name = rs.getString(2);

            double size = Double.parseDouble(txtMixSize.getText());//
            String mixUnit = comboUnit.getValue();//
            String description = txtDescription.getText();//

            if (name.equalsIgnoreCase(materialName)) {

                PreparedStatement pstm = connection.prepareStatement(""
                        + "SELECT primaryID "
                        + "FROM recipe "
                        + "ORDER BY primaryID "
                        + "DESC LIMIT 1;");

                ResultSet prs = pstm.executeQuery();
                prs.next();

                String primaryID = prs.getString(1);//

                stm.close();
                stm = connection.prepareStatement(""
                        + "UPDATE RECIPE "
                        + "SET size=" + size + ",UNIT='" + mixUnit + "',description='" + description + "' "
                        + "WHERE primaryID='" + primaryID + "' ;");

                //conformation Box
                boolean result = MyNotifycation.askWarning("Are Your Sure?");
                if (result == false) {
                    return;
                }

                if (stm.executeUpdate() > 0) {
                    TrayNotification tray = MyNotifycation.sucsessfullyNotify(" UPDATE SUCSESSFULLY.");
                    tray.showAndDismiss(Duration.seconds(2));

                    txtMixSize.clear();
                    comboSelectMaerial.setValue(null);
                    comboUnit.setValue(null);
                    txtDescription.clear();

                    //load data on table
                    loadAllDataOnTable(""
                            + "SELECT r.primaryID , r.proID , r.material_ID   , m.name "
                            + ", r.size,r.unit,r.description "
                            + "FROM recipe r LEFT JPIN material m  "
                            + "ON r.material_ID =m.material_ID "
                            + "WHERE r.proID ='" + proID + "'");

                    tableView.setItems(data);
                    //button reset
                    btnAddMaterial.setText("SAVE");

                } else {
                    //button reset
                    btnAddMaterial.setText("SAVE");

                    TrayNotification tray = MyNotifycation.unSucsessfullyNotify();
                    tray.showAndDismiss(Duration.seconds(2));

                }

            } else {
                TrayNotification tray = MyNotifycation.unSucsessfullyNotify("SOLUSTION! ,YOU CAN DELETE THE MATERIAL.");
                tray.showAndDismiss(Duration.seconds(2));

            }

        } catch (Exception ex) {
            TrayNotification tray = MyNotifycation.unSucsessfullyNotify("SOLUSTION! ,YOU CAN DELETE THE MATERIAL.");
            tray.showAndDismiss(Duration.seconds(2));
        }

    }

}
