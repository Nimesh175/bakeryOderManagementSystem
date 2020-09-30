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
public class OrderDetail {
    
    private String orderID;
    private String itemID;
    private String proName;
    private int qty;
    private double discount;
    private double netPrice;
    private double lastPrice;
    private Button button;

    public OrderDetail() {
    }

    public OrderDetail(String proName, int qty) {
        this.proName = proName;
        this.qty = qty;
    }

  
    
    public OrderDetail(String orderID, String itemID, String proName, int qty, double discount, double netPrice, double lastPrice) {
        this.orderID = orderID;
        this.itemID = itemID;
        this.proName = proName;
        this.qty = qty;
        this.discount = discount;
        this.netPrice = netPrice;
        this.lastPrice = lastPrice;
    }

    public OrderDetail(String orderID, String itemID, String proName, int qty, double discount, double netPrice, double lastPrice, Button button) {
        this.orderID = orderID;
        this.itemID = itemID;
        this.proName = proName;
        this.qty = qty;
        this.discount = discount;
        this.netPrice = netPrice;
        this.lastPrice = lastPrice;
        this.button = button;
    }
    
    

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(double netPrice) {
        this.netPrice = netPrice;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    @Override
    public String toString() {
        return "OrderDetail{" + "orderID=" + orderID + ", itemID=" + itemID + ", proName=" 
                + proName + ", qty=" + qty + ", discount=" + discount + ", netPrice=" + netPrice 
                + ", lastPrice=" + lastPrice + '}';
    }
    
    
   
    
    
    
}
