/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ijse.bakeHouse.model;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;

/**
 *
 * @author NIMESH
 */
public class Product {
    
    private String id;
    private String name;
    private String type;
    private double unitPrice;
    private double weight;
    private String weightUnit;
    private Button button;
    private ObservableList<Recipe> recipeCollection;
    
    public Product() {}

    public Product(String id, String name, String type, double unitPrice, double weight, String weightUnit) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.unitPrice = unitPrice;
        this.weight = weight;
        this.weightUnit = weightUnit;
    }

    public Product(String id, String name, String type, double unitPrice, double weight, String weightUnit, Button button) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.unitPrice = unitPrice;
        this.weight = weight;
        this.weightUnit = weightUnit;
        this.button = button;
    }
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public ObservableList<Recipe> getRecipeCollection() {
        return recipeCollection;
    }

    public void setRecipeCollection(ObservableList<Recipe> recipeCollection) {
        this.recipeCollection = recipeCollection;
    }
    
    
    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", name=" + name + ", type=" + type 
                + ", unitPrice=" + unitPrice + ", weight=" + weight 
                + ", weightUnit=" + weightUnit + '}';
    }
    
}
