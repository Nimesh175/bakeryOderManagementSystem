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
public class AvailaleProduct {
    
    private String itemId;
    private String proID;
    private int qty;
    private Button button;

    public AvailaleProduct() {
    }

    public AvailaleProduct(String itemId, String proID, int qty, Button button) {
        this.itemId = itemId;
        this.proID = proID;
        this.qty = qty;
        this.button = button;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getProID() {
        return proID;
    }

    public void setProID(String proID) {
        this.proID = proID;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    @Override
    public String toString() {
        return "OrderList{" + "itemId=" + itemId + ", proID=" + proID + ", qty=" + qty + ", button=" + button + '}';
    }
    
    
    
}
