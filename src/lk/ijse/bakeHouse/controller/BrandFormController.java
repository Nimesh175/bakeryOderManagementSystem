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
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import lk.ijse.bakeHouse.controller.superFormController.SuperFormController;
import lk.ijse.bakeHouse.custom.GoToAnotherWindow;
import lk.ijse.bakeHouse.custom.IDGeneratorConnection;
import lk.ijse.bakeHouse.custom.MyNotifycation;
import lk.ijse.bakeHouse.db.DBConnection;
import lk.ijse.bakeHouse.model.Brand;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author NIMESH
 */
public class BrandFormController extends SuperFormController implements Initializable {

    
    @FXML
    private JFXButton btnSave;

    @FXML
    private Label lblBrandID;

    @FXML
    private JFXTextField txtBrandName;

    @FXML
    private JFXTextArea txtDescriptionBrand;

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private TableView<Brand> tableView;

    @FXML
    private TableColumn<Brand, String> columnBrandID;

    @FXML
    private TableColumn<Brand, String> columnName;

    @FXML
    private TableColumn<Brand,String> columnDescription;


    @FXML
    private JFXComboBox<String> comboBoxSelectField;
    
    private static final ObservableList<String> selectItemField=FXCollections.observableArrayList("Brand ID","Brand Name","Description");
     
    private static ObservableList<Brand> data;
    
    private static ObservableList<String> allBrand;
    
    public ObservableList<String>  getBrandNameCollection() throws ClassNotFoundException, SQLException{
        loadAllDataOnTable("SELECT * FROM Brand;");
        
        return allBrand;
    }
    
     @FXML
    void goToMaterialBack(ActionEvent event) throws IOException {
        GoToAnotherWindow.getAnotherWindow().makeWindow(event,"/lk/ijse/bakeHouse/view/MaterialForm.fxml");
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         try {
            //GENERATE ID
            lblBrandID.setText(IDGeneratorConnection.getNewID("Brand", "brand_ID", "B"));
            
            //load table 
            customizeTable();
            loadAllDataOnTable("SELECT * FROM Brand;");
            tableView.setItems(data);
            
            //setup comboBox for search
            comboBoxSelectField.setItems(selectItemField);
            
            //TITLE BAR TIME DATE
            showTitleDateTimeAndUserInfo();
            
            
        } catch (Exception ex) {
             System.out.println(ex);
        }
    }    

   

    @Override
    public void searchBarWithTable(KeyEvent event) throws ClassNotFoundException, SQLException {
        try{
            
            String keyword = txtSearch.getText();
            String selectItem = comboBoxSelectField.getValue();

            String combo = null;
            //?switch-column name and database table name is different
            switch (selectItem) {
                case "Brand ID":
                    combo = "brand_ID";
                    break;
                 case "Brand Name":
                    combo = "name";
                    break;
                case "Description":
                    combo = "description";

            }

            loadAllDataOnTable("SELECT * FROM BRAND WHERE " + combo + " LIKE '%" + keyword + "%';");
            tableView.setItems(data); 
       
       }catch(Exception ex){
            TrayNotification tray = MyNotifycation.warningNotify("CHOICE THE COLUMN FIELD!");
            tray.showAndDismiss(Duration.seconds(2));
       }
        
   
    }

    @Override
    public void editButtonAction(ActionEvent event) {
         Brand pro = tableView.getSelectionModel().getSelectedItem();
        if (pro != null) {
            //
            lblBrandID.setText(pro.getId());
            txtBrandName.setText(pro.getName());
            txtDescriptionBrand.setText(pro.getDescription());
                 
            

            //change save button properties to update button
            btnSave.setText("UPDATE");
            btnSave.setStyle("-fx-background-color: #ff8000; -fx-text-fill: white;");
        }
    }

    @Override
    public void deleteButtonAction(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
         Brand pro = tableView.getSelectionModel().getSelectedItem();
        if (pro!=null) {
            
        //conformation Box
        boolean result=MyNotifycation.askWarning("Are Your Sure?");
        if (result==false) {
                    return;
        }    
            
            modifyDataWithExecuteUpdate(""
                    + "DELETE FROM BRAND "
                    + "WHERE brand_ID = '"+pro.getId()+"';");
            
            loadAllDataOnTable("SELECT * FROM BRAND; ");
            tableView.setItems(data);
            
            //GENERATE ID
            lblBrandID.setText(IDGeneratorConnection.getNewID("Brand", "brand_ID", "B"));
            
        }else{
            MyNotifycation.infoNotify("SELECT THE ROW!");
        }
    }

