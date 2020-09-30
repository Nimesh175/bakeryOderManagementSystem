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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import lk.ijse.bakeHouse.db.DBConnection;
import lk.ijse.bakeHouse.model.Customer;
import lk.ijse.bakeHouse.model.OrderDetail;
import lk.ijse.bakeHouse.model.Product;
import lk.ijse.bakeHouse.model.User;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author NIMESH
 */
public class OrderFormController extends SuperFormController implements Initializable {

    //product table
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
    private TableColumn<Product, String> columnAction;
//customer table
    @FXML
    private TableView<Customer> tableViewCustomer;

    @FXML
    private TableColumn<Customer, String> columnCustomerID;

    @FXML
    private TableColumn<Customer, String> columnCustomerName;

// cart tableview
    @FXML
    private TableView<OrderDetail> tableViewOrderDetail;

    @FXML
    private TableColumn<OrderDetail, String> coloumnOrderDetailName;

    @FXML
    private TableColumn<OrderDetail, Double> coloumnOrderDetailNetPrice;

    @FXML
    private TableColumn<OrderDetail, Double> coloumnOrderDetailDiscount;

    @FXML
    private TableColumn<OrderDetail, Integer> coloumnOrderDetailQTY;

    @FXML
    private TableColumn<OrderDetail, Double> coloumnOrderDetailPrice;

    @FXML
    private TableColumn<OrderDetail, String> coloumnOrderDetailAction;

    @FXML
    private Label lblProductID;

    @FXML
    private Label lblProductName;

    @FXML
    private Label lblProductPrice;

    @FXML
    private Label lbllDateNow;

    @FXML
    private Label lbllTimeNow;

    @FXML
    private Label lblOrderID;

    @FXML
    private Label lblCustomerID;

    @FXML
    private Label lblCustomerName;

    @FXML
    private Label lblRemainingItems;

    @FXML
    private JFXTextField txtCustomerIDSearch;

    @FXML
    private JFXTextField txtProductTypeSearch;

    @FXML
    private Label lblTotalPrice;

    @FXML
    private JFXTextField txtCartQTY;

    @FXML
    private JFXTextField txtCartDiscount;

    @FXML
    private JFXButton btnConfirmOrder;

    @FXML
    private JFXButton btnCancelOrder;

    //to add Customer object
    public static ObservableList<Customer> data;
    //to add Customer object
    public static ObservableList<Product> list;
    //search product
    public static ObservableList<Product> allProduct;

    public static ObservableList<OrderDetail> cartList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            customizeTable();

            loadAllDataOnTable("SELECT * FROM Customer;");
            tableViewCustomer.setItems(data);

            loadAllProductData(loadAllProductDataQuery());
            tableViewProduct.setItems(list);

            //set customer id on order detail
            tableViewCustomer.setOnMouseClicked(e -> getSelectRowOnCustomerTable());

            showDate();
            setTime();

            //GENERATE ID
            lblOrderID.setText(IDGeneratorConnection.getNewID("orders", "orderID", "OR"));

            btnCancelOrder.setDisable(true);

