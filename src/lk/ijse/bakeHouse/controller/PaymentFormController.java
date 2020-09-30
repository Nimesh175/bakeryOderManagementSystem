/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.bakeHouse.controller;

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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import lk.ijse.bakeHouse.controller.superFormController.SuperFormController;
import lk.ijse.bakeHouse.custom.MyNotifycation;
import lk.ijse.bakeHouse.db.DBConnection;
import lk.ijse.bakeHouse.model.Customer;
import lk.ijse.bakeHouse.model.Order;
import lk.ijse.bakeHouse.model.OrderDetail;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author NIMESH
 */
public class PaymentFormController extends SuperFormController implements Initializable {

    @FXML
    private TableView<Customer> tblCustomer;

    @FXML
    private TableColumn<Customer, String> columnCustID;

    @FXML
    private TableColumn<Customer, String> columnCustName;

    @FXML
    private TableView<Order> tblOrders;

    @FXML
    private TableColumn<Order, String> columnOrdersUserName;

    @FXML
    private TableColumn<Order, String> columnOrdersOrderID;

    @FXML
    private TableColumn<Order, String> columnOrdersOrderDate;

    @FXML
    private TableColumn<Order, Double> columnOrdersPayment;

    @FXML
    private TableColumn<Order, String> columnOrdersStatus;

    @FXML
    private TableView<OrderDetail> tblOrderItems;

    @FXML
    private TableColumn<OrderDetail, String> columnItemName;

    @FXML
    private TableColumn<OrderDetail, Integer> columnItemQTY;

    @FXML
    private Label lblCustomerName;

    @FXML
    private Label lblOrderID;

    @FXML
    private CheckBox checkBoxPaid;

    @FXML
    private TextField txtLabelToDate;

    @FXML
    private TextField txtLabelFromDate;

    //customer table -->selected Row /to get Customer_ID
    private static String rowSelectCustomerID;

    //to add Customer object
    private static ObservableList<Customer> data;

    //to add order object
    private static ObservableList<Order> list;

