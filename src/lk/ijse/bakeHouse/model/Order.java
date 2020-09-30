/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.bakeHouse.model;

import javafx.scene.control.Button;

/**
 *
 * @author NIMESH
 */
public class Order {
    
    
    private String orderID;
    private String dateTime;
    private String cusID;
    private String cusName;
    private String userID;
    private String userName;
    private String paymentID;
    private double totalPayment;
    private String status;
    private Button button;
    
    
    
    public Order() {
    }
//for paymentForm  order
    public Order(String orderID, String dateTime, String cusID, String cusName, String userID, String userName, String paymentID,double totalPayment, String status) {
        this.orderID = orderID;
        this.dateTime = dateTime;
        this.cusID = cusID;
        this.cusName = cusName;
        this.userID = userID;
        this.userName = userName;
        this.paymentID = paymentID;
        this.status = status;
        this.totalPayment= totalPayment;
    }

    
    
    public Order(String orderID, String dateTime, String cusID, String cusName, String userID, String userName, String paymentID) {
        this.orderID = orderID;
        this.dateTime = dateTime;
        this.cusID = cusID;
        this.cusName = cusName;
        this.userID = userID;
        this.userName = userName;
        this.paymentID = paymentID;
    }

    public Order(String orderID, String dateTime, String cusID, String cusName, String userID, String userName, String paymentID, Button button) {
        this.orderID = orderID;
        this.dateTime = dateTime;
        this.cusID = cusID;
        this.cusName = cusName;
        this.userID = userID;
        this.userName = userName;
        this.paymentID = paymentID;
        this.button = button;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCusID() {
        return cusID;
    }

    public void setCusID(String cusID) {
        this.cusID = cusID;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }
    
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }
    
    @Override
    public String toString() {
        return "Order{" + "orderID=" + orderID + ", dateTime=" + dateTime + ", cusID=" + cusID + ", cusName=" + cusName + ", userID=" + userID + ", userName=" + userName + ", paymentID=" + paymentID + '}';
    }

    
    
    
    
    
}