    @Override
    public void loadAllDataOnTable(String sql) throws ClassNotFoundException, SQLException {
          //observerble list
        data = FXCollections.observableArrayList();
        allBrand = FXCollections.observableArrayList();

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ptm = connection.prepareStatement(sql);
        ResultSet rs = ptm.executeQuery();

        while (rs.next()) {
            String id = rs.getString(1);
            String name = rs.getString(2);
            String description = rs.getString(3);
            
            //make brand object -using database row details
            Brand brand = new Brand(id,name,description);

            //add brand object to brand ObserverbleList
            data.add(brand);
            //collect brand name
            allBrand.add(name);
  
        }//loop end
    }

    @Override
    public void customizeTable() {
         
        //columns add to table
        tableView.getColumns().setAll(columnBrandID,columnName,columnDescription);

        tableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("description"));
       
    }

    @Override
    public void btnSaveUpdateAction(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
      
        String id=lblBrandID.getText();
        String name=txtBrandName.getText();
        String description=txtDescriptionBrand.getText();
       
        if (!name.equalsIgnoreCase("") ) {
        
            if (btnSave.getText().equalsIgnoreCase("SAVE")) {
                
                
                   //  Route route = new Route(id,name,description);

                      Connection connection = DBConnection.getInstance().getConnection();
                      PreparedStatement stm = connection.prepareStatement(
                                        "INSERT INTO BRAND VALUES(?,?,?);");
                       
                            stm.setString(1, id);
                            stm.setString(2, name);
                            stm.setString(3, description);
                    
                      //conformation Box
                        boolean result=MyNotifycation.askQuestion("Are Your Sure?");
                        if (result==false) {
                              return;
                        }                
                            
                        if (stm.executeUpdate() > 0) {
                            // set notification
                            TrayNotification tray = MyNotifycation.sucsessfullyNotify("BRAND ADDED SUCSESSFULLY.");
                            tray.showAndDismiss(Duration.seconds(2));

                            //GENERATE ID
                            lblBrandID.setText(IDGeneratorConnection.getNewID("Brand", "brand_ID", "B"));

                            //clear the form
                            txtBrandName.clear();
                            txtDescriptionBrand.clear();
                            

                            //load Table data after save
                            loadAllDataOnTable("SELECT * FROM BRAND;");
                            tableView.setItems(data);

                        }else{
                            TrayNotification tray = MyNotifycation.unSucsessfullyNotify();
                            tray.showAndDismiss(Duration.seconds(2));
                        }
            }else{
            //button update mode
                try{
                    
                    //conformation Box
                    boolean result=MyNotifycation.askQuestion("Are Your Sure?");
                    if (result==false) {
                           return;
                    }
                  
                    String sql="UPDATE BRAND "
                            + "SET name='"+name+"'"
                            + ",description='"+description+"'"
                            + "WHERE brand_ID='"+id+"' ;";
                    
                    //Brand or Route
                    modifyDataWithExecuteUpdate(sql);
                    loadAllDataOnTable("SELECT * FROM Brand; ");
                    tableView.setItems(data);
                    
                    //NOTIFICATION
                    TrayNotification tray = MyNotifycation.sucsessfullyNotify("UPDATE SUCSESSFULLY.");
                    tray.showAndDismiss(Duration.seconds(2));
                    
                    //BUTTON RESET UPDATE TO SAVE & ID GENERATE
                    btnSave.setText("SAVE");
                    btnSave.setStyle("-fx-background-color:   #019638; -fx-text-fill: white;");
                    //GENERATE ID
                    lblBrandID.setText(IDGeneratorConnection.getNewID("Brand", "brand_ID", "B"));
                    
                    //CLEAR THE TEXTFIELD
                    txtBrandName.clear();
                    txtDescriptionBrand.clear();
                       
                }catch(Exception ex){
                    TrayNotification tray = MyNotifycation.warningNotify("INVALID INFORMATION!");
                    tray.showAndDismiss(Duration.seconds(2));
                    
                    //CLEAR THE TEXTFIELD
                    txtBrandName.clear();
                    txtDescriptionBrand.clear();
                }

            }//CLOSE ELSE
       
           
        }else{
            TrayNotification tray = MyNotifycation.unSucsessfullyNotify();
            tray.showAndDismiss(Duration.seconds(2));
        
        }//close
        
    }
    
}