    //to add orderDetail object
    private static ObservableList<OrderDetail> collection;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {

            customizeTable();

            loadAllCustomerOnTable("SELECT * FROM Customer;");
            tblCustomer.setItems(data);

            //Controll customer table Action -set customer id on order detail
            tblCustomer.setOnMouseClicked((MouseEvent e) -> {
                try {

                    getSelectRowOnCustomerTable();
                    touchOrderTableReActionOnCustomerTable();
                    setAscDescOrderDate(4);

                } catch (Exception ex) {
                    System.out.println("ex = " + ex);
                }
            });

            //Controll Order table Action 
            tblOrders.setOnMouseClicked((MouseEvent e) -> {
                try {

                    getSelectRowOnOrderTable();
                    touchOrderTableReActionOnCustomerTable();

                } catch (Exception ex) {
                    System.out.println("ex = " + ex);
                }
            });

            //TITLE BAR TIME DATE AND USER NAME
            showTitleDateTimeAndUserInfo();

        } catch (Exception ex) {
            System.out.println("ex = " + ex);
        }

    }

    @Override
    public void searchBarWithTable(KeyEvent event) throws ClassNotFoundException, SQLException {
        //
    }

    @Override
    public void editButtonAction(ActionEvent event) {
        //
    }

    @Override
    public void deleteButtonAction(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
        //
    }

    //set Order table refresh
    @Override
    public void loadAllDataOnTable(String sql) throws ClassNotFoundException, SQLException {

        list = FXCollections.observableArrayList();

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ptm = connection.prepareStatement(sql);
        ResultSet rs = ptm.executeQuery();
        while (rs.next()) {

            String orderID = rs.getString(1);
            String dateTime = rs.getString(2);
            String cusID = rs.getString(3);
            String userID = rs.getString(4);

            String paymentID = "DEFAULT";
            double totalPayment = 0;
            String status = "NO";

            PreparedStatement ptmm = connection.prepareStatement(""
                    + "SELECT * FROM PAYMENT "
                    + "WHERE orderID='" + orderID + "';");

            ResultSet rss = ptmm.executeQuery();

            if (rss.next()) {
                paymentID = rss.getString(2);
                totalPayment = rss.getDouble(5);
                status = rss.getString(6);
            }

            String userName = "DEFAULT";
            ptmm = connection.prepareStatement(""
                    + "SELECT fName "
                    + "FROM USER "
                    + "WHERE userID='" + userID + "';");

            rss = ptmm.executeQuery();
            if (rss.next()) {
                userName = rss.getString(1);
            }

            String cusName = "DEFAULT";
            ptmm = connection.prepareStatement(""
                    + "SELECT fName "
                    + "FROM CUSTOMER "
                    + "WHERE cusID='" + cusID + "';");

            rss = ptmm.executeQuery();
            if (rss.next()) {
                cusName = rss.getString(1);
            }

            Order order = new Order(orderID, dateTime, cusID, cusName, userID, userName, paymentID, totalPayment, status);
            list.add(order);

        }

        //( orderID,dateTime,cusID,cusName,userID,userName, paymentID,totalPayment,  status ) 
    }

    //set Customer table refresh
    @FXML
    private void loadAllCustomerOnTable(String sql) throws SQLException, ClassNotFoundException {
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
    public void customizeTable() {
        //columns add to table -Customer Table
        tblCustomer.getColumns().setAll(columnCustID, columnCustName);
        //set Order table
        tblOrders.getColumns().setAll(columnOrdersUserName, columnOrdersOrderID, columnOrdersOrderDate,
                columnOrdersPayment, columnOrdersStatus);
        //set Item table
        tblOrderItems.getColumns().setAll(columnItemName, columnItemQTY);

        tblCustomer.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("cusID"));
        tblCustomer.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("fName"));

        tblOrders.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("userName"));
        tblOrders.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("orderID"));
        tblOrders.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        tblOrders.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("totalPayment"));
        tblOrders.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("status"));

        tblOrderItems.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("proName"));
        tblOrderItems.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("qty"));

    }

    //set Payment Submit Action
    @Override
    public void btnSaveUpdateAction(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {

        Connection connection = DBConnection.getInstance().getConnection();

        String orderID = lblOrderID.getText();
        boolean checkXBox = checkBoxPaid.isSelected();
        String status = checkXBox == true ? "PAID" : "";

        if (orderID.equalsIgnoreCase("-") | lblCustomerName.getText().equalsIgnoreCase("-")) {

            TrayNotification tray = MyNotifycation.infoNotify("SELECT THE ORDER!");
            tray.showAndDismiss(Duration.seconds(2));

            return;
        }

        String sql = ""
                + "UPDATE PAYMENT "
                + "SET status='" + status + "' "
                + "WHERE orderID='" + orderID + "';";

        PreparedStatement ptm = connection.prepareStatement(sql);

        //conformation Box
        boolean result = MyNotifycation.askQuestion("Are Your sure?");
        if (result == false) {
            return;
        }

        if (ptm.executeUpdate() > 0) {

            TrayNotification tray = MyNotifycation.sucsessfullyNotify("PAYMENT STATUS UPDATE SUCSESSFULLY!");
            tray.showAndDismiss(Duration.seconds(2));

            String sql3 = ""
                    + "SELECT * FROM ORDERS "
                    + "WHERE cusID='" + rowSelectCustomerID + "';";

            loadAllDataOnTable(sql3);

            tblOrders.setItems(list);

            touchOrderTableReActionOnCustomerTable();

            lblOrderID.setText("-");
            lblCustomerName.setText("-");

            //LOAD ALL ORDER
            loadAllDataOnTable("SELECT * FROM ORDERS;");
            tblOrders.setItems(list);

        } else {

            TrayNotification tray = MyNotifycation.unSucsessfullyNotify("FAILED!");
            tray.showAndDismiss(Duration.seconds(2));

        }

    }

    private void getSelectRowOnCustomerTable() throws ClassNotFoundException, SQLException {

        Customer cus = tblCustomer.getSelectionModel().getSelectedItem();

        if (cus != null) {
            //set static String Variable
            rowSelectCustomerID = cus.getCusID();

            //SET LABEL
            lblCustomerName.setText(cus.getFName());

            //set All orders on the order table
            String sql = ""
                    + "SELECT * FROM ORDERS "
                    + "WHERE cusID='" + rowSelectCustomerID + "';";

            loadAllDataOnTable(sql);

            tblOrders.setItems(list);
            tblOrderItems.setItems(null);
        }

    }

    private void getSelectRowOnOrderTable() throws ClassNotFoundException, SQLException {
        Order order = tblOrders.getSelectionModel().getSelectedItem();

        if (order != null) {

            //set Taxt
            lblOrderID.setText(order.getOrderID());

            String sql = "SELECT P.NAME,O.QTY "
                    + "FROM order_product O "
                    + "LEFT JOIN available_item  A "
                    + "ON O.item_ID =A.item_ID "
                    + "LEFT JOIN PRODUCT P "
                    + "ON A.proID=P.proID  "
                    + "WHERE O.orderID='" + order.getOrderID() + "';";

            loadAllOrderDetailOnTable(sql);

            tblOrderItems.setItems(collection);

        } else {
            tblOrderItems.setItems(null);
        }
    }

    private void loadAllOrderDetailOnTable(String sql) throws SQLException, ClassNotFoundException {

        collection = FXCollections.observableArrayList();

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ptm = connection.prepareStatement(sql);
        ResultSet rs = ptm.executeQuery();

        while (rs.next()) {

            String proName = rs.getString(1);
            int qty = rs.getInt(2);

            //make orderDetail object -using database row details
            OrderDetail orderDetail = new OrderDetail(proName, qty);

            //add orderDetail object to orderDetail ObserverbleList
            collection.add(orderDetail);

        }

    }

    @FXML
    void deleteOrderButtonAction(ActionEvent event) throws ClassNotFoundException, SQLException {
        Order order = tblOrders.getSelectionModel().getSelectedItem();

        if (order != null) {

            String orderID = order.getOrderID();

            String sql = ""
                    + "DELETE FROM ORDERS "
                    + "WHERE orderID='" + orderID + "'; ";

            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement ptm = connection.prepareStatement(sql);

            //conformation Box
            boolean result = MyNotifycation.askWarning("Are Your sure?");
            if (result == false) {
                return;
            }

            if (ptm.executeUpdate() > 0) {

                TrayNotification tray = MyNotifycation.sucsessfullyNotify("ORDER IS DELETED.");
                tray.showAndDismiss(Duration.seconds(2));

                //refresh order table
                String sql2 = ""
                        + "SELECT * FROM ORDERS "
                        + "WHERE cusID='" + rowSelectCustomerID + "';";

                loadAllDataOnTable(sql2);

                tblOrders.setItems(list);
                tblOrderItems.setItems(null);
            }

        } else {

            TrayNotification tray = MyNotifycation.infoNotify("SELECT THE ORDER.");
            tray.showAndDismiss(Duration.seconds(2));

        }

    }

    @FXML
    void deletePaidAllOrderButtonAction(ActionEvent event) throws SQLException, ClassNotFoundException {

        try {

            String sql = ""
                    + "DELETE ORDERS ,PAYMENT "
                    + "FROM ORDERS "
                    + "LEFT JOIN PAYMENT "
                    + "ON ORDERS.orderID= PAYMENT.orderID "
                    + "WHERE status='PAID'; ";

            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement ptm = connection.prepareStatement(sql);

            //conformation Box
            boolean result = MyNotifycation.askWarning("Are Your sure?");
            if (result == false) {
                return;
            }

            if (ptm.executeUpdate() > 0) {

                TrayNotification tray = MyNotifycation.sucsessfullyNotify("ALL PAID ORDERS ARE DELETED.");
                tray.showAndDismiss(Duration.seconds(2));

                //refresh order table
                String sql2 = ""
                        + "SELECT * FROM ORDERS "
                        + "WHERE cusID='" + rowSelectCustomerID + "';";

                loadAllDataOnTable(sql2);

                tblOrders.setItems(list);
                tblOrderItems.setItems(null);

            }

        } catch (Exception ex) {
            TrayNotification tray = MyNotifycation.sucsessfullyNotify("FAILED.");
            tray.showAndDismiss(Duration.seconds(2));

        }

    }

    @FXML
    void buttonSearchAllAction(ActionEvent event) {
        try {

            loadAllDataOnTable("SELECT * FROM ORDERS;");
            tblOrders.setItems(list);
            setAscDescOrderDate(1);

        } catch (Exception ex) {
            System.out.println("ex = " + ex);
        }

    }

    @FXML
    void buttonSearchPaidAllAction(ActionEvent event) {

        try {

            loadAllDataOnTable("SELECT * FROM ORDERS O "
                    + "LEFT JOIN PAYMENT P "
                    + "ON O.orderID=P.orderID "
                    + "WHERE P.status='PAID';");

            tblOrders.setItems(list);
            setAscDescOrderDate(2);

        } catch (Exception ex) {
            System.out.println("ex = " + ex);
        }

    }

    @FXML
    void buttonSearchUnPaidAllAction(ActionEvent event) {

        try {

            loadAllDataOnTable("SELECT * FROM ORDERS O "
                    + "LEFT JOIN PAYMENT P "
                    + "ON O.orderID=P.orderID "
                    + "WHERE P.status != 'PAID';");

            tblOrders.setItems(list);
            setAscDescOrderDate(3);

        } catch (Exception ex) {
            System.out.println("ex = " + ex);
        }

    }

    private void touchOrderTableReActionOnCustomerTable() {
        Order order = tblOrders.getSelectionModel().getSelectedItem();

        if (order != null) {

            try {
                loadAllCustomerOnTable("SELECT * FROM CUSTOMER  WHERE cusID='" + order.getCusID() + "';");
                tblCustomer.setItems(data);

                lblCustomerName.setText(order.getCusID());

            } catch (Exception ex) {
                System.out.println("ex = " + ex);
            }

        }

    }

    @FXML
    void refreshCustomerTableAction(ActionEvent event) {

        try {

            loadAllCustomerOnTable("SELECT * FROM Customer;");
            tblCustomer.setItems(data);

            tblOrders.setItems(null);
            tblOrderItems.setItems(null);

            lblCustomerName.setText("-");
            lblOrderID.setText("-");
            checkBoxPaid.setSelected(false);
            txtLabelFromDate.setText("-");
            txtLabelToDate.setText("-");

        } catch (Exception ex) {
            System.out.println("ex = " + ex);
        }

    }

    private void setAscDescOrderDate(int x) throws ClassNotFoundException, SQLException {

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement asc = null;
        PreparedStatement desc = null;
        switch (x) {
            case 1://ALL
                asc = connection.prepareStatement(""
                        + "SELECT orderDate "
                        + "FROM orders "
                        + "ORDER BY orderDate "
                        + "ASC LIMIT 1;");

                desc = connection.prepareStatement(""
                        + "SELECT orderDate "
                        + "FROM orders "
                        + "ORDER BY orderDate "
                        + "DESC LIMIT 1;");

                break;

            case 2: //PAID   

                asc = connection.prepareStatement("SELECT O.orderDate "
                        + "FROM ORDERS O "
                        + "INNER JOIN PAYMENT P "
                        + "ON O.orderID=P.orderID "
                        + "WHERE P.status='PAID' "
                        + "ORDER BY O.orderDate "
                        + "ASC LIMIT 1;");

                desc = connection.prepareStatement("SELECT O.orderDate "
                        + "FROM ORDERS O "
                        + "INNER JOIN PAYMENT P "
                        + "ON O.orderID=P.orderID "
                        + "WHERE P.status='PAID' "
                        + "ORDER BY O.orderDate "
                        + "DESC LIMIT 1;");

                break;

            case 3:    //NOT PAID

                asc = connection.prepareStatement("SELECT O.orderDate "
                        + "FROM ORDERS O "
                        + "INNER JOIN PAYMENT P "
                        + "ON O.orderID=P.orderID "
                        + "WHERE P.status !='PAID' "
                        + "ORDER BY O.orderDate "
                        + "ASC LIMIT 1;");

                desc = connection.prepareStatement("SELECT O.orderDate "
                        + "FROM ORDERS O "
                        + "INNER JOIN PAYMENT P "
                        + "ON O.orderID=P.orderID "
                        + "WHERE P.status !='PAID' "
                        + "ORDER BY O.orderDate "
                        + "DESC LIMIT 1;");

                break;

            case 4:    //WITH CUSTOMER ID
                Customer cus = tblCustomer.getSelectionModel().getSelectedItem();
                if (cus != null) {
                    asc = connection.prepareStatement("SELECT O.orderDate "
                            + "FROM ORDERS O "
                            + "INNER JOIN PAYMENT P "
                            + "ON O.orderID=P.orderID "
                            + "WHERE O.cusID='" + cus.getCusID() + "' "
                            + "ORDER BY O.orderDate "
                            + "ASC LIMIT 1;");

                    desc = connection.prepareStatement("SELECT O.orderDate "
                            + "FROM ORDERS O "
                            + "INNER JOIN PAYMENT P "
                            + "ON O.orderID=P.orderID "
                            + "WHERE O.cusID='" + cus.getCusID() + "' "
                            + "ORDER BY O.orderDate "
                            + "DESC LIMIT 1;");

                }

                break;

        }

        String from = "DEFAULT";
        String to = "DEFAULT";

        //2ND FROM-DATE
        ResultSet rs = asc.executeQuery();
        if (rs.next()) {
            from = rs.getString(1);
        }

        //2ND TO-DATE
        rs = desc.executeQuery();
        if (rs.next()) {
            to = rs.getString(1);
        }

        txtLabelFromDate.setText(from);
        txtLabelToDate.setText(to);
    }

}
