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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
import lk.ijse.bakeHouse.model.Material;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author NIMESH
 */
public class MaterialFormController extends SuperFormController implements Initializable {

    @FXML
    private JFXTextField txtMaterialName;

    @FXML
    private Label lblMaterialID;

    @FXML
    private ComboBox<String> comboMaterialBrand;

    @FXML
    private ComboBox<String> comboMaterialType;

    @FXML
    private JFXTextArea txtMaterialDescription;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private JFXComboBox<String> comboBoxSelectField;

    @FXML
    private TableView<Material> tableView;

    @FXML
    private TableColumn<Material, String> columnID;

    @FXML
    private TableColumn<Material, String> columnName;

    @FXML
    private TableColumn<Material, String> columnType;

    @FXML
    private TableColumn<Material, String> columnBrand;

    @FXML
    private TableColumn<Material, String> columnDescription;

    //to add Material object
    private static ObservableList<Material> list;
    //select Item combobox for search
    private final ObservableList<String> selectItemField = FXCollections.observableArrayList("Material ID", "Material Name", "Material Type", "Material Brand", "Description");

    private final ObservableList<String> materialType = FXCollections.observableArrayList("OIL", "POWDER", "OTHER");

    //to get all product name
    private static ObservableList<String> allMaterial;

    //send the all product Names
    public ObservableList<String> getMaterialNameCollection() throws ClassNotFoundException, SQLException {
        loadAllDataOnTable("SELECT * FROM Material;");
        return allMaterial;

    }

    @FXML
    void goToBrandForm(ActionEvent event) throws IOException {
        GoToAnotherWindow.getAnotherWindow().makeWindow(event, "/lk/ijse/bakeHouse/view/BrandForm.fxml");
    }

