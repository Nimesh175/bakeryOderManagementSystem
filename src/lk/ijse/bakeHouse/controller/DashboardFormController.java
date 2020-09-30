/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.bakeHouse.controller;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Duration;
import lk.ijse.bakeHouse.custom.GoToAnotherWindow;
import lk.ijse.bakeHouse.custom.NavigationOnDashboard;
import lk.ijse.bakeHouse.model.User;

/**
 * FXML Controller class
 *
 * @author NIMESH
 */
public class DashboardFormController implements Initializable {

    @FXML
    private Label lblTimeDate;

    @FXML
    private Label lblHi;

    @FXML
    private Label lblUserFullName;
    
    
    
    @FXML
    void goToAvailabe(ActionEvent event) throws IOException {
        System.gc();
        GoToAnotherWindow.getAnotherWindow().makeWindow(event, "/lk/ijse/bakeHouse/view/AvailableItemForm.fxml");
    }

    @FXML
    void goToOrderForm(ActionEvent event) throws IOException {
        System.gc();
        GoToAnotherWindow.getAnotherWindow().makeWindow(event, "/lk/ijse/bakeHouse/view/OrderForm.fxml");
    }

    @FXML
    void goToPaymentForm(ActionEvent event) throws IOException {
        System.gc();
        GoToAnotherWindow.getAnotherWindow().makeWindow(event, "/lk/ijse/bakeHouse/view/PaymentForm.fxml");
    }

    @FXML
    void goToLoginMembers(ActionEvent event) throws IOException {
        System.gc();
        GoToAnotherWindow.getAnotherWindow().makeWindow(event, "/lk/ijse/bakeHouse/view/LoginMemberForm.fxml");
    }

    @FXML
    void goToRecipeForm(ActionEvent event) throws IOException {
        System.gc();
        GoToAnotherWindow.getAnotherWindow().makeWindow(event, "/lk/ijse/bakeHouse/view/RecipeForm.fxml");
    }

    @FXML
    void goToProduct(ActionEvent event) throws IOException {
        System.gc();
        GoToAnotherWindow.getAnotherWindow().makeWindow(event, "/lk/ijse/bakeHouse/view/ProductForm.fxml");
    }

    @FXML
    void goToMaterialForm(ActionEvent event) throws IOException {
        System.gc();
        GoToAnotherWindow.getAnotherWindow().makeWindow(event, "/lk/ijse/bakeHouse/view/MaterialForm.fxml");
    }

    @FXML
    void goToCustomerForm(ActionEvent event) throws IOException {
        System.gc();
        GoToAnotherWindow.getAnotherWindow().makeWindow(event, "/lk/ijse/bakeHouse/view/CustomerForm.fxml");
    }

    @FXML
    void goToRouteForm(ActionEvent event) throws IOException {
        System.gc();
        GoToAnotherWindow.getAnotherWindow().makeWindow(event, "/lk/ijse/bakeHouse/view/RouteForm.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showTitleDateTimeAndUserInfo();//current dateTime
        
        
     
        
    }

    @FXML
    public void goBackLogIn(ActionEvent event) throws IOException {
        System.gc();
        GoToAnotherWindow.getAnotherWindow().makeWindow(event, "/lk/ijse/bakeHouse/view/SecondLoginForm.fxml");
    }

    @FXML
    public void goBacKHome(ActionEvent event) throws IOException {
        System.gc();
        NavigationOnDashboard.getInstance().goBackHome(event);
    }

    public void showTitleDateTimeAndUserInfo() {

        //Date
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date2 = dateFormat.format(date);

        //Time
        Timeline time = new Timeline(new KeyFrame(Duration.seconds(0), (ActionEvent event) -> {
        String time2 = new SimpleDateFormat("hh:mm:ss a").format(new Date());
        
            //user Info
            showTitleUserInfo();
            System.gc();
           
            lblTimeDate.setText(date2 + "  " + time2);
            //  lblDate.setText(new SimpleDateFormat("YYYY:MM:dd").format(new Date()));
        }), new KeyFrame(Duration.seconds(1)));
        time.setCycleCount(Animation.INDEFINITE);
        time.play();

    }
    
    public void showTitleUserInfo(){
       try{ 
         User user = LoginAndSignupFormController.getUserLoginInfo();
          String userName1 = user.getFirstName();
          String userName2 = user.getLastName();
          
          lblUserFullName.setText(userName1+" "+userName2);
          
       }catch(Exception ex){}
    }
     
}
