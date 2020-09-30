/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.bakeHouse.controller.superFormController;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.bakeHouse.controller.LoginAndSignupFormController;
import lk.ijse.bakeHouse.custom.GoToAnotherWindow;
import lk.ijse.bakeHouse.custom.MyNotifycation;
import lk.ijse.bakeHouse.custom.NavigationOnDashboard;
import lk.ijse.bakeHouse.db.DBConnection;
import lk.ijse.bakeHouse.main.NewFXMain;
import lk.ijse.bakeHouse.model.User;

/**
 *
 * @author NIMESH
 */
public abstract class SuperFormController {

    @FXML
    private Label lblTimeDate;

    @FXML
    private Label lblHi;

    @FXML
    private Label lblUserFullName;

    @FXML
    public abstract void searchBarWithTable(KeyEvent event) throws ClassNotFoundException, SQLException;

    @FXML
    public abstract void editButtonAction(ActionEvent event);

    @FXML
    public abstract void deleteButtonAction(ActionEvent event) throws ClassNotFoundException, SQLException, IOException;

    public abstract void loadAllDataOnTable(String sql) throws ClassNotFoundException, SQLException;

    public abstract void customizeTable();

    public abstract void btnSaveUpdateAction(ActionEvent event) throws ClassNotFoundException, SQLException, IOException;

    public int modifyDataWithExecuteUpdate(String sql) throws ClassNotFoundException, SQLException {

        Connection connection;
        connection = DBConnection.getInstance().getConnection();
        PreparedStatement ptm = connection.prepareStatement(sql);
        int num = ptm.executeUpdate();

        return num;
    }

    @FXML
    public void goBackLogIn(ActionEvent event) throws IOException {
        System.gc();
        GoToAnotherWindow.getAnotherWindow().makeWindow(event, "/lk/ijse/bakeHouse/view/SecondLoginForm.fxml");
    }

    @FXML
    public void goBacKHome(ActionEvent event) throws IOException {
        System.gc();
        GoToAnotherWindow.getAnotherWindow().makeWindow(event, "/lk/ijse/bakeHouse/view/DashboardForm.fxml");
    }

    @FXML
    public void goBackCustomer(ActionEvent event) throws IOException {
        System.gc();
        GoToAnotherWindow.getAnotherWindow().makeWindow(event, "/lk/ijse/bakeHouse/view/CustomerForm.fxml");
    }

    @FXML
    public void goBackOrder(ActionEvent event) throws IOException {
        System.gc();
        GoToAnotherWindow.getAnotherWindow().makeWindow(event, "/lk/ijse/bakeHouse/view/OrderForm.fxml");
    }

    @FXML
    public void goBackPayment(ActionEvent event) throws IOException {
        System.gc();
        GoToAnotherWindow.getAnotherWindow().makeWindow(event, "/lk/ijse/bakeHouse/view/PaymentForm.fxml");
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

            lblTimeDate.setText(date2 + "  " + time2);
            //  lblDate.setText(new SimpleDateFormat("YYYY:MM:dd").format(new Date()));
        }), new KeyFrame(Duration.seconds(1)));
        time.setCycleCount(Animation.INDEFINITE);
        time.play();

    }

    public void showTitleUserInfo() {
        try {
            User user = LoginAndSignupFormController.getUserLoginInfo();
            String userName1 = user.getFirstName();
            String userName2 = user.getLastName();

            lblUserFullName.setText(userName1 + " " + userName2);

        } catch (Exception ex) {
        }
    }
     
    

}