    String getSelectAllMaterialQuery() {

        return "SELECT M.material_ID,M.name,B.name , M.material_type,M.description "
                + "FROM MATERIAL M LEFT JOIN BRAND B "
                + "ON M.brand_ID=B.brand_ID ";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {

            //GENERATE ID
            lblMaterialID.setText(IDGeneratorConnection.getNewID("MATERIAL", "material_ID", "M"));

            //load table
            customizeTable();
            loadAllDataOnTable(getSelectAllMaterialQuery());

            tableView.setItems(list);

            comboBoxSelectField.setItems(selectItemField);
            comboMaterialType.setItems(materialType);
            comboMaterialBrand.setItems(new BrandFormController().getBrandNameCollection());

            //TITLE BAR TIME DATE AND USER NAME
            showTitleDateTimeAndUserInfo();

        } catch (Exception ex) {

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
                case "Material ID":
                    combo = "M.material_ID";
                    break;
                case "Material Name":
                    combo = "M.name";
                    break;
                case "Material Brand"://
                    combo = "B.name ";
                    break;
                case "Material Type":
                    combo = "M.material_type";
                    break;
                case "Description":
                    combo = "M.description";

            }

            //? brand_ID is a key name -  brand name convert to brand_ID
            loadAllDataOnTable(getSelectAllMaterialQuery() + "WHERE " + combo + " LIKE '%" + keyword + "%';");
            tableView.setItems(list);

            //tableView.setItems(list);
        } catch (Exception ex) {
            TrayNotification tray = MyNotifycation.warningNotify("CHOICE THE COLUMN FIELD!");
            tray.showAndDismiss(Duration.seconds(2));
        }
    }

    @Override
    public void editButtonAction(ActionEvent event) {
        Material material = tableView.getSelectionModel().getSelectedItem();

        if (material != null) {
            lblMaterialID.setText(material.getMaterialID());
            txtMaterialName.setText(material.getMaterialName());
            comboMaterialBrand.setValue(material.getBrandID());//brand Name is displaying
            comboMaterialType.setValue(material.getMaterialType());
            txtMaterialDescription.setText(material.getMaterialDescription());

            //change save button properties to update button
            btnSave.setText("UPDATE");
            btnSave.setStyle("-fx-background-color: #ff8000; -fx-text-fill: white;");
        }
    }

    @Override
    public void deleteButtonAction(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {

        Material material = tableView.getSelectionModel().getSelectedItem();
        if (material != null) {

            //conformation Box
            boolean result = MyNotifycation.askWarning("Are Your Sure?");
            if (result == false) {
                return;
            }

            modifyDataWithExecuteUpdate(""
                    + "DELETE FROM Material "
                    + "WHERE material_ID = '" + material.getMaterialID() + "';");

            loadAllDataOnTable(getSelectAllMaterialQuery());
            tableView.setItems(list);

            //GENERATE ID
            lblMaterialID.setText(IDGeneratorConnection.getNewID("MATERIAL", "material_ID", "M"));

        } else {
            MyNotifycation.infoNotify("SELECT THE ROW!");
        }

    }

    @Override
    public void loadAllDataOnTable(String sql) throws ClassNotFoundException, SQLException {
        list = FXCollections.observableArrayList();
        allMaterial = FXCollections.observableArrayList();

        Connection connection = DBConnection.getInstance().getConnection();

        PreparedStatement ptm = connection.prepareStatement(sql);
        ResultSet rs = ptm.executeQuery();
        while (rs.next()) {
            String id = rs.getString(1);
            String name = rs.getString(2);

            //get brand name 
            PreparedStatement getBrandName = connection.prepareStatement(""
                    + "SELECT name FROM BRAND WHERE brand_ID='" + rs.getString(3) + "';");
            ResultSet set = getBrandName.executeQuery();
            set.next();
            String brandName = rs.getString(3);

            String type = rs.getString(4);
            String description = rs.getString(5);
            //make material object -using database row details
            Material material = new Material(id, name, brandName, type, description);
            //add material object to customer ObserverbleList
            list.add(material);
            // allMaterial.add(id+" | "+name);
            allMaterial.add(name);
        }
    }

    @Override
    public void customizeTable() {
        //columns add to table
        tableView.getColumns().setAll(columnID, columnName, columnType, columnBrand, columnDescription);

        tableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("materialID"));
        tableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("materialName"));
        tableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("materialType"));
        tableView.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("brandID"));
        tableView.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("materialDescription"));

    }

    @Override
    public void btnSaveUpdateAction(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
        try {
            String id = lblMaterialID.getText();
            String name = txtMaterialName.getText();
            String brandName = comboMaterialBrand.getValue();
            String type = comboMaterialType.getValue();
            String description = txtMaterialDescription.getText();

            if (name.equalsIgnoreCase("")) {
                MyNotifycation.infoNotify("DETAILS NOT COMPLETED!");
                return;
            }
            // get brand_ID using brandName
            String brandID = "";
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement ptm = connection.prepareStatement("SELECT brand_ID FROM BRAND WHERE name='" + brandName + "';");
            ResultSet rs = ptm.executeQuery();
            rs.next();
            brandID = rs.getString(1);

            if (btnSave.getText().equalsIgnoreCase("SAVE")) {

                if ((!name.equalsIgnoreCase(null)) & (!brandName.equalsIgnoreCase(null))) {

                    PreparedStatement stm = connection.prepareStatement(
                            "INSERT INTO MATERIAL VALUES(?,?,?,?,?);");

                    stm.setString(1, id);
                    stm.setString(2, name);
                    stm.setString(3, brandID);
                    stm.setString(4, type);
                    stm.setString(5, description);

                    //conformation Box
                    boolean result = MyNotifycation.askQuestion("Are Your Sure?");
                    if (result == false) {
                        return;
                    }

                    if (stm.executeUpdate() > 0) {
                        // set notification
                        TrayNotification tray = MyNotifycation.sucsessfullyNotify("MATERIAL ADDED SUCSESSFULLY.");
                        tray.showAndDismiss(Duration.seconds(2));

                        //GENERATE ID
                        lblMaterialID.setText(IDGeneratorConnection.getNewID("MATERIAL", "material_ID", "M"));

                        //clear the form
                        txtMaterialName.clear();
                        txtMaterialDescription.clear();

                        //load Table data after save
                        loadAllDataOnTable(getSelectAllMaterialQuery());
                        tableView.setItems(list);

                    } else {
                        TrayNotification tray = MyNotifycation.unSucsessfullyNotify();
                        tray.showAndDismiss(Duration.seconds(2));
                    }//close if

                } else {
                    TrayNotification tray = MyNotifycation.unSucsessfullyNotify();
                    tray.showAndDismiss(Duration.seconds(2));

                    //clear the form
                    txtMaterialName.clear();
                    txtMaterialDescription.clear();
                }

            } else {

                // button Updte mode
                try {

                    //conformation Box
                    boolean result = MyNotifycation.askQuestion("Are Your Sure?");
                    if (result == false) {
                        return;
                    }

                    String sql = "UPDATE MATERIAL "
                            + "SET name='" + name + "'"
                            + ",brand_ID='" + brandID + "',material_type='" + type + "',description='" + description + "' "
                            + "WHERE material_ID='" + id + "' ;";

                    int x = modifyDataWithExecuteUpdate(sql);
                    System.out.println("x = " + x);
                    if (x == 0) {
                        return;
                    }

                    loadAllDataOnTable(getSelectAllMaterialQuery());
                    tableView.setItems(list);

                    //NOTIFICATION
                    TrayNotification tray = MyNotifycation.sucsessfullyNotify("UPDATE SUCSESSFULLY.");
                    tray.showAndDismiss(Duration.seconds(2));

                    //BUTTON RESET UPDATE TO SAVE & ID GENERATE
                    btnSave.setText("SAVE");
                    btnSave.setStyle("-fx-background-color:  #019638; -fx-text-fill: white;");

                    //GENERATE ID
                    lblMaterialID.setText(IDGeneratorConnection.getNewID("MATERIAL", "material_ID", "M"));

                    //CLEAR THE TEXTFIELD
                    txtMaterialName.clear();
                    txtMaterialDescription.clear();

                } catch (Exception ex) {
                    TrayNotification tray = MyNotifycation.warningNotify("INVALID INFORMATION!");
                    tray.showAndDismiss(Duration.seconds(2));
                }

            }

        } catch (Exception ex) {
            TrayNotification tray = MyNotifycation.warningNotify("INVALID INFORMATION!");
            tray.showAndDismiss(Duration.seconds(2));
        }
    }

}