            //TITLE BAR TIME DATE AND USER NAME
            showTitleDateTimeAndUserInfo();

        } catch (Exception ex) {
            System.out.println("ex = " + ex);
        }

    }

    @FXML
    void confirmToPlaceTheOrder(ActionEvent event) throws IOException {
        try {
            if (lblCustomerID.getText().equalsIgnoreCase("-")) {
                return;
            }

            String orderID = lblOrderID.getText();
            String cusID = lblCustomerID.getText();

            User user = LoginAndSignupFormController.getUserLoginInfo();
            String userID = user.getUserID();

            Connection connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            // java.sql.Date date = getCurrentDatetime();
            String dateTime = lbllDateNow.getText() + " " + lbllTimeNow.getText();

            PreparedStatement stm = connection.prepareStatement(""
                    + "INSERT INTO orders "
                    + "VALUES(?,?,?,?);");

            stm.setString(1, orderID);
            stm.setString(2, dateTime);
            stm.setString(3, cusID);
            stm.setString(4, userID);

            //conformation Box
            boolean result = MyNotifycation.askQuestion("Are Your Sure?");
            if (result == false) {
                return;
            }

            if (stm.executeUpdate() > 0) {
                connection.commit();

                //btnConfrim disable
                btnConfirmOrder.setDisable(true);
                //customer table disable
                tableViewCustomer.setDisable(true);
                btnCancelOrder.setDisable(false);

                btnConfirmOrder.setText("DONE");

            } else {
                connection.rollback();
                tableViewCustomer.setDisable(false);
            }

        } catch (Exception ex) {
            System.out.println("ex = " + ex);
        }

    }

    public java.sql.Date getCurrentDatetime() {

        java.util.Date today = new java.util.Date();
        return new java.sql.Date(today.getTime());
    }

    String loadAllProductDataQuery() {
        return "SELECT P.proID ,P.NAME ,P.type ,P.unit_price ,P.weight ,P. weight_unit "
                + "FROM available_item A "
                + "LEFT JOIN PRODUCT P  "
                + "ON A.proID=P.proID;";
    }

    void showDate() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        lbllDateNow.setText(dateFormat.format(date));
    }

    private void setTime() {
        Timeline time = new Timeline(new KeyFrame(Duration.seconds(0), (ActionEvent event) -> {
            lbllTimeNow.setText(new SimpleDateFormat("hh:mm:ss a").format(new Date()));
            //  lblDate.setText(new SimpleDateFormat("YYYY:MM:dd").format(new Date()));
        }), new KeyFrame(Duration.seconds(1)));
        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }

    void getSelectRowOnCustomerTable() {
        Customer cus = tableViewCustomer.getSelectionModel().getSelectedItem();

        if (cus != null) {
            lblCustomerID.setText(cus.getCusID());
            lblCustomerName.setText(cus.getFName());
        }
    }

    @Override
    public void searchBarWithTable(KeyEvent event) throws ClassNotFoundException, SQLException {
        String productType = txtProductTypeSearch.getText();
        loadAllProductData(""
                + "SELECT * FROM Product "
                + "WHERE type "
                + "LIKE '%" + productType + "%';");

        tableViewProduct.setItems(list);

    }

    @Override
    public void editButtonAction(ActionEvent event) {
        //
    }

    @Override
    public void deleteButtonAction(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {

        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(true);

        String orderID = lblOrderID.getText();
//            

        PreparedStatement pstm = connection.prepareStatement(""
                + "DELETE FROM ORDERS "
                + "WHERE orderID='" + orderID + "';");

        //conformation Box
        boolean result = MyNotifycation.askWarning("Are Your Sure?");
        if (result == false) {
            return;
        }

        if (pstm.executeUpdate() > 0) {

            btnConfirmOrder.setDisable(false);
            btnConfirmOrder.setText("Confirm Details");
            tableViewCustomer.setDisable(false);
            //refresh table
            cartList.clear();
            tableViewOrderDetail.setItems(cartList);

            TrayNotification tray = MyNotifycation.infoNotify("ROLLBACK THE ORDER!");
            tray.showAndDismiss(Duration.seconds(2));

            //RESET TOTAL LABEL
            lblTotalPrice.setText(Double.toString(0.0));

        } else {
            System.out.println("Failed order cancel");

            btnConfirmOrder.setDisable(false);
            tableViewCustomer.setDisable(false);
        }

        lblCustomerID.setText("-");
        lblCustomerName.setText("-");
        btnCancelOrder.setDisable(true);
        lblOrderID.setText(IDGeneratorConnection.getNewID("orders", "orderID", "OR"));

    }

    //load customer all
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
    public void customizeTable() {
        //columns add to table -Customer Table
        tableViewCustomer.getColumns().setAll(columnCustomerID, columnCustomerName);
        //columns add to table -Product Table
        tableViewProduct.getColumns().setAll(columnProductID, coloumnName, coloumnType, columnPrice, columnAction);
        //columns add to table-cart table
        tableViewOrderDetail.getColumns().setAll(coloumnOrderDetailName, coloumnOrderDetailNetPrice,
                coloumnOrderDetailDiscount, coloumnOrderDetailQTY, coloumnOrderDetailPrice, coloumnOrderDetailAction);

        tableViewCustomer.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("cusID"));
        tableViewCustomer.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("fName"));

        tableViewProduct.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        tableViewProduct.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tableViewProduct.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("type"));
        tableViewProduct.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tableViewProduct.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("button"));

        tableViewOrderDetail.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("proName"));
        tableViewOrderDetail.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("netPrice"));
        tableViewOrderDetail.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("discount"));
        tableViewOrderDetail.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tableViewOrderDetail.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("lastPrice"));
        tableViewOrderDetail.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("button"));

    }

    @Override
    public void btnSaveUpdateAction(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {

        if (btnConfirmOrder.getText().equalsIgnoreCase("Confirm Details")) {
            return;
        }
        if (cartList.isEmpty()) {
            //rollback the order
            deleteButtonAction(event);
            return;
        }

        btnConfirmOrder.setDisable(false);
        btnConfirmOrder.setText("Confirm Details");
        tableViewCustomer.setDisable(false);
        //refresh table
        cartList.clear();
        tableViewOrderDetail.setItems(cartList);

        lblCustomerID.setText("-");
        lblCustomerName.setText("-");
        btnCancelOrder.setDisable(true);
        lblOrderID.setText(IDGeneratorConnection.getNewID("orders", "orderID", "OR"));

        lblProductID.setText("-");
        lblProductName.setText("-");
        lblProductPrice.setText("-");
        lblRemainingItems.setText("-");

        TrayNotification tray = MyNotifycation.sucsessfullyNotify("ORDER IS COMPLETED SUCSESSFULLY!");
        tray.showAndDismiss(Duration.seconds(2));

        cartList.clear();
        //RESET TOTAL LABEL
        lblTotalPrice.setText(Double.toString(0.0));
    }

    //product table load
    public void loadAllProductData(String sql) throws SQLException, ClassNotFoundException {

        list = FXCollections.observableArrayList();

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ptm = connection.prepareStatement(sql);
        ResultSet rs = ptm.executeQuery();

        while (rs.next()) {
            String id = rs.getString(1);//product id
            String name = rs.getString(2);
            String type = rs.getString(3);
            double unitPrice = rs.getDouble(4);
            double weight = rs.getDouble(5);
            String weightUnit = rs.getString(6);

            //create Button for taBle row
            Button btn = new Button("PURCHASE");
            btn.setStyle("-fx-background-color: #ffff4d;");
            btn.setOnAction(new EventHandler() {
                @Override
                public void handle(Event event) {
                    try {
                        lblProductID.setText(id);
                        lblProductName.setText(name);
                        lblProductPrice.setText(Double.toString(unitPrice));

                        PreparedStatement ptm = connection.prepareStatement(""
                                + "SELECT stock FROM available_item "
                                + "WHERE proID='" + id + "';");

                        ResultSet rs = ptm.executeQuery();
                        rs.next();
                        //set remaining product Items
                        lblRemainingItems.setText(Integer.toString(rs.getInt(1)));

                    } catch (SQLException ex) {
                        System.out.println("ex = " + ex);
                    }

                }
            });

            //make product object -using database row details
            Product product = new Product(id, name, type, unitPrice, weight, weightUnit, btn);

            //add product object to customer ObserverbleList
            list.add(product);

        }

    }

    @FXML
    void AddToCartAction(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {

        //STEP 01
        if (btnConfirmOrder.getText().equalsIgnoreCase("Confirm Details")) {
            TrayNotification tray = MyNotifycation.infoNotify("CONFIRM THE ORDER DETAILS!");
            tray.showAndDismiss(Duration.seconds(0));
            return;
        }

        if (txtCartQTY.getText().equalsIgnoreCase("") | txtCartQTY.getText().equalsIgnoreCase("0")) {

            TrayNotification tray2 = MyNotifycation.infoNotify("ADD QUANTITY!");
            tray2.showAndDismiss(Duration.seconds(0));

            return;

        }
        //STEP 02

        if (lblProductID.getText().equalsIgnoreCase("-") || lblCustomerID.getText().equalsIgnoreCase("-")) {
            TrayNotification tray = MyNotifycation.infoNotify("SELECT THE CUSTOMER AND PRODUCT!");
            tray.showAndDismiss(Duration.seconds(0));

            return;
        }

        //get jdbc connection
        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(true);

        double discount = 0;
        if (!(txtCartDiscount.getText().equalsIgnoreCase(""))) {
            discount = Double.parseDouble(txtCartDiscount.getText());
        } else {
            discount = 0;
        }

        String orderID = lblOrderID.getText();
        String proID = lblProductID.getText();
        String proName = lblProductName.getText();
        int qty = Integer.parseInt(txtCartQTY.getText());
        double netPrice = qty * Double.parseDouble(lblProductPrice.getText());
        double lastPrice = netPrice - discount;

        PreparedStatement pstm = connection.prepareStatement(""
                + "SELECT item_ID FROM available_item "
                + "WHERE proID='" + proID + "';");

        //get itemCode
        String itemID = "";
        ResultSet rst = pstm.executeQuery();
        if (rst.next()) {
            itemID = rst.getString(1);
        }

        //STEP 03
        if (getStockCount(itemID) < qty) {
            //out of Stock
            TrayNotification tray = MyNotifycation.warningNotify("ITEM OUT OF STOCK.");
            tray.showAndDismiss(Duration.seconds(2));
            return;
        }

//BUTTON DELETE
        Button button = new Button("DELETE");
        button.setStyle("-fx-background-color: #ff9999;");

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                ObservableList<OrderDetail> single;
                single = tableViewOrderDetail.getSelectionModel().getSelectedItems();

                OrderDetail orderDetail = tableViewOrderDetail.getSelectionModel().getSelectedItem();

                String order_ID;
                String item_ID;
                String pro_Name;
                int qty_select;
                double discount_select;
                double netPrice_select;
                double lastPrice_select;

                //Select the row              
                if (!single.isEmpty()) {

                    try {

                        order_ID = orderDetail.getOrderID();
                        item_ID = orderDetail.getItemID();
                        pro_Name = orderDetail.getProName();
                        qty_select = orderDetail.getQty();
                        discount_select = orderDetail.getDiscount();
                        netPrice_select = orderDetail.getNetPrice();
                        lastPrice_select = orderDetail.getLastPrice();

                        double discount = 0;
                        if (!(txtCartDiscount.getText().equalsIgnoreCase(""))) {
                            discount = Double.parseDouble(txtCartDiscount.getText());
                        } else {
                            discount = 0;
                        }

                        double net = 0;
                        double dis = 0;
                        double tot = 0;
                        double disBalance = 0;
                        //get payment status
                        PreparedStatement pre = connection.prepareStatement(""
                                + "SELECT net_balance,discount ,total  "
                                + "FROM PAYMENT "
                                + "WHERE orderID='" + order_ID + "';");

                        ResultSet set = pre.executeQuery();

                        if (set.next()) {

                            net = set.getDouble(1);
                            dis = set.getDouble(2);
                            tot = set.getDouble(3);

                        }
                        //new payment after delete product
                        double netBalance = (net - netPrice_select);
                        double lastBalance = (tot - lastPrice_select);
                        if (discount != 0) {
                            disBalance = (dis - discount_select);
                        }

                        Connection connection = DBConnection.getInstance().getConnection();
                        connection.setAutoCommit(false);

                        PreparedStatement stm = connection.prepareStatement(""
                                + "UPDATE PAYMENT SET net_balance="
                                + "" + netBalance + " , discount= "
                                + "" + disBalance + " , total=" + lastBalance + ""
                                + " WHERE orderID='" + order_ID + "'; ");

                        int x = stm.executeUpdate();

                        //step 01 - MANAGE OR DELETE PURCHESE ITEM
                        int qtyCount = 0;
                        //find qty 
                        PreparedStatement find = connection.prepareStatement(""
                                + "SELECT QTY FROM order_product "
                                + "WHERE orderID='" + order_ID + "'  ");

                        ResultSet findSet = find.executeQuery();
                        if (findSet.next()) {

                            qtyCount = findSet.getInt(1);

                        }

                        //step 02  
                        int y = 0;
                        PreparedStatement ptm = null;

                        if (qtyCount > 0) {

                            ptm = connection.prepareStatement(""
                                    + "UPDATE order_product "
                                    + "SET qty ='" + qty_select + "' "
                                    + "WHERE item_ID='" + item_ID + "'; ");

                            //STEP 03           
                        } else {

                            ptm = connection.prepareStatement(""
                                    + "DELETE FROM order_product"
                                    + " WHERE"
                                    + " item_ID='" + item_ID + "'; ");

                        }

                        y = ptm.executeUpdate();

                        //RE-ADD ITEMS TO REMAINING AVAILABLE ITEMS
                        PreparedStatement availableStock = connection.prepareStatement(""
                                + "SELECT stock "
                                + "FROM available_item "
                                + "WHERE item_ID='" + item_ID + "'");

                        ResultSet available = availableStock.executeQuery();
                        int stock_available = 0;
                        if (available.next()) {
                            stock_available = available.getInt(1);
                        }
                        //NEW STOCK
                        int new_Stock = stock_available + qty_select;
                        System.out.println(new_Stock);
                        System.out.println("itm id - " + item_ID);
                        PreparedStatement reTest = connection.prepareStatement(""
                                + "UPDATE available_item "
                                + "SET stock=" + new_Stock + " "
                                + "WHERE item_ID='" + item_ID + "' ;");

                        int z = reTest.executeUpdate();

                        if ((x + y + z) > 2) {

                            connection.commit();

                            //remove rows
                            single.forEach((ob) -> {
                                cartList.remove(ob);
                            });

                            TrayNotification tray = MyNotifycation.sucsessfullyNotify("ROW DELETED!.");
                            tray.showAndDismiss(Duration.seconds(2));

                            //FIND TOTAL              
                            PreparedStatement total = connection.prepareStatement(""
                                    + "SELECT total FROM payment"
                                    + " WHERE  orderID='" + order_ID + "' ; ");

                            ResultSet set2 = total.executeQuery();

                            double value = 0;

                            if (set2.next()) {
                                value = set2.getDouble(1);
                            }
                            //Set Labei TOTAL VALUE & REMAINING STOCK
                            lblTotalPrice.setText(Double.toString(value));
                            lblRemainingItems.setText(Integer.toString(new_Stock));

                        } else {

                            connection.rollback();
                        }

                    } catch (Exception ex) {
                        System.out.println("EXCEPTION ERROR!");
                        System.out.println(ex);
                    }

                }//close if
            }
        });

        connection.setAutoCommit(false);
        int finalStock = getStockCount(itemID) - qty;
        PreparedStatement update = connection.prepareStatement(""
                + "UPDATE available_item SET stock='"
                + finalStock + "' WHERE item_ID= '" + itemID + "';");

        int x = update.executeUpdate();

        //cheak order List  has the same item & update or insert      
        int isSameItem = sameItemAddCheacking(itemID, orderID);
        int y = 0;
        int stockAvailable = isSameItem + qty;

        if (isSameItem > 0) {

            PreparedStatement mod = connection.prepareStatement(""
                    + "UPDATE order_product SET qty='" + stockAvailable
                    + "' WHERE item_ID= '" + itemID + "';");

            y = mod.executeUpdate();

        } else {

            PreparedStatement stm = connection.prepareStatement(""
                    + "INSERT INTO order_product "
                    + "VALUES(?,?,?);");

            stm.setString(1, orderID);
            stm.setString(2, itemID);
            stm.setInt(3, qty);

            y = stm.executeUpdate();

        }

        double z = 0;

        if (hasOrNotPaymentForOder(orderID)) {

            double net = 0;
            double disc = 0;
            double tot = 0;
            PreparedStatement get = connection.prepareStatement(""
                    + "SELECT * FROM payment "
                    + "WHERE orderID='" + orderID + "' ;");

            ResultSet result = get.executeQuery();

            if (result.next()) {
                net = result.getDouble(3);
                disc = result.getDouble(4);
                tot = result.getDouble(5);

            }

            //
            PreparedStatement up = connection.prepareStatement(""
                    + "UPDATE PAYMENT "
                    + "SET net_balance=" + (net + netPrice) + " ,  discount="
                    + (disc + discount) + " ,total=" + (tot + lastPrice) + " "
                    + "WHERE orderID= '" + orderID + "' ;");

            z = up.executeUpdate();

        } else {
            //place the payment  
            String payment_ID = IDGeneratorConnection.getNewID("payment", "payment_ID", "PID");

            PreparedStatement stm = connection.prepareStatement(""
                    + "INSERT INTO payment "
                    + "VALUES(?,?,?,?,?,?);");

            stm.setString(1, orderID);
            stm.setString(2, payment_ID);
            stm.setDouble(3, netPrice);
            stm.setDouble(4, discount);
            stm.setDouble(5, lastPrice);
            stm.setString(6, "");

            z = stm.executeUpdate();

        }

        //
        if ((x + y + z) > 2) {
            connection.commit();

            TrayNotification tray = MyNotifycation.sucsessfullyNotify("ITEM ADDED!.");
            tray.showAndDismiss(Duration.seconds(2));

            //set current stock
            lblRemainingItems.setText(Integer.toString(getStockCount(itemID)));

            OrderDetail orderDetail = new OrderDetail(orderID, itemID, proName, qty, discount, netPrice, lastPrice, button);
            //add orderDetail object to customer ObserverbleList

            //set the TOTAL labet
            PreparedStatement total = connection.prepareStatement(""
                    + "SELECT total "
                    + "FROM payment "
                    + "WHERE  orderID='" + orderID + "' ; ");

            ResultSet set = total.executeQuery();
            double value = 0;
            if (set.next()) {
                value = set.getDouble(1);
            }

            lblTotalPrice.setText(Double.toString(value));

            //add to table
            cartList.add(orderDetail);
            tableViewOrderDetail.setItems(cartList);

        } else {

            connection.rollback();
            TrayNotification tray = MyNotifycation.unSucsessfullyNotify("FAILED!");
            tray.showAndDismiss(Duration.seconds(2));
        }
        //
        txtCartQTY.clear();
        txtCartDiscount.clear();

    }//close

    int getStockCount(String itemID) throws ClassNotFoundException, SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement stmm = connection.prepareStatement(""
                + "SELECT stock "
                + "FROM available_item "
                + "WHERE item_ID='" + itemID + "';");

        ResultSet rst = stmm.executeQuery();
        rst.next();

        return rst.getInt(1);

    }

    int sameItemAddCheacking(String itemCode, String orderID) throws ClassNotFoundException, SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(""
                + "SELECT * FROM order_product "
                + "where orderID='" + orderID + "' ;");

        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {

            if (rs.getString(2).equalsIgnoreCase(itemCode)) {
                return rs.getInt(3);
            }

        }

        return 0;

    }

    boolean hasOrNotPaymentForOder(String orderID) throws SQLException {

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("SELECT orderID FROM payment;");
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {

                if (rs.getString(1).equalsIgnoreCase(orderID)) {
                    return true;
                }

            }

        } catch (ClassNotFoundException ex) {
            System.out.println("ex = " + ex);
        }

        return false;

    }

    public void searchProductUsingType(String keyword) throws ClassNotFoundException, SQLException {
        allProduct = FXCollections.observableArrayList();

        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ptm = connection.prepareStatement(keyword);
        ResultSet rs = ptm.executeQuery();

        while (rs.next()) {
            String id = rs.getString(1);
            String name = rs.getString(2);
            String type = rs.getString(3);
            double unitPrice = rs.getDouble(4);
            double weight = rs.getDouble(5);
            String weightUnit = rs.getString(6);

            //create Button for taBle row
            Button btn = new Button("PURCHASE");
            btn.setStyle("-fx-background-color: #ffff4d;");
            btn.setOnAction(new EventHandler() {
                @Override
                public void handle(Event event) {
                    try {
                        lblProductID.setText(id);
                        lblProductName.setText(name);
                        lblProductPrice.setText(Double.toString(unitPrice));

                        PreparedStatement ptm = connection.prepareStatement(""
                                + "SELECT stock FROM available_item "
                                + "WHERE proID='" + id + "';");

                        ResultSet rs = ptm.executeQuery();
                        rs.next();
                        //set remaining product Items
                        lblRemainingItems.setText(Integer.toString(rs.getInt(1)));

                    } catch (SQLException ex) {
                        System.out.println("ex = " + ex);
                    }

                }
            });

            //make product object -using database row details
            Product product = new Product(id, name, type, unitPrice, weight, weightUnit, btn);

            //add product object to customer ObserverbleList
            allProduct.add(product);

        }

    }

    @FXML
    void searchProductUsingType(KeyEvent event) throws ClassNotFoundException, SQLException {
        String keyword = txtProductTypeSearch.getText();
        searchProductUsingType("SELECT * FROM PRODUCT "
                + "WHERE type LIKE '%" + keyword + "%' OR "
                + "NAME LIKE '%" + keyword + "%';");

        tableViewProduct.setItems(allProduct);

    }

    @FXML
    void findCustomerByCustomerID(KeyEvent event) throws ClassNotFoundException, SQLException {

        String keyword = txtCustomerIDSearch.getText();
        loadAllDataOnTable("SELECT * FROM CUSTOMER "
                + "WHERE fName LIKE '%" + keyword + "%' "
                + "OR cusID LIKE '%" + keyword + "%' ;");

        tableViewCustomer.setItems(data);

    }
}
